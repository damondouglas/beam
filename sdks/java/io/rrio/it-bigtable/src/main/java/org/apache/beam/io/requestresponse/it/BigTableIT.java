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
package org.apache.beam.io.requestresponse.it;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import java.util.HashMap;
import java.util.Map;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.joda.time.Instant;

public class BigTableIT {

  static final String KEY_PREFIX = "keyprefix";
  static final String READ_OR_WRITE = "readorwrite";
  static final String CONNECTOR = "connector";

  public static void main(String[] args) {
    BigTableITOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(BigTableITOptions.class);

    long suffix = Instant.now().getMillis();

    checkStateNotNull(
        options.as(GcpOptions.class).getProject(), "--project is missing but required");

    String keyPrefix = BigTableITHelper.keyPrefixOf(options);

    Map<String, String> labels = new HashMap<>();
    labels.put(KEY_PREFIX, keyPrefix.replaceAll("#", "__"));
    labels.put(READ_OR_WRITE, options.getReadOrWrite().name().toLowerCase());
    labels.put(CONNECTOR, options.getConnector().name().toLowerCase());

    options.as(DataflowPipelineOptions.class).setLabels(labels);

    options
        .as(PipelineOptions.class)
        .setJobName(
            String.format(
                "rrio-bigtable-it-%s-%s-n-%s-size-%s-%d",
                options.getReadOrWrite().name().toLowerCase(),
                options.getConnector().name().toLowerCase(),
                options.getElementSizePerImpulse().name().toLowerCase(),
                options.getMutationSize().name().toLowerCase(),
                suffix));

    BigTableITPipeline bigTableITPipeline =
        BigTableITPipeline.builder()
            .setOptions(options)
            .setKeyPrefix(keyPrefix)
            .setWriter(BigTableITHelper.writerOf(options))
            .setReader(BigTableITHelper.readerOf(options, keyPrefix))
            .build();

    bigTableITPipeline.createPipeline().run();
  }
}
