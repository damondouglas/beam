// Provision the required Google Cloud services
resource "google_project_service" "required_services" {
  for_each = toset([
    "artifactregistry",
    "bigquery",
    "cloudbuild",
  ])

  service            = "${each.key}.googleapis.com"
  disable_on_destroy = false
}