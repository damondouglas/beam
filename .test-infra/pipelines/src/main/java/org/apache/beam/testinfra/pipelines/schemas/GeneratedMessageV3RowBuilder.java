package org.apache.beam.testinfra.pipelines.schemas;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.GeneratedMessageV3;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.sdk.values.TypeDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;
import static org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Preconditions.checkArgument;
import static org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Preconditions.checkState;

class GeneratedMessageV3RowBuilder<T extends GeneratedMessageV3> {
    private final Row.Builder builder;
    private final Schema schema;
    private final Map<String, Object> target = new HashMap<>();
    private final T source;
    private final Descriptor descriptor;

    GeneratedMessageV3RowBuilder(Row.Builder builder, T source) {
        this.schema = builder.getSchema();
        this.builder = builder;
        this.source = source;
        this.descriptor = source.getDescriptorForType();
    }

    Row build() {

        return builder.withFieldValues(target).build();
    }

    private void parse() {

    }

    private <ValueT> List<ValueT> getRepeatedValue(FieldDescriptor fieldDescriptor) {
        checkArgument(fieldDescriptor.isRepeated());
        List<ValueT> result = new ArrayList<>();
        TypeDescriptor<ValueT> collectionType = new TypeDescriptor<ValueT>(){};
        int size = source.getRepeatedFieldCount(fieldDescriptor);
        for (int i = 0; i < size; i++) {
            Object value = checkStateNotNull(source.getRepeatedField(fieldDescriptor, i));
            checkState(collectionType.getRawType().isInstance(value));
            result.add((ValueT) collectionType.getRawType().cast(value));
        }
        return result;
    }

    private <ValueT> ValueT getValueOf(FieldDescriptor fieldDescriptor, Object value) {

    }
}
