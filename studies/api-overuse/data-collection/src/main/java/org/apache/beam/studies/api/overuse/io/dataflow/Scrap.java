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
package org.apache.beam.studies.api.overuse.io.dataflow;

import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceClient.ListTimeSeriesPagedResponse;
import com.google.monitoring.v3.Aggregation;
import com.google.monitoring.v3.Aggregation.Aligner;
import com.google.monitoring.v3.ListTimeSeriesRequest;
import com.google.monitoring.v3.MetricDescriptorName;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.protobuf.util.Timestamps;
import java.io.IOException;

public class Scrap {

  public static void main(String[] args) throws IOException {
    try (MetricServiceClient client = MetricServiceClient.create()) {
      MetricDescriptorName name =
          MetricDescriptorName.of(
              "apache-beam-testing", "serviceruntime.googleapis.com/quota/limit");

      long startMillis = System.currentTimeMillis() - ((60 * 1) * 1000);
      TimeInterval interval =
          TimeInterval.newBuilder()
              .setStartTime(Timestamps.fromMillis(startMillis))
              .setEndTime(Timestamps.fromMillis(System.currentTimeMillis()))
              .build();
      Aggregation aggregation =
          Aggregation.newBuilder().setPerSeriesAligner(Aligner.ALIGN_SUM).build();
      ListTimeSeriesRequest.Builder requestBuilder =
          ListTimeSeriesRequest.newBuilder()
              .setName("projects/apache-beam-testing")
              .setFilter(
                  String.format("metric.type=\"%s\"", "serviceruntime.googleapis.com/quota/limit"))
              .setInterval(interval)
              .setAggregation(aggregation);
      ListTimeSeriesRequest request = requestBuilder.build();

      ListTimeSeriesPagedResponse response = client.listTimeSeries(request);
      for (TimeSeries ts : response.iterateAll()) {
        System.out.println(ts);
      }
    }
  }
}
