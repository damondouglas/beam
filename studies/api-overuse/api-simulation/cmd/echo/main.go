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

// Server runs the simulated quota service.
package main

import (
	"context"
	"fmt"
	"net"
	"os"
	"os/signal"
	"strings"
	"syscall"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/cache"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/echo"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
	"github.com/redis/go-redis/v9"
	"github.com/spf13/cobra"
	"google.golang.org/grpc"
)

const (
	portKey      = "PORT"
	quotaHostKey = "QUOTA_HOST"
)

var (
	rootCmd = &cobra.Command{
		Use:   "api-simulation",
		Short: "A simple service that simulates an API quota.",
		Run:   run,
	}
	address    = fmt.Sprintf(":%s", os.Getenv(portKey))
	logger     = logging.Default.WithName("echo-service")
	cacheQuota cache.Quota

	requiredEnvironmentVariables = []string{
		portKey,
		quotaHostKey,
	}
)

func init() {
	ctx := context.Background()
	envs, err := env()
	if err != nil {
		logger.Error(ctx, envs)
		os.Exit(1)
	}
	logger.Info(ctx, envs)

	if err := vars(ctx); err != nil {
		logger.Error(ctx, map[string]interface{}{
			"message": err.Error(),
		})
		os.Exit(1)
	}
}

func env() (map[string]interface{}, error) {
	var missing []string
	result := make(map[string]interface{})
	for _, k := range requiredEnvironmentVariables {
		result[k] = os.Getenv(k)
		if os.Getenv(k) == "" {
			missing = append(missing, k)
		}
	}
	if len(missing) > 0 {
		err := fmt.Errorf("missing required environment variables: %s", strings.Join(missing, "; "))
		result["message"] = err.Error()
		return result, err
	}
	return result, nil
}

func vars(ctx context.Context) error {
	redisClient := redis.NewClient(&redis.Options{
		Addr: os.Getenv(quotaHostKey),
	})
	cacheQuota = (*cache.RedisCache)(redisClient)

	return cacheQuota.Alive(ctx)
}

func main() {
	if err := rootCmd.Execute(); err != nil {
		panic(err)
	}
}

func run(cmd *cobra.Command, _ []string) {
	ctx, cancel := signal.NotifyContext(cmd.Context(), syscall.SIGINT, syscall.SIGKILL)
	defer cancel()

	lis, err := net.Listen("tcp", address)
	if err != nil {
		err = fmt.Errorf("error listening on address: %s, %w", address, err)
		logger.Error(ctx, map[string]interface{}{
			"message": err.Error(),
			portKey:   os.Getenv(portKey),
		})
		return
	}

	svc := grpc.NewServer()
	if err := echo.RegisterService(ctx, svc, cacheQuota, logger); err != nil {
		err = fmt.Errorf("error registering echo service: %w", err)
		logger.Error(ctx, map[string]interface{}{
			"message": err.Error(),
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

	for {
		select {
		case <-ctx.Done():
			if svc != nil {
				svc.GracefulStop()
			}
			return
		}
	}
}
