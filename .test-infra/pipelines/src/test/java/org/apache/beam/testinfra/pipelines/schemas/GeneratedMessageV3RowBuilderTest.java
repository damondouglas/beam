package org.apache.beam.testinfra.pipelines.schemas;

import com.google.dataflow.v1beta3.Job;
import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Timestamp;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneratedMessageV3RowBuilderTest {

    @Test
    void build_Job() {
//        GeneratedMessageV3SchemaBuilder<Job> schemaBuilder = new GeneratedMessageV3SchemaBuilder<>(
//                Schema.builder(),
//                new GeneratedMessageV3Reflection<>(Job.class)
//        );
//        Schema schema = checkStateNotNull(schemaBuilder.build());
//        Row.Builder rowBuilder = Row.withSchema(schema);
        Job expected = Job.getDefaultInstance().toBuilder()
                .setId("id_value")
                .setClientRequestId("client_request_id_value")
                .setCreateTime(Timestamp.newBuilder().setSeconds(100000L).build())
                .addTempFiles("a")
                .addTempFiles("b")
                .addTempFiles("c")
                .putLabels("foo", "bar")
                .build();
        GeneratedMessageV3 message = expected;
        Descriptors.FieldDescriptor field = message.getDescriptorForType().findFieldByName("temp_files");
        assertTrue(field.isRepeated());
        assertEquals(Descriptors.FieldDescriptor.JavaType.STRING, field.getJavaType());
        assertEquals(3, message.getRepeatedFieldCount(field));
        assertEquals("a", message.getRepeatedField(field, 0));
        assertEquals("b", message.getRepeatedField(field, 1));
        assertEquals("c", message.getRepeatedField(field, 2));
        assertEquals(Arrays.asList("a", "b", "c"), message.getAllFields().get(field));
        assertFalse(field.isMapField());
        field = message.getDescriptorForType().findFieldByName("labels");
        assertTrue(field.isMapField());
//        rowBuilder.withFieldValue("temp_files", Arrays.asList("a", "b", "c"));
//        GeneratedMessageV3RowBuilder<Job> generatedRowBuilder = new GeneratedMessageV3RowBuilder<>(rowBuilder, Job.getDefaultInstance());
//        Row row = generatedRowBuilder.build();
//        assertNotNull(row);
//        assertEquals(expected.getId(), row.getString("id"));
//        assertEquals(expected.getClientRequestId(), row.getString("client_request_id"));
    }
}