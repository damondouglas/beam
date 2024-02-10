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
package org.apache.beam.io.requestresponse;

import com.google.auto.value.AutoValue;
import com.google.protobuf.ByteString;
import java.util.UUID;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.testinfra.mockapis.echo.v1.Echo;
import org.joda.time.Instant;

@AutoValue
abstract class GenerateEchoRequests
    extends PTransform<PCollection<Instant>, PCollection<Echo.EchoRequest>> {

  static Builder builder() {
    return new AutoValue_GenerateEchoRequests.Builder();
  }

  abstract Long getNumRequestsPerInstant();

  abstract String getId();

  @Override
  public PCollection<Echo.EchoRequest> expand(PCollection<Instant> input) {
    return input.apply(GenerateFn.class.getSimpleName(), ParDo.of(new GenerateFn()));
  }

  private class GenerateFn extends DoFn<Instant, Echo.EchoRequest> {
    @ProcessElement
    public void process(OutputReceiver<Echo.EchoRequest> receiver) {
      for (long i = 0; i < getNumRequestsPerInstant(); i++) {
        receiver.output(
            Echo.EchoRequest.newBuilder()
                .setId(getId())
                .setPayload(ByteString.copyFromUtf8(UUID.randomUUID().toString()))
                .build());
      }
    }
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setId(String value);

    abstract Builder setNumRequestsPerInstant(Long value);

    abstract GenerateEchoRequests build();
  }
}
