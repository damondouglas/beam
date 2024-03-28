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
import java.util.List;
import java.util.stream.Collectors;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joda.time.Instant;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class MetricUpdate {

  static List<MetricUpdate> from(List<com.google.dataflow.v1beta3.MetricUpdate> source) {
    return source.stream().map(MetricUpdate::from).collect(Collectors.toList());
  }

  static MetricUpdate from(com.google.dataflow.v1beta3.MetricUpdate source) {
    Builder builder =
        builder()
            .setKind(source.getKind())
            .setCumulative(source.getCumulative())
            .setUpdateTime(Instant.ofEpochSecond(source.getUpdateTime().getSeconds()));

    if (source.getScalar().hasNumberValue()) {
      builder.setScalar(source.getScalar().getNumberValue());
    }

    if (source.getMeanSum().hasNumberValue()) {
      builder.setMeanSum(source.getMeanSum().getNumberValue());
    }

    if (source.getMeanCount().hasNumberValue()) {
      builder.setMeanCount(Double.valueOf(source.getMeanCount().getNumberValue()).longValue());
    }

    return builder.build();
  }

  static Builder builder() {
    return new AutoValue_MetricUpdate.Builder();
  }

  public abstract MetricStructuredName getName();

  public abstract String getKind();

  public abstract Boolean getCumulative();

  public abstract @Nullable Double getScalar();

  public abstract @Nullable Double getMeanSum();

  public abstract @Nullable Long getMeanCount();

  public abstract Instant getUpdateTime();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setName(MetricStructuredName name);

    public abstract Builder setKind(String kind);

    public abstract Builder setCumulative(Boolean cumulative);

    public abstract Builder setScalar(Double scalar);

    public abstract Builder setMeanSum(Double meanSum);

    public abstract Builder setMeanCount(Long meanCount);

    public abstract Builder setUpdateTime(Instant updateTime);

    public abstract MetricUpdate build();
  }
}
