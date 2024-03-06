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

resource "google_project_service" "required" {
  service            = "bigtable.googleapis.com"
  disable_on_destroy = false
}

resource "google_bigtable_instance" "default" {
  depends_on   = [google_project_service.required]
  name         = var.resource_name_basis
  display_name = var.resource_name_basis
  cluster {
    cluster_id   = "${var.resource_name_basis}-cluster"
    storage_type = "SSD"
    zone         = data.google_compute_zones.available.names[0]
    num_nodes    = 1
  }
}

resource "google_bigtable_table" "default" {
  instance_name = google_bigtable_instance.default.name
  name          = "${var.resource_name_basis}-table"
  column_family {
    family = var.column_family
  }
}

resource "google_bigtable_gc_policy" "default" {
  column_family = var.column_family
  instance_name = google_bigtable_instance.default.name
  table         = google_bigtable_table.default.name
  max_age {
    duration = "1h"
  }
}
