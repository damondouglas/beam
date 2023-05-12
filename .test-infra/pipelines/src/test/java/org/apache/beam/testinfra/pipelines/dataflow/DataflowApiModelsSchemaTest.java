package org.apache.beam.testinfra.pipelines.dataflow;

import com.google.cloud.bigquery.storage.v1.ProtoSchema;
import com.google.dataflow.v1beta3.Job;
import io.grpc.protobuf.ProtoUtils;
import org.apache.beam.sdk.schemas.JavaBeanSchema;
import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaUtils;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.utils.StaticSchemaInference;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataflowApiModelsSchemaTest {
//    private static final DefaultSchema.DefaultSchemaProvider SCHEMA_PROVIDER = new DefaultSchema.DefaultSchemaProvider();
    @Test
    public void canDeriveJobSchema() {

    }
}
