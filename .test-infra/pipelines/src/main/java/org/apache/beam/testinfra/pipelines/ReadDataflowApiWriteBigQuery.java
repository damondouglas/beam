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

import com.google.dataflow.v1beta3.GetJobRequest;
import com.google.dataflow.v1beta3.Job;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.WithFailures;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.testinfra.pipelines.bigquery.BigQueryWriteOptions;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowClientFactoryConfiguration;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowGetJobs;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowJobsOptions;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowReadResult;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowRequests;
import org.apache.beam.testinfra.pipelines.eventarc.ConversionError;
import org.apache.beam.testinfra.pipelines.eventarc.EventarcConversions;
import org.apache.beam.testinfra.pipelines.pubsub.PubsubReadOptions;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ReadDataflowApiWriteBigQuery {
  //  private static final Logger LOG = LoggerFactory.getLogger(ReadDataflowApiWriteBigQuery.class);

  public interface Options extends DataflowJobsOptions, PubsubReadOptions, BigQueryWriteOptions {}

  public static void main(String[] args) {
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
    Pipeline pipeline = Pipeline.create(options);

    DataflowClientFactoryConfiguration configuration =
        DataflowClientFactoryConfiguration.builder(options).build();

    PCollection<String> json =
        pipeline.apply(
            "Read Eventarc Pubsub",
            PubsubIO.readStrings()
                .fromSubscription(options.getSubscription().getValue().getPath()));

    WithFailures.Result<
            @NonNull PCollection<com.google.events.cloud.dataflow.v1beta3.Job>,
            ConversionError<String>>
        events = json.apply(EventarcConversions.fromJson());

    DataflowReadResult<GetJobRequest, Job> jobs =
        events
            .output()
            .apply("GetDataflowJob requests", DataflowRequests.jobRequestsFromEventsViewAll())
            .apply("GetDataflowJobs", DataflowGetJobs.create(configuration));

    pipeline.run();
  }

  //  private static <T> void debug(PCollection<T> input) {
  //    input.apply("debug", ParDo.of(new DoFn<T, T>() {
  //      @ProcessElement
  //      public void process(@Element T elem) {
  //        LOG.info("ELEM: {}", elem);
  //      }
  //    }));
  //  }
}
