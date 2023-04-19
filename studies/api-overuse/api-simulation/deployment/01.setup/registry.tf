// Provision Artifact Registry repository to store built images.
resource "google_artifact_registry_repository" "default" {
  depends_on    = [google_project_service.required_services]
  format        = "DOCKER"
  repository_id = var.artifact_repository_id
  location      = var.region
}
