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
package org.apache.beam.sdk.io.stackexchange.encoding;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import org.apache.avro.generic.GenericRecord;
import org.apache.beam.sdk.extensions.avro.schemas.utils.AvroUtils;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;

public class GenericRecordEncoderDecoder<T> {

  public static <T> GenericRecordEncoderDecoder<T> of(Class<T> type) {
    return new GenericRecordEncoderDecoder<>(type);
  }

  private static final AutoValueSchema SCHEMA_PROVIDER = new AutoValueSchema();

  private final SerializableFunction<T, Row> toRowFn;
  private final SerializableFunction<Row, T> fromRowFn;
  private final SerializableFunction<Row, GenericRecord> fromRowToGenericRecordFn;
  private final SerializableFunction<GenericRecord, Row> toRowFromGenericRecordFn;

  GenericRecordEncoderDecoder(Class<T> type) {
    TypeDescriptor<T> typeDescriptor = TypeDescriptor.of(type);
    this.toRowFn = checkStateNotNull(SCHEMA_PROVIDER.toRowFunction(typeDescriptor));
    this.fromRowFn = checkStateNotNull(SCHEMA_PROVIDER.fromRowFunction(typeDescriptor));
    Schema beamSchema = checkStateNotNull(SCHEMA_PROVIDER.schemaFor(typeDescriptor));
    org.apache.avro.Schema avroSchema = AvroUtils.toAvroSchema(beamSchema);
    this.fromRowToGenericRecordFn =
        checkStateNotNull(AvroUtils.getRowToGenericRecordFunction(avroSchema));
    this.toRowFromGenericRecordFn =
        checkStateNotNull(AvroUtils.getGenericRecordToRowFunction(beamSchema));
  }

  public GenericRecord encode(T input) {
    Row row = checkStateNotNull(toRowFn.apply(input));
    return checkStateNotNull(fromRowToGenericRecordFn.apply(row));
  }

  public T decode(GenericRecord input) {
    Row row = checkStateNotNull(toRowFromGenericRecordFn.apply(input));
    return checkStateNotNull(fromRowFn.apply(row));
  }
}
