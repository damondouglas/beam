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

import static org.apache.beam.io.requestresponse.it.BigTableIT.CONNECTOR;
import static org.apache.beam.io.requestresponse.it.BigTableIT.KEY_PREFIX;
import static org.apache.beam.io.requestresponse.it.BigTableIT.READ_OR_WRITE;
import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;
import static org.apache.beam.sdk.values.TypeDescriptors.strings;

import com.google.cloud.run.v2.EnvVar;
import com.google.cloud.run.v2.JobName;
import com.google.cloud.run.v2.RunJobRequest;
import com.google.events.cloud.dataflow.v1beta3.Job;
import com.google.events.cloud.dataflow.v1beta3.JobState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.Partition;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.TypeDescriptor;

class EventarcJobs {
  private static final String ENVIRONMENT_KEY_MODIFIER_KEY = "BIG_TABLE_KEY_MODIFIER";

  private static final SerializableFunction<Job, JobState> CURRENT_STATE_FN =
      job -> checkStateNotNull(job).getCurrentState();

  private static final SerializableFunction<Job, Boolean> IS_DONE_FN =
      job -> CURRENT_STATE_FN.apply(job).equals(JobState.JOB_STATE_DONE);
  private static final SerializableFunction<Job, Boolean> HAS_LABELS_FN =
      job ->
          checkStateNotNull(job).containsLabels(CONNECTOR)
              && job.containsLabels(READ_OR_WRITE)
              && job.containsLabels(KEY_PREFIX);

  private static final SerializableFunction<Job, String> CONNECTOR_FN =
      job -> checkStateNotNull(job).getLabelsOrThrow(CONNECTOR);

  static SerializableFunction<Job, String> READ_OR_WRITE_FN =
      job -> checkStateNotNull(job).getLabelsOrThrow(READ_OR_WRITE);

  private static final SerializableFunction<Job, String> KEY_PREFIX_FN =
      job -> checkStateNotNull(job).getLabelsOrThrow(KEY_PREFIX);

  private static final SerializableFunction<Job, String> ID_FN =
      job -> checkStateNotNull(job).getId();

  private static final SerializableFunction<Job, String> NAME_FN =
      job -> checkStateNotNull(job).getName();

  private static final SerializableFunction<Job, String> SUMMARY_LABELS_FN =
      job -> {
        Job safeJob = checkStateNotNull(job);
        List<String> labels = new ArrayList<>();
        for (Map.Entry<String, String> entry : safeJob.getLabelsMap().entrySet()) {
          labels.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        return String.join("; ", labels);
      };

  private static final SerializableFunction<Job, Boolean> IS_WRITE_FN =
      job ->
          READ_OR_WRITE_FN
              .apply(job)
              .equals(BigTableITHelper.ReadOrWrite.WRITE.name().toLowerCase());

  private static final SerializableFunction<Job, String> SUMMARY_FN =
      job ->
          String.format(
              "job{name:%s, id:%s, state:%s labels:%s}",
              NAME_FN.apply(job),
              ID_FN.apply(job),
              CURRENT_STATE_FN.apply(job),
              SUMMARY_LABELS_FN.apply(job));

  static MapElements<Job, String> SUMMARIZE_FN = MapElements.into(strings()).via(SUMMARY_FN);

  private static final SerializableFunction<Job, Boolean> INCLUDE_FN =
      job -> HAS_LABELS_FN.apply(job) && IS_WRITE_FN.apply(job) && IS_DONE_FN.apply(job);

  static int PARTITION_INCLUDED_INDEX = 0;
  static int PARTITION_EXCLUDED_INDEX = 1;

  static Partition<Job> PARTITION_JOBS =
      Partition.of(
          2,
          (Partition.PartitionFn<Job>)
              (elem, numPartitions) ->
                  INCLUDE_FN.apply(elem) ? PARTITION_INCLUDED_INDEX : PARTITION_EXCLUDED_INDEX);

  private static final SerializableFunction<Job, RunJobRequest> TO_RUN_JOB_REQUEST_FN =
      job -> {
        Job safeJob = checkStateNotNull(job);
        String name =
            String.format(
                "rrio-bigtable-it-%s-%s",
                CONNECTOR_FN.apply(safeJob),
                BigTableITHelper.ReadOrWrite.READ.name().toLowerCase());
        JobName jobName = JobName.of(safeJob.getProjectId(), safeJob.getLocation(), name);
        RunJobRequest.Overrides.ContainerOverride containerOverride =
            RunJobRequest.Overrides.ContainerOverride.newBuilder()
                .addEnv(
                    EnvVar.newBuilder()
                        .setName(ENVIRONMENT_KEY_MODIFIER_KEY)
                        .setValue(KEY_PREFIX_FN.apply(safeJob))
                        .build())
                .build();
        RunJobRequest.Overrides overrides =
            RunJobRequest.Overrides.newBuilder().addContainerOverrides(containerOverride).build();
        return RunJobRequest.newBuilder()
            .setName(jobName.toString())
            .setOverrides(overrides)
            .build();
      };

  static final MapElements<Job, RunJobRequest> MAP_TO_RUN_JOB_REQUESTS =
      MapElements.into(TypeDescriptor.of(RunJobRequest.class)).via(TO_RUN_JOB_REQUEST_FN);
}
