<!--
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
-->

# Overview

This directory contains Infrastructure-as-Code (IaC) to provision dependent resources to execute the Beam pipeline on
Dataflow.

# Requirements

- [Google Cloud SDK](https://cloud.google.com/sdk); `gcloud init`
  and `gcloud auth`
- Google Cloud project with billing enabled
- [terraform](https://www.terraform.io/)

# List of all provision GCP resources

See the README in each folder for a list of provisioned GCP resources
that result from applying its IaC.

# Usage

Each folder is numbered according to the recommended order of execution. See each individual folder's README for
further instructions.