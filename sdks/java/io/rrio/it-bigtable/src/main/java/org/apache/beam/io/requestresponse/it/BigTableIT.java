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

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.PeriodicImpulse;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.UUID;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

public class BigTableIT {

  public static void main(String[] args) {
    BigTableITOptions options = PipelineOptionsFactory.fromArgs(args)
            .as(BigTableITOptions.class);

    long suffix = Instant.now().getMillis();

    options.as(PipelineOptions.class)
            .setJobName(String.format("rrio-bigtable-it-%s-n-%s-size-%s-%d",
                    options.getConnector().name().toLowerCase(),
                    options.getElementSizePerImpulse().name().toLowerCase(),
                    options.getMutationSize().name().toLowerCase(),
                    suffix));

    checkStateNotNull(options.as(GcpOptions.class).getProject(), "--project is missing but required");

    String keyPrefix = String.format("%s#%s", options.getConnector().name().toLowerCase(), UUID.randomUUID());

    BigTableITPipeline bigTableITPipeline = BigTableITPipeline.builder()
            .setOptions(options)
            .setKeyPrefix(keyPrefix)
            .setWriter(BigTableITHelper.writerOf(options))
            .setReader(BigTableITHelper.readerOf(options, keyPrefix))
            .build();

    bigTableITPipeline.createWritePipeline().run();
//    bigTableITPipeline.createReadPipeline().run();
  }
}
