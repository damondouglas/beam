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

import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.dataflow.v1beta3.GetJobMetricsRequest;
import com.google.dataflow.v1beta3.JobMetrics;
import com.google.dataflow.v1beta3.MetricsV1Beta3Client;
import java.io.IOException;
import org.apache.beam.io.requestresponse.Caller;
import org.apache.beam.io.requestresponse.RequestResponseIO;
import org.apache.beam.io.requestresponse.Result;
import org.apache.beam.io.requestresponse.SetupTeardown;
import org.apache.beam.io.requestresponse.UserCodeExecutionException;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

class GetJobMetrics implements Caller<GetJobMetricsRequest, JobMetrics>, SetupTeardown {

  private transient @MonotonicNonNull MetricsV1Beta3Client client;

  public static PTransform<PCollection<GetJobMetricsRequest>, Result<JobMetrics>> pTransform() {
    return RequestResponseIO.ofCallerAndSetupTeardown(new GetJobMetrics(), new JobMetricsCoder());
  }

  private GetJobMetrics() {}

  @Override
  public JobMetrics call(GetJobMetricsRequest request) throws UserCodeExecutionException {
    try {
      return checkStateNotNull(client).getJobMetrics(request);
    } catch (UncheckedExecutionException e) {
      throw new UserCodeExecutionException(e);
    }
  }

  @Override
  public void setup() throws UserCodeExecutionException {
    try {
      client = MetricsV1Beta3Client.create();
    } catch (IOException e) {
      throw new UserCodeExecutionException(e);
    }
  }

  @Override
  public void teardown() throws UserCodeExecutionException {
    if (client != null) {
      client.close();
    }
  }
}
