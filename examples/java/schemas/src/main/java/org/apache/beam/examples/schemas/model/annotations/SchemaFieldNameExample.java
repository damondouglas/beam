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
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaFieldName;

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
