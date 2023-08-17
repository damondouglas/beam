package org.apache.beam.examples.vertexai;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;
import static org.apache.beam.sdk.values.TypeDescriptors.rows;
import static org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Preconditions.checkState;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import com.google.cloud.Timestamp;
import com.google.cloud.bigquery.StandardTableDefinition;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload.JsonPayload;
import com.google.cloud.logging.Severity;
import com.google.cloud.spanner.Mutation.WriteBuilder;
import com.google.cloud.spanner.ValueBinder;
import com.google.gson.Gson;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineRunner;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO.TypedRead;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryUtils;
import org.apache.beam.sdk.io.gcp.spanner.SpannerIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.Schema.Field;
import org.apache.beam.sdk.schemas.Schema.FieldType;
import org.apache.beam.sdk.schemas.Schema.LogicalType;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.GroupIntoBatches;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.Values;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unused"})
public class ReadBigQueryWriteSpanner {

  private static final Pattern SOURCE =
      Pattern.compile("^(?<project>[a-z0-9\\-]+)\\.(?<dataset>[a-z0-9\\-]+)\\.(?<table>[a-z0-9_]+)$");
  private static final Pattern TARGET =
      Pattern.compile("^projects/(?<project>[a-z0-9\\-]+)/regions/(?<region>[a-z0-9\\-]+)/instances/(?<instance>[a-z0-9\\-]+)/databases/(?<database>[a-z0-9\\-]+)/tables/(?<table>[A-Za-z0-9]+)$");

  public interface Options extends PipelineOptions {
    @Required
    String getSource();
    void setSource(String value);

    @Required
    String getTarget();
    void setTarget(String value);
  }

  public static void main(String[] args) {
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
    Pipeline p = Pipeline.create(options);

    Logger info = new Logger(Severity.INFO, options.getRunner(), "debug");

    Schema schema = getTableSchema(options);

    p.apply("bigquery/read", bigqueryRead(options))
        .apply("bigquery/window", Window.into(FixedWindows.of(Duration.standardSeconds(1))))
        .apply("bigquery/torow", tableRowToBeamRow(schema)).setRowSchema(schema)
        .apply("rowToMutation", rowToMutation(options))
        .apply("spanner/write", spannerIOWrite(options));

    p.run();
  }

  private static SpannerIO.Write spannerIOWrite(Options options) {
    Matcher target = TARGET.matcher(options.getTarget());
    checkState(target.matches());
    String projectId = checkStateNotNull(target.group("project"));
    String instanceId = checkStateNotNull(target.group("instance"));
    String databaseId = checkStateNotNull(target.group("database"));
    return SpannerIO.write().withInstanceId(instanceId).withDatabaseId(databaseId)
        .withProjectId(projectId);
  }

  private static Schema getTableSchema(Options options) {
    Matcher source = SOURCE.matcher(options.getSource());
    checkState(source.matches());
    String project = checkStateNotNull(source.group("project"));
    String dataset = checkStateNotNull(source.group("dataset"));
    String tableName = checkStateNotNull(source.group("table"));
    TableId tableId = TableId.of(project, dataset, tableName);
    BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
    Table table = checkStateNotNull(bigquery.getTable(tableId));
    com.google.cloud.bigquery.Schema schema =
        checkStateNotNull(table.<StandardTableDefinition>getDefinition().getSchema());
    List<TableFieldSchema> fields = new ArrayList<>();
    for (com.google.cloud.bigquery.Field field : schema.getFields()) {
      fields.add(new TableFieldSchema().setName(field.getName())
          .setMode(field.getMode().name())
          .setType(field.getType().getStandardType().name()));
    }
    TableSchema tableSchema = new TableSchema().setFields(fields);
    return BigQueryUtils.fromTableSchema(tableSchema);
  }

  private static BigQueryIO.TypedRead<TableRow> bigqueryRead(Options options) {
    return BigQueryIO.readTableRows().from(options.getSource())
        .withMethod(TypedRead.Method.DIRECT_READ);
  }

  private static MapElements<Row, Mutation> rowToMutation(Options options) {
    Matcher target = TARGET.matcher(options.getTarget());
    checkState(target.matches());
    String tableId = checkStateNotNull(target.group("table"));
    return MapElements.into(TypeDescriptor.of(Mutation.class)).via(
        (Row row) -> {
          Schema schema = row.getSchema();
          Mutation.WriteBuilder builder = Mutation.newInsertBuilder(tableId);
          for (Field field : schema.getFields()) {
            ValueBinder<WriteBuilder> binder = builder.set(field.getName());
            if (field.getType().equals(FieldType.STRING)) {
              binder.to(row.getString(field.getName()));
              continue;
            }
            if (field.getType().equals(FieldType.DATETIME)) {
              Instant instance = checkStateNotNull(row.getDateTime(field.getName())).toInstant();
              binder.to(Timestamp.ofTimeSecondsAndNanos(instance.getMillis()/1000, 0));
              continue;
            }
            if (field.getType().equals(FieldType.DOUBLE)) {
              binder.to(row.getDouble(field.getName()));
            }
          }
          return builder.build();
        }
    );
  }

  private static MapElements<TableRow, Row> tableRowToBeamRow(@NonNull Schema schema) {
    return MapElements.into(rows())
        .via(tableRow -> BigQueryUtils
            .toBeamRow(schema, checkStateNotNull(tableRow)));
  }

  private static MapElements<Row, JsonPayload> tableRowToJson() {
    return MapElements.into(TypeDescriptor.of(JsonPayload.class)).via(
        row -> {
          Row safeRow = checkStateNotNull(row);
          Map<String, Object> values = new HashMap<>();
          for (Field field : safeRow.getSchema().getFields()) {
            values.put(field.getName(), safeRow.getValue(field.getName()).toString());
          }
          return JsonPayload.of(values);
        }
    );
  }

  private static class Logger extends PTransform<PCollection<JsonPayload>, PCollection<Void>> {

    private final Severity severity;
    private final Class<? extends PipelineRunner<?>> runner;
    private final String logName;

    private Logger(Severity severity, Class<? extends PipelineRunner<?>> runner, String name) {
      this.severity = severity;
      this.runner = runner;
      this.logName = name;
    }

    static Logger error(PipelineOptions options, String name) {
      return new Logger(Severity.ERROR, options.getRunner(), name);
    }

    static Logger info(PipelineOptions options, String name) {
      return new Logger(Severity.INFO, options.getRunner(), name);
    }

    @Override
    public PCollection<Void> expand(PCollection<JsonPayload> input) {
      PCollection<Iterable<JsonPayload>> entries =
          input
              .apply("log/applyKey", WithKeys.of(0))
              .apply(
                  "log/bundle",
                  GroupIntoBatches.<Integer, JsonPayload>ofSize(10L)
                      .withMaxBufferingDuration(Duration.standardSeconds(3L)))
              .apply("log/iterables", Values.create());

      if (runner.equals(DataflowRunner.class)) {
        return entries.apply("log/write", ParDo.of(new DataflowLoggerFn(this)));
      }
      return entries.apply("log/write", ParDo.of(new LocalLoggerFn(this)));
    }
  }

  private static class LocalLoggerFn extends DoFn<Iterable<JsonPayload>, Void> {

    private static final Gson GSON = new Gson();
    private final org.slf4j.Logger logger;
    private final Logger spec;

    private LocalLoggerFn(Logger spec) {
      this.spec = spec;
      this.logger = LoggerFactory.getLogger(spec.logName);
    }

    @ProcessElement
    public void process(@Element Iterable<JsonPayload> element) {
      for (JsonPayload payload : element) {
        logFn().accept(GSON.toJson(payload));
      }
    }

    private Consumer<String> logFn() {
      if (spec.severity.equals(Severity.ERROR)) {
        return logger::error;
      }
      return logger::info;
    }
  }


  private static class DataflowLoggerFn extends DoFn<Iterable<JsonPayload>, Void> {

    private final Logger spec;
    private transient @MonotonicNonNull Logging client;

    private DataflowLoggerFn(Logger spec) {
      this.spec = spec;
    }

    @Setup
    public void setup() {
      client = LoggingOptions.getDefaultInstance().getService();
    }

    @Teardown
    public void teardown() throws Exception {
      if (client != null) {
        checkStateNotNull(client).close();
      }
    }

    @ProcessElement
    public void process(@Element Iterable<JsonPayload> element) {
      Logging safeClient = checkStateNotNull(client);

      List<LogEntry> entries = new ArrayList<>();
      for (JsonPayload payload : element) {
        entries.add(
            LogEntry.newBuilder(payload)
                .setSeverity(spec.severity)
                .setLogName(spec.logName)
                .build());
      }
      safeClient.write(entries);
      safeClient.flush();
    }
  }
}
