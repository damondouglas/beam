package org.apache.beam.io.requestresponse;

import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.Mutation;
import com.google.bigtable.v2.Row;
import com.google.protobuf.ByteString;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableWriteResult;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.PeriodicImpulse;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Duration;
import org.joda.time.Instant;

@AutoValue
abstract class BigTableITPipeline {

    static Builder builder() {
        return new AutoValue_BigTableITPipeline.Builder();
    }

    abstract PTransform<PBegin, PCollection<Row>> getReader();

    abstract PTransform<PCollection<KV<ByteString, Iterable<Mutation>>>, PCollection<BigtableWriteResult>> getWriter();

    abstract BigTableITOptions getOptions();

    Pipeline createWritePipeline() {
        Pipeline pipeline = Pipeline.create(getOptions());
        PCollection<Instant> impulse = pipeline.apply("impulse", PeriodicImpulse.create()
                        .withInterval(Duration.standardSeconds(getOptions().getGenerateDataIntervalSeconds()))
                .stopAfter(getOptions().getDuration()));

        BigTableITHelper.generateMutationsPerImpulse(getOptions().getBigTableFamilyName(), getOptions().getNumElementsPerImpulse(), getOptions().getMutationSize(), impulse)
                .apply("Write", getWriter())
                .apply("Count writes", BigTableITHelper.countWrites());

        return pipeline;
    }

    Pipeline createReadPipeline() {
        Pipeline pipeline = Pipeline.create(getOptions());

        pipeline
                .apply("Read", getReader())
                .apply("Count reads", BigTableITHelper.countReads());

        return pipeline;
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setReader(PTransform<PBegin, PCollection<Row>> value);
        abstract Builder setWriter(PTransform<PCollection<KV<ByteString, Iterable<Mutation>>>, PCollection<BigtableWriteResult>> value);

        abstract Builder setOptions(BigTableITOptions value);

        abstract BigTableITPipeline build();
    }
}
