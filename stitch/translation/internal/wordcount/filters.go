package wordcount

import (
	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/transforms/filter"
)

// ExcludeEmpty string elements in a PCollection<String>.
func ExcludeEmpty(s beam.Scope, input beam.PCollection) beam.PCollection {
	return filter.Exclude(s.Scope("ExcludeEmpty"), input, isEmptyFn)
}
