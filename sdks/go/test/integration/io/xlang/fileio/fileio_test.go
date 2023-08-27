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

package fileio

import (
	"cloud.google.com/go/storage"
	"context"
	_ "embed"
	"errors"
	"flag"
	"fmt"
	"github.com/apache/beam/sdks/v2/go/pkg/beam"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/xlang/fileio"
	_ "github.com/apache/beam/sdks/v2/go/pkg/beam/runners/flink"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/testing/ptest"
	"github.com/apache/beam/sdks/v2/go/test/integration"
	"github.com/google/go-cmp/cmp"
	"google.golang.org/api/iterator"
	"log"
	"reflect"
	"sort"
	"sync"
	"testing"
	"time"
)

const (
	labelFileIO = "fileio"
)

var (
	expansionAddr string
	bucket        *storage.BucketHandle

	flatSchemaPtrType  = reflect.TypeOf((*flatSchema)(nil))
	repeatedSchemaType = reflect.TypeOf((*repeatedTypesSchema)(nil))
	timeContainingType = reflect.TypeOf((*timeContainingSchema)(nil))

	flatSchemaElementPtrs = []interface{}{
		&flatSchema{
			AString:  "🦄独角兽",
			AnInt32:  1,
			AnInt64:  1001,
			AFloat32: 1.1,
			AFloat64: 1000.1,
			ABoolean: true,
		},
		&flatSchema{
			AString:  "🦄ユニコーン",
			AnInt32:  2,
			AnInt64:  2002,
			AFloat32: 2.2,
			AFloat64: 2000.2,
			ABoolean: false,
		},
		&flatSchema{
			AString:  "🦄unicorn",
			AnInt32:  3,
			AnInt64:  3003,
			AFloat32: 3.3,
			AFloat64: 3000.3,
			ABoolean: true,
		},
	}

	flatSchemaElementStructs = []interface{}{
		flatSchema{
			AString:  "🦄独角兽",
			AnInt32:  1,
			AnInt64:  1001,
			AFloat32: 1.1,
			AFloat64: 1000.1,
			ABoolean: true,
		},
		flatSchema{
			AString:  "🦄ユニコーン",
			AnInt32:  2,
			AnInt64:  2002,
			AFloat32: 2.2,
			AFloat64: 2000.2,
			ABoolean: false,
		},
		flatSchema{
			AString:  "🦄unicorn",
			AnInt32:  3,
			AnInt64:  3003,
			AFloat32: 3.3,
			AFloat64: 3000.3,
			ABoolean: true,
		},
	}

	flatSchemaElementsCsv = []interface{}{
		"a_boolean,a_float32,a_float64,a_string,an_int32,an_int64",
		"true,1.1,1000.1,🦄独角兽,1,1001",
		"false,2.2,2000.2,🦄ユニコーン,2,2002",
		"true,3.3,3000.3,🦄unicorn,3,3003",
	}

	flatSchemaElementsJson = []interface{}{
		"{\"a_string\":\"🦄独角兽\",\"an_int32\":1,\"an_int64\":1001,\"a_float32\":1.1,\"a_float64\":1000.1,\"a_boolean\":true}",
		"{\"a_string\":\"🦄ユニコーン\",\"an_int32\":2,\"an_int64\":2002,\"a_float32\":2.2,\"a_float64\":2000.2,\"a_boolean\":false}",
		"{\"a_string\":\"🦄unicorn\",\"an_int32\":3,\"an_int64\":3003,\"a_float32\":3.3,\"a_float64\":3000.3,\"a_boolean\":true}",
	}

	flatSchemaElementsXml []interface{}

	repeatedTypesSchemaElements = []interface{}{
		&repeatedTypesSchema{
			AString:  []string{"a", "b", "c"},
			AnInt32:  []int32{1, 2, 3},
			AnInt64:  []int64{100, 200, 300},
			AFloat32: []float32{1.1, 2.2, 3.3},
			AFloat64: []float64{100.111, 200.222, 300.333},
			ABoolean: []bool{false, true, false},
		},
	}

	repeatedTypesSchemaElementsJson = []interface{}{
		"{\"a_string\":[\"a\",\"b\",\"c\"],\"an_int32\":[1,2,3],\"an_int64\":[100,200,300],\"a_float32\":[1.1,2.2,3.3],\"a_float64\":[100.111,200.222,300.333],\"a_boolean\":[false,true,false]}",
	}

	timeContainingSchemaElements = []interface{}{
		&timeContainingSchema{
			ATime: Time(time.Unix(1000, 1000)),
		},
		&timeContainingSchema{
			ATime: Time(time.Unix(2000, 2000)),
		},
		&timeContainingSchema{
			ATime: Time(time.Unix(3000, 3000)),
		},
	}

	timeContainingSchemaElementsJson = []interface{}{
		"{\"a_time\": \"1969-12-31T16:16:40.000001-08:00\"}",
		"{\"a_time\": \"1969-12-31T16:33:20.000003-08:00\"}",
		"{\"a_time\": \"1969-12-31T16:50:00.000003-08:00\"}",
	}
)

func checkFlags(t *testing.T) {
	if *integration.StorageBucket == "" {
		t.Skip("Missing --gcs_bucket flag. Google Cloud Storage Bucket required.")
	}
}

func setup(ctx context.Context) error {
	client, err := storage.NewClient(ctx)
	if err != nil {
		return fmt.Errorf("error initializing storage client: %w", err)
	}
	bucket = client.Bucket(*integration.StorageBucket)

	// check if bucket exists using the Attrs method
	_, err = bucket.Attrs(ctx)
	if err != nil {
		return fmt.Errorf("error acquiring Storage Bucket attributes; %s may not exist or access denied",
			*integration.StorageBucket)
	}

	var rows []*flatSchemaRow
	for _, elem := range flatSchemaElementPtrs {
		schemaElem := elem.(*flatSchema)
		row := schemaElem.toRow()
		sort.Sort(row)
		rows = append(rows, row)
	}
	flatSchemaElementsXml = []interface{}{
		&flatSchemaXml{
			Rows: rows,
		},
	}

	// We want to delete any previously created resources prior to running tests.
	return deleteAllResources(ctx)
}

func TestWriteThenRead(t *testing.T) {
	integration.CheckFilters(t)
	checkFlags(t)
	tests := []testWriteCase{
		{
			name: "format=avro;schema=flat",
			configuration: &fileio.WriteConfiguration{
				Format:    fileio.FormatAvro,
				NumShards: 1,
			},
			elements:         flatSchemaElementPtrs,
			writeType:        flatSchemaPtrType,
			readPipelineFn:   readAvroPipeline,
			wantReadElements: flatSchemaElementPtrs,
		},
		{
			name: "format=avro;schema=repeated",
			configuration: &fileio.WriteConfiguration{
				Format:    fileio.FormatAvro,
				NumShards: 1,
			},
			elements:         repeatedTypesSchemaElements,
			writeType:        repeatedSchemaType,
			readPipelineFn:   readAvroPipeline,
			wantReadElements: repeatedTypesSchemaElements,
		},
		{
			name: "format=csv;schema=flat",
			configuration: &fileio.WriteConfiguration{
				Compression: fileio.CompressionUnCompressed,
				Format:      fileio.FormatCsv,
				NumShards:   1,
				CsvConfiguration: &fileio.CsvWrite{
					PredefinedCsvFormat: fileio.PredefinedCsvFormatDefault,
				},
			},
			elements:         flatSchemaElementPtrs,
			writeType:        reflect.TypeOf(&flatSchema{}),
			readPipelineFn:   readTextPipeline,
			wantReadElements: flatSchemaElementsCsv,
		},
		{
			name: "format=json;schema=flat",
			configuration: &fileio.WriteConfiguration{
				Format:    fileio.FormatJson,
				NumShards: 1,
			},
			elements:         flatSchemaElementPtrs,
			writeType:        reflect.TypeOf(&flatSchema{}),
			readPipelineFn:   readTextPipeline,
			wantReadElements: flatSchemaElementsJson,
		},
		{
			// readParquetPipeline using parquetio with Pointer elements results in the error:
			// 'failed to create schema from tag map: type : not a valid Type string'.
			// Therefore, we test fileio.FormatParquet using structs only.
			name: "format=parquet;schema=flat",
			configuration: &fileio.WriteConfiguration{
				Format:    fileio.FormatParquet,
				NumShards: 1,
				ParquetConfiguration: &fileio.ParquetWrite{
					CompressionCodecName: fileio.ParquetCompressionGzip,
				},
			},
			elements:         flatSchemaElementStructs,
			writeType:        reflect.TypeOf(flatSchema{}),
			readPipelineFn:   readParquetPipeline,
			wantReadElements: flatSchemaElementStructs,
		},
		{
			name: "format=xml;schema=flat",
			configuration: &fileio.WriteConfiguration{
				Format:    fileio.FormatXml,
				NumShards: 1,
				XmlConfiguration: &fileio.XmlWrite{
					RootElement: "flatSchema",
					Charset:     fileio.XmlCharsetUTF8,
				},
			},
			elements:         flatSchemaElementPtrs,
			writeType:        reflect.TypeOf(&flatSchemaXml{}),
			readPipelineFn:   readXmlPipeline,
			wantReadElements: flatSchemaElementsXml,
		},
		{
			name: "format=json;schema=repeated",
			configuration: &fileio.WriteConfiguration{
				Format:    fileio.FormatJson,
				NumShards: 1,
			},
			elements:         repeatedTypesSchemaElements,
			writeType:        reflect.TypeOf(&repeatedTypesSchema{}),
			readPipelineFn:   readTextPipeline,
			wantReadElements: repeatedTypesSchemaElementsJson,
		},
		{
			// TODO: fails with "java.lang.RuntimeException: Unhandled logical type fileio.Time"
			// 	NOTE: time.Time also failed.
			// 	See: https://github.com/apache/beam/issues/28179
			skip: true,
			name: "format=avro;schema=time_containing",
			configuration: &fileio.WriteConfiguration{
				Format:    fileio.FormatAvro,
				NumShards: 1,
			},
			elements:         timeContainingSchemaElements,
			writeType:        timeContainingType,
			readPipelineFn:   readAvroPipeline,
			wantReadElements: timeContainingSchemaElements,
		},
		{
			// TODO: fails with "java.io.EOFException: reached end of stream after reading 35 bytes; 127 bytes expected"
			// 	See: https://github.com/apache/beam/issues/28179
			skip: true,
			name: "format=json;schema=time_containing",
			configuration: &fileio.WriteConfiguration{
				Format:    fileio.FormatJson,
				NumShards: 1,
			},
			elements:         timeContainingSchemaElements,
			writeType:        reflect.TypeOf(timeContainingSchema{}),
			readPipelineFn:   readTextPipeline,
			wantReadElements: timeContainingSchemaElementsJson,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.skip {
				return
			}
			if err := tt.initE(); err != nil {
				t.Fatalf("fatal: testWriteCase.initE() err %v", err)
			}
			write := writePipeline(expansionAddr, tt)
			ptest.RunAndValidate(t, write)
			read := tt.readPipelineFn(tt)
			ptest.RunAndValidate(t, read)
		})
	}
}

func TestWriteConfigurationErrors(t *testing.T) {
	integration.CheckFilters(t)
	checkFlags(t)
	tests := []testWriteCase{
		{
			name: "format!=csv;CsvWrite!=null",
			configuration: &fileio.WriteConfiguration{
				Format:           fileio.FormatAvro,
				CsvConfiguration: &fileio.CsvWrite{},
			},
			elements: flatSchemaElementPtrs,
			wantErr:  errors.New("invalid configuration: format: avro incompatible with non-nil *fileio.CsvWrite"),
		},
		{
			name: "format!=parquet;ParquetWrite!=null",
			configuration: &fileio.WriteConfiguration{
				Format:               fileio.FormatAvro,
				ParquetConfiguration: &fileio.ParquetWrite{},
			},
			elements: flatSchemaElementStructs,
			wantErr:  errors.New("invalid configuration: format: avro incompatible with non-nil *fileio.ParquetWrite"),
		},
		{
			name: "format!=xml;XmlWrite!=null",
			configuration: &fileio.WriteConfiguration{
				Format:           fileio.FormatAvro,
				XmlConfiguration: &fileio.XmlWrite{},
			},
			elements: flatSchemaElementStructs,
			wantErr:  errors.New("invalid configuration: format: avro incompatible with non-nil *fileio.XmlWrite"),
		},
		{
			name: "format==csv;CsvWrite==null",
			configuration: &fileio.WriteConfiguration{
				Format: fileio.FormatCsv,
			},
			elements: flatSchemaElementPtrs,
			wantErr:  errors.New("invalid configuration: format: csv requires a non-nil *fileio.CsvWrite"),
		},
		{
			name: "format==parquet;ParquetWrite==null",
			configuration: &fileio.WriteConfiguration{
				Format: fileio.FormatParquet,
			},
			elements: flatSchemaElementPtrs,
			wantErr:  errors.New("invalid configuration: format: parquet requires a non-nil *fileio.ParquetWrite"),
		},
		{
			name: "format==xml;XmlWrite==null",
			configuration: &fileio.WriteConfiguration{
				Format: fileio.FormatXml,
			},
			elements: flatSchemaElementPtrs,
			wantErr:  errors.New("invalid configuration: format: xml requires a non-nil *fileio.XmlWrite"),
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			_, s := beam.NewPipelineWithRoot()
			input := beam.Create(s, tt.elements...)
			_, err := fileio.TryWrite(s, expansionAddr, tt.configuration, input)
			if err == nil {
				t.Errorf("TryWrite(%+v) err = nil, want: %v", tt.configuration, tt.wantErr)
				return
			}
			if diff := cmp.Diff(err.Error(), tt.wantErr.Error()); diff != "" {
				t.Errorf("TryWrite(%+v) err = %v, diff:\n%s", tt.configuration, err, diff)
			}
		})
	}
}

func TestMain(m *testing.M) {
	ctx := context.Background()
	flag.Parse()
	beam.Init()
	if err := setup(ctx); err != nil {
		log.Fatalf("setup() err: %v", err)
	}
	services := integration.NewExpansionServices()
	defer services.Shutdown()
	addr, err := services.GetAddr(labelFileIO)
	if err != nil {
		log.Printf("skipping missing expansion service: %v", err)
		return
	}

	expansionAddr = addr
	ptest.MainRet(m)
}

func deleteAllResources(ctx context.Context) error {
	ctx, cancel := context.WithCancel(ctx)

	var wg sync.WaitGroup
	// collate errors instead of exiting immediately
	var errs []error
	errChan := make(chan error)

	wg.Add(1)
	go deleteAllObjects(ctx, fmt.Sprintf("%s*", commonPrefix(fileio.FormatAvro)), &wg, errChan)

	wg.Add(1)
	go deleteAllObjects(ctx, fmt.Sprintf("%s*", commonPrefix(fileio.FormatCsv)), &wg, errChan)

	wg.Add(1)
	go deleteAllObjects(ctx, fmt.Sprintf("%s*", commonPrefix(fileio.FormatJson)), &wg, errChan)

	wg.Add(1)
	go deleteAllObjects(ctx, fmt.Sprintf("%s*", commonPrefix(fileio.FormatParquet)), &wg, errChan)

	wg.Add(1)
	go deleteAllObjects(ctx, fmt.Sprintf("%s*", commonPrefix(fileio.FormatXml)), &wg, errChan)

	wg.Wait()
	// conventional defer cancel() does not work and needs to be placed after wg.Wait()
	cancel()

	for {
		select {
		case <-ctx.Done():
			if len(errs) > 0 {
				return errors.Join(errs...)
			}
			return nil
		case err := <-errChan:
			errs = append(errs, err)
		}
	}
}

func deleteAllObjects(ctx context.Context, glob string, wg *sync.WaitGroup, errChan chan error) {
	defer wg.Done()
	itr := bucket.Objects(ctx, &storage.Query{
		MatchGlob: glob,
	})
	var count int
	for {
		attr, err := itr.Next()
		if errors.Is(err, iterator.Done) {
			return
		}
		if err != nil {
			errChan <- fmt.Errorf("error: iterator.Next() %w", err)
			return
		}
		count++
		obj := bucket.Object(attr.Name)
		if err = obj.Delete(ctx); err != nil {
			errChan <- fmt.Errorf("error: ObjectHandle.Delete(%s) %w", attr.Name, err)
			return
		}
	}
}
