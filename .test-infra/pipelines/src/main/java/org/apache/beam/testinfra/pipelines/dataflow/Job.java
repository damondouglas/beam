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

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import com.google.auto.value.AutoValue;
import com.google.dataflow.v1beta3.JobState;
import com.google.dataflow.v1beta3.JobType;
import java.util.List;
import java.util.Map;
import org.apache.beam.sdk.coders.RowCoder;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;
import org.joda.time.Instant;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class Job {

  private static final AutoValueSchema SCHEMA_PROVIDER = new AutoValueSchema();

  private static final TypeDescriptor<Job> TYPE_DESCRIPTOR = TypeDescriptor.of(Job.class);

  private static final Schema SCHEMA =
      checkStateNotNull(SCHEMA_PROVIDER.schemaFor(TYPE_DESCRIPTOR));

  public static final SerializableFunction<Job, Row> TO_ROW_FN =
      checkStateNotNull(SCHEMA_PROVIDER.toRowFunction(TYPE_DESCRIPTOR));

  static final SerializableFunction<Row, Job> FROM_ROW_FN =
      checkStateNotNull(SCHEMA_PROVIDER.fromRowFunction(TYPE_DESCRIPTOR));

  public static final RowCoder ROW_CODER = RowCoder.of(SCHEMA);

  static Job from(com.google.dataflow.v1beta3.Job source) {
    return builder()
        .setId(source.getId())
        .setProjectId(source.getProjectId())
        .setName(source.getName())
        .setJobType(source.getType())
        .setStepsLocation(source.getStepsLocation())
        .setCurrentState(source.getCurrentState())
        .setCurrentStateTime(Instant.ofEpochSecond(source.getCurrentStateTime().getSeconds()))
        .setRequestedState(source.getRequestedState())
        .setCreateTime(Instant.ofEpochSecond(source.getCreateTime().getSeconds()))
        .setReplaceJobId(source.getReplaceJobId())
        .setTransformNameMapping(source.getTransformNameMappingMap())
        .setClientRequestId(source.getClientRequestId())
        .setReplacedByJobId(source.getReplacedByJobId())
        .setTempFiles(source.getTempFilesList())
        .setLabels(source.getLabelsMap())
        .setLocation(source.getLocation())
        .setStartTime(Instant.ofEpochSecond(source.getStartTime().getSeconds()))
        .setCreatedFromSnapshotId(source.getCreatedFromSnapshotId())
        .setSatisfiesPzs(source.getSatisfiesPzs())
        .build();
  }

  static Builder builder() {
    return new AutoValue_Job.Builder();
  }

  public abstract String getId();

  public abstract String getProjectId();

  public abstract String getName();

  public abstract JobType getJobType();

  public abstract String getStepsLocation();

  public abstract JobState getCurrentState();

  public abstract Instant getCurrentStateTime();

  public abstract JobState getRequestedState();

  public abstract Instant getCreateTime();

  public abstract String getReplaceJobId();

  public abstract Map<String, String> getTransformNameMapping();

  public abstract String getClientRequestId();

  public abstract String getReplacedByJobId();

  public abstract List<String> getTempFiles();

  public abstract Map<String, String> getLabels();

  public abstract String getLocation();

  public abstract Instant getStartTime();

  public abstract String getCreatedFromSnapshotId();

  public abstract Boolean getSatisfiesPzs();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setId(String id);

    public abstract Builder setProjectId(String projectId);

    public abstract Builder setName(String name);

    public abstract Builder setJobType(JobType jobType);

    public abstract Builder setStepsLocation(String stepsLocation);

    public abstract Builder setCurrentState(JobState currentState);

    public abstract Builder setCurrentStateTime(Instant currentStateTime);

    public abstract Builder setRequestedState(JobState requestedState);

    public abstract Builder setCreateTime(Instant createTime);

    public abstract Builder setReplaceJobId(String replaceJobId);

    public abstract Builder setTransformNameMapping(Map<String, String> transformNameMapping);

    public abstract Builder setClientRequestId(String clientRequestId);

    public abstract Builder setReplacedByJobId(String replacedByJobId);

    public abstract Builder setTempFiles(List<String> tempFiles);

    public abstract Builder setLabels(Map<String, String> labels);

    public abstract Builder setLocation(String location);

    public abstract Builder setStartTime(Instant startTime);

    public abstract Builder setCreatedFromSnapshotId(String createdFromSnapshotId);

    public abstract Builder setSatisfiesPzs(Boolean satisfiesPzs);

    public abstract Job build();
  }
}
