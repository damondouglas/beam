package xlangx

import (
	"reflect"

	"github.com/apache/beam/sdks/v2/go/pkg/beam"
)

type DynamicStruct reflect.Value

func NewDynamicStruct(fields ...reflect.StructField) DynamicStruct {
	typ := reflect.StructOf(fields)
	v := reflect.New(typ).Elem()
	return (DynamicStruct)(v)
}

func (c DynamicStruct) SetString(fieldName, value string) {
	v := (reflect.Value)(c)
	v.FieldByName(fieldName).SetString(value)
}

func (c DynamicStruct) Set(fieldName string, value any) {
	v := (reflect.Value)(c)
	v.FieldByName(fieldName).Set(reflect.ValueOf(value))
}

func (c DynamicStruct) CrossLanguagePayload() []byte {
	v := (reflect.Value)(c)
	vv := v.Addr().Interface()
	return beam.CrossLanguagePayload(vv)
}
