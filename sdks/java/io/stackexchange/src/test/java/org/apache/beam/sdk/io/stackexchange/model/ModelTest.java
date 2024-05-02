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

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.beam.sdk.io.stackexchange.encoding.EncoderDecoder;
import org.apache.beam.sdk.io.stackexchange.encoding.EncoderDecoders;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.io.ByteStreams;
import org.junit.Test;

abstract class ModelTest<T> {
  abstract Class<T> getType();

  abstract String getResourceName();

  abstract void assertThatEquals(T got);

  protected EncoderDecoder<T, String> getJsonEncoderDecoder() {
    return EncoderDecoders.jsonOf(getType());
  }

  protected String getJson() throws IOException {
    try (InputStream is = checkStateNotNull(getType().getResourceAsStream(getResourceName()))) {
      byte[] bytes = ByteStreams.toByteArray(is);
      return new String(bytes, StandardCharsets.UTF_8);
    }
  }

  @Test
  public void decodeFromJson() throws IOException {
    String json = getJson();
    EncoderDecoder<T, String> encoderDecoder = getJsonEncoderDecoder();
    T decoded = encoderDecoder.decode(json);
    assertThatEquals(decoded);
  }
}
