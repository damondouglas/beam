package quota

import (
	"context"
	"fmt"
	"time"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/cache"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
	quota_v1 "github.com/apache/beam/studies/api-overuse/api-simulation/internal/proto/quota/v1"
	"google.golang.org/grpc"
)

const (
	create    = "create"
	delete    = "delete"
	delimiter = ":"
)

type createMessage string

type deleteMessage string

func RegisterService(ctx context.Context, server *grpc.Server, quotaRefresher cache.Refresher, publisher cache.Publisher, subscriber cache.Subscriber, logger logging.Logger) error {
	for _, itr := range []interface{}{
		quotaRefresher,
		subscriber,
		publisher,
	} {
		if itr == nil {
			return fmt.Errorf("fatal: RegisterService requires: %T, %T, %T", quotaRefresher, publisher, subscriber)
		}
	}
	if logger == nil {
		logger = logging.Default
	}
	svc := &quotaService{
		logger:         logger,
		quotaRefresher: quotaRefresher,
		publisher:      publisher,
		subscriber:     subscriber,
		createRequests: make(chan *quota_v1.Quota),
	}
	if err := svc.quotaRefresher.Alive(ctx); err != nil {
		return err
	}
	svc.logger.Info(ctx, map[string]interface{}{
		"message":        "registered quota service",
		"logger":         fmt.Sprintf("%T", svc.logger),
		"cache":          fmt.Sprintf("%T", svc.quotaRefresher),
		"quotaRefresher": fmt.Sprintf("%T", svc.quotaRefresher),
		"publisher":      fmt.Sprintf("%T", svc.publisher),
		"subscriber":     fmt.Sprintf("%T", svc.subscriber),
	})
	quota_v1.RegisterQuotaServiceServer(server, svc)

	return nil
}

type quotaService struct {
	quota_v1.UnimplementedQuotaServiceServer
	logger         logging.Logger
	quotaRefresher cache.Refresher
	publisher      cache.Publisher
	subscriber     cache.Subscriber
	createRequests chan *quota_v1.Quota
}

func (q *quotaService) Watch(ctx context.Context) error {
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()
	errChan := make(chan error)
	creates := make(chan cache.Message)
	deletes := make(chan cache.Message)
	stops := map[string]chan bool{}

	for {
		select {
		case qq := <-q.createRequests:
			stop := make(chan bool)
			stops[qq.Id] = stop
			go q.createAndRefresh(ctx, qq, errChan, stop)
		case err := <-errChan:
			return err
		case <-ctx.Done():
			return nil
		}
	}
}

func (q *quotaService) createAndRefresh(ctx context.Context, qq *quota_v1.Quota, errChan chan error, stop chan bool) {
	if qq.Size <= 0 {
		errChan <- fmt.Errorf("quota size must be > 0, got: %v, for: %s", qq.Size, qq.Id)
		return
	}
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()
	size := uint64(qq.Size)
	interval := time.Duration(qq.RefreshMillisecondsInterval)
	go func() {
		if err := q.quotaRefresher.InitializeAndRefreshPerInterval(ctx, qq.Id, size, interval); err != nil {
			errChan <- err
			return
		}
	}()
	for {
		select {
		case <-ctx.Done():
			return
		case <-stop:
			return
		}
	}
}

func (q *quotaService) Create(ctx context.Context, request *quota_v1.CreateQuotaRequest) (*quota_v1.CreateQuotaResponse, error) {
	q.logger.Info(ctx, map[string]interface{}{
		"request": request,
	})
	q.createRequests <- request.Quota

	return nil, nil
}

func (q *quotaService) List(ctx context.Context, request *quota_v1.ListQuotasRequest) (*quota_v1.ListQuotasResponse, error) {
	return nil, fmt.Errorf("not yet implemented")
}

func (q *quotaService) Delete(ctx context.Context, request *quota_v1.DeleteQuotaRequest) (*quota_v1.DeleteQuotaResponse, error) {
	q.logger.Info(ctx, map[string]interface{}{
		"request": request,
	})
	return nil, nil
}

func (q *quotaService) Describe(ctx context.Context, request *quota_v1.DescribeQuotaRequest) (*quota_v1.DescribeQuotaResponse, error) {
	return nil, fmt.Errorf("not yet implemented")
}

func (msg deleteMessage) String() string {
	return fmt.Sprintf("%s%s%s", string(msg), delimiter, delete)
}

func (msg createMessage) String() string {
	return fmt.Sprintf("%s%s%s", string(msg), delimiter, create)
}
