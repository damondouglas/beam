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

import com.google.auto.value.AutoValue;
import java.util.Optional;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.joda.time.Duration;
import org.joda.time.Instant;

@AutoValue
public abstract class DataflowIOReadJobsConfiguration {

  public abstract Instant getStart();

  public abstract Instant getStop();

  public abstract Duration getInterval();

  public abstract String getProject();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setStart(Instant value);

    abstract Optional<Instant> getStart();

    public abstract Builder setStop(Instant value);

    abstract Optional<Instant> getStop();

    public abstract Builder setInterval(Duration value);

    public abstract Builder setProject(String value);

    abstract Optional<Duration> getInterval();

    abstract DataflowIOReadJobsConfiguration autoBuild();

    public final DataflowIOReadJobsConfiguration build() {

      if (getStart().isEmpty()) {
        setStart(Instant.now());
      }

      if (getStop().isEmpty()) {
        setStop(BoundedWindow.TIMESTAMP_MAX_VALUE);
      }

      if (getInterval().isEmpty()) {
        setInterval(Duration.standardMinutes(1L));
      }

      return autoBuild();
    }
  }
}
