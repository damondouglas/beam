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
package org.apache.beam.testinfra.pipelines.dataflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Value;
import java.util.HashMap;
import java.util.Map;

final class WellKnown {
  static Map<String, String> from(com.google.protobuf.Struct source) {
    Map<String, String> result = new HashMap<>();
    for (Map.Entry<String, Value> entry : source.getFieldsMap().entrySet()) {
      result.put(entry.getKey(), entry.getValue().toString());
    }
    return result;
  }

  static Map<String, String> from(com.google.protobuf.Any source) {
    Map<String, String> result = new HashMap<>();
    for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : source.getAllFields().entrySet()) {
      result.put(entry.getKey().getName(), entry.getValue().toString());
    }
    return result;
  }

  static String from(com.google.protobuf.Value source) {
    if (source.hasStringValue()) {
      return source.getStringValue();
    }
    if (source.hasBoolValue()) {
      return String.valueOf(source.getBoolValue());
    }
    if (source.hasListValue()) {
      return source.getListValue().getValuesList().toString();
    }
    if (source.hasNullValue()) {
      return "";
    }
    if (source.hasNumberValue()) {
      return String.valueOf(source.getNumberValue());
    }
    if (source.hasStructValue()) {
      Map<String, String> result = from(source.getStructValue());
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.valueToTree(result);
      try {
        return mapper.writeValueAsString(node);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
    return source.toString();
  }
}
