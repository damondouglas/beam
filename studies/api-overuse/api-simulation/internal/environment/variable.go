package environment

import (
	"fmt"
	"os"
	"strings"
)

type Variable string

func (v Variable) Default(value string) error {
	if v.Missing() {
		return os.Setenv((string)(v), value)
	}
	return nil
}

func (v Variable) Missing() bool {
	return v.Value() == ""
}

func (v Variable) Key() string {
	return (string)(v)
}

func (v Variable) Value() string {
	return os.Getenv((string)(v))
}

func (v Variable) KeyValue() string {
	return fmt.Sprintf("%s=%s", (string)(v), v.Value())
}

func Missing(vars ...Variable) error {
	var missing []string
	for _, v := range vars {
		if v.Missing() {
			missing = append(missing, v.KeyValue())
		}
	}
	if len(missing) > 0 {
		return fmt.Errorf("variables empty but expected from environtment: %s", strings.Join(missing, "; "))
	}
	return nil
}

func Map(vars ...Variable) map[string]interface{} {
	result := map[string]interface{}{}
	for _, v := range vars {
		result[(string)(v)] = v.Value()
	}
	return result
}
