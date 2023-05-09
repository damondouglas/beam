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

import com.google.dataflow.v1beta3.ListJobsRequest;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.testinfra.pipelines.common.GeneratedMessageV3Conversions;

public final class DataflowTypeConversions {
  private static final TypeDescriptor<ListJobsRequest> LIST_JOBS_REQUEST_TYPE =
      TypeDescriptor.of(ListJobsRequest.class);

  public static MapElements.MapWithFailures<ByteString, ListJobsRequest, String>
      listJobsRequestsFromByteStrings() {
    return GeneratedMessageV3Conversions.fromByteStringsWithFailures(
        LIST_JOBS_REQUEST_TYPE, listJobsRequestsFromByteStringsFn());
  }

  public static MapElements.MapWithFailures<ListJobsRequest, ByteString, String>
      listJobsRequestsToByteStrings() {
    return GeneratedMessageV3Conversions.toByteStringsWithFailures();
  }

  private static SerializableFunction<ByteString, ListJobsRequest>
      listJobsRequestsFromByteStringsFn() {
    return input -> {
      try {
        return ListJobsRequest.parseFrom(input);
      } catch (InvalidProtocolBufferException e) {
        throw new IllegalStateException(e);
      }
    };
  }
}
