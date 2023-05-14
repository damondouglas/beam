package org.apache.beam.testinfra.pipelines.schemas;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.Descriptor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

class DescriptorReflection {
    private final Descriptor descriptor;
    private final Map<String, Descriptors.FieldDescriptor> fieldDescriptorMap = new HashMap<>();
    DescriptorReflection(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    @NonNull
    Map<String, Descriptors.FieldDescriptor> getFieldDescriptorMap() {
        if (fieldDescriptorMap.isEmpty()) {
            for (Descriptors.FieldDescriptor field : descriptor.getFields()) {
                fieldDescriptorMap.put(field.getName(), field);
            }
        }
        return fieldDescriptorMap;
    }

    Descriptors.@NonNull FieldDescriptor getField(String name) {
        return checkStateNotNull(getFieldDescriptorMap().get(name));
    }

    @NonNull
    Collection<Descriptors.FieldDescriptor> getFields() {
        return getFieldDescriptorMap().values();
    }

    @NonNull
    String getName() {
        return descriptor.getName();
    }

    @NonNull
    String getFullName() {
        return descriptor.getFullName();
    }
}
