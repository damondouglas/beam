package main

import (
	"context"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/graphx"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/xlangx"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/typex"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/util/reflectx"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/textio"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/options/jobopts"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/runners/universal/runnerlib"
	"log"

	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	_ "github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/harness/init"
	_ "github.com/apache/beam/sdks/v2/go/pkg/beam/io/filesystem/gcs"
)

var (
	source = "gs://apache-beam-samples/shakespeare/*"
	//output   = "gs://6567d1de-5fe2-46de-b420-e1b8631e1cb0/output"
	output   = "gs://16fc1003-5311-4d63-b01a-cbb2f62a8e23/output"
	endpoint = "localhost:8099"

	expansionAddress = "localhost:8080"

	worker = "localhost:58721"
)

func main() {
	beam.Init()
	if err := run(context.Background()); err != nil {
		log.Fatalln(err)
	}
}

func run(ctx context.Context) error {
	*jobopts.EnvironmentType = "external"
	//srv, err := extworker.StartLoopback(ctx, 0)
	//if err != nil {
	//	return err
	//}
	//defer srv.Stop(ctx)
	//getEnvCfg := srv.EnvironmentConfig
	getEnvCfg := func(ctx context.Context) string {
		return worker
	}
	envUrn := jobopts.GetEnvironmentUrn(ctx)

	p := buildPipeline()

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
}

func buildPipeline() *beam.Pipeline {
	p, s := beam.NewPipelineWithRoot()

	readPl := beam.CrossLanguagePayload(&struct {
		FilenamePrefix string
	}{
		FilenamePrefix: source,
	})

	readIn := beam.UnnamedInput(beam.Impulse(s))
	readOutT := beam.UnnamedOutput(typex.New(reflectx.String))
	read := beam.CrossLanguage(s, "beam:transform:org.apache.beam.stitch:file:read:v1", readPl, expansionAddress, readIn, readOutT)

	textio.Write(s, output, read[beam.UnnamedOutputTag()])
	return p
}
