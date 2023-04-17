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
package org.apache.beam.examples.schemas.model.autovalueschema;

import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/**
 * Illustrates modeling a nullable primitive types (i.e. boolean, integer, etc) containing {@link
 * org.apache.beam.sdk.schemas.Schema} using an {@link AutoValue} annotated class. This class is
 * exactly like {@link PrimitiveTypesContaining} yet with a {@code @Nullable} annotation. The
 * purpose of this example is for you to compare with or without the {@code @Nullable} annotation on
 * the getters to see the outcome on the {@link Schema}. See corresponding
 * NullableTypesContainingTest for a runnable example.
 */
@DefaultSchema(AutoValueSchema.class)
@AutoValue
public abstract class NullableTypesContaining {

  public static Builder builder() {
    return new AutoValue_NullableTypesContaining.Builder();
  }

  @Nullable
  public abstract Boolean getABoolean();

  @Nullable
  public abstract Byte getAByte();

  @Nullable
  public abstract Double getADouble();

  @Nullable
  public abstract Float getAFloat();

  @Nullable
  public abstract Short getAShort();

  @Nullable
  public abstract Integer getAnInteger();

  @Nullable
  public abstract Long getALong();

  @Nullable
  public abstract String getAString();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setABoolean(Boolean value);

    public abstract Builder setAByte(Byte value);

    public abstract Builder setADouble(Double value);

    public abstract Builder setAFloat(Float value);

    public abstract Builder setAShort(Short value);

    public abstract Builder setAnInteger(Integer value);

    public abstract Builder setALong(Long value);

    public abstract Builder setAString(String value);

    public abstract NullableTypesContaining build();
  }
}
