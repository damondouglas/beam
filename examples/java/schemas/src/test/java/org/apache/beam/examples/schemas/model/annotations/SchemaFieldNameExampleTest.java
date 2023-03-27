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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SchemaFieldNameExampleTest {
  private static final AutoValueSchema SCHEMA_PROVIDER = new AutoValueSchema();

  private static final TypeDescriptor<SchemaFieldNameExample> TYPE_DESCRIPTOR =
      TypeDescriptor.of(SchemaFieldNameExample.class);

  private static final Schema SCHEMA =
      Objects.requireNonNull(SCHEMA_PROVIDER.schemaFor(TYPE_DESCRIPTOR));

  private static final SerializableFunction<SchemaFieldNameExample, Row> TO_ROW_FN =
      SCHEMA_PROVIDER.toRowFunction(TYPE_DESCRIPTOR);

  private static final SerializableFunction<Row, SchemaFieldNameExample> FROM_ROW_FN =
      SCHEMA_PROVIDER.fromRowFunction(TYPE_DESCRIPTOR);

  @Test
  public void schemaFieldNamesMatchSchemaFieldNameAnnotation() {
    assertEquals(Arrays.asList("some_double", "some_integer"), SCHEMA.sorted().getFieldNames());
  }

  @Test
  public void toRowFieldNamesMatchSchemaFieldNameAnnotation() {
    SchemaFieldNameExample example =
        SchemaFieldNameExample.builder().setAnInteger(1).setADouble(2.0).build();
    Row row = TO_ROW_FN.apply(example);
    assertNotNull(row);

    assertEquals(Integer.valueOf(1), row.getValue("some_integer"));
    assertEquals(Double.valueOf(2.0), row.getValue("some_double"));
  }

  @Test
  public void fromRowFieldNamesMatchSchemaFieldNameAnnotation() {
    Row row =
        Row.withSchema(SCHEMA)
            .withFieldValue("some_integer", 1)
            .withFieldValue("some_double", 2.0)
            .build();
    SchemaFieldNameExample example = FROM_ROW_FN.apply(row);
    assertNotNull(example);

    assertEquals(Integer.valueOf(1), example.getAnInteger());
    assertEquals(Double.valueOf(2.0), example.getADouble());
  }
}
