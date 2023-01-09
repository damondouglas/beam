package firestorex

import (
	"context"
	"fmt"
	"reflect"

	"cloud.google.com/go/firestore"
	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/typex"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/util/reflectx"
	"github.com/apache/beam/stitch/translation/internal/xlangx"
)

const (
	noInputIndex = -1
)

var (
	typeRefMap = map[string]reflect.Type{
		"long":            reflectx.Int64,
		"pbegin":          nil,
		"pdone":           nil,
		"string":          reflectx.String,
		"kv[string,long]": reflect.TypeOf(typex.NewKV(typex.New(reflectx.String), typex.New(reflectx.Int64))),
	}
)

type Step struct {
	Configuration map[string]any         `firestore:"configuration"`
	Expansion     *firestore.DocumentRef `firestore:"expansion"`
	expansion     *Expansion
	Input         int `firestore:"input"`
	beamOutput    map[string]beam.PCollection
	p             *Pipeline
}

func (s *Step) beamInput(scope beam.Scope) map[string]beam.PCollection {
	if s.Input == noInputIndex {
		return beam.UnnamedInput(beam.Impulse(scope))
	}
	step := s.p.Steps[s.Input]
	return step.beamOutput
}

func (s *Step) beamOutputType(ctx context.Context) (map[string]typex.FullType, error) {
	e, err := s.getOrLoadExpansion(ctx)
	if err != nil {
		return nil, err
	}
	outTypeRef := e.Output
	outType, ok := typeRefMap[outTypeRef.ID]
	if !ok {
		return nil, fmt.Errorf("%s does not map to a known reflect.Type", outTypeRef.ID)
	}
	if outType != nil {
		return beam.UnnamedOutput(typex.New(outType)), nil
	}
	return nil, nil
}

func (s *Step) getOrLoadExpansion(ctx context.Context) (*Expansion, error) {
	if s.expansion != nil {
		return s.expansion, nil
	}
	var e *Expansion
	snap, err := s.Expansion.Get(ctx)
	if err != nil {
		return nil, err
	}
	if err := snap.DataTo(&e); err != nil {
		return nil, err
	}
	s.expansion = e
	return e, nil
}

func (s *Step) expansionConfigurationFields(ctx context.Context) ([]reflect.StructField, error) {
	var result []reflect.StructField

	e, err := s.getOrLoadExpansion(ctx)
	if err != nil {
		return nil, err
	}

	for k := range s.Configuration {
		typeRef, ok := e.Configuration[k]
		if !ok {
			return nil, fmt.Errorf("%+v and %+v misalignment at key: %s", s.Configuration, e.Configuration, k)
		}
		t, ok := typeRefMap[typeRef.ID]
		if !ok {
			return nil, fmt.Errorf("%s does not map to a known reflect.Type", typeRef.ID)
		}
		result = append(result, reflect.StructField{
			Name: k,
			Type: t,
		})
	}

	return result, nil
}

func (s *Step) populateConfigurationValues(dynamicStruct xlangx.DynamicStruct) {
	for k, v := range s.Configuration {
		dynamicStruct.Set(k, v)
	}
}
