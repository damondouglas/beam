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

import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TimePartitioning;
import com.google.dataflow.v1beta3.GetJobMetricsRequest;
import com.google.dataflow.v1beta3.GetJobRequest;
import com.google.events.cloud.dataflow.v1beta3.JobState;
import org.apache.beam.io.requestresponse.Result;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.annotations.Internal;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.testinfra.pipelines.bigquery.BigQueryWriteOptions;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowJobsOptions;
import org.apache.beam.testinfra.pipelines.dataflow.GetJob;
import org.apache.beam.testinfra.pipelines.dataflow.GetMetrics;
import org.apache.beam.testinfra.pipelines.dataflow.Job;
import org.apache.beam.testinfra.pipelines.dataflow.JobMetrics;
import org.apache.beam.testinfra.pipelines.pubsub.EventarcConvertPubsubMessageToJob;
import org.apache.beam.testinfra.pipelines.pubsub.PubsubReadOptions;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Strings;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Constructs and executes a {@link Pipeline} that reads from the Dataflow API and writes to
 * BigQuery. For internal use only.
 */
@Internal
public class ReadDataflowApiWriteBigQuery {

  public interface Options extends DataflowJobsOptions, PubsubReadOptions, BigQueryWriteOptions {}

  public static void main(String[] args) {
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
    String projectId = options.as(GcpOptions.class).getProject();
    if (Strings.isNullOrEmpty(projectId)) {
      throw new IllegalArgumentException("--project is required");
    }
    Pipeline pipeline = Pipeline.create(options);

    PCollection<PubsubMessage> messages =
        pipeline.apply(
            PubsubIO.readMessagesWithAttributes().fromSubscription(options.getSubscription()));
    EventarcConvertPubsubMessageToJob.Result events =
        messages.apply(EventarcConvertPubsubMessageToJob.of());
    logErrors("Eventarc to PubSub errors", events.getFailures());

    PCollection<com.google.events.cloud.dataflow.v1beta3.Job> doneJobs = Transforms.jobs(events.getOutput(), JobState.JOB_STATE_DONE);
    PCollection<com.google.events.cloud.dataflow.v1beta3.Job> canceledJobs = Transforms.jobs(events.getOutput(), JobState.JOB_STATE_CANCELLED);
    PCollection<com.google.events.cloud.dataflow.v1beta3.Job> drainedJobs = Transforms.jobs(events.getOutput(), JobState.JOB_STATE_DRAINED);

    PCollectionList<com.google.events.cloud.dataflow.v1beta3.Job> filteredPColList = PCollectionList
            .of(doneJobs)
            .and(canceledJobs)
            .and(drainedJobs);

    PCollection<com.google.events.cloud.dataflow.v1beta3.Job> filteredJobs = filteredPColList.apply("flatten filtered jobs", Flatten.pCollections());

    logInfo("filteredJobs", filteredJobs);

    PCollection<GetJobRequest> getJobRequests = Transforms.jobRequestsFrom(filteredJobs);

    PCollection<GetJobMetricsRequest> getJobMetricsRequests = Transforms.metricRequestsFrom(filteredJobs);

    logInfo("getJobRequests", getJobRequests);
    logInfo("getJobMetricsRequests", getJobMetricsRequests);

    Result<Job> jobsFromApi = getJobRequests.apply("GetJobs", GetJob.pTransform());

    Result<JobMetrics> metricsFromApi = getJobMetricsRequests.apply("GetMetrics", GetMetrics.pTransform());

    logErrors("jobsFromApi", jobsFromApi.getFailures());
    logErrors("metricsFromApi", metricsFromApi.getFailures());

    TableReference jobsTable = new TableReference()
            .setProjectId(projectId)
            .setDatasetId(options.getDataset())
            .setTableId("jobs_" + Instant.now().getMillis());

    TableReference metricsTable = new TableReference()
            .setProjectId(projectId)
            .setDatasetId(options.getDataset())
            .setTableId("metrics_" + Instant.now().getMillis());

    PCollection<Row> jobRows = Transforms.jobRowsFrom(jobsFromApi.getResponses());
    PCollection<Row> metricRows = Transforms.metricRowsFrom(metricsFromApi.getResponses());

    jobRows.apply("write jobs", BigQueryIO
            .<Row>write()
            .useBeamSchema()
            .to(jobsTable)
            .withTimePartitioning(new TimePartitioning())
            .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
            .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));

    metricRows.apply("write metrics", BigQueryIO
            .<Row>write()
            .useBeamSchema()
            .to(metricsTable)
            .withTimePartitioning(new TimePartitioning())
            .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
            .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));

    pipeline.run();
  }

  private static <T> void logErrors(String name, PCollection<T> errors) {
    errors.apply(name, ParDo.of(new ErrorFn<>()));
  }

  private static class ErrorFn<T> extends DoFn<T, T> {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorFn.class);

    @ProcessElement
    public void process(@Element T element, OutputReceiver<T> receiver) {
      LOG.error("{}: {}", Instant.now(), element);
      receiver.output(element);
    }
  }

  private static <T> void logInfo(String name, PCollection<T> col) {
    col.apply(name, ParDo.of(new InfoFn<>()));
  }

  private static class InfoFn<T> extends DoFn<T, T> {
    private static final Logger LOG = LoggerFactory.getLogger(InfoFn.class);

    @ProcessElement
    public void process(@Element T element, OutputReceiver<T> receiver) {
      LOG.info("{}: {}", Instant.now(), element);
      receiver.output(element);
    }
  }
}
