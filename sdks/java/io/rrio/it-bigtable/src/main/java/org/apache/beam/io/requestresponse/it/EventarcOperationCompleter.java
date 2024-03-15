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

import com.google.cloud.run.v2.JobsClient;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.longrunning.Operation;
import com.google.longrunning.OperationsClient;
import com.google.longrunning.WaitOperationRequest;
import java.io.IOException;
import org.apache.beam.io.requestresponse.Caller;
import org.apache.beam.io.requestresponse.RequestResponseIO;
import org.apache.beam.io.requestresponse.Result;
import org.apache.beam.io.requestresponse.SetupTeardown;
import org.apache.beam.io.requestresponse.UserCodeExecutionException;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

class EventarcOperationCompleter implements Caller<Operation, Operation>, SetupTeardown {

  static PTransform<PCollection<Operation>, Result<Operation>> pTransform() {
    return RequestResponseIO.ofCallerAndSetupTeardown(
        new EventarcOperationCompleter(), new EventarcOperationCoder());
  }

  private transient @MonotonicNonNull JobsClient jobsClient;
  private transient @MonotonicNonNull OperationsClient operationsClient;

  @Override
  public Operation call(Operation request) throws UserCodeExecutionException {
    WaitOperationRequest waitOperationRequest =
        WaitOperationRequest.newBuilder().setName(request.getName()).build();

    try {
      return checkStateNotNull(operationsClient).waitOperationCallable().call(waitOperationRequest);
    } catch (UncheckedExecutionException e) {
      throw new UserCodeExecutionException(e);
    }
  }

  @Override
  public void setup() throws UserCodeExecutionException {
    try {
      jobsClient = JobsClient.create();
    } catch (IOException e) {
      throw new UserCodeExecutionException(e);
    }
    operationsClient = checkStateNotNull(jobsClient).getOperationsClient();
  }

  @Override
  public void teardown() throws UserCodeExecutionException {
    if (operationsClient != null) {
      operationsClient.close();
    }
    if (jobsClient != null) {
      jobsClient.close();
    }
  }
}
