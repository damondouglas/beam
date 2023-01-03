package wordcount

import (
	"fmt"

	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/register"
)

func init() {
	// Register functions to optimize execution at runtime:

	// Register displayCountsFn that takes 2 inputs and returns 1 output.
	register.Function2x1(displayCountsFn)
}

// FormatCounts of a PCollection<KV<string, int64>> to "key: value"
func FormatCounts(s beam.Scope, input beam.PCollection) beam.PCollection {
	return beam.ParDo(s.Scope("FormatCounts"), displayCountsFn, input)
}

func displayCountsFn(key string, value int) string {
	return fmt.Sprintf("%s: %v", key, value)
}
