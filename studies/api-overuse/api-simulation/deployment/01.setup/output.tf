locals {
  root_path = abspath("${path.module}/../../..")
}
output "build_command" {
  value = <<EOF
cd ${local.root_path}; \
export KO_DOCKER_REPO=${google_artifact_registry_repository.default.location}-docker.pkg.dev/${google_artifact_registry_repository.default.project}/${google_artifact_registry_repository.default.repository_id}/server; \
ko build ./cmd -t latest --bare
EOF
}
