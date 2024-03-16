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
package org.apache.beam.testinfra.pipelines;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.storage.v1.AppendRowsRequest;
import com.google.cloud.bigquery.storage.v1.AppendRowsResponse;
import com.google.cloud.bigquery.storage.v1.BigQueryWriteClient;
import com.google.cloud.bigquery.storage.v1.CreateWriteStreamRequest;
import com.google.cloud.bigquery.storage.v1.ProtoSchema;
import com.google.cloud.bigquery.storage.v1.ProtoSchemaConverter;
import com.google.cloud.bigquery.storage.v1.StreamWriter;
import com.google.cloud.bigquery.storage.v1.TableName;
import com.google.cloud.bigquery.storage.v1.WriteStream;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import org.apache.beam.io.requestresponse.Caller;
import org.apache.beam.io.requestresponse.RequestResponseIO;
import org.apache.beam.io.requestresponse.Result;
import org.apache.beam.io.requestresponse.SetupTeardown;
import org.apache.beam.io.requestresponse.UserCodeExecutionException;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

class WriteToBigQuery implements Caller<AppendRowsRequest, AppendRowsResponse>, SetupTeardown {

  static PTransform<PCollection<AppendRowsRequest>, Result<AppendRowsResponse>> pTransform(
      TableName tableName, Descriptors.Descriptor descriptor) {
    return RequestResponseIO.ofCallerAndSetupTeardown(
        new WriteToBigQuery(tableName, descriptor), new AppendRowsResponseCoder());
  }

  private transient @MonotonicNonNull BigQueryWriteClient client;
  private transient @MonotonicNonNull StreamWriter writer;

  private final String project;
  private final String dataset;
  private final String table;

  private final ProtoSchema schema;

  WriteToBigQuery(TableName tableName, Descriptors.Descriptor descriptor) {
    this.project = tableName.getProject();
    this.dataset = tableName.getDataset();
    this.table = tableName.getTable();
    this.schema = ProtoSchemaConverter.convert(descriptor);
  }

  @Override
  public AppendRowsResponse call(AppendRowsRequest request) throws UserCodeExecutionException {
    try {
      return checkStateNotNull(writer).append(request.getProtoRows().getRows()).get();
    } catch (InterruptedException | ExecutionException e) {
      throw new UserCodeExecutionException(e);
    }
  }

  @Override
  public void setup() throws UserCodeExecutionException {
    TableName tableName =
        TableName.newBuilder().setProject(project).setDataset(dataset).setTable(table).build();
    try {
      client = BigQueryWriteClient.create();

      WriteStream stream =
          client.createWriteStream(
              CreateWriteStreamRequest.newBuilder()
                  .setParent(tableName.toString())
                  .setWriteStream(
                      WriteStream.newBuilder().setType(WriteStream.Type.COMMITTED).build())
                  .build());

      writer =
          StreamWriter.newBuilder(stream.getName(), client)
              .setWriterSchema(schema)
              .build();

    } catch (BigQueryException | IOException e) {
      throw new UserCodeExecutionException(e);
    }
  }

  @Override
  public void teardown() throws UserCodeExecutionException {
    if (writer != null) {
      writer.close();
    }
    if (client != null) {
      client.close();
    }
  }

  static class AppendRowsResponseCoder extends CustomCoder<AppendRowsResponse> {

    @Override
    public void encode(AppendRowsResponse value, OutputStream outStream)
        throws CoderException, IOException {
      value.writeTo(outStream);
    }

    @Override
    public AppendRowsResponse decode(InputStream inStream) throws CoderException, IOException {
      return AppendRowsResponse.parseFrom(inStream);
    }
  }
}
