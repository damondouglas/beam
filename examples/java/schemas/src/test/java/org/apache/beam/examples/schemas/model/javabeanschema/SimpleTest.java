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
package org.apache.beam.examples.schemas.model.javabeanschema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import org.apache.beam.sdk.schemas.JavaBeanSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaProvider;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.joda.time.Instant;
import org.junit.Test;

/**
 * Tests for {@link Simple} demonstrating the resulting modeled {@link Schema} and {@link Row}
 * conversions.
 */
public class SimpleTest {

  /**
   * We use {@link JavaBeanSchema} to model the {@link Schema} and {@link Row} conversion
   * convenience methods. Alternatively, we can use {@link DefaultSchema.DefaultSchemaProvider} if
   * we are uncertain of the annotated {@link SchemaProvider} type.
   */
  private static final JavaBeanSchema JAVA_BEAN_SCHEMA = new JavaBeanSchema();

  /**
   * A {@link TypeDescriptor} is the basis for all methods required by the {@link SchemaProvider}.
   */
  private static final TypeDescriptor<Simple> TYPE_DESCRIPTOR = TypeDescriptor.of(Simple.class);

  /**
   * Model {@link Simple}'s {@link Schema} using the {@link SchemaProvider}'s convenience method and
   * its {@link TypeDescriptor}.
   */
  private static final Schema SCHEMA = JAVA_BEAN_SCHEMA.schemaFor(TYPE_DESCRIPTOR);

  /**
   * {@link SchemaProvider} provides a convenience method to convert from a {@link Simple} instance
   * to a {@link Row} using {@link Simple}'s {@link TypeDescriptor}.
   */
  private static final SerializableFunction<Simple, Row> TO_ROW_FN =
      JAVA_BEAN_SCHEMA.toRowFunction(TYPE_DESCRIPTOR);

  /**
   * {@link SchemaProvider} provides a convenience method to convert from a {@link Row} to a {@link
   * Simple} instance using {@link Simple}'s {@link TypeDescriptor}.
   */
  private static final SerializableFunction<Row, Simple> FROM_ROW_FN =
      JAVA_BEAN_SCHEMA.fromRowFunction(TYPE_DESCRIPTOR);

  /**
   * The {@link SchemaProvider} provides a convenient function to convert from a {@link Simple}
   * instance to a {@link Row}.
   */
  @Test
  public void exampleSchemaContainsExpectedFields() {
    //  NOTICE the need for sorted() to guarantee the order of returned {@link
    // Schema#getFieldNames}.
    assertEquals(
        Arrays.asList("aBoolean", "aDouble", "aString", "anInstant", "anInteger"),
        SCHEMA.sorted().getFieldNames());

    // Assertions demonstrate that the SchemaProvider derived the correct type based on Simple's
    // field types.
    assertEquals(Schema.FieldType.BOOLEAN, SCHEMA.getField("aBoolean").getType());
    assertEquals(Schema.FieldType.DOUBLE, SCHEMA.getField("aDouble").getType());
    assertEquals(Schema.FieldType.STRING, SCHEMA.getField("aString").getType());
    assertEquals(Schema.FieldType.DATETIME, SCHEMA.getField("anInstant").getType());
    assertEquals(Schema.FieldType.INT32, SCHEMA.getField("anInteger").getType());
  }

  /**
   * Test illustrates converting from a {@link Simple} instance to a {@link Row}. Normally this
   * would take place in the context of a {@link DoFn} or {@link MapElements#via}, for example.
   * Refer to README at examples/java/schemas.
   */
  @Test
  public void exampleToRow() {
    Simple example = new Simple();
    example.setABoolean(true);
    example.setADouble(1.0);
    example.setAnInteger(2);
    example.setAString("a");
    example.setAnInstant(Instant.now());

    // Using the to row conversion function, we can convert the Simple instance to a Row.
    Row row = TO_ROW_FN.apply(example);

    // The conversion may not be successful, so we need to assert that the row is not null.
    assertNotNull(row);

    // We assert that the correct values were copied during the conversion.
    assertEquals(example.getABoolean(), row.getValue("aBoolean"));
    assertEquals(example.getAnInteger(), row.getValue("anInteger"));
    assertEquals(example.getADouble(), row.getValue("aDouble"));
    assertEquals(example.getAString(), row.getValue("aString"));
    assertEquals(example.getAnInstant(), row.getValue("anInstant"));
  }

  /**
   * Test illustrates converting from a {@link Row} to a {@link Simple} instance. Normally this
   * would take place in the context of a {@link DoFn} or {@link MapElements#via}, for example.
   * Refer to README at examples/java/schemas.
   */
  @Test
  public void exampleFromRow() {
    Row row =
        Row.withSchema(SCHEMA)
            .withFieldValue("aBoolean", true)
            .withFieldValue("aDouble", 1.0)
            .withFieldValue("anInteger", 2)
            .withFieldValue("aString", "a")
            .withFieldValue("anInstant", Instant.now())
            .build();

    // Using the to row conversion function, we can convert a Row instance to a Simple instance.
    Simple example = FROM_ROW_FN.apply(row);

    // The conversion may not be successful, so we need to assert that the instance is not null.
    assertNotNull(example);

    // We assert that the correct values were copied during the conversion.
    assertEquals(row.getValue("aBoolean"), example.getABoolean());
    assertEquals(row.getValue("aDouble"), example.getADouble());
    assertEquals(row.getValue("anInteger"), example.getAnInteger());
    assertEquals(row.getValue("aString"), example.getAString());
    assertEquals(row.getValue("anInstant"), example.getAnInstant());
  }
}
