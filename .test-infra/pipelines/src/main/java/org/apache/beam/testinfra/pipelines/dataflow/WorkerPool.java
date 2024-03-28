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
import com.google.dataflow.v1beta3.DefaultPackageSet;
import com.google.dataflow.v1beta3.TeardownPolicy;
import com.google.dataflow.v1beta3.WorkerIPAddressConfiguration;
import java.util.List;
import java.util.Map;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class WorkerPool {

  static WorkerPool from(com.google.dataflow.v1beta3.WorkerPool source) {
    return WorkerPool.builder()
        .setKind(source.getKind())
        .setNumWorkers(source.getNumWorkers())
        .setPackages(WorkerPackage.from(source.getPackagesList()))
        .setDefaultPackageSet(source.getDefaultPackageSet())
        .setMachineType(source.getMachineType())
        .setTeardownPolicy(source.getTeardownPolicy())
        .setDiskSizeGb(source.getDiskSizeGb())
        .setDiskType(source.getDiskType())
        .setDiskSourceImage(source.getDiskSourceImage())
        .setZone(source.getZone())
        .setTaskRunnerSettings(TaskRunnerSettings.from(source.getTaskrunnerSettings()))
        .setOnHostMaintenance(source.getOnHostMaintenance())
        .setDataDisks(Disk.from(source.getDataDisksList()))
        .setMetadata(source.getMetadataMap())
        .setAutoScalingSettings(AutoscalingSettings.from(source.getAutoscalingSettings()))
        .setPoolArgs(WellKnown.from(source.getPoolArgs()))
        .setNetwork(source.getNetwork())
        .setSubnetwork(source.getSubnetwork())
        .setWorkerHarnessContainerImage(source.getWorkerHarnessContainerImage())
        .setNumThreadsPerWorker(source.getNumThreadsPerWorker())
        .setIpConfiguration(source.getIpConfiguration())
        .setSdkHarnessContainerImage(
            SdkHarnessContainerImage.from(source.getSdkHarnessContainerImagesList()))
        .build();
  }

  static Builder builder() {
    return new AutoValue_WorkerPool.Builder();
  }

  public abstract String getKind();

  public abstract Integer getNumWorkers();

  public abstract List<WorkerPackage> getPackages();

  public abstract DefaultPackageSet getDefaultPackageSet();

  public abstract String getMachineType();

  public abstract TeardownPolicy getTeardownPolicy();

  public abstract Integer getDiskSizeGb();

  public abstract String getDiskType();

  public abstract String getDiskSourceImage();

  public abstract String getZone();

  public abstract TaskRunnerSettings getTaskRunnerSettings();

  public abstract String getOnHostMaintenance();

  public abstract List<Disk> getDataDisks();

  public abstract Map<String, String> getMetadata();

  public abstract AutoscalingSettings getAutoScalingSettings();

  public abstract Map<String, String> getPoolArgs();

  public abstract String getNetwork();

  public abstract String getSubnetwork();

  public abstract String getWorkerHarnessContainerImage();

  public abstract Integer getNumThreadsPerWorker();

  public abstract WorkerIPAddressConfiguration getIpConfiguration();

  public abstract List<SdkHarnessContainerImage> getSdkHarnessContainerImage();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setKind(String kind);

    public abstract Builder setNumWorkers(Integer numWorkers);

    public abstract Builder setPackages(List<WorkerPackage> packages);

    public abstract Builder setDefaultPackageSet(DefaultPackageSet defaultPackageSet);

    public abstract Builder setMachineType(String machineType);

    public abstract Builder setTeardownPolicy(TeardownPolicy teardownPolicy);

    public abstract Builder setDiskSizeGb(Integer diskSizeGb);

    public abstract Builder setDiskType(String diskType);

    public abstract Builder setDiskSourceImage(String diskSourceImage);

    public abstract Builder setZone(String zone);

    public abstract Builder setTaskRunnerSettings(TaskRunnerSettings taskRunnerSettings);

    public abstract Builder setOnHostMaintenance(String onHostMaintenance);

    public abstract Builder setDataDisks(List<Disk> dataDisks);

    public abstract Builder setMetadata(Map<String, String> metadata);

    public abstract Builder setAutoScalingSettings(AutoscalingSettings autoScalingSettings);

    public abstract Builder setPoolArgs(Map<String, String> poolArgs);

    public abstract Builder setNetwork(String network);

    public abstract Builder setSubnetwork(String subnetwork);

    public abstract Builder setWorkerHarnessContainerImage(String workerHarnessContainerImage);

    public abstract Builder setNumThreadsPerWorker(Integer numThreadsPerWorker);

    public abstract Builder setIpConfiguration(WorkerIPAddressConfiguration ipConfiguration);

    public abstract Builder setSdkHarnessContainerImage(
        List<SdkHarnessContainerImage> sdkHarnessContainerImage);

    public abstract WorkerPool build();
  }
}
