package main

import (
	"cloud.google.com/go/dataflow/apiv1beta3/dataflowpb"
	"cloud.google.com/go/logging"
	run "cloud.google.com/go/run/apiv2"
	"cloud.google.com/go/run/apiv2/runpb"
	"context"
	"errors"
	"fmt"
	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/encoding/eventarc"
	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/environment"
	cloudevent "github.com/cloudevents/sdk-go/v2"
	"github.com/google/uuid"
	dataflow "github.com/googleapis/google-cloudevents-go/cloud/dataflowdatav1beta3"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"strings"
)

const (
	kKeyPrefix   = "keyprefix"
	kReadOrWrite = "readorwrite"
	kConnector   = "connector"

	kJobNameTmpl = "projects/%s/locations/%s/jobs/rrio-bigtable-it-%s-read"

	kWrite = "write"

	kBigTableKeyModifier = "BIG_TABLE_KEY_MODIFIER"
	kKeyPrefixDelim      = "__"
)

var (
	port       environment.Variable = "PORT"
	project    environment.Variable = "PROJECT"
	region     environment.Variable = "REGION"
	closers    []func() error
	logger     *logging.Logger
	jobsClient *run.JobsClient
)

func init() {
	if err := initE(context.Background()); err != nil {
		panic(err)
	}
}

func initE(ctx context.Context) error {
	if err := port.Default("8080"); err != nil {
		return err
	}
	if err := environment.Missing(project, region); err != nil {
		return err
	}
	lc, err := logging.NewClient(ctx, fmt.Sprintf("projects/%s", project.Value()))
	if err != nil {
		return err
	}
	closers = append(closers, lc.Close)

	logger = lc.Logger(".test-infra/pipelines/src/main/go/cmd/eventarc")

	rc, err := run.NewJobsClient(ctx)
	if err != nil {
		return err
	}
	closers = append(closers, rc.Close)

	jobsClient = rc

	return nil
}

func main() {
	defer func() {
		var errs []error
		for _, closer := range closers {
			if err := closer(); err != nil {
				errs = append(errs, err)
			}
		}
		if err := errors.Join(errs...); err != nil {
			panic(err)
		}
	}()
	if err := runE(context.Background()); err != nil {
		panic(err)
	}
}

func runE(ctx context.Context) error {
	ctx, cancel := signal.NotifyContext(ctx, os.Interrupt)
	defer cancel()
	errChan := make(chan error)
	http.HandleFunc("/", handleEvents)
	portVal, err := strconv.Atoi(port.Value())
	if err != nil {
		return err
	}
	addr := fmt.Sprintf(":%v", portVal)
	logger.Log(logging.Entry{
		Severity: logging.Info,
		Payload:  fmt.Sprintf("started listening at %s", addr),
	})
	go func() {
		if err := http.ListenAndServe(addr, nil); err != nil {
			errChan <- err
		}
	}()
	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return nil
	}
}

func handleEvents(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Expected HTTP POST request with CloudEvent payload", http.StatusMethodNotAllowed)
		return
	}

	session := label{
		key:   "session",
		value: uuid.NewString(),
	}

	event, err := cloudevent.NewEventFromHTTPRequest(r)
	if err != nil {
		logErrorf(fmt.Errorf("cloudevent.NewEventFromHTTPRequest: %v", err).Error(), session)
		http.Error(w, "Failed to create CloudEvent from request", http.StatusBadRequest)
		return
	}
	logger.Log(logging.Entry{
		Severity: logging.Debug,
		Payload:  event,
		Labels: map[string]string{
			"stage":     "cloudevent.NewEventFromHTTPRequest",
			"input":     r.RequestURI,
			session.key: session.value,
		},
	})

	jobData, err := eventarc.NewDecoder(event).Decode()
	if err != nil {
		logErrorf(fmt.Errorf("error Decode(): %v", err).Error(), session)
		http.Error(w, "Failed to marshal event data", http.StatusInternalServerError)
		return
	}

	logger.Log(logging.Entry{
		Severity: logging.Debug,
		Payload:  jobData,
		Labels: map[string]string{
			"stage":     "Decode",
			"input":     event.DataSchema(),
			session.key: session.value,
		},
	})

	if err := handleJob(r.Context(), jobData.Payload, session); err != nil {
		logErrorf(fmt.Errorf("handleJob err: %v", err).Error(), session)
		http.Error(w, "Failed to handle jobData", http.StatusInternalServerError)
		return
	}

	_, _ = fmt.Fprintf(w, http.StatusText(http.StatusOK))
}

func handleJob(ctx context.Context, job *dataflow.Job, labels ...label) error {
	if job == nil {
		return fmt.Errorf("job is nil")
	}
	if job.CurrentState.String() != dataflowpb.JobState_JOB_STATE_DONE.String() {
		logger.Log(logging.Entry{
			Severity: logging.Debug,
			Payload:  fmt.Sprintf("ignoring job: %s, status: %s", job.Id, job.CurrentState),
			Labels:   makeLabels(labels...),
		})
		return nil
	}

	readOrWrite, ok := job.Labels[kReadOrWrite]
	if !ok {
		return fmt.Errorf("missing label: %s for job: %s", kReadOrWrite, job.Id)
	}

	if readOrWrite != kWrite {
		logger.Log(logging.Entry{
			Severity: logging.Debug,
			Payload:  fmt.Sprintf("ignoring job: %s, status: %s, readOrWrite: %s", job.Id, job.CurrentState, readOrWrite),
			Labels:   makeLabels(labels...),
		})
		return nil
	}

	keyPrefix, ok := job.Labels[kKeyPrefix]
	if !ok {
		return fmt.Errorf("missing label: %s for job: %s", kKeyPrefix, job.Id)
	}

	toks := strings.Split(keyPrefix, kKeyPrefixDelim)
	if len(toks) != 2 {
		return fmt.Errorf("label: %s in job: %s fails to follow the expected format <connector>__<prefix modifier> where <prefix modifier> is usually some UUID", keyPrefix, job.Id)
	}

	prefixModifier := toks[1]

	connector, ok := job.Labels[kConnector]
	if !ok {
		return fmt.Errorf("missing label: %s for job: %s", kConnector, job.Id)
	}

	req := &runpb.RunJobRequest{
		Name: fmt.Sprintf(kJobNameTmpl, project.Value(), region.Value(), connector),
		Overrides: &runpb.RunJobRequest_Overrides{
			ContainerOverrides: []*runpb.RunJobRequest_Overrides_ContainerOverride{
				{
					Env: []*runpb.EnvVar{
						{
							Name: kBigTableKeyModifier,
							Values: &runpb.EnvVar_Value{
								Value: prefixModifier,
							},
						},
					},
				},
			},
		},
	}

	logger.Log(logging.Entry{
		Severity: logging.Debug,
		Payload: map[string]interface{}{
			"cloud_run_request": req,
			"dataflow_job":      job,
		},
		Labels: makeLabels(labels...),
	})

	op, err := jobsClient.RunJob(ctx, req)
	if err != nil {
		return fmt.Errorf("error running Cloud Run job: %s from Dataflow job: %s, err: %w", req.Name, job.Id, err)
	}

	logger.Log(logging.Entry{
		Severity: logging.Info,
		Payload: map[string]interface{}{
			"message":           "triggered Cloud Run job",
			"cloud_run_request": req,
			"dataflow_job":      job,
			"cloud_run_op":      op,
		},
		Labels: makeLabels(labels...),
	})

	return nil
}

func logErrorf(message interface{}, labels ...label) {
	logger.Log(logging.Entry{
		Severity: logging.Error,
		Payload:  message,
		Labels:   makeLabels(labels...),
	})
}

type label struct {
	key   string
	value string
}

func makeLabels(l ...label) map[string]string {
	result := map[string]string{}
	for _, kv := range l {
		result[kv.key] = kv.value
	}
	return result
}
