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
package org.apache.beam.examples.schemas.model.annotations;

import com.google.auto.value.AutoValue;
import org.apache.beam.examples.schemas.encoding.JsonEncodingExample;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaProvider;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.CaseFormat;
import org.joda.time.Instant;

/**
 * This example demonstrates the use of {@link SchemaCaseFormat} to customize the field name case.
 * Without the use of {@link SchemaCaseFormat}, field names generated by a {@link SchemaProvider}
 * when modeling a {@link Schema} default to camel case. For example, in a {@link AutoValueSchema}
 * context, used below, the getter/setter pair {@code getMyProperty()} / {@code setMyProperty(...)}
 * generates the field name {@code myProperty}. However, if we annotate the class with
 * {@code @SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)}, the {@link SchemaProvider} instead
 * generates the field name as {@code my_property}.
 *
 * <p>This is helpful in contexts where we require specific field name cases, typical of REST API
 * payloads, for example. This valuable annotation prevents us from having to manually get/set the
 * field names in their conventional case formats.
 *
 * <p>See the corresponding tests for this class as well as tests for {@link JsonEncodingExample}
 * for examples how field names generate using {@link SchemaCaseFormat}.
 */
@DefaultSchema(AutoValueSchema.class)
@AutoValue
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
public abstract class CaseFormatExample {

  public static CaseFormatExample of(
      String aString, Integer anInteger, Double aDouble, Boolean aBoolean, Instant anInstant) {
    return CaseFormatExample.builder()
        .setAString(aString)
        .setABoolean(aBoolean)
        .setAnInteger(anInteger)
        .setADouble(aDouble)
        .setAnInstant(anInstant)
        .build();
  }

  public static Builder builder() {
    return new AutoValue_CaseFormatExample.Builder();
  }

  public abstract String getAString();

  public abstract Integer getAnInteger();

  public abstract Double getADouble();

  public abstract Boolean getABoolean();

  public abstract Instant getAnInstant();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setAString(String value);

    public abstract Builder setAnInteger(Integer value);

    public abstract Builder setADouble(Double value);

    public abstract Builder setABoolean(Boolean value);

    public abstract Builder setAnInstant(Instant value);

    public abstract CaseFormatExample build();
  }
}
