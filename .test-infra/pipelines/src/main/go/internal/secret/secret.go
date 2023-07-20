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

// Package secret supports reading from and writing to Google Secret Manager
// secrets.
package secret

import (
	"bytes"
	"context"
	"fmt"
	"io"

	secretmanager "cloud.google.com/go/secretmanager/apiv1"
	"cloud.google.com/go/secretmanager/apiv1/secretmanagerpb"
	"google.golang.org/api/option"
)

// Read the contents of a secret version into an io.Writer.
// secretVersionResourcePath is the resource name of the secret version in the
// format: projects/*/secrets/*/versions/*.
// projects/*/secrets/*/versions/latest is an alias to the most recently created
// secret version.
func Read(ctx context.Context, secretVersionResourcePath string, w io.Writer, opts ...option.ClientOption) error {
	client, err := secretmanager.NewClient(ctx, opts...)
	if err != nil {
		return err
	}
	resp, err := client.AccessSecretVersion(ctx, &secretmanagerpb.AccessSecretVersionRequest{
		Name: secretVersionResourcePath,
	})
	if err != nil {
		return err
	}
	if resp.Payload == nil {
		return fmt.Errorf("error: could not retrieve payload from: %s", secretVersionResourcePath)
	}
	r := bytes.NewReader(resp.Payload.Data)
	_, err = io.Copy(w, r)
	return err
}

// Write a new Google Secret Manager Secret version from an io.Reader.
// secretVersionResourcePath is the resource name of the secret to associate
// with the new version in the format projects/*/secrets/*.
func Write(ctx context.Context, secretVersionResourcePath string, r io.Reader, opts ...option.ClientOption) error {
	client, err := secretmanager.NewClient(ctx, opts...)
	if err != nil {
		return err
	}
	b, err := io.ReadAll(r)
	if err != nil {
		return err
	}
	_, err = client.AddSecretVersion(ctx, &secretmanagerpb.AddSecretVersionRequest{
		Parent: secretVersionResourcePath,
		Payload: &secretmanagerpb.SecretPayload{
			Data: b,
		},
	})
	return err
}
