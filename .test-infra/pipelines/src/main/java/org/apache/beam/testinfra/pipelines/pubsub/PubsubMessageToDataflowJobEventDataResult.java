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
package org.apache.beam.testinfra.pipelines.pubsub;

import com.google.events.cloud.dataflow.v1beta3.JobEventData;
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

public class PubsubMessageToDataflowJobEventDataResult implements POutput {

  public static PubsubMessageToDataflowJobEventDataResult of(PCollectionTuple pct) {
    return new PubsubMessageToDataflowJobEventDataResult(pct);
  }

  static final TupleTag<JobEventData> SUCCESS = new TupleTag<JobEventData>() {};
  static final TupleTag<PubsubMessageToDataflowJobEventError> ERROR =
      new TupleTag<PubsubMessageToDataflowJobEventError>() {};

  private final Pipeline pipeline;

  private final PCollection<JobEventData> success;

  private final PCollection<PubsubMessageToDataflowJobEventError> error;

  private PubsubMessageToDataflowJobEventDataResult(PCollectionTuple pct) {
    this.pipeline = pct.getPipeline();
    this.success = pct.get(SUCCESS);
    this.error = pct.get(ERROR);
  }

  public PCollection<JobEventData> getSuccess() {
    return success;
  }

  public PCollection<PubsubMessageToDataflowJobEventError> getError() {
    return error;
  }

  @Override
  public @UnknownKeyFor @NonNull @Initialized Pipeline getPipeline() {
    return pipeline;
  }

  @Override
  public Map<TupleTag<?>, PValue>
      expand() {
    return ImmutableMap.of(
        SUCCESS, success,
        ERROR, error);
  }

  @Override
  public void finishSpecifyingOutput(
      @UnknownKeyFor @NonNull @Initialized String transformName,
      @UnknownKeyFor @NonNull @Initialized PInput input,
      @UnknownKeyFor @NonNull @Initialized
          PTransform<@UnknownKeyFor @NonNull @Initialized ?, @UnknownKeyFor @NonNull @Initialized ?>
              transform) {}
}
