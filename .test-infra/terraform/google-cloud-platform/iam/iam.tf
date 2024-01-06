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

// Generate a random string
resource "random_string" "postfix" {
  length  = 6
  upper   = false
  special = false
  lower   = true
  numeric = true
}

locals {
  service_account_id = "${var.service_account_id_prefix}-${random_string.postfix.result}"
}

// Create the service account
resource "google_service_account" "default" {
  account_id   = local.service_account_id
  display_name = local.service_account_id
}

// Bind the IAM roles.
resource "google_project_iam_member" "default" {
  for_each = toset(var.roles)
  member   = "serviceAccount:${google_service_account.default.email}"
  project  = var.project
  role     = each.key
}
