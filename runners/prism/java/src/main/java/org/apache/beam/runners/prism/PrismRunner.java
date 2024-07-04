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
package org.apache.beam.runners.prism;

import org.apache.beam.runners.portability.PortableRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.PipelineRunner;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.util.construction.Environments;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class PrismRunner extends PipelineRunner<PipelineResult> {

  private static final Logger LOG = LoggerFactory.getLogger(PrismRunner.class);

  private static final String DEFAULT_PRISM_ENDPOINT = "localhost:8073";

  public static PrismRunner fromOptions(PipelineOptions options) {
    PrismPipelineOptions prismPipelineOptions = options.as(PrismPipelineOptions.class);
    assignDefaultsIfNeeded(prismPipelineOptions);
    PortableRunner internal = PortableRunner.fromOptions(options);
    return new PrismRunner(internal, prismPipelineOptions);
  }

  private static void assignDefaultsIfNeeded(PrismPipelineOptions prismPipelineOptions) {
    if (Strings.isNullOrEmpty(prismPipelineOptions.getDefaultEnvironmentType())) {
      prismPipelineOptions.setDefaultEnvironmentType(Environments.ENVIRONMENT_LOOPBACK);
    }
    if (Strings.isNullOrEmpty(prismPipelineOptions.getJobEndpoint())) {
      prismPipelineOptions.setJobEndpoint(DEFAULT_PRISM_ENDPOINT);
    }
  }

  private final PortableRunner internal;
  private final PrismPipelineOptions prismPipelineOptions;

  private PrismRunner(PortableRunner internal, PrismPipelineOptions prismPipelineOptions) {
    this.internal = internal;
    this.prismPipelineOptions = prismPipelineOptions;
  }

  @Override
  public PipelineResult run(Pipeline pipeline) {
    LOG.info(
        "running Pipeline using {}: defaultEnvironmentType: {}, jobEndpoint: {}",
        PortableRunner.class.getName(),
        prismPipelineOptions.getDefaultEnvironmentType(),
        prismPipelineOptions.getJobEndpoint());
    return internal.run(pipeline);
  }

}
