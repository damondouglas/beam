// Licensed to the Apache Software Foundation (ASF) under one or more
// contributor license agreements.  See the NOTICE file distributed with
// this work for additional information regarding copyright ownership.
// The ASF licenses this file to You under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with
// the License.  You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Package httpx holds convenience methods for calling HTTP based APIs.
package httpx

import (
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"math"
	"net/http"
	"net/url"
	"strconv"
	"strings"
	"time"

	"github.com/apache/beam/test-infra/pipelines/src/main/go/internal/encoding/form"
	"golang.org/x/oauth2"
)

const (
	authorization                  = "Authorization"
	contentType                    = "Content-Type"
	contentTypeJson                = "application/json"
	contentTypeFormUrlEncoded      = "application/x-www-form-urlencoded"
	token                          = "token"
	defaultLimit              uint = 3
	accessTokenKey                 = "access_token"
	expiresInKey                   = "expires_in"
)

var (
	ContentTypeJson http.Header = map[string][]string{
		contentType: {contentTypeJson},
	}
	ContentTypeFormUrlEncoded http.Header = map[string][]string{
		contentType: {contentTypeFormUrlEncoded},
	}
)

// Credentials holds credentials to authenticate API calls.
type Credentials struct {
	Host         string `json:"host" form:"-"`
	ClientId     string `json:"client_id" form:"client_id"`
	ClientSecret string `json:"client_secret" form:"client_secret"`
}

type Client struct {
	Credentials *Credentials
	token       *oauth2.Token
	Host        string
	Base        Path
	Login       Path
}

func (client *Client) WatchAndRefreshToken(ctx context.Context, errChan chan error, ready chan struct{}) {
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()
	tick := time.Tick(time.Second)
	for {
		select {
		case <-tick:
			if err := client.refresh(ctx, ready); err != nil {
				errChan <- err
			}
		case <-ctx.Done():
			return
		}
	}
}

func (client *Client) refresh(ctx context.Context, ready chan struct{}) error {
	if client.Credentials == nil {
		return errors.New("client Credentials is nil and required")
	}
	if client.token != nil && client.token.Expiry.After(time.Now()) {
		return nil
	}
	buf := &bytes.Buffer{}
	if err := client.formPost(ctx, client.Login, buf); err != nil {
		return err
	}
	token, err := decodeToken(buf)
	if err != nil {
		return err
	}
	client.token = token
	ready <- struct{}{}
	close(ready)
	return nil
}

func (client *Client) formPost(ctx context.Context, path Path, w io.Writer) error {
	buf := &bytes.Buffer{}
	if err := form.NewEncoder(buf).Encode(client.Credentials); err != nil {
		return err
	}
	rawUrl, err := path.Encode(client.Host, client.Base)
	if err != nil {
		return err
	}
	return callInto(ctx, ContentTypeFormUrlEncoded, http.MethodPost, rawUrl, nil, buf, w)
}

func (client *Client) header() (http.Header, error) {
	if client.token == nil {
		return nil, errors.New("client token is nil and not authenticated")
	}
	h := ContentTypeJson.Clone()
	h.Set(authorization, fmt.Sprintf("%s %s", token, client.token.AccessToken))
	return h, nil
}

func (client *Client) JsonPost(ctx context.Context, path Path, params url.Values, body io.Reader, w io.Writer) error {
	h, err := client.header()
	if err != nil {
		return err
	}
	rawUrl, err := path.Encode(client.Host, client.Base)
	if err != nil {
		return err
	}
	return callInto(ctx, h, http.MethodPost, rawUrl, params, body, w)
}

func (client *Client) Get(ctx context.Context, path Path, params url.Values, w io.Writer) error {
	h, err := client.header()
	if err != nil {
		return err
	}
	rawUrl, err := path.Encode(client.Host, client.Base)
	if err != nil {
		return err
	}
	return callInto(ctx, h, http.MethodGet, rawUrl, params, nil, w)
}

func callInto(ctx context.Context, header http.Header, method string, rawUrl string, params url.Values, body io.Reader, w io.Writer) error {
	if header == nil {
		header = ContentTypeJson
	}
	var q string
	if params != nil {
		q = fmt.Sprintf("?%s", params.Encode())
	}
	u, err := url.Parse(fmt.Sprintf("%s%s", rawUrl, q))
	if err != nil {
		return err
	}
	req, err := http.NewRequestWithContext(ctx, method, u.String(), body)
	if err != nil {
		return err
	}
	for k, v := range header {
		for _, vv := range v {
			req.Header.Add(k, vv)
		}
	}
	rep := &repeater{
		limit:     defaultLimit,
		requester: http.DefaultClient,
	}
	resp, err := rep.Do(req)
	respBody := &bytes.Buffer{}
	if resp != nil {
		_, err = io.Copy(respBody, resp.Body)
	}
	hErr := httpError(resp, respBody.String(), err)
	if hErr != nil {
		return hErr
	}
	_, err = io.Copy(w, respBody)
	return err
}

// Requester makes HTTP requests.
type Requester interface {

	// Do an http.Request.
	Do(req *http.Request) (*http.Response, error)
}

type repeater struct {
	limit     uint
	requester Requester
}

// Do a request for repeater's limit applying exponential backoff for each
// error.
func (client *repeater) Do(req *http.Request) (*http.Response, error) {
	var errs []error
	n := client.limit
	pow := 0
	for {
		n--
		if n == 0 {
			return nil, errors.Join(errs...)
		}
		response, err := client.requester.Do(req)
		if err == nil {
			return response, nil
		}
		errs = append(errs, err)
		pow++
		seconds := int64(math.Pow(2, float64(pow)))
		time.Sleep(time.Second * time.Duration(1000*seconds))
	}
}

type httpErrorData struct {
	RequestURI     string `json:"request_uri"`
	RequestPayload string `json:"request_payload"`
	StatusCode     int    `json:"status_code"`
	Error          string `json:"error"`
	Status         string `json:"status"`
}

func httpError(resp *http.Response, body string, err error) error {
	if err != nil {
		return err
	}
	if resp == nil {
		return nil
	}
	if resp.StatusCode < 300 {
		return nil
	}
	e := &httpErrorData{
		Status:     resp.Status,
		StatusCode: resp.StatusCode,
	}
	if resp.Request != nil {
		e.RequestURI = resp.Request.RequestURI
		if resp.Request.Body != nil {
			b, _ := io.ReadAll(resp.Request.Body)
			e.RequestPayload = string(b)
		}
	}
	e.Error = body
	b, _ := json.Marshal(e)
	return errors.New(string(b))
}

type Path []string

type Param struct {
	Name  string
	Value string
}

func (p Path) MustPath(params ...Param) Path {
	pp, err := p.Path(params...)
	if err != nil {
		panic(err)
	}
	return pp
}

func (p Path) Path(params ...Param) (Path, error) {
	var result Path
	paramMap := map[string]string{}
	for _, k := range params {
		paramMap[k.Name] = k.Value
	}
	for _, k := range p {
		seg := k
		if strings.Contains(k, "{") {
			v, ok := paramMap[k]
			if !ok {
				return nil, fmt.Errorf("could not find value for {%s} in supplied params", k)
			}
			seg = v
		}
		result = append(result, seg)
	}
	return result, nil
}

func (p Path) Encode(host string, base Path) (string, error) {
	segs := append(base, p...)
	return url.JoinPath(host, segs...)
}

func decodeToken(r io.Reader) (*oauth2.Token, error) {
	tok := map[string]interface{}{}
	if err := json.NewDecoder(r).Decode(&tok); err != nil {
		return nil, err
	}
	accessToken, ok := tok[accessTokenKey]
	if !ok {
		return nil, fmt.Errorf("oauth2 token payload does not contain: %s", accessTokenKey)
	}

	expiresInV, ok := tok[expiresInKey]
	if !ok {
		return nil, fmt.Errorf("oauth2 token payload does not contain: %s", expiresInV)
	}

	expiresInStr := fmt.Sprint(expiresInV)
	expiresIn, err := strconv.Atoi(expiresInStr)
	if err != nil {
		return nil, fmt.Errorf("oauth2 token payload value at: %s cannot convert to an int: %w", expiresInKey, err)
	}

	return &oauth2.Token{
		AccessToken: fmt.Sprint(accessToken),
		Expiry:      time.Now().Add(time.Second * time.Duration(expiresIn*1000)),
	}, nil
}
