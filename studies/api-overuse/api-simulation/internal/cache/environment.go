package cache

import "github.com/apache/beam/studies/api-overuse/api-simulation/internal/environment"

var (
	Host                 environment.Variable = "CACHE_HOST"
	QuotaId              environment.Variable = "QUOTA_ID"
	QuotaSize            environment.Variable = "QUOTA_SIZE"
	QuotaRefreshInterval environment.Variable = "QUOTA_REFRESH_INTERVAL"
)
