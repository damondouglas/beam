data "google_service_account" "default" {
  account_id = var.kubernetes_cluster_service_account_id
}

resource "google_project_iam_member" "artifact_registry_accessor" {
  member  = "serviceAccount:${data.google_service_account.default.email}"
  project = var.project
  role    = "roles/artifactregistry.reader"
}
