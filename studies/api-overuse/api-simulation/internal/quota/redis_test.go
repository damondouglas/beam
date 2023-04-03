package quota

import (
	"context"
	"strconv"
	"testing"
	"time"

	"github.com/alicebob/miniredis/v2"
	"github.com/redis/go-redis/v9"
)

func TestRedisQuota_Alive(t *testing.T) {
	s := miniredis.RunT(t)
	defer s.Close()
	type args struct {
		ctx context.Context
	}
	tests := []struct {
		name    string
		q       *RedisQuota
		args    args
		wantErr bool
	}{
		{
			name: "with correct addr should be available",
			q: (*RedisQuota)(redis.NewClient(&redis.Options{
				Addr: s.Addr(),
			})),
			args: args{
				ctx: context.Background(),
			},
			wantErr: false,
		},
		{
			name: "with incorrect addr should not be available",
			q: (*RedisQuota)(redis.NewClient(&redis.Options{
				Addr: "localhost:1234",
			})),
			args: args{
				ctx: context.Background(),
			},
			wantErr: true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if err := tt.q.Alive(tt.args.ctx); (err != nil) != tt.wantErr {
				t.Errorf("Alive() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}

func TestRedisQuota_refresh(t *testing.T) {
	s := miniredis.RunT(t)
	defer s.Close()

	q := (*RedisQuota)(redis.NewClient(&redis.Options{
		Addr: s.Addr(),
	}))

	type state struct {
		quotaID   string
		size      uint64
		timestamp time.Time
		ttl       time.Duration
	}

	type args struct {
		timestamp time.Time
		quotaID   string
		size      uint64
		interval  time.Duration
	}

	tests := []struct {
		name    string
		before  *state
		args    args
		after   *state
		wantErr bool
	}{
		{
			name: "initialization at start sets value with ttl",
			args: args{
				timestamp: time.UnixMilli(0),
				quotaID:   "a",
				size:      100,
				interval:  time.Second,
			},
			after: &state{
				quotaID:   "a",
				size:      100,
				timestamp: time.UnixMilli(0),
				ttl:       time.Second,
			},
			wantErr: false,
		},
		{
			name: "set pre-existing overwrites value and ttl",
			before: &state{
				quotaID:   "a",
				size:      50,
				timestamp: time.UnixMilli(500),
				ttl:       500 * time.Millisecond,
			},
			args: args{
				timestamp: time.UnixMilli(600),
				quotaID:   "a",
				size:      100,
				interval:  time.Second,
			},
			after: &state{
				timestamp: time.UnixMilli(600),
				quotaID:   "a",
				size:      100,
				ttl:       time.Second,
			},
			wantErr: false,
		},
		{
			name: "value expires after refresh and no update",
			args: args{
				timestamp: time.UnixMilli(0),
				quotaID:   "a",
				size:      100,
				interval:  time.Second,
			},
			after: &state{
				quotaID:   "a",
				size:      0,
				timestamp: time.UnixMilli(0).Add(time.Second + time.Millisecond),
				ttl:       0,
			},
			wantErr: false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			s.FlushDB()
			ctx := context.Background()
			if tt.after == nil && !tt.wantErr {
				t.Fatal("define after in wantErr = false test cases")
			}
			if tt.before != nil {
				s.SetTime(tt.before.timestamp)
				if err := s.Set(tt.before.quotaID, strconv.FormatUint(tt.before.size, 10)); err != nil {
					t.Fatal(err)
				}
				s.SetTTL(tt.before.quotaID, tt.before.ttl)
			}
			s.SetTime(tt.args.timestamp)
			if err := q.refresh(ctx, tt.args.quotaID, tt.args.size, tt.args.interval); (err != nil) != tt.wantErr {
				t.Errorf("refresh() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if tt.wantErr {
				return
			}
			s.FastForward(tt.after.timestamp.Sub(tt.args.timestamp))
			s.SetTime(tt.after.timestamp)
			ttl := s.TTL(tt.after.quotaID)
			var got uint64
			if ttl > 0 {
				gotStr, err := s.Get(tt.after.quotaID)
				if err != nil {
					t.Fatal("unexpected error assessing after state: ", err)
				}
				got, err = strconv.ParseUint(gotStr, 10, 64)
				if err != nil {
					t.Fatal("unexpected error assessing after state: ", err)
				}
			}
			if got != tt.after.size {
				t.Errorf("refresh(%+v) = %+v, want %+v", tt.args, got, tt.after)
			}
		})
	}
}

func TestRedisQuota_Decrement(t *testing.T) {
	ctx := context.Background()
	s := miniredis.RunT(t)
	defer s.Close()

	q := (*RedisQuota)(redis.NewClient(&redis.Options{
		Addr: s.Addr(),
	}))

	interval := time.Second

	type state struct {
		quotaID   string
		size      uint64
		timestamp time.Time
	}

	type args struct {
		timestamp time.Time
		queueID   string
	}

	tests := []struct {
		name    string
		before  *state
		args    args
		want    uint64
		wantErr bool
	}{
		{
			name:   "decrement of non-existing key returns error",
			before: nil,
			args: args{
				timestamp: time.UnixMilli(0),
				queueID:   "a",
			},
			want:    0,
			wantErr: true,
		},
		{
			name: "decrement of key when > 0 ok",
			before: &state{
				quotaID:   "a",
				size:      1,
				timestamp: time.UnixMilli(0),
			},
			args: args{
				timestamp: time.UnixMilli(1),
				queueID:   "a",
			},
			want:    0,
			wantErr: false,
		},
		{
			name: "decrement of expired key returns error",
			before: &state{
				quotaID:   "a",
				size:      100,
				timestamp: time.UnixMilli(0),
			},
			args: args{
				timestamp: time.UnixMilli(0).Add(interval),
				queueID:   "a",
			},
			want:    0,
			wantErr: true,
		},
		{
			name: "decrement of key=0 returns error",
			before: &state{
				quotaID:   "a",
				size:      0,
				timestamp: time.UnixMilli(0),
			},
			args: args{
				timestamp: time.UnixMilli(0),
				queueID:   "a",
			},
			want:    0,
			wantErr: true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			s.FlushDB()
			if tt.before != nil {
				s.SetTime(tt.before.timestamp)
				if err := s.Set(tt.before.quotaID, strconv.FormatUint(tt.before.size, 10)); err != nil {
					t.Fatal(err)
				}
				s.SetTTL(tt.before.quotaID, interval)
				s.FastForward(tt.args.timestamp.Sub(tt.before.timestamp))
			}
			s.SetTime(tt.args.timestamp)
			err := q.Decrement(ctx, tt.args.queueID)
			if err != nil && !tt.wantErr {
				t.Errorf("Decrement() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if tt.wantErr {
				return
			}
			gotStr, err := s.Get(tt.args.queueID)
			if err != nil {
				t.Fatal(err)
			}
			got, err := strconv.ParseUint(gotStr, 10, 64)
			if err != nil {
				t.Fatal(err)
			}
			if got != tt.want {
				t.Errorf("Decrement() = %v, want %v", got, tt.want)
			}
		})
	}
}
