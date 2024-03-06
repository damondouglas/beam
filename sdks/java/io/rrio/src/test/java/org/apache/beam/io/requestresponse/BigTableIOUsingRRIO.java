package org.apache.beam.io.requestresponse;

import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.Mutation;
import com.google.bigtable.v2.Row;
import com.google.protobuf.ByteString;
import org.apache.beam.sdk.extensions.protobuf.ProtoCoder;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableWriteResult;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableWriteResultCoder;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;

/** A test implementation of BigTable using {@link RequestResponseIO}. */
class BigTableIOUsingRRIO {

    static class Read extends PTransform<PBegin, PCollection<Row>> {

        @Override
        public PCollection<Row> expand(PBegin input) {
            return input.apply(Create.empty(ProtoCoder.of(Row.class)));
        }
    }

    static class Write extends PTransform<PCollection<KV<ByteString, Iterable<Mutation>>>, PCollection<BigtableWriteResult>> {

        @Override
        public PCollection<BigtableWriteResult> expand(PCollection<KV<ByteString, Iterable<Mutation>>> input) {
            return input.getPipeline().apply(Create.empty(BigtableWriteResultCoder.of()));
        }
    }

    @AutoValue
    abstract static class ReadConfiguration {

        @AutoValue.Builder
        abstract static class Builder {

            abstract ReadConfiguration build();
        }
    }

    @AutoValue
    abstract static class WriteConfiguration {

        @AutoValue.Builder
        abstract static class Builder {

            abstract WriteConfiguration build();
        }
    }
}
