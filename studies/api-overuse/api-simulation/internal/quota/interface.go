package quota

import (
	"context"
	"time"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
)

var (
	logger = logging.Default.WithName("quota")
)

type Quota interface {
	Alive(ctx context.Context) error
	Decrement(ctx context.Context, quotaID string) error
	InitializeAndRefreshPerInterval(ctx context.Context, quotaID string, size uint64, interval time.Duration) error
}
