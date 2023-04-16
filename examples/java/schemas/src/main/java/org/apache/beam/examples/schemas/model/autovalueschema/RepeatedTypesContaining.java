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
 * {@link RepeatedTypesContaining} illustrates how to model repeated types in a Schema-aware context using an
 * {@link AutoValue} class. In this example's context {@link SchemaProvider} derives {@link #getAnIntegerList} and
 * {@link #getAStringList()} as collection {@link Schema.FieldType}s.
 */
@DefaultSchema(AutoValueSchema.class)
@AutoValue
public abstract class RepeatedTypesContaining {

  public abstract List<String> getAStringList();

  public abstract List<Integer> getAnIntegerList();

  public abstract static class Builder {

    public abstract Builder setAStringList(List<String> value);

    public abstract Builder setAnIntegerList(List<Integer> value);

    public abstract RepeatedTypesContaining build();
  }
}
