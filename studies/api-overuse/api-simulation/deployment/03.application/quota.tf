locals {
  quota_image     = "${var.image_repository}/${var.quota_service.image_id}:${var.image_tag}"
  refresher_image = "${var.image_repository}/${var.quota_service.refresher_image_id}:${var.image_tag}"
}

resource "kubernetes_config_map" "quota" {
  metadata {
    name      = var.quota_service.name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
  data = {
    PORT            = tostring(var.quota_service.port)
    CACHE_HOST      = local.cache_host
    REFRESHER_IMAGE = local.refresher_image
    NAMESPACE       = data.kubernetes_namespace.quota.metadata[0].name
  }
}

resource "kubernetes_deployment" "quota" {
  metadata {
    name      = var.quota_service.name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
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

resource "kubernetes_service" "quota" {
  metadata {
    name      = var.quota_service.name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
  spec {
    selector = {
      app = var.quota_service.name
    }
    port {
      port        = tostring(var.quota_service.port)
      target_port = tostring(var.quota_service.port)
    }
  }
}