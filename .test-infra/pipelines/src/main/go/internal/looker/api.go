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

// Package looker holds code for Looker API calls.
package looker

import (
	"fmt"
	"regexp"
	"strconv"

	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/httpx"
)

const (
	apiVersion       = "4.0"
	resultFormatName = "{result_format}"

	FolderKind Kind = "folders"
	LookKind   Kind = "looks"
)

var (

	// FormatPng is a url parameter that specifies a png format.
	FormatPng = httpx.Param{
		Name:  resultFormatName,
		Value: "png",
	}

	// LookIDParam is a url parameter that specifies the Look ID.
	LookIDParam = httpx.Param{
		Name: "{look_id}",
	}

	// Spec configures Looker API endpoint paths.
	// See https://cloud.google.com/looker/docs/reference/looker-api/latest
	Spec = Looker{
		Base: []string{
			"api",
			apiVersion,
		},
		Auth: ApiAuth{
			Login: []string{
				"login",
			},
		},
		Looks: Looks{
			Create: httpx.Path{
				"looks",
			},
			Describe: httpx.Path{
				"looks",
				LookIDParam.Name,
			},
			List: httpx.Path{
				"looks",
			},
			Search: httpx.Path{
				"looks",
				"search",
			},
			Run: httpx.Path{
				"looks",
				LookIDParam.Name,
				"run",
				resultFormatName,
			},
		},
		Queries: Queries{
			Create: httpx.Path{
				"queries",
			},
			Describe: httpx.Path{
				"queries",
				"slug",
				"{slug}",
			},
			List: httpx.Path{
				"queries",
			},
			Run: httpx.Path{
				"queries",
				"run",
				resultFormatName,
			},
		},
	}
)

type Looker struct {
	Auth    ApiAuth
	Base    httpx.Path
	Looks   Looks
	Queries Queries
}

type ApiAuth struct {
	Login httpx.Path
}

type Looks struct {
	Create   httpx.Path
	Describe httpx.Path
	List     httpx.Path
	Search   httpx.Path
	Run      httpx.Path
}

type Queries struct {
	Create   httpx.Path
	Describe httpx.Path
	List     httpx.Path
	Run      httpx.Path
}

type Kind string

type Resource struct {
	Kind Kind
	ID   int
}

func (res Resource) String() string {
	return fmt.Sprintf("%s/%v", res.Kind, res.ID)
}

func ParseResource(s string) (Resource, error) {
	var err error
	var result Resource
	p := regexp.MustCompile(`(?P<kind>[a-z]+)/(?P<id>[1-9][0-9]*)`)
	matches := p.FindStringSubmatch(s)
	if len(matches) < 2 {
		return result, fmt.Errorf("could not parse resource from %s", s)
	}
	result.Kind = Kind(matches[p.SubexpIndex("kind")])
	if result.ID, err = strconv.Atoi(matches[p.SubexpIndex("id")]); err != nil {
		return result, err
	}
	return result, nil
}

type Look struct {
	ID          string `json:"id"`
	Title       string `json:"title"`
	IsRunOnLoad bool   `json:"is_run_on_load"`
	Public      bool   `json:"public"`
	QueryId     string `json:"query_id"`
	FolderId    string `json:"folder_id"`
	PublicSlug  string `json:"public_slug"`
}
