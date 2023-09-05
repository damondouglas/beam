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
	"errors"
	"fmt"
	"github.com/apache/beam/sdks/v2/go/pkg/beam/io/xlang/fileio"
	"github.com/apache/beam/sdks/v2/go/test/integration"
	"google.golang.org/api/iterator"
	"path/filepath"
	"sync"
	"testing"
)

const (
	formatAvro    = "avro"
	formatCsv     = "csv"
	formatJson    = "json"
	formatParquet = "parquet"
	formatXml     = "xml"

	expansionAddrLabel = "fileio"
)

var (
	bucket    *storage.BucketHandle
	writeOpts []fileio.WriteOption
)

func checkFlags(t *testing.T) {
	if *integration.StorageBucket == "" {
		t.Skip("Missing --gcs_bucket flag. Google Cloud Storage Bucket required.")
	}
}

func setup(ctx context.Context, svc *integration.ExpansionServices) error {
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

	expansionAddr, err := svc.GetAddr(expansionAddrLabel)
	if err != nil {
		return fmt.Errorf("error querying expansion address for label: %s err %w", expansionAddrLabel, err)
	}

	writeOpts = append(writeOpts, fileio.WithWriteExpansionAddr(expansionAddr))

	// We want to delete any previously created resources prior to running tests.
	return deleteAllResources(ctx)
}

func deleteAllResources(ctx context.Context) error {
	ctx, cancel := context.WithCancel(ctx)

	var wg sync.WaitGroup
	// collate errors instead of exiting immediately
	var errs []error
	errChan := make(chan error)

	for _, format := range []string{
		formatAvro,
		formatCsv,
		formatJson,
		formatParquet,
		formatXml,
	} {
		wg.Add(1)
		go deleteAllObjects(ctx, readPrefix(format), &wg, errChan)
	}

	wg.Wait()
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

func writePrefix(format string, addl ...string) string {
	return commonPrefix(format, addl...)
}

func readPrefix(format string, addl ...string) string {
	return fmt.Sprintf("%s*", commonPrefix(format, addl...))
}

func commonPrefix(format string, addl ...string) string {
	addl = append([]string{fmt.Sprintf(`gs://%s`, *integration.StorageBucket), format}, addl...)
	return filepath.Join(addl...)
}
