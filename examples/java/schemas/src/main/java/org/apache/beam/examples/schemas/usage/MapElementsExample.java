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
package org.apache.beam.examples.schemas.usage;

import static org.apache.beam.sdk.values.TypeDescriptors.rows;

import org.apache.beam.examples.schemas.model.annotations.CaseFormatExample;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapElementsExample {
  private static final Logger LOG = LoggerFactory.getLogger(MapElementsExample.class);

  public static void main(String[] args) {
    Pipeline pipeline = Pipeline.create();

    PCollection<CaseFormatExample> examples =
        pipeline.apply(
            Create.of(
                CaseFormatExample.of("a", 1, 1.0, true, Instant.now()),
                CaseFormatExample.of("b", 2, 2.0, true, Instant.now()),
                CaseFormatExample.of("c", 3, 3.0, true, Instant.now())));

    SerializableFunction<CaseFormatExample, Row> toRowFn = examples.getToRowFunction();

    PCollection<Row> rows =
        examples
            .apply("To Row", MapElements.into(rows()).via(toRowFn))
            .setRowSchema(examples.getSchema());

    rows.apply(
        "Log",
        ParDo.of(
            new DoFn<Row, Void>() {
              @ProcessElement
              public void process(@Element Row row) {
                LOG.info(row.toString());
              }
            }));

    pipeline.run();
  }
}
