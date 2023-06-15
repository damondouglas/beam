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
package org.apache.beam.testinfra.pipelines.proto.quota.v1;

@SuppressWarnings({
  "ReferenceEquality",
  "MissingOverride",
  "initialization.fields.uninitialized",
  "override.param",
  "assignment",
  "UnusedVariable"
})
public final class QuotaOuterClass {
  private QuotaOuterClass() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public interface CreateQuotaRequestOrBuilder
      extends
      // @@protoc_insertion_point(interface_extends:proto.quota.v1.CreateQuotaRequest)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return Whether the quota field is set.
     */
    boolean hasQuota();
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return The quota.
     */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota();
    /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getQuotaOrBuilder();
  }
  /**
   *
   *
   * <pre>
   * CreateQuotaRequest defines a request for a new quota entry.
   * </pre>
   *
   * Protobuf type {@code proto.quota.v1.CreateQuotaRequest}
   */
  public static final class CreateQuotaRequest extends com.google.protobuf.GeneratedMessageV3
      implements
      // @@protoc_insertion_point(message_implements:proto.quota.v1.CreateQuotaRequest)
      CreateQuotaRequestOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use CreateQuotaRequest.newBuilder() to construct.
    private CreateQuotaRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }

    private CreateQuotaRequest() {}

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
      return new CreateQuotaRequest();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_CreateQuotaRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_CreateQuotaRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
                  .class,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
                  .Builder.class);
    }

    public static final int QUOTA_FIELD_NUMBER = 1;
    private org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota quota_;
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return Whether the quota field is set.
     */
    @java.lang.Override
    public boolean hasQuota() {
      return quota_ != null;
    }
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return The quota.
     */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota() {
      return quota_ == null
          ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
              .getDefaultInstance()
          : quota_;
    }
    /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getQuotaOrBuilder() {
      return quota_ == null
          ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
              .getDefaultInstance()
          : quota_;
    }

    private byte memoizedIsInitialized = -1;

    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      if (quota_ != null) {
        output.writeMessage(1, getQuota());
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (quota_ != null) {
        size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getQuota());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest)) {
        return super.equals(obj);
      }
      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest other =
          (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest)
              obj;

      if (hasQuota() != other.hasQuota()) return false;
      if (hasQuota()) {
        if (!getQuota().equals(other.getQuota())) return false;
      }
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasQuota()) {
        hash = (37 * hash) + QUOTA_FIELD_NUMBER;
        hash = (53 * hash) + getQuota().hashCode();
      }
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(
            java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseDelimitedFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
            prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     *
     *
     * <pre>
     * CreateQuotaRequest defines a request for a new quota entry.
     * </pre>
     *
     * Protobuf type {@code proto.quota.v1.CreateQuotaRequest}
     */
    public static final class Builder
        extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
        implements
        // @@protoc_insertion_point(builder_implements:proto.quota.v1.CreateQuotaRequest)
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_CreateQuotaRequest_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_CreateQuotaRequest_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaRequest.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaRequest.Builder.class);
      }

      // Construct using
      // org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest.newBuilder()
      private Builder() {}

      private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
      }

      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        quota_ = null;
        if (quotaBuilder_ != null) {
          quotaBuilder_.dispose();
          quotaBuilder_ = null;
        }
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_CreateQuotaRequest_descriptor;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
          getDefaultInstanceForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
            .getDefaultInstance();
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
          build() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
            result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
          buildPartial() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
            result =
                new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaRequest(this);
        if (bitField0_ != 0) {
          buildPartial0(result);
        }
        onBuilt();
        return result;
      }

      private void buildPartial0(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
              result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.quota_ = quotaBuilder_ == null ? quota_ : quotaBuilder_.build();
        }
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }

      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.setField(field, value);
      }

      @java.lang.Override
      public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }

      @java.lang.Override
      public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }

      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index,
          java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }

      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other
            instanceof
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest) {
          return mergeFrom(
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .CreateQuotaRequest)
                  other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
              other) {
        if (other
            == org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
                .getDefaultInstance()) return this;
        if (other.hasQuota()) {
          mergeQuota(other.getQuota());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10:
                {
                  input.readMessage(getQuotaFieldBuilder().getBuilder(), extensionRegistry);
                  bitField0_ |= 0x00000001;
                  break;
                } // case 10
              default:
                {
                  if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                    done = true; // was an endgroup tag
                  }
                  break;
                } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }

      private int bitField0_;

      private org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota quota_;
      private com.google.protobuf.SingleFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          quotaBuilder_;
      /**
       * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
       *
       * @return Whether the quota field is set.
       */
      public boolean hasQuota() {
        return ((bitField0_ & 0x00000001) != 0);
      }
      /**
       * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
       *
       * @return The quota.
       */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota() {
        if (quotaBuilder_ == null) {
          return quota_ == null
              ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                  .getDefaultInstance()
              : quota_;
        } else {
          return quotaBuilder_.getMessage();
        }
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder setQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (quotaBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          quota_ = value;
        } else {
          quotaBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder setQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
              builderForValue) {
        if (quotaBuilder_ == null) {
          quota_ = builderForValue.build();
        } else {
          quotaBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder mergeQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (quotaBuilder_ == null) {
          if (((bitField0_ & 0x00000001) != 0)
              && quota_ != null
              && quota_
                  != org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                      .getDefaultInstance()) {
            getQuotaBuilder().mergeFrom(value);
          } else {
            quota_ = value;
          }
        } else {
          quotaBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder clearQuota() {
        bitField0_ = (bitField0_ & ~0x00000001);
        quota_ = null;
        if (quotaBuilder_ != null) {
          quotaBuilder_.dispose();
          quotaBuilder_ = null;
        }
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
          getQuotaBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getQuotaFieldBuilder().getBuilder();
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
          getQuotaOrBuilder() {
        if (quotaBuilder_ != null) {
          return quotaBuilder_.getMessageOrBuilder();
        } else {
          return quota_ == null
              ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                  .getDefaultInstance()
              : quota_;
        }
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      private com.google.protobuf.SingleFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          getQuotaFieldBuilder() {
        if (quotaBuilder_ == null) {
          quotaBuilder_ =
              new com.google.protobuf.SingleFieldBuilderV3<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .QuotaOrBuilder>(getQuota(), getParentForChildren(), isClean());
          quota_ = null;
        }
        return quotaBuilder_;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }

      // @@protoc_insertion_point(builder_scope:proto.quota.v1.CreateQuotaRequest)
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.CreateQuotaRequest)
    private static final org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        DEFAULT_INSTANCE;

    static {
      DEFAULT_INSTANCE =
          new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .CreateQuotaRequest();
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaRequest
        getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<CreateQuotaRequest> PARSER =
        new com.google.protobuf.AbstractParser<CreateQuotaRequest>() {
          @java.lang.Override
          public CreateQuotaRequest parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
              builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
              throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
              throw e.asInvalidProtocolBufferException()
                  .setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
              throw new com.google.protobuf.InvalidProtocolBufferException(e)
                  .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
          }
        };

    public static com.google.protobuf.Parser<CreateQuotaRequest> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<CreateQuotaRequest> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
        getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }

  public interface CreateQuotaResponseOrBuilder
      extends
      // @@protoc_insertion_point(interface_extends:proto.quota.v1.CreateQuotaResponse)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return Whether the quota field is set.
     */
    boolean hasQuota();
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return The quota.
     */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota();
    /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getQuotaOrBuilder();
  }
  /**
   *
   *
   * <pre>
   * CreateQuotaResponse defines the response to a CreateQuota fulfillment.
   * </pre>
   *
   * Protobuf type {@code proto.quota.v1.CreateQuotaResponse}
   */
  public static final class CreateQuotaResponse extends com.google.protobuf.GeneratedMessageV3
      implements
      // @@protoc_insertion_point(message_implements:proto.quota.v1.CreateQuotaResponse)
      CreateQuotaResponseOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use CreateQuotaResponse.newBuilder() to construct.
    private CreateQuotaResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }

    private CreateQuotaResponse() {}

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
      return new CreateQuotaResponse();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_CreateQuotaResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_CreateQuotaResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
                  .class,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
                  .Builder.class);
    }

    public static final int QUOTA_FIELD_NUMBER = 1;
    private org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota quota_;
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return Whether the quota field is set.
     */
    @java.lang.Override
    public boolean hasQuota() {
      return quota_ != null;
    }
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return The quota.
     */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota() {
      return quota_ == null
          ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
              .getDefaultInstance()
          : quota_;
    }
    /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getQuotaOrBuilder() {
      return quota_ == null
          ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
              .getDefaultInstance()
          : quota_;
    }

    private byte memoizedIsInitialized = -1;

    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      if (quota_ != null) {
        output.writeMessage(1, getQuota());
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (quota_ != null) {
        size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getQuota());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse)) {
        return super.equals(obj);
      }
      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse other =
          (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse)
              obj;

      if (hasQuota() != other.hasQuota()) return false;
      if (hasQuota()) {
        if (!getQuota().equals(other.getQuota())) return false;
      }
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasQuota()) {
        hash = (37 * hash) + QUOTA_FIELD_NUMBER;
        hash = (53 * hash) + getQuota().hashCode();
      }
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(
            java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseDelimitedFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
            prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     *
     *
     * <pre>
     * CreateQuotaResponse defines the response to a CreateQuota fulfillment.
     * </pre>
     *
     * Protobuf type {@code proto.quota.v1.CreateQuotaResponse}
     */
    public static final class Builder
        extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
        implements
        // @@protoc_insertion_point(builder_implements:proto.quota.v1.CreateQuotaResponse)
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_CreateQuotaResponse_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_CreateQuotaResponse_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaResponse.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaResponse.Builder.class);
      }

      // Construct using
      // org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse.newBuilder()
      private Builder() {}

      private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
      }

      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        quota_ = null;
        if (quotaBuilder_ != null) {
          quotaBuilder_.dispose();
          quotaBuilder_ = null;
        }
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_CreateQuotaResponse_descriptor;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
          getDefaultInstanceForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse.getDefaultInstance();
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
          build() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
            result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
          buildPartial() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
            result =
                new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaResponse(this);
        if (bitField0_ != 0) {
          buildPartial0(result);
        }
        onBuilt();
        return result;
      }

      private void buildPartial0(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
              result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.quota_ = quotaBuilder_ == null ? quota_ : quotaBuilder_.build();
        }
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }

      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.setField(field, value);
      }

      @java.lang.Override
      public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }

      @java.lang.Override
      public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }

      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index,
          java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }

      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other
            instanceof
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .CreateQuotaResponse) {
          return mergeFrom(
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .CreateQuotaResponse)
                  other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
              other) {
        if (other
            == org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .CreateQuotaResponse.getDefaultInstance()) return this;
        if (other.hasQuota()) {
          mergeQuota(other.getQuota());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10:
                {
                  input.readMessage(getQuotaFieldBuilder().getBuilder(), extensionRegistry);
                  bitField0_ |= 0x00000001;
                  break;
                } // case 10
              default:
                {
                  if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                    done = true; // was an endgroup tag
                  }
                  break;
                } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }

      private int bitField0_;

      private org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota quota_;
      private com.google.protobuf.SingleFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          quotaBuilder_;
      /**
       * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
       *
       * @return Whether the quota field is set.
       */
      public boolean hasQuota() {
        return ((bitField0_ & 0x00000001) != 0);
      }
      /**
       * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
       *
       * @return The quota.
       */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota() {
        if (quotaBuilder_ == null) {
          return quota_ == null
              ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                  .getDefaultInstance()
              : quota_;
        } else {
          return quotaBuilder_.getMessage();
        }
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder setQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (quotaBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          quota_ = value;
        } else {
          quotaBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder setQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
              builderForValue) {
        if (quotaBuilder_ == null) {
          quota_ = builderForValue.build();
        } else {
          quotaBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder mergeQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (quotaBuilder_ == null) {
          if (((bitField0_ & 0x00000001) != 0)
              && quota_ != null
              && quota_
                  != org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                      .getDefaultInstance()) {
            getQuotaBuilder().mergeFrom(value);
          } else {
            quota_ = value;
          }
        } else {
          quotaBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder clearQuota() {
        bitField0_ = (bitField0_ & ~0x00000001);
        quota_ = null;
        if (quotaBuilder_ != null) {
          quotaBuilder_.dispose();
          quotaBuilder_ = null;
        }
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
          getQuotaBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getQuotaFieldBuilder().getBuilder();
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
          getQuotaOrBuilder() {
        if (quotaBuilder_ != null) {
          return quotaBuilder_.getMessageOrBuilder();
        } else {
          return quota_ == null
              ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                  .getDefaultInstance()
              : quota_;
        }
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      private com.google.protobuf.SingleFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          getQuotaFieldBuilder() {
        if (quotaBuilder_ == null) {
          quotaBuilder_ =
              new com.google.protobuf.SingleFieldBuilderV3<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .QuotaOrBuilder>(getQuota(), getParentForChildren(), isClean());
          quota_ = null;
        }
        return quotaBuilder_;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }

      // @@protoc_insertion_point(builder_scope:proto.quota.v1.CreateQuotaResponse)
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.CreateQuotaResponse)
    private static final org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        DEFAULT_INSTANCE;

    static {
      DEFAULT_INSTANCE =
          new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .CreateQuotaResponse();
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .CreateQuotaResponse
        getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<CreateQuotaResponse> PARSER =
        new com.google.protobuf.AbstractParser<CreateQuotaResponse>() {
          @java.lang.Override
          public CreateQuotaResponse parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
              builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
              throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
              throw e.asInvalidProtocolBufferException()
                  .setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
              throw new com.google.protobuf.InvalidProtocolBufferException(e)
                  .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
          }
        };

    public static com.google.protobuf.Parser<CreateQuotaResponse> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<CreateQuotaResponse> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
        getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }

  public interface ListQuotasRequestOrBuilder
      extends
      // @@protoc_insertion_point(interface_extends:proto.quota.v1.ListQuotasRequest)
      com.google.protobuf.MessageOrBuilder {}
  /**
   *
   *
   * <pre>
   * ListQuotasRequest defines a request to list existing quota entries.
   * </pre>
   *
   * Protobuf type {@code proto.quota.v1.ListQuotasRequest}
   */
  public static final class ListQuotasRequest extends com.google.protobuf.GeneratedMessageV3
      implements
      // @@protoc_insertion_point(message_implements:proto.quota.v1.ListQuotasRequest)
      ListQuotasRequestOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use ListQuotasRequest.newBuilder() to construct.
    private ListQuotasRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }

    private ListQuotasRequest() {}

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
      return new ListQuotasRequest();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_ListQuotasRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_ListQuotasRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
                  .class,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
                  .Builder.class);
    }

    private byte memoizedIsInitialized = -1;

    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest)) {
        return super.equals(obj);
      }
      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest other =
          (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest)
              obj;

      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(
            java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseDelimitedFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
            prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     *
     *
     * <pre>
     * ListQuotasRequest defines a request to list existing quota entries.
     * </pre>
     *
     * Protobuf type {@code proto.quota.v1.ListQuotasRequest}
     */
    public static final class Builder
        extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
        implements
        // @@protoc_insertion_point(builder_implements:proto.quota.v1.ListQuotasRequest)
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_ListQuotasRequest_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_ListQuotasRequest_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
                    .class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
                    .Builder.class);
      }

      // Construct using
      // org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest.newBuilder()
      private Builder() {}

      private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
      }

      @java.lang.Override
      public Builder clear() {
        super.clear();
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_ListQuotasRequest_descriptor;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
          getDefaultInstanceForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
            .getDefaultInstance();
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
          build() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
            result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
          buildPartial() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
            result =
                new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .ListQuotasRequest(this);
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }

      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.setField(field, value);
      }

      @java.lang.Override
      public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }

      @java.lang.Override
      public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }

      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index,
          java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }

      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other
            instanceof
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest) {
          return mergeFrom(
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest)
                  other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
              other) {
        if (other
            == org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
                .getDefaultInstance()) return this;
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              default:
                {
                  if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                    done = true; // was an endgroup tag
                  }
                  break;
                } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }

      // @@protoc_insertion_point(builder_scope:proto.quota.v1.ListQuotasRequest)
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.ListQuotasRequest)
    private static final org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        DEFAULT_INSTANCE;

    static {
      DEFAULT_INSTANCE =
          new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .ListQuotasRequest();
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasRequest
        getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<ListQuotasRequest> PARSER =
        new com.google.protobuf.AbstractParser<ListQuotasRequest>() {
          @java.lang.Override
          public ListQuotasRequest parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
              builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
              throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
              throw e.asInvalidProtocolBufferException()
                  .setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
              throw new com.google.protobuf.InvalidProtocolBufferException(e)
                  .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
          }
        };

    public static com.google.protobuf.Parser<ListQuotasRequest> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<ListQuotasRequest> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
        getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }

  public interface ListQuotasResponseOrBuilder
      extends
      // @@protoc_insertion_point(interface_extends:proto.quota.v1.ListQuotasResponse)
      com.google.protobuf.MessageOrBuilder {

    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    java.util.List<org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota>
        getListList();
    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getList(int index);
    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    int getListCount();
    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    java.util.List<
            ? extends
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
        getListOrBuilderList();
    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getListOrBuilder(int index);
  }
  /**
   *
   *
   * <pre>
   * ListQuotaResponse defines the response to a ListQuota fulfillment.
   * </pre>
   *
   * Protobuf type {@code proto.quota.v1.ListQuotasResponse}
   */
  public static final class ListQuotasResponse extends com.google.protobuf.GeneratedMessageV3
      implements
      // @@protoc_insertion_point(message_implements:proto.quota.v1.ListQuotasResponse)
      ListQuotasResponseOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use ListQuotasResponse.newBuilder() to construct.
    private ListQuotasResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }

    private ListQuotasResponse() {
      list_ = java.util.Collections.emptyList();
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
      return new ListQuotasResponse();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_ListQuotasResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_ListQuotasResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
                  .class,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
                  .Builder.class);
    }

    public static final int LIST_FIELD_NUMBER = 1;

    @SuppressWarnings("serial")
    private java.util.List<org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota>
        list_;
    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    @java.lang.Override
    public java.util.List<org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota>
        getListList() {
      return list_;
    }
    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    @java.lang.Override
    public java.util.List<
            ? extends
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
        getListOrBuilderList() {
      return list_;
    }
    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    @java.lang.Override
    public int getListCount() {
      return list_.size();
    }
    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getList(
        int index) {
      return list_.get(index);
    }
    /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getListOrBuilder(int index) {
      return list_.get(index);
    }

    private byte memoizedIsInitialized = -1;

    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      for (int i = 0; i < list_.size(); i++) {
        output.writeMessage(1, list_.get(i));
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < list_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, list_.get(i));
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse)) {
        return super.equals(obj);
      }
      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse other =
          (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse)
              obj;

      if (!getListList().equals(other.getListList())) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (getListCount() > 0) {
        hash = (37 * hash) + LIST_FIELD_NUMBER;
        hash = (53 * hash) + getListList().hashCode();
      }
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(
            java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseDelimitedFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
            prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     *
     *
     * <pre>
     * ListQuotaResponse defines the response to a ListQuota fulfillment.
     * </pre>
     *
     * Protobuf type {@code proto.quota.v1.ListQuotasResponse}
     */
    public static final class Builder
        extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
        implements
        // @@protoc_insertion_point(builder_implements:proto.quota.v1.ListQuotasResponse)
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_ListQuotasResponse_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_ListQuotasResponse_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .ListQuotasResponse.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .ListQuotasResponse.Builder.class);
      }

      // Construct using
      // org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse.newBuilder()
      private Builder() {}

      private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
      }

      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        if (listBuilder_ == null) {
          list_ = java.util.Collections.emptyList();
        } else {
          list_ = null;
          listBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_ListQuotasResponse_descriptor;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
          getDefaultInstanceForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
            .getDefaultInstance();
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
          build() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
            result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
          buildPartial() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
            result =
                new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .ListQuotasResponse(this);
        buildPartialRepeatedFields(result);
        if (bitField0_ != 0) {
          buildPartial0(result);
        }
        onBuilt();
        return result;
      }

      private void buildPartialRepeatedFields(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
              result) {
        if (listBuilder_ == null) {
          if (((bitField0_ & 0x00000001) != 0)) {
            list_ = java.util.Collections.unmodifiableList(list_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.list_ = list_;
        } else {
          result.list_ = listBuilder_.build();
        }
      }

      private void buildPartial0(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
              result) {
        int from_bitField0_ = bitField0_;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }

      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.setField(field, value);
      }

      @java.lang.Override
      public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }

      @java.lang.Override
      public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }

      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index,
          java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }

      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other
            instanceof
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse) {
          return mergeFrom(
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .ListQuotasResponse)
                  other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
              other) {
        if (other
            == org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
                .getDefaultInstance()) return this;
        if (listBuilder_ == null) {
          if (!other.list_.isEmpty()) {
            if (list_.isEmpty()) {
              list_ = other.list_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureListIsMutable();
              list_.addAll(other.list_);
            }
            onChanged();
          }
        } else {
          if (!other.list_.isEmpty()) {
            if (listBuilder_.isEmpty()) {
              listBuilder_.dispose();
              listBuilder_ = null;
              list_ = other.list_;
              bitField0_ = (bitField0_ & ~0x00000001);
              listBuilder_ =
                  com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders
                      ? getListFieldBuilder()
                      : null;
            } else {
              listBuilder_.addAllMessages(other.list_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10:
                {
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota m =
                      input.readMessage(
                          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                              .parser(),
                          extensionRegistry);
                  if (listBuilder_ == null) {
                    ensureListIsMutable();
                    list_.add(m);
                  } else {
                    listBuilder_.addMessage(m);
                  }
                  break;
                } // case 10
              default:
                {
                  if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                    done = true; // was an endgroup tag
                  }
                  break;
                } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }

      private int bitField0_;

      private java.util.List<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota>
          list_ = java.util.Collections.emptyList();

      private void ensureListIsMutable() {
        if (!((bitField0_ & 0x00000001) != 0)) {
          list_ =
              new java.util.ArrayList<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota>(list_);
          bitField0_ |= 0x00000001;
        }
      }

      private com.google.protobuf.RepeatedFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          listBuilder_;

      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public java.util.List<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota>
          getListList() {
        if (listBuilder_ == null) {
          return java.util.Collections.unmodifiableList(list_);
        } else {
          return listBuilder_.getMessageList();
        }
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public int getListCount() {
        if (listBuilder_ == null) {
          return list_.size();
        } else {
          return listBuilder_.getCount();
        }
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getList(
          int index) {
        if (listBuilder_ == null) {
          return list_.get(index);
        } else {
          return listBuilder_.getMessage(index);
        }
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public Builder setList(
          int index,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (listBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureListIsMutable();
          list_.set(index, value);
          onChanged();
        } else {
          listBuilder_.setMessage(index, value);
        }
        return this;
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public Builder setList(
          int index,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
              builderForValue) {
        if (listBuilder_ == null) {
          ensureListIsMutable();
          list_.set(index, builderForValue.build());
          onChanged();
        } else {
          listBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public Builder addList(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (listBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureListIsMutable();
          list_.add(value);
          onChanged();
        } else {
          listBuilder_.addMessage(value);
        }
        return this;
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public Builder addList(
          int index,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (listBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureListIsMutable();
          list_.add(index, value);
          onChanged();
        } else {
          listBuilder_.addMessage(index, value);
        }
        return this;
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public Builder addList(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
              builderForValue) {
        if (listBuilder_ == null) {
          ensureListIsMutable();
          list_.add(builderForValue.build());
          onChanged();
        } else {
          listBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public Builder addList(
          int index,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
              builderForValue) {
        if (listBuilder_ == null) {
          ensureListIsMutable();
          list_.add(index, builderForValue.build());
          onChanged();
        } else {
          listBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public Builder addAllList(
          java.lang.Iterable<
                  ? extends
                      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota>
              values) {
        if (listBuilder_ == null) {
          ensureListIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(values, list_);
          onChanged();
        } else {
          listBuilder_.addAllMessages(values);
        }
        return this;
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public Builder clearList() {
        if (listBuilder_ == null) {
          list_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          listBuilder_.clear();
        }
        return this;
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public Builder removeList(int index) {
        if (listBuilder_ == null) {
          ensureListIsMutable();
          list_.remove(index);
          onChanged();
        } else {
          listBuilder_.remove(index);
        }
        return this;
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
          getListBuilder(int index) {
        return getListFieldBuilder().getBuilder(index);
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
          getListOrBuilder(int index) {
        if (listBuilder_ == null) {
          return list_.get(index);
        } else {
          return listBuilder_.getMessageOrBuilder(index);
        }
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public java.util.List<
              ? extends
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          getListOrBuilderList() {
        if (listBuilder_ != null) {
          return listBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(list_);
        }
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
          addListBuilder() {
        return getListFieldBuilder()
            .addBuilder(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                    .getDefaultInstance());
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
          addListBuilder(int index) {
        return getListFieldBuilder()
            .addBuilder(
                index,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                    .getDefaultInstance());
      }
      /** <code>repeated .proto.quota.v1.Quota list = 1 [json_name = "list"];</code> */
      public java.util.List<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder>
          getListBuilderList() {
        return getListFieldBuilder().getBuilderList();
      }

      private com.google.protobuf.RepeatedFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          getListFieldBuilder() {
        if (listBuilder_ == null) {
          listBuilder_ =
              new com.google.protobuf.RepeatedFieldBuilderV3<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .QuotaOrBuilder>(
                  list_, ((bitField0_ & 0x00000001) != 0), getParentForChildren(), isClean());
          list_ = null;
        }
        return listBuilder_;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }

      // @@protoc_insertion_point(builder_scope:proto.quota.v1.ListQuotasResponse)
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.ListQuotasResponse)
    private static final org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        DEFAULT_INSTANCE;

    static {
      DEFAULT_INSTANCE =
          new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .ListQuotasResponse();
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .ListQuotasResponse
        getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<ListQuotasResponse> PARSER =
        new com.google.protobuf.AbstractParser<ListQuotasResponse>() {
          @java.lang.Override
          public ListQuotasResponse parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
              builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
              throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
              throw e.asInvalidProtocolBufferException()
                  .setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
              throw new com.google.protobuf.InvalidProtocolBufferException(e)
                  .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
          }
        };

    public static com.google.protobuf.Parser<ListQuotasResponse> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<ListQuotasResponse> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
        getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }

  public interface DeleteQuotaRequestOrBuilder
      extends
      // @@protoc_insertion_point(interface_extends:proto.quota.v1.DeleteQuotaRequest)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The id.
     */
    java.lang.String getId();
    /**
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The bytes for id.
     */
    com.google.protobuf.ByteString getIdBytes();
  }
  /**
   *
   *
   * <pre>
   * DeleteQuotasRequest defines a request to delete a quota entry.
   * </pre>
   *
   * Protobuf type {@code proto.quota.v1.DeleteQuotaRequest}
   */
  public static final class DeleteQuotaRequest extends com.google.protobuf.GeneratedMessageV3
      implements
      // @@protoc_insertion_point(message_implements:proto.quota.v1.DeleteQuotaRequest)
      DeleteQuotaRequestOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use DeleteQuotaRequest.newBuilder() to construct.
    private DeleteQuotaRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }

    private DeleteQuotaRequest() {
      id_ = "";
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
      return new DeleteQuotaRequest();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_DeleteQuotaRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_DeleteQuotaRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
                  .class,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
                  .Builder.class);
    }

    public static final int ID_FIELD_NUMBER = 1;

    @SuppressWarnings("serial")
    private volatile java.lang.Object id_ = "";
    /**
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The id.
     */
    @java.lang.Override
    public java.lang.String getId() {
      java.lang.Object ref = id_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        id_ = s;
        return s;
      }
    }
    /**
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The bytes for id.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getIdBytes() {
      java.lang.Object ref = id_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
        id_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;

    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, id_);
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, id_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest)) {
        return super.equals(obj);
      }
      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest other =
          (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest)
              obj;

      if (!getId().equals(other.getId())) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + getId().hashCode();
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(
            java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseDelimitedFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
            prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     *
     *
     * <pre>
     * DeleteQuotasRequest defines a request to delete a quota entry.
     * </pre>
     *
     * Protobuf type {@code proto.quota.v1.DeleteQuotaRequest}
     */
    public static final class Builder
        extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
        implements
        // @@protoc_insertion_point(builder_implements:proto.quota.v1.DeleteQuotaRequest)
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DeleteQuotaRequest_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DeleteQuotaRequest_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaRequest.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaRequest.Builder.class);
      }

      // Construct using
      // org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest.newBuilder()
      private Builder() {}

      private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
      }

      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        id_ = "";
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DeleteQuotaRequest_descriptor;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
          getDefaultInstanceForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
            .getDefaultInstance();
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
          build() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
            result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
          buildPartial() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
            result =
                new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaRequest(this);
        if (bitField0_ != 0) {
          buildPartial0(result);
        }
        onBuilt();
        return result;
      }

      private void buildPartial0(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
              result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.id_ = id_;
        }
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }

      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.setField(field, value);
      }

      @java.lang.Override
      public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }

      @java.lang.Override
      public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }

      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index,
          java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }

      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other
            instanceof
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest) {
          return mergeFrom(
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DeleteQuotaRequest)
                  other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
              other) {
        if (other
            == org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
                .getDefaultInstance()) return this;
        if (!other.getId().isEmpty()) {
          id_ = other.id_;
          bitField0_ |= 0x00000001;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10:
                {
                  id_ = input.readStringRequireUtf8();
                  bitField0_ |= 0x00000001;
                  break;
                } // case 10
              default:
                {
                  if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                    done = true; // was an endgroup tag
                  }
                  break;
                } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }

      private int bitField0_;

      private java.lang.Object id_ = "";
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @return The id.
       */
      public java.lang.String getId() {
        java.lang.Object ref = id_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          id_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @return The bytes for id.
       */
      public com.google.protobuf.ByteString getIdBytes() {
        java.lang.Object ref = id_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
          id_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @param value The id to set.
       * @return This builder for chaining.
       */
      public Builder setId(java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }
        id_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @return This builder for chaining.
       */
      public Builder clearId() {
        id_ = getDefaultInstance().getId();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @param value The bytes for id to set.
       * @return This builder for chaining.
       */
      public Builder setIdBytes(com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        checkByteStringIsUtf8(value);
        id_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }

      // @@protoc_insertion_point(builder_scope:proto.quota.v1.DeleteQuotaRequest)
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.DeleteQuotaRequest)
    private static final org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        DEFAULT_INSTANCE;

    static {
      DEFAULT_INSTANCE =
          new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DeleteQuotaRequest();
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaRequest
        getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<DeleteQuotaRequest> PARSER =
        new com.google.protobuf.AbstractParser<DeleteQuotaRequest>() {
          @java.lang.Override
          public DeleteQuotaRequest parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
              builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
              throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
              throw e.asInvalidProtocolBufferException()
                  .setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
              throw new com.google.protobuf.InvalidProtocolBufferException(e)
                  .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
          }
        };

    public static com.google.protobuf.Parser<DeleteQuotaRequest> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<DeleteQuotaRequest> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
        getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }

  public interface DeleteQuotaResponseOrBuilder
      extends
      // @@protoc_insertion_point(interface_extends:proto.quota.v1.DeleteQuotaResponse)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return Whether the quota field is set.
     */
    boolean hasQuota();
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return The quota.
     */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota();
    /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getQuotaOrBuilder();
  }
  /**
   *
   *
   * <pre>
   * DeleteQuotaResponse defines the response to a DeleteQuota fulfillment.
   * </pre>
   *
   * Protobuf type {@code proto.quota.v1.DeleteQuotaResponse}
   */
  public static final class DeleteQuotaResponse extends com.google.protobuf.GeneratedMessageV3
      implements
      // @@protoc_insertion_point(message_implements:proto.quota.v1.DeleteQuotaResponse)
      DeleteQuotaResponseOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use DeleteQuotaResponse.newBuilder() to construct.
    private DeleteQuotaResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }

    private DeleteQuotaResponse() {}

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
      return new DeleteQuotaResponse();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_DeleteQuotaResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_DeleteQuotaResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
                  .class,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
                  .Builder.class);
    }

    public static final int QUOTA_FIELD_NUMBER = 1;
    private org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota quota_;
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return Whether the quota field is set.
     */
    @java.lang.Override
    public boolean hasQuota() {
      return quota_ != null;
    }
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return The quota.
     */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota() {
      return quota_ == null
          ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
              .getDefaultInstance()
          : quota_;
    }
    /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getQuotaOrBuilder() {
      return quota_ == null
          ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
              .getDefaultInstance()
          : quota_;
    }

    private byte memoizedIsInitialized = -1;

    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      if (quota_ != null) {
        output.writeMessage(1, getQuota());
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (quota_ != null) {
        size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getQuota());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse)) {
        return super.equals(obj);
      }
      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse other =
          (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse)
              obj;

      if (hasQuota() != other.hasQuota()) return false;
      if (hasQuota()) {
        if (!getQuota().equals(other.getQuota())) return false;
      }
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasQuota()) {
        hash = (37 * hash) + QUOTA_FIELD_NUMBER;
        hash = (53 * hash) + getQuota().hashCode();
      }
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(
            java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseDelimitedFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
            prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     *
     *
     * <pre>
     * DeleteQuotaResponse defines the response to a DeleteQuota fulfillment.
     * </pre>
     *
     * Protobuf type {@code proto.quota.v1.DeleteQuotaResponse}
     */
    public static final class Builder
        extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
        implements
        // @@protoc_insertion_point(builder_implements:proto.quota.v1.DeleteQuotaResponse)
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DeleteQuotaResponse_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DeleteQuotaResponse_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaResponse.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaResponse.Builder.class);
      }

      // Construct using
      // org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse.newBuilder()
      private Builder() {}

      private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
      }

      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        quota_ = null;
        if (quotaBuilder_ != null) {
          quotaBuilder_.dispose();
          quotaBuilder_ = null;
        }
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DeleteQuotaResponse_descriptor;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
          getDefaultInstanceForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse.getDefaultInstance();
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
          build() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
            result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
          buildPartial() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
            result =
                new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaResponse(this);
        if (bitField0_ != 0) {
          buildPartial0(result);
        }
        onBuilt();
        return result;
      }

      private void buildPartial0(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
              result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.quota_ = quotaBuilder_ == null ? quota_ : quotaBuilder_.build();
        }
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }

      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.setField(field, value);
      }

      @java.lang.Override
      public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }

      @java.lang.Override
      public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }

      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index,
          java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }

      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other
            instanceof
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .DeleteQuotaResponse) {
          return mergeFrom(
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DeleteQuotaResponse)
                  other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
              other) {
        if (other
            == org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .DeleteQuotaResponse.getDefaultInstance()) return this;
        if (other.hasQuota()) {
          mergeQuota(other.getQuota());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10:
                {
                  input.readMessage(getQuotaFieldBuilder().getBuilder(), extensionRegistry);
                  bitField0_ |= 0x00000001;
                  break;
                } // case 10
              default:
                {
                  if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                    done = true; // was an endgroup tag
                  }
                  break;
                } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }

      private int bitField0_;

      private org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota quota_;
      private com.google.protobuf.SingleFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          quotaBuilder_;
      /**
       * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
       *
       * @return Whether the quota field is set.
       */
      public boolean hasQuota() {
        return ((bitField0_ & 0x00000001) != 0);
      }
      /**
       * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
       *
       * @return The quota.
       */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota() {
        if (quotaBuilder_ == null) {
          return quota_ == null
              ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                  .getDefaultInstance()
              : quota_;
        } else {
          return quotaBuilder_.getMessage();
        }
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder setQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (quotaBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          quota_ = value;
        } else {
          quotaBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder setQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
              builderForValue) {
        if (quotaBuilder_ == null) {
          quota_ = builderForValue.build();
        } else {
          quotaBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder mergeQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (quotaBuilder_ == null) {
          if (((bitField0_ & 0x00000001) != 0)
              && quota_ != null
              && quota_
                  != org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                      .getDefaultInstance()) {
            getQuotaBuilder().mergeFrom(value);
          } else {
            quota_ = value;
          }
        } else {
          quotaBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder clearQuota() {
        bitField0_ = (bitField0_ & ~0x00000001);
        quota_ = null;
        if (quotaBuilder_ != null) {
          quotaBuilder_.dispose();
          quotaBuilder_ = null;
        }
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
          getQuotaBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getQuotaFieldBuilder().getBuilder();
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
          getQuotaOrBuilder() {
        if (quotaBuilder_ != null) {
          return quotaBuilder_.getMessageOrBuilder();
        } else {
          return quota_ == null
              ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                  .getDefaultInstance()
              : quota_;
        }
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      private com.google.protobuf.SingleFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          getQuotaFieldBuilder() {
        if (quotaBuilder_ == null) {
          quotaBuilder_ =
              new com.google.protobuf.SingleFieldBuilderV3<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .QuotaOrBuilder>(getQuota(), getParentForChildren(), isClean());
          quota_ = null;
        }
        return quotaBuilder_;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }

      // @@protoc_insertion_point(builder_scope:proto.quota.v1.DeleteQuotaResponse)
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.DeleteQuotaResponse)
    private static final org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        DEFAULT_INSTANCE;

    static {
      DEFAULT_INSTANCE =
          new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DeleteQuotaResponse();
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DeleteQuotaResponse
        getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<DeleteQuotaResponse> PARSER =
        new com.google.protobuf.AbstractParser<DeleteQuotaResponse>() {
          @java.lang.Override
          public DeleteQuotaResponse parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
              builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
              throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
              throw e.asInvalidProtocolBufferException()
                  .setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
              throw new com.google.protobuf.InvalidProtocolBufferException(e)
                  .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
          }
        };

    public static com.google.protobuf.Parser<DeleteQuotaResponse> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<DeleteQuotaResponse> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
        getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }

  public interface DescribeQuotaRequestOrBuilder
      extends
      // @@protoc_insertion_point(interface_extends:proto.quota.v1.DescribeQuotaRequest)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The id.
     */
    java.lang.String getId();
    /**
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The bytes for id.
     */
    com.google.protobuf.ByteString getIdBytes();
  }
  /**
   *
   *
   * <pre>
   * DescribeQuotaResponse defines the request to describe a quota entry.
   * </pre>
   *
   * Protobuf type {@code proto.quota.v1.DescribeQuotaRequest}
   */
  public static final class DescribeQuotaRequest extends com.google.protobuf.GeneratedMessageV3
      implements
      // @@protoc_insertion_point(message_implements:proto.quota.v1.DescribeQuotaRequest)
      DescribeQuotaRequestOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use DescribeQuotaRequest.newBuilder() to construct.
    private DescribeQuotaRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }

    private DescribeQuotaRequest() {
      id_ = "";
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
      return new DescribeQuotaRequest();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_DescribeQuotaRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_DescribeQuotaRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .DescribeQuotaRequest.class,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .DescribeQuotaRequest.Builder.class);
    }

    public static final int ID_FIELD_NUMBER = 1;

    @SuppressWarnings("serial")
    private volatile java.lang.Object id_ = "";
    /**
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The id.
     */
    @java.lang.Override
    public java.lang.String getId() {
      java.lang.Object ref = id_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        id_ = s;
        return s;
      }
    }
    /**
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The bytes for id.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getIdBytes() {
      java.lang.Object ref = id_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
        id_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;

    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, id_);
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, id_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaRequest)) {
        return super.equals(obj);
      }
      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
          other =
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DescribeQuotaRequest)
                  obj;

      if (!getId().equals(other.getId())) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + getId().hashCode();
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(
            java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseDelimitedFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
            prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     *
     *
     * <pre>
     * DescribeQuotaResponse defines the request to describe a quota entry.
     * </pre>
     *
     * Protobuf type {@code proto.quota.v1.DescribeQuotaRequest}
     */
    public static final class Builder
        extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
        implements
        // @@protoc_insertion_point(builder_implements:proto.quota.v1.DescribeQuotaRequest)
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DescribeQuotaRequest_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DescribeQuotaRequest_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaRequest.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaRequest.Builder.class);
      }

      // Construct using
      // org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest.newBuilder()
      private Builder() {}

      private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
      }

      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        id_ = "";
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DescribeQuotaRequest_descriptor;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
          getDefaultInstanceForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest.getDefaultInstance();
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
          build() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
            result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
          buildPartial() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
            result =
                new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaRequest(this);
        if (bitField0_ != 0) {
          buildPartial0(result);
        }
        onBuilt();
        return result;
      }

      private void buildPartial0(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
              result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.id_ = id_;
        }
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }

      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.setField(field, value);
      }

      @java.lang.Override
      public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }

      @java.lang.Override
      public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }

      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index,
          java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }

      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other
            instanceof
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .DescribeQuotaRequest) {
          return mergeFrom(
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DescribeQuotaRequest)
                  other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
              other) {
        if (other
            == org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .DescribeQuotaRequest.getDefaultInstance()) return this;
        if (!other.getId().isEmpty()) {
          id_ = other.id_;
          bitField0_ |= 0x00000001;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10:
                {
                  id_ = input.readStringRequireUtf8();
                  bitField0_ |= 0x00000001;
                  break;
                } // case 10
              default:
                {
                  if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                    done = true; // was an endgroup tag
                  }
                  break;
                } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }

      private int bitField0_;

      private java.lang.Object id_ = "";
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @return The id.
       */
      public java.lang.String getId() {
        java.lang.Object ref = id_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          id_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @return The bytes for id.
       */
      public com.google.protobuf.ByteString getIdBytes() {
        java.lang.Object ref = id_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
          id_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @param value The id to set.
       * @return This builder for chaining.
       */
      public Builder setId(java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }
        id_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @return This builder for chaining.
       */
      public Builder clearId() {
        id_ = getDefaultInstance().getId();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @param value The bytes for id to set.
       * @return This builder for chaining.
       */
      public Builder setIdBytes(com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        checkByteStringIsUtf8(value);
        id_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }

      // @@protoc_insertion_point(builder_scope:proto.quota.v1.DescribeQuotaRequest)
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.DescribeQuotaRequest)
    private static final org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        DEFAULT_INSTANCE;

    static {
      DEFAULT_INSTANCE =
          new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaRequest();
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaRequest
        getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<DescribeQuotaRequest> PARSER =
        new com.google.protobuf.AbstractParser<DescribeQuotaRequest>() {
          @java.lang.Override
          public DescribeQuotaRequest parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
              builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
              throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
              throw e.asInvalidProtocolBufferException()
                  .setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
              throw new com.google.protobuf.InvalidProtocolBufferException(e)
                  .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
          }
        };

    public static com.google.protobuf.Parser<DescribeQuotaRequest> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<DescribeQuotaRequest> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
        getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }

  public interface DescribeQuotaResponseOrBuilder
      extends
      // @@protoc_insertion_point(interface_extends:proto.quota.v1.DescribeQuotaResponse)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return Whether the quota field is set.
     */
    boolean hasQuota();
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return The quota.
     */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota();
    /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getQuotaOrBuilder();
  }
  /**
   *
   *
   * <pre>
   * DescribeQuotaResponse defines the response to a DescribeQuota fulfillment.
   * </pre>
   *
   * Protobuf type {@code proto.quota.v1.DescribeQuotaResponse}
   */
  public static final class DescribeQuotaResponse extends com.google.protobuf.GeneratedMessageV3
      implements
      // @@protoc_insertion_point(message_implements:proto.quota.v1.DescribeQuotaResponse)
      DescribeQuotaResponseOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use DescribeQuotaResponse.newBuilder() to construct.
    private DescribeQuotaResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }

    private DescribeQuotaResponse() {}

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
      return new DescribeQuotaResponse();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_DescribeQuotaResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_DescribeQuotaResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .DescribeQuotaResponse.class,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .DescribeQuotaResponse.Builder.class);
    }

    public static final int QUOTA_FIELD_NUMBER = 1;
    private org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota quota_;
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return Whether the quota field is set.
     */
    @java.lang.Override
    public boolean hasQuota() {
      return quota_ != null;
    }
    /**
     * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
     *
     * @return The quota.
     */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota() {
      return quota_ == null
          ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
              .getDefaultInstance()
          : quota_;
    }
    /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
        getQuotaOrBuilder() {
      return quota_ == null
          ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
              .getDefaultInstance()
          : quota_;
    }

    private byte memoizedIsInitialized = -1;

    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      if (quota_ != null) {
        output.writeMessage(1, getQuota());
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (quota_ != null) {
        size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getQuota());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaResponse)) {
        return super.equals(obj);
      }
      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse
          other =
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DescribeQuotaResponse)
                  obj;

      if (hasQuota() != other.hasQuota()) return false;
      if (hasQuota()) {
        if (!getQuota().equals(other.getQuota())) return false;
      }
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasQuota()) {
        hash = (37 * hash) + QUOTA_FIELD_NUMBER;
        hash = (53 * hash) + getQuota().hashCode();
      }
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(
            java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseDelimitedFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse
            prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     *
     *
     * <pre>
     * DescribeQuotaResponse defines the response to a DescribeQuota fulfillment.
     * </pre>
     *
     * Protobuf type {@code proto.quota.v1.DescribeQuotaResponse}
     */
    public static final class Builder
        extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
        implements
        // @@protoc_insertion_point(builder_implements:proto.quota.v1.DescribeQuotaResponse)
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DescribeQuotaResponse_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DescribeQuotaResponse_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaResponse.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaResponse.Builder.class);
      }

      // Construct using
      // org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse.newBuilder()
      private Builder() {}

      private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
      }

      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        quota_ = null;
        if (quotaBuilder_ != null) {
          quotaBuilder_.dispose();
          quotaBuilder_ = null;
        }
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_DescribeQuotaResponse_descriptor;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaResponse
          getDefaultInstanceForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse.getDefaultInstance();
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaResponse
          build() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse
            result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaResponse
          buildPartial() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse
            result =
                new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaResponse(this);
        if (bitField0_ != 0) {
          buildPartial0(result);
        }
        onBuilt();
        return result;
      }

      private void buildPartial0(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse
              result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.quota_ = quotaBuilder_ == null ? quota_ : quotaBuilder_.build();
        }
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }

      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.setField(field, value);
      }

      @java.lang.Override
      public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }

      @java.lang.Override
      public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }

      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index,
          java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }

      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other
            instanceof
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .DescribeQuotaResponse) {
          return mergeFrom(
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DescribeQuotaResponse)
                  other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse
              other) {
        if (other
            == org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .DescribeQuotaResponse.getDefaultInstance()) return this;
        if (other.hasQuota()) {
          mergeQuota(other.getQuota());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10:
                {
                  input.readMessage(getQuotaFieldBuilder().getBuilder(), extensionRegistry);
                  bitField0_ |= 0x00000001;
                  break;
                } // case 10
              default:
                {
                  if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                    done = true; // was an endgroup tag
                  }
                  break;
                } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }

      private int bitField0_;

      private org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota quota_;
      private com.google.protobuf.SingleFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          quotaBuilder_;
      /**
       * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
       *
       * @return Whether the quota field is set.
       */
      public boolean hasQuota() {
        return ((bitField0_ & 0x00000001) != 0);
      }
      /**
       * <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code>
       *
       * @return The quota.
       */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota getQuota() {
        if (quotaBuilder_ == null) {
          return quota_ == null
              ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                  .getDefaultInstance()
              : quota_;
        } else {
          return quotaBuilder_.getMessage();
        }
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder setQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (quotaBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          quota_ = value;
        } else {
          quotaBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder setQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
              builderForValue) {
        if (quotaBuilder_ == null) {
          quota_ = builderForValue.build();
        } else {
          quotaBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder mergeQuota(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota value) {
        if (quotaBuilder_ == null) {
          if (((bitField0_ & 0x00000001) != 0)
              && quota_ != null
              && quota_
                  != org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                      .getDefaultInstance()) {
            getQuotaBuilder().mergeFrom(value);
          } else {
            quota_ = value;
          }
        } else {
          quotaBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public Builder clearQuota() {
        bitField0_ = (bitField0_ & ~0x00000001);
        quota_ = null;
        if (quotaBuilder_ != null) {
          quotaBuilder_.dispose();
          quotaBuilder_ = null;
        }
        onChanged();
        return this;
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
          getQuotaBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getQuotaFieldBuilder().getBuilder();
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder
          getQuotaOrBuilder() {
        if (quotaBuilder_ != null) {
          return quotaBuilder_.getMessageOrBuilder();
        } else {
          return quota_ == null
              ? org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                  .getDefaultInstance()
              : quota_;
        }
      }
      /** <code>.proto.quota.v1.Quota quota = 1 [json_name = "quota"];</code> */
      private com.google.protobuf.SingleFieldBuilderV3<
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder>
          getQuotaFieldBuilder() {
        if (quotaBuilder_ == null) {
          quotaBuilder_ =
              new com.google.protobuf.SingleFieldBuilderV3<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder,
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .QuotaOrBuilder>(getQuota(), getParentForChildren(), isClean());
          quota_ = null;
        }
        return quotaBuilder_;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }

      // @@protoc_insertion_point(builder_scope:proto.quota.v1.DescribeQuotaResponse)
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.DescribeQuotaResponse)
    private static final org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        DEFAULT_INSTANCE;

    static {
      DEFAULT_INSTANCE =
          new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaResponse();
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .DescribeQuotaResponse
        getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<DescribeQuotaResponse> PARSER =
        new com.google.protobuf.AbstractParser<DescribeQuotaResponse>() {
          @java.lang.Override
          public DescribeQuotaResponse parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
              builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
              throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
              throw e.asInvalidProtocolBufferException()
                  .setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
              throw new com.google.protobuf.InvalidProtocolBufferException(e)
                  .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
          }
        };

    public static com.google.protobuf.Parser<DescribeQuotaResponse> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<DescribeQuotaResponse> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse
        getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }

  public interface QuotaOrBuilder
      extends
      // @@protoc_insertion_point(interface_extends:proto.quota.v1.Quota)
      com.google.protobuf.MessageOrBuilder {

    /**
     *
     *
     * <pre>
     * id assigns a unique identifier to the quota entry.
     * </pre>
     *
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The id.
     */
    java.lang.String getId();
    /**
     *
     *
     * <pre>
     * id assigns a unique identifier to the quota entry.
     * </pre>
     *
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The bytes for id.
     */
    com.google.protobuf.ByteString getIdBytes();

    /**
     *
     *
     * <pre>
     * size assigns the value of the quota entry.
     * </pre>
     *
     * <code>int64 size = 2 [json_name = "size"];</code>
     *
     * @return The size.
     */
    long getSize();

    /**
     *
     *
     * <pre>
     * refreshMillisecondsInterval configures how often the quota service
     * should refresh the quota's value back to the assigned size.
     * </pre>
     *
     * <code>int64 refreshMillisecondsInterval = 3 [json_name = "refreshMillisecondsInterval"];
     * </code>
     *
     * @return The refreshMillisecondsInterval.
     */
    long getRefreshMillisecondsInterval();
  }
  /**
   *
   *
   * <pre>
   * Quota defines details of a quota.
   * </pre>
   *
   * Protobuf type {@code proto.quota.v1.Quota}
   */
  public static final class Quota extends com.google.protobuf.GeneratedMessageV3
      implements
      // @@protoc_insertion_point(message_implements:proto.quota.v1.Quota)
      QuotaOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use Quota.newBuilder() to construct.
    private Quota(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }

    private Quota() {
      id_ = "";
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
      return new Quota();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_Quota_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
          .internal_static_proto_quota_v1_Quota_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.class,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
                  .class);
    }

    public static final int ID_FIELD_NUMBER = 1;

    @SuppressWarnings("serial")
    private volatile java.lang.Object id_ = "";
    /**
     *
     *
     * <pre>
     * id assigns a unique identifier to the quota entry.
     * </pre>
     *
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The id.
     */
    @java.lang.Override
    public java.lang.String getId() {
      java.lang.Object ref = id_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        id_ = s;
        return s;
      }
    }
    /**
     *
     *
     * <pre>
     * id assigns a unique identifier to the quota entry.
     * </pre>
     *
     * <code>string id = 1 [json_name = "id"];</code>
     *
     * @return The bytes for id.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getIdBytes() {
      java.lang.Object ref = id_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
        id_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int SIZE_FIELD_NUMBER = 2;
    private long size_ = 0L;
    /**
     *
     *
     * <pre>
     * size assigns the value of the quota entry.
     * </pre>
     *
     * <code>int64 size = 2 [json_name = "size"];</code>
     *
     * @return The size.
     */
    @java.lang.Override
    public long getSize() {
      return size_;
    }

    public static final int REFRESHMILLISECONDSINTERVAL_FIELD_NUMBER = 3;
    private long refreshMillisecondsInterval_ = 0L;
    /**
     *
     *
     * <pre>
     * refreshMillisecondsInterval configures how often the quota service
     * should refresh the quota's value back to the assigned size.
     * </pre>
     *
     * <code>int64 refreshMillisecondsInterval = 3 [json_name = "refreshMillisecondsInterval"];
     * </code>
     *
     * @return The refreshMillisecondsInterval.
     */
    @java.lang.Override
    public long getRefreshMillisecondsInterval() {
      return refreshMillisecondsInterval_;
    }

    private byte memoizedIsInitialized = -1;

    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, id_);
      }
      if (size_ != 0L) {
        output.writeInt64(2, size_);
      }
      if (refreshMillisecondsInterval_ != 0L) {
        output.writeInt64(3, refreshMillisecondsInterval_);
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, id_);
      }
      if (size_ != 0L) {
        size += com.google.protobuf.CodedOutputStream.computeInt64Size(2, size_);
      }
      if (refreshMillisecondsInterval_ != 0L) {
        size +=
            com.google.protobuf.CodedOutputStream.computeInt64Size(3, refreshMillisecondsInterval_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj
          instanceof org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota)) {
        return super.equals(obj);
      }
      org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota other =
          (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota) obj;

      if (!getId().equals(other.getId())) return false;
      if (getSize() != other.getSize()) return false;
      if (getRefreshMillisecondsInterval() != other.getRefreshMillisecondsInterval()) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + getId().hashCode();
      hash = (37 * hash) + SIZE_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(getSize());
      hash = (37 * hash) + REFRESHMILLISECONDSINTERVAL_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(getRefreshMillisecondsInterval());
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(
            java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseDelimitedFrom(
            java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
          PARSER, input, extensionRegistry);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
          PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     *
     *
     * <pre>
     * Quota defines details of a quota.
     * </pre>
     *
     * Protobuf type {@code proto.quota.v1.Quota}
     */
    public static final class Builder
        extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
        implements
        // @@protoc_insertion_point(builder_implements:proto.quota.v1.Quota)
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_Quota_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_Quota_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.Builder
                    .class);
      }

      // Construct using
      // org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota.newBuilder()
      private Builder() {}

      private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
      }

      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        id_ = "";
        size_ = 0L;
        refreshMillisecondsInterval_ = 0L;
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
            .internal_static_proto_quota_v1_Quota_descriptor;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
          getDefaultInstanceForType() {
        return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
            .getDefaultInstance();
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota build() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota result =
            buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
          buildPartial() {
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota result =
            new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota(this);
        if (bitField0_ != 0) {
          buildPartial0(result);
        }
        onBuilt();
        return result;
      }

      private void buildPartial0(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.id_ = id_;
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.size_ = size_;
        }
        if (((from_bitField0_ & 0x00000004) != 0)) {
          result.refreshMillisecondsInterval_ = refreshMillisecondsInterval_;
        }
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }

      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.setField(field, value);
      }

      @java.lang.Override
      public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }

      @java.lang.Override
      public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }

      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index,
          java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }

      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other
            instanceof org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota) {
          return mergeFrom(
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota other) {
        if (other
            == org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
                .getDefaultInstance()) return this;
        if (!other.getId().isEmpty()) {
          id_ = other.id_;
          bitField0_ |= 0x00000001;
          onChanged();
        }
        if (other.getSize() != 0L) {
          setSize(other.getSize());
        }
        if (other.getRefreshMillisecondsInterval() != 0L) {
          setRefreshMillisecondsInterval(other.getRefreshMillisecondsInterval());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10:
                {
                  id_ = input.readStringRequireUtf8();
                  bitField0_ |= 0x00000001;
                  break;
                } // case 10
              case 16:
                {
                  size_ = input.readInt64();
                  bitField0_ |= 0x00000002;
                  break;
                } // case 16
              case 24:
                {
                  refreshMillisecondsInterval_ = input.readInt64();
                  bitField0_ |= 0x00000004;
                  break;
                } // case 24
              default:
                {
                  if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                    done = true; // was an endgroup tag
                  }
                  break;
                } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }

      private int bitField0_;

      private java.lang.Object id_ = "";
      /**
       *
       *
       * <pre>
       * id assigns a unique identifier to the quota entry.
       * </pre>
       *
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @return The id.
       */
      public java.lang.String getId() {
        java.lang.Object ref = id_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          id_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       *
       *
       * <pre>
       * id assigns a unique identifier to the quota entry.
       * </pre>
       *
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @return The bytes for id.
       */
      public com.google.protobuf.ByteString getIdBytes() {
        java.lang.Object ref = id_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
          id_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       *
       *
       * <pre>
       * id assigns a unique identifier to the quota entry.
       * </pre>
       *
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @param value The id to set.
       * @return This builder for chaining.
       */
      public Builder setId(java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }
        id_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       *
       *
       * <pre>
       * id assigns a unique identifier to the quota entry.
       * </pre>
       *
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @return This builder for chaining.
       */
      public Builder clearId() {
        id_ = getDefaultInstance().getId();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       *
       *
       * <pre>
       * id assigns a unique identifier to the quota entry.
       * </pre>
       *
       * <code>string id = 1 [json_name = "id"];</code>
       *
       * @param value The bytes for id to set.
       * @return This builder for chaining.
       */
      public Builder setIdBytes(com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        checkByteStringIsUtf8(value);
        id_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }

      private long size_;
      /**
       *
       *
       * <pre>
       * size assigns the value of the quota entry.
       * </pre>
       *
       * <code>int64 size = 2 [json_name = "size"];</code>
       *
       * @return The size.
       */
      @java.lang.Override
      public long getSize() {
        return size_;
      }
      /**
       *
       *
       * <pre>
       * size assigns the value of the quota entry.
       * </pre>
       *
       * <code>int64 size = 2 [json_name = "size"];</code>
       *
       * @param value The size to set.
       * @return This builder for chaining.
       */
      public Builder setSize(long value) {

        size_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       *
       *
       * <pre>
       * size assigns the value of the quota entry.
       * </pre>
       *
       * <code>int64 size = 2 [json_name = "size"];</code>
       *
       * @return This builder for chaining.
       */
      public Builder clearSize() {
        bitField0_ = (bitField0_ & ~0x00000002);
        size_ = 0L;
        onChanged();
        return this;
      }

      private long refreshMillisecondsInterval_;
      /**
       *
       *
       * <pre>
       * refreshMillisecondsInterval configures how often the quota service
       * should refresh the quota's value back to the assigned size.
       * </pre>
       *
       * <code>int64 refreshMillisecondsInterval = 3 [json_name = "refreshMillisecondsInterval"];
       * </code>
       *
       * @return The refreshMillisecondsInterval.
       */
      @java.lang.Override
      public long getRefreshMillisecondsInterval() {
        return refreshMillisecondsInterval_;
      }
      /**
       *
       *
       * <pre>
       * refreshMillisecondsInterval configures how often the quota service
       * should refresh the quota's value back to the assigned size.
       * </pre>
       *
       * <code>int64 refreshMillisecondsInterval = 3 [json_name = "refreshMillisecondsInterval"];
       * </code>
       *
       * @param value The refreshMillisecondsInterval to set.
       * @return This builder for chaining.
       */
      public Builder setRefreshMillisecondsInterval(long value) {

        refreshMillisecondsInterval_ = value;
        bitField0_ |= 0x00000004;
        onChanged();
        return this;
      }
      /**
       *
       *
       * <pre>
       * refreshMillisecondsInterval configures how often the quota service
       * should refresh the quota's value back to the assigned size.
       * </pre>
       *
       * <code>int64 refreshMillisecondsInterval = 3 [json_name = "refreshMillisecondsInterval"];
       * </code>
       *
       * @return This builder for chaining.
       */
      public Builder clearRefreshMillisecondsInterval() {
        bitField0_ = (bitField0_ & ~0x00000004);
        refreshMillisecondsInterval_ = 0L;
        onChanged();
        return this;
      }

      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }

      // @@protoc_insertion_point(builder_scope:proto.quota.v1.Quota)
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.Quota)
    private static final org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        DEFAULT_INSTANCE;

    static {
      DEFAULT_INSTANCE =
          new org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota();
    }

    public static org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Quota> PARSER =
        new com.google.protobuf.AbstractParser<Quota>() {
          @java.lang.Override
          public Quota parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
              builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
              throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
              throw e.asInvalidProtocolBufferException()
                  .setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
              throw new com.google.protobuf.InvalidProtocolBufferException(e)
                  .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
          }
        };

    public static com.google.protobuf.Parser<Quota> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Quota> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota
        getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }

  /**
   *
   *
   * <pre>
   * A QuotaService manages the internal cached quota state of the application.
   * </pre>
   *
   * Protobuf service {@code proto.quota.v1.QuotaService}
   */
  public abstract static class QuotaService implements com.google.protobuf.Service {
    protected QuotaService() {}

    public interface Interface {
      /**
       *
       *
       * <pre>
       * Create a new quota entry.
       * </pre>
       *
       * <code>
       * rpc Create(.proto.quota.v1.CreateQuotaRequest) returns (.proto.quota.v1.CreateQuotaResponse);
       * </code>
       */
      public abstract void create(
          com.google.protobuf.RpcController controller,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
              request,
          com.google.protobuf.RpcCallback<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .CreateQuotaResponse>
              done);

      /**
       *
       *
       * <pre>
       * List available quota entries.
       * </pre>
       *
       * <code>
       * rpc List(.proto.quota.v1.ListQuotasRequest) returns (.proto.quota.v1.ListQuotasResponse);
       * </code>
       */
      public abstract void list(
          com.google.protobuf.RpcController controller,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
              request,
          com.google.protobuf.RpcCallback<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .ListQuotasResponse>
              done);

      /**
       *
       *
       * <pre>
       * Delete a quota entry.
       * </pre>
       *
       * <code>
       * rpc Delete(.proto.quota.v1.DeleteQuotaRequest) returns (.proto.quota.v1.DeleteQuotaResponse);
       * </code>
       */
      public abstract void delete(
          com.google.protobuf.RpcController controller,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
              request,
          com.google.protobuf.RpcCallback<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DeleteQuotaResponse>
              done);

      /**
       *
       *
       * <pre>
       * Describe a quota entry.
       * </pre>
       *
       * <code>
       * rpc Describe(.proto.quota.v1.DescribeQuotaRequest) returns (.proto.quota.v1.DescribeQuotaResponse);
       * </code>
       */
      public abstract void describe(
          com.google.protobuf.RpcController controller,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
              request,
          com.google.protobuf.RpcCallback<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DescribeQuotaResponse>
              done);
    }

    public static com.google.protobuf.Service newReflectiveService(final Interface impl) {
      return new QuotaService() {
        @java.lang.Override
        public void create(
            com.google.protobuf.RpcController controller,
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
                request,
            com.google.protobuf.RpcCallback<
                    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                        .CreateQuotaResponse>
                done) {
          impl.create(controller, request, done);
        }

        @java.lang.Override
        public void list(
            com.google.protobuf.RpcController controller,
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
                request,
            com.google.protobuf.RpcCallback<
                    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                        .ListQuotasResponse>
                done) {
          impl.list(controller, request, done);
        }

        @java.lang.Override
        public void delete(
            com.google.protobuf.RpcController controller,
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
                request,
            com.google.protobuf.RpcCallback<
                    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                        .DeleteQuotaResponse>
                done) {
          impl.delete(controller, request, done);
        }

        @java.lang.Override
        public void describe(
            com.google.protobuf.RpcController controller,
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
                request,
            com.google.protobuf.RpcCallback<
                    org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                        .DescribeQuotaResponse>
                done) {
          impl.describe(controller, request, done);
        }
      };
    }

    public static com.google.protobuf.BlockingService newReflectiveBlockingService(
        final BlockingInterface impl) {
      return new com.google.protobuf.BlockingService() {
        public final com.google.protobuf.Descriptors.ServiceDescriptor getDescriptorForType() {
          return getDescriptor();
        }

        public final com.google.protobuf.Message callBlockingMethod(
            com.google.protobuf.Descriptors.MethodDescriptor method,
            com.google.protobuf.RpcController controller,
            com.google.protobuf.Message request)
            throws com.google.protobuf.ServiceException {
          if (method.getService() != getDescriptor()) {
            throw new java.lang.IllegalArgumentException(
                "Service.callBlockingMethod() given method descriptor for "
                    + "wrong service type.");
          }
          switch (method.getIndex()) {
            case 0:
              return impl.create(
                  controller,
                  (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                          .CreateQuotaRequest)
                      request);
            case 1:
              return impl.list(
                  controller,
                  (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                          .ListQuotasRequest)
                      request);
            case 2:
              return impl.delete(
                  controller,
                  (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                          .DeleteQuotaRequest)
                      request);
            case 3:
              return impl.describe(
                  controller,
                  (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                          .DescribeQuotaRequest)
                      request);
            default:
              throw new java.lang.AssertionError("Can't get here.");
          }
        }

        public final com.google.protobuf.Message getRequestPrototype(
            com.google.protobuf.Descriptors.MethodDescriptor method) {
          if (method.getService() != getDescriptor()) {
            throw new java.lang.IllegalArgumentException(
                "Service.getRequestPrototype() given method "
                    + "descriptor for wrong service type.");
          }
          switch (method.getIndex()) {
            case 0:
              return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .CreateQuotaRequest.getDefaultInstance();
            case 1:
              return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .ListQuotasRequest.getDefaultInstance();
            case 2:
              return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .DeleteQuotaRequest.getDefaultInstance();
            case 3:
              return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .DescribeQuotaRequest.getDefaultInstance();
            default:
              throw new java.lang.AssertionError("Can't get here.");
          }
        }

        public final com.google.protobuf.Message getResponsePrototype(
            com.google.protobuf.Descriptors.MethodDescriptor method) {
          if (method.getService() != getDescriptor()) {
            throw new java.lang.IllegalArgumentException(
                "Service.getResponsePrototype() given method "
                    + "descriptor for wrong service type.");
          }
          switch (method.getIndex()) {
            case 0:
              return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .CreateQuotaResponse.getDefaultInstance();
            case 1:
              return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .ListQuotasResponse.getDefaultInstance();
            case 2:
              return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .DeleteQuotaResponse.getDefaultInstance();
            case 3:
              return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                  .DescribeQuotaResponse.getDefaultInstance();
            default:
              throw new java.lang.AssertionError("Can't get here.");
          }
        }
      };
    }

    /**
     *
     *
     * <pre>
     * Create a new quota entry.
     * </pre>
     *
     * <code>
     * rpc Create(.proto.quota.v1.CreateQuotaRequest) returns (.proto.quota.v1.CreateQuotaResponse);
     * </code>
     */
    public abstract void create(
        com.google.protobuf.RpcController controller,
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
            request,
        com.google.protobuf.RpcCallback<
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaResponse>
            done);

    /**
     *
     *
     * <pre>
     * List available quota entries.
     * </pre>
     *
     * <code>
     * rpc List(.proto.quota.v1.ListQuotasRequest) returns (.proto.quota.v1.ListQuotasResponse);
     * </code>
     */
    public abstract void list(
        com.google.protobuf.RpcController controller,
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
            request,
        com.google.protobuf.RpcCallback<
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .ListQuotasResponse>
            done);

    /**
     *
     *
     * <pre>
     * Delete a quota entry.
     * </pre>
     *
     * <code>
     * rpc Delete(.proto.quota.v1.DeleteQuotaRequest) returns (.proto.quota.v1.DeleteQuotaResponse);
     * </code>
     */
    public abstract void delete(
        com.google.protobuf.RpcController controller,
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
            request,
        com.google.protobuf.RpcCallback<
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaResponse>
            done);

    /**
     *
     *
     * <pre>
     * Describe a quota entry.
     * </pre>
     *
     * <code>
     * rpc Describe(.proto.quota.v1.DescribeQuotaRequest) returns (.proto.quota.v1.DescribeQuotaResponse);
     * </code>
     */
    public abstract void describe(
        com.google.protobuf.RpcController controller,
        org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
            request,
        com.google.protobuf.RpcCallback<
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaResponse>
            done);

    public static final com.google.protobuf.Descriptors.ServiceDescriptor getDescriptor() {
      return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.getDescriptor()
          .getServices()
          .get(0);
    }

    public final com.google.protobuf.Descriptors.ServiceDescriptor getDescriptorForType() {
      return getDescriptor();
    }

    public final void callMethod(
        com.google.protobuf.Descriptors.MethodDescriptor method,
        com.google.protobuf.RpcController controller,
        com.google.protobuf.Message request,
        com.google.protobuf.RpcCallback<com.google.protobuf.Message> done) {
      if (method.getService() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
            "Service.callMethod() given method descriptor for wrong " + "service type.");
      }
      switch (method.getIndex()) {
        case 0:
          this.create(
              controller,
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .CreateQuotaRequest)
                  request,
              com.google.protobuf.RpcUtil
                  .<org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                          .CreateQuotaResponse>
                      specializeCallback(done));
          return;
        case 1:
          this.list(
              controller,
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest)
                  request,
              com.google.protobuf.RpcUtil
                  .<org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                          .ListQuotasResponse>
                      specializeCallback(done));
          return;
        case 2:
          this.delete(
              controller,
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DeleteQuotaRequest)
                  request,
              com.google.protobuf.RpcUtil
                  .<org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                          .DeleteQuotaResponse>
                      specializeCallback(done));
          return;
        case 3:
          this.describe(
              controller,
              (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DescribeQuotaRequest)
                  request,
              com.google.protobuf.RpcUtil
                  .<org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                          .DescribeQuotaResponse>
                      specializeCallback(done));
          return;
        default:
          throw new java.lang.AssertionError("Can't get here.");
      }
    }

    public final com.google.protobuf.Message getRequestPrototype(
        com.google.protobuf.Descriptors.MethodDescriptor method) {
      if (method.getService() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
            "Service.getRequestPrototype() given method " + "descriptor for wrong service type.");
      }
      switch (method.getIndex()) {
        case 0:
          return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .CreateQuotaRequest.getDefaultInstance();
        case 1:
          return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .ListQuotasRequest.getDefaultInstance();
        case 2:
          return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DeleteQuotaRequest.getDefaultInstance();
        case 3:
          return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaRequest.getDefaultInstance();
        default:
          throw new java.lang.AssertionError("Can't get here.");
      }
    }

    public final com.google.protobuf.Message getResponsePrototype(
        com.google.protobuf.Descriptors.MethodDescriptor method) {
      if (method.getService() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
            "Service.getResponsePrototype() given method " + "descriptor for wrong service type.");
      }
      switch (method.getIndex()) {
        case 0:
          return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .CreateQuotaResponse.getDefaultInstance();
        case 1:
          return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .ListQuotasResponse.getDefaultInstance();
        case 2:
          return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DeleteQuotaResponse.getDefaultInstance();
        case 3:
          return org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaResponse.getDefaultInstance();
        default:
          throw new java.lang.AssertionError("Can't get here.");
      }
    }

    public static Stub newStub(com.google.protobuf.RpcChannel channel) {
      return new Stub(channel);
    }

    public static final class Stub
        extends org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.QuotaService
        implements Interface {
      private Stub(com.google.protobuf.RpcChannel channel) {
        this.channel = channel;
      }

      private final com.google.protobuf.RpcChannel channel;

      public com.google.protobuf.RpcChannel getChannel() {
        return channel;
      }

      public void create(
          com.google.protobuf.RpcController controller,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
              request,
          com.google.protobuf.RpcCallback<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .CreateQuotaResponse>
              done) {
        channel.callMethod(
            getDescriptor().getMethods().get(0),
            controller,
            request,
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
                .getDefaultInstance(),
            com.google.protobuf.RpcUtil.generalizeCallback(
                done,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaResponse.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaResponse.getDefaultInstance()));
      }

      public void list(
          com.google.protobuf.RpcController controller,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
              request,
          com.google.protobuf.RpcCallback<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .ListQuotasResponse>
              done) {
        channel.callMethod(
            getDescriptor().getMethods().get(1),
            controller,
            request,
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
                .getDefaultInstance(),
            com.google.protobuf.RpcUtil.generalizeCallback(
                done,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .ListQuotasResponse.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .ListQuotasResponse.getDefaultInstance()));
      }

      public void delete(
          com.google.protobuf.RpcController controller,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
              request,
          com.google.protobuf.RpcCallback<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DeleteQuotaResponse>
              done) {
        channel.callMethod(
            getDescriptor().getMethods().get(2),
            controller,
            request,
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
                .getDefaultInstance(),
            com.google.protobuf.RpcUtil.generalizeCallback(
                done,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaResponse.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaResponse.getDefaultInstance()));
      }

      public void describe(
          com.google.protobuf.RpcController controller,
          org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest
              request,
          com.google.protobuf.RpcCallback<
                  org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DescribeQuotaResponse>
              done) {
        channel.callMethod(
            getDescriptor().getMethods().get(3),
            controller,
            request,
            org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse
                .getDefaultInstance(),
            com.google.protobuf.RpcUtil.generalizeCallback(
                done,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaResponse.class,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaResponse.getDefaultInstance()));
      }
    }

    public static BlockingInterface newBlockingStub(
        com.google.protobuf.BlockingRpcChannel channel) {
      return new BlockingStub(channel);
    }

    public interface BlockingInterface {
      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
          create(
              com.google.protobuf.RpcController controller,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
                  request)
              throws com.google.protobuf.ServiceException;

      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
          list(
              com.google.protobuf.RpcController controller,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
                  request)
              throws com.google.protobuf.ServiceException;

      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
          delete(
              com.google.protobuf.RpcController controller,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
                  request)
              throws com.google.protobuf.ServiceException;

      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaResponse
          describe(
              com.google.protobuf.RpcController controller,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DescribeQuotaRequest
                  request)
              throws com.google.protobuf.ServiceException;
    }

    private static final class BlockingStub implements BlockingInterface {
      private BlockingStub(com.google.protobuf.BlockingRpcChannel channel) {
        this.channel = channel;
      }

      private final com.google.protobuf.BlockingRpcChannel channel;

      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse
          create(
              com.google.protobuf.RpcController controller,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest
                  request)
              throws com.google.protobuf.ServiceException {
        return (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .CreateQuotaResponse)
            channel.callBlockingMethod(
                getDescriptor().getMethods().get(0),
                controller,
                request,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .CreateQuotaResponse.getDefaultInstance());
      }

      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse
          list(
              com.google.protobuf.RpcController controller,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest
                  request)
              throws com.google.protobuf.ServiceException {
        return (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .ListQuotasResponse)
            channel.callBlockingMethod(
                getDescriptor().getMethods().get(1),
                controller,
                request,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .ListQuotasResponse.getDefaultInstance());
      }

      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse
          delete(
              com.google.protobuf.RpcController controller,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest
                  request)
              throws com.google.protobuf.ServiceException {
        return (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .DeleteQuotaResponse)
            channel.callBlockingMethod(
                getDescriptor().getMethods().get(2),
                controller,
                request,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DeleteQuotaResponse.getDefaultInstance());
      }

      public org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
              .DescribeQuotaResponse
          describe(
              com.google.protobuf.RpcController controller,
              org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                      .DescribeQuotaRequest
                  request)
              throws com.google.protobuf.ServiceException {
        return (org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                .DescribeQuotaResponse)
            channel.callBlockingMethod(
                getDescriptor().getMethods().get(3),
                controller,
                request,
                org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass
                    .DescribeQuotaResponse.getDefaultInstance());
      }
    }

    // @@protoc_insertion_point(class_scope:proto.quota.v1.QuotaService)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
      internal_static_proto_quota_v1_CreateQuotaRequest_descriptor;
  private static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_quota_v1_CreateQuotaRequest_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
      internal_static_proto_quota_v1_CreateQuotaResponse_descriptor;
  private static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_quota_v1_CreateQuotaResponse_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
      internal_static_proto_quota_v1_ListQuotasRequest_descriptor;
  private static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_quota_v1_ListQuotasRequest_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
      internal_static_proto_quota_v1_ListQuotasResponse_descriptor;
  private static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_quota_v1_ListQuotasResponse_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
      internal_static_proto_quota_v1_DeleteQuotaRequest_descriptor;
  private static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_quota_v1_DeleteQuotaRequest_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
      internal_static_proto_quota_v1_DeleteQuotaResponse_descriptor;
  private static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_quota_v1_DeleteQuotaResponse_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
      internal_static_proto_quota_v1_DescribeQuotaRequest_descriptor;
  private static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_quota_v1_DescribeQuotaRequest_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
      internal_static_proto_quota_v1_DescribeQuotaResponse_descriptor;
  private static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_quota_v1_DescribeQuotaResponse_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
      internal_static_proto_quota_v1_Quota_descriptor;
  private static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_quota_v1_Quota_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n\024proto/v1/quota.proto\022\016proto.quota.v1\"A"
          + "\n\022CreateQuotaRequest\022+\n\005quota\030\001 \001(\0132\025.pr"
          + "oto.quota.v1.QuotaR\005quota\"B\n\023CreateQuota"
          + "Response\022+\n\005quota\030\001 \001(\0132\025.proto.quota.v1"
          + ".QuotaR\005quota\"\023\n\021ListQuotasRequest\"?\n\022Li"
          + "stQuotasResponse\022)\n\004list\030\001 \003(\0132\025.proto.q"
          + "uota.v1.QuotaR\004list\"$\n\022DeleteQuotaReques"
          + "t\022\016\n\002id\030\001 \001(\tR\002id\"B\n\023DeleteQuotaResponse"
          + "\022+\n\005quota\030\001 \001(\0132\025.proto.quota.v1.QuotaR\005"
          + "quota\"&\n\024DescribeQuotaRequest\022\016\n\002id\030\001 \001("
          + "\tR\002id\"D\n\025DescribeQuotaResponse\022+\n\005quota\030"
          + "\001 \001(\0132\025.proto.quota.v1.QuotaR\005quota\"m\n\005Q"
          + "uota\022\016\n\002id\030\001 \001(\tR\002id\022\022\n\004size\030\002 \001(\003R\004size"
          + "\022@\n\033refreshMillisecondsInterval\030\003 \001(\003R\033r"
          + "efreshMillisecondsInterval2\344\002\n\014QuotaServ"
          + "ice\022S\n\006Create\022\".proto.quota.v1.CreateQuo"
          + "taRequest\032#.proto.quota.v1.CreateQuotaRe"
          + "sponse\"\000\022O\n\004List\022!.proto.quota.v1.ListQu"
          + "otasRequest\032\".proto.quota.v1.ListQuotasR"
          + "esponse\"\000\022S\n\006Delete\022\".proto.quota.v1.Del"
          + "eteQuotaRequest\032#.proto.quota.v1.DeleteQ"
          + "uotaResponse\"\000\022Y\n\010Describe\022$.proto.quota"
          + ".v1.DescribeQuotaRequest\032%.proto.quota.v"
          + "1.DescribeQuotaResponse\"\000BG\n2org.apache."
          + "beam.testinfra.pipelines.proto.quota.v1Z"
          + "\016proto/quota/v1\210\001\001b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData, new com.google.protobuf.Descriptors.FileDescriptor[] {});
    internal_static_proto_quota_v1_CreateQuotaRequest_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_proto_quota_v1_CreateQuotaRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_proto_quota_v1_CreateQuotaRequest_descriptor,
            new java.lang.String[] {
              "Quota",
            });
    internal_static_proto_quota_v1_CreateQuotaResponse_descriptor =
        getDescriptor().getMessageTypes().get(1);
    internal_static_proto_quota_v1_CreateQuotaResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_proto_quota_v1_CreateQuotaResponse_descriptor,
            new java.lang.String[] {
              "Quota",
            });
    internal_static_proto_quota_v1_ListQuotasRequest_descriptor =
        getDescriptor().getMessageTypes().get(2);
    internal_static_proto_quota_v1_ListQuotasRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_proto_quota_v1_ListQuotasRequest_descriptor, new java.lang.String[] {});
    internal_static_proto_quota_v1_ListQuotasResponse_descriptor =
        getDescriptor().getMessageTypes().get(3);
    internal_static_proto_quota_v1_ListQuotasResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_proto_quota_v1_ListQuotasResponse_descriptor,
            new java.lang.String[] {
              "List",
            });
    internal_static_proto_quota_v1_DeleteQuotaRequest_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_proto_quota_v1_DeleteQuotaRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_proto_quota_v1_DeleteQuotaRequest_descriptor,
            new java.lang.String[] {
              "Id",
            });
    internal_static_proto_quota_v1_DeleteQuotaResponse_descriptor =
        getDescriptor().getMessageTypes().get(5);
    internal_static_proto_quota_v1_DeleteQuotaResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_proto_quota_v1_DeleteQuotaResponse_descriptor,
            new java.lang.String[] {
              "Quota",
            });
    internal_static_proto_quota_v1_DescribeQuotaRequest_descriptor =
        getDescriptor().getMessageTypes().get(6);
    internal_static_proto_quota_v1_DescribeQuotaRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_proto_quota_v1_DescribeQuotaRequest_descriptor,
            new java.lang.String[] {
              "Id",
            });
    internal_static_proto_quota_v1_DescribeQuotaResponse_descriptor =
        getDescriptor().getMessageTypes().get(7);
    internal_static_proto_quota_v1_DescribeQuotaResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_proto_quota_v1_DescribeQuotaResponse_descriptor,
            new java.lang.String[] {
              "Quota",
            });
    internal_static_proto_quota_v1_Quota_descriptor = getDescriptor().getMessageTypes().get(8);
    internal_static_proto_quota_v1_Quota_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_proto_quota_v1_Quota_descriptor,
            new java.lang.String[] {
              "Id", "Size", "RefreshMillisecondsInterval",
            });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
