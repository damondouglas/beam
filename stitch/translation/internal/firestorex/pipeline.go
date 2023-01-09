package firestorex

import (
	"context"

	"cloud.google.com/go/firestore"
	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/stitch/translation/internal/xlangx"
)

type withBeamPipelineOpt beam.Pipeline
type withScopeOpt beam.Scope

type Option interface {
	apply(pipeline *Pipeline)
}

func WithBeamPipeline(p *beam.Pipeline) Option {
	return (*withBeamPipelineOpt)(p)
}

func WithScope(s beam.Scope) Option {
	return (withScopeOpt)(s)
}

func (opt withBeamPipelineOpt) apply(pipeline *Pipeline) {
	pipeline.beamPipeline = (*beam.Pipeline)(&opt)
}

func (opt withScopeOpt) apply(pipeline *Pipeline) {
	pipeline.scope = (beam.Scope)(opt)
}

type Pipeline struct {
	Steps        []*Step `firestore:"steps"`
	beamPipeline *beam.Pipeline
	scope        beam.Scope
}

func loadPipeline(ctx context.Context, doc *firestore.DocumentRef, opts ...Option) (*Pipeline, error) {
	var result *Pipeline
	snap, err := doc.Get(ctx)
	if err != nil {
		return nil, err
	}
	if err := snap.DataTo(&result); err != nil {
		return nil, err
	}

	for _, opt := range opts {
		opt.apply(result)
	}

	if result.beamPipeline == nil {
		result.beamPipeline, result.scope = beam.NewPipelineWithRoot()
	}

	if &result.scope == nil {
		result.scope = result.beamPipeline.Root()
	}

	return result, nil
}

func (p *Pipeline) Translate(ctx context.Context) (*beam.Pipeline, error) {
	for _, step := range p.Steps {
		step.p = p
		fields, err := step.expansionConfigurationFields(ctx)
		if err != nil {
			return nil, err
		}
		dynamicStruct := xlangx.NewDynamicStruct(fields...)
		step.populateConfigurationValues(dynamicStruct)
		pl := dynamicStruct.CrossLanguagePayload()
		in := step.beamInput(p.scope)
		out, err := step.beamOutputType(ctx)
		if err != nil {
			return nil, err
		}
		expansion, err := step.getOrLoadExpansion(ctx)
		if err != nil {
			return nil, err
		}
		step.beamOutput, err = beam.TryCrossLanguage(p.scope, step.Expansion.ID, pl, expansion.Host, in, out)
		if err != nil {
			return nil, err
		}
	}
	return p.beamPipeline, nil
}
