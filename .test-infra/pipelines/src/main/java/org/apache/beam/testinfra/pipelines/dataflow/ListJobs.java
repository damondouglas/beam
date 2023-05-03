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

import com.google.api.services.dataflow.Dataflow.Projects.Jobs.List;
import com.google.api.services.dataflow.model.ListJobsResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Strings;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Throwables;

public class ListJobs extends PTransform<PCollection<ListJobsRequest>, ListJobsResult> {

  @Override
  public ListJobsResult expand(PCollection<ListJobsRequest> input) {

    PCollectionTuple pct =
        input.apply(
            "DataflowClientFactory.List.Jobs",
            ParDo.of(new ListJobsFn())
                .withOutputTags(
                    ListJobsResult.SUCCESS,
                    TupleTagList.of(
                        Arrays.asList(
                            ListJobsResult.FAILURE, ListJobsResult.WITH_NEXT_PAGE_TOKEN))));

    return ListJobsResult.from(pct);
  }

  static class ListJobsFn extends DoFn<ListJobsRequest, ListJobsResponse> {
    @ProcessElement
    public void process(List request, MultiOutputReceiver receiver) {
      try {
        ListJobsResponse response = request.execute();
        receiver.get(ListJobsResult.SUCCESS).output(response);
        if (!Strings.isNullOrEmpty(response.getNextPageToken())) {
          ListJobsRequest withTokenRequest = new ListJobsRequest(request.setPageToken(response.getNextPageToken()));
          receiver
              .get(ListJobsResult.WITH_NEXT_PAGE_TOKEN)
              .output(withTokenRequest);
        }
      } catch (IOException e) {
        Optional<String> safeMessage = Optional.ofNullable(e.getMessage());
        String message = "";
        if (safeMessage.isPresent()) {
          message = safeMessage.get();
        }
        DataflowApiError error =
            DataflowApiError.builder()
                .setErrorMessage(message)
                .setObservedTime(Instant.now())
                .setRequestPayload(request.toString())
                .setStackTrace(Throwables.getStackTraceAsString(e))
                .setStatusCode(request.getLastStatusCode())
                .build();
        receiver.get(ListJobsResult.FAILURE).output(error);
      }
    }
  }
}
