/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

variable "namespace" {
  type        = string
  description = "The Kubernetes namespace to provision resources"
}

variable "project" {
  type        = string
  description = "The Google Cloud Platform (GCP) project within which resources are provisioned"
}

variable "region" {
  type        = string
  description = "The Google Cloud Platform (GCP) region in which to provision resources"
}

variable "service_account_id" {
  type        = string
  description = "The Dataflow Worker Service Account ID"
}

variable "secret_manager_secret" {
  type        = string
  description = "The Google Secret Manager Secret name to store Looker API credentials"
}

variable "storage_bucket" {
  type        = string
  description = "The Google Cloud Storage Bucket to export Looker Looks"
}

variable "artifact_registry_id" {
  type        = string
  description = "The ID of the artifact registry repository"
}

variable "go_executable_path" {
  type        = string
  description = "The go mod path to the executable"
}

variable "look_result_output_pattern" {
  type        = string
  description = "The output pattern of the exported look. Uses go template syntax"
}

variable "look_result_format" {
  type        = string
  description = "The output format of the Looker Look. See https://cloud.google.com/looker/docs/reference/looker-api/latest/methods/Look/run_look"
}

variable "look_resources" {
  type        = list(string)
  description = "The look resources to run and export. Expected form: looks/LOOK_ID or folders/FOLDER_ID. See https://cloud.google.com/looker/docs/reference/looker-api/latest/methods/Look/run_look"
}

variable "cron_job_frequency" {
  type        = string
  description = "The cron job frequency i.e. 0 0 * * *"
}