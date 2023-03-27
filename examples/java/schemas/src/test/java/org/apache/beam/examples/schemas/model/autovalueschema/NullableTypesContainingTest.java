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
package org.apache.beam.examples.schemas.model.autovalueschema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Objects;
import org.apache.beam.examples.schemas.model.imperative.ImperativeSchemaExample;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaProvider;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link NullableTypesContaining} demonstrate the outcome of modeling a {@link Schema}
 * using {@code @DefaultSchema(AutoValueSchema.class)} for a class with only primitive types (i.e.
 * integer, boolean, etc).
 */
@RunWith(JUnit4.class)
public class NullableTypesContainingTest {

  /**
   * AutoValueSchema is a {@link SchemaProvider} implementation that provides convenience methods
   * related to Schema and Row. You can also use {@link DefaultSchema.DefaultSchemaProvider} instead
   * when you want to anticipate multiple {@link SchemaProvider}s.
   */
  private static final AutoValueSchema AUTO_VALUE_SCHEMA = new AutoValueSchema();

  /**
   * The {@link TypeDescriptor} is the basis for all the {@link SchemaProvider} convenience methods.
   */
  private static final TypeDescriptor<NullableTypesContaining> TYPE_DESCRIPTOR =
      TypeDescriptor.of(NullableTypesContaining.class);

  /**
   * Model the {@link Schema} from the {@link TypeDescriptor} using the {@link AutoValueSchema}
   * {@link SchemaProvider}. Compare this to the {@link ImperativeSchemaExample} where we hard coded
   * the fields in the {@link Schema}.
   */
  private static final Schema SCHEMA =
      Objects.requireNonNull(AUTO_VALUE_SCHEMA.schemaFor(TYPE_DESCRIPTOR));

  /**
   * A {@link SchemaProvider} provides a convenience method to convert from a our java class to a
   * {@link Row}.
   */
  private static final SerializableFunction<NullableTypesContaining, Row> TO_ROW_FN =
      AUTO_VALUE_SCHEMA.toRowFunction(TYPE_DESCRIPTOR);

  /**
   * A {@link SchemaProvider} also provides a convenience method to convert from a Row to our java
   * class.
   */
  private static final SerializableFunction<Row, NullableTypesContaining> FROM_ROW_FN =
      AUTO_VALUE_SCHEMA.fromRowFunction(TYPE_DESCRIPTOR);

  @Test
  public void exampleGeneratedSchemaShowsUserTypeClassFields() {
    assertEquals(
        Schema.FieldType.BOOLEAN.withNullable(true), SCHEMA.getField("aBoolean").getType());
    assertEquals(Schema.FieldType.BYTE.withNullable(true), SCHEMA.getField("aByte").getType());
    assertEquals(Schema.FieldType.INT16.withNullable(true), SCHEMA.getField("aShort").getType());
    assertEquals(Schema.FieldType.INT32.withNullable(true), SCHEMA.getField("anInteger").getType());
    assertEquals(Schema.FieldType.INT64.withNullable(true), SCHEMA.getField("aLong").getType());
    assertEquals(Schema.FieldType.FLOAT.withNullable(true), SCHEMA.getField("aFloat").getType());
    assertEquals(Schema.FieldType.DOUBLE.withNullable(true), SCHEMA.getField("aDouble").getType());
    assertEquals(Schema.FieldType.STRING.withNullable(true), SCHEMA.getField("aString").getType());
  }

  @Test
  public void exampleToRow() {
    NullableTypesContaining example =
        NullableTypesContaining.builder()
            .setABoolean(false)
            .setAByte((byte) 1)
            .setAShort((short) 2)
            .setAnInteger(3)
            .setALong(4L)
            .setAFloat(5.0f)
            .setADouble(6.0)
            .setAString("a")
            .build();

    // Using the conversion method, we can easily instantiate a Row from our custom user type.
    Row row = TO_ROW_FN.apply(example);

    // It's possible for the conversion to fail so we assert that the row is not null.
    assertNotNull(row);

    // These assertions validate that the Row converted successfully.
    assertEquals(example.getABoolean(), row.getValue("aBoolean"));
    assertEquals(example.getAByte(), row.getValue("aByte"));
    assertEquals(example.getAShort(), row.getValue("aShort"));
    assertEquals(example.getAnInteger(), row.getValue("anInteger"));
    assertEquals(example.getALong(), row.getValue("aLong"));
    assertEquals(example.getAFloat(), row.getValue("aFloat"));
    assertEquals(example.getADouble(), row.getValue("aDouble"));
    assertEquals(example.getAString(), row.getValue("aString"));
  }

  @Test
  public void exampleFromRow() {
    Row row =
        Row.withSchema(SCHEMA)
            .withFieldValue("aBoolean", true)
            .withFieldValue("aByte", (byte) 1)
            .withFieldValue("aShort", (short) 2)
            .withFieldValue("anInteger", 3)
            .withFieldValue("aLong", 4L)
            .withFieldValue("aFloat", 5.0f)
            .withFieldValue("aDouble", 6.0)
            .withFieldValue("aString", "a")
            .build();

    // Using the conversion method, we can easily instantiate a custom user type from a Row.
    NullableTypesContaining example = FROM_ROW_FN.apply(row);

    // It's possible for the conversion to fail so we assert that the user type is not null.
    assertNotNull(example);

    // These assertions validate that the custom user type converted successfully.
    assertEquals(row.getValue("aBoolean"), example.getABoolean());
    assertEquals(row.getValue("aByte"), example.getAByte());
    assertEquals(row.getValue("aShort"), example.getAShort());
    assertEquals(row.getValue("anInteger"), example.getAnInteger());
    assertEquals(row.getValue("aLong"), example.getALong());
    assertEquals(row.getValue("aFloat"), example.getAFloat());
    assertEquals(row.getValue("aDouble"), example.getADouble());
    assertEquals(row.getValue("aString"), example.getAString());
  }
}
