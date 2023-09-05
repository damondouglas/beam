// Licensed to the Apache Software Foundation (ASF) under one or more
// contributor license agreements.  See the NOTICE file distributed with
// this work for additional information regarding copyright ownership.
// The ASF licenses this file to You under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with
// the License.  You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Package fileio contains integration tests for cross-language File Schema Provider transforms.
package fileio

import (
	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/xlang/fileio"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/testing/passert"
)

func testWrite(xfn func(s beam.Scope, input beam.PCollection) (beam.PCollection, error), elems []interface{}, want []*fileio.WriteResult) (*beam.Pipeline, error) {
	p, s := beam.NewPipelineWithRoot()
	input := beam.Create(s, elems...)
	got, err := xfn(s, input)
	if err != nil {
		return nil, err
	}
	passert.Equals(s, got, want)
	return p, nil
}
