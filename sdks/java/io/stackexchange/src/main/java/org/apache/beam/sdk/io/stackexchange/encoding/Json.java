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
package org.apache.beam.sdk.io.stackexchange.encoding;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import java.nio.charset.StandardCharsets;
import org.apache.beam.sdk.schemas.io.payloads.JsonPayloadSerializerProvider;
import org.apache.beam.sdk.schemas.io.payloads.PayloadSerializer;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.collect.ImmutableMap;

final class Json<T> extends EncoderDecoder<T, String> {
  Json(Class<T> type) {
    super(type);
  }

  @Override
  protected PayloadSerializer createPayloadSerializer() {
    return new JsonPayloadSerializerProvider().getSerializer(schema, ImmutableMap.of());
  }

  @Override
  public String encode(T input) {
    Row row = checkStateNotNull(toRowFn.apply(input));
    byte[] bytes = getOrCreatePayloadSerializer().serialize(row);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  @Override
  public T decode(String input) {
    byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
    Row row = getOrCreatePayloadSerializer().deserialize(bytes);
    return checkStateNotNull(fromRowFn.apply(row));
  }
}
