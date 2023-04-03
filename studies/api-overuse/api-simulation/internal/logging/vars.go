package logging

import (
	"encoding/json"
	"os"
)

var (
	Default Logger = &jsonEncoderLogger{
		enc: json.NewEncoder(os.Stdout),
	}
)
