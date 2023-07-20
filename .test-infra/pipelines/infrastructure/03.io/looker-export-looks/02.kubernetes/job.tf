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
  job_name = "export-looks"
  image    = "${data.google_artifact_registry_repository.default.location}-docker.pkg.dev/${var.project}/${data.google_artifact_registry_repository.default.repository_id}/${var.go_executable_path}"
}

// Provision a cron job to export Looker Looks.
// Note the terraform provider for cronjob did not work; even its included
// example. Therefore, this module uses kubernetes_manifest instead.
resource "kubernetes_manifest" "export_looks" {
  for_each = toset(var.look_resources)
  manifest = {
    apiVersion = "batch/v1"
    kind       = "CronJob"
    metadata   = {
      name      = "export-looks-${replace(each.key, "/", "-")}"
      namespace = kubernetes_namespace.default.metadata[0].name
      labels    = {
        look_result_bucket = var.storage_bucket
      }
    }
    spec = {
      schedule    = var.cron_job_frequency
      jobTemplate = {
        spec = {
          template = {
            spec = {
              serviceAccountName = kubernetes_service_account.default.metadata[0].name
              containers         = [
                {
                  name            = "export-looks"
                  image           = local.image
                  imagePullPolicy = "IfNotPresent"
                  args            = [
                    "looks",
                    "run",
                    "--looker_credentials_secret_path=${data.google_secret_manager_secret.default.id}/versions/latest",
                    "--look_result_bucket=${data.google_storage_bucket.default.name}",
                    "--look_result_output_pattern=${var.look_result_output_pattern}",
                    "--look_result_format=${var.look_result_format}",
                    each.key,
                  ]
                  resources = {
                    requests = {
                      cpu    = "150m"
                      memory = "150Mi"
                    }
                  }
                }
              ]
              restartPolicy = "Never"
            }
          }
        }
      }
    }
  }
}
