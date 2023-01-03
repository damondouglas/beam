package main

import (
	"context"
	"log"

	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/textio"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/options/jobopts"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/runners/universal/runnerlib"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/transforms/stats"
	"github.com/apache/beam/stitch/translation/internal/translation"
	"github.com/apache/beam/stitch/translation/internal/wordcount"

	_ "github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/harness/init"
	_ "github.com/apache/beam/sdks/v2/go/pkg/beam/io/filesystem/gcs"
)

var (
	source   = "gs://apache-beam-samples/shakespeare/*"
	output   = "gs://6567d1de-5fe2-46de-b420-e1b8631e1cb0/output"
	endpoint = "localhost:8099"
)

func main() {
	beam.Init()
	if err := run(context.Background()); err != nil {
		log.Fatalln(err)
	}
}

func run(ctx context.Context) error {
	p := wordcountPipeline()
	pp, err := translation.Encode(p)
	if err != nil {
		return err
	}

	opt := &runnerlib.JobOptions{
		Name:        jobopts.GetJobName(),
		Experiments: jobopts.GetExperiments(),
		//Worker:       *jobopts.WorkerBinary,
		RetainDocker: *jobopts.RetainDockerContainers,
		Parallelism:  *jobopts.Parallelism,
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
