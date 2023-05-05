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
package org.apache.beam.testinfra.pipelines.dataflow;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;
import static org.apache.beam.testinfra.pipelines.dataflow.DataflowListJobsResult.FAILURE;
import static org.apache.beam.testinfra.pipelines.dataflow.DataflowListJobsResult.SUCCESS;
import static org.apache.beam.testinfra.pipelines.dataflow.DataflowListJobsResult.TOKENIZED_REQUESTS;

import com.google.dataflow.v1beta3.JobsV1Beta3Grpc;
import com.google.dataflow.v1beta3.ListJobsRequest;
import com.google.dataflow.v1beta3.ListJobsResponse;
import io.grpc.StatusRuntimeException;
import java.util.Optional;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Strings;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Throwables;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableList;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;

public class DataflowListJobs
    extends PTransform<
        @UnknownKeyFor @NonNull @Initialized PCollection<ListJobsRequest>,
        @NonNull DataflowListJobsResult> {

  public static DataflowListJobs create(DataflowJobsOptions options) {
    return new DataflowListJobs(options);
  }

  private final DataflowClientFactoryConfiguration configuration;

  private DataflowListJobs(DataflowJobsOptions options) {
    this.configuration = DataflowClientFactoryConfiguration.builder(options).build();
  }

  @Override
  @NonNull
  public DataflowListJobsResult expand(@NonNull PCollection<ListJobsRequest> input) {
    PCollectionTuple pct =
        input.apply(
            DataflowListJobs.class.getSimpleName(),
            ParDo.of(new ListJobsFn(this))
                .withOutputTags(
                    SUCCESS, TupleTagList.of(ImmutableList.of(FAILURE, TOKENIZED_REQUESTS))));
    return DataflowListJobsResult.of(pct);
  }

  static class ListJobsFn extends DoFn<ListJobsRequest, ListJobsResponse> {

    private final DataflowListJobs spec;

    private transient JobsV1Beta3Grpc.@MonotonicNonNull JobsV1Beta3BlockingStub client;

    ListJobsFn(DataflowListJobs spec) {
      this.spec = spec;
    }

    @Setup
    public void setup() {
      client = DataflowClientFactory.createJobsClient(spec.configuration);
    }

    @ProcessElement
    public void process(@Element ListJobsRequest request, MultiOutputReceiver receiver) {
      JobsV1Beta3Grpc.JobsV1Beta3BlockingStub safeClient = checkStateNotNull(client);
      try {
        ListJobsResponse response = safeClient.listJobs(request);
        receiver.get(SUCCESS).output(response);
        if (!Strings.isNullOrEmpty(response.getNextPageToken())) {
          ListJobsRequest withNextPageToken =
              request.toBuilder().setPageToken(response.getNextPageToken()).build();
          receiver.get(TOKENIZED_REQUESTS).output(withNextPageToken);
        }
      } catch (StatusRuntimeException e) {
        DataflowListJobsError error =
            DataflowListJobsError.builder()
                .setMessage(Optional.ofNullable(e.getMessage()).orElse(""))
                .setRequest(request)
                .setStackTrace(Throwables.getStackTraceAsString(e))
                .setStatus(e.getStatus())
                .build();
        receiver.get(FAILURE).output(error);
      }
    }
  }
}
