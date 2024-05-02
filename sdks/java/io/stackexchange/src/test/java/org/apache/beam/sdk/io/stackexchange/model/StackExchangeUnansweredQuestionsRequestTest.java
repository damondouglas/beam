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
package org.apache.beam.sdk.io.stackexchange.model;

import static com.google.common.truth.Truth.assertThat;

import org.apache.beam.sdk.io.stackexchange.encoding.EncoderDecoder;
import org.apache.beam.sdk.io.stackexchange.encoding.EncoderDecoders;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.collect.ImmutableList;
import org.junit.Test;

public class StackExchangeUnansweredQuestionsRequestTest {
  EncoderDecoder<StackExchangeUnansweredQuestionsRequest, String> URL_PARAM_ENCODER =
      EncoderDecoders.urlParamOf(StackExchangeUnansweredQuestionsRequest.class);

  @Test
  public void testEncodeURLParams() {
    String got =
        URL_PARAM_ENCODER.encode(
            StackExchangeUnansweredQuestionsRequest.builder()
                .setOrder(Order.DESC)
                .setSort(Sort.ACTIVITY)
                .setPage(1)
                .setPageSize(10)
                .setSite("stackoverflow")
                .setTagged(ImmutableList.of("apache-beam", "google-cloud-dataflow"))
                .build());

    assertThat(got)
        .isEqualTo(
            "sort=activity&order=desc&page=1&pagesize=10&tagged=apache-beam;google-cloud-dataflow&site=stackoverflow");
  }
}
