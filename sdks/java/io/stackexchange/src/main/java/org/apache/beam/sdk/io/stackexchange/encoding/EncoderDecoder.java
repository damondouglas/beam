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

import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.io.payloads.PayloadSerializer;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public abstract class EncoderDecoder<DecodedT, EncodedT> {

  protected static final AutoValueSchema SCHEMA_PROVIDER = new AutoValueSchema();

  protected final Schema schema;
  protected final SerializableFunction<DecodedT, Row> toRowFn;
  protected final SerializableFunction<Row, DecodedT> fromRowFn;

  private @MonotonicNonNull PayloadSerializer payloadSerializer;

  protected EncoderDecoder(Class<DecodedT> type) {
    TypeDescriptor<DecodedT> typeDescriptor = TypeDescriptor.of(type);
    schema = checkStateNotNull(SCHEMA_PROVIDER.schemaFor(typeDescriptor));
    toRowFn = checkStateNotNull(SCHEMA_PROVIDER.toRowFunction(typeDescriptor));
    fromRowFn = checkStateNotNull(SCHEMA_PROVIDER.fromRowFunction(typeDescriptor));
  }

  protected PayloadSerializer getOrCreatePayloadSerializer() {
    if (payloadSerializer == null) {
      payloadSerializer = createPayloadSerializer();
    }
    return checkStateNotNull(payloadSerializer);
  }

  protected abstract PayloadSerializer createPayloadSerializer();

  public abstract EncodedT encode(DecodedT input);

  public abstract DecodedT decode(EncodedT input);
}
