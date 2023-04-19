variable "project" {
  type        = string
  description = "The Google Cloud Platform (GCP) project within which resources are provisioned"
}

variable "region" {
  type        = string
  description = "The Google Cloud Platform (GCP) region in which to provision resources"
}

variable "artifact_repository_id" {
  type        = string
  description = "The id assigned to the Artifact Registry repository"
}

variable "kubernetes_cluster_service_account_id" {
  type        = string
  description = "The id assigned to the kubernetes service account"
}