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
  volume    = {
    name       = "empty-dir-volume"
    medium     = "MEMORY"
    size_limit = "128Mi"
    mount      = "/opt"
  }
  writes = {
    "${local.inherent}-n-${local.small}-size-${local.small}" : {
      connector = local.inherent,
      n         = local.small,
      size      = local.small,
    }
    "${local.inherent}-n-${local.small}-size-${local.medium}" : {
      connector = local.inherent,
      n         = local.small,
      size      = local.medium,
    }
    "${local.inherent}-n-${local.small}-size-${local.large}" : {
      connector = local.inherent,
      n         = local.small,
      size      = local.large,
    }
    "${local.inherent}-n-${local.medium}-size-${local.small}" : {
      connector = local.inherent,
      n         = local.medium,
      size      = local.small,
    }
    "${local.inherent}-n-${local.medium}-size-${local.medium}" : {
      connector = local.inherent,
      n         = local.medium,
      size      = local.medium,
    }
    "${local.inherent}-n-${local.medium}-size-${local.large}" : {
      connector = local.inherent,
      n         = local.medium,
      size      = local.large,
    }
    "${local.inherent}-n-${local.large}-size-${local.small}" : {
      connector = local.inherent,
      n         = local.large,
      size      = local.small,
    }
    "${local.inherent}-n-${local.large}-size-${local.medium}" : {
      connector = local.inherent,
      n         = local.large,
      size      = local.medium,
    }
    "${local.inherent}-n-${local.large}-size-${local.large}" : {
      connector = local.inherent,
      n         = local.large,
      size      = local.large,
    }

    "${local.rrio}-n-${local.small}-size-${local.small}" : {
      connector = local.rrio,
      n         = local.small,
      size      = local.small,
    }
    "${local.rrio}-n-${local.small}-size-${local.medium}" : {
      connector = local.rrio,
      n         = local.small,
      size      = local.medium,
    }
    "${local.rrio}-n-${local.small}-size-${local.large}" : {
      connector = local.rrio,
      n         = local.small,
      size      = local.large,
    }
    "${local.rrio}-n-${local.medium}-size-${local.small}" : {
      connector = local.rrio,
      n         = local.medium,
      size      = local.small,
    }
    "${local.rrio}-n-${local.medium}-size-${local.medium}" : {
      connector = local.rrio,
      n         = local.medium,
      size      = local.medium,
    }
    "${local.rrio}-n-${local.medium}-size-${local.large}" : {
      connector = local.rrio,
      n         = local.medium,
      size      = local.large,
    }
    "${local.rrio}-n-${local.large}-size-${local.small}" : {
      connector = local.rrio,
      n         = local.large,
      size      = local.small,
    }
    "${local.rrio}-n-${local.large}-size-${local.medium}" : {
      connector = local.rrio,
      n         = local.large,
      size      = local.medium,
    }
    "${local.rrio}-n-${local.large}-size-${local.large}" : {
      connector = local.rrio,
      n         = local.large,
      size      = local.large,
    }
  }

  reads = {
    "${local.inherent}-read" : {
      connector = local.inherent,
    }
    "${local.rrio}-read" : {
      connector = local.rrio,
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

data "google_compute_network" "default" {
  name = var.network_name_base
}

data "google_compute_subnetwork" "default" {
  name   = data.google_compute_network.default.name
  region = var.region
}

data "google_service_account" "worker" {
  account_id = var.dataflow_worker_service_account_id
}

data "google_storage_bucket" "default" {
  name = var.storage_bucket_name
}

resource "google_storage_bucket_object" "jar" {
  depends_on = [null_resource.shadowjar]
  bucket     = data.google_storage_bucket.default.name
  source     = "${local.beam_root}/${var.gradle_project_dir}/build/libs/${var.pipeline_jar_name}"
  name       = "${var.pipeline_image_prefix}/${var.pipeline_jar_name}"
}

resource "google_cloud_run_v2_job" "writes" {
  depends_on = [google_project_service.required]
  for_each   = local.writes
  location   = var.region
  name       = "rrio-bigtable-it-${lower(each.key)}"
  template {
    template {
      max_retries     = 1
      service_account = data.google_service_account.worker.email
      volumes {
        name = local.volume.name
      }
      containers {
        image   = local.gcloud
        command = ["bash"]
        args    = [
          "-c",
          <<EOF
gcloud storage cp gs://${google_storage_bucket_object.jar.bucket}/${google_storage_bucket_object.jar.name} ${local.volume.mount}/${basename(google_storage_bucket_object.jar.name)};
java -jar ${local.volume.mount}/${basename(google_storage_bucket_object.jar.name)} \
--runner=Dataflow \
--serviceAccount=${data.google_service_account.worker.email} \
--network=${data.google_compute_network.default.name} \
--region=${var.region} \
--subnetwork=regions/${data.google_compute_subnetwork.default.region}/subnetworks/${data.google_compute_subnetwork.default.name} \
--usePublicIps=false \
--connector=${each.value["connector"]} \
--project=${var.project} \
--elementSizePerImpulse=${each.value["n"]} \
--mutationSize=${each.value["size"]} \
--experiments=use_runner_v2 \
--readOrWrite=WRITE
EOF
        ]
      }
    }
  }
}

resource "google_cloud_run_v2_job" "read" {
  depends_on = [google_project_service.required]
  for_each   = local.reads
  location   = var.region
  name       = "rrio-bigtable-it-${lower(each.key)}"
  template {
    template {
      max_retries     = 1
      service_account = data.google_service_account.worker.email
      volumes {
        name = local.volume.name
      }
      containers {
        image   = local.gcloud
        command = ["bash"]
        args    = [
          "-c",
          <<EOF
gcloud storage cp gs://${google_storage_bucket_object.jar.bucket}/${google_storage_bucket_object.jar.name} ${local.volume.mount}/${basename(google_storage_bucket_object.jar.name)};
java -jar ${local.volume.mount}/${basename(google_storage_bucket_object.jar.name)} \
--runner=Dataflow \
--serviceAccount=${data.google_service_account.worker.email} \
--network=${data.google_compute_network.default.name} \
--region=${var.region} \
--subnetwork=regions/${data.google_compute_subnetwork.default.region}/subnetworks/${data.google_compute_subnetwork.default.name} \
--usePublicIps=false \
--connector=${each.value["connector"]} \
--project=${var.project} \
--experiments=use_runner_v2 \
--readOrWrite=READ \
--bigTableKeyModifier=$BIG_TABLE_KEY_MODIFIER
EOF
        ]
        env {
          name = "BIG_TABLE_KEY_MODIFIER"
        }
      }
    }
  }
}
