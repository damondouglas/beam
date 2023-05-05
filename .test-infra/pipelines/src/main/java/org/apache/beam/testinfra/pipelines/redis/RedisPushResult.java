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
package org.apache.beam.testinfra.pipelines.redis;

import java.util.Map;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.PInput;
import org.apache.beam.sdk.values.POutput;
import org.apache.beam.sdk.values.PValue;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableMap;

public class RedisPushResult implements POutput {

  static RedisPushResult of(PCollectionTuple pct) {
    return new RedisPushResult(pct);
  }

  static final TupleTag<KV<String, Long>> SUCCESS = new TupleTag<KV<String, Long>>() {};

  static final TupleTag<KV<String, RedisError>> FAILURE = new TupleTag<KV<String, RedisError>>() {};

  private final Pipeline pipeline;
  private final PCollection<KV<String, Long>> success;
  private final PCollection<KV<String, RedisError>> failure;

  private RedisPushResult(PCollectionTuple pct) {
    this.pipeline = pct.getPipeline();
    this.success = pct.get(SUCCESS);
    this.failure = pct.get(FAILURE);
  }

  public PCollection<KV<String, Long>> getSuccess() {
    return success;
  }

  public PCollection<KV<String, RedisError>> getFailure() {
    return failure;
  }

  @Override
  public Pipeline getPipeline() {
    return pipeline;
  }

  @Override
  public Map<TupleTag<?>, PValue> expand() {
    return ImmutableMap.of(
        SUCCESS, success,
        FAILURE, failure);
  }

  @Override
  public void finishSpecifyingOutput(
      String transformName, PInput input, PTransform<?, ?> transform) {}
}
