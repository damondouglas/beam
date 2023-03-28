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

import java.util.Objects;
import org.apache.avro.generic.GenericRecord;
import org.apache.beam.examples.schemas.model.annotations.CaseFormatExample;
import org.apache.beam.examples.schemas.model.javabeanschema.Simple;
import org.apache.beam.sdk.extensions.avro.schemas.utils.AvroUtils;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaProvider;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;

public class AvroEncodingExample {
  /**
   * We use {@link DefaultSchema.DefaultSchemaProvider} to model the {@link Schema} and generate
   * {@link Row} conversion methods.
   */
  private static final DefaultSchema.DefaultSchemaProvider DEFAULT_SCHEMA_PROVIDER =
      new DefaultSchema.DefaultSchemaProvider();

  /** A {@link SchemaProvider} needs a {@link TypeDescriptor} to model a type's {@link Schema}. */
  private static final TypeDescriptor<Simple> TYPE_DESCRIPTOR = TypeDescriptor.of(Simple.class);

  /**
   * We model a {@link Schema} using the {@link SchemaProvider} and {@link CaseFormatExample}'s
   * {@link TypeDescriptor}.
   */
  private static final Schema SCHEMA = DEFAULT_SCHEMA_PROVIDER.schemaFor(TYPE_DESCRIPTOR);

  private static final org.apache.avro.Schema AVRO_SCHEMA = AvroUtils.toAvroSchema(SCHEMA);

  private static final SerializableFunction<Simple, Row> SIMPLE_TO_ROW_FN =
      DEFAULT_SCHEMA_PROVIDER.toRowFunction(TYPE_DESCRIPTOR);

  private static final SerializableFunction<Row, Simple> SIMPLE_FROM_ROW_FN =
      DEFAULT_SCHEMA_PROVIDER.fromRowFunction(TYPE_DESCRIPTOR);

  private static final SerializableFunction<GenericRecord, Row> ROW_FROM_AVRO_FN =
      AvroUtils.getGenericRecordToRowFunction(SCHEMA);

  private static final SerializableFunction<Row, GenericRecord> ROW_TO_AVRO_FN =
      AvroUtils.getRowToGenericRecordFunction(AVRO_SCHEMA);

  public static final SerializableFunction<Simple, GenericRecord> TO_AVRO_FN =
      input -> {
        Row row = Objects.requireNonNull(SIMPLE_TO_ROW_FN.apply(input));
        return ROW_TO_AVRO_FN.apply(row);
      };

  public static final SerializableFunction<GenericRecord, Simple> FROM_AVRO_FN =
      input -> {
        Row row = Objects.requireNonNull(ROW_FROM_AVRO_FN.apply(input));
        return SIMPLE_FROM_ROW_FN.apply(row);
      };
}
