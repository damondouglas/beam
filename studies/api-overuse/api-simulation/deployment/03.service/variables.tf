variable "namespace" {
  type        = string
  description = "The Kubernetes namespace"
}

variable "redis_service_name" {
  type        = string
  description = "The name of the Kubernetes service exposing redis"
}

variable "image_tag" {
  type        = string
  description = "The image tag to reference for the container images"
  default     = "latest"
}

variable "image_repository" {
  type        = string
  description = "The image repository to reference for the container images"
  default     = "ko.local"
}

variable "echo_service" {
  type = object({
    name     = string
    image_id = string
    port     = number
  })

  description = "Echo service configuration"
}

variable "quota_service" {
  type = object({
    name               = string
    image_id           = string
    port               = number
    refresher_image_id = string
  })

  description = "Quota service configuration"
}
