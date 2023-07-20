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

// Package form handles HTTP form encoding.
// See example_test.go for usage.
package form

import (
	"bufio"
	"io"
	"net/url"
	"reflect"
)

const (
	formTag = "form"
)

// NewEncoder instantiates an HTTP form encoder that writes to w.
func NewEncoder(w io.Writer) *Encoder {
	return &Encoder{
		w: bufio.NewWriter(w),
	}
}

// Encoder writes HTML form values to an output stream.
type Encoder struct {
	w *bufio.Writer
}

// Encode writes the HTML form values of v to the stream.
func (enc *Encoder) Encode(v interface{}) error {
	u := url.Values{}
	t := reflect.TypeOf(v)
	vv := reflect.ValueOf(v)
	if t.Kind() == reflect.Ptr {
		t = t.Elem()
		vv = vv.Elem()
	}
	for i := 0; i < t.NumField(); i++ {
		field := t.Field(i)
		if f, ok := field.Tag.Lookup(formTag); ok {
			if f != "" && f != "-" {
				fv := vv.Field(i)
				vvv := fv.String()
				u.Set(f, vvv)
			}
		}
	}
	_, err := enc.w.WriteString(u.Encode())
	if err != nil {
		return err
	}
	return enc.w.Flush()
}
