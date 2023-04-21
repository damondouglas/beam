package k8s

import (
	"context"

	corev1 "k8s.io/api/core/v1"
	k8s "sigs.k8s.io/controller-runtime/pkg/client"
)

type Namespace struct {
	name     string
	internal k8s.Client
}

func (client *Client) Namespace(name string) *Namespace {
	return &Namespace{
		name:     name,
		internal: client.internal,
	}
}

func (ns *Namespace) Exists(ctx context.Context) error {
	key := k8s.ObjectKey{
		Name: ns.name,
	}
	err := ns.internal.Get(ctx, key, &corev1.Namespace{})
	return err
}
