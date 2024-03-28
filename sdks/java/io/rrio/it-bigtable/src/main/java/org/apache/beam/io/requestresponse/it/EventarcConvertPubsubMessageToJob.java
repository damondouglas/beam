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

import com.google.common.base.Throwables;
import com.google.events.cloud.dataflow.v1beta3.Job;
import com.google.events.cloud.dataflow.v1beta3.JobEventData;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.commons.lang3.tuple.Pair;

class EventarcConvertPubsubMessageToJob extends PTransform<PCollection<PubsubMessage>, PCollectionTuple> {
  private static final String DATA_CONTENT_TYPE = "ce-datacontenttype";
  private static final String ID = "ce-id";
  private static final String DATA_SCHEMA = "ce-dataschema";
  private static final String SOURCE = "ce-source";
  private static final String SUBJECT = "ce-subject";
  private static final String TIME = "ce-time";
  private static final String TYPE = "ce-type";
  private final TupleTag<Job> output = new TupleTag<Job>() {};
  private final TupleTag<Pair<String, String>> failure = new TupleTag<Pair<String, String>>() {};

  @Override
  public PCollectionTuple expand(PCollection<PubsubMessage> input) {
    return input.apply(
        JsonToJobFn.class.getSimpleName(),
        ParDo.of(new JsonToJobFn()).withOutputTags(output, TupleTagList.of(failure)));
  }

  public TupleTag<Job> getOutputTag() {
    return output;
  }

  public TupleTag<Pair<String, String>> getFailureTag() {
    return failure;
  }

  private class JsonToJobFn extends DoFn<PubsubMessage, Job> {
    @ProcessElement
    public void process(@Element PubsubMessage message, MultiOutputReceiver receiver) {
      CloudEvent cloudEvent = cloudEventFrom(message);
      String json = new String(message.getPayload(), StandardCharsets.UTF_8);
      try {
        Job job = jobFrom(cloudEvent);
        receiver.get(output).output(job);
      } catch (InvalidProtocolBufferException e) {
        Pair<String, String> pair = encodePairOf(json, e);
        receiver.get(failure).output(pair);
      }
    }
  }

  private static CloudEvent cloudEventFrom(PubsubMessage message) {
    return CloudEventBuilder.v1()
        .newBuilder()
        .withData(message.getPayload())
        .withDataSchema(
            URI.create(
                attributeOf(
                    DATA_SCHEMA,
                    message,
                    "https://googleapis.github.io/google-cloudevents/proto/google/events/cloud/dataflow/v1beta3/data.proto#JobEventData")))
        .withType(attributeOf(TYPE, message, "google.cloud.dataflow.job.v1beta3.statusChanged"))
        .withId(attributeOf(ID, message, UUID.randomUUID().toString()))
        .withSource(
            URI.create(
                attributeOf(
                    SOURCE, message, "jobs/google.cloud.dataflow.job.v1beta3.statusChanged")))
        .withDataContentType(
            attributeOf(DATA_CONTENT_TYPE, message, io.cloudevents.jackson.JsonFormat.CONTENT_TYPE))
        .withSubject(
            attributeOf(SUBJECT, message, "jobs/google.cloud.dataflow.job.v1beta3.statusChanged"))
        .withTime(OffsetDateTime.parse(attributeOf(TIME, message, OffsetDateTime.now().toString())))
        .build();
  }

  private static String attributeOf(String key, PubsubMessage message, String defaultValue) {
    Optional<String> optional = Optional.ofNullable(message.getAttribute(key));
    return optional.orElse(defaultValue);
  }

  private static Job jobFrom(CloudEvent event) throws InvalidProtocolBufferException {
    byte[] bytes = checkStateNotNull(event.getData()).toBytes();
    String json = new String(bytes, StandardCharsets.UTF_8);
    JobEventData.Builder builder = JobEventData.newBuilder();
    JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
    return builder.build().getPayload();
  }

  private static Pair<String, String> encodePairOf(String json, Exception e) {
    return Pair.of(json, Throwables.getStackTraceAsString(e));
  }

  static String inputOf(Pair<String, String> pair) {
    return pair.getKey();
  }

  static String errorOf(Pair<String, String> pair) {
    return pair.getValue();
  }
}
