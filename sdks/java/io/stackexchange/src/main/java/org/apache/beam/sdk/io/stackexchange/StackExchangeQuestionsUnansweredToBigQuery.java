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
package org.apache.beam.sdk.io.stackexchange;

import static com.google.common.base.Preconditions.checkState;
import static org.apache.beam.sdk.io.stackexchange.StackExchangeQuestionsUnansweredToBigQueryPipelineOptions.TAG_DELIMITER;
import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TimePartitioning;
import com.google.common.base.Strings;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.beam.io.requestresponse.Caller;
import org.apache.beam.io.requestresponse.RequestResponseIO;
import org.apache.beam.io.requestresponse.Result;
import org.apache.beam.io.requestresponse.SetupTeardown;
import org.apache.beam.io.requestresponse.UserCodeExecutionException;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.NullableCoder;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.stackexchange.model.Question;
import org.apache.beam.sdk.io.stackexchange.model.StackExchangeUnansweredQuestionsRequest;
import org.apache.beam.sdk.io.stackexchange.model.StackExchangeUnansweredQuestionsRequestCoder;
import org.apache.beam.sdk.io.stackexchange.model.StackExchangeUnansweredQuestionsResponse;
import org.apache.beam.sdk.io.stackexchange.model.Total;
import org.apache.beam.sdk.io.stackexchange.transforms.StackExchange;
import org.apache.beam.sdk.io.stackexchange.transforms.StackExchangeQuestionsUnanswered;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.PeriodicImpulse;
import org.apache.beam.sdk.transforms.Values;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisException;

public class StackExchangeQuestionsUnansweredToBigQuery {

  private static final String QUESTIONS = "questions";
  private static final Logger LOG =
      LoggerFactory.getLogger(StackExchangeQuestionsUnansweredToBigQuery.class);

  private static final String REQUEST_QUEUE =
      StackExchangeUnansweredQuestionsRequest.class.getName();

  private static final TypeDescriptor<StackExchangeUnansweredQuestionsRequest> REQUEST_TYPE =
      TypeDescriptor.of(StackExchangeUnansweredQuestionsRequest.class);

  private static final StackExchangeUnansweredQuestionsRequestCoder REQUEST_CODER =
      new StackExchangeUnansweredQuestionsRequestCoder();

  private static void enQueueRequests(
      String stepName, PCollection<StackExchangeUnansweredQuestionsRequest> requests, URI uri) {
    requests
        .apply(stepName + EnQueueRequests.class.getSimpleName(), EnQueueRequests.pTransform(uri))
        .getFailures()
        .apply(stepName + EnQueueRequests.class.getSimpleName() + "-failures", errorOf());
  }

  private static PCollection<StackExchangeUnansweredQuestionsRequest> deQueueRequests(
      PCollection<Instant> impulse, URI uri) {
    Result<@Nullable StackExchangeUnansweredQuestionsRequest> result =
        impulse.apply(DequeueRequests.class.getSimpleName(), DequeueRequests.pTransform(uri));
    result.getFailures().apply(DequeueRequests.class.getSimpleName() + "-failures", errorOf());
    return result.getResponses().apply("filterNonNull", Filter.by(Objects::nonNull));
  }

  private static PCollection<StackExchangeUnansweredQuestionsRequest> createRequestsFromImpulse(
      PCollection<Instant> impulses, Duration interval, int pageSize, List<String> tags) {
    return impulses.apply(
        "createRequestsFromImpulse",
        MapElements.into(REQUEST_TYPE)
            .via(
                instant -> {
                  Instant safeInstant = checkStateNotNull(instant);
                  int page = 1;
                  Instant start = safeInstant.minus(interval);
                  return StackExchangeUnansweredQuestionsRequest.builderWithDefaults()
                      .setPage(page)
                      .setPageSize(pageSize)
                      .setFromDate(start)
                      .setToDate(safeInstant)
                      .setTagged(tags)
                      .build();
                }));
  }

  private static PCollection<StackExchangeUnansweredQuestionsRequest> createRequestsFromTotal(
      Pipeline pipeline, int pageSize, Total total, Instant start, Instant to, List<String> tags) {
    List<StackExchangeUnansweredQuestionsRequest> result = new ArrayList<>();
    int n = total.getValue();
    int page = 0;
    while (n > 0) {
      page++;
      result.add(
          StackExchangeUnansweredQuestionsRequest.builderWithDefaults()
              .setPage(page)
              .setPageSize(pageSize)
              .setFromDate(start)
              .setToDate(to)
              .setTagged(tags)
              .build());
      n -= pageSize;
    }

    return pipeline.apply("createRequestsFromTotal", Create.of(result));
  }

  private static Total getTotal(
      StackExchangeQuestionsUnanswered.Total caller, Instant from, Instant to, List<String> tags)
      throws UserCodeExecutionException {
    return caller
        .call(
            StackExchangeUnansweredQuestionsRequest.builderWithDefaults()
                .setPage(1)
                .setFromDate(from)
                .setToDate(to)
                .setTagged(tags)
                .build()
                .withTotal())
        .getValue();
  }

  private static <T> ParDo.SingleOutput<T, T> errorOf() {
    return ParDo.of(new ErrorFn<>());
  }

  private static <T> ParDo.SingleOutput<T, T> infoOf() {
    return ParDo.of(new InfoFn<>());
  }

  private static class ErrorFn<T> extends DoFn<T, T> {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorFn.class);

    @ProcessElement
    public void process(@Element T element, OutputReceiver<T> receiver) {
      LOG.error("{}: {}", Instant.now(), element);
      receiver.output(element);
    }
  }

  private static class InfoFn<T> extends DoFn<T, T> {
    private static final Logger LOG = LoggerFactory.getLogger(InfoFn.class);

    @ProcessElement
    public void process(@Element T element, OutputReceiver<T> receiver) {
      LOG.info("{}: {}", Instant.now(), element);
      receiver.output(element);
    }
  }

  private static class EnQueueRequests extends RequestQueue
      implements Caller<
          StackExchangeUnansweredQuestionsRequest, StackExchangeUnansweredQuestionsRequest> {

    static PTransform<
            PCollection<StackExchangeUnansweredQuestionsRequest>,
            Result<StackExchangeUnansweredQuestionsRequest>>
        pTransform(URI uri) {
      return RequestResponseIO.ofCallerAndSetupTeardown(new EnQueueRequests(uri), REQUEST_CODER);
    }

    private EnQueueRequests(URI uri) {
      super(uri);
    }

    @Override
    public StackExchangeUnansweredQuestionsRequest call(
        StackExchangeUnansweredQuestionsRequest request) throws UserCodeExecutionException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try {
        REQUEST_CODER.encode(request, baos);
        byte[] bytes = baos.toByteArray();
        rpush(REQUEST_QUEUE, bytes);
        StackExchangeMetrics.REQUEST_QUEUE.inc();
        return request;
      } catch (IOException e) {
        throw new UserCodeExecutionException(e);
      }
    }
  }

  private static class DequeueRequests extends RequestQueue
      implements Caller<Instant, @Nullable StackExchangeUnansweredQuestionsRequest> {

    static PTransform<
            PCollection<Instant>, Result<@Nullable StackExchangeUnansweredQuestionsRequest>>
        pTransform(URI uri) {
      return RequestResponseIO.ofCallerAndSetupTeardown(
          new DequeueRequests(uri), NullableCoder.of(REQUEST_CODER));
    }

    private DequeueRequests(URI uri) {
      super(uri);
    }

    @Override
    public @Nullable StackExchangeUnansweredQuestionsRequest call(Instant ignored)
        throws UserCodeExecutionException {
      if (isEmpty(REQUEST_QUEUE)) {
        return null;
      }
      byte[] bytes = lpop(REQUEST_QUEUE);
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      try {
        StackExchangeMetrics.REQUEST_QUEUE.dec();
        return REQUEST_CODER.decode(bais);
      } catch (IOException e) {
        throw new UserCodeExecutionException(e);
      }
    }
  }

  private static class RequestQueue implements SetupTeardown {

    private final URI uri;
    private transient @MonotonicNonNull JedisPooled jedis;

    private RequestQueue(URI uri) {
      this.uri = uri;
    }

    /**
     * Pushes items to the back ('right') of the list. Naming of this method preserves that of the
     * underlying {@link JedisPooled} client and performs a null check prior to execution.
     */
    void rpush(String key, byte[]... items) throws UserCodeExecutionException {
      try {
        getSafeClient().rpush(key.getBytes(StandardCharsets.UTF_8), items);
      } catch (JedisException e) {
        throw new UserCodeExecutionException(e);
      }
    }

    /**
     * Pops items from the front ('left') of the list. Naming of this method preserves that of the
     * underlying {@link JedisPooled} client and performs a null check prior to execution.
     */
    byte[] lpop(String key) throws UserCodeExecutionException {
      try {
        return getSafeClient().lpop(key.getBytes(StandardCharsets.UTF_8));
      } catch (JedisException e) {
        throw new UserCodeExecutionException(e);
      }
    }

    /**
     * Query the size of a list identified by the key. Returns 0 if key does not exist, per Redis
     * convention. Naming of this method preserves that of the underlying {@link JedisPooled} client
     * and performs a null check prior to execution.
     */
    long llen(String key) throws UserCodeExecutionException {
      try {
        return getSafeClient().llen(key);
      } catch (JedisException e) {
        throw new UserCodeExecutionException(e);
      }
    }

    /** Query whether the Redis list is empty. Calls {@link #llen} to determine this. */
    boolean isEmpty(String key) throws UserCodeExecutionException {
      return this.llen(key) == 0L;
    }

    @Override
    public void setup() throws UserCodeExecutionException {
      try {
        jedis = new JedisPooled(uri);
        jedis.ping();
      } catch (JedisException e) {
        String message =
            String.format("Failed to connect to host: %s, error: %s", uri, e.getMessage());
        throw new UserCodeExecutionException(message, e);
      }
    }

    private @NonNull JedisPooled getSafeClient() {
      return checkStateNotNull(jedis);
    }

    @Override
    public void teardown() throws UserCodeExecutionException {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  private static PCollection<StackExchangeUnansweredQuestionsRequest> mapRequestsFromResponses(
      PCollection<
              KV<StackExchangeUnansweredQuestionsRequest, StackExchangeUnansweredQuestionsResponse>>
          responseKV) {
    return responseKV.apply(
        MapRequestsFromResponsesFn.class.getSimpleName() + "ParDo",
        ParDo.of(new MapRequestsFromResponsesFn()));
  }

  private static class MapRequestsFromResponsesFn
      extends DoFn<
          KV<StackExchangeUnansweredQuestionsRequest, StackExchangeUnansweredQuestionsResponse>,
          StackExchangeUnansweredQuestionsRequest> {
    @ProcessElement
    public void process(
        @Element
            KV<StackExchangeUnansweredQuestionsRequest, StackExchangeUnansweredQuestionsResponse>
                element,
        OutputReceiver<StackExchangeUnansweredQuestionsRequest> receiver) {
      StackExchangeUnansweredQuestionsRequest request = element.getKey();
      StackExchangeUnansweredQuestionsResponse response = checkStateNotNull(element.getValue());
      StackExchangeMetrics.QUESTION_COUNT.inc(response.getItems().size());
      if (response.getHasMore()) {
        receiver.output(
            StackExchangeUnansweredQuestionsRequest.builderWithDefaults()
                .setPage(request.getPage() + 1)
                .setPageSize(request.getPageSize())
                .setFromDate(checkStateNotNull(request.getFromDate()))
                .setToDate(checkStateNotNull(request.getToDate()))
                .setSort(request.getSort())
                .setOrder(request.getOrder())
                .setTagged(request.getTagged())
                .setSite(request.getSite())
                .build());
      }
    }
  }

  private static PCollection<Question> mapQuestionsFromResponses(
      PCollection<StackExchangeUnansweredQuestionsResponse> responses) {
    return responses.apply(
        MapRequestsFromResponsesFn.class.getSimpleName(),
        ParDo.of(new MapQuestionsFromResponsesFn()));
  }

  private static class MapQuestionsFromResponsesFn
      extends DoFn<StackExchangeUnansweredQuestionsResponse, Question> {
    @ProcessElement
    public void process(
        @Element StackExchangeUnansweredQuestionsResponse element,
        OutputReceiver<Question> receiver) {
      for (Question question : element.getItems()) {
        receiver.output(question);
      }
    }
  }

  interface Options extends PipelineOptions {
    String TAG_DELIMITER = ";";

    @Description("The tags to filter unanswered questions by, delimited by semicolon, ';'")
    @Validation.Required
    String getTags();

    void setTags(String value);

    @Description("The page size per request")
    @Default.Integer(100)
    Integer getPageSize();

    void setPageSize(Integer value);

    @Description("The BigQuery dataset ID to which to store data")
    @Validation.Required
    String getDataset();

    void setDataset(String value);

    @Description("The URI of the queue")
    @Validation.Required
    String getQueueURI();

    void setQueueURI(String value);

    @Description(
        "The StackExchange API Key. Per documentation, this enables a higher quota request, is not considered a secret, and may be safely embedded. See https://api.stackexchange.com/docs/throttle for more details.")
    @Validation.Required
    String getApiKey();

    void setApiKey(String value);
  }

  interface BatchOptions extends Options {
    @Description("The start time in seconds epoch from which to query data.")
    @Default.Long(0L)
    Long getFromSecondsEpoch();

    void setFromSecondsEpoch(Long value);
  }

  public static class Batch {
    public static void main(String[] args) {
      StackExchangeQuestionsUnansweredToBigQueryPipelineOptions options =
          PipelineOptionsFactory.fromArgs(args)
              .withValidation()
              .as(StackExchangeQuestionsUnansweredToBigQueryPipelineOptions.class);

      String projectId = options.as(GcpOptions.class).getProject();
      checkState(!Strings.isNullOrEmpty(projectId));

      Pipeline pipeline = Pipeline.create(options);

      Instant now = Instant.now();
      Duration interval = Duration.parse(options.getInterval());
      Instant start = now.minus(interval);
      if (options.getFromSecondsEpoch() != null) {
        start = Instant.ofEpochSecond(options.getFromSecondsEpoch());
      }

      URI queueURI = URI.create(options.getQueueURI());

      List<String> tags =
          Arrays.stream(options.getTags().split(TAG_DELIMITER))
              .map(String::trim)
              .collect(Collectors.toList());

      if (start.isBefore(now.minus(interval))) {
        try {
          Instant end = now.minus(interval);
          StackExchangeQuestionsUnanswered.Total totalCaller =
              new StackExchangeQuestionsUnanswered.Total(options.getApiKey());
          Total totalToBackFill = getTotal(totalCaller, start, end, tags);
          LOG.info(
              "back filling from:{}, to:{}, total: {}", start, end, totalToBackFill.getValue());
          PCollection<StackExchangeUnansweredQuestionsRequest> requests =
              createRequestsFromTotal(
                  pipeline, options.getPageSize(), totalToBackFill, start, end, tags);
          enQueueRequests("initial", requests, queueURI);
          start = end;
        } catch (UserCodeExecutionException e) {
          throw new RuntimeException(e);
        }
      }

      PCollection<Instant> impulses =
          pipeline.apply(
              PeriodicImpulse.class.getSimpleName(),
              PeriodicImpulse.create().startAt(start).withInterval(interval));

      PCollection<StackExchangeUnansweredQuestionsRequest> requestsFromImpulse =
          createRequestsFromImpulse(impulses, interval, options.getPageSize(), tags);

      enQueueRequests("enqueueRequestsFromImpulse", requestsFromImpulse, queueURI);

      PCollection<StackExchangeUnansweredQuestionsRequest> requests =
          deQueueRequests(impulses, queueURI);

      requests.apply("logRequests", infoOf());

      Result<KV<StackExchangeUnansweredQuestionsRequest, StackExchangeUnansweredQuestionsResponse>>
          result =
              requests.apply(
                  StackExchangeQuestionsUnanswered.class.getSimpleName(),
                  StackExchange.questions().unanswered(options.getApiKey()).pTransform());

      result
          .getFailures()
          .apply(StackExchangeQuestionsUnanswered.class.getSimpleName() + "-failures", errorOf());

      PCollection<StackExchangeUnansweredQuestionsRequest> moreRequests =
          mapRequestsFromResponses(result.getResponses());
      enQueueRequests("moreRequests", moreRequests, queueURI);

      PCollection<StackExchangeUnansweredQuestionsResponse> responses =
          result.getResponses().apply("responses", Values.create());

      PCollection<Question> questions = mapQuestionsFromResponses(responses);

      TableReference tableReference =
          new TableReference()
              .setProjectId(projectId)
              .setDatasetId(options.getDataset())
              .setTableId(String.format("%s_%s", QUESTIONS, Instant.now().getMillis()));

      questions.apply(
          "writeQuestionsToBigQuery",
          BigQueryIO.<Question>write()
              .to(tableReference)
              .useBeamSchema()
              .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
              .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
              .withTimePartitioning(new TimePartitioning()));

      pipeline.run();
    }
  }

  interface StreamOptions extends Options {
    @Description("The Duration interval in ISO-8601 format to make Web calls.")
    @Default.String("PT5s")
    String getInterval();

    void setInterval(String value);
  }

  public static class Stream {
    public static void main(String[] args) {
      StreamOptions options =
          PipelineOptionsFactory.fromArgs(args).withValidation().as(StreamOptions.class);

      String projectId = options.as(GcpOptions.class).getProject();
      checkState(!Strings.isNullOrEmpty(projectId));

      Pipeline pipeline = Pipeline.create(options);

      Instant now = Instant.now();
      Duration interval = Duration.parse(options.getInterval());
      Instant start = now.minus(interval);

      URI queueURI = URI.create(options.getQueueURI());

      List<String> tags =
          Arrays.stream(options.getTags().split(TAG_DELIMITER))
              .map(String::trim)
              .collect(Collectors.toList());

      PCollection<Instant> impulses =
          pipeline.apply(
              PeriodicImpulse.class.getSimpleName(),
              PeriodicImpulse.create().startAt(start).withInterval(interval));

      PCollection<StackExchangeUnansweredQuestionsRequest> requestsFromImpulse =
          createRequestsFromImpulse(impulses, interval, options.getPageSize(), tags);

      enQueueRequests("enqueueRequestsFromImpulse", requestsFromImpulse, queueURI);

      PCollection<StackExchangeUnansweredQuestionsRequest> requests =
          deQueueRequests(impulses, queueURI);

      requests.apply("logRequests", infoOf());

      Result<KV<StackExchangeUnansweredQuestionsRequest, StackExchangeUnansweredQuestionsResponse>>
          result =
              requests.apply(
                  StackExchangeQuestionsUnanswered.class.getSimpleName(),
                  StackExchange.questions().unanswered(options.getApiKey()).pTransform());

      result
          .getFailures()
          .apply(StackExchangeQuestionsUnanswered.class.getSimpleName() + "-failures", errorOf());

      PCollection<StackExchangeUnansweredQuestionsRequest> moreRequests =
          mapRequestsFromResponses(result.getResponses());
      enQueueRequests("moreRequests", moreRequests, queueURI);

      PCollection<StackExchangeUnansweredQuestionsResponse> responses =
          result.getResponses().apply("responses", Values.create());

      PCollection<Question> questions = mapQuestionsFromResponses(responses);

      TableReference tableReference =
          new TableReference()
              .setProjectId(projectId)
              .setDatasetId(options.getDataset())
              .setTableId(String.format("%s_%s", QUESTIONS, Instant.now().getMillis()));

      questions.apply(
          "writeQuestionsToBigQuery",
          BigQueryIO.<Question>write()
              .to(tableReference)
              .useBeamSchema()
              .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
              .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
              .withTimePartitioning(new TimePartitioning()));

      pipeline.run();
    }
  }
}
