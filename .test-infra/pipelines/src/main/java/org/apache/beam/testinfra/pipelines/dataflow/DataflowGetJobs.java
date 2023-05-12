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

import com.google.dataflow.v1beta3.GetJobRequest;
import com.google.dataflow.v1beta3.Job;
import com.google.dataflow.v1beta3.JobsV1Beta3Grpc;
import io.grpc.StatusRuntimeException;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.JavaBeanSchema;
import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaRegistry;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.utils.JavaBeanUtils;
import org.apache.beam.sdk.schemas.utils.StaticSchemaInference;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

public class DataflowGetJobs
    extends PTransform<
        @NonNull PCollection<GetJobRequest>,
        @NonNull DataflowReadResult<Job, DataflowRequestError<GetJobRequest>>> {

  private static final DefaultSchema.DefaultSchemaProvider SCHEMA_PROVIDER = new DefaultSchema.DefaultSchemaProvider();

  private static final TupleTag<Job> SUCCESS = new TupleTag<Job>(){};

  private static final TupleTag<DataflowRequestError<GetJobRequest>> FAILURE =
      new TupleTag<DataflowRequestError<GetJobRequest>>() {};

  public static DataflowGetJobs create(DataflowClientFactoryConfiguration configuration) {
    return new DataflowGetJobs(configuration);
  }

  private final DataflowClientFactoryConfiguration configuration;

  private DataflowGetJobs(@NonNull DataflowClientFactoryConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public @NonNull DataflowReadResult<Job, DataflowRequestError<GetJobRequest>> expand(
      PCollection<GetJobRequest> input) {

    Schema successSchema = checkStateNotNull(SCHEMA_PROVIDER.schemaFor(TypeDescriptor.of(Job.class)));
    Schema failureSchema = checkStateNotNull(SCHEMA_PROVIDER.schemaFor(new TypeDescriptor<DataflowRequestError<GetJobRequest>>(){}));

    PCollectionTuple pct =
        input.apply(
            "GetJobs",
            ParDo.of(new GetJobsFn(this)).withOutputTags(SUCCESS, TupleTagList.of(FAILURE)));

    return DataflowReadResult.of(SUCCESS, FAILURE, successSchema, failureSchema, pct);
  }

  private static class GetJobsFn extends DoFn<GetJobRequest, Job> {
    private final DataflowGetJobs spec;
    private transient JobsV1Beta3Grpc.@MonotonicNonNull JobsV1Beta3BlockingStub client;

    GetJobsFn(DataflowGetJobs spec) {
      this.spec = spec;
    }

    @Setup
    public void setup() {
      client = DataflowClientFactory.createJobsClient(spec.configuration);
    }

    @ProcessElement
    public void process(@Element GetJobRequest request, MultiOutputReceiver receiver) {
      try {
        Job job = checkStateNotNull(client).getJob(request);
        receiver.get(SUCCESS).output(job);
      } catch (StatusRuntimeException e) {
        receiver.get(FAILURE).output(DataflowRequestError.<GetJobRequest>builder().build());
      }
    }
  }
}
