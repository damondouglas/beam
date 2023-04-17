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
import java.util.List;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaProvider;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/**
 * {@link NestedTypeContaining} demonstrates that we can nest other Schema-aware types. In the
 * following example, the class contains {@link PrimitiveTypesContaining} and {@link
 * TimeTypesContaining} field types, as well as a List to illustrate that nested repeated types work
 * as well. {@link SchemaProvider} in this context will generate {@link NestedTypeContaining}'s
 * {@link Schema} based on both of the inner classes' {@link Schema}. See corresponding
 * NestedTypeContainingTest for a runnable example.
 */
@DefaultSchema(AutoValueSchema.class)
@AutoValue
public abstract class NestedTypeContaining {

  public static Builder builder() {
    return new AutoValue_NestedTypeContaining.Builder();
  }

  public abstract PrimitiveTypesContaining getPrimitiveTypesContaining();

  public abstract TimeTypesContaining getTimeTypesContaining();

  public abstract List<PrimitiveTypesContaining> getPrimitiveTypesContainingList();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setPrimitiveTypesContaining(PrimitiveTypesContaining value);

    public abstract Builder setTimeTypesContaining(TimeTypesContaining value);

    public abstract Builder setPrimitiveTypesContainingList(List<PrimitiveTypesContaining> value);

    public abstract NestedTypeContaining build();
  }
}
