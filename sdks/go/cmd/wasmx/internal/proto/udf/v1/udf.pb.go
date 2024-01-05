//
// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//
// Protocol buffers describing a support for User Defined Function (UDF) management.

// Code generated by protoc-gen-go. DO NOT EDIT.
// versions:
// 	protoc-gen-go v1.28.1
// 	protoc        (unknown)
// source: proto/udf/v1/udf.proto

package v1

import (
	protoreflect "google.golang.org/protobuf/reflect/protoreflect"
	protoimpl "google.golang.org/protobuf/runtime/protoimpl"
	timestamppb "google.golang.org/protobuf/types/known/timestamppb"
	reflect "reflect"
	sync "sync"
)

const (
	// Verify that this generated code is sufficiently up-to-date.
	_ = protoimpl.EnforceVersion(20 - protoimpl.MinVersion)
	// Verify that runtime/protoimpl is sufficiently up-to-date.
	_ = protoimpl.EnforceVersion(protoimpl.MaxVersion - 20)
)

type UserDefinedFunction_Language int32

const (
	UserDefinedFunction_Language_UNSPECIFIED UserDefinedFunction_Language = 0
	UserDefinedFunction_Language_Go          UserDefinedFunction_Language = 1
	UserDefinedFunction_Language_Rust        UserDefinedFunction_Language = 2
)

// Enum value maps for UserDefinedFunction_Language.
var (
	UserDefinedFunction_Language_name = map[int32]string{
		0: "Language_UNSPECIFIED",
		1: "Language_Go",
		2: "Language_Rust",
	}
	UserDefinedFunction_Language_value = map[string]int32{
		"Language_UNSPECIFIED": 0,
		"Language_Go":          1,
		"Language_Rust":        2,
	}
)

func (x UserDefinedFunction_Language) Enum() *UserDefinedFunction_Language {
	p := new(UserDefinedFunction_Language)
	*p = x
	return p
}

func (x UserDefinedFunction_Language) String() string {
	return protoimpl.X.EnumStringOf(x.Descriptor(), protoreflect.EnumNumber(x))
}

func (UserDefinedFunction_Language) Descriptor() protoreflect.EnumDescriptor {
	return file_proto_udf_v1_udf_proto_enumTypes[0].Descriptor()
}

func (UserDefinedFunction_Language) Type() protoreflect.EnumType {
	return &file_proto_udf_v1_udf_proto_enumTypes[0]
}

func (x UserDefinedFunction_Language) Number() protoreflect.EnumNumber {
	return protoreflect.EnumNumber(x)
}

// Deprecated: Use UserDefinedFunction_Language.Descriptor instead.
func (UserDefinedFunction_Language) EnumDescriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{0, 0}
}

// Models a User Defined Function (UDF).
type UserDefinedFunction struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Urn      string                       `protobuf:"bytes,1,opt,name=urn,proto3" json:"urn,omitempty"`
	Bytes    []byte                       `protobuf:"bytes,2,opt,name=bytes,proto3" json:"bytes,omitempty"`
	Language UserDefinedFunction_Language `protobuf:"varint,3,opt,name=language,proto3,enum=UserDefinedFunction_Language" json:"language,omitempty"`
	Created  *timestamppb.Timestamp       `protobuf:"bytes,4,opt,name=created,proto3" json:"created,omitempty"`
	Updated  *timestamppb.Timestamp       `protobuf:"bytes,5,opt,name=updated,proto3" json:"updated,omitempty"`
}

func (x *UserDefinedFunction) Reset() {
	*x = UserDefinedFunction{}
	if protoimpl.UnsafeEnabled {
		mi := &file_proto_udf_v1_udf_proto_msgTypes[0]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *UserDefinedFunction) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*UserDefinedFunction) ProtoMessage() {}

func (x *UserDefinedFunction) ProtoReflect() protoreflect.Message {
	mi := &file_proto_udf_v1_udf_proto_msgTypes[0]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use UserDefinedFunction.ProtoReflect.Descriptor instead.
func (*UserDefinedFunction) Descriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{0}
}

func (x *UserDefinedFunction) GetUrn() string {
	if x != nil {
		return x.Urn
	}
	return ""
}

func (x *UserDefinedFunction) GetBytes() []byte {
	if x != nil {
		return x.Bytes
	}
	return nil
}

func (x *UserDefinedFunction) GetLanguage() UserDefinedFunction_Language {
	if x != nil {
		return x.Language
	}
	return UserDefinedFunction_Language_UNSPECIFIED
}

func (x *UserDefinedFunction) GetCreated() *timestamppb.Timestamp {
	if x != nil {
		return x.Created
	}
	return nil
}

func (x *UserDefinedFunction) GetUpdated() *timestamppb.Timestamp {
	if x != nil {
		return x.Updated
	}
	return nil
}

type DescribeRequest struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Urn string `protobuf:"bytes,1,opt,name=urn,proto3" json:"urn,omitempty"`
}

func (x *DescribeRequest) Reset() {
	*x = DescribeRequest{}
	if protoimpl.UnsafeEnabled {
		mi := &file_proto_udf_v1_udf_proto_msgTypes[1]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *DescribeRequest) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*DescribeRequest) ProtoMessage() {}

func (x *DescribeRequest) ProtoReflect() protoreflect.Message {
	mi := &file_proto_udf_v1_udf_proto_msgTypes[1]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use DescribeRequest.ProtoReflect.Descriptor instead.
func (*DescribeRequest) Descriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{1}
}

func (x *DescribeRequest) GetUrn() string {
	if x != nil {
		return x.Urn
	}
	return ""
}

type CreateRequest struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Udf *UserDefinedFunction `protobuf:"bytes,1,opt,name=udf,proto3" json:"udf,omitempty"`
}

func (x *CreateRequest) Reset() {
	*x = CreateRequest{}
	if protoimpl.UnsafeEnabled {
		mi := &file_proto_udf_v1_udf_proto_msgTypes[2]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *CreateRequest) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*CreateRequest) ProtoMessage() {}

func (x *CreateRequest) ProtoReflect() protoreflect.Message {
	mi := &file_proto_udf_v1_udf_proto_msgTypes[2]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use CreateRequest.ProtoReflect.Descriptor instead.
func (*CreateRequest) Descriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{2}
}

func (x *CreateRequest) GetUdf() *UserDefinedFunction {
	if x != nil {
		return x.Udf
	}
	return nil
}

type UpdateRequest struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Udf *UserDefinedFunction `protobuf:"bytes,1,opt,name=udf,proto3" json:"udf,omitempty"`
}

func (x *UpdateRequest) Reset() {
	*x = UpdateRequest{}
	if protoimpl.UnsafeEnabled {
		mi := &file_proto_udf_v1_udf_proto_msgTypes[3]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *UpdateRequest) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*UpdateRequest) ProtoMessage() {}

func (x *UpdateRequest) ProtoReflect() protoreflect.Message {
	mi := &file_proto_udf_v1_udf_proto_msgTypes[3]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use UpdateRequest.ProtoReflect.Descriptor instead.
func (*UpdateRequest) Descriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{3}
}

func (x *UpdateRequest) GetUdf() *UserDefinedFunction {
	if x != nil {
		return x.Udf
	}
	return nil
}

type DeleteRequest struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Urn string `protobuf:"bytes,1,opt,name=urn,proto3" json:"urn,omitempty"`
}

func (x *DeleteRequest) Reset() {
	*x = DeleteRequest{}
	if protoimpl.UnsafeEnabled {
		mi := &file_proto_udf_v1_udf_proto_msgTypes[4]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *DeleteRequest) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*DeleteRequest) ProtoMessage() {}

func (x *DeleteRequest) ProtoReflect() protoreflect.Message {
	mi := &file_proto_udf_v1_udf_proto_msgTypes[4]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use DeleteRequest.ProtoReflect.Descriptor instead.
func (*DeleteRequest) Descriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{4}
}

func (x *DeleteRequest) GetUrn() string {
	if x != nil {
		return x.Urn
	}
	return ""
}

type DescribeResponse struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Udf *UserDefinedFunction `protobuf:"bytes,1,opt,name=udf,proto3" json:"udf,omitempty"`
}

func (x *DescribeResponse) Reset() {
	*x = DescribeResponse{}
	if protoimpl.UnsafeEnabled {
		mi := &file_proto_udf_v1_udf_proto_msgTypes[5]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *DescribeResponse) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*DescribeResponse) ProtoMessage() {}

func (x *DescribeResponse) ProtoReflect() protoreflect.Message {
	mi := &file_proto_udf_v1_udf_proto_msgTypes[5]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use DescribeResponse.ProtoReflect.Descriptor instead.
func (*DescribeResponse) Descriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{5}
}

func (x *DescribeResponse) GetUdf() *UserDefinedFunction {
	if x != nil {
		return x.Udf
	}
	return nil
}

type CreateResponse struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Udf *UserDefinedFunction `protobuf:"bytes,1,opt,name=udf,proto3" json:"udf,omitempty"`
}

func (x *CreateResponse) Reset() {
	*x = CreateResponse{}
	if protoimpl.UnsafeEnabled {
		mi := &file_proto_udf_v1_udf_proto_msgTypes[6]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *CreateResponse) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*CreateResponse) ProtoMessage() {}

func (x *CreateResponse) ProtoReflect() protoreflect.Message {
	mi := &file_proto_udf_v1_udf_proto_msgTypes[6]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use CreateResponse.ProtoReflect.Descriptor instead.
func (*CreateResponse) Descriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{6}
}

func (x *CreateResponse) GetUdf() *UserDefinedFunction {
	if x != nil {
		return x.Udf
	}
	return nil
}

type UpdateResponse struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Udf *UserDefinedFunction `protobuf:"bytes,1,opt,name=udf,proto3" json:"udf,omitempty"`
}

func (x *UpdateResponse) Reset() {
	*x = UpdateResponse{}
	if protoimpl.UnsafeEnabled {
		mi := &file_proto_udf_v1_udf_proto_msgTypes[7]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *UpdateResponse) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*UpdateResponse) ProtoMessage() {}

func (x *UpdateResponse) ProtoReflect() protoreflect.Message {
	mi := &file_proto_udf_v1_udf_proto_msgTypes[7]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use UpdateResponse.ProtoReflect.Descriptor instead.
func (*UpdateResponse) Descriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{7}
}

func (x *UpdateResponse) GetUdf() *UserDefinedFunction {
	if x != nil {
		return x.Udf
	}
	return nil
}

type DeleteResponse struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Udf *UserDefinedFunction `protobuf:"bytes,1,opt,name=udf,proto3" json:"udf,omitempty"`
}

func (x *DeleteResponse) Reset() {
	*x = DeleteResponse{}
	if protoimpl.UnsafeEnabled {
		mi := &file_proto_udf_v1_udf_proto_msgTypes[8]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *DeleteResponse) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*DeleteResponse) ProtoMessage() {}

func (x *DeleteResponse) ProtoReflect() protoreflect.Message {
	mi := &file_proto_udf_v1_udf_proto_msgTypes[8]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use DeleteResponse.ProtoReflect.Descriptor instead.
func (*DeleteResponse) Descriptor() ([]byte, []int) {
	return file_proto_udf_v1_udf_proto_rawDescGZIP(), []int{8}
}

func (x *DeleteResponse) GetUdf() *UserDefinedFunction {
	if x != nil {
		return x.Udf
	}
	return nil
}

var File_proto_udf_v1_udf_proto protoreflect.FileDescriptor

var file_proto_udf_v1_udf_proto_rawDesc = []byte{
	0x0a, 0x16, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x2f, 0x75, 0x64, 0x66, 0x2f, 0x76, 0x31, 0x2f, 0x75,
	0x64, 0x66, 0x2e, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x1a, 0x1f, 0x67, 0x6f, 0x6f, 0x67, 0x6c, 0x65,
	0x2f, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x62, 0x75, 0x66, 0x2f, 0x74, 0x69, 0x6d, 0x65, 0x73, 0x74,
	0x61, 0x6d, 0x70, 0x2e, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x22, 0xae, 0x02, 0x0a, 0x13, 0x55, 0x73,
	0x65, 0x72, 0x44, 0x65, 0x66, 0x69, 0x6e, 0x65, 0x64, 0x46, 0x75, 0x6e, 0x63, 0x74, 0x69, 0x6f,
	0x6e, 0x12, 0x10, 0x0a, 0x03, 0x75, 0x72, 0x6e, 0x18, 0x01, 0x20, 0x01, 0x28, 0x09, 0x52, 0x03,
	0x75, 0x72, 0x6e, 0x12, 0x14, 0x0a, 0x05, 0x62, 0x79, 0x74, 0x65, 0x73, 0x18, 0x02, 0x20, 0x01,
	0x28, 0x0c, 0x52, 0x05, 0x62, 0x79, 0x74, 0x65, 0x73, 0x12, 0x39, 0x0a, 0x08, 0x6c, 0x61, 0x6e,
	0x67, 0x75, 0x61, 0x67, 0x65, 0x18, 0x03, 0x20, 0x01, 0x28, 0x0e, 0x32, 0x1d, 0x2e, 0x55, 0x73,
	0x65, 0x72, 0x44, 0x65, 0x66, 0x69, 0x6e, 0x65, 0x64, 0x46, 0x75, 0x6e, 0x63, 0x74, 0x69, 0x6f,
	0x6e, 0x2e, 0x4c, 0x61, 0x6e, 0x67, 0x75, 0x61, 0x67, 0x65, 0x52, 0x08, 0x6c, 0x61, 0x6e, 0x67,
	0x75, 0x61, 0x67, 0x65, 0x12, 0x34, 0x0a, 0x07, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x64, 0x18,
	0x04, 0x20, 0x01, 0x28, 0x0b, 0x32, 0x1a, 0x2e, 0x67, 0x6f, 0x6f, 0x67, 0x6c, 0x65, 0x2e, 0x70,
	0x72, 0x6f, 0x74, 0x6f, 0x62, 0x75, 0x66, 0x2e, 0x54, 0x69, 0x6d, 0x65, 0x73, 0x74, 0x61, 0x6d,
	0x70, 0x52, 0x07, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x64, 0x12, 0x34, 0x0a, 0x07, 0x75, 0x70,
	0x64, 0x61, 0x74, 0x65, 0x64, 0x18, 0x05, 0x20, 0x01, 0x28, 0x0b, 0x32, 0x1a, 0x2e, 0x67, 0x6f,
	0x6f, 0x67, 0x6c, 0x65, 0x2e, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x62, 0x75, 0x66, 0x2e, 0x54, 0x69,
	0x6d, 0x65, 0x73, 0x74, 0x61, 0x6d, 0x70, 0x52, 0x07, 0x75, 0x70, 0x64, 0x61, 0x74, 0x65, 0x64,
	0x22, 0x48, 0x0a, 0x08, 0x4c, 0x61, 0x6e, 0x67, 0x75, 0x61, 0x67, 0x65, 0x12, 0x18, 0x0a, 0x14,
	0x4c, 0x61, 0x6e, 0x67, 0x75, 0x61, 0x67, 0x65, 0x5f, 0x55, 0x4e, 0x53, 0x50, 0x45, 0x43, 0x49,
	0x46, 0x49, 0x45, 0x44, 0x10, 0x00, 0x12, 0x0f, 0x0a, 0x0b, 0x4c, 0x61, 0x6e, 0x67, 0x75, 0x61,
	0x67, 0x65, 0x5f, 0x47, 0x6f, 0x10, 0x01, 0x12, 0x11, 0x0a, 0x0d, 0x4c, 0x61, 0x6e, 0x67, 0x75,
	0x61, 0x67, 0x65, 0x5f, 0x52, 0x75, 0x73, 0x74, 0x10, 0x02, 0x22, 0x23, 0x0a, 0x0f, 0x44, 0x65,
	0x73, 0x63, 0x72, 0x69, 0x62, 0x65, 0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x12, 0x10, 0x0a,
	0x03, 0x75, 0x72, 0x6e, 0x18, 0x01, 0x20, 0x01, 0x28, 0x09, 0x52, 0x03, 0x75, 0x72, 0x6e, 0x22,
	0x37, 0x0a, 0x0d, 0x43, 0x72, 0x65, 0x61, 0x74, 0x65, 0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74,
	0x12, 0x26, 0x0a, 0x03, 0x75, 0x64, 0x66, 0x18, 0x01, 0x20, 0x01, 0x28, 0x0b, 0x32, 0x14, 0x2e,
	0x55, 0x73, 0x65, 0x72, 0x44, 0x65, 0x66, 0x69, 0x6e, 0x65, 0x64, 0x46, 0x75, 0x6e, 0x63, 0x74,
	0x69, 0x6f, 0x6e, 0x52, 0x03, 0x75, 0x64, 0x66, 0x22, 0x37, 0x0a, 0x0d, 0x55, 0x70, 0x64, 0x61,
	0x74, 0x65, 0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x12, 0x26, 0x0a, 0x03, 0x75, 0x64, 0x66,
	0x18, 0x01, 0x20, 0x01, 0x28, 0x0b, 0x32, 0x14, 0x2e, 0x55, 0x73, 0x65, 0x72, 0x44, 0x65, 0x66,
	0x69, 0x6e, 0x65, 0x64, 0x46, 0x75, 0x6e, 0x63, 0x74, 0x69, 0x6f, 0x6e, 0x52, 0x03, 0x75, 0x64,
	0x66, 0x22, 0x21, 0x0a, 0x0d, 0x44, 0x65, 0x6c, 0x65, 0x74, 0x65, 0x52, 0x65, 0x71, 0x75, 0x65,
	0x73, 0x74, 0x12, 0x10, 0x0a, 0x03, 0x75, 0x72, 0x6e, 0x18, 0x01, 0x20, 0x01, 0x28, 0x09, 0x52,
	0x03, 0x75, 0x72, 0x6e, 0x22, 0x3a, 0x0a, 0x10, 0x44, 0x65, 0x73, 0x63, 0x72, 0x69, 0x62, 0x65,
	0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x12, 0x26, 0x0a, 0x03, 0x75, 0x64, 0x66, 0x18,
	0x01, 0x20, 0x01, 0x28, 0x0b, 0x32, 0x14, 0x2e, 0x55, 0x73, 0x65, 0x72, 0x44, 0x65, 0x66, 0x69,
	0x6e, 0x65, 0x64, 0x46, 0x75, 0x6e, 0x63, 0x74, 0x69, 0x6f, 0x6e, 0x52, 0x03, 0x75, 0x64, 0x66,
	0x22, 0x38, 0x0a, 0x0e, 0x43, 0x72, 0x65, 0x61, 0x74, 0x65, 0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e,
	0x73, 0x65, 0x12, 0x26, 0x0a, 0x03, 0x75, 0x64, 0x66, 0x18, 0x01, 0x20, 0x01, 0x28, 0x0b, 0x32,
	0x14, 0x2e, 0x55, 0x73, 0x65, 0x72, 0x44, 0x65, 0x66, 0x69, 0x6e, 0x65, 0x64, 0x46, 0x75, 0x6e,
	0x63, 0x74, 0x69, 0x6f, 0x6e, 0x52, 0x03, 0x75, 0x64, 0x66, 0x22, 0x38, 0x0a, 0x0e, 0x55, 0x70,
	0x64, 0x61, 0x74, 0x65, 0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x12, 0x26, 0x0a, 0x03,
	0x75, 0x64, 0x66, 0x18, 0x01, 0x20, 0x01, 0x28, 0x0b, 0x32, 0x14, 0x2e, 0x55, 0x73, 0x65, 0x72,
	0x44, 0x65, 0x66, 0x69, 0x6e, 0x65, 0x64, 0x46, 0x75, 0x6e, 0x63, 0x74, 0x69, 0x6f, 0x6e, 0x52,
	0x03, 0x75, 0x64, 0x66, 0x22, 0x38, 0x0a, 0x0e, 0x44, 0x65, 0x6c, 0x65, 0x74, 0x65, 0x52, 0x65,
	0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x12, 0x26, 0x0a, 0x03, 0x75, 0x64, 0x66, 0x18, 0x01, 0x20,
	0x01, 0x28, 0x0b, 0x32, 0x14, 0x2e, 0x55, 0x73, 0x65, 0x72, 0x44, 0x65, 0x66, 0x69, 0x6e, 0x65,
	0x64, 0x46, 0x75, 0x6e, 0x63, 0x74, 0x69, 0x6f, 0x6e, 0x52, 0x03, 0x75, 0x64, 0x66, 0x32, 0xc6,
	0x01, 0x0a, 0x0a, 0x55, 0x44, 0x46, 0x53, 0x65, 0x72, 0x76, 0x69, 0x63, 0x65, 0x12, 0x31, 0x0a,
	0x08, 0x44, 0x65, 0x73, 0x63, 0x72, 0x69, 0x62, 0x65, 0x12, 0x10, 0x2e, 0x44, 0x65, 0x73, 0x63,
	0x72, 0x69, 0x62, 0x65, 0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x1a, 0x11, 0x2e, 0x44, 0x65,
	0x73, 0x63, 0x72, 0x69, 0x62, 0x65, 0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x22, 0x00,
	0x12, 0x2b, 0x0a, 0x06, 0x43, 0x72, 0x65, 0x61, 0x74, 0x65, 0x12, 0x0e, 0x2e, 0x43, 0x72, 0x65,
	0x61, 0x74, 0x65, 0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x1a, 0x0f, 0x2e, 0x43, 0x72, 0x65,
	0x61, 0x74, 0x65, 0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x22, 0x00, 0x12, 0x2b, 0x0a,
	0x06, 0x55, 0x70, 0x64, 0x61, 0x74, 0x65, 0x12, 0x0e, 0x2e, 0x55, 0x70, 0x64, 0x61, 0x74, 0x65,
	0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x1a, 0x0f, 0x2e, 0x55, 0x70, 0x64, 0x61, 0x74, 0x65,
	0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x22, 0x00, 0x12, 0x2b, 0x0a, 0x06, 0x44, 0x65,
	0x6c, 0x65, 0x74, 0x65, 0x12, 0x0e, 0x2e, 0x44, 0x65, 0x6c, 0x65, 0x74, 0x65, 0x52, 0x65, 0x71,
	0x75, 0x65, 0x73, 0x74, 0x1a, 0x0f, 0x2e, 0x44, 0x65, 0x6c, 0x65, 0x74, 0x65, 0x52, 0x65, 0x73,
	0x70, 0x6f, 0x6e, 0x73, 0x65, 0x22, 0x00, 0x42, 0x0e, 0x5a, 0x0c, 0x70, 0x72, 0x6f, 0x74, 0x6f,
	0x2f, 0x75, 0x64, 0x66, 0x2f, 0x76, 0x31, 0x62, 0x06, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x33,
}

var (
	file_proto_udf_v1_udf_proto_rawDescOnce sync.Once
	file_proto_udf_v1_udf_proto_rawDescData = file_proto_udf_v1_udf_proto_rawDesc
)

func file_proto_udf_v1_udf_proto_rawDescGZIP() []byte {
	file_proto_udf_v1_udf_proto_rawDescOnce.Do(func() {
		file_proto_udf_v1_udf_proto_rawDescData = protoimpl.X.CompressGZIP(file_proto_udf_v1_udf_proto_rawDescData)
	})
	return file_proto_udf_v1_udf_proto_rawDescData
}

var file_proto_udf_v1_udf_proto_enumTypes = make([]protoimpl.EnumInfo, 1)
var file_proto_udf_v1_udf_proto_msgTypes = make([]protoimpl.MessageInfo, 9)
var file_proto_udf_v1_udf_proto_goTypes = []interface{}{
	(UserDefinedFunction_Language)(0), // 0: UserDefinedFunction.Language
	(*UserDefinedFunction)(nil),       // 1: UserDefinedFunction
	(*DescribeRequest)(nil),           // 2: DescribeRequest
	(*CreateRequest)(nil),             // 3: CreateRequest
	(*UpdateRequest)(nil),             // 4: UpdateRequest
	(*DeleteRequest)(nil),             // 5: DeleteRequest
	(*DescribeResponse)(nil),          // 6: DescribeResponse
	(*CreateResponse)(nil),            // 7: CreateResponse
	(*UpdateResponse)(nil),            // 8: UpdateResponse
	(*DeleteResponse)(nil),            // 9: DeleteResponse
	(*timestamppb.Timestamp)(nil),     // 10: google.protobuf.Timestamp
}
var file_proto_udf_v1_udf_proto_depIdxs = []int32{
	0,  // 0: UserDefinedFunction.language:type_name -> UserDefinedFunction.Language
	10, // 1: UserDefinedFunction.created:type_name -> google.protobuf.Timestamp
	10, // 2: UserDefinedFunction.updated:type_name -> google.protobuf.Timestamp
	1,  // 3: CreateRequest.udf:type_name -> UserDefinedFunction
	1,  // 4: UpdateRequest.udf:type_name -> UserDefinedFunction
	1,  // 5: DescribeResponse.udf:type_name -> UserDefinedFunction
	1,  // 6: CreateResponse.udf:type_name -> UserDefinedFunction
	1,  // 7: UpdateResponse.udf:type_name -> UserDefinedFunction
	1,  // 8: DeleteResponse.udf:type_name -> UserDefinedFunction
	2,  // 9: UDFService.Describe:input_type -> DescribeRequest
	3,  // 10: UDFService.Create:input_type -> CreateRequest
	4,  // 11: UDFService.Update:input_type -> UpdateRequest
	5,  // 12: UDFService.Delete:input_type -> DeleteRequest
	6,  // 13: UDFService.Describe:output_type -> DescribeResponse
	7,  // 14: UDFService.Create:output_type -> CreateResponse
	8,  // 15: UDFService.Update:output_type -> UpdateResponse
	9,  // 16: UDFService.Delete:output_type -> DeleteResponse
	13, // [13:17] is the sub-list for method output_type
	9,  // [9:13] is the sub-list for method input_type
	9,  // [9:9] is the sub-list for extension type_name
	9,  // [9:9] is the sub-list for extension extendee
	0,  // [0:9] is the sub-list for field type_name
}

func init() { file_proto_udf_v1_udf_proto_init() }
func file_proto_udf_v1_udf_proto_init() {
	if File_proto_udf_v1_udf_proto != nil {
		return
	}
	if !protoimpl.UnsafeEnabled {
		file_proto_udf_v1_udf_proto_msgTypes[0].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*UserDefinedFunction); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_proto_udf_v1_udf_proto_msgTypes[1].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*DescribeRequest); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_proto_udf_v1_udf_proto_msgTypes[2].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*CreateRequest); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_proto_udf_v1_udf_proto_msgTypes[3].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*UpdateRequest); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_proto_udf_v1_udf_proto_msgTypes[4].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*DeleteRequest); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_proto_udf_v1_udf_proto_msgTypes[5].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*DescribeResponse); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_proto_udf_v1_udf_proto_msgTypes[6].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*CreateResponse); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_proto_udf_v1_udf_proto_msgTypes[7].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*UpdateResponse); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_proto_udf_v1_udf_proto_msgTypes[8].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*DeleteResponse); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
	}
	type x struct{}
	out := protoimpl.TypeBuilder{
		File: protoimpl.DescBuilder{
			GoPackagePath: reflect.TypeOf(x{}).PkgPath(),
			RawDescriptor: file_proto_udf_v1_udf_proto_rawDesc,
			NumEnums:      1,
			NumMessages:   9,
			NumExtensions: 0,
			NumServices:   1,
		},
		GoTypes:           file_proto_udf_v1_udf_proto_goTypes,
		DependencyIndexes: file_proto_udf_v1_udf_proto_depIdxs,
		EnumInfos:         file_proto_udf_v1_udf_proto_enumTypes,
		MessageInfos:      file_proto_udf_v1_udf_proto_msgTypes,
	}.Build()
	File_proto_udf_v1_udf_proto = out.File
	file_proto_udf_v1_udf_proto_rawDesc = nil
	file_proto_udf_v1_udf_proto_goTypes = nil
	file_proto_udf_v1_udf_proto_depIdxs = nil
}
