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

import com.google.api.gax.rpc.ApiException;
import org.apache.beam.io.requestresponse.UserCodeExecutionException;
import org.apache.beam.io.requestresponse.UserCodeQuotaException;
import org.apache.beam.io.requestresponse.UserCodeRemoteSystemException;
import org.apache.beam.io.requestresponse.UserCodeTimeoutException;

final class ApiExceptions {
  static void handle(ApiException e) throws UserCodeExecutionException {
    switch (e.getStatusCode().getCode()) {
      case RESOURCE_EXHAUSTED:
        throw new UserCodeQuotaException(e);
      case INTERNAL:
      case UNAVAILABLE:
      case UNKNOWN:
        throw new UserCodeRemoteSystemException(e);
      case DEADLINE_EXCEEDED:
        throw new UserCodeTimeoutException(e);
      default:
        throw new UserCodeExecutionException(e);
    }
  }
}
