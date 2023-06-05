locals {
  cache_host = "${data.kubernetes_service.redis.metadata[0].name}.${data.kubernetes_service.redis.metadata[0].namespace}.svc.cluster.local:${data.kubernetes_service.redis.spec[0].port[0].port}"
}

data "kubernetes_namespace" "quota" {
  metadata {
    name = var.namespace
  }
}

data "kubernetes_service" "redis" {
  metadata {
    name      = var.redis_service_name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
}
