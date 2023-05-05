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
import com.google.dataflow.v1beta3.ListJobsResponse;
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
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;

public class DataflowListJobsResult implements POutput {

  public static DataflowListJobsResult of(PCollectionTuple pct) {
    return new DataflowListJobsResult(pct);
  }

  static final TupleTag<ListJobsResponse> SUCCESS = new TupleTag<ListJobsResponse>() {};
  static final TupleTag<ListJobsRequest> TOKENIZED_REQUESTS = new TupleTag<ListJobsRequest>() {};
  static final TupleTag<DataflowListJobsError> FAILURE = new TupleTag<DataflowListJobsError>() {};
  private final Pipeline pipeline;

  private final PCollection<ListJobsResponse> success;

  private final PCollection<ListJobsRequest> tokenizedRequests;

  private final PCollection<DataflowListJobsError> failure;

  private DataflowListJobsResult(PCollectionTuple pct) {
    this.pipeline = pct.getPipeline();
    this.success = pct.get(SUCCESS);
    this.tokenizedRequests = pct.get(TOKENIZED_REQUESTS);
    this.failure = pct.get(FAILURE);
  }

  public PCollection<ListJobsResponse> getSuccess() {
    return success;
  }

  public PCollection<ListJobsRequest> getTokenizedRequests() {
    return tokenizedRequests;
  }

  public PCollection<DataflowListJobsError> getFailure() {
    return failure;
  }

  @Override
  public @UnknownKeyFor @NonNull @Initialized Pipeline getPipeline() {
    return pipeline;
  }

  @Override
  public Map<TupleTag<?>, PValue> expand() {
    return ImmutableMap.of(
        SUCCESS, success,
        FAILURE, failure,
        TOKENIZED_REQUESTS, tokenizedRequests);
  }

  @Override
  public void finishSpecifyingOutput(
      @UnknownKeyFor @NonNull @Initialized String transformName,
      @UnknownKeyFor @NonNull @Initialized PInput input,
      @UnknownKeyFor @NonNull @Initialized
          PTransform<@UnknownKeyFor @NonNull @Initialized ?, @UnknownKeyFor @NonNull @Initialized ?>
              transform) {}
}
