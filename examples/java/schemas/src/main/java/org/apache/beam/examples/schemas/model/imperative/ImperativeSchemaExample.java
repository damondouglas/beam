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

import org.apache.beam.sdk.schemas.Schema;

/**
 * This example demonstrates modeling a {@link org.apache.beam.sdk.schemas.Schema} programmatically.
 * This approach is not recommended for real pipelines but useful for tests. This example assigns
 * single letters to field names to demonstrate field ordering in the corresponding
 * ImperativeSchemaExampleTest.
 */
public class ImperativeSchemaExample {

  private static final Schema.Field A = Schema.Field.of("a", Schema.FieldType.BOOLEAN);
  private static final Schema.Field B = Schema.Field.of("b", Schema.FieldType.INT32);
  private static final Schema.Field C = Schema.Field.of("c", Schema.FieldType.STRING);

  // An example Schema with hard coded fields.
  public static final Schema EXAMPLE_SCHEMA = Schema.of(C, A, B);
}
