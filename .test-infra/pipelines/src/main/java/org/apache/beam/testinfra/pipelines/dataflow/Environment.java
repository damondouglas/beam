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
import com.google.dataflow.v1beta3.ShuffleMode;
import com.google.events.cloud.dataflow.v1beta3.FlexResourceSchedulingGoal;
import java.util.List;
import java.util.Map;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class Environment {

  public abstract String getTempStoragePrefix();

  public abstract String getClusterManagerApiService();

  public abstract List<String> getExperiments();

  public abstract List<String> getServiceOptions();

  public abstract String getServiceKmsKeyName();

  public abstract List<WorkerPool> getWorkerPools();

  public abstract Map<String, String> getUserAgent();

  public abstract Map<String, String> getVersion();

  public abstract String getDataset();

  public abstract Map<String, String> getSdkPipelineOptions();

  public abstract Map<String, String> getInternalExperiments();

  public abstract String getServiceAccountEmail();

  public abstract FlexResourceSchedulingGoal getFlexResourceSchedulingGoal();

  public abstract String getWorkerRegion();

  public abstract String getWorkerZone();

  public abstract ShuffleMode getShuffleMode();

  public abstract Boolean getUseStreamingEngineResourceBasedBilling();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setTempStoragePrefix(String tempStoragePrefix);

    public abstract Builder setClusterManagerApiService(String clusterManagerApiService);

    public abstract Builder setExperiments(List<String> experiments);

    public abstract Builder setServiceOptions(List<String> serviceOptions);

    public abstract Builder setServiceKmsKeyName(String serviceKmsKeyName);

    public abstract Builder setWorkerPools(List<WorkerPool> workerPools);

    public abstract Builder setUserAgent(Map<String, String> userAgent);

    public abstract Builder setVersion(Map<String, String> version);

    public abstract Builder setDataset(String dataset);

    public abstract Builder setSdkPipelineOptions(Map<String, String> sdkPipelineOptions);

    public abstract Builder setInternalExperiments(Map<String, String> internalExperiments);

    public abstract Builder setServiceAccountEmail(String serviceAccountEmail);

    public abstract Builder setFlexResourceSchedulingGoal(
        FlexResourceSchedulingGoal flexResourceSchedulingGoal);

    public abstract Builder setWorkerRegion(String workerRegion);

    public abstract Builder setWorkerZone(String workerZone);

    public abstract Builder setShuffleMode(ShuffleMode shuffleMode);

    public abstract Builder setUseStreamingEngineResourceBasedBilling(
        Boolean useStreamingEngineResourceBasedBilling);

    public abstract Environment build();
  }
}
