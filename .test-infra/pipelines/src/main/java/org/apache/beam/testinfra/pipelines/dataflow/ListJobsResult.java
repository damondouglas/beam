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

import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.model.ListJobsResponse;
import java.util.Map;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.PInput;
import org.apache.beam.sdk.values.POutput;
import org.apache.beam.sdk.values.PValue;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableMap;

public class ListJobsResult implements POutput {

  public static ListJobsResult from(PCollectionTuple pct) {
    return new ListJobsResult(pct);
  }

  static final TupleTag<ListJobsRequest> WITH_NEXT_PAGE_TOKEN =
      new TupleTag<ListJobsRequest>() {};

  static final TupleTag<ListJobsResponse> SUCCESS = new TupleTag<ListJobsResponse>() {};

  static final TupleTag<DataflowApiError> FAILURE = new TupleTag<DataflowApiError>() {};

  private final Pipeline pipeline;

  private final PCollection<ListJobsRequest> withNextPageToken;
  private final PCollection<ListJobsResponse> success;

  private final PCollection<DataflowApiError> failure;

  private ListJobsResult(PCollectionTuple pct) {
    this.pipeline = pct.getPipeline();
    this.withNextPageToken = pct.get(WITH_NEXT_PAGE_TOKEN);
    this.success = pct.get(SUCCESS);
    this.failure = pct.get(FAILURE);
  }

  public PCollection<ListJobsRequest> getWithNextPageToken() {
    return withNextPageToken;
  }

  public PCollection<ListJobsResponse> getSuccess() {
    return success;
  }

  public PCollection<DataflowApiError> getFailure() {
    return failure;
  }

  @Override
  public Pipeline getPipeline() {
    return pipeline;
  }

  @Override
  public Map<TupleTag<?>, PValue> expand() {
    return ImmutableMap.of(
        WITH_NEXT_PAGE_TOKEN, withNextPageToken,
        SUCCESS, success,
        FAILURE, failure);
  }

  @Override
  public void finishSpecifyingOutput(
      String transformName, PInput input, PTransform<?, ?> transform) {}
}
