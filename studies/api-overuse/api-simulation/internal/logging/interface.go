package logging

import "context"

type Logger interface {
	Info(ctx context.Context, payload map[string]string)
	Debug(ctx context.Context, payload map[string]string)
	Error(ctx context.Context, payload map[string]string)
	WithName(name string) Logger
	WithLabels(kv ...string) Logger
}
