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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.io.stackexchange.encoding.EncoderDecoder;
import org.apache.beam.sdk.io.stackexchange.encoding.EncoderDecoders;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.io.ByteStreams;

public class TotalCoder extends CustomCoder<Total> {
  private static final EncoderDecoder<Total, byte[]> ENCODER_DECODER =
      EncoderDecoders.avroOf(Total.class);

  @Override
  public void encode(Total value, OutputStream outStream) throws CoderException, IOException {
    byte[] bytes = ENCODER_DECODER.encode(value);
    outStream.write(bytes);
    outStream.flush();
  }

  @Override
  public Total decode(InputStream inStream) throws CoderException, IOException {
    byte[] bytes = ByteStreams.toByteArray(inStream);
    return ENCODER_DECODER.decode(bytes);
  }
}
