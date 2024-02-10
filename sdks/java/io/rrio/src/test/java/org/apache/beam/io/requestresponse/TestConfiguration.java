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
import java.util.Map;
import java.util.Optional;
import org.joda.time.Duration;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

@AutoValue
abstract class TestConfiguration {

  static Builder builder() {
    return new AutoValue_TestConfiguration.Builder();
  }

  abstract Long getSize();

  abstract Integer getNumWorkers();

  abstract Duration getStopAfter();

  abstract Duration getInterval();

  Map<String, String> getLabels() {
    ImmutableMap.Builder<String, String> builder =
        ImmutableMap.<String, String>builder().put("size", getSize().toString());
    if (getNumWorkers() > 0) {
      builder = builder.put("numWorkers", getNumWorkers().toString());
    }
    if (getStopAfter().isLongerThan(Duration.ZERO)) {
      builder = builder.put("stopAfter", getStopAfter().toString());
    }
    if (getInterval().isLongerThan(Duration.ZERO)) {
      builder = builder.put("interval", getInterval().toString());
    }
    return builder.build();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSize(Long size);

    abstract Builder setNumWorkers(Integer numWorkers);

    abstract Optional<Integer> getNumWorkers();

    abstract Builder setStopAfter(Duration stopAfter);

    abstract Optional<Duration> getStopAfter();

    abstract Builder setInterval(Duration interval);

    abstract Optional<Duration> getInterval();

    abstract TestConfiguration autoBuild();

    final TestConfiguration build() {
      if (!getNumWorkers().isPresent()) {
        setNumWorkers(0);
      }
      if (!getStopAfter().isPresent()) {
        setStopAfter(Duration.ZERO);
      }
      if (!getInterval().isPresent()) {
        setInterval(Duration.ZERO);
      }

      return autoBuild();
    }
  }
}
