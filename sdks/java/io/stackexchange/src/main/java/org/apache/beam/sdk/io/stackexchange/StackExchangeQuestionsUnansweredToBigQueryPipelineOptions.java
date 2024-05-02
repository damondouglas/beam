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
package org.apache.beam.sdk.io.stackexchange;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface StackExchangeQuestionsUnansweredToBigQueryPipelineOptions extends PipelineOptions {

  String TAG_DELIMITER = ";";

  @Description("The tags to filter unanswered questions by, delimited by semicolon, ';'")
  @Validation.Required
  String getTags();

  void setTags(String value);

  @Description("The Duration interval in ISO-8601 format to make Web calls.")
  @Default.String("PT2s")
  String getInterval();

  void setInterval(String value);

  @Description(
      "The start time in seconds epoch of the data range. Defaults to current time minus the interval")
  Long getFromSecondsEpoch();

  void setFromSecondsEpoch(Long value);

  @Description("The page size per request")
  @Default.Integer(100)
  Integer getPageSize();

  void setPageSize(Integer value);

  @Description("The BigQuery dataset ID to which to store data")
  @Validation.Required
  String getDataset();

  void setDataset(String value);

  @Description("The URI of the queue")
  @Validation.Required
  String getQueueURI();

  void setQueueURI(String value);

  @Description(
      "The StackExchange API Key. Per documentation, this enables a higher quota request, is not considered a secret, and may be safely embedded. See https://api.stackexchange.com/docs/throttle for more details.")
  @Validation.Required
  String getApiKey();

  void setApiKey(String value);
}
