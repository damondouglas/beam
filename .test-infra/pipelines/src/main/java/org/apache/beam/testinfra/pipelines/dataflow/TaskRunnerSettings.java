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

import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.List;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class TaskRunnerSettings {

  static TaskRunnerSettings from(com.google.dataflow.v1beta3.TaskRunnerSettings source) {
    return builder()
        .setTaskUser(source.getTaskUser())
        .setTaskGroup(source.getTaskGroup())
        .setOauthScopes(new ArrayList<>(source.getOauthScopesList()))
        .setBaseUrl(source.getBaseUrl())
        .setDataflowApiVersion(source.getDataflowApiVersion())
        .setParallelWorkerSettings(WorkerSettings.from(source.getParallelWorkerSettings()))
        .setBaseTaskDir(source.getBaseTaskDir())
        .setContinueOnException(source.getContinueOnException())
        .setLogToSerialConsole(source.getLogToSerialconsole())
        .setAlsoLogToStderr(source.getAlsologtostderr())
        .setLogUploadLocation(source.getLogUploadLocation())
        .setLogDir(source.getLogDir())
        .setTempStoragePrefix(source.getTempStoragePrefix())
        .setHarnessCommand(source.getHarnessCommand())
        .setWorkflowFileName(source.getWorkflowFileName())
        .setCommandlinesFileName(source.getCommandlinesFileName())
        .setVmId(source.getVmId())
        .setLanguageHint(source.getLanguageHint())
        .setStreamingWorkerMainClass(source.getStreamingWorkerMainClass())
        .build();
  }

  static Builder builder() {
    return new AutoValue_TaskRunnerSettings.Builder();
  }

  public abstract String getTaskUser();

  public abstract String getTaskGroup();

  public abstract List<String> getOauthScopes();

  public abstract String getBaseUrl();

  public abstract String getDataflowApiVersion();

  public abstract WorkerSettings getParallelWorkerSettings();

  public abstract String getBaseTaskDir();

  public abstract Boolean getContinueOnException();

  public abstract Boolean getLogToSerialConsole();

  public abstract Boolean getAlsoLogToStderr();

  public abstract String getLogUploadLocation();

  public abstract String getLogDir();

  public abstract String getTempStoragePrefix();

  public abstract String getHarnessCommand();

  public abstract String getWorkflowFileName();

  public abstract String getCommandlinesFileName();

  public abstract String getVmId();

  public abstract String getLanguageHint();

  public abstract String getStreamingWorkerMainClass();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setTaskUser(String taskUser);

    public abstract Builder setTaskGroup(String taskGroup);

    public abstract Builder setOauthScopes(List<String> oauthScopes);

    public abstract Builder setBaseUrl(String baseUrl);

    public abstract Builder setDataflowApiVersion(String dataflowApiVersion);

    public abstract Builder setParallelWorkerSettings(WorkerSettings parallelWorkerSettings);

    public abstract Builder setBaseTaskDir(String baseTaskDir);

    public abstract Builder setContinueOnException(Boolean continueOnException);

    public abstract Builder setLogToSerialConsole(Boolean logToSerialConsole);

    public abstract Builder setAlsoLogToStderr(Boolean alsoLogToStderr);

    public abstract Builder setLogUploadLocation(String logUploadLocation);

    public abstract Builder setLogDir(String logDir);

    public abstract Builder setTempStoragePrefix(String tempStoragePrefix);

    public abstract Builder setHarnessCommand(String harnessCommand);

    public abstract Builder setWorkflowFileName(String workflowFileName);

    public abstract Builder setCommandlinesFileName(String commandlinesFileName);

    public abstract Builder setVmId(String vmId);

    public abstract Builder setLanguageHint(String languageHint);

    public abstract Builder setStreamingWorkerMainClass(String streamingWorkerMainClass);

    public abstract TaskRunnerSettings build();
  }
}
