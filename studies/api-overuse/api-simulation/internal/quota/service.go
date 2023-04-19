package quota

import (
	"context"
	"fmt"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/cache"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/job"
	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/logging"
	quota_v1 "github.com/apache/beam/studies/api-overuse/api-simulation/internal/proto/quota/v1"
	"google.golang.org/grpc"
)

const (
	jobName = "refresher-service"
)

type RefresherServiceSpec job.Spec

func (spec *RefresherServiceSpec) isValid() error {
	if spec.Image == "" {
		return fmt.Errorf("%T.Image property is required", spec)
	}
	return nil
}

type ServiceSpec struct {
	RefresherServiceSpec *RefresherServiceSpec
	Cache                cache.Quota
	Publisher            cache.Publisher
	JobsClient           *job.Jobs
}

func (spec *ServiceSpec) isValid() error {
	for _, prop := range []interface{}{
		spec.RefresherServiceSpec,
		spec.Cache,
		spec.Publisher,
		spec.JobsClient,
	} {
		if prop == nil {
			return fmt.Errorf("%T is required but nil", prop)
		}
	}
	return spec.RefresherServiceSpec.isValid()
}

func RegisterService(ctx context.Context, server *grpc.Server, spec *ServiceSpec) error {
	if spec.RefresherServiceSpec.Labels == nil {
		spec.RefresherServiceSpec.Labels = map[string]string{}
	}
	if err := spec.isValid(); err != nil {
		return err
	}
	svc := &quotaService{
		logger: logging.Default.WithName("quota-service"),
		spec:   spec,
	}

	quota_v1.RegisterQuotaServiceServer(server, svc)

	return nil
}

type quotaService struct {
	quota_v1.UnimplementedQuotaServiceServer
	logger logging.Logger
	spec   *ServiceSpec
}

func (q *quotaService) Create(ctx context.Context, request *quota_v1.CreateQuotaRequest) (*quota_v1.CreateQuotaResponse, error) {
	q.logger.Debug(ctx, map[string]interface{}{
		"request": request,
	})

	qq := request.Quota

	spec := (*job.Spec)(q.spec.RefresherServiceSpec)
	spec.Name = fmt.Sprintf("%s-%s", jobName, qq.Id)

	j, err := q.spec.JobsClient.Start(ctx, spec)
	if err != nil {
		q.logger.Error(ctx, map[string]interface{}{
			"message": err.Error(),
			"request": request,
			"jobSpec": spec,
		})
		return nil, err
	}

	q.logger.Debug(ctx, map[string]interface{}{
		"message": "created refresher-service job",
		"job":     j,
	})

	return &quota_v1.CreateQuotaResponse{}, nil
}

func (q *quotaService) List(ctx context.Context, request *quota_v1.ListQuotasRequest) (*quota_v1.ListQuotasResponse, error) {
	return nil, fmt.Errorf("not yet implemented")
}

func (q *quotaService) Delete(ctx context.Context, request *quota_v1.DeleteQuotaRequest) (*quota_v1.DeleteQuotaResponse, error) {
	q.logger.Info(ctx, map[string]interface{}{
		"request": request,
	})

	payload := []byte(fmt.Sprintf("%s:%s", request.Id, "delete"))

	if err := q.spec.Publisher.Publish(ctx, request.Id, payload); err != nil {
		return nil, fmt.Errorf("error publishing delete request key: %s, payload: %s, err %w", request.Id, string(payload), err)
	}

	q.logger.Debug(ctx, map[string]interface{}{
		"message": "published deletion request",
		"key":     request.Id,
		"payload": string(payload),
	})

	return &quota_v1.DeleteQuotaResponse{
		Quota: &quota_v1.Quota{
			Id: request.Id,
		},
	}, nil
}

func (q *quotaService) Describe(ctx context.Context, request *quota_v1.DescribeQuotaRequest) (*quota_v1.DescribeQuotaResponse, error) {
	return nil, fmt.Errorf("not yet implemented")
}
