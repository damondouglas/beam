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

import static org.apache.beam.examples.schemas.encoding.AvroEncodingExample.FROM_AVRO_FN;
import static org.apache.beam.examples.schemas.encoding.AvroEncodingExample.TO_AVRO_FN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.util.Utf8;
import org.apache.beam.examples.schemas.model.javabeanschema.Simple;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AvroEncodingExampleTest {

  @Test
  public void avroConvertsToSimpleInstance() {
    Schema.Parser parser = new Schema.Parser();
    Schema schema =
        parser.parse(
            "{\"namespace\": \"example\",\n"
                + " \"type\": \"record\",\n"
                + " \"name\": \"Simple\",\n"
                + " \"fields\": [\n"
                + "     {\"name\": \"aBoolean\", \"type\": \"boolean\"},\n"
                + "     {\"name\": \"anInteger\", \"type\": \"int\"},\n"
                + "     {\"name\": \"aDouble\", \"type\": \"double\"},\n"
                + "     {\"name\": \"aString\", \"type\": \"string\"},\n"
                + "     {"
                + "      \"name\": \"anInstant\","
                + "      \"type\": {"
                + "         \"type\": \"long\","
                + "         \"logicalType\": \"timestamp-millis\""
                + "       }"
                + "     }"
                + " ]\n"
                + "}\n");

    GenericRecordBuilder builder = new GenericRecordBuilder(schema);
    builder.set("aBoolean", false);
    builder.set("anInteger", 1);
    builder.set("aDouble", 2.0);
    builder.set("aString", "🦄");
    builder.set("anInstant", 1257895845000L);

    GenericRecord avro = builder.build();
    Simple example = FROM_AVRO_FN.apply(avro);
    assertNotNull(example);

    assertEquals(false, example.getABoolean());
    assertEquals(Integer.valueOf(1), example.getAnInteger());
    assertEquals(Double.valueOf(2.0), example.getADouble());
    assertEquals("🦄", example.getAString());
    assertEquals(Instant.ofEpochMilli(1257895845000L), example.getAnInstant());
  }

  @Test
  public void simpleInstanceConvertsToAvro() {
    Simple example = new Simple();
    example.setABoolean(true);
    example.setADouble(1.0);
    example.setAnInteger(2);
    example.setAString("🦄");
    example.setAnInstant(Instant.ofEpochMilli(1257895845000L));

    GenericRecord avro = TO_AVRO_FN.apply(example);
    assertNotNull(avro);

    assertEquals(true, avro.get("aBoolean"));
    assertEquals(1.0, avro.get("aDouble"));
    assertEquals(2, avro.get("anInteger"));
    assertEquals(new Utf8("🦄"), avro.get("aString"));
    assertEquals(1257895845000L, avro.get("anInstant"));
  }
}
