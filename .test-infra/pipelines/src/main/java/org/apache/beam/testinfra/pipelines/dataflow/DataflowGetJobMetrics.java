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

import com.google.dataflow.v1beta3.GetJobMetricsRequest;
import com.google.dataflow.v1beta3.JobMetrics;
import com.google.dataflow.v1beta3.MetricsV1Beta3Grpc;
import io.grpc.StatusRuntimeException;
import java.util.Optional;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Throwables;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joda.time.Instant;

public class DataflowGetJobMetrics
    extends PTransform<
        @NonNull PCollection<GetJobMetricsRequest>,
        @NonNull DataflowReadResult<GetJobMetricsRequest, JobMetricsWithJobId>> {

  public static DataflowGetJobMetrics create(DataflowClientFactoryConfiguration configuration) {
    return new DataflowGetJobMetrics(configuration);
  }

  private static final TupleTag<JobMetricsWithJobId> SUCCESS =
      new TupleTag<JobMetricsWithJobId>() {};

  private static final TupleTag<DataflowRequestError<GetJobMetricsRequest>> FAILURE =
      new TupleTag<DataflowRequestError<GetJobMetricsRequest>>() {};

  private final DataflowClientFactoryConfiguration configuration;

  private DataflowGetJobMetrics(DataflowClientFactoryConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public @NonNull DataflowReadResult<GetJobMetricsRequest, JobMetricsWithJobId> expand(
      PCollection<GetJobMetricsRequest> input) {
    PCollectionTuple pct =
        input.apply(
            DataflowGetJobMetrics.class.getSimpleName(),
            ParDo.of(new GetJobMetricsFn(this)).withOutputTags(SUCCESS, TupleTagList.of(FAILURE)));

    return DataflowReadResult.of(SUCCESS, FAILURE, pct);
  }

  private static class GetJobMetricsFn extends DoFn<GetJobMetricsRequest, JobMetricsWithJobId> {

    private final DataflowGetJobMetrics spec;
    private transient MetricsV1Beta3Grpc.@MonotonicNonNull MetricsV1Beta3BlockingStub client;

    private GetJobMetricsFn(DataflowGetJobMetrics spec) {
      this.spec = spec;
    }

    @Setup
    public void setup() {
      client = DataflowClientFactory.createMetricsClient(spec.configuration);
    }

    @ProcessElement
    public void process(@Element GetJobMetricsRequest request, MultiOutputReceiver receiver) {
      try {
        JobMetrics response = checkStateNotNull(client).getJobMetrics(request);
        receiver
            .get(SUCCESS)
            .output(
                JobMetricsWithJobId.builder()
                    .setJobId(request.getJobId())
                    .setJobMetrics(response)
                    .build());
      } catch (StatusRuntimeException e) {
        receiver
            .get(FAILURE)
            .output(
                DataflowRequestError.<GetJobMetricsRequest>builder()
                    .setObservedTime(Instant.now())
                    .setRequest(request)
                    .setMessage(Optional.ofNullable(e.getMessage()).orElse(""))
                    .setStackTrace(Throwables.getStackTraceAsString(e))
                    .build());
      }
    }
  }
}
