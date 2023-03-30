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
package org.apache.beam.examples.schemas.model.imperative;

import static org.apache.beam.examples.schemas.model.imperative.ImperativeSchemaExample.EXAMPLE_SCHEMA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

/** Tests for {@link ImperativeSchemaExample} demonstrating various {@link Schema} methods. */
public class ImperativeSchemaExampleTest {

  /**
   * Test demonstrates {@link Schema.Field} order based on {@link Schema#of} usage. Schema field
   * order is something to pay attention to when writing test assertions and troubleshooting why
   * field orders do not appear as expected.
   */
  @Test
  public void exampleSchemaFieldOrderFollowsSchemaOf() {
    // Compare these results with the EXAMPLE_SCHEMA instantiation and notice how Schema.of
    // determines the order.
    assertEquals(Arrays.asList("c", "a", "b"), EXAMPLE_SCHEMA.getFieldNames());

    // Similarly, notice how getFields obeys the Schema.of order.
    List<String> fieldNamesInFieldOrder = new ArrayList<>();
    for (Schema.Field field : EXAMPLE_SCHEMA.getFields()) {
      fieldNamesInFieldOrder.add(field.getName());
    }
    assertEquals(Arrays.asList("c", "a", "b"), fieldNamesInFieldOrder);

    // A Schema stores encoded positions and also obeys the Schema.of placement order.
    Map<String, Integer> encodedPositions = EXAMPLE_SCHEMA.getEncodingPositions();
    assertEquals(Integer.valueOf(0), encodedPositions.get("c"));
    assertEquals(Integer.valueOf(1), encodedPositions.get("a"));
    assertEquals(Integer.valueOf(2), encodedPositions.get("b"));
  }

  /**
   * Test demonstrates {@link Schema#setEncodingPositions} and its impact on {@link Schema}'s field
   * order.
   */
  @Test
  public void exampleSchemaWithEncodedPositionsDoesNotChangeFieldOrder() {
    // We first copy the original EXAMPLE_SCHEMA.
    Schema schema = Schema.builder().addFields(EXAMPLE_SCHEMA.getFields()).build();

    // Assert that the two schemas encoding positions are equals.
    assertEquals(EXAMPLE_SCHEMA.getEncodingPositions(), schema.getEncodingPositions());

    // Assert that the copied Schema has the same field order.
    assertEquals(Arrays.asList("c", "a", "b"), schema.getFieldNames());

    // Invoke the setEncodingPositions to change the encoding position order.
    schema.setEncodingPositions(
        ImmutableMap.of(
            "a", 0,
            "b", 1,
            "c", 2));

    // Assert that the new encoding positions differ from the original.
    assertNotEquals(EXAMPLE_SCHEMA.getEncodingPositions(), schema.getEncodingPositions());

    // Notice that the field order from getFieldsNames remains as the original, UNCHANGED from
    // setEncodingPositions.
    assertEquals(Arrays.asList("c", "a", "b"), schema.getFieldNames());

    // Assert that we actually changed the encoded positions. This is a silly assertion but helpful
    // to see in the
    // context of the previous UNCHANGED getFieldsNames result.
    Map<String, Integer> encodedPositions = schema.getEncodingPositions();
    assertEquals(Integer.valueOf(0), encodedPositions.get("a"));
    assertEquals(Integer.valueOf(1), encodedPositions.get("b"));
    assertEquals(Integer.valueOf(2), encodedPositions.get("c"));

    // Also, notice that the two schemas are equal.
    assertEquals(EXAMPLE_SCHEMA, schema);
  }

  /**
   * Test demonstrates that {@link Schema#sorted} changes {@link Schema#getFieldNames} output and
   * not the encoding positions.
   */
  @Test
  public void exampleSchemaAsSortedChangesGetFieldNamesOrder() {
    // We first copy the original EXAMPLE_SCHEMA with sorted field names.
    Schema schema = EXAMPLE_SCHEMA.sorted();

    // Notice that the copied Schema with sorted field names outputs in alphabetical order.
    assertEquals(Arrays.asList("a", "b", "c"), schema.getFieldNames());

    // Yet, also notice that sorted() does not change the encoding positions.
    assertEquals(EXAMPLE_SCHEMA.getEncodingPositions(), schema.getEncodingPositions());

    // Also, notice that the despite having the same fields, they assert as not equal. This is an
    // important
    // observation when troubleshooting failing tests.
    assertNotEquals(EXAMPLE_SCHEMA, schema);

    // In tests with uncertain field order, use the equivalent method.
    assertTrue(schema.equivalent(EXAMPLE_SCHEMA));
  }
}
