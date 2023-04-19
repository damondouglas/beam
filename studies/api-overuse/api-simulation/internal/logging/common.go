package logging

import (
	"encoding/json"
	"os"
	"time"

	"cloud.google.com/go/logging"
)

var (
	Default Logger = &jsonEncoderLogger{
		enc: json.NewEncoder(os.Stdout),
	}
)

func infoEntry(payload map[string]interface{}) logging.Entry {
	return logging.Entry{
		Timestamp: time.Now(),
		Severity:  logging.Info,
		Payload:   payload,
	}
}

func debugEntry(payload map[string]interface{}) logging.Entry {
	return logging.Entry{
		Timestamp: time.Now(),
		Severity:  logging.Debug,
		Payload:   payload,
	}
}

func errorEntry(payload map[string]interface{}) logging.Entry {
	return logging.Entry{
		Timestamp: time.Now(),
		Severity:  logging.Error,
		Payload:   payload,
	}
}
