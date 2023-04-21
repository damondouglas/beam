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

// Runs the quota service.
package main

import (
	"context"
	"fmt"
	"net"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/cache"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/environment"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/k8s"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/quota"
	"github.com/redis/go-redis/v9"
	"google.golang.org/grpc"
	"sigs.k8s.io/controller-runtime/pkg/manager/signals"
)

var (
	port           environment.Variable = "PORT"
	refresherImage environment.Variable = "REFRESHER_IMAGE"
	namespace      environment.Variable = "NAMESPACE"

	spec = &quota.ServiceSpec{
		RefresherServiceSpec: &quota.RefresherServiceSpec{
			ContainerName: "refresher",
			Image:         refresherImage.Value(),
		},
	}

	logger = logging.Default.WithName("quota-service")

	required = []environment.Variable{
		port,
		cache.Host,
		namespace,
		refresherImage,
	}

	env     = environment.Map(required...)
	address = fmt.Sprintf(":%s", port.Value())
)

func init() {
	ctx := context.Background()
	if err := environment.Missing(required...); err != nil {
		logger.Fatal(ctx, map[string]interface{}{
			"message": err.Error(),
			"env":     env,
		})
	}

	if err := vars(ctx); err != nil {
		logger.Fatal(ctx, map[string]interface{}{
			"message": err.Error(),
			"env":     env,
		})
	}
}

func vars(ctx context.Context) error {
	redisClient := redis.NewClient(&redis.Options{
		Addr: cache.Host.Value(),
	})

	cacheClient := (*cache.RedisCache)(redisClient)

	spec.Cache = cacheClient
	spec.Publisher = cacheClient

	k8sClient, err := k8s.NewDefaultClient()
	if err != nil {
		return err
	}

	ns := k8sClient.Namespace(namespace.Value())
	spec.JobsClient = k8sClient.Jobs(ns)

	if err := redisClient.Ping(ctx).Err(); err != nil {
		return err
	}

	logger.Info(ctx, map[string]interface{}{
		"message": "pinged cache host ok",
		"host":    cache.Host.Value(),
	})

	return nil
}

func main() {
	ctx, cancel := context.WithCancel(signals.SetupSignalHandler())
	defer cancel()

	lis, err := net.Listen("tcp", address)
	if err != nil {
		logger.Error(ctx, map[string]interface{}{
			"message": err.Error(),
			"env":     env,
		})
	}

	svc := grpc.NewServer()

	if err := quota.RegisterService(ctx, svc, spec); err != nil {
		logger.Error(ctx, map[string]interface{}{
			"message": err.Error(),
			"env":     env,
		})
		return
	}

	go func() {
		if err := svc.Serve(lis); err != nil {
			logger.Error(ctx, map[string]interface{}{
				"message": err.Error(),
			})
			return
		}
	}()

	logger.Info(ctx, map[string]interface{}{
		"message": "started quota service",
		"env":     env,
	})

	for {
		select {
		case <-ctx.Done():
			svc.GracefulStop()
			return
		}
	}
}
