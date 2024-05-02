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
package org.apache.beam.sdk.io.stackexchange.transforms;

import static org.apache.beam.sdk.io.stackexchange.model.StackExchangeUnansweredQuestionsRequest.FILTER_TOTAL;
import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.apache.beam.io.requestresponse.Caller;
import org.apache.beam.io.requestresponse.RequestResponseIO;
import org.apache.beam.io.requestresponse.Result;
import org.apache.beam.io.requestresponse.UserCodeExecutionException;
import org.apache.beam.io.requestresponse.UserCodeQuotaException;
import org.apache.beam.io.requestresponse.UserCodeRemoteSystemException;
import org.apache.beam.io.requestresponse.UserCodeTimeoutException;
import org.apache.beam.sdk.coders.KvCoder;
import org.apache.beam.sdk.io.stackexchange.encoding.EncoderDecoder;
import org.apache.beam.sdk.io.stackexchange.encoding.EncoderDecoders;
import org.apache.beam.sdk.io.stackexchange.model.StackExchangeUnansweredQuestionsRequest;
import org.apache.beam.sdk.io.stackexchange.model.StackExchangeUnansweredQuestionsRequestCoder;
import org.apache.beam.sdk.io.stackexchange.model.StackExchangeUnansweredQuestionsResponse;
import org.apache.beam.sdk.io.stackexchange.model.StackExchangeUnansweredQuestionsResponseCoder;
import org.apache.beam.sdk.io.stackexchange.model.Total;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.io.ByteStreams;
import org.joda.time.Instant;

public class StackExchangeQuestionsUnanswered
    implements Caller<
        StackExchangeUnansweredQuestionsRequest,
        KV<StackExchangeUnansweredQuestionsRequest, StackExchangeUnansweredQuestionsResponse>> {

  private static final KvCoder<
          StackExchangeUnansweredQuestionsRequest, StackExchangeUnansweredQuestionsResponse>
      KV_CODER =
          KvCoder.of(
              new StackExchangeUnansweredQuestionsRequestCoder(),
              new StackExchangeUnansweredQuestionsResponseCoder());

  private final String apiKey;

  public StackExchangeQuestionsUnanswered(String apiKey) {
    this.apiKey = apiKey;
  }

  public PTransform<
          PCollection<StackExchangeUnansweredQuestionsRequest>,
          Result<
              KV<
                  StackExchangeUnansweredQuestionsRequest,
                  StackExchangeUnansweredQuestionsResponse>>>
      pTransform() {
    return RequestResponseIO.of(new StackExchangeQuestionsUnanswered(apiKey), KV_CODER);
  }

  private static final int STATUS_TOO_MANY_REQUESTS = 429;
  private static final int STATUS_TIMEOUT = 408;

  private static final int CLIENT_ERROR = 400;

  private static final int SYSTEM_ERROR = 500;

  private static final String ENDPOINT = "https://api.stackexchange.com/2.3/questions/unanswered";

  private static final String KEY = "key";

  private static final EncoderDecoder<StackExchangeUnansweredQuestionsRequest, String>
      PARAM_ENCODER = EncoderDecoders.urlParamOf(StackExchangeUnansweredQuestionsRequest.class);

  private static final EncoderDecoder<StackExchangeUnansweredQuestionsResponse, String>
      JSON_DECODER_FULL_RESPONSE =
          EncoderDecoders.jsonOf(StackExchangeUnansweredQuestionsResponse.class);

  private static final EncoderDecoder<org.apache.beam.sdk.io.stackexchange.model.Total, String>
      JSON_DECODER_TOTAL_RESPONSE =
          EncoderDecoders.jsonOf(org.apache.beam.sdk.io.stackexchange.model.Total.class);

  private static final HttpRequestFactory REQUEST_FACTORY =
      new NetHttpTransport().createRequestFactory();

  @Override
  public KV<StackExchangeUnansweredQuestionsRequest, StackExchangeUnansweredQuestionsResponse> call(
      StackExchangeUnansweredQuestionsRequest request) throws UserCodeExecutionException {

    GenericUrl url =
        new GenericUrl(String.format("%s?%s", ENDPOINT, PARAM_ENCODER.encode(request)));

    try {
      HttpResponse httpResponse = get(url, apiKey);

      validateResponse(httpResponse);

      InputStream is = httpResponse.getContent();
      byte[] bytes = ByteStreams.toByteArray(is);
      String json = new String(bytes, StandardCharsets.UTF_8);

      StackExchangeUnansweredQuestionsResponse response = JSON_DECODER_FULL_RESPONSE.decode(json);
      return KV.of(request, response);

    } catch (IOException e) {
      throw new UserCodeExecutionException(e);
    }
  }

  public static class Total
      implements Caller<
          StackExchangeUnansweredQuestionsRequest,
          KV<Instant, org.apache.beam.sdk.io.stackexchange.model.Total>> {

    private final String apiKey;

    public Total(String apiKey) {
      this.apiKey = apiKey;
    }

    @Override
    public KV<Instant, org.apache.beam.sdk.io.stackexchange.model.Total> call(
        StackExchangeUnansweredQuestionsRequest request) throws UserCodeExecutionException {
      String filter = Optional.ofNullable(request.getFilter()).orElse("");
      if (filter.isEmpty()) {
        throw new UserCodeExecutionException(
            String.format(
                "%s requires setting %s's filter with %s",
                Total.class, StackExchangeUnansweredQuestionsRequest.class, FILTER_TOTAL));
      }

      GenericUrl url =
          new GenericUrl(String.format("%s?%s", ENDPOINT, PARAM_ENCODER.encode(request)));

      try {

        HttpResponse httpResponse = get(url, apiKey);

        validateResponse(httpResponse);

        InputStream is = httpResponse.getContent();
        byte[] bytes = ByteStreams.toByteArray(is);
        String json = new String(bytes, StandardCharsets.UTF_8);

        org.apache.beam.sdk.io.stackexchange.model.Total total =
            JSON_DECODER_TOTAL_RESPONSE.decode(json);

        Instant timestamp = checkStateNotNull(request.getToDate());

        return KV.of(timestamp, total);

      } catch (IOException e) {
        throw new UserCodeExecutionException(e);
      }
    }
  }

  private static HttpResponse get(GenericUrl url, String apiKey) throws IOException {
    url = url.set(KEY, apiKey);
    HttpRequest httpRequest = REQUEST_FACTORY.buildGetRequest(url);
    return httpRequest.execute();
  }

  private static void validateResponse(HttpResponse httpResponse)
      throws UserCodeExecutionException {
    if (httpResponse.getStatusCode() >= SYSTEM_ERROR) {
      throw new UserCodeRemoteSystemException(httpResponse.getStatusMessage());
    }

    if (httpResponse.getStatusCode() >= CLIENT_ERROR) {
      switch (httpResponse.getStatusCode()) {
        case STATUS_TOO_MANY_REQUESTS:
          throw new UserCodeQuotaException(httpResponse.getStatusMessage());
        case STATUS_TIMEOUT:
          throw new UserCodeTimeoutException(httpResponse.getStatusMessage());
        default:
          throw new UserCodeExecutionException(httpResponse.getStatusMessage());
      }
    }
  }
}
