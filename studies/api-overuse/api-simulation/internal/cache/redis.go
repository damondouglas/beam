package cache

import (
	"context"
	"fmt"
	"strconv"
	"time"

	"github.com/redis/go-redis/v9"
)

const maxRetries = 3

type RedisCache redis.Client

func (q *RedisCache) Alive(ctx context.Context) error {
	client := (*redis.Client)(q)
	return client.Ping(ctx).Err()
}

func (q *RedisCache) Decrement(ctx context.Context, quotaID string) error {
	client := (*redis.Client)(q)
	txf := func(tx *redis.Tx) error {
		n, err := tx.Get(ctx, quotaID).Uint64()
		if err != nil && err != redis.Nil {
			return err
		}
		if n == 0 || err == redis.Nil {
			return fmt.Errorf("quota exhausted for quotaID: %s", quotaID)
		}

		_, err = tx.TxPipelined(ctx, func(pipe redis.Pipeliner) error {
			return pipe.Decr(ctx, quotaID).Err()
		})

		return err
	}

	for i := 0; i < maxRetries; i++ {
		err := client.Watch(ctx, txf, quotaID)
		if err == nil {
			return nil
		}
		if err == redis.TxFailedErr {
			continue
		}
		return err
	}

	return fmt.Errorf("error: Decrement(%s) reached maximum number of retries: %v", quotaID, maxRetries)
}

func (q *RedisCache) InitializeAndRefreshPerInterval(ctx context.Context, quotaID string, size uint64, interval time.Duration) error {
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()
	tick := time.Tick(interval)
	for {
		select {
		case <-tick:
			if err := q.refresh(ctx, quotaID, size, interval); err != nil {
				return err
			}
			logger.Debug(ctx, map[string]interface{}{
				"message": "refreshed quota",
				"quotaID": quotaID,
				"size":    strconv.FormatUint(size, 10),
			})
		case <-ctx.Done():
			return nil
		}
	}
}

func (q *RedisCache) refresh(ctx context.Context, quotaID string, size uint64, interval time.Duration) error {
	client := (*redis.Client)(q)
	return client.SetEx(ctx, quotaID, size, interval).Err()
}

func (q *RedisCache) Publish(ctx context.Context, key string, event Event) error {
	client := (*redis.Client)(q)
	return client.Publish(ctx, key, event).Err()
}

func (q *RedisCache) Subscribe(ctx context.Context, events chan Event, keys ...string) error {
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()
	client := (*redis.Client)(q)
	sub := client.PSubscribe(ctx, keys...)
	channel := sub.Channel()
	logger.Debug(ctx, map[string]interface{}{
		"message": "opening channel",
		"keys":    keys,
	})
	for {
		select {
		case msg := <-channel:
			logger.Debug(ctx, map[string]interface{}{
				"message":  "retrieved channel message",
				"received": msg,
			})
			events <- []byte(msg.Payload)
		case <-ctx.Done():
			return nil
		}
	}
}
