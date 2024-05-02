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
package org.apache.beam.sdk.io.stackexchange.model;

import com.google.auto.value.AutoValue;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;
import org.checkerframework.checker.nullness.qual.Nullable;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class ShallowUser {

  public abstract @Nullable Integer getAcceptRate();

  public abstract @Nullable String getDisplayName();

  public abstract @Nullable String getLink();

  public abstract @Nullable String getProfileImage();

  public abstract @Nullable Integer getReputation();

  public abstract @Nullable Integer getUserId();

  public abstract @Nullable String getUserType();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setAcceptRate(Integer acceptRate);

    public abstract Builder setDisplayName(String displayName);

    public abstract Builder setLink(String link);

    public abstract Builder setProfileImage(String profileImage);

    public abstract Builder setReputation(Integer reputation);

    public abstract Builder setUserId(Integer userId);

    public abstract Builder setUserType(String userType);

    public abstract ShallowUser build();
  }
}
