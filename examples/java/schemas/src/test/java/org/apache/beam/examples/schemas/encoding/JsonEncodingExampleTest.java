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

import static org.apache.beam.examples.schemas.encoding.JsonEncodingExample.FROM_JSON_FN;
import static org.apache.beam.examples.schemas.encoding.JsonEncodingExample.TO_JSON_FN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.beam.examples.schemas.model.annotations.CaseFormatExample;
import org.apache.beam.vendor.grpc.v1p48p1.com.google.gson.Gson;
import org.apache.beam.vendor.grpc.v1p48p1.com.google.gson.JsonObject;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JsonEncodingExampleTest {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.dateTime();

  @Test
  public void jsonConvertsToCaseFormatExampleInstance() {
    String json =
        "{"
            + "\"a_boolean\": true,"
            + "\"an_integer\": 1,"
            + "\"a_double\": 2.0,"
            + "\"a_string\": \"🦄\","
            + "\"an_instant\": \"2009-11-10T23:30:45Z\""
            + "}";

    CaseFormatExample example = FROM_JSON_FN.apply(json);

    assertNotNull(example);

    assertEquals(true, example.getABoolean());
    assertEquals(Integer.valueOf(1), example.getAnInteger());
    assertEquals(Double.valueOf(2.0), example.getADouble());
    assertEquals("🦄", example.getAString());
    assertEquals(1257895845000L, example.getAnInstant().getMillis());
    assertEquals("2009-11-10T23:30:45.000Z", example.getAnInstant().toString(DATE_TIME_FORMATTER));
  }

  @Test
  public void caseFormatExampleInstanceConvertsToJson() {
    CaseFormatExample example =
        CaseFormatExample.builder()
            .setABoolean(true)
            .setAnInteger(1)
            .setADouble(2.0)
            .setAString("🦄")
            .setAnInstant(Instant.ofEpochMilli(1257895845000L))
            .build();

    Gson gson = new Gson();
    String json = TO_JSON_FN.apply(example);
    JsonObject actual = gson.fromJson(json, JsonObject.class);
    assertEquals(true, actual.get("a_boolean").getAsBoolean());
    assertEquals(1, actual.get("an_integer").getAsInt());
    assertEquals(2.0, actual.get("a_double").getAsDouble(), 0.0);
    assertEquals("🦄", actual.get("a_string").getAsString());
    assertEquals("2009-11-10T23:30:45.000Z", actual.get("an_instant").getAsString());
  }
}
