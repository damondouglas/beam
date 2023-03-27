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
package org.apache.beam.examples.schemas.model.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Objects;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CaseFormatExampleTest {

  private static final AutoValueSchema SCHEMA_PROVIDER = new AutoValueSchema();

  private static final TypeDescriptor<CaseFormatExample> TYPE_DESCRIPTOR =
      TypeDescriptor.of(CaseFormatExample.class);

  private static final Schema SCHEMA =
      Objects.requireNonNull(SCHEMA_PROVIDER.schemaFor(TYPE_DESCRIPTOR));

  private static final SerializableFunction<CaseFormatExample, Row> TO_ROW_FN =
      SCHEMA_PROVIDER.toRowFunction(TYPE_DESCRIPTOR);

  private static final SerializableFunction<Row, CaseFormatExample> FROM_ROW_FN =
      SCHEMA_PROVIDER.fromRowFunction(TYPE_DESCRIPTOR);

  @Test
  public void schemaFieldsInLowerUnderscoreCaseFormat() {
    assertEquals(
        Arrays.asList("a_boolean", "a_double", "a_string", "an_instant", "an_integer"),
        SCHEMA.sorted().getFieldNames());
  }

  @Test
  public void caseFormatExampleInstanceConvertsToRow() {
    CaseFormatExample example =
        CaseFormatExample.builder()
            .setABoolean(true)
            .setADouble(1.0)
            .setAnInteger(2)
            .setAString("🦄")
            .setAnInstant(Instant.ofEpochSecond(1670000000))
            .build();

    Row row = TO_ROW_FN.apply(example);
    assertNotNull(row);

    assertEquals(true, row.getValue("a_boolean"));
    assertEquals(Double.valueOf(1.0), row.getValue("a_double"));
    assertEquals(Integer.valueOf(2), row.getValue("an_integer"));
    assertEquals("🦄", row.getValue("a_string"));
    assertEquals(Instant.ofEpochSecond(1670000000), row.getValue("an_instant"));
  }

  @Test
  public void rowConvertsToCaseFormatExampleInstance() {
    Row row =
        Row.withSchema(SCHEMA)
            .withFieldValue("a_boolean", true)
            .withFieldValue("a_double", 1.0)
            .withFieldValue("an_integer", 2)
            .withFieldValue("a_string", "🦄")
            .withFieldValue("an_instant", Instant.ofEpochSecond(1670000000))
            .build();

    CaseFormatExample example = FROM_ROW_FN.apply(row);
    assertNotNull(example);

    assertEquals(true, example.getABoolean());
    assertEquals(Double.valueOf(1.0), example.getADouble());
    assertEquals(Integer.valueOf(2), example.getAnInteger());
    assertEquals("🦄", example.getAString());
    assertEquals(Instant.ofEpochSecond(1670000000), example.getAnInstant());
  }
}
