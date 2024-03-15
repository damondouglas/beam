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

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import com.google.api.core.ApiFuture;
import com.google.api.core.ForwardingApiFuture;
import com.google.cloud.run.v2.JobsClient;
import com.google.cloud.run.v2.RunJobRequest;
import com.google.longrunning.Operation;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.apache.beam.io.requestresponse.Caller;
import org.apache.beam.io.requestresponse.RequestResponseIO;
import org.apache.beam.io.requestresponse.Result;
import org.apache.beam.io.requestresponse.SetupTeardown;
import org.apache.beam.io.requestresponse.UserCodeExecutionException;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

class EventarcJobInvoker implements Caller<RunJobRequest, Operation>, SetupTeardown {
  static PTransform<PCollection<RunJobRequest>, Result<Operation>> pTransform() {
    return RequestResponseIO.ofCallerAndSetupTeardown(
        new EventarcJobInvoker(), new EventarcOperationCoder());
  }

  private transient @MonotonicNonNull JobsClient client;

  @Override
  public Operation call(RunJobRequest request) throws UserCodeExecutionException {
    JobsClient safeClient = checkStateNotNull(client);
    ApiFuture<Operation> response = safeClient.runJobCallable().futureCall(request);
    ForwardingApiFuture<Operation> forwardingApiFuture = new ForwardingApiFuture<>(response);
    try {
      return forwardingApiFuture.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new UserCodeExecutionException(e);
    }
  }

  @Override
  public void setup() throws UserCodeExecutionException {
    try {
      client = JobsClient.create();
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
