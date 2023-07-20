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

resource "kubernetes_service_account" "default" {
  metadata {
    namespace   = kubernetes_namespace.default.metadata[0].name
    name        = var.service_account_id
    annotations = {
      "iam.gke.io/gcp-service-account" : data.google_service_account.default.email
    }
  }
}

resource "google_service_account_iam_member" "workload_identity" {
  member             = "serviceAccount:${var.project}.svc.id.goog[${kubernetes_namespace.default.metadata[0].name}/${kubernetes_service_account.default.metadata[0].name}]"
  role               = "roles/iam.workloadIdentityUser"
  service_account_id = data.google_service_account.default.id
}
