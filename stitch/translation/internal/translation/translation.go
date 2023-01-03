package translation

import (
	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/graphx"
	pipeline "github.com/apache/beam/sdks/v2/go/pkg/beam/model/pipeline_v1"
)

func Encode(p *beam.Pipeline) (*pipeline.Pipeline, error) {
	edges, _, err := p.Build()
	if err != nil {
		return nil, err
	}

	return graphx.Marshal(edges, &graphx.Options{})
}
