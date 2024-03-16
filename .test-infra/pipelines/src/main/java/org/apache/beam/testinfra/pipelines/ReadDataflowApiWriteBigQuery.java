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

import com.google.cloud.bigquery.storage.v1.AppendRowsRequest;
import com.google.cloud.bigquery.storage.v1.TableName;
import com.google.dataflow.v1beta3.Job;
import com.google.dataflow.v1beta3.JobType;
import com.google.dataflow.v1beta3.Step;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.annotations.Internal;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;

/**
 * Constructs and executes a {@link Pipeline} that reads from the Dataflow API and writes to
 * BigQuery. For internal use only.
 */
@Internal
public class ReadDataflowApiWriteBigQuery {

  //  private static final EventarcConvertPubsubToJob EVENTARC_CONVERT_PUBSUB_TO_JOB = new
  // EventarcConvertPubsubToJob();

  public static void main(String[] args) {
    ReadDataflowApiWriteBigQueryOptions options =
        PipelineOptionsFactory.fromArgs(args)
            .withValidation()
            .as(ReadDataflowApiWriteBigQueryOptions.class);
    Pipeline pipeline = Pipeline.create(options);

    //    PCollectionTuple jobsFromEvents = pipeline
    //            .apply(PubsubIO.Read.class.getSimpleName(),
    // PubsubIO.readMessages().fromSubscription(options.getSubscription()))
    //            .apply(EventarcConvertPubsubToJob.class.getSimpleName(),
    // EVENTARC_CONVERT_PUBSUB_TO_JOB);
    //
    //    Logs.errorOf(jobsFromEvents.get(EVENTARC_CONVERT_PUBSUB_TO_JOB.getFailureTag()));
    //

    PCollection<Job> jobs =
        pipeline.apply(
            Create.of(
                Job.newBuilder()
                    .setId("some id")
                    .setLocation("us-central1")
                    .setProjectId("project id")
                        .setType(JobType.JOB_TYPE_STREAMING)
                    .addSteps(
                        Step.newBuilder()
                            .setKind("kind value1")
                            .setName("step name1")
                            .setProperties(
                                Struct.newBuilder()
                                    .putFields(
                                        "property-key1",
                                        Value.newBuilder()
                                            .setStringValue("property-value1")
                                            .build())
                                    .build())
                            .build())
                    .addSteps(
                        Step.newBuilder()
                            .setKind("kind value2")
                            .setName("step name2")
                            .setProperties(
                                Struct.newBuilder()
                                    .putFields(
                                        "property-key1",
                                        Value.newBuilder()
                                            .setStringValue("property-value1")
                                            .build())
                                    .build())
                            .build())
                    .build()));

    PCollection<Iterable<Job>> bundledJobs = Conversions.bundle(jobs);
    PCollection<AppendRowsRequest> appendRowsRequests =
        bundledJobs.apply(Conversions.MAP_JOBS_TO_APPEND_ROWS);
    appendRowsRequests.apply(
        WriteToBigQuery.pTransform(
            TableName.newBuilder()
                .setProject("dd-test-dd16b1b0")
                .setDataset("dataflow_job_v1beta3_status_changed")
                .setTable("jobs")
                .build(),
            Job.getDescriptor()));

    pipeline.run();
  }
}
