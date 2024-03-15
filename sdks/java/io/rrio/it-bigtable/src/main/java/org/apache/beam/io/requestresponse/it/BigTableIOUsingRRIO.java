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

import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.Mutation;
import com.google.bigtable.v2.Row;
import com.google.bigtable.v2.RowFilter;
import com.google.protobuf.ByteString;
import java.io.Serializable;
import org.apache.beam.io.requestresponse.RequestResponseIO;
import org.apache.beam.io.requestresponse.Result;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.coders.IterableCoder;
import org.apache.beam.sdk.extensions.protobuf.ProtoCoder;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableWriteResult;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableWriteResultCoder;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;

/** A test implementation of BigTable using {@link RequestResponseIO}. */
class BigTableIOUsingRRIO {

  static Read read() {
    return new Read(ReadConfiguration.builder());
  }

  static Write write() {
    return new Write(WriteConfiguration.builder());
  }

  static class Read extends PTransform<PBegin, PCollection<Row>> {

    @SuppressWarnings({"unused"})
    private final ReadConfiguration.Builder configurationBuilder;

    private Read(ReadConfiguration.Builder builder) {
      this.configurationBuilder = builder;
    }

    @Override
    public PCollection<Row> expand(PBegin input) {
      Coder<Row> rowCoder = ProtoCoder.of(Row.class);
      IterableCoder<Row> coder = IterableCoder.of(rowCoder);
      BigTableReader reader = new BigTableReader(configurationBuilder.build());
      RequestResponseIO<byte[], Iterable<Row>> transform =
          RequestResponseIO.ofCallerAndSetupTeardown(reader, coder);
      Result<Iterable<Row>> result =
          input
              .apply("impulse", Create.of(new byte[0]))
              .apply(BigTableReader.class.getSimpleName(), transform);
      return result.getResponses().apply("flatten", Flatten.iterables());
    }

    Read withProjectId(String project) {
      return new Read(configurationBuilder.setProjectId(project));
    }

    Read withInstanceId(String instanceId) {
      return new Read(configurationBuilder.setInstanceId(instanceId));
    }

    public Read withTableId(String tableId) {
      return new Read(configurationBuilder.setTableId(tableId));
    }

    public Read withRowFilter(RowFilter rowFilter) {
      return new Read(configurationBuilder.setRowFilter(rowFilter));
    }
  }

  static class Write
      extends PTransform<
          PCollection<KV<ByteString, Iterable<Mutation>>>, PCollection<BigtableWriteResult>> {

    @SuppressWarnings({"unused"})
    private final WriteConfiguration.Builder configurationBuilder;

    private Write(WriteConfiguration.Builder builder) {
      this.configurationBuilder = builder;
    }

    @Override
    public PCollection<BigtableWriteResult> expand(
        PCollection<KV<ByteString, Iterable<Mutation>>> input) {
      Result<BigtableWriteResult> result =
          input.apply(
              BigTableWriter.class.getSimpleName(),
              RequestResponseIO.ofCallerAndSetupTeardown(
                  new BigTableWriter(configurationBuilder.build()), BigtableWriteResultCoder.of()));

      return result.getResponses();
    }

    public Write withProjectId(String project) {
      return new Write(configurationBuilder.setProjectId(project));
    }

    public Write withInstanceId(String instanceId) {
      return new Write(configurationBuilder.setInstanceId(instanceId));
    }

    public Write withTableId(String tableId) {
      return new Write(configurationBuilder.setTableId(tableId));
    }
  }

  @AutoValue
  abstract static class ReadConfiguration implements Serializable {

    abstract String getProjectId();

    abstract String getInstanceId();

    abstract String getTableId();

    abstract RowFilter getRowFilter();

    static Builder builder() {
      return new AutoValue_BigTableIOUsingRRIO_ReadConfiguration.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder implements Serializable {

      abstract Builder setProjectId(String value);

      abstract Builder setInstanceId(String value);

      abstract Builder setTableId(String value);

      abstract Builder setRowFilter(RowFilter value);

      abstract ReadConfiguration build();
    }
  }

  @AutoValue
  abstract static class WriteConfiguration implements Serializable {

    static Builder builder() {
      return new AutoValue_BigTableIOUsingRRIO_WriteConfiguration.Builder();
    }

    abstract String getProjectId();

    abstract String getInstanceId();

    abstract String getTableId();

    @AutoValue.Builder
    abstract static class Builder implements Serializable {

      abstract Builder setProjectId(String value);

      abstract Builder setInstanceId(String value);

      abstract Builder setTableId(String value);

      abstract WriteConfiguration build();
    }
  }
}
