package form_test

import (
	"os"

	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/encoding/form"
)

func ExampleEncoder_Encode() {
	type user struct {
		Name     string `form:"customer"`
		UserId   string `form:"user_id"`
		IgnoreMe string `form:"-"`
	}

	_ = form.NewEncoder(os.Stdout).Encode(&user{
		Name:     "Filbert The Frog",
		UserId:   "filbert#12345",
		IgnoreMe: "should not appear in encoded stream",
	})
	// Output:
	// customer=Filbert+The+Frog&user_id=filbert%2312345
}
