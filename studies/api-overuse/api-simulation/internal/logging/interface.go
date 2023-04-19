package logging

import "context"

type Logger interface {
	Info(ctx context.Context, payload map[string]interface{})
	Debug(ctx context.Context, payload map[string]interface{})
	Error(ctx context.Context, payload map[string]interface{})
	WithName(name string) Logger
	WithLabels(kv ...string) Logger
}
