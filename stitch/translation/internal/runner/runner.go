package runner

import (
	"context"

	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/graphx"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/xlangx"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/options/jobopts"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/runners/universal/extworker"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/runners/universal/runnerlib"
)

var (
	DefaultJobOptions = &runnerlib.JobOptions{
		Name:        jobopts.GetJobName(),
		Experiments: jobopts.GetExperiments(),
		Parallelism: 1,
	}
)

type Translator interface {
	Translate(ctx context.Context) (*beam.Pipeline, error)
}

type Runner struct {
	jobServiceEndpoint string
	opt                *runnerlib.JobOptions
	translator         Translator
}

func New(jobServiceEndpoint string, opt *runnerlib.JobOptions, translator Translator) *Runner {
	return &Runner{
		jobServiceEndpoint: jobServiceEndpoint,
		opt:                opt,
		translator:         translator,
	}
}

func (r *Runner) Run(ctx context.Context) (string, error) {
	*jobopts.EnvironmentType = "LOOPBACK"
	srv, err := extworker.StartLoopback(ctx, 0)
	if err != nil {
		return "", err
	}
	defer srv.Stop(ctx)
	getEnvCfg := srv.EnvironmentConfig
	envUrn := jobopts.GetEnvironmentUrn(ctx)

	p, err := r.translator.Translate(ctx)
	if err != nil {
		return "", err
	}

	edges, _, err := p.Build()
	if err != nil {
		return "", err
	}
	xlangx.ResolveArtifacts(ctx, edges, nil)
	if err != nil {
		return "", err
	}

	environment, err := graphx.CreateEnvironment(ctx, envUrn, getEnvCfg)
	if err != nil {
		return "", err
	}

	pp, err := graphx.Marshal(edges, &graphx.Options{Environment: environment})
	if err != nil {
		return "", err
	}

	result, err := runnerlib.Execute(ctx, pp, r.jobServiceEndpoint, r.opt, *jobopts.Async)
	if err != nil {
		return "", err
	}
	return result.JobID(), nil
}
