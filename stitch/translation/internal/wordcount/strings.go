package wordcount

import (
	"strings"

	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/register"
)

const (
	splitDelimiter = " "
)

func init() {
	// Register functions to optimize execution at runtime:

	// strings.ToLower takes 1 input and returns 1 output.
	register.Function1x1(strings.ToLower)

	// splitFn takes 2 inputs, whereas the last is an emitter func
	register.Function2x0(splitFn)

	// register the string emitter func used by splitFn
	register.Emitter1[string]()
}

// ToLower converts PCollection<String> elements to lower case strings.
func ToLower(s beam.Scope, input beam.PCollection) beam.PCollection {
	return beam.ParDo(s.Scope("ToLower"), strings.ToLower, input)
}

// Split strings delimited by " " in a PCollection<String> emitting each
// resulting token.
func Split(s beam.Scope, input beam.PCollection) beam.PCollection {
	return beam.ParDo(s.Scope("Split"), splitFn, input)
}

func isEmptyFn(s string) bool {
	return s == ""
}

func splitFn(s string, emit func(string)) {
	for _, k := range strings.Split(s, splitDelimiter) {
		emit(k)
	}
}
