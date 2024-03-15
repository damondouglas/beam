package eventarc

import (
	cloudevent "github.com/cloudevents/sdk-go/v2"
	"testing"
)

func TestEncodeDecode(t *testing.T) {
	evt := cloudevent.Event{}
	if err := evt.UnmarshalJSON([]byte(data)); err != nil {
		t.Fatal(err)
	}
	dec := NewDecoder(&evt)
	job, err := dec.Decode()
	if err != nil {
		t.Fatal(err)
	}
	t.Log(job)
}

const data = `
{
    "source": "//dataflow.googleapis.com/projects/dd-test-dd16b1b0/locations/us-central1",
    "project": "660670451013",
    "type": "google.cloud.dataflow.job.v1beta3.statusChanged",
    "job": "rrio-bigtable-it-write-rrio-n-small-size-small-1710361845353",
    "time": "2024-03-13T20:30:57.10656Z",
    "location": "us-central1",
    "id": "d20f9a8d4703f3a4313c1dde7c1b19ff",
    "datacontenttype": "application/json; charset=utf-8",
    "subject": "jobs/rrio-bigtable-it-write-rrio-n-small-size-small-1710361845353",
    "dataschema": "https://googleapis.github.io/google-cloudevents/proto/google/events/cloud/dataflow/v1beta3/data.proto#JobEventData",
    "specversion": "1.0",
    "data": {
      "payload": {
        "location": "us-central1",
        "currentStateTime": "2024-03-13T20:30:57.106560Z",
        "name": "rrio-bigtable-it-write-rrio-n-small-size-small-1710361845353",
        "labels": {
          "readorwrite": "write",
          "keyprefix": "rrio__7ae95dd1-1785-4c8a-8c5f-c0a8adcff2ac",
          "connector": "rrio"
        },
        "type": "JOB_TYPE_STREAMING",
        "environment": {
          "version": {
            "major": "8",
            "job_type": "FNAPI_STREAMING"
          },
          "userAgent": {
            "container.base_repository": "gcr.io/cloud-dataflow/v1beta3",
            "fnapi.environment.major.version": "8",
            "legacy.container.version": "beam-master-20240125",
            "java.vendor": "Debian",
            "legacy.environment.major.version": "8",
            "java.version": "11.0.22",
            "os.name": "Linux",
            "os.version": "5.15.60",
            "os.arch": "amd64",
            "version": "2.56.0-SNAPSHOT",
            "fnapi.container.version": "beam-master-20240125",
            "name": "Apache Beam SDK for Java"
          }
        },
        "jobMetadata": {
          "sdkVersion": {
            "version": "2.56.0-SNAPSHOT",
            "versionDisplayName": "Apache Beam SDK for Java",
            "sdkSupportStatus": "SUPPORTED"
          }
        },
        "clientRequestId": "20240313203055030_4661",
        "currentState": "JOB_STATE_PENDING",
        "projectId": "dd-test-dd16b1b0",
        "startTime": "2024-03-13T20:30:57.106560Z",
        "id": "2024-03-13_13_30_56-9773037660972565699",
        "createTime": "2024-03-13T20:30:57.106560Z"
      },
      "@type": "type.googleapis.com/google.events.cloud.dataflow.v1beta3.JobEventData"
    }
  },
  "resource": {
    "type": "cloud_run_revision",
    "labels": {
      "project_id": "dd-test-dd16b1b0",
      "configuration_name": "dataflow-eventarc-listener",
      "location": "us-central1",
      "service_name": "dataflow-eventarc-listener",
      "revision_name": "dataflow-eventarc-listener-00015-mt8"
    }
  },
  "timestamp": "2024-03-13T20:31:11.734559724Z",
  "severity": "DEBUG",
  "labels": {
    "stage": "cloudevent.NewEventFromHTTPRequest",
    "input": "/"
  }
}
`
