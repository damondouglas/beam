namespace          = "quota"
redis_service_name = "redis-master"
image_tag          = "latest"

echo_service = {
  name     = "echo"
  image_id = "echo"
  port     = 8080
}

quota_service = {
  name               = "quota"
  image_id           = "quota"
  port               = 8080
  refresher_image_id = "refresher"
}
