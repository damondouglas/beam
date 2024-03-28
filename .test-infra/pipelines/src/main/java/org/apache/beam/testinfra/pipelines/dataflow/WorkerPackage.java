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
import com.google.dataflow.v1beta3.Package;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class WorkerPackage {

  static List<WorkerPackage> from(List<Package> source) {
    return source.stream()
        .map(pkg -> builder().setName(pkg.getName()).setLocation(pkg.getLocation()).build())
        .collect(Collectors.toList());
  }

  static WorkerPackage from(com.google.dataflow.v1beta3.Package source) {
    return builder().setName(source.getName()).setLocation(source.getLocation()).build();
  }

  static Builder builder() {
    return new AutoValue_WorkerPackage.Builder();
  }

  public abstract String getName();

  public abstract String getLocation();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String name);

    public abstract Builder setLocation(String location);

    public abstract WorkerPackage build();
  }
}
