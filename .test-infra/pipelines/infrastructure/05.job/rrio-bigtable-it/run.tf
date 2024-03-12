/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

locals {
  beam_root = "${path.module}/../../../../.."
  inherent  = "INHERENT"
  rrio      = "RRIO"
  small     = "SMALL"
  medium    = "MEDIUM"
  large     = "LARGE"
  gcloud    = "gcr.io/google.com/cloudsdktool/google-cloud-cli:latest"
  java      = "gradle:jdk11-focal"
  volume    = {
    name       = "empty-dir-volume"
    medium     = "MEMORY"
    size_limit = "128Mi"
    mount      = "/opt"
  }
  experiments = {
    "${local.inherent}-n-${local.small}-size-${local.small}" : {
      connector = local.inherent,
      n         = local.small,
      size      = local.small,
    }
  }
}

resource "google_project_service" "required" {
  for_each = toset([
    "dataflow",
    "run",
  ])
  service            = "${each.key}.googleapis.com"
  disable_on_destroy = false
}

// Builds the Shadow jar via the gradle command.
resource "null_resource" "shadowjar" {
  triggers = {
    id = uuid()
  }
  provisioner "local-exec" {
    working_dir = local.beam_root
    command     = "./gradlew ${var.gradle_project}:shadowJar"
  }
}

data "google_service_account" "worker" {
  account_id = var.dataflow_worker_service_account_id
}

data "google_storage_bucket" "default" {
  name = var.storage_bucket_name
}

resource "google_storage_bucket_object" "jar" {
  bucket = data.google_storage_bucket.default.name
  source = "${local.beam_root}/${var.gradle_project_dir}/build/libs/${var.pipeline_jar_name}"
  name   = "${var.pipeline_image_prefix}/${var.pipeline_jar_name}"
}

resource "google_cloud_run_v2_job" "jobs" {
  depends_on = [google_project_service.required]
  for_each   = local.experiments
  location   = var.region
  name       = "rrio-bigtable-it-${lower(each.key)}"
  template {
    template {
      service_account = data.google_service_account.worker.email
      volumes {
        name = local.volume.name
      }
      containers {
        name    = "fetch-pipeline-jar"
        image   = local.gcloud
        command = ["gcloud"]
        args    = [
          "storage", "cp", "gs://${google_storage_bucket_object.jar.bucket}/${google_storage_bucket_object.jar.name}",
          "${local.volume.mount}/${google_storage_bucket_object.jar.name}"
        ]
        volume_mounts {
          mount_path = local.volume.mount
          name       = local.volume.name
        }
      }
      containers {
        name    = "run-pipeline"
        image   = local.java
        command = ["java"]
        args    = [
          "-jar",
          "${local.volume.mount}/${google_storage_bucket_object.jar.name}"
        ]
      }
    }
  }
}