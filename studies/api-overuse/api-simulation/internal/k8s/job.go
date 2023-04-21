package k8s

import (
	"context"
	"fmt"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/environment"
	batchv1 "k8s.io/api/batch/v1"
	corev1 "k8s.io/api/core/v1"
	"k8s.io/apimachinery/pkg/api/errors"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	k8s "sigs.k8s.io/controller-runtime/pkg/client"
)

type Jobs struct {
	namespace *Namespace
	internal  k8s.Client
}

type Spec struct {
	Name          string
	ContainerName string
	Image         string
	Labels        map[string]string
	Command       []string
	RestartOnFail bool
	Environment   []environment.Variable
}

func (client *Client) Jobs(ns *Namespace) *Jobs {
	return &Jobs{
		namespace: ns,
		internal:  client.internal,
	}
}

func (js *Jobs) init(ctx context.Context, name string) (*batchv1.Job, error) {
	if err := js.namespace.Exists(ctx); err != nil && errors.IsNotFound(err) {
		return nil, fmt.Errorf("namespace: %s does not exist", js.namespace.name)
	}
	return &batchv1.Job{
		ObjectMeta: metav1.ObjectMeta{
			Name:      name,
			Namespace: js.namespace.name,
		},
	}, nil
}

func (js *Jobs) Start(ctx context.Context, spec *Spec) (*batchv1.Job, error) {
	job, err := js.init(ctx, spec.Name)
	if err != nil {
		return nil, err
	}

	restartPolicy := corev1.RestartPolicyNever
	if spec.RestartOnFail {
		restartPolicy = corev1.RestartPolicyOnFailure
	}

	var envs []corev1.EnvVar
	for _, v := range spec.Environment {
		envs = append(envs, corev1.EnvVar{
			Name:  v.Key(),
			Value: v.Value(),
		})
	}

	job.Spec = batchv1.JobSpec{
		Selector: &metav1.LabelSelector{
			MatchLabels: spec.Labels,
		},
		Template: corev1.PodTemplateSpec{
			Spec: corev1.PodSpec{
				Containers: []corev1.Container{
					{
						Name:    spec.ContainerName,
						Image:   spec.Image,
						Command: spec.Command,
						Env:     envs,
					},
				},
				RestartPolicy: restartPolicy,
			},
		},
	}

	if err := js.internal.Create(ctx, job); err != nil {
		return nil, err
	}

	return job, nil
}
