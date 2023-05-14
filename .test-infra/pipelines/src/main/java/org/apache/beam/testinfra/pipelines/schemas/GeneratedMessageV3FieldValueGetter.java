package org.apache.beam.testinfra.pipelines.schemas;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import org.apache.beam.sdk.schemas.FieldValueGetter;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

class GeneratedMessageV3FieldValueGetter<ObjectT extends GeneratedMessageV3, ValueT> implements FieldValueGetter<ObjectT, ValueT> {

    private final Descriptors.FieldDescriptor fieldDescriptor;
    GeneratedMessageV3FieldValueGetter(Descriptors.FieldDescriptor fieldDescriptor) {
        this.fieldDescriptor = fieldDescriptor;
    }

    @Override
    public @Nullable ValueT get(ObjectT object) {
        return (ValueT) checkStateNotNull(object.getAllFields().get(fieldDescriptor));
    }

    @Override
    public @UnknownKeyFor @NonNull @Initialized String name() {
        return fieldDescriptor.getName();
    }
}
