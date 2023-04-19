package cache

import (
	"context"
	"fmt"
	"strconv"
	"time"

	"github.com/redis/go-redis/v9"
)

const maxRetries = 3

type RedisQuota redis.Client

func (q *RedisQuota) Alive(ctx context.Context) error {
	client := (*redis.Client)(q)
	return client.Ping(ctx).Err()
}

func (q *RedisQuota) Decrement(ctx context.Context, quotaID string) error {
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

func (q *RedisQuota) InitializeAndRefreshPerInterval(ctx context.Context, quotaID string, size uint64, interval time.Duration) error {
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

func (q *RedisQuota) refresh(ctx context.Context, quotaID string, size uint64, interval time.Duration) error {
	client := (*redis.Client)(q)
	return client.SetEx(ctx, quotaID, size, interval).Err()
}

func (q *RedisQuota) Publish(ctx context.Context, key string, message Message) error {
	client := (*redis.Client)(q)
	return client.Publish(ctx, key, message.String()).Err()
}

func (q *RedisQuota) Subscribe(ctx context.Context, messages chan Message, keys ...string) error {
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
			messages <- msg
		case <-ctx.Done():
			return nil
		}
	}
}
