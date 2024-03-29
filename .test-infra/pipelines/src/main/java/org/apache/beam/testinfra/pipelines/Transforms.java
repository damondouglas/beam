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
import static org.apache.beam.sdk.values.TypeDescriptors.rows;

import com.google.dataflow.v1beta3.GetJobMetricsRequest;
import com.google.dataflow.v1beta3.GetJobRequest;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.testinfra.pipelines.dataflow.Job;
import org.apache.beam.testinfra.pipelines.dataflow.JobMetrics;

final class Transforms {
  static PCollection<com.google.events.cloud.dataflow.v1beta3.Job> jobs(
      PCollection<com.google.events.cloud.dataflow.v1beta3.Job> jobs,
      com.google.events.cloud.dataflow.v1beta3.JobState state) {
    return jobs.apply("filter " + state, Filter.by(job -> job.getCurrentState().equals(state)));
  }

  static PCollection<GetJobRequest> jobRequestsFrom(
      PCollection<com.google.events.cloud.dataflow.v1beta3.Job> jobs) {
    return jobs.apply(
        "GetJobRequest from Jobs",
        MapElements.into(TypeDescriptor.of(GetJobRequest.class))
            .via(
                job -> {
                  com.google.events.cloud.dataflow.v1beta3.Job safeJob = checkStateNotNull(job);
                  return GetJobRequest.newBuilder()
                      .setLocation(safeJob.getLocation())
                      .setProjectId(safeJob.getProjectId())
                      .setJobId(safeJob.getId())
                      .build();
                }));
  }

  static PCollection<GetJobMetricsRequest> metricRequestsFrom(
      PCollection<com.google.events.cloud.dataflow.v1beta3.Job> jobs) {
    return jobs.apply(
        "GetJobMetricsRequest from Jobs",
        MapElements.into(TypeDescriptor.of(GetJobMetricsRequest.class))
            .via(
                job -> {
                  com.google.events.cloud.dataflow.v1beta3.Job safeJob = checkStateNotNull(job);
                  return GetJobMetricsRequest.newBuilder()
                      .setJobId(safeJob.getId())
                      .setProjectId(safeJob.getProjectId())
                      .setLocation(safeJob.getLocation())
                      .build();
                }));
  }

  static PCollection<Row> jobRowsFrom(PCollection<Job> jobs) {
    return jobs.apply("jobRows", MapElements.into(rows()).via(Job.TO_ROW_FN))
        .setCoder(Job.ROW_CODER)
            .setRowSchema(Job.ROW_CODER.getSchema());
  }

  static PCollection<Row> metricRowsFrom(PCollection<JobMetrics> metrics) {
    return metrics
        .apply("metricRows", MapElements.into(rows()).via(JobMetrics.TO_ROW_FN))
        .setCoder(JobMetrics.ROW_CODER)
            .setRowSchema(JobMetrics.ROW_CODER.getSchema());
  }
}
