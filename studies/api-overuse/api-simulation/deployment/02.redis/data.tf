data "kubernetes_namespace" "default" {
  metadata {
    name = "quota"
  }
}
