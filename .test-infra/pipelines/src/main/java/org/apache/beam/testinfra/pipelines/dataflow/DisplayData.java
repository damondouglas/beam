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
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;
import org.joda.time.Duration;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class DisplayData {

  static List<DisplayData> from(List<com.google.dataflow.v1beta3.DisplayData> source) {
    return source.stream().map(DisplayData::from).collect(Collectors.toList());
  }

  static DisplayData from(com.google.dataflow.v1beta3.DisplayData source) {
    Builder builder =
        builder()
            .setKey(source.getKey())
            .setLabel(source.getLabel())
            .setShortStrValue(source.getShortStrValue())
            .setNamespace(source.getNamespace())
            .setLabel(source.getLabel());

    switch (source.getValueCase()) {
      case STR_VALUE:
        builder.setValue(source.getStrValue());
        return builder.build();
      case BOOL_VALUE:
        builder.setValue(String.valueOf(source.getBoolValue()));
        return builder.build();
      case DURATION_VALUE:
        builder.setValue(Duration.millis(source.getDurationValue().getSeconds() * 1000).toString());
        return builder.build();
      case FLOAT_VALUE:
        builder.setValue(String.valueOf(source.getFloatValue()));
        return builder.build();
      case INT64_VALUE:
        builder.setValue(String.valueOf(source.getInt64Value()));
        return builder.build();
      case JAVA_CLASS_VALUE:
        builder.setValue(source.getJavaClassValue());
        return builder.build();
      case TIMESTAMP_VALUE:
        builder.setValue(Instant.ofEpochSecond(source.getTimestampValue().getSeconds()).toString());
        return builder.build();
      case VALUE_NOT_SET:
        builder.setValue("");
        return builder.build();
      default:
        return builder.build();
    }
  }

  static Builder builder() {
    return new AutoValue_DisplayData.Builder();
  }

  public abstract String getKey();

  public abstract String getNamespace();

  public abstract String getShortStrValue();

  public abstract String getUrl();

  public abstract String getLabel();

  public abstract String getValue();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setKey(String key);

    public abstract Builder setNamespace(String namespace);

    public abstract Builder setShortStrValue(String shortStrValue);

    public abstract Builder setUrl(String url);

    public abstract Builder setLabel(String label);

    public abstract Builder setValue(String value);

    public abstract DisplayData build();
  }
}
