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
package org.apache.beam.examples.schemas.encoding;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.beam.examples.schemas.model.annotations.CaseFormatExample;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaProvider;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.io.payloads.JsonPayloadSerializerProvider;
import org.apache.beam.sdk.schemas.io.payloads.PayloadSerializer;
import org.apache.beam.sdk.schemas.io.payloads.PayloadSerializerProvider;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableMap;

/**
 * This example demonstrates the use of {@link JsonPayloadSerializerProvider} to generate methods to
 * convert between {@link Row}s and JSON strings.
 */
public class JsonEncodingExample {

  /**
   * We use {@link DefaultSchema.DefaultSchemaProvider} to model the {@link Schema} and generate
   * {@link Row} conversion methods.
   */
  private static final DefaultSchema.DefaultSchemaProvider DEFAULT_SCHEMA_PROVIDER =
      new DefaultSchema.DefaultSchemaProvider();

  /** A {@link SchemaProvider} needs a {@link TypeDescriptor} to model a type's {@link Schema}. */
  private static final TypeDescriptor<CaseFormatExample> TYPE_DESCRIPTOR =
      TypeDescriptor.of(CaseFormatExample.class);

  /**
   * We model a {@link Schema} using the {@link SchemaProvider} and {@link CaseFormatExample}'s
   * {@link TypeDescriptor}.
   */
  private static final Schema SCHEMA = DEFAULT_SCHEMA_PROVIDER.schemaFor(TYPE_DESCRIPTOR);

  private static final SerializableFunction<CaseFormatExample, Row> TO_ROW_FN =
      DEFAULT_SCHEMA_PROVIDER.toRowFunction(TYPE_DESCRIPTOR);

  private static final SerializableFunction<Row, CaseFormatExample> FROM_ROW_FN =
      DEFAULT_SCHEMA_PROVIDER.fromRowFunction(TYPE_DESCRIPTOR);

  /**
   * {@link JsonPayloadSerializerProvider} is a {@link PayloadSerializerProvider} that provides a
   * {@link PayloadSerializer} to convert between {@link Row}s and JSON encodings.
   */
  private static final JsonPayloadSerializerProvider SERIALIZER_PROVIDER =
      new JsonPayloadSerializerProvider();

  /**
   * Generate a {@link PayloadSerializer} from the {@link PayloadSerializerProvider} using the
   * {@link Schema}.
   */
  private static final PayloadSerializer PAYLOAD_SERIALIZER =
      SERIALIZER_PROVIDER.getSerializer(SCHEMA, ImmutableMap.of());

  /**
   * Generate a {@link SerializableFunction} to convert from {@link CaseFormatExample} instance to
   * JSON strings.
   */
  public static final SerializableFunction<CaseFormatExample, String> TO_JSON_FN =
      input -> {
        Row row = Objects.requireNonNull(TO_ROW_FN.apply(input));
        byte[] bytes = PAYLOAD_SERIALIZER.serialize(row);
        return new String(bytes, StandardCharsets.UTF_8);
      };

  /**
   * Generate a {@link SerializableFunction} to convert from JSON strings to a {@link
   * CaseFormatExample} instance.
   */
  public static final SerializableFunction<String, CaseFormatExample> FROM_JSON_FN =
      input -> {
        byte[] bytes = Objects.requireNonNull(input).getBytes(StandardCharsets.UTF_8);
        Row row = PAYLOAD_SERIALIZER.deserialize(bytes);
        return FROM_ROW_FN.apply(row);
      };
}
