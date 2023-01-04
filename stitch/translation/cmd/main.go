package main

import (
	"context"
	"log"

	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/graphx"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/xlangx"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/textio"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/options/jobopts"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/runners/universal/extworker"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/runners/universal/runnerlib"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/transforms/stats"
	"github.com/apache/beam/stitch/translation/internal/wordcount"

	_ "github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/harness/init"
	_ "github.com/apache/beam/sdks/v2/go/pkg/beam/io/filesystem/gcs"
)

var (
	source   = "gs://apache-beam-samples/shakespeare/*"
	output   = "gs://6567d1de-5fe2-46de-b420-e1b8631e1cb0/output"
	endpoint = "localhost:8099"
	worker   = "localhost:39311"
)

func main() {
	beam.Init()
	if err := run(context.Background()); err != nil {
		log.Fatalln(err)
	}
}

func run(ctx context.Context) error {
	*jobopts.EnvironmentType = "LOOPBACK"
	srv, err := extworker.StartLoopback(ctx, 0)
	if err != nil {
		return err
	}
	defer srv.Stop(ctx)
	getEnvCfg := srv.EnvironmentConfig
	envUrn := jobopts.GetEnvironmentUrn(ctx)
	p := wordcountPipeline()
	edges, _, err := p.Build()
	if err != nil {
		return err
	}
	xlangx.ResolveArtifacts(ctx, edges, nil)
	if err != nil {
		return err
	}
	environment, err := graphx.CreateEnvironment(ctx, envUrn, getEnvCfg)
	if err != nil {
		return err
	}

	pp, err := graphx.Marshal(edges, &graphx.Options{Environment: environment})
	if err != nil {
		return err
	}

	opt := &runnerlib.JobOptions{
		Name:        jobopts.GetJobName(),
		Experiments: jobopts.GetExperiments(),
		Parallelism: 1,
	}

	_, err = runnerlib.Execute(ctx, pp, endpoint, opt, *jobopts.Async)

	return err
	//return beamx.Run(ctx, p)
}
func wordcountPipeline() *beam.Pipeline {
	p, s := beam.NewPipelineWithRoot()

	lines := textio.Read(s.Scope("ReadFromSource"), source)

	tokens := wordcount.Split(s, lines)

	nonEmptyTokens := wordcount.ExcludeEmpty(s, tokens)

	lowerCaseTokens := wordcount.ToLower(s, nonEmptyTokens)

	countPerToken := stats.Count(s.Scope("CountPerToken"), lowerCaseTokens)

	formatCounts := wordcount.FormatCounts(s, countPerToken)

	textio.Write(s.Scope("OutputResult"), output, formatCounts)

	return p
}
