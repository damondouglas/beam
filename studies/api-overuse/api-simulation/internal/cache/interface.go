package cache

import (
	"context"
	"time"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
)

var (
	logger = logging.Default.WithName("quota")
)

type Event []byte

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

type Publisher interface {
	aliveChecker
	Publish(ctx context.Context, key string, event Event) error
}

type Subscriber interface {
	aliveChecker
	Subscribe(ctx context.Context, events chan Event, keys ...string) error
}
