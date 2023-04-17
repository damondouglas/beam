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
package org.apache.beam.examples.schemas.format;

import static org.apache.beam.sdk.values.TypeDescriptors.rows;
import static org.apache.beam.sdk.values.TypeDescriptors.strings;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.beam.examples.schemas.model.annotations.CaseFormatExample;
import org.apache.beam.examples.schemas.model.autovalueschema.NestedTypeContaining;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.io.payloads.JsonPayloadSerializerProvider;
import org.apache.beam.sdk.schemas.io.payloads.PayloadSerializer;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableMap;

/**
 * {@link JsonEncodingExample} demonstrates converting to and from Json formats using Schema-aware
 * types. {@link NestedTypeContaining} was chosen purposely to demonstrate a variety of primitive,
 * time-related, and nested types. See corresponding AvroEncodingExampleTest for a runnable example.
 */
public class JsonEncodingExample {
  private static final DefaultSchema.DefaultSchemaProvider SCHEMA_PROVIDER =
      new DefaultSchema.DefaultSchemaProvider();

  private static final TypeDescriptor<CaseFormatExample> TYPE_DESCRIPTOR =
      TypeDescriptor.of(CaseFormatExample.class);

  private static final Schema SCHEMA = SCHEMA_PROVIDER.schemaFor(TYPE_DESCRIPTOR);

  private static final PayloadSerializer PAYLOAD_SERIALIZER =
      new JsonPayloadSerializerProvider().getSerializer(SCHEMA, ImmutableMap.of());

  private static final SerializableFunction<CaseFormatExample, Row> TO_ROW_FN =
      SCHEMA_PROVIDER.toRowFunction(TYPE_DESCRIPTOR);

  private static final SerializableFunction<Row, CaseFormatExample> FROM_ROW_FN =
      SCHEMA_PROVIDER.fromRowFunction(TYPE_DESCRIPTOR);

  private static final SerializableFunction<Row, String> TO_JSON_FN =
      row -> {
        Row safeRow = Objects.requireNonNull(row);
        return new String(PAYLOAD_SERIALIZER.serialize(safeRow), StandardCharsets.UTF_8);
      };

  private static final SerializableFunction<String, Row> FROM_JSON_FN =
      json -> {
        String safeJson = Objects.requireNonNull(json);
        return PAYLOAD_SERIALIZER.deserialize(safeJson.getBytes(StandardCharsets.UTF_8));
      };

  public static class ToJson
      extends PTransform<PCollection<CaseFormatExample>, PCollection<String>> {
    @Override
    public PCollection<String> expand(PCollection<CaseFormatExample> input) {
      return input
          .apply("To Row", MapElements.into(rows()).via(TO_ROW_FN))
          .apply("To Json", MapElements.into(strings()).via(TO_JSON_FN));
    }
  }

  public static class FromJson
      extends PTransform<PCollection<String>, PCollection<CaseFormatExample>> {
    @Override
    public PCollection<CaseFormatExample> expand(PCollection<String> input) {
      return input
          .apply("From Json", MapElements.into(rows()).via(FROM_JSON_FN))
          .apply("From Row", MapElements.into(TYPE_DESCRIPTOR).via(FROM_ROW_FN));
    }
  }
}
