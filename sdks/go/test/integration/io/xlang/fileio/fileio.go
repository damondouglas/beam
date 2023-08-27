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
	"context"
	"encoding/xml"
	"errors"
	"fmt"
	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/graph/coder"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/runtime/graphx/schema"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/core/util/reflectx"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/avroio"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/filesystem"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/parquetio"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/textio"
	fileiox "github.com/apache/beam/sdks/v2/go/pkg/beam/io/xlang/fileio"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/log"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/testing/passert"
	"github.com/apache/beam/sdks/v2/go/test/integration"
	"io"
	"path/filepath"
	"reflect"
	"sort"
	"strings"
	"time"
)

var (
	timeType        = reflect.TypeOf((*Time)(nil)).Elem()
	timeStorageType = reflectx.String
)

func init() {
	beam.RegisterType(reflect.TypeOf((*flatSchema)(nil)).Elem())
	beam.RegisterType(reflect.TypeOf((*flatSchemaXml)(nil)).Elem())
	beam.RegisterType(reflect.TypeOf((*flatSchemaRow)(nil)).Elem())
	beam.RegisterType(reflect.TypeOf((*rowEntry)(nil)).Elem())
	beam.RegisterType(reflect.TypeOf((*rowValue)(nil)).Elem())
	beam.RegisterType(timeType)
	beam.RegisterType(reflect.TypeOf((*timeContainingSchema)(nil)).Elem())
	schema.RegisterLogicalType(schema.ToLogicalType("fileio.Time", timeType, timeStorageType))
	coder.RegisterSchemaProviders(timeType, timeEnc, timeDec)
}

func timeEnc(reflect.Type) (func(any, io.Writer) error, error) {
	return func(iface any, w io.Writer) error {
		if err := coder.WriteSimpleRowHeader(1, w); err != nil {
			return nil
		}
		t := iface.(Time)
		tt := (time.Time)(t)
		data, err := tt.MarshalText()
		if err != nil {
			return fmt.Errorf("marshalling time: %v", err)
		}
		if err := coder.EncodeBytes(data, w); err != nil {
			return err
		}
		return nil
	}, nil
}

func timeDec(reflect.Type) (func(io.Reader) (any, error), error) {
	return func(r io.Reader) (any, error) {
		if err := coder.ReadSimpleRowHeader(1, r); err != nil {
			return nil, err
		}
		data, err := coder.DecodeBytes(r)
		if err != nil {
			return nil, err
		}
		t := time.Time{}
		if err := t.UnmarshalText(data); err != nil {
			return nil, err
		}
		return Time(t), nil
	}, nil
}

type flatSchema struct {
	AString  string  `beam:"a_string" json:"a_string" parquet:"name=a_string, type=BYTE_ARRAY, convertedtype=UTF8, encoding=PLAIN_DICTIONARY"`
	AnInt32  int32   `beam:"an_int32" json:"an_int32" parquet:"name=an_int32, type=INT32, encoding=PLAIN"`
	AnInt64  int64   `beam:"an_int64" json:"an_int64" parquet:"name=an_int64, type=INT64"`
	AFloat32 float32 `beam:"a_float32" json:"a_float32" parquet:"name=a_float32, type=FLOAT"`
	AFloat64 float64 `beam:"a_float64" json:"a_float64" parquet:"name=a_float64, type=DOUBLE"`
	ABoolean bool    `beam:"a_boolean" json:"a_boolean" parquet:"name=a_boolean, type=BOOLEAN"`
}

type repeatedTypesSchema struct {
	AString  []string  `beam:"a_string" json:"a_string"`
	AnInt32  []int32   `beam:"an_int32" json:"an_int32"`
	AnInt64  []int64   `beam:"an_int64" json:"an_int64"`
	AFloat32 []float32 `beam:"a_float32" json:"a_float32"`
	AFloat64 []float64 `beam:"a_float64" json:"a_float64"`
	ABoolean []bool    `beam:"a_boolean" json:"a_boolean"`
}

type Time time.Time

type timeContainingSchema struct {
	ATime Time `beam:"a_time" json:"a_time"`
}

func (flat *flatSchema) toRow() *flatSchemaRow {
	return &flatSchemaRow{
		RowEntry: []*rowEntry{
			{
				Key: "a_string",
				Value: &rowValue{
					Type:  "xs:string",
					Value: flat.AString,
				},
			},
			{
				Key: "an_int32",
				Value: &rowValue{
					Type:  "xs:int",
					Value: fmt.Sprint(flat.AnInt32),
				},
			},
			{
				Key: "an_int64",
				Value: &rowValue{
					Type:  "xs:long",
					Value: fmt.Sprint(flat.AnInt64),
				},
			},
			{
				Key: "a_float32",
				Value: &rowValue{
					Type:  "xs:float",
					Value: fmt.Sprint(flat.AFloat32),
				},
			},
			{
				Key: "a_float64",
				Value: &rowValue{
					Type:  "xs:double",
					Value: fmt.Sprint(flat.AFloat64),
				},
			},
			{
				Key: "a_boolean",
				Value: &rowValue{
					Type:  "xs:boolean",
					Value: fmt.Sprint(flat.ABoolean),
				},
			},
		},
	}
}

type flatSchemaXml struct {
	Rows []*flatSchemaRow `xml:"row>data"`
}

func (schema *flatSchemaXml) String() string {
	return fmt.Sprintf("Rows: %+v", schema.Rows)
}

type flatSchemaRow struct {
	RowEntry []*rowEntry `xml:"entry"`
}

func (row *flatSchemaRow) Len() int {
	return len(row.RowEntry)
}

func (row *flatSchemaRow) Less(i, j int) bool {
	return row.RowEntry[i].Key < row.RowEntry[j].Key
}

func (row *flatSchemaRow) Swap(i, j int) {
	ith := row.RowEntry[i]
	jth := row.RowEntry[j]
	row.RowEntry[i] = jth
	row.RowEntry[j] = ith
}

func (row *flatSchemaRow) String() string {
	return fmt.Sprintf("Entries: %+v", row.RowEntry)
}

type rowEntry struct {
	Key   string    `xml:"key"`
	Value *rowValue `xml:"value>value"`
}

func (entry *rowEntry) String() string {
	return fmt.Sprintf("{key=%s, value=%+v}", entry.Key, entry.Value)
}

type rowValue struct {
	Type  string `xml:"type,attr"`
	Value string `xml:",chardata"`
}

type testWriteCase struct {
	name             string
	configuration    *fileiox.WriteConfiguration
	elements         []interface{}
	writeType        reflect.Type
	readPipelineFn   func(testWriteCase) *beam.Pipeline
	wantReadElements []interface{}
	wantErr          error
	skip             bool
}

func (caze testWriteCase) initE() error {
	if caze.configuration.Format == "" {
		return fmt.Errorf("%T.Format is required", caze.configuration)
	}
	if caze.writeType == nil {
		return errors.New("writeType is required")
	}
	if caze.configuration.FilenamePrefix == "" {
		caze.configuration.FilenamePrefix = caze.writePrefix()
	}
	return nil
}

func (caze testWriteCase) readPrefix() string {
	return readPrefix(caze.configuration.Format, strings.ReplaceAll(caze.writeType.String(), "*", ""))
}

func (caze testWriteCase) writePrefix() string {
	return writePrefix(caze.configuration.Format, strings.ReplaceAll(caze.writeType.String(), "*", ""))
}

func (caze testWriteCase) wantWriteResults() []interface{} {
	var result []interface{}
	n := caze.configuration.NumShards
	i := -1
	for {
		i++
		n--
		if n < 0 {
			return result
		}
		result = append(result, fileiox.WriteResult{
			FileName: fmt.Sprintf("%s-%05d-of-%05d.%s", caze.writePrefix(), i, caze.configuration.NumShards, caze.configuration.Format),
		})
	}
}

func readAvroPipeline(tt testWriteCase) *beam.Pipeline {
	p, s := beam.NewPipelineWithRoot()
	got := avroio.Read(s, tt.readPrefix(), tt.writeType)
	passert.Equals(s, got, tt.wantReadElements...)
	return p
}

func readParquetPipeline(tt testWriteCase) *beam.Pipeline {
	p, s := beam.NewPipelineWithRoot()
	got := parquetio.Read(s, tt.readPrefix(), tt.writeType)
	passert.Equals(s, got, tt.wantReadElements...)
	return p
}

func readTextPipeline(tt testWriteCase) *beam.Pipeline {
	p, s := beam.NewPipelineWithRoot()
	got := textio.Read(s, tt.readPrefix())
	passert.Equals(s, got, tt.wantReadElements...)
	return p
}

func readXmlPipeline(tt testWriteCase) *beam.Pipeline {
	p, s := beam.NewPipelineWithRoot()
	s = s.Scope("readXmlPipeline")
	filesystem.ValidateScheme(tt.readPrefix())
	got := readFlatSchemaXml(s, beam.Create(s, tt.readPrefix()))
	passert.Equals(s, got, tt.wantReadElements...)
	return p
}

func writePipeline(expansionAddr string, tt testWriteCase) *beam.Pipeline {
	p, s := beam.NewPipelineWithRoot()
	input := beam.Create(s, tt.elements...)
	gotWriteResults := fileiox.Write(s, expansionAddr, tt.configuration, input)
	wantWriteResults := tt.wantWriteResults()
	passert.Equals(s, gotWriteResults, wantWriteResults...)
	return p
}

func readFlatSchemaXml(s beam.Scope, col beam.PCollection) beam.PCollection {
	files := beam.ParDo(s, expandFn, col)
	return beam.ParDo(s, xmlReadFlatSchema, files)
}

func xmlReadFlatSchema(ctx context.Context, filename string, emit func(*flatSchemaXml)) error {
	log.Infof(ctx, "Reading XML from %v", filename)
	fs, err := filesystem.New(ctx, filename)
	if err != nil {
		return err
	}
	defer fs.Close()

	fd, err := fs.OpenRead(ctx, filename)
	if err != nil {
		return err
	}
	defer fd.Close()
	var val *flatSchemaXml
	if err := xml.NewDecoder(fd).Decode(&val); err != nil {
		return err
	}

	for _, row := range val.Rows {
		sort.Sort(row)
	}

	emit(val)

	return nil
}

func expandFn(ctx context.Context, glob string, emit func(string)) error {
	if strings.TrimSpace(glob) == "" {
		return nil // ignore empty string elements here
	}

	fs, err := filesystem.New(ctx, glob)
	if err != nil {
		return err
	}
	defer fs.Close()

	files, err := fs.List(ctx, glob)
	if err != nil {
		return err
	}
	for _, filename := range files {
		emit(filename)
	}
	return nil
}

func writePrefix(format fileiox.Format, addl ...string) string {
	return fmt.Sprintf("gs://%s/%s", *integration.StorageBucket, commonPrefix(format, addl...))
}

func readPrefix(format fileiox.Format, addl ...string) string {
	return fmt.Sprintf("gs://%s/%s*", *integration.StorageBucket, commonPrefix(format, addl...))
}

func commonPrefix(format fileiox.Format, addl ...string) string {
	addl = append([]string{string(format)}, addl...)
	addl = append(addl, "out")
	return filepath.Join(addl...)
}
