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
package org.apache.beam.examples.vertexai;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;
import static org.apache.beam.sdk.values.TypeDescriptors.strings;
import static org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Preconditions.checkState;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.Metric;
import com.google.api.MonitoredResource;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.aiplatform.v1.DoubleArray;
import com.google.cloud.aiplatform.v1.EntityTypeName;
import com.google.cloud.aiplatform.v1.FeatureValue;
import com.google.cloud.aiplatform.v1.FeaturestoreOnlineServingServiceClient;
import com.google.cloud.aiplatform.v1.FeaturestoreOnlineServingServiceSettings;
import com.google.cloud.aiplatform.v1.Int64Array;
import com.google.cloud.aiplatform.v1.StringArray;
import com.google.cloud.aiplatform.v1.WriteFeatureValuesPayload;
import com.google.cloud.aiplatform.v1.WriteFeatureValuesRequest;
import com.google.cloud.aiplatform.v1.WriteFeatureValuesResponse;
import com.google.cloud.aiplatform.v1.stub.FeaturestoreOnlineServingServiceStubSettings;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload.JsonPayload;
import com.google.cloud.logging.Severity;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.Point;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.monitoring.v3.TypedValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineRunner;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.GroupIntoBatches;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.PeriodicImpulse;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.Values;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.transforms.windowing.DefaultTrigger;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.PInput;
import org.apache.beam.sdk.values.POutput;
import org.apache.beam.sdk.values.PValue;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Throwables;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.collect.ImmutableList;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class FeaturestoreWriteSyntheaExample {

  private static final Counter SUCCESS =
      Metrics.counter(FeaturestoreWriteSyntheaExample.class, "success");

  private static final Counter FAILURE =
      Metrics.counter(FeaturestoreWriteSyntheaExample.class, "failure");

  private static final Counter REQUESTS =
      Metrics.counter(FeaturestoreWriteSyntheaExample.class, "requests");

  public interface Options extends PipelineOptions {

    @Required
    String getFeaturestoreId();

    void setFeaturestoreId(String value);

    @Required
    String getCacheHost();

    void setCacheHost(String value);

    @Required
    Integer getCachePort();

    void setCachePort(Integer value);

    @Required
    Long getQuotaRefreshIntervalSeconds();

    void setQuotaRefreshIntervalSeconds(Long value);

    @Required
    Long getQuotaRefreshValue();

    void setQuotaRefreshValue(Long value);

    @Required
    String getQuotaId();

    void setQuotaId(String value);

    @Required
    String getQueueId();

    void setQueueId(String value);

    @Required
    String getConditionPath();

    void setConditionPath(String value);

    @Required
    String getMedicationPath();

    void setMedicationPath(String value);

    @Required
    String getObservationPath();

    void setObservationPath(String value);

    @Required
    String getDataset();
    void setDataset(String value);
  }

  public static void main(String[] args) throws IOException {
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
    Pipeline p = Pipeline.create(options);

    DataflowPipelineOptions gcpOptions = options.as(DataflowPipelineOptions.class);

    String projectId = checkStateNotNull(gcpOptions.getProject(), "--project is required");
    String location = checkStateNotNull(gcpOptions.getRegion(), "--region is required");
    String featurestoreId = options.getFeaturestoreId();
    String host = options.getCacheHost();
    int port = options.getCachePort();
    String quotaId = options.getQuotaId();
    String queueId = options.getQueueId();
    Duration refreshInterval = Duration.standardSeconds(options.getQuotaRefreshIntervalSeconds());
    long refreshValue = options.getQuotaRefreshValue();

    SerializableFunction<WriteFeatureValuesRequest, byte[]> encodeFn = new EncodeRequestFn();
    SerializableFunction<byte[], WriteFeatureValuesRequest> decodeFn = new DecodeRequestFn();

    PCollection<String> data = PCollectionList
        .of(p.apply("condition/read", TextIO.read().from(options.getConditionPath())))
        .and(p.apply("medication/read", TextIO.read().from(options.getMedicationPath())))
        .and( p.apply("observation/read", TextIO.read().from(options.getObservationPath())))
        .apply("flatten", Flatten.pCollections());

    PCollection<JsonPayload> refreshErrors =
        p.apply("quotaRefresh/impulse", PeriodicImpulse.create().withInterval(refreshInterval))
            .apply("quotaRefresh/window", Window.into(FixedWindows.of(refreshInterval)))
            .apply(
                "quotaRefresh/do",
                new RefreshQuota(host, port, quotaId, refreshValue, refreshInterval));

    PCollection<JsonPayload> monitorQuotaErrors =
        p.apply(
                "monitorQuota/impulse",
                PeriodicImpulse.create().withInterval(Duration.standardSeconds(10)))
            .apply("monitorQuota/window", Window.into(FixedWindows.of(Duration.standardSeconds(10))))
            .apply("monitorQuota/do", new MeasureQuota(projectId, host, port, quotaId));

    PCollection<JsonPayload> enqueueErrors = data
            .apply(
                "generateWriteRequests",
                new GenerateWriteRequests(projectId, location, featurestoreId))
            .apply("enqueueRequests", new EnqueueRequests<>(host, port, queueId, encodeFn));

    TupleTag<WriteFeatureValuesRequest> dequeueSuccess =
        new TupleTag<WriteFeatureValuesRequest>() {};

    PCollectionTuple dequeued =
        p.apply(
                "executeRequests/impulse",
                PeriodicImpulse.create().withInterval(Duration.millis(1)))
            .apply("executeRequests/window", Window.into(FixedWindows.of(Duration.millis(1))))
            .apply(
                "executeRequests/dequeue",
                new DequeueRequests<>(dequeueSuccess, host, port, queueId, decodeFn));

    RequestResponseIOResult<JsonPayload, WriteFeatureValuesResponse> result =
        dequeued
            .get(dequeueSuccess)
            .apply(
                "executeRequests/do",
                new RequestResponseIO<WriteFeatureValuesRequest, WriteFeatureValuesResponse>(
                    new TupleTag<WriteFeatureValuesResponse>() {},
                    new TupleTag<JsonPayload>() {},
                    new ExecuteWriteRequestsFn(location),
                    encodeFn,
                    host,
                    port,
                    quotaId,
                    queueId));

    Logger logger =
        new Logger(
            Severity.ERROR,
            options.getRunner(),
            FeaturestoreWriteSyntheaExample.class.getSimpleName());

    monitorQuotaErrors.apply("logErrors/monitoring", logger);
    refreshErrors.apply("logErrors/refreshQuota", logger);
    enqueueErrors.apply("logErrors/enqueue", logger);
    dequeued.get(DequeueRequests.ERRORS).apply("logErrors/dequeue", logger);
    result.getFailure().apply("logErrors/rrio", logger);

    p.run();
  }

  private abstract static class RequestResponseIOExecuteFn<RequestT, ResponseT>
      implements SerializableFunction<RequestT, ResponseT> {

    abstract void close();
  }

  private static class ExecuteWriteRequestsFn
      extends RequestResponseIOExecuteFn<WriteFeatureValuesRequest, WriteFeatureValuesResponse> {

    private final String location;
    private transient @Nullable FeaturestoreOnlineServingServiceClient client = null;

    private ExecuteWriteRequestsFn(String location) {
      this.location = location;
    }

    @Override
    void close() {
      if (client != null) {
        client.close();
      }
    }

    private @NonNull FeaturestoreOnlineServingServiceClient getOrInstantiateClient() {
      if (client == null) {
        try {
          client =
              FeaturestoreOnlineServingServiceClient.create(
                  FeaturestoreOnlineServingServiceSettings.create(
                      FeaturestoreOnlineServingServiceStubSettings.newBuilder()
                          .setEndpoint(location + "-aiplatform.googleapis.com:443")
                          .build()));
        } catch (IOException e) {
          throw new IllegalStateException(e);
        }
      }
      return checkStateNotNull(client);
    }

    @Override
    public WriteFeatureValuesResponse apply(WriteFeatureValuesRequest input) {
      try {
        return getOrInstantiateClient().writeFeatureValues(input);
      } catch (ApiException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  private static class EncodeRequestFn
      implements SerializableFunction<WriteFeatureValuesRequest, byte[]> {

    @Override
    public byte[] apply(WriteFeatureValuesRequest input) {
      return checkStateNotNull(input).toByteArray();
    }
  }

  private static class DequeueRequests<RequestT>
      extends PTransform<PCollection<Instant>, PCollectionTuple> {

    private static final TupleTag<JsonPayload> ERRORS = new TupleTag<JsonPayload>() {
    };
    private final TupleTag<RequestT> successTag;
    private final String host;
    private final int port;
    private final String queueId;
    private final SerializableFunction<byte[], RequestT> decodeFn;

    private DequeueRequests(
        TupleTag<RequestT> successTag,
        String host,
        int port,
        String queueId,
        SerializableFunction<byte[], RequestT> decodeFn) {
      this.successTag = successTag;
      this.host = host;
      this.port = port;
      this.queueId = queueId;
      this.decodeFn = decodeFn;
    }

    @Override
    public PCollectionTuple expand(PCollection<Instant> input) {
      return input.apply(
          "dequeueRequestsFn",
          ParDo.of(new DequeueRequestsFn<>(this))
              .withOutputTags(successTag, TupleTagList.of(ERRORS)));
    }
  }

  private static class DequeueRequestsFn<RequestT> extends RedisDoFn<Instant, RequestT> {

    private final DequeueRequests<RequestT> spec;

    private DequeueRequestsFn(DequeueRequests<RequestT> spec) {
      super(spec.host, spec.port);
      this.spec = spec;
    }

    @ProcessElement
    public void process(MultiOutputReceiver receiver) {
      byte[] data = new byte[0];
      try {
        if (!getRedisClient().exists(spec.queueId)) {
          return;
        }
        data = getRedisClient().dequeue(spec.queueId);
        RequestT request = spec.decodeFn.apply(data);
        receiver.get(spec.successTag).output(request);
      } catch (RedisClientException e) {
        receiver.get(DequeueRequests.ERRORS).output(e.toJsonPayload());
      } catch (IllegalStateException e) {
        receiver
            .get(DequeueRequests.ERRORS)
            .output(from(ByteString.copyFrom(data).toStringUtf8(), e));
      }
    }
  }

  private static class EnqueueRequests<RequestT>
      extends PTransform<PCollection<RequestT>, PCollection<JsonPayload>> {

    private final String host;
    private final int port;
    private final String queueId;
    private final SerializableFunction<RequestT, byte[]> encodeFn;

    private EnqueueRequests(
        String host, int port, String queueId, SerializableFunction<RequestT, byte[]> encodeFn) {
      this.host = host;
      this.port = port;
      this.queueId = queueId;
      this.encodeFn = encodeFn;
    }

    @Override
    public PCollection<JsonPayload> expand(PCollection<RequestT> input) {
      return input.apply("enqueue", ParDo.of(new EnqueueRequestsFn<>(this)));
    }
  }

  private static class EnqueueRequestsFn<RequestT> extends RedisDoFn<RequestT, JsonPayload> {

    private final EnqueueRequests<RequestT> spec;

    private EnqueueRequestsFn(EnqueueRequests<RequestT> spec) {
      super(spec.host, spec.port);
      this.spec = spec;
    }

    @ProcessElement
    public void process(@Element RequestT element, OutputReceiver<JsonPayload> receiver) {
      try {
        byte[] data = spec.encodeFn.apply(element);
        getRedisClient().enqueue(spec.queueId, data);
      } catch (RedisClientException e) {
        receiver.output(e.toJsonPayload());
      }
    }
  }

  private static class DecodeRequestFn
      implements SerializableFunction<byte[], WriteFeatureValuesRequest> {

    @Override
    public WriteFeatureValuesRequest apply(byte[] input) {
      try {
        return WriteFeatureValuesRequest.parseFrom(input);
      } catch (InvalidProtocolBufferException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  private static class RequestResponseIO<RequestT, ResponseT>
      extends PTransform<PCollection<RequestT>, RequestResponseIOResult<JsonPayload, ResponseT>> {

    private final TupleTag<ResponseT> successTag;
    private final TupleTag<JsonPayload> failureTag;
    private final SerializableFunction<RequestT, ResponseT> executeFn;

    private final SerializableFunction<RequestT, byte[]> encodeFn;

    private final String host;

    private final int port;
    private final String quotaId;
    private final String queueId;

    RequestResponseIO(
        TupleTag<ResponseT> successTag,
        TupleTag<JsonPayload> failureTag,
        SerializableFunction<RequestT, ResponseT> executeFn,
        SerializableFunction<RequestT, byte[]> encodeFn,
        String host,
        int port,
        String quotaId,
        String queueId) {
      this.successTag = successTag;
      this.failureTag = failureTag;
      this.executeFn = executeFn;
      this.encodeFn = encodeFn;
      this.host = host;
      this.port = port;
      this.quotaId = quotaId;
      this.queueId = queueId;
    }

    @Override
    public RequestResponseIOResult<JsonPayload, ResponseT> expand(PCollection<RequestT> input) {
      PCollectionTuple pct =
          input.apply(
              "execute",
              ParDo.of(new RequestResponseFn<>(this))
                  .withOutputTags(successTag, TupleTagList.of(failureTag)));
      return new RequestResponseIOResult<>(successTag, failureTag, pct);
    }
  }

  private static class RequestResponseFn<RequestT, ResponseT, ErrorT>
      extends RedisDoFn<RequestT, ResponseT> {

    private final RequestResponseIO<RequestT, ResponseT> spec;

    RequestResponseFn(RequestResponseIO<RequestT, ResponseT> spec) {
      super(spec.host, spec.port);
      this.spec = spec;
    }

    @ProcessElement
    public void process(@Element RequestT request, MultiOutputReceiver receiver) {
      REQUESTS.inc();
      RedisClient safeClient = getRedisClient();
      try {
        if (!safeClient.exists(spec.quotaId)) {
          return;
        }
        Long quota = safeClient.get(spec.quotaId);
        if (quota <= 0) {
          byte[] encoded = spec.encodeFn.apply(request);
          safeClient.enqueue(spec.queueId, encoded);
          return;
        }
        safeClient.decr(spec.quotaId);
        ResponseT response = spec.executeFn.apply(request);
        receiver.get(spec.successTag).output(response);
        SUCCESS.inc();
      } catch (RedisClientException e) {
        FAILURE.inc();
        receiver.get(spec.failureTag).output(e.toJsonPayload());
      } catch (IllegalStateException e) {
        FAILURE.inc();
        receiver.get(spec.failureTag).output(from(checkStateNotNull(request).toString(), e));
      }
    }
  }

  private static class RequestResponseIOResult<ErrorT, ResponseT> implements POutput {

    private final Pipeline pipeline;
    private final TupleTag<ResponseT> successTag;
    private final PCollection<ResponseT> success;
    private final TupleTag<ErrorT> failureTag;
    private PCollection<ErrorT> failure;

    PCollection<ResponseT> getSuccess() {
      return success;
    }

    PCollection<ErrorT> getFailure() {
      return failure;
    }

    RequestResponseIOResult(
        TupleTag<ResponseT> successTag, TupleTag<ErrorT> failureTag, PCollectionTuple pct) {
      this.pipeline = pct.getPipeline();
      this.successTag = successTag;
      this.success = pct.get(successTag);
      this.failureTag = failureTag;
      this.failure = pct.get(failureTag);
    }

    @Override
    public Pipeline getPipeline() {
      return pipeline;
    }

    @Override
    public Map<TupleTag<?>, PValue> expand() {
      return ImmutableMap.of(
          successTag, success,
          failureTag, failure);
    }

    @Override
    public void finishSpecifyingOutput(
        String transformName, PInput input, PTransform<?, ?> transform) {
    }
  }

  private static class GenerateWriteRequests
      extends PTransform<PCollection<String>, PCollection<WriteFeatureValuesRequest>> {

    private final String projectId;
    private final String location;
    private final String featurestoreId;

    private GenerateWriteRequests(String projectId, String location, String featurestoreId) {
      this.projectId = projectId;
      this.location = location;
      this.featurestoreId = featurestoreId;
    }

    @Override
    public PCollection<WriteFeatureValuesRequest> expand(PCollection<String> input) {
      return input.apply("generateRequests", ParDo.of(new GenerateWriteRequestsFn(this)));
    }
  }

  private static class GenerateWriteRequestsFn extends DoFn<String, WriteFeatureValuesRequest> {

    private final GenerateWriteRequests spec;

    private GenerateWriteRequestsFn(GenerateWriteRequests spec) {
      this.spec = spec;
    }

    @ProcessElement
    public void process(OutputReceiver<WriteFeatureValuesRequest> receiver) {
      List<String> ids = ImmutableList.of("a", "b", "c", "d", "e");
      Random random = new Random(Instant.now().getMillis());
        String id = ids.get(random.nextInt(ids.size()));
        receiver.output(
            WriteFeatureValuesRequest.getDefaultInstance()
                .toBuilder()
                .setEntityType(
                    EntityTypeName.of(spec.projectId, spec.location, spec.featurestoreId, "foo")
                        .toString())
                .addPayloads(
                    WriteFeatureValuesPayload.getDefaultInstance()
                        .toBuilder()
                        .setEntityId(id)
                        .putFeatureValues(
                            "some_bytes",
                            FeatureValue.getDefaultInstance()
                                .toBuilder()
                                .setBytesValue(
                                    ByteString.copyFrom("abcdefg", StandardCharsets.UTF_8))
                                .build())
                        .putFeatureValues(
                            "an_int",
                            FeatureValue.getDefaultInstance()
                                .toBuilder()
                                .setInt64Value(random.nextLong())
                                .build())
                        .putFeatureValues(
                            "an_int_array",
                            FeatureValue.getDefaultInstance()
                                .toBuilder()
                                .setInt64ArrayValue(
                                    Int64Array.getDefaultInstance()
                                        .toBuilder()
                                        .addAllValues(
                                            ImmutableList.of(
                                                random.nextLong(),
                                                random.nextLong(),
                                                random.nextLong()))
                                        .build())
                                .build())
                        .putFeatureValues(
                            "a_double",
                            FeatureValue.getDefaultInstance()
                                .toBuilder()
                                .setDoubleValue(random.nextDouble())
                                .build())
                        .putFeatureValues(
                            "a_double_array",
                            FeatureValue.getDefaultInstance()
                                .toBuilder()
                                .setDoubleArrayValue(
                                    DoubleArray.getDefaultInstance()
                                        .toBuilder()
                                        .addAllValues(
                                            ImmutableList.of(
                                                random.nextDouble(),
                                                random.nextDouble(),
                                                random.nextDouble()))
                                        .build())
                                .build())
                        .putFeatureValues(
                            "a_string",
                            FeatureValue.getDefaultInstance()
                                .toBuilder()
                                .setStringValue("abcefgg")
                                .build())
                        .putFeatureValues(
                            "a_string_array",
                            FeatureValue.getDefaultInstance()
                                .toBuilder()
                                .setStringArrayValue(
                                    StringArray.getDefaultInstance()
                                        .toBuilder()
                                        .addAllValues(ImmutableList.of("a", "b", "c"))
                                        .build())
                                .build())
                        .build())
                .build());
      }
  }

  private static class RefreshQuota
      extends PTransform<PCollection<Instant>, PCollection<JsonPayload>> {

    private final String host;
    private final int port;
    private final String quotaId;
    private final long refreshValue;
    private final Duration refreshInterval;

    private RefreshQuota(
        String host, int port, String quotaId, long refreshValue, Duration refreshInterval) {
      this.host = host;
      this.port = port;
      this.quotaId = quotaId;
      this.refreshValue = refreshValue;
      this.refreshInterval = refreshInterval;
    }

    @Override
    public PCollection<JsonPayload> expand(PCollection<Instant> input) {
      return input.apply("refreshQuota", ParDo.of(new RefreshQuotaFn(this)));
    }
  }

  private static class RefreshQuotaFn extends RedisDoFn<Instant, JsonPayload> {

    private final RefreshQuota spec;

    private RefreshQuotaFn(RefreshQuota spec) {
      super(spec.host, spec.port);
      this.spec = spec;
    }

    @ProcessElement
    public void process(OutputReceiver<JsonPayload> receiver) {
      RedisClient client = getRedisClient();
      try {
        client.setex(spec.quotaId, spec.refreshValue, spec.refreshInterval);
      } catch (RedisClientException e) {
        receiver.output(e.toJsonPayload());
      }
    }
  }

  private abstract static class RedisDoFn<InputT, OutputT> extends DoFn<InputT, OutputT> {

    private final String host;
    private final int port;

    private transient @MonotonicNonNull RedisClient redisClient;

    private RedisDoFn(String host, int port) {
      this.host = host;
      this.port = port;
    }

    @Setup
    public void setup() {
      redisClient = new RedisClient(host, port);
    }

    @Teardown
    public void teardown() {
      if (redisClient != null) {
        checkStateNotNull(redisClient).close();
      }
    }

    protected @NonNull RedisClient getRedisClient() {
      return checkStateNotNull(redisClient);
    }
  }

  private static class RedisClient implements Serializable {

    private final transient @Nonnull Jedis client;
    private final String host;
    private final int port;

    RedisClient(String host, int port) {
      this.host = host;
      this.port = port;
      this.client = new Jedis(host, port);
    }

    boolean exists(String key) throws RedisClientException {
      try {
        return client.exists(key);
      } catch (JedisException e) {
        throw new RedisClientException(e, formatSource("exists", key));
      }
    }

    <T> T get(String key, Function<String, T> convertFn) throws RedisClientException {
      try {
        String value = client.get(key);
        return convertFn.apply(value);
      } catch (NumberFormatException | JedisException e) {
        throw new RedisClientException(e, formatSource("get", key));
      }
    }

    Long get(String key) throws RedisClientException {
      return get(key, Long::parseLong);
    }

    void decr(String key) throws RedisClientException {
      try {
        client.decr(key);
      } catch (JedisException e) {
        throw new RedisClientException(e, formatSource("decr", key));
      }
    }

    byte[] dequeue(String key) throws RedisClientException {
      try {
        String data = client.lpop(key);
        return Base64.getDecoder().decode(data);
      } catch (JedisException e) {
        throw new RedisClientException(e, formatSource("lpop", key));
      }
    }

    void enqueue(String key, byte[] data) throws RedisClientException {
      try {
        client.rpush(key, Base64.getEncoder().encodeToString(data));
      } catch (JedisException e) {
        throw new RedisClientException(e, formatSource("rpush", key), data);
      }
    }

    void setex(String key, long value, Duration ttl) throws RedisClientException {
      try {
        client.setex(key, ttl.getStandardSeconds(), String.valueOf(value));
      } catch (JedisException e) {
        throw new RedisClientException(
            e, formatSource("setex", key, ttl.toString(), String.valueOf(value)));
      }
    }

    void close() {
      client.close();
    }

    private String formatSource(String command, String key, String... addl) {
      return String.format(
          "redis://%s:%d:%s:%s:%s", host, port, command, key, String.join(":", addl));
    }
  }

  static class RedisClientException extends Throwable implements Serializable {

    private final Exception e;
    private final String source;

    private final byte @Nullable [] data;

    RedisClientException(Exception e, String source, byte @Nullable [] data) {
      this.e = e;
      this.source = source;
      this.data = data;
    }

    RedisClientException(Exception e, String source) {
      this(e, source, null);
    }

    JsonPayload toJsonPayload() {
      Struct.Builder builder =
          Struct.newBuilder()
              .putFields(
                  "message",
                  Value.newBuilder()
                      .setStringValue(Optional.ofNullable(e.getMessage()).orElse(""))
                      .build())
              .putFields(
                  "stackTrace",
                  Value.newBuilder().setStringValue(Throwables.getStackTraceAsString(e)).build())
              .putFields("source", Value.newBuilder().setStringValue(source).build());
      if (data != null) {
        builder.putFields(
            "data", Value.newBuilder().setStringValueBytes(ByteString.copyFrom(data)).build());
      }
      return JsonPayload.of(builder.build());
    }
  }

  private static class MeasureQuota
      extends PTransform<PCollection<Instant>, PCollection<JsonPayload>> {

    private static final TupleTag<KV<Instant, Point>> POINTS =
        new TupleTag<KV<Instant, Point>>() {
        };
    private static final TupleTag<JsonPayload> ERRORS = new TupleTag<JsonPayload>() {
    };

    private final String projectId;
    private final String host;
    private final int port;
    private final String quotaId;

    private MeasureQuota(String projectId, String host, int port, String quotaId) {
      this.projectId = projectId;
      this.host = host;
      this.port = port;
      this.quotaId = quotaId;
    }

    @Override
    public PCollection<JsonPayload> expand(PCollection<Instant> input) {
      PCollectionTuple pct =
          input
              .apply(
                  "measureQuota/window",
                  Window.<Instant>into(FixedWindows.of(Duration.standardSeconds(10L)))
                      .triggering(DefaultTrigger.of())
                      .discardingFiredPanes())
              .apply(
                  "measureQuota/points",
                  ParDo.of(new MeasureQuotaFn(this))
                      .withOutputTags(POINTS, TupleTagList.of(ERRORS)));
      PCollection<JsonPayload> errors =
          pct.get(POINTS)
              .apply(
                  "measureQuota/points/bundle",
                  GroupIntoBatches.<Instant, Point>ofSize(10L)
                      .withMaxBufferingDuration(Duration.standardSeconds(10L)))
              .apply("measureQuota/points/collect", Values.create())
              .apply("measureQuota/write", ParDo.of(new MetricServiceFn(this)));

      return PCollectionList.of(errors)
          .and(pct.get(ERRORS))
          .apply("measureQuota/errors/flatten", Flatten.pCollections());
    }
  }

  private static class MeasureQuotaFn extends RedisDoFn<Instant, KV<Instant, Point>> {

    private final MeasureQuota spec;

    MeasureQuotaFn(MeasureQuota spec) {
      super(spec.host, spec.port);
      this.spec = spec;
    }

    @ProcessElement
    public void process(@Element Instant element, MultiOutputReceiver receiver) {
      RedisClient redisClient = getRedisClient();
      try {
        if (!redisClient.exists(spec.quotaId)) {
          return;
        }
      } catch (RedisClientException ignored) {
      }
      try {
        com.google.protobuf.Timestamp timestamp =
            com.google.protobuf.Timestamp.getDefaultInstance()
                .toBuilder()
                .setSeconds(element.getMillis() / 1000)
                .build();
        Long value = getRedisClient().get(spec.quotaId);
        Point p =
            Point.getDefaultInstance()
                .toBuilder()
                .setInterval(
                    TimeInterval.getDefaultInstance().toBuilder().setEndTime(timestamp).build())
                .setValue(TypedValue.getDefaultInstance().toBuilder().setInt64Value(value).build())
                .build();
        receiver.get(MeasureQuota.POINTS).output(KV.of(element, p));
      } catch (RedisClientException e) {
        receiver.get(MeasureQuota.ERRORS).output(e.toJsonPayload());
      }
    }
  }

  private static class MetricServiceFn extends DoFn<Iterable<Point>, JsonPayload> {

    private final MeasureQuota spec;
    private transient @MonotonicNonNull MetricService metricService;

    MetricServiceFn(MeasureQuota spec) {
      this.spec = spec;
    }

    @Setup
    public void setup() {
      try {
        metricService = new MetricService(spec.projectId);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }

    @Teardown
    public void teardown() {
      if (metricService != null) {
        checkStateNotNull(metricService).close();
      }
    }

    @ProcessElement
    public void process(@Element Iterable<Point> element, OutputReceiver<JsonPayload> receiver)
        throws IOException {
      List<Point> points =
          StreamSupport.stream(element.spliterator(), false).collect(Collectors.toList());
      try {
        MetricService safeClient = checkStateNotNull(metricService);
        safeClient.write(spec.quotaId, points);
      } catch (ApiException e) {
        if (e.getMessage() != null && e.getMessage().contains("Points must be written in order")) {
          return;
        }
        receiver.output(from(points.toString(), e));
      }
    }
  }

  private static class MetricService implements Serializable {

    private static final MonitoredResource MONITORED_RESOURCE =
        MonitoredResource.newBuilder().setType("global").build();
    private static final String TYPE_PREFIX = "custom.googleapis.com";
    private transient @Nullable MetricServiceClient client;
    private final ProjectName projectName;

    MetricService(String projectId) throws IOException {
      projectName = ProjectName.of(projectId);
    }

    @NonNull MetricServiceClient getOrInstantiate() throws IOException {
      if (client == null) {
        client = MetricServiceClient.create();
      }
      return checkStateNotNull(client);
    }

    Metric getMetric(String name) {
      return Metric.newBuilder().setType(String.format("%s/%s", TYPE_PREFIX, name)).build();
    }

    void write(String name, List<Point> points) throws ApiException, IOException {
      Metric metric = getMetric(name);
      TimeSeries timeSeries =
          TimeSeries.newBuilder()
              .setMetric(metric)
              .setResource(MONITORED_RESOURCE)
              .addAllPoints(points)
              .build();

      CreateTimeSeriesRequest request =
          CreateTimeSeriesRequest.newBuilder()
              .setName(projectName.toString())
              .addTimeSeries(timeSeries)
              .build();
      getOrInstantiate().createTimeSeries(request);
    }

    void close() {
      if (client != null) {
        checkStateNotNull(client).close();
      }
    }
  }

  private static class Logger extends PTransform<PCollection<JsonPayload>, PCollection<Void>> {

    private final Severity severity;
    private final Class<? extends PipelineRunner<?>> runner;
    private final String logName;

    private Logger(Severity severity, Class<? extends PipelineRunner<?>> runner, String name) {
      this.severity = severity;
      this.runner = runner;
      this.logName = name;
    }

    static Logger error(PipelineOptions options, String name) {
      return new Logger(Severity.ERROR, options.getRunner(), name);
    }

    static Logger info(PipelineOptions options, String name) {
      return new Logger(Severity.INFO, options.getRunner(), name);
    }

    @Override
    public PCollection<Void> expand(PCollection<JsonPayload> input) {
      PCollection<Iterable<JsonPayload>> entries =
          input
              .apply("log/applyKey", WithKeys.of(0))
              .apply(
                  "log/bundle",
                  GroupIntoBatches.<Integer, JsonPayload>ofSize(10L)
                      .withMaxBufferingDuration(Duration.standardSeconds(3L)))
              .apply("log/iterables", Values.create());

      if (runner.equals(DataflowRunner.class)) {
        return entries.apply("log/write", ParDo.of(new DataflowLoggerFn(this)));
      }
      return entries.apply("log/write", ParDo.of(new LocalLoggerFn(this)));
    }
  }

  private static class DataflowLoggerFn extends DoFn<Iterable<JsonPayload>, Void> {

    private final Logger spec;
    private transient @MonotonicNonNull Logging client;

    private DataflowLoggerFn(Logger spec) {
      this.spec = spec;
    }

    @Setup
    public void setup() {
      client = LoggingOptions.getDefaultInstance().getService();
    }

    @Teardown
    public void teardown() throws Exception {
      if (client != null) {
        checkStateNotNull(client).close();
      }
    }

    @ProcessElement
    public void process(@Element Iterable<JsonPayload> element) {
      Logging safeClient = checkStateNotNull(client);

      List<LogEntry> entries = new ArrayList<>();
      for (JsonPayload payload : element) {
        entries.add(
            LogEntry.newBuilder(payload)
                .setSeverity(spec.severity)
                .setLogName(spec.logName)
                .build());
      }
      safeClient.write(entries);
      safeClient.flush();
    }
  }

  private static class LocalLoggerFn extends DoFn<Iterable<JsonPayload>, Void> {

    private static final Gson GSON = new Gson();
    private final org.slf4j.Logger logger;
    private final Logger spec;

    private LocalLoggerFn(Logger spec) {
      this.spec = spec;
      this.logger = LoggerFactory.getLogger(spec.logName);
    }

    @ProcessElement
    public void process(@Element Iterable<JsonPayload> element) {
      for (JsonPayload payload : element) {
        logFn().accept(GSON.toJson(payload));
      }
    }

    private Consumer<String> logFn() {
      if (spec.severity.equals(Severity.ERROR)) {
        return logger::error;
      }
      return logger::info;
    }
  }

  private static JsonPayload from(String request, IllegalStateException e) {
    return JsonPayload.of(
        Struct.newBuilder()
            .putFields("request", Value.newBuilder().setStringValue(request).build())
            .putFields(
                "message",
                Value.newBuilder()
                    .setStringValue(Optional.ofNullable(e.getMessage()).orElse(""))
                    .build())
            .putFields(
                "stackTrace",
                Value.newBuilder().setStringValue(Throwables.getStackTraceAsString(e)).build())
            .build());
  }

  private static JsonPayload from(String request, ApiException e) {
    return JsonPayload.of(
        Struct.newBuilder()
            .putFields("request", Value.newBuilder().setStringValue(request).build())
            .putFields(
                "message",
                Value.newBuilder()
                    .setStringValue(Optional.ofNullable(e.getMessage()).orElse(""))
                    .build())
            .putFields(
                "stackTrace",
                Value.newBuilder().setStringValue(Throwables.getStackTraceAsString(e)).build()
            )
            .build());
  }


  private static class Medication implements Serializable {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String code;
    private final Instant timestamp;
    private final String patient;

    @SuppressWarnings({"unused"})
    static ParDo.SingleOutput<String, Medication> fromJson() {
      return ParDo.of(new JsonToMedicationFn());
    }

    @SuppressWarnings({"unused"})
    static MapElements<Medication, JsonPayload> toJson() {
      return MapElements.into(TypeDescriptor.of(JsonPayload.class)).via(Medication::toJsonPayload);
    }

    Medication(String json) {
      try {
        JsonNode root = MAPPER.readTree(json);
        code =
            checkStateNotNull(
                root.get("medicationCodeableConcept").get("coding").get(0).get("code"))
                .asText();
        timestamp = Instant.parse(checkStateNotNull(root.get("effectiveDateTime")).asText());
        patient =
            checkStateNotNull(root.get("subject").get("reference"))
                .asText()
                .replaceAll("urn:uuid:", "");

      } catch (JsonProcessingException | IllegalStateException e) {
        throw new IllegalStateException(e);
      }
    }

    JsonPayload toJsonPayload() {
      return JsonPayload.of(Struct.newBuilder()
          .putFields("code", Value.newBuilder()
              .setStringValue(code)
              .build())
          .putFields("timestamp", Value.newBuilder()
              .setStringValue(timestamp.toString())
              .build())
          .putFields("patient", Value.newBuilder()
              .setStringValue(patient)
              .build())
          .build());
    }

    @Override
    public boolean equals(@Nullable Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Medication that = (Medication) o;
      return Objects.equal(code, that.code)
          && Objects.equal(timestamp, that.timestamp)
          && Objects.equal(patient, that.patient);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(code, timestamp, patient);
    }
  }

  private static class JsonToMedicationFn extends DoFn<String, Medication> {

    @ProcessElement
    public void process(@Element String json, OutputReceiver<Medication> receiver) {
      try {
        receiver.output(new Medication(json));
      } catch (IllegalStateException ignored) {

      }
    }
  }

  private static class Condition implements Serializable {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String code;
    private final Instant timestamp;
    private final String patient;

    @SuppressWarnings({"unused"})
    private static ParDo.SingleOutput<String, Condition> fromJson() {
      return ParDo.of(new JsonToConditionFn());
    }

    @SuppressWarnings({"unused"})
    static MapElements<Condition, JsonPayload> toJson() {
      return MapElements.into(TypeDescriptor.of(JsonPayload.class)).via(Condition::toJsonPayload);
    }

    Condition(String json) {
      try {
        JsonNode root = MAPPER.readTree(json);
        JsonNode code = checkStateNotNull(root.get("code"));
        JsonNode coding = checkStateNotNull(code.get("coding"));
        checkState(coding.isArray());
        checkState(!coding.isEmpty());
        JsonNode codingCode = checkStateNotNull(coding.get(0));
        this.code = checkStateNotNull(codingCode.get("code")).asText();
        this.timestamp = Instant.parse(checkStateNotNull(root.get("onsetDateTime")).asText());
        String reference = checkStateNotNull(root.get("subject").get("reference")).asText();
        this.patient = reference.replaceAll("urn:uuid:", "");
      } catch (JsonProcessingException | IllegalStateException e) {
        throw new IllegalStateException(e);
      }
    }

    JsonPayload toJsonPayload() {
      return JsonPayload.of(Struct.newBuilder()
          .putFields("code", Value.newBuilder()
              .setStringValue(code)
              .build())
          .putFields("timestamp", Value.newBuilder()
              .setStringValue(timestamp.toString())
              .build())
          .putFields("patient", Value.newBuilder()
              .setStringValue(patient)
              .build())
          .build());
    }

    @Override
    public boolean equals(@Nullable Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Condition condition = (Condition) o;
      return Objects.equal(code, condition.code)
          && Objects.equal(timestamp, condition.timestamp)
          && Objects.equal(patient, condition.patient);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(code, timestamp, patient);
    }
  }

  private static class JsonToConditionFn extends DoFn<String, Condition> {

    @ProcessElement
    public void process(@Element String json, OutputReceiver<Condition> receiver) {
      try {
        receiver.output(new Condition(json));
      } catch (IllegalStateException ignored) {

      }
    }
  }

  private static class Observation implements Serializable {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String code;
    private final Instant timestamp;
    private final String patient;
    private final Double value;


    @SuppressWarnings({"unused"})
    static ParDo.SingleOutput<String, Observation> fromJson() {
      return ParDo.of(new JsonToObservationFn());
    }

    @SuppressWarnings({"unused"})
    static MapElements<Observation, JsonPayload> toJson() {
      return MapElements.into(TypeDescriptor.of(JsonPayload.class)).via(Observation::toJsonPayload);
    }
    Observation(String json) {
      try {
        JsonNode root = MAPPER.readTree(json);
        code = checkStateNotNull(root.get("code").get("coding").get(0).get("code")).asText();
        timestamp = Instant.parse(checkStateNotNull(root.get("effectiveDateTime")).asText());
        patient =
            checkStateNotNull(root.get("subject").get("reference"))
                .asText()
                .replaceAll("urn:uuid:", "");

        double value = 0.0;
        if (root.has("valueQuantity") && root.get("valueQuantity").has("value")) {
          value = checkStateNotNull(root.get("valueQuantity").get("value")).asDouble();
        }
        this.value = value;
      } catch (JsonProcessingException | IllegalStateException e) {
        throw new IllegalStateException(e);
      }
    }

    JsonPayload toJsonPayload() {
      return JsonPayload.of(Struct.newBuilder()
          .putFields("code", Value.newBuilder()
              .setStringValue(code)
              .build())
          .putFields("timestamp", Value.newBuilder()
              .setStringValue(timestamp.toString())
              .build())
          .putFields("patient", Value.newBuilder()
              .setStringValue(patient)
              .build())
          .putFields("value", Value.newBuilder()
              .setNumberValue(value)
              .build())
          .build());
    }

    @Override
    public boolean equals(@Nullable Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Observation that = (Observation) o;
      return Objects.equal(code, that.code)
          && Objects.equal(timestamp, that.timestamp)
          && Objects.equal(patient, that.patient)
          && Objects.equal(value, that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(code, timestamp, patient, value);
    }
  }

  private static class JsonToObservationFn extends DoFn<String, Observation> {

    @ProcessElement
    public void process(@Element String json, OutputReceiver<Observation> receiver) {
      try {
        receiver.output(new Observation(json));
      } catch (IllegalStateException ignored) {

      }
    }
  }

}
