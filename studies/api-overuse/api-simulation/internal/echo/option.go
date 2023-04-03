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
	"context"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/quota"
)

// Option for the echoService.
type Option interface {
	apply(context.Context, *echoService) error
}

// WithQuota Option assigns a quota.Quota to the echo service.
// Panics if cache is nil.
func WithQuota(q quota.Quota) Option {
	if q == nil {
		panic("q is nil")
	}
	return &quotaOption{
		q: q,
	}
}

// WithLogger Option assigns a logging.Logger
func WithLogger(logger logging.Logger) Option {
	return &loggerOption{
		logger: logger,
	}
}

type quotaOption struct {
	q quota.Quota
}

func (opt *quotaOption) apply(ctx context.Context, svc *echoService) error {
	if err := opt.q.Alive(ctx); err != nil {
		return err
	}
	svc.q = opt.q
	return nil
}

type loggerOption struct {
	logger logging.Logger
}

func (opt *loggerOption) apply(_ context.Context, svc *echoService) error {
	svc.logger = opt.logger
	return nil
}
