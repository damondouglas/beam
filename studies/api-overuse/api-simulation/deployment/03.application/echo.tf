locals {
  echo_image = "${var.image_repository}/${var.echo_service.image_id}:${var.image_tag}"
}

resource "kubernetes_config_map" "echo" {
  metadata {
    name      = var.echo_service.name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
  data = {
    PORT       = tostring(var.echo_service.port)
    CACHE_HOST = local.cache_host
  }
}

resource "kubernetes_deployment" "echo" {
  wait_for_rollout = false
  metadata {
    name      = var.echo_service.name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
  spec {
    selector {
      match_labels = {
        app = var.echo_service.name
      }
    }
    template {
      metadata {
        labels = {
          app = var.echo_service.name
        }
      }
      spec {
        container {
          name              = var.echo_service.name
          image             = local.echo_image
          image_pull_policy = "IfNotPresent"
          env_from {
            config_map_ref {
              name = kubernetes_config_map.echo.metadata[0].name
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "echo" {
  metadata {
    name      = var.echo_service.name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
  spec {
    selector = {
      app = var.echo_service.name
    }
    port {
      port        = tostring(var.echo_service.port)
      target_port = tostring(var.echo_service.port)
    }
  }
}
