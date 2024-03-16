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

import com.google.cloud.bigquery.storage.v1.AppendRowsRequest;
import com.google.cloud.bigquery.storage.v1.ProtoRows;
import com.google.dataflow.v1beta3.GetJobMetricsRequest;
import com.google.dataflow.v1beta3.GetJobRequest;
import com.google.events.cloud.dataflow.v1beta3.Job;
import org.apache.beam.sdk.transforms.GroupIntoBatches;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.Values;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.joda.time.Duration;

final class Conversions {

  private static final SerializableFunction<Job, GetJobRequest> JOB_TO_GET_REQUEST_FN =
      job -> {
        Job safeJob = checkStateNotNull(job);
        return GetJobRequest.newBuilder()
            .setJobId(safeJob.getId())
            .setProjectId(safeJob.getProjectId())
            .setLocation(job.getLocation())
            .build();
      };

  private static final SerializableFunction<Job, GetJobMetricsRequest> JOB_TO_GET_METRICS_FN =
      job -> {
        Job safeJob = checkStateNotNull(job);
        return GetJobMetricsRequest.newBuilder()
            .setJobId(safeJob.getId())
            .setProjectId(safeJob.getProjectId())
            .setLocation(safeJob.getLocation())
            .build();
      };

  private static final SerializableFunction<
          Iterable<com.google.dataflow.v1beta3.Job>, AppendRowsRequest>
      JOBS_TO_APPEND_ROWS_FN =
          itr -> {
            ProtoRows.Builder builder = ProtoRows.newBuilder();
            for (com.google.dataflow.v1beta3.Job job : itr) {
              builder.addSerializedRows(job.toByteString());
            }
            return AppendRowsRequest.newBuilder()
                .setProtoRows(
                    AppendRowsRequest.ProtoData.newBuilder().setRows(builder.build()).build())
                .build();
          };

  static PCollection<Iterable<com.google.dataflow.v1beta3.Job>> bundle(
      PCollection<com.google.dataflow.v1beta3.Job> input) {
    return input
        .apply(WithKeys.class.getSimpleName(), WithKeys.of(0L))
        .apply(
            GroupIntoBatches.class.getSimpleName(),
            GroupIntoBatches.<Long, com.google.dataflow.v1beta3.Job>ofSize(10L)
                .withMaxBufferingDuration(Duration.standardSeconds(1L)))
        .apply(Values.class.getSimpleName(), Values.create());
  }

  static final MapElements<Iterable<com.google.dataflow.v1beta3.Job>, AppendRowsRequest>
      MAP_JOBS_TO_APPEND_ROWS =
          MapElements.into(TypeDescriptor.of(AppendRowsRequest.class)).via(JOBS_TO_APPEND_ROWS_FN);

  static final MapElements<Job, GetJobRequest> MAP_JOB_TO_GET_REQUEST =
      MapElements.into(TypeDescriptor.of(GetJobRequest.class)).via(JOB_TO_GET_REQUEST_FN);

  static final MapElements<Job, GetJobMetricsRequest> MAP_JOB_TO_GET_METRICS =
      MapElements.into(TypeDescriptor.of(GetJobMetricsRequest.class)).via(JOB_TO_GET_METRICS_FN);
}
