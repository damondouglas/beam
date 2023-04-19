package main

import (
	"context"
	"strconv"
	"time"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/cache"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/environment"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
	"github.com/redis/go-redis/v9"
	"sigs.k8s.io/controller-runtime/pkg/manager/signals"
)

var (
	required = []environment.Variable{
		environment.CacheHost,
		environment.QuotaId,
		environment.QuotaSize,
		environment.QuotaRefreshInterval,
	}

	size     uint64
	interval time.Duration

	logger         = logging.Default.WithName("refresher-service")
	cacheRefresher cache.Refresher
	subscriber     cache.Subscriber
)

func init() {
	ctx := context.Background()
	if err := vars(ctx); err != nil {
		logger.Fatal(ctx, map[string]interface{}{
			"message": err,
		})
	}
}

func vars(ctx context.Context) error {
	var err error
	if err = environment.Missing(required...); err != nil {
		return err
	}
	logger.Debug(ctx, environment.Map(required...))

	if size, err = strconv.ParseUint(environment.QuotaSize.Value(), 10, 64); err != nil {
		return err
	}

	if interval, err = time.ParseDuration(environment.QuotaRefreshInterval.Value()); err != nil {
		return err
	}

	client := redis.NewClient(&redis.Options{
		Addr: environment.CacheHost.Value(),
	})

	rc := (*cache.RedisCache)(client)
	cacheRefresher = rc
	subscriber = rc

	return rc.Alive(ctx)
}

func main() {
	ctx := context.Background()
	if err := run(); err != nil {
		logger.Fatal(ctx, map[string]interface{}{
			"message": err,
		})
	}
}

func run() error {
	ctx, cancel := context.WithCancel(signals.SetupSignalHandler())
	defer cancel()
	errChan := make(chan error)
	evts := make(chan cache.Event)
	go func() {
		if err := subscriber.Subscribe(ctx, evts, environment.QuotaId.Value()); err != nil {
			errChan <- err
		}
	}()
	go func() {
		if err := cacheRefresher.InitializeAndRefreshPerInterval(ctx, environment.QuotaId.Value(), size, interval); err != nil {
			errChan <- err
		}
	}()
	for {
		select {
		case evt := <-evts:
			logger.Info(ctx, map[string]interface{}{
				"message":       "termination signal received",
				"event-payload": string(evt),
				"env":           environment.Map(required...),
			})
			return nil
		case err := <-errChan:
			return err
		case <-ctx.Done():
			return nil
		}
	}
}
