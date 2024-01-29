# Overview

This directory holds [Kubernetes](https://kubernetes.io) manifests and related files for provisioning
a flink cluster and job server.

# Requirements

Usage of this code requires:

- [helm](https://helm.sh/docs/intro/install/)
- Ingress configurations assume a Google Kubernetes Engine cluster
  (See [.test-infra/terraform/google-cloud-platform/google-kubernetes-engine](../terraform/google-cloud-platform/google-kubernetes-engine))
but will be potentially be ignored in other Kubernetes environments.
- IntelliJ or VS Code kubernetes plugin (optional but **highly** recommended)
- Connection to a kubernetes cluster (See: .test-infra/terraform/google-cloud-platform/google-kubernetes-engine/google-kubernetes-engine in this repository)

# Usage

To deploy the flink cluster and job server:

```
kubectl kustomize --enable-helm . | kubectl apply -f -
```

# Flink UI

First port forward the `flink-jobmanager` service:

```
kubectl port-forward service/flink-jobmanager --namespace=flink 8081:8081
```

Navigate to: http://localhost:8081

# Job Server

Port forward the `job-server`:

```
kubectl port-forward service/job-server --namespace=flink 8097:8097 8098:8098 8099:8099
```

# Python Worker Pool

Port forward the `python-worker-pool`

```
kubectl port-forward service/python-worker-pool --namespace=flink 50000:50000
```

# Test

The following show how to submit the Word Count example using the following Beam SDKS. Each
assumes you have a Google Cloud Storage bucket defined:

```
BUCKET=
```

## Java

```
./gradlew :examples:java:execute -PmainClass=org.apache.beam.examples.WordCount -PrunnerDependency=:runners:portability:java -Pexec.args="--output=gs://$BUCKET/output/java/out --runner=PortableRunner --jobEndpoint=localhost:8099 --defaultEnvironmentType=LOOPBACK"
```

## Python

```
pip install "apache_beam[gcp]"
python ./sdks/python/apache_beam/examples/wordcount.py --output=gs://$BUCKET/output/java/out --runner=PortableRunner --job_endpoint=localhost:8099 --environment_type=LOOPBACK
```
