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

import static org.apache.beam.sdk.util.Preconditions.checkArgumentNotNull;
import static org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Preconditions.checkState;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.io.payloads.PayloadSerializer;
import org.apache.beam.sdk.schemas.logicaltypes.EnumerationType;
import org.apache.beam.sdk.values.Row;
import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.Duration;
import org.joda.time.Instant;

class UrlParams<T> extends EncoderDecoder<T, String> {
  UrlParams(Class<T> type) {
    super(type);
  }

  @Override
  protected PayloadSerializer createPayloadSerializer() {
    return new UrlParamPayloadSerializer(schema);
  }

  @Override
  public String encode(T input) {
    Row row = toRowFn.apply(input);
    byte[] bytes = getOrCreatePayloadSerializer().serialize(row);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  @Override
  public T decode(String input) {
    throw new NotImplementedException("not implemented");
  }

  static class UrlParamPayloadSerializer implements PayloadSerializer {

    private final Schema schema;

    UrlParamPayloadSerializer(Schema schema) {
      this.schema = schema;
    }

    @Override
    public byte[] serialize(Row row) {
      StringJoiner joiner = new StringJoiner("&");
      for (int i = 0; i < schema.getFieldCount(); i++) {
        Schema.Field field = schema.getField(i);
        Object value = row.getValue(field.getName());
        if (value == null) {
          continue;
        }
        try {
          String encoded = encode(field, value);
          if (!encoded.isEmpty()) {
            joiner.add(encode(field, value));
          }
        } catch (UnsupportedEncodingException e) {
          throw new RuntimeException(e);
        }
      }

      return joiner.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static String encode(Schema.Field field, Object value)
        throws UnsupportedEncodingException {
      checkArgumentNotNull(field);
      checkArgumentNotNull(value);

      if (value instanceof List) {
        List<?> list = (List<?>) value;
        String encodedValue = encode(list);
        return encode(field.getName(), encodedValue);
      }

      if (value instanceof Instant) {
        Instant instant = (Instant) value;
        Duration sinceEpoch = Duration.millis(instant.getMillis());
        String encodedValue = String.valueOf(sinceEpoch.getStandardSeconds());
        return encode(field.getName(), encodedValue);
      }

      if (field.getType().isLogicalType(EnumerationType.IDENTIFIER)) {
        checkState(value instanceof EnumerationType.Value);
        EnumerationType.Value enumValue = (EnumerationType.Value) value;
        int enumIndex = enumValue.getValue();
        EnumerationType enumerationType = field.getType().getLogicalType(EnumerationType.class);
        String encodedValue =
            checkArgumentNotNull(enumerationType.getValues().get(enumIndex)).toLowerCase();
        return encode(field.getName(), encodedValue);
      }

      if (!field.getType().getTypeName().isPrimitiveType()) {
        throw new UnsupportedEncodingException(
            String.format(
                "%s only supports primitive types, got: %s",
                UrlParamPayloadSerializer.class, field.getType().getTypeName()));
      }

      return encode(field.getName(), value.toString());
    }

    private static String encode(String fieldName, String value)
        throws UnsupportedEncodingException {
      String encodedName = fieldName.toLowerCase().replaceAll("_", "");
      //      value = URLEncoder.encode(value, StandardCharsets.UTF_8.displayName());
      return String.format("%s=%s", encodedName, value);
    }

    private static String encode(List<?> value) {
      StringJoiner listJoiner = new StringJoiner(";");
      for (Object item : value) {
        listJoiner.add(checkArgumentNotNull(item).toString());
      }
      return listJoiner.toString();
    }

    @Override
    public Row deserialize(byte[] bytes) {
      throw new NotImplementedException("not implemented");
    }
  }
}
