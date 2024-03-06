package org.apache.beam.io.requestresponse;

import com.google.bigtable.v2.Mutation;
import com.google.bigtable.v2.Row;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.coders.IterableCoder;
import org.apache.beam.sdk.coders.KvCoder;
import org.apache.beam.sdk.extensions.protobuf.ByteStringCoder;
import org.apache.beam.sdk.io.gcp.bigtable.BigtableWriteResult;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import com.google.protobuf.ByteString;
import org.joda.time.Instant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

final class BigTableITHelper {

    private static final Class<?> COUNTER_CLASS = BigTableITPipeline.class;

    private static final Counter COUNTER_GENERATE = Metrics.counter(COUNTER_CLASS, "num_elements_generated");
    private static final Counter COUNTER_WRITE = Metrics.counter(COUNTER_CLASS, "num_elements_written");
    private static final Counter COUNTER_READ = Metrics.counter(COUNTER_CLASS, "num_elements_read");

    private static final ByteStringCoder BYTE_STRING_CODER = ByteStringCoder.of();

    private static final MutationCoder MUTATION_CODER = MutationCoder.of();

    private static final IterableCoder<Mutation> ITERABLE_CODER = IterableCoder.of(MUTATION_CODER);

    private static final KvCoder<ByteString, Iterable<Mutation>> KV_CODER = KvCoder.of(BYTE_STRING_CODER, ITERABLE_CODER);

    static PCollection<KV<ByteString, Iterable<Mutation>>> generateMutationsPerImpulse(
            String familyName,
            int numPerImpulse,
            int sizePerMutation,
            PCollection<Instant> impulse
    ) {
        return impulse
                .apply("generateMutations", ParDo.of(new GenerateMutationFn(familyName, numPerImpulse, sizePerMutation)))
                .setCoder(KV_CODER);
    }
    private static class GenerateMutationFn extends DoFn<Instant, KV<ByteString, Iterable<Mutation>>> {
        
        private static final Random RANDOM = new Random();

        private final String familyName;
        private final int numPerImpulse;
        private final int sizePerMutation;

        private GenerateMutationFn(String familyName, int numPerImpulse, int sizePerMutation) {
            this.familyName = familyName;
            this.numPerImpulse = numPerImpulse;
            this.sizePerMutation = sizePerMutation;
        }


        @ProcessElement
        public void process(
                @Element Instant element,
                OutputReceiver<KV<ByteString, Iterable<Mutation>>> receiver
        ) {
            byte[] valBytes = new byte[sizePerMutation];
            RANDOM.nextBytes(valBytes);
            ByteString value = ByteString.copyFrom(valBytes);
            List<Mutation> result = new ArrayList<>();
            ByteString key = ByteString.copyFromUtf8(UUID.randomUUID().toString());
            for (int i = 0; i < numPerImpulse; i++) {
                result.add(
                        Mutation.newBuilder()
                                .setSetCell(Mutation.SetCell.newBuilder()
                                        .setFamilyName(familyName)
                                        .setTimestampMicros(element.getMillis() * 1_000L)
                                        .setValue(value)
                                        .build())
                                .build()
                );
            }
            COUNTER_GENERATE.inc(numPerImpulse);
            receiver.output(KV.of(key, result));
        }
    }

    private static class MutationCoder extends CustomCoder<Mutation> {
        public static MutationCoder of() {
            return new MutationCoder();
        }

        @Override
        public void encode(Mutation mutation, OutputStream outputStream) throws CoderException, IOException {
            mutation.writeTo(outputStream);
        }

        @Override
        public Mutation decode(InputStream inputStream) throws CoderException, IOException {
            return Mutation.parseFrom(inputStream);
        }
    }

    static ParDo.SingleOutput<Row, Row> countReads() {
        return ParDo.of(new ReadCounterFn());
    }

    private static class ReadCounterFn extends DoFn<Row, Row> {
        @ProcessElement
        public void process(
                @Element Row row,
                OutputReceiver<Row> receiver
        ) {
            COUNTER_READ.inc();
            receiver.output(row);
        }
    }

    static ParDo.SingleOutput<BigtableWriteResult, BigtableWriteResult> countWrites() {
        return ParDo.of(new WriteCounterFn());
    }

    private static class WriteCounterFn extends DoFn<BigtableWriteResult, BigtableWriteResult> {
        @ProcessElement
        public void process(
                @Element BigtableWriteResult result,
                OutputReceiver<BigtableWriteResult> receiver
        ) {
            COUNTER_WRITE.inc(result.getRowsWritten());
            receiver.output(result);
        }
    }
}
