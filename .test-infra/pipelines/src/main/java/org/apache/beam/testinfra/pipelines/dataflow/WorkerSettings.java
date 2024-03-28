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
package org.apache.beam.testinfra.pipelines.dataflow;

import com.google.auto.value.AutoValue;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class WorkerSettings {

  static WorkerSettings from(com.google.dataflow.v1beta3.WorkerSettings source) {
    return WorkerSettings.builder()
        .setBaseUrl(source.getBaseUrl())
        .setReportingEnabled(source.getReportingEnabled())
        .setServicePath(source.getServicePath())
        .setShuffleServicePath(source.getShuffleServicePath())
        .setWorkerId(source.getWorkerId())
        .setTempStoragePrefix(source.getTempStoragePrefix())
        .build();
  }

  static Builder builder() {
    return new AutoValue_WorkerSettings.Builder();
  }

  public abstract String getBaseUrl();

  public abstract Boolean getReportingEnabled();

  public abstract String getServicePath();

  public abstract String getShuffleServicePath();

  public abstract String getWorkerId();

  public abstract String getTempStoragePrefix();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setBaseUrl(String baseUrl);

    public abstract Builder setReportingEnabled(Boolean reportingEnabled);

    public abstract Builder setServicePath(String servicePath);

    public abstract Builder setShuffleServicePath(String shuffleServicePath);

    public abstract Builder setWorkerId(String workerId);

    public abstract Builder setTempStoragePrefix(String tempStoragePrefix);

    public abstract WorkerSettings build();
  }
}
