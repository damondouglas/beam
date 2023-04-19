package cache

import (
	"context"
	"time"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
)

var (
	logger = logging.Default.WithName("quota")
)

type aliveChecker interface {
	Alive(ctx context.Context) error
}

type Quota interface {
	aliveChecker
	Decrement(ctx context.Context, quotaID string) error
}

type Refresher interface {
	aliveChecker
	InitializeAndRefreshPerInterval(ctx context.Context, quotaID string, size uint64, interval time.Duration) error
}

type Message interface {
	String() string
}

type Publisher interface {
	Publish(ctx context.Context, key string, message Message) error
}

type Subscriber interface {
	Subscribe(ctx context.Context, messages chan Message, keys ...string) error
}
