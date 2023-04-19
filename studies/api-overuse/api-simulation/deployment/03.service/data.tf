locals {
  image_base = "${data.google_artifact_registry_repository.default.location}-docker.pkg.dev/${var.project}/${data.google_artifact_registry_repository.default.repository_id}"
  echo       = {
    image_url    = "${local.image_base}/${var.echo_image_name_tag}"
    service_name = "echo"
  }
  quota_host = "${data.kubernetes_service.redis.metadata[0].name}.${data.kubernetes_service.redis.metadata[0].namespace}.svc.cluster.local:${data.kubernetes_service.redis.spec[0].port[0].port}"
}

data "google_artifact_registry_repository" "default" {
  location      = var.region
  repository_id = var.artifact_registry_id
}

data "kubernetes_namespace" "quota" {
  metadata {
    name = var.resource_name
  }
}

data "kubernetes_service" "redis" {
  metadata {
    name      = var.redis_service_name
    namespace = data.kubernetes_namespace.quota.metadata[0].name
  }
}
