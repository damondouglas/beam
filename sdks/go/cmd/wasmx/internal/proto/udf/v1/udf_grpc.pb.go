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

// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.3.0
// - protoc             (unknown)
// source: proto/udf/v1/udf.proto

package v1

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

const (
	UDFService_Describe_FullMethodName = "/UDFService/Describe"
	UDFService_Create_FullMethodName   = "/UDFService/Create"
	UDFService_Update_FullMethodName   = "/UDFService/Update"
	UDFService_Delete_FullMethodName   = "/UDFService/Delete"
)

// UDFServiceClient is the client API for UDFService service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type UDFServiceClient interface {
	Describe(ctx context.Context, in *DescribeRequest, opts ...grpc.CallOption) (*DescribeResponse, error)
	Create(ctx context.Context, in *CreateRequest, opts ...grpc.CallOption) (*CreateResponse, error)
	Update(ctx context.Context, in *UpdateRequest, opts ...grpc.CallOption) (*UpdateResponse, error)
	Delete(ctx context.Context, in *DeleteRequest, opts ...grpc.CallOption) (*DeleteResponse, error)
}

type uDFServiceClient struct {
	cc grpc.ClientConnInterface
}

func NewUDFServiceClient(cc grpc.ClientConnInterface) UDFServiceClient {
	return &uDFServiceClient{cc}
}

func (c *uDFServiceClient) Describe(ctx context.Context, in *DescribeRequest, opts ...grpc.CallOption) (*DescribeResponse, error) {
	out := new(DescribeResponse)
	err := c.cc.Invoke(ctx, UDFService_Describe_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *uDFServiceClient) Create(ctx context.Context, in *CreateRequest, opts ...grpc.CallOption) (*CreateResponse, error) {
	out := new(CreateResponse)
	err := c.cc.Invoke(ctx, UDFService_Create_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *uDFServiceClient) Update(ctx context.Context, in *UpdateRequest, opts ...grpc.CallOption) (*UpdateResponse, error) {
	out := new(UpdateResponse)
	err := c.cc.Invoke(ctx, UDFService_Update_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *uDFServiceClient) Delete(ctx context.Context, in *DeleteRequest, opts ...grpc.CallOption) (*DeleteResponse, error) {
	out := new(DeleteResponse)
	err := c.cc.Invoke(ctx, UDFService_Delete_FullMethodName, in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// UDFServiceServer is the server API for UDFService service.
// All implementations must embed UnimplementedUDFServiceServer
// for forward compatibility
type UDFServiceServer interface {
	Describe(context.Context, *DescribeRequest) (*DescribeResponse, error)
	Create(context.Context, *CreateRequest) (*CreateResponse, error)
	Update(context.Context, *UpdateRequest) (*UpdateResponse, error)
	Delete(context.Context, *DeleteRequest) (*DeleteResponse, error)
	mustEmbedUnimplementedUDFServiceServer()
}

// UnimplementedUDFServiceServer must be embedded to have forward compatible implementations.
type UnimplementedUDFServiceServer struct {
}

func (UnimplementedUDFServiceServer) Describe(context.Context, *DescribeRequest) (*DescribeResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Describe not implemented")
}
func (UnimplementedUDFServiceServer) Create(context.Context, *CreateRequest) (*CreateResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Create not implemented")
}
func (UnimplementedUDFServiceServer) Update(context.Context, *UpdateRequest) (*UpdateResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Update not implemented")
}
func (UnimplementedUDFServiceServer) Delete(context.Context, *DeleteRequest) (*DeleteResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Delete not implemented")
}
func (UnimplementedUDFServiceServer) mustEmbedUnimplementedUDFServiceServer() {}

// UnsafeUDFServiceServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to UDFServiceServer will
// result in compilation errors.
type UnsafeUDFServiceServer interface {
	mustEmbedUnimplementedUDFServiceServer()
}

func RegisterUDFServiceServer(s grpc.ServiceRegistrar, srv UDFServiceServer) {
	s.RegisterService(&UDFService_ServiceDesc, srv)
}

func _UDFService_Describe_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(DescribeRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(UDFServiceServer).Describe(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: UDFService_Describe_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(UDFServiceServer).Describe(ctx, req.(*DescribeRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _UDFService_Create_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(CreateRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(UDFServiceServer).Create(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: UDFService_Create_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(UDFServiceServer).Create(ctx, req.(*CreateRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _UDFService_Update_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(UpdateRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(UDFServiceServer).Update(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: UDFService_Update_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(UDFServiceServer).Update(ctx, req.(*UpdateRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _UDFService_Delete_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(DeleteRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(UDFServiceServer).Delete(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: UDFService_Delete_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(UDFServiceServer).Delete(ctx, req.(*DeleteRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// UDFService_ServiceDesc is the grpc.ServiceDesc for UDFService service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var UDFService_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "UDFService",
	HandlerType: (*UDFServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "Describe",
			Handler:    _UDFService_Describe_Handler,
		},
		{
			MethodName: "Create",
			Handler:    _UDFService_Create_Handler,
		},
		{
			MethodName: "Update",
			Handler:    _UDFService_Update_Handler,
		},
		{
			MethodName: "Delete",
			Handler:    _UDFService_Delete_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "proto/udf/v1/udf.proto",
}
