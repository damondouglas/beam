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
package org.apache.beam.testinfra.pipelines.common;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;
import static org.apache.beam.sdk.values.TypeDescriptors.strings;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import java.nio.charset.StandardCharsets;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Throwables;

public final class GeneratedMessageV3Conversions {
  private static final TypeDescriptor<ByteString> BYTE_STRING_TYPE =
      TypeDescriptor.of(ByteString.class);

  public static <T extends GeneratedMessageV3>
      MapElements.MapWithFailures<T, ByteString, String> toByteStringsWithFailures() {
    return MapElements.into(BYTE_STRING_TYPE)
        .via(
            (T element) -> {
              T safeElement = checkStateNotNull(element, "cannot map null element to ByteString");
              return safeElement.toByteString();
            })
        .exceptionsInto(strings())
        .exceptionsVia(
            exceptionElement -> Throwables.getStackTraceAsString(exceptionElement.exception()));
  }

  public static <T extends GeneratedMessageV3>
      MapElements.MapWithFailures<ByteString, T, String> fromByteStringsWithFailures(
          TypeDescriptor<T> type, SerializableFunction<ByteString, T> conversionFn) {
    return MapElements.into(type)
        .via(
            (ByteString source) -> {
              ByteString safeSource =
                  checkStateNotNull(source, "cannot map null ByteString to element");
              return conversionFn.apply(safeSource);
            })
        .exceptionsInto(strings())
        .exceptionsVia(
            exceptionElement -> Throwables.getStackTraceAsString(exceptionElement.exception()));
  }

  public static MapElements.MapWithFailures<ByteString, String, String> fromByteStringsToStrings() {
    return MapElements.into(strings())
        .via(
            (ByteString source) -> {
              ByteString safeSource =
                  checkStateNotNull(source, "cannot map null ByteString to element");
              return safeSource.toString(StandardCharsets.UTF_8);
            })
        .exceptionsInto(strings())
        .exceptionsVia(
            exceptionElement -> Throwables.getStackTraceAsString(exceptionElement.exception()));
  }

  public static MapElements.MapWithFailures<String, ByteString, String> toByteStringsFromStrings() {
    return MapElements.into(BYTE_STRING_TYPE)
        .via(
            (String source) -> {
              String safeSource = checkStateNotNull(source);
              return ByteString.copyFromUtf8(safeSource);
            })
        .exceptionsInto(strings())
        .exceptionsVia(
            exceptionElement -> Throwables.getStackTraceAsString(exceptionElement.exception()));
  }
}
