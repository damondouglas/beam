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

import static org.apache.beam.io.requestresponse.it.Logs.errorOf;
import static org.apache.beam.io.requestresponse.it.Logs.infoOf;

import com.google.cloud.run.v2.RunJobRequest;
import com.google.events.cloud.dataflow.v1beta3.Job;
import com.google.longrunning.Operation;
import org.apache.beam.io.requestresponse.Result;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.commons.lang3.tuple.Pair;

public class EventarcListener {
  private static final EventarcConvertJsonToJob CONVERT_EVENTARC_JSON_TO_JOB =
      new EventarcConvertJsonToJob();

  public static void main(String[] args) {
    EventarcListenerOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(EventarcListenerOptions.class);
    Pipeline pipeline = Pipeline.create(options);

    PCollection<PubsubMessage> events =
        pipeline.apply(
            "events", PubsubIO.readMessages().fromSubscription(options.getEventarcSubscription()));

    PCollectionTuple pct =
        events.apply(EventarcConvertJsonToJob.class.getSimpleName(), CONVERT_EVENTARC_JSON_TO_JOB);

    PCollection<Pair<String, String>> failures =
        pct.get(CONVERT_EVENTARC_JSON_TO_JOB.getFailureTag());

    errorOf(failures);

    PCollection<Job> jobs = pct.get(CONVERT_EVENTARC_JSON_TO_JOB.getOutputTag());

    PCollectionList<Job> partitioned = jobs.apply("partition", EventarcJobs.PARTITION_JOBS);
    PCollection<Job> included = partitioned.get(EventarcJobs.PARTITION_INCLUDED_INDEX);
    PCollection<Job> excluded = partitioned.get(EventarcJobs.PARTITION_EXCLUDED_INDEX);

    infoOf(included.apply("includedJobName", EventarcJobs.SUMMARIZE_FN), "included");
    infoOf(excluded.apply("excludedJobName", EventarcJobs.SUMMARIZE_FN), "excluded");

    PCollection<RunJobRequest> invokeJobRequests =
        included.apply("toInvokeJobRequests", EventarcJobs.MAP_TO_RUN_JOB_REQUESTS);

    Result<Operation> invokeOperations =
        invokeJobRequests.apply("runJobs", EventarcJobInvoker.pTransform());

    errorOf(invokeOperations.getFailures());
    infoOf(invokeOperations.getResponses(), "runJobOperation");

    Result<Operation> completedOperations =
        invokeOperations.getResponses().apply(EventarcOperationCompleter.pTransform());
    errorOf(completedOperations.getFailures());
    infoOf(completedOperations.getResponses(), "completedRunJobOperation");

    pipeline.run();
  }
}
