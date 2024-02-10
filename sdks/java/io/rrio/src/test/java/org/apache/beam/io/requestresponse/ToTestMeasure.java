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
package org.apache.beam.io.requestresponse;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import com.google.auto.value.AutoValue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.coders.VarLongCoder;
import org.apache.beam.sdk.extensions.avro.coders.AvroGenericCoder;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PCollection;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.joda.time.Instant;

@AutoValue
abstract class ToTestMeasure<T> extends PTransform<PCollection<T>, PCollection<GenericRecord>> {

  static <T> Builder<T> builder() {
    return new AutoValue_ToTestMeasure.Builder<>();
  }

  abstract SerializableFunction<T, GenericRecord> getToGenericRecordFn();

  abstract String getGenericRecordSchema();

  abstract SerializableFunction<T, Instant> getToTimestampFn();

  abstract SerializableFunction<PipelineOptions, GenericRecord> getMetadataSupplier();

  abstract String getMetadataSchema();

  @Override
  public PCollection<GenericRecord> expand(PCollection<T> input) {
    Schema schema = TestMeasure.buildSchemaOf(getGenericRecordSchema(), getMetadataSchema());
    return input
        .apply(ToTestMeasureFn.class.getSimpleName(), ParDo.of(new ToTestMeasureFn()))
        .setCoder(TestMeasureCoder.of(getGenericRecordSchema(), getMetadataSchema()))
        .apply(ToGenericRecordFn.class.getSimpleName(), ParDo.of(new ToGenericRecordFn()))
        .setCoder(AvroGenericCoder.of(schema));
  }

  private class ToTestMeasureFn extends DoFn<T, TestMeasure> {

    @ProcessElement
    public void process(
        @Element T element, OutputReceiver<TestMeasure> receiver, PipelineOptions options) {
      receiver.output(
          TestMeasure.builder()
              .setValue(getToGenericRecordFn().apply(element))
              .setMetadata(getMetadataSupplier().apply(options))
              .setTimestamp(getToTimestampFn().apply(element))
              .build());
    }
  }

  @AutoValue.Builder
  abstract static class Builder<T> {

    abstract Builder<T> setToGenericRecordFn(SerializableFunction<T, GenericRecord> value);

    abstract Builder<T> setGenericRecordSchema(String value);

    abstract Builder<T> setToTimestampFn(SerializableFunction<T, Instant> value);

    abstract Optional<SerializableFunction<T, Instant>> getToTimestampFn();

    abstract Builder<T> setMetadataSupplier(
        SerializableFunction<PipelineOptions, GenericRecord> value);

    abstract Optional<SerializableFunction<PipelineOptions, GenericRecord>> getMetadataSupplier();

    abstract Builder<T> setMetadataSchema(String value);

    abstract ToTestMeasure<T> autoBuild();

    final ToTestMeasure<T> build() {
      if (!getToTimestampFn().isPresent()) {
        setToTimestampFn(ignored -> Instant.now());
      }
      if (!getMetadataSupplier().isPresent()) {
        setMetadataSupplier(
            options ->
                new GenericRecordBuilder(
                        SchemaBuilder.builder().record("metadata").fields().endRecord())
                    .build());
      }
      return autoBuild();
    }
  }

  private class ToGenericRecordFn extends DoFn<TestMeasure, GenericRecord> {
    private transient @MonotonicNonNull Schema schema;

    @Setup
    public void setup() {
      schema = TestMeasure.buildSchemaOf(getGenericRecordSchema(), getMetadataSchema());
    }

    @ProcessElement
    public void process(@Element TestMeasure element, OutputReceiver<GenericRecord> receiver) {
      GenericRecord record = element.toAvro(checkStateNotNull(schema));
      receiver.output(record);
    }
  }

  private static class TestMeasureCoder extends CustomCoder<TestMeasure> {
    static TestMeasureCoder of(String valueSchema, String metadataSchema) {
      Schema.Parser parser = new Schema.Parser();
      return new TestMeasureCoder(parser.parse(valueSchema), parser.parse(metadataSchema));
    }

    private static final VarLongCoder TIMESTAMP_CODER = VarLongCoder.of();
    private final AvroGenericCoder valueCoder;
    private final AvroGenericCoder metadataCoder;

    private TestMeasureCoder(Schema valueSchema, Schema metadataSchema) {
      valueCoder = AvroGenericCoder.of(valueSchema);
      metadataCoder = AvroGenericCoder.of(metadataSchema);
    }

    @Override
    public void encode(TestMeasure element, OutputStream outStream)
        throws CoderException, IOException {
      valueCoder.encode(element.getValue(), outStream);
      metadataCoder.encode(element.getMetadata(), outStream);
      TIMESTAMP_CODER.encode(element.getTimestamp().getMillis(), outStream);
    }

    @Override
    public TestMeasure decode(InputStream inStream) throws CoderException, IOException {
      GenericRecord value = valueCoder.decode(inStream);
      GenericRecord metadata = metadataCoder.decode(inStream);
      long timestampMillis = TIMESTAMP_CODER.decode(inStream);
      Instant timestamp = new Instant(timestampMillis);
      return TestMeasure.builder()
          .setValue(value)
          .setMetadata(metadata)
          .setTimestamp(timestamp)
          .build();
    }
  }
}
