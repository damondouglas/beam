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
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link PrimitiveTypesContaining} demonstrate the outcome of modeling a {@link Schema}
 * using {@code @DefaultSchema(AutoValueSchema.class)} for a class with only primitive types (i.e.
 * integer, boolean, etc).
 */
@RunWith(JUnit4.class)
public class PrimitiveTypesContainingTest {

  /**
   * AutoValueSchema is a {@link SchemaProvider} implementation that provides convenience methods
   * related to Schema and Row. You can also use {@link DefaultSchema.DefaultSchemaProvider} instead
   * when you want to anticipate multiple {@link SchemaProvider}s.
   */
  private static final AutoValueSchema AUTO_VALUE_SCHEMA = new AutoValueSchema();

  /**
   * The {@link TypeDescriptor} is the basis for all the {@link SchemaProvider} convenience methods.
   */
  private static final TypeDescriptor<PrimitiveTypesContaining> TYPE_DESCRIPTOR =
      TypeDescriptor.of(PrimitiveTypesContaining.class);

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
  private static final SerializableFunction<PrimitiveTypesContaining, Row> TO_ROW_FN =
      AUTO_VALUE_SCHEMA.toRowFunction(TYPE_DESCRIPTOR);

  /**
   * A {@link SchemaProvider} also provides a convenience method to convert from a Row to our java
   * class.
   */
  private static final SerializableFunction<Row, PrimitiveTypesContaining> FROM_ROW_FN =
      AUTO_VALUE_SCHEMA.fromRowFunction(TYPE_DESCRIPTOR);

  /**
   * Test illustrates that the {@link SchemaProvider} successfully generated all the expected fields
   * and their {@link Schema.FieldType}s. Notice how the field names map to {@link
   * PrimitiveTypesContaining}'s getters. You may be wondering how to customize the case of the
   * resulting names, such as snake case i.e. 'a_boolean' instead of 'aBoolean'. Refer to the
   * annotations section of the examples/java/schemas README for more information.
   */
  @Test
  public void schemaFieldsEqualPrimitiveTypesFields() {
    assertEquals(Schema.FieldType.BOOLEAN, SCHEMA.getField("aBoolean").getType());
    assertEquals(Schema.FieldType.BYTE, SCHEMA.getField("aByte").getType());
    assertEquals(Schema.FieldType.INT16, SCHEMA.getField("aShort").getType());
    assertEquals(Schema.FieldType.INT32, SCHEMA.getField("anInteger").getType());
    assertEquals(Schema.FieldType.INT64, SCHEMA.getField("aLong").getType());
    assertEquals(Schema.FieldType.FLOAT, SCHEMA.getField("aFloat").getType());
    assertEquals(Schema.FieldType.DOUBLE, SCHEMA.getField("aDouble").getType());
    assertEquals(Schema.FieldType.STRING, SCHEMA.getField("aString").getType());
  }

  /**
   * Test illustrates the conversion from an {@link PrimitiveTypesContaining} instance to a {@link
   * Row}. Normally this would take place within a {@link DoFn} or {@link MapElements#via}. Refer to
   * the section of the examples/java/schemas README where it directs usage examples in the context
   * of a {@link PTransform} and {@link DoFn}.
   */
  @Test
  public void primitiveTypesContainingInstanceConvertsToRow() {
    PrimitiveTypesContaining example =
        PrimitiveTypesContaining.builder()
            .setABoolean(false)
            .setAByte((byte) 1)
            .setAShort((short) 2)
            .setAnInteger(3)
            .setALong(4L)
            .setAFloat(5.0f)
            .setADouble(6.0)
            .setAString("🦄")
            .build();

    // Using the conversion method, we can easily instantiate a Row from our custom user type.
    Row row = TO_ROW_FN.apply(example);

    // It's possible for the conversion to fail, so we assert that the row is not null.
    assertNotNull(row);

    // These assertions validate that the Row converted successfully.
    assertEquals(false, row.getValue("aBoolean"));
    assertEquals(Byte.valueOf((byte) 1), row.getValue("aByte"));
    assertEquals(Short.valueOf((short) 2), row.getValue("aShort"));
    assertEquals(Integer.valueOf(3), row.getValue("anInteger"));
    assertEquals(Long.valueOf(4L), row.getValue("aLong"));
    assertEquals(Float.valueOf(5.0f), row.getValue("aFloat"));
    assertEquals(Double.valueOf(6.0), row.getValue("aDouble"));
    assertEquals("🦄", row.getValue("aString"));
  }

  /**
   * Test illustrates the conversion from a {@link Row} to a {@link PrimitiveTypesContaining}
   * instance. Normally this would take place within a {@link DoFn} or {@link MapElements#via}.
   * Refer to the section of the examples/java/schemas README where it directs usage examples in the
   * context of a {@link PTransform} and {@link DoFn}.
   */
  @Test
  public void rowConvertsToPrimitiveTypesContainingInstance() {
    Row row =
        Row.withSchema(SCHEMA)
            .withFieldValue("aBoolean", true)
            .withFieldValue("aByte", (byte) 1)
            .withFieldValue("aShort", (short) 2)
            .withFieldValue("anInteger", 3)
            .withFieldValue("aLong", 4L)
            .withFieldValue("aFloat", 5.0f)
            .withFieldValue("aDouble", 6.0)
            .withFieldValue("aString", "🦄")
            .build();

    // Using the conversion method, we can easily instantiate a custom user type from a Row.
    PrimitiveTypesContaining example = FROM_ROW_FN.apply(row);

    // It's possible for the conversion to fail so we assert that the user type is not null.
    assertNotNull(example);

    // These assertions validate that the custom user type converted successfully.
    assertEquals(true, example.getABoolean());
    assertEquals(Byte.valueOf((byte) 1), example.getAByte());
    assertEquals(Short.valueOf((short) 2), example.getAShort());
    assertEquals(Integer.valueOf(3), example.getAnInteger());
    assertEquals(Long.valueOf(4L), example.getALong());
    assertEquals(Float.valueOf(5.0f), example.getAFloat());
    assertEquals(Double.valueOf(6.0), example.getADouble());
    assertEquals("🦄", example.getAString());
  }
}
