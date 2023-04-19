provider "google" {
  project = var.project
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}
