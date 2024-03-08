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

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface BigTableITOptions extends PipelineOptions {
  @Description("Test duration in seconds")
  @Default.Long(3L)
  Long getTestDurationSeconds();

  void setTestDurationSeconds(Long value);

  @Description("Generate data interval in seconds")
  @Default.Long(1L)
  Long getGenerateDataIntervalSeconds();

  void setGenerateDataIntervalSeconds(Long value);

  @Description("BigTable family name")
  @Default.String("cf1")
  String getBigTableFamilyName();

  void setBigTableFamilyName(String value);

  @Description("Number of elements to generate per impulse")
  @Default.Enum("LOCAL")
  BigTableITHelper.Size getElementSizePerImpulse();

  void setElementSizePerImpulse(BigTableITHelper.Size value);

  @Description("Size per mutation")
  @Default.Enum("LOCAL")
  BigTableITHelper.Size getMutationSize();

  void setMutationSize(BigTableITHelper.Size value);

  @Description("BigTable connector")
  @Validation.Required
  BigTableITHelper.Connector getConnector();
  void setConnector(BigTableITHelper.Connector value);

  @Description("BigTable Instance ID")
  @Default.String("rrio-bigtable-it")
  String getInstanceId();
  void setInstanceId(String value);

  @Description("BigTable Table ID")
  @Default.String("rrio-bigtable-it-table")
  String getTableId();
  void setTableId(String value);
}
