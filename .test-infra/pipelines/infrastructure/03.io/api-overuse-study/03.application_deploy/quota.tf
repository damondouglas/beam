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
  quota_image     = "${var.image_repository}/${var.quota_service.image_id}:${var.image_tag}"
  refresher_image = "${var.image_repository}/${var.quota_service.refresher_image_id}:${var.image_tag}"
}

resource "kubernetes_config_map" "quota" {
  metadata {
    name      = var.quota_service.name
    namespace = data.kubernetes_namespace.default.metadata[0].name
  }
  data = {
    PORT            = tostring(var.quota_service.port)
    CACHE_HOST      = local.cache_host
    REFRESHER_IMAGE = local.refresher_image
    NAMESPACE       = data.kubernetes_namespace.default.metadata[0].name
  }
}

resource "kubernetes_deployment" "quota" {
  wait_for_rollout = false
  metadata {
    name      = var.quota_service.name
    namespace = data.kubernetes_namespace.default.metadata[0].name
  }
  spec {
    selector {
      match_labels = {
        app = var.quota_service.name
      }
    }
    template {
      metadata {
        labels = {
          app = var.quota_service.name
        }
      }
      spec {
        container {
          name              = var.quota_service.name
          image             = local.quota_image
          image_pull_policy = "IfNotPresent"
          env_from {
            config_map_ref {
              name = kubernetes_config_map.quota.metadata[0].name
            }
          }
        }
      }
    }
  }
}
