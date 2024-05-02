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
import java.io.Serializable;
import java.util.List;
import org.apache.beam.sdk.schemas.AutoValueSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;
import org.checkerframework.checker.nullness.qual.Nullable;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class Question implements Serializable {

  public abstract @Nullable Integer getAcceptedAnswerId();

  public abstract Integer getAnswerCount();

  public abstract @Nullable Integer getBountyAmount();

  public abstract @Nullable Long getBountyClosesDate();

  public abstract @Nullable Long getClosedDate();

  public abstract @Nullable String getClosedReason();

  public abstract @Nullable List<Collective> getCollectives();

  public abstract @Nullable Long getCommunityOwnedDate();

  public abstract @Nullable String getContentLicense();

  public abstract Long getCreationDate();

  public abstract Boolean getIsAnswered();

  public abstract Long getLastActivityDate();

  public abstract @Nullable Long getLastEditDate();

  public abstract String getLink();

  public abstract @Nullable Long getLockedDate();

  public abstract @Nullable ShallowUser getOwner();

  public abstract @Nullable List<Collective> getPostedByCollectives();

  public abstract @Nullable Long getProtectedDate();

  public abstract Integer getQuestionId();

  public abstract Integer getScore();

  public abstract List<String> getTags();

  public abstract String getTitle();

  public abstract Integer getViewCount();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setAcceptedAnswerId(Integer acceptedAnswerId);

    public abstract Builder setAnswerCount(Integer answerCount);

    public abstract Builder setBountyAmount(Integer bountyAmount);

    public abstract Builder setBountyClosesDate(Long bountyClosesDate);

    public abstract Builder setClosedDate(Long closedDate);

    public abstract Builder setClosedReason(String closedReason);

    public abstract Builder setCollectives(List<Collective> collectives);

    public abstract Builder setCommunityOwnedDate(Long communityOwnedDate);

    public abstract Builder setContentLicense(String contentLicense);

    public abstract Builder setCreationDate(Long creationDate);

    public abstract Builder setIsAnswered(Boolean isAnswered);

    public abstract Builder setLastActivityDate(Long lastActivityDate);

    public abstract Builder setLastEditDate(Long lastEditDate);

    public abstract Builder setLink(String link);

    public abstract Builder setLockedDate(Long lockedDate);

    public abstract Builder setOwner(ShallowUser owner);

    public abstract Builder setPostedByCollectives(List<Collective> postedByCollectives);

    public abstract Builder setProtectedDate(Long protectedDate);

    public abstract Builder setQuestionId(Integer questionId);

    public abstract Builder setScore(Integer score);

    public abstract Builder setTags(List<String> tags);

    public abstract Builder setTitle(String title);

    public abstract Builder setViewCount(Integer viewCount);

    public abstract Question build();
  }
}
