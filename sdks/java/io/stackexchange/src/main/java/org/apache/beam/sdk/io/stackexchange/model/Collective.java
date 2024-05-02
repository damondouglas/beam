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

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class Collective implements Serializable {

  public abstract String getDescription();

  public abstract List<CollectiveExternalLink> getExternalLinks();

  public abstract String getLink();

  public abstract String getName();

  public abstract String getSlug();

  public abstract List<String> getTags();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setDescription(String description);

    public abstract Builder setExternalLinks(List<CollectiveExternalLink> externalLinks);

    public abstract Builder setLink(String link);

    public abstract Builder setName(String name);

    public abstract Builder setSlug(String slug);

    public abstract Builder setTags(List<String> tags);

    public abstract Collective build();
  }
}
