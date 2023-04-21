package integration

import (
	"log"
	"testing"

	"github.com/apache/beam/studies/api-overuse/api-simulation/internal/environment"
	quota_v1 "github.com/apache/beam/studies/api-overuse/api-simulation/internal/proto/quota/v1"
)

var (
	quotaAddress environment.Variable = "QUOTA_HOST"
	client       *quota_v1.QuotaServiceClient
)

func init() {
	if err := environment.Missing(quotaAddress); err != nil {
		log.Fatal(err)
	}
}

func TestCreateQuota(t *testing.T) {

}
