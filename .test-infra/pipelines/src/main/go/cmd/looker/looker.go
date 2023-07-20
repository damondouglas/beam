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

package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"log"

	"github.com/apache/beam/test-infra/pipelines/src/main/go/cmd/looker/common"
	"github.com/apache/beam/test-infra/pipelines/src/main/go/cmd/looker/looks"
	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/secret"
	"github.com/spf13/cobra"
)

const (
	credentialsSecretPathFlag = "looker_credentials_secret_path"
)

var (
	rootCmd = &cobra.Command{
		Use:               "lookerctl",
		Short:             "Manager Looker resources",
		PersistentPreRunE: preRunE,
	}

	credentialsSecretPath string
)

func init() {
	rootCmd.AddCommand(looks.Command)
	rootCmd.PersistentFlags().StringVar(&credentialsSecretPath, credentialsSecretPathFlag, "", "The resource path to the Google Secret Manager secret holding Looker credentials")
}

func main() {
	if err := rootCmd.Execute(); err != nil {
		log.Fatal(err)
	}
}

func preRunE(c *cobra.Command, _ []string) error {
	if credentialsSecretPath == "" {
		return fmt.Errorf("missing --%s", credentialsSecretPathFlag)
	}
	ctx := c.Context()
	buf := &bytes.Buffer{}
	if err := secret.Read(ctx, credentialsSecretPath, buf); err != nil {
		return err
	}
	if err := json.NewDecoder(buf).Decode(&common.Client.Credentials); err != nil {
		return err
	}
	common.Client.Host = common.Client.Credentials.Host
	return nil
}
