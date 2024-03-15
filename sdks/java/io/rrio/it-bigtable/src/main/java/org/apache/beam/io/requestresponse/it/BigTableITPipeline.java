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
package org.apache.beam.io.requestresponse.it;

import static org.apache.beam.io.requestresponse.it.BigTableITHelper.valueOf;

import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.Mutation;
import com.google.bigtable.v2.Row;
import com.google.protobuf.ByteString;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableWriteResult;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.PeriodicImpulse;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Duration;
import org.joda.time.Instant;

@AutoValue
abstract class BigTableITPipeline {

  static Builder builder() {
    return new AutoValue_BigTableITPipeline.Builder();
  }

  abstract String getKeyPrefix();

  abstract PTransform<PBegin, PCollection<Row>> getReader();

  abstract PTransform<
          PCollection<KV<ByteString, Iterable<Mutation>>>, PCollection<BigtableWriteResult>>
      getWriter();

  abstract BigTableITOptions getOptions();

  Pipeline createPipeline() {
    if (getOptions().getReadOrWrite().equals(BigTableITHelper.ReadOrWrite.WRITE)) {
      return createWritePipeline();
    }

    return createReadPipeline();
  }

  private Pipeline createWritePipeline() {

    BigTableITOptions options = getOptions();

    Duration interval = Duration.standardSeconds(options.getGenerateDataIntervalSeconds());

    Pipeline pipeline = Pipeline.create(options);
    PCollection<Instant> impulse =
        pipeline
            .apply(
                "impulse",
                PeriodicImpulse.create()
                    .withInterval(interval)
                    .stopAfter(Duration.standardSeconds(options.getTestDurationSeconds())))
            .apply("window", Window.into(FixedWindows.of(interval)));

    BigTableITHelper.generateMutationsPerImpulse(
            options.getBigTableFamilyName(),
            valueOf(options.getElementSizePerImpulse()),
            valueOf(options.getMutationSize()),
            getKeyPrefix(),
            impulse)
        .apply("Write", getWriter())
        .apply("Count writes", BigTableITHelper.countWrites());

    return pipeline;
  }

  private Pipeline createReadPipeline() {
    Pipeline pipeline = Pipeline.create(getOptions());

    pipeline.apply("Read", getReader()).apply("Count reads", BigTableITHelper.countReads());

    return pipeline;
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setKeyPrefix(String value);

    abstract Builder setReader(PTransform<PBegin, PCollection<Row>> value);

    abstract Builder setWriter(
        PTransform<
                PCollection<KV<ByteString, Iterable<Mutation>>>, PCollection<BigtableWriteResult>>
            value);

    abstract Builder setOptions(BigTableITOptions value);

    abstract BigTableITPipeline build();
  }
}
