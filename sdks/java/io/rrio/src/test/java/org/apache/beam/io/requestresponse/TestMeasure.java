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

import com.google.auto.value.AutoValue;
import java.io.Serializable;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.joda.time.Instant;

@AutoValue
abstract class TestMeasure implements Serializable {

  private static final String VALUE = "value";
  private static final String TIMESTAMP = "timestamp";

  private static final String METADATA = "metadata";

  static Schema buildSchemaOf(Schema valueSchema, Schema metadataSchema) {
    return SchemaBuilder.record(TestMeasure.class.getSimpleName())
        .fields()
        .name(VALUE)
        .type(valueSchema)
        .noDefault()
        .requiredLong(TIMESTAMP)
        .name(METADATA)
        .type(metadataSchema)
        .noDefault()
        .endRecord();
  }

  static Schema buildSchemaOf(String valueSchema, String metadataSchema) {
    Schema.Parser parser = new Schema.Parser();
    return buildSchemaOf(parser.parse(valueSchema), parser.parse(metadataSchema));
  }

  static Builder builder() {
    return new AutoValue_TestMeasure.Builder();
  }

  abstract GenericRecord getValue();

  abstract Instant getTimestamp();

  abstract GenericRecord getMetadata();

  GenericRecord toAvro(Schema schema) {
    return new GenericRecordBuilder(schema)
        .set(VALUE, getValue())
        .set(TIMESTAMP, getTimestamp().getMillis())
        .set(METADATA, getMetadata())
        .build();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(GenericRecord value);

    abstract Builder setTimestamp(Instant value);

    abstract Builder setMetadata(GenericRecord value);

    abstract TestMeasure build();
  }
}
