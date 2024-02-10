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

import static org.apache.beam.io.requestresponse.EchoITOptions.GRPC_ENDPOINT_ADDRESS_NAME;
import static org.apache.beam.sdk.io.common.IOITHelper.readIOTestPipelineOptions;

import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableSchema;
import com.google.api.services.bigquery.model.TimePartitioning;
import java.io.IOException;
import java.net.URI;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.beam.repackaged.core.org.apache.commons.lang3.RandomStringUtils;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.extensions.avro.schemas.utils.AvroUtils;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryUtils;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.testinfra.mockapis.echo.v1.Echo;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.CaseFormat;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Strings;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration tests to validate {@link RequestResponseIO} with {@link Throttle} using {@link
 * EchoGRPCCallerWithSetupTeardown}. See {@link EchoITOptions} for details on how to configure and
 * execute tests.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class RequestResponseIOWithThrottleIT {
  @Rule public TestName name = new TestName();

  private @MonotonicNonNull EchoITOptions options;
  private @MonotonicNonNull EchoGRPCCallerWithSetupTeardown client;
  private static final String ID = "id";
  private static final String PAYLOAD = "payload";
  private static final String JOB_NAME = "jobName";

  private static final Schema METADATA_SCHEMA =
      SchemaBuilder.builder().record("metadata").fields().requiredString(JOB_NAME).endRecord();

  private static final Schema ECHO_REQUEST_SCHEMA =
      SchemaBuilder.builder()
          .record(Echo.EchoRequest.class.getSimpleName())
          .fields()
          .requiredString(ID)
          .requiredString(PAYLOAD)
          .endRecord();

  private static final Schema ECHO_RESPONSE_SCHEMA =
      SchemaBuilder.builder()
          .record(Echo.EchoResponse.class.getSimpleName())
          .fields()
          .requiredString(ID)
          .requiredString(PAYLOAD)
          .endRecord();

  private static final SerializableFunction<Echo.EchoRequest, GenericRecord>
      ECHO_REQUEST_TO_RECORD_FN =
          request ->
              new GenericRecordBuilder(ECHO_REQUEST_SCHEMA)
                  .set(ID, request.getId())
                  .set(PAYLOAD, request.getPayload().toStringUtf8())
                  .build();
  private static final SerializableFunction<Echo.EchoResponse, GenericRecord>
      ECHO_RESPONSE_TO_RECORD_FN =
          response ->
              new GenericRecordBuilder(ECHO_RESPONSE_SCHEMA)
                  .set(ID, response.getId())
                  .set(PAYLOAD, response.getPayload().toStringUtf8())
                  .build();

  private static final SerializableFunction<PipelineOptions, GenericRecord> METADATA_SUPPLIER =
      options ->
          new GenericRecordBuilder(METADATA_SCHEMA).set(JOB_NAME, options.getJobName()).build();

  private static final String JOB_NAME_PREFIX =
      CaseFormat.LOWER_CAMEL.to(
          CaseFormat.LOWER_HYPHEN, RequestResponseIOWithThrottleIT.class.getSimpleName());

  // See
  // .test-infra/mock-apis/infrastructure/kubernetes/refresher/overlays/echo-RequestResponseIOWithThrottleIT-10-per-1s-quota
  private static final String QUOTA_ID = "echo-RequestResponseIOWithThrottleIT-10-per-1s-quota";
  private static final String DATASET = "requestresponse_io_it_tests";

  @Before
  public void setUp() throws UserCodeExecutionException {
    options = readIOTestPipelineOptions(EchoITOptions.class);
    if (Strings.isNullOrEmpty(options.getGrpcEndpointAddress())) {
      throw new RuntimeException(
          "--"
              + GRPC_ENDPOINT_ADDRESS_NAME
              + " is missing. See "
              + EchoITOptions.class
              + " for details.");
    }
    client = EchoGRPCCallerWithSetupTeardown.of(URI.create(options.getGrpcEndpointAddress()));
  }

  @Test
  public void testBatchASmall() throws IOException {
    run(TestConfiguration.builder()
            .setSize(1_000L)
            .build());
  }

  private void run(TestConfiguration configuration) {
    String random = RandomStringUtils.randomAlphanumeric(6).toLowerCase();
    String testName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, name.getMethodName());
    options.setJobName(String.join("-", JOB_NAME_PREFIX, testName, random));

    DataflowPipelineOptions dataflowPipelineOptions = options.as(DataflowPipelineOptions.class);
    dataflowPipelineOptions.setLabels(configuration.getLabels());

    if (configuration.getNumWorkers() > 0) {
      dataflowPipelineOptions.setNumWorkers(configuration.getNumWorkers());
      dataflowPipelineOptions.setMaxNumWorkers(configuration.getNumWorkers());
    }

    Pipeline pipeline = Pipeline.create(options);
    PCollection<Echo.EchoRequest> requests =
        pipeline
            .apply("impulse", Create.of(Instant.now()))
            .apply(
                GenerateEchoRequests.class.getSimpleName(),
                GenerateEchoRequests.builder()
                    .setId(QUOTA_ID)
                    .setNumRequestsPerInstant(configuration.getSize())
                    .build());

    Result<Echo.EchoResponse> result =
        requests
            .apply(
                Throttle.class.getSimpleName(),
                Throttle.of(Rate.of(10, Duration.standardSeconds(1L))))
            .apply(
                Echo.class.getSimpleName(),
                RequestResponseIO.ofCallerAndSetupTeardown(client, EchoResponseCoder.of()));

    requests
        .apply(
            "requestsToTestMeasure", toTestMeasure(ECHO_REQUEST_SCHEMA, ECHO_REQUEST_TO_RECORD_FN))
        .apply(
            "write requests",
            write(
                "requests_" + random,
                TestMeasure.buildSchemaOf(ECHO_REQUEST_SCHEMA, METADATA_SCHEMA)));

    result
        .getResponses()
        .apply(
            "responsesToTestMeasure",
            toTestMeasure(ECHO_RESPONSE_SCHEMA, ECHO_RESPONSE_TO_RECORD_FN))
        .apply(
            "write responses",
            write(
                "responses_" + random,
                TestMeasure.buildSchemaOf(ECHO_RESPONSE_SCHEMA, METADATA_SCHEMA)));

    result.getFailures().apply("failures", Log.error());

    pipeline.run();
  }

  private static PTransform<PBegin, PCollection<Instant>>

  private static <T> ToTestMeasure<T> toTestMeasure(
      Schema recordSchema, SerializableFunction<T, GenericRecord> toRecordFn) {
    return ToTestMeasure.<T>builder()
        .setMetadataSupplier(METADATA_SUPPLIER)
        .setMetadataSchema(METADATA_SCHEMA.toString())
        .setToGenericRecordFn(toRecordFn)
        .setGenericRecordSchema(recordSchema.toString())
        .build();
  }

  private BigQueryIO.Write<GenericRecord> write(String tableName, Schema avroSchema) {
    org.apache.beam.sdk.schemas.Schema beamSchema = AvroUtils.toBeamSchema(avroSchema);
    TableSchema tableSchema = BigQueryUtils.toTableSchema(beamSchema);
    return BigQueryIO.writeGenericRecords()
        .withSchema(tableSchema)
        .to(tableReferenceOf(tableName))
        .withTimePartitioning(new TimePartitioning())
        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
        .withMethod(BigQueryIO.Write.Method.STORAGE_WRITE_API);
  }

  private TableReference tableReferenceOf(String tableName) {
    GcpOptions gcpOptions = options.as(GcpOptions.class);
    return new TableReference()
        .setProjectId(gcpOptions.getProject())
        .setDatasetId(DATASET)
        .setTableId(tableName);
  }

  private static class Log {
    private static final Logger LOG = LoggerFactory.getLogger(Log.class);
    private static final Instant START = Instant.now();

    static <T> ParDo.SingleOutput<T, T> error() {
      return ParDo.of(new ErrorFn<>());
    }

    private static class ErrorFn<T> extends DoFn<T, T> {
      @ProcessElement
      public void process(@Element T t, OutputReceiver<T> receiver) {
        Duration elapsed = Duration.millis(Instant.now().getMillis() - START.getMillis());
        LOG.error("{}: {}", elapsed, t);
        receiver.output(t);
      }
    }
  }
}
