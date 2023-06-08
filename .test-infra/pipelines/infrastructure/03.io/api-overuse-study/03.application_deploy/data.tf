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

locals {
  cache_host = "${data.kubernetes_service.redis.metadata[0].name}.${data.kubernetes_service.redis.metadata[0].namespace}.svc.cluster.local:${data.kubernetes_service.redis.spec[0].port[0].port}"
}

data "kubernetes_namespace" "default" {
  metadata {
    name = var.namespace
  }
}

data "kubernetes_service" "redis" {
  metadata {
    name      = var.redis_service_name
    namespace = data.kubernetes_namespace.default.metadata[0].name
  }
}
