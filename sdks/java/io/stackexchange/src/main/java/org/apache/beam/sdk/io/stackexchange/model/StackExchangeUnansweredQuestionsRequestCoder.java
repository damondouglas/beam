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
import java.io.OutputStream;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.coders.RowCoder;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;

public class StackExchangeUnansweredQuestionsRequestCoder
    extends CustomCoder<StackExchangeUnansweredQuestionsRequest> {

  private static final AutoValueSchema SCHEMA_PROVIDER = new AutoValueSchema();

  private static final TypeDescriptor<StackExchangeUnansweredQuestionsRequest> TYPE_DESCRIPTOR =
      TypeDescriptor.of(StackExchangeUnansweredQuestionsRequest.class);

  private static final Schema SCHEMA =
      checkStateNotNull(SCHEMA_PROVIDER.schemaFor(TYPE_DESCRIPTOR));

  private static final RowCoder ROW_CODER = RowCoder.of(SCHEMA);

  private static final SerializableFunction<StackExchangeUnansweredQuestionsRequest, Row>
      TO_ROW_FN = checkStateNotNull(SCHEMA_PROVIDER.toRowFunction(TYPE_DESCRIPTOR));

  private static final SerializableFunction<Row, StackExchangeUnansweredQuestionsRequest>
      FROM_ROW_FN = checkStateNotNull(SCHEMA_PROVIDER.fromRowFunction(TYPE_DESCRIPTOR));

  @Override
  public void encode(StackExchangeUnansweredQuestionsRequest value, OutputStream outStream)
      throws CoderException, IOException {
    Row row = checkStateNotNull(TO_ROW_FN.apply(value));
    ROW_CODER.encode(row, outStream);
  }

  @Override
  public StackExchangeUnansweredQuestionsRequest decode(InputStream inStream)
      throws CoderException, IOException {
    Row row = checkStateNotNull(ROW_CODER.decode(inStream));
    return checkStateNotNull(FROM_ROW_FN.apply(row));
  }
}
