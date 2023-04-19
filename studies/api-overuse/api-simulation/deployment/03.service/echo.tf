resource "kubernetes_config_map" "echo" {
  metadata {
    name      = local.echo.service_name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
  data = {
    PORT       = var.service_port
    QUOTA_HOST = local.quota_host
  }
}

resource "kubernetes_deployment" "echo" {
  wait_for_rollout = false
  metadata {
    name      = local.echo.service_name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
  spec {
    selector {
      match_labels = {
        app = local.echo.service_name
      }
    }
    template {
      metadata {
        labels = {
          app = local.echo.service_name
        }
      }
      spec {
        container {
          name  = var.resource_name
          image = local.echo.image_url
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
    name      = local.echo.service_name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
  spec {
    selector = {
      app = local.echo.service_name
    }
    type = "NodePort"
    port {
      port        = 8080
      target_port = "8080"
    }
  }
}
