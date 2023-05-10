package org.apache.beam.testinfra.pipelines.bigquery;

import com.google.api.services.bigquery.model.TableReference;
import com.google.dataflow.v1beta3.Job;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joda.time.Instant;

public class BigQueryWriteJobs extends PTransform<@NonNull PCollection<Job>, @NonNull WriteResult> {

    public static BigQueryWriteJobs create(DatasetReferenceOptionValue dataset) {
        return new BigQueryWriteJobs(dataset);
    }

    private final DatasetReferenceOptionValue dataset;

    private BigQueryWriteJobs(DatasetReferenceOptionValue dataset) {
        this.dataset = dataset;
    }

    @Override
    public @NonNull WriteResult expand(PCollection<Job> input) {
        TableReference table = new TableReference()
                .setProjectId(dataset.getValue().getProjectId())
                .setDatasetId(dataset.getValue().getDatasetId())
                .setTableId(String.format("dataflow_jobs_%s", Instant.now().getMillis()));
        return input.apply("Write Jobs", BigQueryIO.<Job>write()
                .to(table)
                .withTimePartitioning());
    }
}
