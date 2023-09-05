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
	"fmt"
	"time"
)

var (
	flatSchemaElems = []interface{}{
		&FlatSchema{
			AString:  "🦄独角兽",
			AnInt32:  1,
			AnInt64:  1001,
			AFloat32: 1.1,
			AFloat64: 1000.1,
			ABoolean: true,
		},
		&FlatSchema{
			AString:  "🦄ユニコーン",
			AnInt32:  2,
			AnInt64:  2002,
			AFloat32: 2.2,
			AFloat64: 2000.2,
			ABoolean: false,
		},
		&FlatSchema{
			AString:  "🦄unicorn",
			AnInt32:  3,
			AnInt64:  3003,
			AFloat32: 3.3,
			AFloat64: 3000.3,
			ABoolean: true,
		},
	}

	flatSchemaCsv = []interface{}{
		"a_boolean,a_float32,a_float64,a_string,an_int32,an_int64",
		"true,1.1,1000.1,🦄独角兽,1,1001",
		"false,2.2,2000.2,🦄ユニコーン,2,2002",
		"true,3.3,3000.3,🦄unicorn,3,3003",
	}

	flatSchemaJson = []interface{}{
		"{\"a_string\":\"🦄独角兽\",\"an_int32\":1,\"an_int64\":1001,\"a_float32\":1.1,\"a_float64\":1000.1,\"a_boolean\":true}",
		"{\"a_string\":\"🦄ユニコーン\",\"an_int32\":2,\"an_int64\":2002,\"a_float32\":2.2,\"a_float64\":2000.2,\"a_boolean\":false}",
		"{\"a_string\":\"🦄unicorn\",\"an_int32\":3,\"an_int64\":3003,\"a_float32\":3.3,\"a_float64\":3000.3,\"a_boolean\":true}",
	}

	flatSchemaXmlElems []interface{}

	repeatedTypesSchemaElems = []interface{}{
		&repeatedTypesSchema{
			AString:  []string{"a", "b", "c"},
			AnInt32:  []int32{1, 2, 3},
			AnInt64:  []int64{100, 200, 300},
			AFloat32: []float32{1.1, 2.2, 3.3},
			AFloat64: []float64{100.111, 200.222, 300.333},
			ABoolean: []bool{false, true, false},
		},
	}

	nestedElems = []interface{}{
		&nested{
			FlatSchema: &FlatSchema{
				AString:  "🦄独角兽",
				AnInt32:  1,
				AnInt64:  1001,
				AFloat32: 1.1,
				AFloat64: 1000.1,
				ABoolean: true,
			},
		},
		&nested{
			FlatSchema: &FlatSchema{
				AString:  "🦄ユニコーン",
				AnInt32:  2,
				AnInt64:  2002,
				AFloat32: 2.2,
				AFloat64: 2000.2,
				ABoolean: false,
			},
		},
		&nested{
			FlatSchema: &FlatSchema{
				AString:  "🦄unicorn",
				AnInt32:  3,
				AnInt64:  3003,
				AFloat32: 3.3,
				AFloat64: 3000.3,
				ABoolean: true,
			},
		},
	}

	nestedRepeatedElems = []interface{}{
		&nestedRepeated{
			Repeated: []*FlatSchema{
				{
					AString:  "🦄独角兽",
					AnInt32:  1,
					AnInt64:  1001,
					AFloat32: 1.1,
					AFloat64: 1000.1,
					ABoolean: true,
				},
				{
					AString:  "🦄ユニコーン",
					AnInt32:  2,
					AnInt64:  2002,
					AFloat32: 2.2,
					AFloat64: 2000.2,
					ABoolean: false,
				},
				{
					AString:  "🦄unicorn",
					AnInt32:  3,
					AnInt64:  3003,
					AFloat32: 3.3,
					AFloat64: 3000.3,
					ABoolean: true,
				},
			},
		},
	}

	repeatedTypesSchemaJson = []interface{}{
		"{\"a_string\":[\"a\",\"b\",\"c\"],\"an_int32\":[1,2,3],\"an_int64\":[100,200,300],\"a_float32\":[1.1,2.2,3.3],\"a_float64\":[100.111,200.222,300.333],\"a_boolean\":[false,true,false]}",
	}

	timeContainingElems = []interface{}{
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

	timeContainingJson = []interface{}{
		"{\"a_time\": \"1969-12-31T16:16:40.000001-08:00\"}",
		"{\"a_time\": \"1969-12-31T16:33:20.000003-08:00\"}",
		"{\"a_time\": \"1969-12-31T16:50:00.000003-08:00\"}",
	}
)

type FlatSchema struct {
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

type nested struct {
	FlatSchema *FlatSchema
}

type nestedRepeated struct {
	Repeated []*FlatSchema
}

func (flat *FlatSchema) toRow() *flatSchemaRow {
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
