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
import com.google.dataflow.v1beta3.GetJobRequest;
import com.google.events.cloud.dataflow.v1beta3.Job;
import com.google.events.cloud.dataflow.v1beta3.JobType;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.WithFailures;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.testinfra.pipelines.bigquery.BigQueryWriteConversionErrors;
import org.apache.beam.testinfra.pipelines.bigquery.BigQueryWriteOptions;
import org.apache.beam.testinfra.pipelines.conversions.ConversionError;
import org.apache.beam.testinfra.pipelines.conversions.EventarcConversions;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowClientFactoryConfiguration;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowFilterEventarcJobs;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowGetJobs;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowJobsOptions;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowReadResult;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowRequests;
import org.apache.beam.testinfra.pipelines.pubsub.PubsubReadOptions;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.beam.sdk.values.TypeDescriptors.strings;

public class ReadDataflowApiWriteBigQuery {
//  private static final Logger LOG = LoggerFactory.getLogger(ReadDataflowApiWriteBigQuery.class);

  public interface Options extends DataflowJobsOptions, PubsubReadOptions, BigQueryWriteOptions {}

  public static void main(String[] args) {
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);

    Pipeline pipeline = Pipeline.create(options);

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
