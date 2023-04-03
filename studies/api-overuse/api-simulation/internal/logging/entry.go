package logging

import (
	"time"

	"cloud.google.com/go/logging"
)

func infoEntry(payload map[string]string) logging.Entry {
	return logging.Entry{
		Timestamp: time.Now(),
		Severity:  logging.Info,
		Payload:   payload,
	}
}

func debugEntry(payload map[string]string) logging.Entry {
	return logging.Entry{
		Timestamp: time.Now(),
		Severity:  logging.Debug,
		Payload:   payload,
	}
}

func errorEntry(payload map[string]string) logging.Entry {
	return logging.Entry{
		Timestamp: time.Now(),
		Severity:  logging.Error,
		Payload:   payload,
	}
}
