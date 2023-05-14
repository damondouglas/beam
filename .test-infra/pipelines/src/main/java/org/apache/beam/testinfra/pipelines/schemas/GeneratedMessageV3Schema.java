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
package org.apache.beam.testinfra.pipelines.schemas;

import com.google.protobuf.GeneratedMessageV3;
import org.apache.beam.sdk.schemas.FieldValueGetter;
import org.apache.beam.sdk.schemas.FieldValueTypeInformation;
import org.apache.beam.sdk.schemas.GetterBasedSchemaProvider;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.SchemaUserTypeCreator;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class GeneratedMessageV3Schema {
//public class GeneratedMessageV3Schema extends GetterBasedSchemaProvider {
//
//    @Override
//    public @UnknownKeyFor
//    @NonNull
//    @Initialized List<
//                @UnknownKeyFor @NonNull @Initialized FieldValueGetter>
//        fieldValueGetters(
//            @UnknownKeyFor @NonNull @Initialized
//                Class<@UnknownKeyFor @NonNull @Initialized ?> targetClass,
//            @UnknownKeyFor @NonNull @Initialized Schema schema) {
//
//      TypeDescriptor<?> type = TypeDescriptor.of(targetClass);
//      checkArgument(
//          type.isSubtypeOf(TypeDescriptor.of(GeneratedMessageV3.class)),
//          "%s is not a subtype of %s",
//          targetClass,
//          GeneratedMessageV3.class);
//
//      return null;
//    }
//
//    @Override
//    public @UnknownKeyFor @NonNull @Initialized List<
//            @UnknownKeyFor @NonNull @Initialized FieldValueTypeInformation>
//        fieldValueTypeInformations(
//            @UnknownKeyFor @NonNull @Initialized
//                Class<@UnknownKeyFor @NonNull @Initialized ?> targetClass,
//            @UnknownKeyFor @NonNull @Initialized Schema schema) {
//      return null;
//    }
//
//    @Override
//    public @UnknownKeyFor @NonNull @Initialized SchemaUserTypeCreator schemaTypeCreator(
//        @UnknownKeyFor @NonNull @Initialized
//            Class<@UnknownKeyFor @NonNull @Initialized ?> targetClass,
//        @UnknownKeyFor @NonNull @Initialized Schema schema) {
//      return null;
//    }
//
//    @Override
//    public @Nullable @UnknownKeyFor @Initialized <T> Schema schemaFor(
//        @UnknownKeyFor @NonNull @Initialized TypeDescriptor<T> typeDescriptor) {
//      return null;
//    }
}
