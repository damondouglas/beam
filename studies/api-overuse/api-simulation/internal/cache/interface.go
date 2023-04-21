package cache

import (
	"context"
	"time"
)

type Event []byte

type Decrementer interface {
	Decrement(ctx context.Context, quotaID string) error
}

type Refresher interface {
	Refresh(ctx context.Context, quotaID string, size uint64, interval time.Duration) error
}

type Publisher interface {
	Publish(ctx context.Context, key string, event Event) error
}

type Subscriber interface {
	Subscribe(ctx context.Context, events chan Event, keys ...string) error
}
