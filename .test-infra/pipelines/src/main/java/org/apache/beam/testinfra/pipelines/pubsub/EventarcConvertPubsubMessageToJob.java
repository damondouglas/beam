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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.events.cloud.dataflow.v1beta3.Job;
import com.google.events.cloud.dataflow.v1beta3.JobEventData;
import com.google.protobuf.util.JsonFormat;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.PInput;
import org.apache.beam.sdk.values.POutput;
import org.apache.beam.sdk.values.PValue;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventarcConvertPubsubMessageToJob
    extends PTransform<PCollection<PubsubMessage>, EventarcConvertPubsubMessageToJob.Result> {

  private static final Logger LOG =
      LoggerFactory.getLogger(EventarcConvertPubsubMessageToJob.class);

  public static PTransform<PCollection<PubsubMessage>, EventarcConvertPubsubMessageToJob.Result>
      of() {
    return new EventarcConvertPubsubMessageToJob();
  }

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final String DATA = "data";

  private final TupleTag<Job> output = new TupleTag<Job>() {};
  private final TupleTag<Pair<String, String>> failure = new TupleTag<Pair<String, String>>() {};

  @Override
  public EventarcConvertPubsubMessageToJob.Result expand(PCollection<PubsubMessage> input) {
    PCollectionTuple pct =
        input.apply(
            JsonToJobFn.class.getSimpleName(),
            ParDo.of(new JsonToJobFn()).withOutputTags(output, TupleTagList.of(failure)));
    return EventarcConvertPubsubMessageToJob.Result.of(pct, output, failure);
  }

  private class JsonToJobFn extends DoFn<PubsubMessage, Job> {
    @ProcessElement
    public void process(@Element PubsubMessage message, MultiOutputReceiver receiver) {
      try {
        JsonNode node = OBJECT_MAPPER.readTree(message.getPayload());
        if (!node.has(DATA)) {
          throw new IllegalArgumentException("missing field: " + DATA);
        }
        String data = node.get(DATA).toString();
        JobEventData.Builder builder = JobEventData.newBuilder();
        JsonFormat.parser().ignoringUnknownFields().merge(data, builder);
        JobEventData job = builder.build();
        LOG.info(JsonFormat.printer().omittingInsignificantWhitespace().print(job));
        receiver.get(output).output(job.getPayload());
      } catch (IOException | IllegalArgumentException e) {
        String json = new String(message.getPayload(), StandardCharsets.UTF_8);
        Pair<String, String> pair = encodePairOf(json, e);
        receiver.get(failure).output(pair);
      }
    }
  }

  private static Pair<String, String> encodePairOf(String json, Exception e) {
    return Pair.of(json, Throwables.getStackTraceAsString(e));
  }

  public static class Result implements POutput {

    private final Pipeline pipeline;

    private final TupleTag<Job> outputTag;
    private final PCollection<Job> output;

    private final TupleTag<Pair<String, String>> failureTag;
    private final PCollection<Pair<String, String>> failures;

    static Result of(
        PCollectionTuple pct, TupleTag<Job> outputTag, TupleTag<Pair<String, String>> failureTag) {
      return new Result(pct, outputTag, failureTag);
    }

    Result(
        PCollectionTuple pct, TupleTag<Job> outputTag, TupleTag<Pair<String, String>> failureTag) {
      this.pipeline = pct.getPipeline();
      this.outputTag = outputTag;
      this.failureTag = failureTag;
      this.output = pct.get(outputTag);
      this.failures = pct.get(failureTag);
    }

    public PCollection<Job> getOutput() {
      return output;
    }

    public PCollection<Pair<String, String>> getFailures() {
      return failures;
    }

    @Override
    public Pipeline getPipeline() {
      return pipeline;
    }

    @Override
    public Map<TupleTag<?>, PValue> expand() {
      return ImmutableMap.of(
          outputTag, output,
          failureTag, failures);
    }

    @Override
    public void finishSpecifyingOutput(
        String transformName, PInput input, PTransform<?, ?> transform) {}
  }
}
