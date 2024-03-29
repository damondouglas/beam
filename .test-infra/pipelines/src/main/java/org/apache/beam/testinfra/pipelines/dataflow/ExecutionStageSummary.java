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
import com.google.dataflow.v1beta3.KindType;
import java.util.List;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class ExecutionStageSummary {

  public abstract String getName();

  public abstract String getId();

  public abstract KindType getKind();

  public abstract List<StageSource> getInputSource();

  public abstract List<StageSource> getOutputSource();

  public abstract String getPrerequisiteStage();

  public abstract List<ComponentTransform> getComponentTransform();

  public abstract List<ComponentSource> getComponentSource();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setName(String name);

    public abstract Builder setId(String id);

    public abstract Builder setKind(KindType kind);

    public abstract Builder setInputSource(List<StageSource> inputSource);

    public abstract Builder setOutputSource(List<StageSource> outputSource);

    public abstract Builder setPrerequisiteStage(String prerequisiteStage);

    public abstract Builder setComponentTransform(List<ComponentTransform> componentTransform);

    public abstract Builder setComponentSource(List<ComponentSource> componentSource);

    public abstract ExecutionStageSummary build();
  }
}
