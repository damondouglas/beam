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

import com.google.bigtable.v2.Mutation;
import com.google.bigtable.v2.Row;
import com.google.bigtable.v2.RowFilter;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.coders.IterableCoder;
import org.apache.beam.sdk.coders.KvCoder;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.extensions.protobuf.ByteStringCoder;
import org.apache.beam.sdk.io.gcp.bigquery.RowMutation;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableWriteResult;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Instant;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableIO;

public final class BigTableITHelper {

  private static final Class<?> COUNTER_CLASS = BigTableITPipeline.class;

  private static final Counter COUNTER_GENERATE =
      Metrics.counter(COUNTER_CLASS, "num_elements_generated");
  private static final Counter COUNTER_WRITE =
      Metrics.counter(COUNTER_CLASS, "num_elements_written");
  private static final Counter COUNTER_READ = Metrics.counter(COUNTER_CLASS, "num_elements_read");

  static PCollection<KV<ByteString, Iterable<Mutation>>> generateMutationsPerImpulse(
      String familyName, int numPerImpulse, int sizePerMutation, String keyPrefix, PCollection<Instant> impulse) {
    return impulse
        .apply(
            "generateMutations",
            ParDo.of(new GenerateMutationFn(familyName, numPerImpulse, sizePerMutation, keyPrefix)));
  }

  private static class GenerateMutationFn
      extends DoFn<Instant, KV<ByteString, Iterable<Mutation>>> {

    private static final Random RANDOM = new Random();

    private final String familyName;
    private final int numPerImpulse;
    private final int sizePerMutation;

    private final String keyPrefix;

    private GenerateMutationFn(String familyName, int numPerImpulse, int sizePerMutation, String keyPrefix) {
      this.familyName = familyName;
      this.numPerImpulse = numPerImpulse;
      this.sizePerMutation = sizePerMutation;
        this.keyPrefix = keyPrefix;
    }

    @ProcessElement
    public void process(
        @Element Instant element, OutputReceiver<KV<ByteString, Iterable<Mutation>>> receiver) {
      byte[] valBytes = new byte[sizePerMutation];
      RANDOM.nextBytes(valBytes);
      ByteString value = ByteString.copyFrom(valBytes);
      List<Mutation> result = new ArrayList<>();
      ByteString key = ByteString.copyFromUtf8(keyPrefix + "#" + UUID.randomUUID().toString());
      for (int i = 0; i < numPerImpulse; i++) {
        result.add(
            Mutation.newBuilder()
                    .setSetCell(Mutation.SetCell.newBuilder()
                            .setTimestampMicros(element.getMillis() * 1_000L)
                            .setFamilyName(familyName)
                            .setValue(value).build())
                .build());
      }
      COUNTER_GENERATE.inc(numPerImpulse);
      receiver.output(KV.of(key, result));
    }
  }

  static ParDo.SingleOutput<Row, Row> countReads() {
    return ParDo.of(new ReadCounterFn());
  }

  private static class ReadCounterFn extends DoFn<Row, Row> {
    @ProcessElement
    public void process(@Element Row row, OutputReceiver<Row> receiver) {
      COUNTER_READ.inc();
      receiver.output(row);
    }
  }

  static ParDo.SingleOutput<BigtableWriteResult, BigtableWriteResult> countWrites() {
    return ParDo.of(new WriteCounterFn());
  }

  private static class WriteCounterFn extends DoFn<BigtableWriteResult, BigtableWriteResult> {
    @ProcessElement
    public void process(
        @Element BigtableWriteResult result, OutputReceiver<BigtableWriteResult> receiver) {
      COUNTER_WRITE.inc(result.getRowsWritten());
      receiver.output(result);
    }
  }

  public enum Size {
    LOCAL,
    SMALL,
    MEDIUM,
    LARGE,
  }

  static int valueOf(Size size) {
    switch (size) {
      case LARGE:
        return 100_000_000;
      case MEDIUM:
        return 10_000_000;
      case SMALL:
        return 1_000_000;
      case LOCAL:
        return 1_000;
    }
    throw new IllegalArgumentException("no value maps to " + size);
  }

  public enum Connector {
    INHERENT,
    RRIO,
  }

  static PTransform<PBegin, PCollection<Row>> readerOf(BigTableITOptions options, String keyPrefix) {
    Connector connector = options.getConnector();
    switch (connector) {
      case RRIO:
        return BigTableIOUsingRRIO.read()
                .withProjectId(options.as(GcpOptions.class).getProject())
                .withInstanceId(options.getInstanceId())
                .withTableId(options.getTableId())
                .withRowFilter(rowFilterOf(keyPrefix));

      case INHERENT:
        return BigtableIO.read()
                .withProjectId(options.as(GcpOptions.class).getProject())
                .withInstanceId(options.getInstanceId())
                .withTableId(options.getTableId())
                .withRowFilter(rowFilterOf(keyPrefix));
    }
    throw new IllegalArgumentException("no value maps to " + connector);
  }

  static PTransform<PCollection<KV<ByteString, Iterable<Mutation>>>, PCollection<BigtableWriteResult>> writerOf(BigTableITOptions options) {
    Connector connector = options.getConnector();
    switch (connector) {
      case RRIO:
        return BigTableIOUsingRRIO.write()
                .withProjectId(options.as(GcpOptions.class).getProject())
                .withInstanceId(options.getInstanceId())
                .withTableId(options.getTableId());

      case INHERENT:
        return BigtableIO.write()
                .withProjectId(options.as(GcpOptions.class).getProject())
                .withInstanceId(options.getInstanceId())
                .withTableId(options.getTableId())
                .withWriteResults();
    }
    throw new IllegalArgumentException("no value maps to " + connector);
  }

  private static RowFilter rowFilterOf(String keyPrefix) {
    return RowFilter.newBuilder()
            .setRowKeyRegexFilter(ByteString.copyFromUtf8(String.format("^%s.*", keyPrefix)))
            .build();
  }
}
