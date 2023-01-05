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
package org.apache.beam.stitch.expansion.transforms.strings;

import static org.apache.beam.sdk.values.TypeDescriptors.rows;
import static org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Preconditions.checkState;

import java.util.Optional;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.Schema.Field;
import org.apache.beam.sdk.schemas.Schema.FieldType;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;

public class CountExpansion extends PTransform<PCollection<String>, PCollection<Row>> {

  static final Field KEY_FIELD = Field.of("key", FieldType.STRING);
  static final Field VALUE_FIELD = Field.of("value", FieldType.INT64);

  static final Schema COUNT_EXPANSION_SCHEMA = Schema.of(KEY_FIELD, VALUE_FIELD);

  @Override
  public PCollection<Row> expand(PCollection<String> input) {
    return input
        .apply("Count Per Element", Count.perElement())
        .apply(
            "To Row",
            MapElements.into(rows())
                .via(
                    (KV<String, Long> kv) -> {
                      Optional<KV<String, Long>> safeKV = Optional.ofNullable(kv);
                      checkState(safeKV.isPresent());
                      return Row.withSchema(COUNT_EXPANSION_SCHEMA)
                          .withFieldValue(KEY_FIELD.getName(), safeKV.get().getKey())
                          .withFieldValue(VALUE_FIELD.getName(), safeKV.get().getValue())
                          .build();
                    }))
        .setRowSchema(COUNT_EXPANSION_SCHEMA);
  }
}
