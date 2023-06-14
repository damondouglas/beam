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

// Tests the ./cmd/echo service.
// Assumes the service is already running and required environment variables
// that it reports if missing.
// To run:
// go test ./src/main/go/testing/echo

package integration

import (
	"context"
	"fmt"
	"testing"
	"time"

	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/environment"
	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/logging"
	echov1 "github.com/apache/beam/test-infra/pipelines/src/main/go/internal/proto/echo/v1"
	quotav1 "github.com/apache/beam/test-infra/pipelines/src/main/go/internal/proto/quota/v1"
	"github.com/google/go-cmp/cmp"
	"github.com/google/uuid"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

const (
	echoHost  environment.Variable = "ECHO_HOST"
	quotaHost environment.Variable = "QUOTA_HOST"
)

var (
	echoClient  echov1.EchoServiceClient
	quotaClient quotav1.QuotaServiceClient
	logger      *logging.Logger
)

func init() {
	ctx := context.Background()
	logger = logging.New(ctx, "github.com/apache/beam/.test-infra/pipelines/src/main/go/testing/echo", logging.LevelVariable)
	if err := environment.Missing(echoHost); err != nil {
		logger.Fatal(ctx, err)
	}
	if err := vars(); err != nil {
		logger.Fatal(ctx, err)
	}
}

func vars() error {
	echoConn, err := grpc.Dial(echoHost.Value(), grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return err
	}

	echoClient = echov1.NewEchoServiceClient(echoConn)

	quotaConn, err := grpc.Dial(quotaHost.Value(), grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return err
	}

	quotaClient = quotav1.NewQuotaServiceClient(quotaConn)

	return nil
}

func TestEchoErrors(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*3)
	defer cancel()
	id := uuid.New().String()
	for _, tt := range []struct {
		name    string
		req     *echov1.EchoRequest
		want    *echov1.EchoResponse
		wantErr error
	}{
		{
			name:    "nil quota should error",
			wantErr: fmt.Errorf("rpc error: code = Internal desc = grpc: error while marshaling: proto: Marshal called with nil"),
		},
		{
			name:    "empty quota id should error",
			req:     &echov1.EchoRequest{},
			wantErr: fmt.Errorf("rpc error: code = InvalidArgument desc = invalid request: Id is required but empty"),
		},
		{
			name: "quota id does not exist should error",
			req: &echov1.EchoRequest{
				Id: id,
			},
			wantErr: fmt.Errorf("rpc error: code = ResourceExhausted desc = quota exhausted for quotaID: %s", id),
		},
	} {
		t.Run(tt.name, func(t *testing.T) {
			resp, err := echoClient.Echo(ctx, tt.req)
			var diff string
			if tt.wantErr != nil {
				diff = cmp.Diff(tt.wantErr.Error(), err.Error())
			}
			if resp != nil {
				diff = cmp.Diff(tt.want, resp)
			}
			if diff != "" {
				t.Errorf("Echo(%+v) diff: %s\n", tt.req, diff)
			}
		})
	}

	select {
	case <-ctx.Done():
		if ctx.Err() != nil {
			t.Errorf("deadline exceeded: %s", ctx.Err())
		}
	default:
		return
	}
}

func TestEchoWithQuota(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*3)
	defer cancel()
	existsId := uuid.New().String()
	quota := &quotav1.Quota{
		Id:                          existsId,
		Size:                        10,
		RefreshMillisecondsInterval: (time.Second * 3).Milliseconds(),
	}
	createRequest := &quotav1.CreateQuotaRequest{
		Quota: quota,
	}
	created, err := quotaClient.Create(ctx, createRequest)
	if err != nil {
		t.Fatalf("error invoking %T.Create(%+v): %s", quotaClient, createRequest, err)
	}

	if created.Quota == nil {
		t.Errorf("%T.Create(%+v) = nil, want: %s", quotaClient, createRequest, quota.Id)
		return
	}

	if created.Quota.Id != quota.Id {
		t.Errorf("%T.Create(%+v) = %s, want: %s", quotaClient, createRequest, created.Quota.Id, quota.Id)
	}

	select {
	case <-ctx.Done():
		if ctx.Err() != nil {
			t.Errorf("deadline exceeded: %s", ctx.Err())
		}
	default:
		return
	}
}
