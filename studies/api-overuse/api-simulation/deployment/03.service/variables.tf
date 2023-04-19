variable "project" {
  type        = string
  description = "The Google Cloud Platform (GCP) project within which resources are provisioned"
}

variable "region" {
  type        = string
  description = "The Google Cloud Platform (GCP) region in which to provision resources"
}

variable "artifact_registry_id" {
  type        = string
  description = "The id assigned to the Artifact Registry repository"
}

variable "echo_image_name_tag" {
  type        = string
  description = "The name and tag of the echo service image i.e. 'echo:latest'"
}

variable "redis_service_name" {
  type        = string
  description = "The name of the kubernetes service exposing redis"
}

variable "resource_name" {
  type        = string
  description = "The common resource name applied to shared resources such as namespace, etc"
}

variable "service_port" {
  type        = number
  description = "The port to bind to the quota service"
}
