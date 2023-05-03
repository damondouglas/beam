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

import com.google.api.services.dataflow.Dataflow.Projects.Jobs.List;
import com.google.api.services.dataflow.model.ListJobsResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.runners.dataflow.util.DataflowTransport;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.testinfra.pipelines.bigquery.BigQueryWriteOptions;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowClientFactory;
import org.apache.beam.testinfra.pipelines.dataflow.DataflowReadOptions;
import org.apache.beam.testinfra.pipelines.dataflow.ListJobs;
import org.apache.beam.testinfra.pipelines.dataflow.ListJobsRequest;
import org.apache.beam.testinfra.pipelines.dataflow.ListJobsResult;
import org.apache.beam.testinfra.pipelines.redis.RedisOptions;

public class ReadDataflowApiWriteBigQuery {

  public interface Options extends DataflowReadOptions, BigQueryWriteOptions, RedisOptions {}

  public static void main(String[] args) throws GeneralSecurityException, IOException {
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
    Pipeline pipeline = Pipeline.create(options);
    List list =
        DataflowTransport.newDataflowClient(options.as(DataflowPipelineOptions.class)).build().projects().jobs().list(options.getDataflowProject());
    // ListJobsResponse response = list.setLocation(options.getDataflowLocation()).execute();
    // System.out.println(response.toPrettyString());
    ListJobsRequest request = new ListJobsRequest(list);
    PCollection<ListJobsRequest> requests = pipeline.apply(Create.of(request));
    ListJobsResult result = requests.apply(new ListJobs());
    result.getSuccess().apply(ParDo.of(new DoFn<ListJobsResponse, ListJobsResponse>() {
      @ProcessElement
      public void process(@Element ListJobsResponse response) {
        System.out.println(response);
      }
    }));

    pipeline.run();
  }
}
