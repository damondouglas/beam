// Package common holds variables shared by all commands.
package common

import (
	"context"
	"errors"

	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/httpx"
	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/looker"
	"github.com/spf13/cobra"
)

var (

	// Client manages Looker resources using the Looker API.
	Client = &httpx.Client{
		Base:  looker.Spec.Base,
		Login: looker.Spec.Auth.Login,
	}

	// Resource is looker.Resource parsed from command line arguments
	Resource looker.Resource
)

// ParseResourceArgs parses the first command line argument into Resource
// using the looker.ParseResource method.
func ParseResourceArgs(_ *cobra.Command, args []string) error {
	var err error
	if len(args) == 0 {
		return errors.New("RESOURCE is missing")
	}
	Resource, err = looker.ParseResource(args[0])
	return err
}

// RunWithCredentials wraps a cobra run command calling
// Client.WatchAndRefreshToken and calling runE when ready.
func RunWithCredentials(c *cobra.Command, args []string, runE func(*cobra.Command, []string) error) error {
	ctx, cancel := context.WithCancel(c.Context())
	defer cancel()
	errChan := make(chan error)
	ready := make(chan struct{})
	go Client.WatchAndRefreshToken(ctx, errChan, ready)
	for {
		select {
		case <-ready:
			if err := runE(c, args); err != nil {
				errChan <- err
			}
			return nil
		case err := <-errChan:
			return err
		case <-ctx.Done():
			return nil
		}
	}
}
