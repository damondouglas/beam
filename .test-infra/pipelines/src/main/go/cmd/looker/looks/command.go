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

// Package looks holds commands for managing Looker Looks.
package looks

import (
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"log"
	"net/url"
	"text/template"

	"cloud.google.com/go/storage"
	"github.com/apache/beam/test-infra/pipelines/src/main/go/cmd/looker/common"
	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/looker"
	"github.com/spf13/cobra"
)

const (
	resultOutputPatternFlag = "look_result_output_pattern"
	resultBucketFlag        = "look_result_bucket"
)

var (

	// Command groups subcommands for managing Looker Looks.
	Command = &cobra.Command{
		Use:   "looks",
		Short: "Manage Looker Looks",
	}

	runCmd = &cobra.Command{
		Use:     "run RESOURCE",
		Short:   "Run a Looker Look. RESOURCE accepts either looks/LOOK_ID or folders/FOLDER_ID, where all looks in a folder are executed",
		PreRunE: runLookPreRunE,
		Args:    common.ParseResourceArgs,
		Long: `
Run a Looker Look.

Usage: run (LOOK|FOLDER)

LOOK: looks/LOOK_ID
FOLDER: folders/FOLDER_ID
`,
		RunE: func(c *cobra.Command, args []string) error {
			return common.RunWithCredentials(c, args, runLookE)
		},
	}

	resultFormat        = looker.FormatPng
	resultOutputPattern string
	resultBucket        string

	bucketHandle *storage.BucketHandle
	nameTmpl     *template.Template
)

func init() {
	Command.AddCommand(runCmd)
	runCmd.Flags().StringVar(&resultFormat.Value, "look_result_format", resultFormat.Value, "The format of the Look run result")

	runCmd.Flags().StringVar(&resultOutputPattern, resultOutputPatternFlag, resultOutputPattern, "The output pattern to name the result")
	_ = runCmd.MarkFlagRequired(resultOutputPatternFlag)

	runCmd.Flags().StringVar(&resultBucket, resultBucketFlag, resultBucket, "The Google Cloud storage bucket to write result")
	_ = runCmd.MarkFlagRequired(resultBucketFlag)
}

func runLookPreRunE(c *cobra.Command, _ []string) error {
	ctx := c.Context()
	allowedResources := map[looker.Kind]struct{}{
		looker.FolderKind: {},
		looker.LookKind:   {},
	}
	if _, ok := allowedResources[common.Resource.Kind]; !ok {
		return fmt.Errorf("resource unrecognized: %s", common.Resource.Kind)
	}

	nameTmpl = template.Must(template.New("name").Parse(resultOutputPattern))

	client, err := storage.NewClient(ctx)
	if err != nil {
		return err
	}

	// mark required by flags
	if resultBucket == "" {
		return nil
	}

	bucketHandle = client.Bucket(resultBucket)
	// check if bucket exists and has access
	_, err = bucketHandle.Attrs(ctx)
	return err
}

func runLookE(c *cobra.Command, _ []string) error {
	var errs []error
	ctx := c.Context()
	result, err := looks(ctx)
	if err != nil {
		return err
	}

	for _, look := range result {
		if err := run(ctx, look); err != nil {
			errs = append(errs, err)
		}
	}
	return errors.Join(errs...)
}

func looks(ctx context.Context) ([]*looker.Look, error) {
	var result []*looker.Look
	if common.Resource.Kind == looker.LookKind {
		l, err := look(ctx, common.Resource.ID)
		if err != nil {
			return nil, err
		}
		result = append(result, l)
		return result, nil
	}
	buf := &bytes.Buffer{}
	u, _ := url.ParseQuery("")
	u.Set("folder_id", fmt.Sprint(common.Resource.ID))
	if err := common.Client.Get(ctx, looker.Spec.Looks.Search, u, buf); err != nil {
		return nil, err
	}

	if err := json.NewDecoder(buf).Decode(&result); err != nil {
		return nil, err
	}

	return result, nil
}

func look(ctx context.Context, id int) (*looker.Look, error) {
	var result *looker.Look
	param := looker.LookIDParam
	param.Value = fmt.Sprint(id)
	path := looker.Spec.Looks.Describe.MustPath(param)
	buf := &bytes.Buffer{}
	if err := common.Client.Get(ctx, path, nil, buf); err != nil {
		return nil, err
	}
	if err := json.NewDecoder(buf).Decode(&result); err != nil {
		return nil, err
	}
	return result, nil
}

func run(ctx context.Context, look *looker.Look) error {
	param := looker.LookIDParam
	param.Value = fmt.Sprint(look.ID)
	path := looker.Spec.Looks.Run.MustPath(param, resultFormat)
	buf := &bytes.Buffer{}
	if err := common.Client.Get(ctx, path, nil, buf); err != nil {
		return err
	}

	return save(ctx, look, buf)
}

func save(ctx context.Context, look *looker.Look, result io.Reader) error {
	buf := &bytes.Buffer{}
	if err := nameTmpl.Execute(buf, look); err != nil {
		return err
	}
	w := bucketHandle.Object(buf.String()).NewWriter(ctx)
	if _, err := io.Copy(w, result); err != nil {
		return err
	}
	log.Printf("saved %s", buf.String())
	return w.Close()
}
