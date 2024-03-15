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

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import com.google.api.gax.rpc.ServerStream;
import com.google.bigtable.v2.Cell;
import com.google.bigtable.v2.Column;
import com.google.bigtable.v2.Family;
import com.google.bigtable.v2.Row;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Filters;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.beam.io.requestresponse.Caller;
import org.apache.beam.io.requestresponse.SetupTeardown;
import org.apache.beam.io.requestresponse.UserCodeExecutionException;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

class BigTableReader implements Caller<byte[], Iterable<Row>>, SetupTeardown {
  private final BigTableIOUsingRRIO.ReadConfiguration configuration;
  private final Filters.Filter filter;
  private transient @MonotonicNonNull BigtableDataClient client;

  BigTableReader(BigTableIOUsingRRIO.ReadConfiguration configuration) {
    this.configuration = configuration;
    filter = Filters.FILTERS.key().regex(configuration.getRowFilter().getRowKeyRegexFilter());
  }

  @Override
  public Iterable<Row> call(byte[] ignored) throws UserCodeExecutionException {
    List<Row> result = new ArrayList<>();
    Query query = Query.create(configuration.getTableId()).filter(filter);
    ServerStream<com.google.cloud.bigtable.data.v2.models.Row> rows =
        checkStateNotNull(client).readRows(query);
    for (com.google.cloud.bigtable.data.v2.models.Row row : rows) {
      for (RowCell cell : row.getCells()) {
        result.add(
            Row.newBuilder()
                .addFamilies(
                    Family.newBuilder()
                        .setName(cell.getFamily())
                        .addColumns(
                            Column.newBuilder()
                                .setQualifier(cell.getQualifier())
                                .addCells(
                                    Cell.newBuilder()
                                        .setValue(cell.getValue())
                                        .setTimestampMicros(cell.getTimestamp())
                                        .build())
                                .build())
                        .build())
                .setKey(row.getKey())
                .build());
      }
    }
    return result;
  }

  @Override
  public void setup() throws UserCodeExecutionException {
    try {
      this.client =
          BigtableDataClient.create(configuration.getProjectId(), configuration.getInstanceId());
    } catch (IOException e) {
      throw new UserCodeExecutionException(
          String.format(
              "error connecting to BigTable: projects/%s/instances/%s",
              configuration.getProjectId(), configuration.getInstanceId()),
          e);
    }
  }

  @Override
  public void teardown() throws UserCodeExecutionException {
    if (client != null) {
      client.close();
    }
  }
}
