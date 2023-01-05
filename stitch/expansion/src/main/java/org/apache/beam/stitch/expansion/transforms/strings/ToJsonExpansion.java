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
package org.apache.beam.stitch.expansion.transforms.strings;

import static org.apache.beam.sdk.values.TypeDescriptors.strings;

import java.nio.charset.StandardCharsets;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.io.payloads.JsonPayloadSerializerProvider;
import org.apache.beam.sdk.schemas.io.payloads.PayloadSerializer;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableMap;

public class ToJsonExpansion extends PTransform<PCollection<Row>, PCollection<String>> {
  @Override
  public PCollection<String> expand(PCollection<Row> input) {
    Schema schema = input.getSchema();
    PayloadSerializer payloadSerializer =
        new JsonPayloadSerializerProvider().getSerializer(schema, ImmutableMap.of());
    return input.apply(
        "To Json",
        MapElements.into(strings())
            .via(
                (Row element) ->
                    new String(payloadSerializer.serialize(element), StandardCharsets.UTF_8)));
  }
}
