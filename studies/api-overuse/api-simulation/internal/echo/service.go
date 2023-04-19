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

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/cache"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
	echo_v1 "github.com/apache/beam/studies/api-overuse/api-simulation/internal/proto/echo/v1"
	"google.golang.org/grpc"
)

// RegisterService to a grpcServer.
func RegisterService(ctx context.Context, server *grpc.Server, quotaCache cache.Quota, logger logging.Logger) error {
	if quotaCache == nil {
		quotaCache = &cache.InMemory{}
	}
	if logger == nil {
		logger = logging.Default
	}
	svc := &echoService{
		logger:     logger,
		quotaCache: quotaCache,
	}
	if err := svc.quotaCache.Alive(ctx); err != nil {
		return err
	}
	svc.logger.Info(ctx, map[string]interface{}{
		"message": "registered echo service",
		"logger":  fmt.Sprintf("%T", svc.logger),
		"cache":   fmt.Sprintf("%T", svc.quotaCache),
	})
	echo_v1.RegisterEchoServiceServer(server, svc)
	return nil
}

type echoService struct {
	echo_v1.UnimplementedEchoServiceServer
	quotaCache cache.Quota
	logger     logging.Logger
}

func (e *echoService) Echo(ctx context.Context, request *echo_v1.EchoRequest) (*echo_v1.EchoResponse, error) {
	if err := e.quotaCache.Decrement(ctx, request.Id); err != nil {
		return nil, err
	}
	return &echo_v1.EchoResponse{
		Id:      request.Id,
		Payload: request.Payload,
	}, nil
}
