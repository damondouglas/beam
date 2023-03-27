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

import org.apache.beam.examples.schemas.model.javabeanschema.Simple;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldAccessExample {
  private static final Logger LOG = LoggerFactory.getLogger(FieldAccessExample.class);

  public static void main(String[] args) {
    Pipeline pipeline = Pipeline.create();

    PCollection<Simple> example =
        pipeline.apply(
            Create.of(
                Simple.of(true, 1, 1.0, "a", Instant.parse("2023-01-01T01:00:00Z")),
                Simple.of(false, 2, 2.0, "b", Instant.parse("2023-01-02T01:00:00Z")),
                Simple.of(true, 3, 3.0, "c", Instant.parse("2023-01-03T01:00:00Z")),
                Simple.of(false, 4, 4.0, "d", Instant.parse("2023-01-04T01:00:00Z"))));

    example.apply("select aBoolean", ParDo.of(new SelectABooleanFn()));
    example.apply("select anInteger", ParDo.of(new SelectAnIntegerFn()));

    pipeline.run();
  }

  private static class SelectABooleanFn extends DoFn<Simple, Void> {
    @ProcessElement
    public void process(
        @FieldAccess("aString") String aString, @FieldAccess("aBoolean") Boolean aBoolean) {
      LOG.info("select 'aBoolean': {}: {}", aString, aBoolean);
    }
  }

  private static class SelectAnIntegerFn extends DoFn<Simple, Void> {
    @ProcessElement
    public void process(
        @FieldAccess("aString") String aString, @FieldAccess("anInteger") Integer anInteger) {
      LOG.info("select 'anInteger': {}: {}", aString, anInteger);
    }
  }
}
