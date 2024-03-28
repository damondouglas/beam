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
import java.util.List;
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
public abstract class JobMetrics {

  private static final AutoValueSchema SCHEMA_PROVIDER = new AutoValueSchema();

  private static final TypeDescriptor<JobMetrics> TYPE_DESCRIPTOR =
      TypeDescriptor.of(JobMetrics.class);

  private static final Schema SCHEMA =
      checkStateNotNull(SCHEMA_PROVIDER.schemaFor(TYPE_DESCRIPTOR));

  public static final SerializableFunction<JobMetrics, Row> TO_ROW_FN =
      checkStateNotNull(SCHEMA_PROVIDER.toRowFunction(TYPE_DESCRIPTOR));

  static final SerializableFunction<Row, JobMetrics> FROM_ROW_FN =
      checkStateNotNull(SCHEMA_PROVIDER.fromRowFunction(TYPE_DESCRIPTOR));

  public static final RowCoder ROW_CODER = RowCoder.of(SCHEMA);

  static JobMetrics from(com.google.dataflow.v1beta3.JobMetrics response) {
    return builder()
        .setMetricTime(Instant.ofEpochSecond(response.getMetricTime().getSeconds()))
        .setMetrics(MetricUpdate.from(response.getMetricsList()))
        .build();
  }

  static Builder builder() {
    return new AutoValue_JobMetrics.Builder();
  }

  public abstract Instant getMetricTime();

  public abstract List<MetricUpdate> getMetrics();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setMetricTime(Instant metricTime);

    public abstract Builder setMetrics(List<MetricUpdate> metrics);

    public abstract JobMetrics build();
  }
}
