package org.apache.beam.sdk.io.csv;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.common.SchemaAwareJavaBeans;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.schemas.io.payloads.JsonPayloadSerializerProvider;
import org.apache.beam.sdk.schemas.io.payloads.PayloadSerializer;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.collect.ImmutableMap;
import org.apache.commons.csv.CSVFormat;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.apache.beam.sdk.io.common.SchemaAwareJavaBeans.ALL_PRIMITIVE_DATA_TYPES_SCHEMA;
import static org.apache.beam.sdk.io.common.SchemaAwareJavaBeans.allPrimitiveDataTypes;
import static org.apache.beam.sdk.io.common.SchemaAwareJavaBeans.allPrimitiveDataTypesToRowFn;
import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

public class CsvIOParseLoadIT {
    private final Pipeline pipeline;
    private static final SerializableFunction<byte[], SchemaAwareJavaBeans.AllPrimitiveDataTypes> GENERATOR_FN
            = generateFn();

    private static final SerializableFunction<SchemaAwareJavaBeans.AllPrimitiveDataTypes, String> TO_CSV_FN
            = toCsvRecordFn();

    CsvIOParseLoadIT(Pipeline pipeline) {
        this.pipeline = pipeline;
    }


    public static void main(String[] args) {
        Options options = PipelineOptionsFactory
                .fromArgs(args)
                .withValidation()
                .as(Options.class);
        options.setJobName(CsvIOParseLoadIT.class.getSimpleName() + "-size-" + options.getSize() + "-" + System.currentTimeMillis());
        options.as(DataflowPipelineOptions.class)
                .setLabels(ImmutableMap.of("size", options.getSize().toString()));
        Pipeline p = Pipeline.create(options);
        String[] header = header();
        CsvIOParseLoadIT loadTest
                = new CsvIOParseLoadIT(p);
        CsvIOParse<SchemaAwareJavaBeans.AllPrimitiveDataTypes> underTest
                = CsvIO.parse(SchemaAwareJavaBeans.AllPrimitiveDataTypes.class, CSVFormat.DEFAULT.withHeader(header).withAllowDuplicateHeaderNames(false));
        CsvIOParseResult<SchemaAwareJavaBeans.AllPrimitiveDataTypes> result = loadTest.generateRecords(options.getSize()).apply(underTest);
        result.getErrors().apply(LogErrorsFn.class.getSimpleName(), ParDo.of(new LogErrorsFn()));
        PCollection<SchemaAwareJavaBeans.AllPrimitiveDataTypes> output = result.getOutput();
        if (options.getLogOutput()) {
            output.apply(LogInfoFn.class.getSimpleName(), ParDo.of(new LogInfoFn()));
        }

        p.run();
    }

    private PCollection<String> generateRecords(long size) {
        return pipeline
                .apply("seed", Create.of(size))
                .apply(GenerateFn.class.getSimpleName(), ParDo.of(new GenerateFn()));
    }

    private static class GenerateFn extends DoFn<Long, String> {

        @ProcessElement
        public void process(@Element Long element, OutputReceiver<String> receiver) {
            for (long i = 0; i < element; i++) {
                receiver.output(TO_CSV_FN.apply(GENERATOR_FN.apply(new byte[0])));
            }
        }
    }

    public interface Options extends PipelineOptions {
        @Description("The number CSV records to generate/load")
        @Default.Long(1000L)
        Long getSize();
        void setSize(Long value);

        @Description("If true, logs the parse result output")
        @Default.Boolean(false)
        Boolean getLogOutput();
        void setLogOutput(Boolean value);
    }

    private static SerializableFunction<byte[], SchemaAwareJavaBeans.AllPrimitiveDataTypes> generateFn() {
        boolean aBool = false;
        BigDecimal bigDecimal = BigDecimal.valueOf(1);
        Double aDouble = 1.0;
        Float aFloat = 1.0f;
        Integer anInteger = 1;
        Long aLong = Instant.now().getMillis();
        String aString = UUID.randomUUID().toString();
        return input -> allPrimitiveDataTypes(aBool, bigDecimal, aDouble, aFloat, anInteger, aLong, aString);
    }

    private static SerializableFunction<SchemaAwareJavaBeans.AllPrimitiveDataTypes, String> toCsvRecordFn() {
        return input -> String.format("%s,%s,%s,%s,%s,%s,%s",
                input.getABoolean(),
                input.getADecimal(),
                input.getADouble(),
                input.getAFloat(),
                input.getAnInteger(),
                input.getALong(),
                input.getAString());
    }

    private static String[] header() {
        return new String[]{"aBoolean", "aDecimal", "aDouble", "aFloat", "anInteger", "aLong", "aString"};
    }

    private static class LogErrorsFn extends DoFn<CsvIOParseError, CsvIOParseError> {
        private static final Logger LOG = LoggerFactory.getLogger(LogErrorsFn.class);
        private static final PayloadSerializer PAYLOAD_SERIALIZER =
                new JsonPayloadSerializerProvider().getSerializer(CsvIOParseError.SCHEMA, ImmutableMap.of());

        @ProcessElement
        public void process(@Element CsvIOParseError element, OutputReceiver<CsvIOParseError> receiver) {
            Row row = checkStateNotNull(CsvIOParseError.TO_ROW_FN.apply(element));
            byte[] bytes = checkStateNotNull(PAYLOAD_SERIALIZER.serialize(row));
            String json = new String(bytes, StandardCharsets.UTF_8);
            LOG.error(json);
            receiver.output(element);
        }
    }

    private static class LogInfoFn extends DoFn<SchemaAwareJavaBeans.AllPrimitiveDataTypes, SchemaAwareJavaBeans.AllPrimitiveDataTypes> {
        private static final Logger LOG = LoggerFactory.getLogger(LogInfoFn.class);
        private static final PayloadSerializer PAYLOAD_SERIALIZER
                = new JsonPayloadSerializerProvider().getSerializer(ALL_PRIMITIVE_DATA_TYPES_SCHEMA, ImmutableMap.of());
        private static final SerializableFunction<SchemaAwareJavaBeans.AllPrimitiveDataTypes, Row> TO_ROW_FN = allPrimitiveDataTypesToRowFn();

        @ProcessElement
        public void process(@Element SchemaAwareJavaBeans.AllPrimitiveDataTypes element, OutputReceiver<SchemaAwareJavaBeans.AllPrimitiveDataTypes> receiver) {
            Row row = checkStateNotNull(TO_ROW_FN.apply(element));
            byte[] bytes = checkStateNotNull(PAYLOAD_SERIALIZER.serialize(row));
            String json = new String(bytes, StandardCharsets.UTF_8);
            LOG.info(json);
            receiver.output(element);
        }
    }
}
