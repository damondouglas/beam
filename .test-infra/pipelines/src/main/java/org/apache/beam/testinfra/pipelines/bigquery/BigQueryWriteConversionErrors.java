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
package org.apache.beam.testinfra.pipelines.bigquery;

import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TimePartitioning;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.testinfra.pipelines.conversions.ConversionError;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joda.time.Instant;

public class BigQueryWriteConversionErrors<SourceT>
    extends PTransform<@NonNull PCollection<ConversionError<SourceT>>, @NonNull WriteResult> {

  public static <SourceT> BigQueryWriteConversionErrors<SourceT> create(
      DatasetReferenceOptionValue dataset) {
    return new BigQueryWriteConversionErrors<>(dataset);
  }

  private final DatasetReferenceOptionValue dataset;

  private BigQueryWriteConversionErrors(DatasetReferenceOptionValue dataset) {
    this.dataset = dataset;
  }

  @Override
  public @NonNull WriteResult expand(PCollection<ConversionError<SourceT>> input) {
    TableReference table =
        new TableReference()
            .setProjectId(dataset.getValue().getProjectId())
            .setDatasetId(dataset.getValue().getDatasetId())
            .setTableId(String.format("conversion_errors_%s", Instant.now().getMillis()));

    return input.apply(
        "Write Conversion Errors",
        BigQueryIO.<ConversionError<SourceT>>write()
            .to(table)
            .useBeamSchema()
            .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
            .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
            .withTimePartitioning(
                new TimePartitioning().setType("HOUR").setField("observationTime")));
  }
}
