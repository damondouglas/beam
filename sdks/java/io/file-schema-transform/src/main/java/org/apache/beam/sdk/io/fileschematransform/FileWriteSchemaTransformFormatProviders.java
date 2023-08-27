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
package org.apache.beam.sdk.io.fileschematransform;

import static org.apache.beam.sdk.io.fileschematransform.FileWriteSchemaTransformProvider.ERROR_SCHEMA;
import static org.apache.beam.sdk.io.fileschematransform.FileWriteSchemaTransformProvider.ERROR_TAG;
import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.apache.beam.sdk.annotations.Internal;
import org.apache.beam.sdk.extensions.avro.schemas.utils.AvroUtils;
import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.io.FileBasedSink;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.io.Providers;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.base.Strings;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link FileWriteSchemaTransformFormatProviders} contains {@link
 * FileWriteSchemaTransformFormatProvider} implementations.
 *
 * <p>The design goals of this class are to enable clean {@link
 * FileWriteSchemaTransformConfiguration#getFormat()} lookups mapping to the appropriate {@link
 * org.apache.beam.sdk.io.FileIO.Write} that encodes the file data into the configured format.
 */
@Internal
public final class FileWriteSchemaTransformFormatProviders {

  static final String AVRO = "avro";
  static final String CSV = "csv";
  static final String JSON = "json";
  static final String PARQUET = "parquet";
  static final String XML = "xml";
  private static final Logger LOG =
      LoggerFactory.getLogger(FileWriteSchemaTransformFormatProviders.class);

  /** Load all {@link FileWriteSchemaTransformFormatProvider} implementations. */
  public static Map<String, FileWriteSchemaTransformFormatProvider> loadProviders() {
    return Providers.loadProviders(FileWriteSchemaTransformFormatProvider.class);
  }

  /** Builds a {@link MapElements}i transform to map {@link Row}s to {@link GenericRecord}s. */
  static MapElements<Row, GenericRecord> mapRowsToGenericRecords(Schema beamSchema) {
    return MapElements.into(TypeDescriptor.of(GenericRecord.class))
        .via(AvroUtils.getRowToGenericRecordFunction(AvroUtils.toAvroSchema(beamSchema)));
  }

  // Applies generic mapping from Beam row to other data types through the provided mapFn.
  // Implemenets error handling with metrics and DLQ support.
  // Arguments:
  //    name: the metric name to use.
  //    mapFn: the mapping function for mapping from Beam row to other data types.
  //    outputTag: TupleTag for output. Used to direct output to correct output source, or in the
  //        case of error, a DLQ.
  static class BeamRowMapperWithDlq<OutputT extends Object> extends DoFn<Row, OutputT> {
    private SerializableFunction<Row, OutputT> mapFn;
    private Counter errorCounter;
    private TupleTag<OutputT> outputTag;
    private long errorsInBundle = 0L;

    public BeamRowMapperWithDlq(
        String name, SerializableFunction<Row, OutputT> mapFn, TupleTag<OutputT> outputTag) {
      errorCounter = Metrics.counter(FileWriteSchemaTransformFormatProvider.class, name);
      this.mapFn = mapFn;
      this.outputTag = outputTag;
    }

    @ProcessElement
    public void process(@DoFn.Element Row row, MultiOutputReceiver receiver) {
      try {
        receiver.get(outputTag).output(mapFn.apply(row));
      } catch (Exception e) {
        errorsInBundle += 1;
        LOG.warn("Error while parsing input element", e);
        receiver
            .get(ERROR_TAG)
            .output(
                Row.withSchema(ERROR_SCHEMA)
                    .addValues(e.toString(), row.toString().getBytes(StandardCharsets.UTF_8))
                    .build());
      }
    }

    @FinishBundle
    public void finish() {
      errorCounter.inc(errorsInBundle);
      errorsInBundle = 0L;
    }
  }

  /**
   * Applies common parameters from {@link FileWriteSchemaTransformConfiguration} to {@link
   * FileIO.Write}.
   */
  static <T> FileIO.Write<Void, T> buildFileWrite(
      FileIO.Sink<T> sink, FileWriteSchemaTransformConfiguration configuration) {

    String suffix = "." + configuration.getFormat();
    if (!Strings.isNullOrEmpty(configuration.getFilenameSuffix())) {
      suffix = checkStateNotNull(configuration.getFilenameSuffix());
    }

    ResourceId resourceId =
        FileBasedSink.convertToFileResourceIfPossible(configuration.getFilenamePrefix());

    String to = resourceId.getCurrentDirectory().toString();

    String prefix = "output";
    if (!(resourceId.isDirectory() && Strings.isNullOrEmpty(resourceId.getFilename()))) {
      prefix = checkStateNotNull(resourceId.getFilename());
    }

    FileIO.Write<Void, T> write =
        FileIO.<T>write().to(to).withPrefix(prefix).withSuffix(suffix).via(sink);

    if (!isZeroValue(configuration.getNumShards())) {
      int numShards = getNumShards(configuration);
      write = write.withNumShards(numShards);
    }

    if (!Strings.isNullOrEmpty(configuration.getCompression())) {
      write = write.withCompression(getCompression(configuration, Compression.UNCOMPRESSED));
    }

    return write;
  }

  /**
   * Applies common parameters from {@link FileWriteSchemaTransformConfiguration} to {@link
   * TextIO.Write}.
   */
  static TextIO.Write applyCommonTextIOWriteFeatures(
      TextIO.Write write, FileWriteSchemaTransformConfiguration configuration) {
    write = write.to(configuration.getFilenamePrefix());

    if (!Strings.isNullOrEmpty(configuration.getFilenameSuffix())) {
      write = write.withSuffix(getFilenameSuffix(configuration));
    }

    if (!Strings.isNullOrEmpty(configuration.getCompression())) {
      write = write.withCompression(getCompression(configuration, Compression.UNCOMPRESSED));
    }

    if (!isZeroValue(configuration.getNumShards())) {
      int numShards = getNumShards(configuration);
      write = write.withNumShards(numShards);
    }

    if (!Strings.isNullOrEmpty(configuration.getShardNameTemplate())) {
      write = write.withShardNameTemplate(getShardNameTemplate(configuration));
    }

    return write;
  }

  /**
   * Resolves Checker Framework incompatible argument for valueOf parameter. Defaults to
   * defaultValue when {@code Strings.isNullOrEmpty(configuration.getCompression())}.
   */
  static @NonNull Compression getCompression(
      FileWriteSchemaTransformConfiguration configuration, @NonNull Compression defaultValue) {
    if (Strings.isNullOrEmpty(configuration.getCompression())) {
      return defaultValue;
    }
    String safeCompressionValue = checkStateNotNull(configuration.getCompression());
    try {
      return Compression.valueOf(safeCompressionValue);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(String.format("%s: %s", e, safeCompressionValue));
    }
  }

  /**
   * Resolves Checker Framework incompatible argument for parameter suffix of withSuffix. Defaults
   * to {@code "." + configuration.getFormat()} when {@link
   * FileWriteSchemaTransformConfiguration#getFilenameSuffix()} is null or empty.
   */
  static @NonNull String getFilenameSuffix(FileWriteSchemaTransformConfiguration configuration) {
    if (Strings.isNullOrEmpty(configuration.getFilenameSuffix())) {
      return "." + configuration.getFormat();
    }
    return checkStateNotNull(configuration.getFilenameSuffix());
  }

  static boolean isZeroValue(@Nullable Integer value) {
    if (value == null) {
      return true;
    }
    return value.equals(0);
  }

  /**
   * Resolves Checker Framework unboxing a possibly-null reference with {@code
   * configuration.getNumShards()}.
   */
  static @NonNull Integer getNumShards(FileWriteSchemaTransformConfiguration configuration) {
    return checkStateNotNull(configuration.getNumShards());
  }

  /**
   * Resolves Checker Framework incompatible null argument for {@code
   * configuration.getShardNameTemplate()}.
   */
  static @NonNull String getShardNameTemplate(FileWriteSchemaTransformConfiguration configuration) {
    return checkStateNotNull(configuration.getShardNameTemplate());
  }
}
