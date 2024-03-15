package eventarc

import (
	cloudevent "github.com/cloudevents/sdk-go/v2"
	dataflow "github.com/googleapis/google-cloudevents-go/cloud/dataflowdatav1beta3"
	"google.golang.org/protobuf/encoding/protojson"
)

var (
	pj = protojson.UnmarshalOptions{DiscardUnknown: true}
)

func NewDecoder(evt *cloudevent.Event) *Decoder {
	return &Decoder{
		evt: evt,
	}
}

type Decoder struct {
	evt *cloudevent.Event
}

func (dec *Decoder) Decode() (*dataflow.JobEventData, error) {
	var result dataflow.JobEventData
	if err := pj.Unmarshal(dec.evt.Data(), &result); err != nil {
		return nil, err
	}
	return &result, nil
}
