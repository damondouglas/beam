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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.apache.beam.runners.portability.PortableRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.PipelineRunner;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PortablePipelineOptions;
import org.apache.beam.sdk.util.construction.Environments;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link PipelineRunner} executed on Prism. Downloads, prepares, and executes the Prism service
 * on behalf of the developer when {@link PipelineRunner#run}ning the pipeline. If users want to
 * submit to an already running Prism service, use the {@link PortableRunner} with the {@link
 * PortablePipelineOptions#getJobEndpoint()} option instead. Prism is a {@link
 * org.apache.beam.runners.portability.PortableRunner} maintained at * <a
 * href="https://github.com/apache/beam/tree/master/sdks/go/cmd/prism">sdks/go/cmd/prism</a>.
 */
public class PrismRunner extends PipelineRunner<PipelineResult> {

  private static final Logger LOG = LoggerFactory.getLogger(PrismRunner.class);

  private static final String DEFAULT_PRISM_ENDPOINT = "localhost:8073";

  public static PrismRunner fromOptions(PipelineOptions options) {
    PrismPipelineOptions prismPipelineOptions = options.as(PrismPipelineOptions.class);

    PrismLocator locator = new PrismLocator(prismPipelineOptions);
    PrismExecutor.Builder executorBuilder =
        PrismExecutor.builder().setArguments(argumentsFrom(prismPipelineOptions));

    try {
      String command = locator.resolve();
      executorBuilder.setCommand(command);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assignDefaultsIfNeeded(prismPipelineOptions);
    PortableRunner internal = PortableRunner.fromOptions(options);
    return new PrismRunner(internal, prismPipelineOptions, executorBuilder.build());
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
  private final PrismExecutor prismExecutor;

  private PrismRunner(
      PortableRunner internal,
      PrismPipelineOptions prismPipelineOptions,
      PrismExecutor prismExecutor) {
    this.internal = internal;
    this.prismPipelineOptions = prismPipelineOptions;
    this.prismExecutor = prismExecutor;
  }

  /**
   * Executes the Prism service prior to submitting the {@param pipeline} to the {@link
   * PrismPipelineOptions#getJobEndpoint()}.
   */
  @Override
  public PipelineResult run(Pipeline pipeline) {
    try {
      prismExecutor.execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    LOG.info(
        "running Pipeline using {}: defaultEnvironmentType: {}, jobEndpoint: {}",
        PortableRunner.class.getName(),
        prismPipelineOptions.getDefaultEnvironmentType(),
        prismPipelineOptions.getJobEndpoint());

    PipelineResult result = internal.run(pipeline);
    return new PrismPipelineResult(result, prismExecutor::stop);
  }

  private static List<String> argumentsFrom(PrismPipelineOptions options) {
    if (Strings.isNullOrEmpty(options.getJobEndpoint())) {
      return Collections.emptyList();
    }
    try {
      String endpoint = options.getJobEndpoint();
      if (!endpoint.startsWith("http")) {
        endpoint = "http://" + endpoint;
      }
      URL url = new URL(endpoint);
      String arg = "-job_port=" + url.getPort();
      return Collections.singletonList(arg);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
