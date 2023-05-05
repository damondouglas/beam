/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.testinfra.pipelines.redis;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Throwables;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class RedisPush extends PTransform<PCollection<KV<String, String>>, RedisPushResult> {

  public static RedisPush create(HostAndPort hostAndPort) {
    return new RedisPush(hostAndPort);
  }

  private final HostAndPort hostAndPort;

  RedisPush(HostAndPort hostAndPort) {
    this.hostAndPort = hostAndPort;
  }

  @Override
  public RedisPushResult expand(PCollection<KV<String, String>> input) {
    PCollectionTuple pct =
        input.apply(
            RedisPush.class.getSimpleName(),
            ParDo.of(new PushFn(this))
                .withOutputTags(RedisPushResult.SUCCESS, TupleTagList.of(RedisPushResult.FAILURE)));

    return RedisPushResult.of(pct);
  }

  static class PushFn extends DoFn<KV<String, String>, KV<String, Long>> {
    private final RedisPush spec;

    private transient @MonotonicNonNull Jedis client;

    PushFn(RedisPush spec) {
      this.spec = spec;
    }

    @Setup
    public void setup() {
      client = new Jedis(spec.hostAndPort);
    }

    @ProcessElement
    public void process(@Element KV<String, String> element, MultiOutputReceiver receiver) {

      try {
        Long result = checkStateNotNull(client).lpush(element.getKey(), element.getValue());
        receiver.get(RedisPushResult.SUCCESS).output(KV.of(element.getKey(), result));
      } catch (JedisException e) {
        String message = "";
        if (e.getMessage() != null) {
          message = e.getMessage();
        }
        RedisError error =
            RedisError.builder()
                .setKey(element.getKey())
                .setValue(element.getValue())
                .setMessage(message)
                .setOperation("lpush")
                .setStackTrace(Throwables.getStackTraceAsString(e))
                .build();
        receiver.get(RedisPushResult.FAILURE).output(KV.of(element.getKey(), error));
      }
    }
  }
}
