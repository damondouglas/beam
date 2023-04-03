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

package quota

import (
	"context"
	"time"

	"github.com/alicebob/miniredis/v2"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
	"github.com/redis/go-redis/v9"
)

type InMemory struct {
	svc      *miniredis.Miniredis
	internal *RedisQuota
	logger   logging.Logger
}

func (q *InMemory) Alive(ctx context.Context) error {
	if q.svc == nil {
		var err error
		q.svc, err = miniredis.Run()
		if err != nil {
			return err
		}
	}
	if q.internal == nil {
		q.internal = (*RedisQuota)(redis.NewClient(&redis.Options{
			Addr: q.svc.Addr(),
		}))
	}
	return q.internal.Alive(ctx)
}

func (q InMemory) Decrement(ctx context.Context, quotaID string) error {
	return q.internal.Decrement(ctx, quotaID)
}

func (q InMemory) InitializeAndRefreshPerInterval(ctx context.Context, quotaID string, size uint64, interval time.Duration) error {
	return q.internal.InitializeAndRefreshPerInterval(ctx, quotaID, size, interval)
}
