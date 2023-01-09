package main

import (
	"context"
	"fmt"
	"os"
	"os/signal"
	"strings"
	"time"

	"cloud.google.com/go/firestore"
	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	_ "github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/harness/init"
	_ "github.com/apache/beam/sdks/v2/go/pkg/beam/io/filesystem/gcs"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/log"
	"github.com/apache/beam/stitch/translation/internal/firestorex"
	"github.com/apache/beam/stitch/translation/internal/runner"
)

const (
	jobsPath                 = "jobs"
	jobServiceEnvironmentKey = "JOB_SERVICE"
	projectIdEnvironmentKey  = "PROJECT"

	pollDuration = time.Second
)

var (
	client *firestore.Client

	jobs *firestore.CollectionRef

	watcher *firestorex.Watcher

	jobService = os.Getenv(jobServiceEnvironmentKey)
	projectId  = os.Getenv(projectIdEnvironmentKey)

	requiredEnvVars = []string{
		jobServiceEnvironmentKey,
		projectIdEnvironmentKey,
	}
)

func init() {
	ctx := context.Background()
	if err := env(ctx); err != nil {
		log.Fatal(ctx, err)
	}
	if err := vars(ctx); err != nil {
		log.Fatal(ctx, err)
	}
}

func env(ctx context.Context) error {
	var missing []string
	for _, k := range requiredEnvVars {
		v := os.Getenv(k)
		vv := fmt.Sprintf("%s=%s", k, v)
		if v == "" {
			missing = append(missing, vv)
		}
		log.Infof(ctx, vv)
	}
	if len(missing) > 0 {
		return fmt.Errorf("missing environment variables: %s", strings.Join(missing, "; "))
	}
	return nil
}

func vars(ctx context.Context) error {
	var err error

	client, err = firestore.NewClient(ctx, projectId)
	if err != nil {
		return err
	}

	jobs = client.Collection(jobsPath)
	dispatcher, err := firestorex.NewPipelineDispatcher(client, jobService, runner.DefaultJobOptions)
	if err != nil {
		return err
	}

	watcher, err = firestorex.NewWatcher(ctx, jobs, pollDuration, dispatcher)
	if err != nil {
		return err
	}

	return nil
}

func main() {
	beam.Init()
	ctx := context.Background()
	if err := run(ctx); err != nil {
		log.Fatal(ctx, err)
	}
}

func run(ctx context.Context) error {
	ctx, cancel := signal.NotifyContext(ctx, os.Interrupt)
	defer cancel()
	go func() {
		if err := watcher.Watch(ctx); err != nil {
			log.Error(ctx, err)
			cancel()
		}
	}()
	for {
		select {
		case <-ctx.Done():
			_ = client.Close()
			log.Infof(ctx, "shutting down watcher...")
			return nil
		}
	}
}
