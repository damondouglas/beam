package form

import (
	"bytes"
	"testing"
)

func TestEncoder_Encode(t *testing.T) {
	type noFormTags struct {
		Name   string
		UserId string
	}
	type hasFormTags struct {
		Name     string `form:"customer"`
		UserId   string `form:"user_id"`
		IgnoreMe string `form:"-"`
	}
	type args struct {
		v interface{}
	}
	tests := []struct {
		name    string
		args    args
		want    string
		wantErr bool
	}{
		{
			name: "noFormTags pointer",
			args: args{
				v: &noFormTags{
					Name:   "name value",
					UserId: "userid value",
				},
			},
			want:    "Name=name%20value&UserId=userid%20value",
			wantErr: false,
		},
		{
			name: "noFormTags struct",
			args: args{
				v: noFormTags{
					Name:   "name value",
					UserId: "userid value",
				},
			},
			want:    "Name=name%20value&UserId=userid%20value",
			wantErr: false,
		},
		{
			name: "hasFormTags pointer",
			args: args{
				v: &hasFormTags{
					Name:     "name value",
					UserId:   "userid value",
					IgnoreMe: "ignore me value",
				},
			},
			want:    "customer=name%20value&user_id=userid%20value",
			wantErr: false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			w := &bytes.Buffer{}
			enc := NewEncoder(w)
			if err := enc.Encode(tt.args.v); (err != nil) != tt.wantErr {
				t.Errorf("Encode() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}
