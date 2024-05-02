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
import org.joda.time.Instant;

@DefaultSchema(AutoValueSchema.class)
@SchemaCaseFormat(CaseFormat.LOWER_UNDERSCORE)
@AutoValue
public abstract class StackExchangeUnansweredQuestionsRequest implements Serializable {

  public static final Order DEFAULT_ORDER = Order.DESC;
  public static final Sort DEFAULT_SORT = Sort.ACTIVITY;

  public static final String DEFAULT_SITE = "stackoverflow";

  /**
   * The default page size. See <a href="https://api.stackexchange.com/docs/paging">StackExchange
   * Paging documentation for more details.</a>.
   */
  public static final Integer DEFAULT_PAGE_SIZE = 100;

  public static final String FILTER_TOTAL = "total";

  public static Builder builderWithDefaults() {
    return builder()
        .setOrder(DEFAULT_ORDER)
        .setSort(DEFAULT_SORT)
        .setSite(DEFAULT_SITE)
        .setPageSize(DEFAULT_PAGE_SIZE);
  }

  public StackExchangeUnansweredQuestionsRequest withTotal() {
    return toBuilder().setFilter(FILTER_TOTAL).build();
  }

  public static Builder builder() {
    return new AutoValue_StackExchangeUnansweredQuestionsRequest.Builder();
  }

  public abstract Order getOrder();

  public abstract Sort getSort();

  public abstract Integer getPage();

  public abstract Integer getPageSize();

  public abstract Instant getFromDate();

  public abstract Instant getToDate();

  public abstract List<String> getTagged();

  public abstract String getSite();

  public abstract @Nullable String getFilter();

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setOrder(Order order);

    public abstract Builder setSort(Sort sort);

    public abstract Builder setPage(Integer page);

    public abstract Builder setPageSize(Integer pageSize);

    public abstract Builder setFromDate(Instant fromDate);

    public abstract Builder setToDate(Instant toDate);

    public abstract Builder setTagged(List<String> tags);

    public abstract Builder setSite(String site);

    public abstract Builder setFilter(String filter);

    public abstract StackExchangeUnansweredQuestionsRequest build();
  }
}
