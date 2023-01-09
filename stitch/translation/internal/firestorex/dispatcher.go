package firestorex

import (
	"context"
	"fmt"
	"time"

	"cloud.google.com/go/firestore"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/log"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/runners/universal/runnerlib"
	"github.com/apache/beam/stitch/translation/internal/runner"
)

const (
	jobCanceled  = "CANCEL"
	jobDone      = "DONE"
	jobError     = "ERROR"
	jobRunning   = "RUNNING"
	jobSubmitted = "SUBMITTED"
)

type Job struct {
	ref         *firestore.DocumentRef
	Pipeline    *firestore.DocumentRef `firestore:"pipeline"`
	State       string                 `firestore:"state"`
	LastUpdated time.Time              `firestore:"lastUpdated"`
	Result      string                 `firestore:"result"`
}

func loadJob(ctx context.Context, ref *firestore.DocumentRef) (*Job, error) {
	var job *Job
	snap, err := ref.Get(ctx)
	if err != nil {
		return nil, err
	}
	if err := snap.DataTo(&job); err != nil {
		return nil, err
	}
	job.ref = ref
	return job, nil
}

type Dispatcher interface {
	Dispatch(ctx context.Context, doc *firestore.DocumentSnapshot) error
}

type PipelineDispatcher struct {
	client             *firestore.Client
	opts               []Option
	jobServiceEndpoint string
	jobOpts            *runnerlib.JobOptions
}

func NewPipelineDispatcher(client *firestore.Client, jobServiceEndpoint string, jobOpts *runnerlib.JobOptions, opts ...Option) (*PipelineDispatcher, error) {
	if client == nil {
		return nil, fmt.Errorf("client is nil")
	}
	if jobServiceEndpoint == "" {
		return nil, fmt.Errorf("jobServiceEndpoint is empty")
	}
	if jobOpts == nil {
		return nil, fmt.Errorf("jobopts is nil")
	}

	return &PipelineDispatcher{
		client:             client,
		opts:               opts,
		jobServiceEndpoint: jobServiceEndpoint,
		jobOpts:            jobOpts,
	}, nil
}

func (dispatcher *PipelineDispatcher) Dispatch(ctx context.Context, doc *firestore.DocumentSnapshot) error {
	ref := doc.Ref

	// block multiple dispatchers from access and update of the same job details
	if err := dispatcher.client.RunTransaction(ctx, func(ctx context.Context, tx *firestore.Transaction) error {
		var job *Job
		jobDoc, err := tx.Get(ref)
		if err != nil {
			return err
		}

		if err := jobDoc.DataTo(&job); err != nil {
			return err
		}

		if job.State != jobSubmitted {
			return nil
		}

		job.State = jobRunning
		job.ref = ref
		job.LastUpdated = time.Now()

		return tx.Set(ref, job)
	}); err != nil {
		return err
	}

	job, err := loadJob(ctx, ref)
	if err != nil {
		return err
	}
	return dispatcher.run(ctx, job)
}

func (dispatcher *PipelineDispatcher) run(ctx context.Context, job *Job) error {
	p, err := loadPipeline(ctx, job.Pipeline, dispatcher.opts...)
	if err != nil {
		return err
	}
	r := runner.New(dispatcher.jobServiceEndpoint, dispatcher.jobOpts, p)

	result, err := r.Run(ctx)
	if err != nil {
		log.Error(ctx, err)
		job.State = jobError
		job.LastUpdated = time.Now()
		job.Result = err.Error()
		if _, updateErr := job.ref.Set(ctx, job); updateErr != nil {
			return updateErr
		}
		return nil
	}

	job.State = jobDone
	job.LastUpdated = time.Now()
	job.Result = result

	_, err = job.ref.Set(ctx, job)
	return err
}
