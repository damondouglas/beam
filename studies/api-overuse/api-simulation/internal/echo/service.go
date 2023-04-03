// Licensed to the Apache Software Foundation (ASF) under one or more
// contributor license agreements.  See the NOTICE file distributed with
// this work for additional information regarding copyright ownership.
// The ASF licenses this file to You under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with
// the License.  You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package echo

import (
	context "context"
	"fmt"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
	echo_v1 "github.com/apache/beam/studies/api-overuse/api-simulation/internal/proto/echo/v1"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/quota"
	"google.golang.org/grpc"
)

// RegisterService to a grpcServer. Provided Option opts override local
// in-memory instances.
func RegisterService(ctx context.Context, server *grpc.Server, opts ...Option) error {
	svc := &echoService{}
	for _, opt := range opts {
		if err := opt.apply(ctx, svc); err != nil {
			return err
		}
	}
	if svc.logger == nil {
		svc.logger = logging.Default
	}
	if svc.q == nil {
		svc.q = &quota.InMemory{}
	}
	if err := svc.q.Alive(ctx); err != nil {
		return err
	}
	svc.logger.Info(ctx, map[string]string{
		"message": "registered echo service",
		"logger":  fmt.Sprintf("%T", svc.logger),
		"quota":   fmt.Sprintf("%T", svc.q),
	})
	echo_v1.RegisterEchoServiceServer(server, svc)
	return nil
}

type echoService struct {
	echo_v1.UnimplementedEchoServiceServer
	q      quota.Quota
	logger logging.Logger
}

func (e *echoService) Echo(ctx context.Context, request *echo_v1.EchoRequest) (*echo_v1.EchoResponse, error) {
	if err := e.q.Decrement(ctx, request.Id); err != nil {
		return nil, err
	}
	return &echo_v1.EchoResponse{
		Id:      request.Id,
		Payload: request.Payload,
	}, nil
}
