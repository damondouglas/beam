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
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaProvider;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaFieldName;

/**
 * {@link SchemaFieldNameExample} demonstrates overriding the field name for a property. In the
 * {@link AutoValueSchema} context shown below, {@link SchemaProvider} assigns the {@link
 * Schema.Field} name reflecting on the getters and setters. Without
 * {@code @SchemaFieldName("some_integer")}, the {@link SchemaProvider} assigns the field name
 * {@code anInteger}. Applying the {@link SchemaFieldName} annotation overrides this default name to
 * {@code some_integer}. See the corresponding SchemaFieldNameExampleTest for a runnable example.
 */
@DefaultSchema(AutoValueSchema.class)
@AutoValue
public abstract class SchemaFieldNameExample {

  public static Builder builder() {
    return new AutoValue_SchemaFieldNameExample.Builder();
  }

  @SchemaFieldName("some_integer")
  public abstract Integer getAnInteger();

  @SchemaFieldName("some_double")
  public abstract Double getADouble();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setAnInteger(Integer value);

    public abstract Builder setADouble(Double value);

    public abstract SchemaFieldNameExample build();
  }
}
