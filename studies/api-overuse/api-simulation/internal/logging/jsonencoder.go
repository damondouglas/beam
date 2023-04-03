package logging

import (
	"context"
	"encoding/json"
	"fmt"

	"cloud.google.com/go/logging"
)

type jsonEncoderLogger struct {
	enc    *json.Encoder
	labels map[string]string
	name   string
}

func (logger *jsonEncoderLogger) apply(entry *logging.Entry) {
	entry.Labels = logger.labels
	entry.LogName = logger.name
}

func (logger *jsonEncoderLogger) Info(_ context.Context, payload map[string]string) {
	entry := infoEntry(payload)
	logger.apply(&entry)
	_ = logger.enc.Encode(entry)
}

func (logger *jsonEncoderLogger) Debug(_ context.Context, payload map[string]string) {
	entry := debugEntry(payload)
	logger.apply(&entry)
	_ = logger.enc.Encode(entry)
}

func (logger *jsonEncoderLogger) Error(_ context.Context, payload map[string]string) {
	entry := errorEntry(payload)
	logger.apply(&entry)
	_ = logger.enc.Encode(entry)
}

func (logger *jsonEncoderLogger) WithName(name string) Logger {
	loggerCopy := logger.copyOf()
	loggerCopy.name = name
	return loggerCopy
}

func (logger *jsonEncoderLogger) WithLabels(kv ...string) Logger {
	if len(kv)%2 != 0 {
		panic(fmt.Errorf("len(kv) must be even, got: %v", len(kv)))
	}
	loggerCopy := logger.copyOf()
	if loggerCopy.labels == nil {
		loggerCopy.labels = map[string]string{}
	}
	for i := 0; i < len(kv); i += 2 {
		loggerCopy.labels[kv[i]] = kv[i+1]
	}
	return loggerCopy
}

func (logger *jsonEncoderLogger) copyOf() *jsonEncoderLogger {
	return &jsonEncoderLogger{
		enc:    logger.enc,
		labels: logger.labels,
		name:   logger.name,
	}
}
