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

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assume.assumeTrue;

@RunWith(JUnit4.class)
public class PrismRunnerTest {
  // See build.gradle for test task configuration.
  static final String PRISM_BUILD_TARGET_PROPERTY_NAME = "prism.buildTarget";


  private static final PipelineOptions OPTIONS = PipelineOptionsFactory.create();

  static {
    OPTIONS.setRunner(PrismRunner.class);
  }

  @Test
  public void givenUnboundedSource_runsUntilCancel() {}

  @Test
  public void givenBoundedSource_runsUntilSourceReadDone() {
    Pipeline pipeline = Pipeline.create(OPTIONS);
    pipeline.apply(Create.of("a", "b", "c"));
    pipeline.run();
  }

  @Test
  public void pipelineResultReportsJobState() {}

  @Test
  public void givenDefaultWaitUntilFinish_threadBlocks() {}

  @Test
  public void givenDefaultWaitUntilFinishWithTimestamp_threadBlockedUntilTimeout() {}

  @Test
  public void givenCancelInvoked_thenPipelineCancels() {}

  @Test
  public void givenDrainInvoked_thenPipelineDrains() {}

  /**
   * Drives ignoring of tests via checking {@link org.junit.Assume#assumeTrue} that the {@link
   * System#getProperty} for {@link #PRISM_BUILD_TARGET_PROPERTY_NAME} is not null or empty.
   */
  static String getLocalPrismBuildOrIgnoreTest() {
    String command = System.getProperty(PRISM_BUILD_TARGET_PROPERTY_NAME);
    assumeTrue(
            "System property: "
                    + PRISM_BUILD_TARGET_PROPERTY_NAME
                    + " is not set; see build.gradle for test task configuration",
            !Strings.isNullOrEmpty(command));
    return command;
  }

}
