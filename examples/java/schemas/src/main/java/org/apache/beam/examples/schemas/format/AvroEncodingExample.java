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

import org.apache.avro.generic.GenericRecord;
import org.apache.beam.examples.schemas.model.autovalueschema.NestedTypeContaining;
import org.apache.beam.sdk.extensions.avro.schemas.utils.AvroUtils;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;

/**
 * {@link AvroEncodingExample} demonstrates converting to and from Avro formats using Schema-aware
 * types. {@link NestedTypeContaining} was chosen purposely to demonstrate a variety of primitive,
 * time-related, and nested types. See corresponding AvroEncodingExampleTest for a runnable example.
 */
public class AvroEncodingExample {
  public static class ToAvro
      extends PTransform<PCollection<NestedTypeContaining>, PCollection<GenericRecord>> {

    @Override
    public PCollection<GenericRecord> expand(PCollection<NestedTypeContaining> input) {
      // Since NestedTypeContaining is annotated with @DefaultSchema, we can acquire the Schema from
      // the input.
      Schema inputSchema = input.getSchema();

      // Derive an Avro Schema from a Beam Schema.
      org.apache.avro.Schema avroSchema = AvroUtils.toAvroSchema(inputSchema);

      // The PCollection Input also provides the function to convert from NestedTypeContaining to a
      // Row representation.
      SerializableFunction<NestedTypeContaining, Row> toRowFn = input.getToRowFunction();

      // AvroUtils gives us the method that converts a Row to a GenericRecord Avro representation.
      SerializableFunction<Row, GenericRecord> toAvroFn =
          AvroUtils.getRowToGenericRecordFunction(avroSchema);

      return input
          .apply("To Row", MapElements.into(rows()).via(toRowFn))
          .apply("To Avro", MapElements.into(TypeDescriptor.of(GenericRecord.class)).via(toAvroFn));
    }
  }

  public static class FromAvro
      extends PTransform<PCollection<GenericRecord>, PCollection<NestedTypeContaining>> {
    @Override
    public PCollection<NestedTypeContaining> expand(PCollection<GenericRecord> input) {

      // The PCollection input provides us the function to convert from a GenericRecord to a Row.
      SerializableFunction<GenericRecord, Row> toRowFn = input.getToRowFunction();

      // AvroUtils gives us a method to convert from a Row to a NestedTypeContaining instance.
      SerializableFunction<Row, NestedTypeContaining> fromRowFn =
          AvroUtils.getFromRowFunction(NestedTypeContaining.class);

      return input
          .apply("To Row", MapElements.into(rows()).via(toRowFn))
          .apply(
              "From Row",
              MapElements.into(TypeDescriptor.of(NestedTypeContaining.class)).via(fromRowFn));
    }
  }
}
