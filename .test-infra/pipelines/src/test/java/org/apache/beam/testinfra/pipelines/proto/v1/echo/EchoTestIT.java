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
package org.apache.beam.testinfra.pipelines.proto.v1.echo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.protobuf.ByteString;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.NettyChannelBuilder;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.beam.testinfra.pipelines.proto.echo.v1.Echo.EchoRequest;
import org.apache.beam.testinfra.pipelines.proto.echo.v1.EchoServiceGrpc;
import org.apache.beam.testinfra.pipelines.proto.echo.v1.EchoServiceGrpc.EchoServiceBlockingStub;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaRequest;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.CreateQuotaResponse;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaRequest;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DeleteQuotaResponse;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaRequest;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.DescribeQuotaResponse;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasRequest;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.ListQuotasResponse;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaServiceGrpc;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaServiceGrpc.QuotaServiceBlockingStub;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("IntegrationTest")
public class EchoTestIT {

  private static final String ECHO_HOST = System.getenv("ECHO_HOST");
  private static final String QUOTA_HOST = System.getenv("QUOTA_HOST");

  private static final ManagedChannel ECHO_CHANNEL = channel(ECHO_HOST);
  private static final ManagedChannel QUOTA_CHANNEL = channel(QUOTA_HOST);

  @AfterAll
  static void teardown() {
    ECHO_CHANNEL.shutdown();
    try {
      ECHO_CHANNEL.awaitTermination(1000L, TimeUnit.MILLISECONDS);
    } catch (InterruptedException ignored) {
    }

    QUOTA_CHANNEL.shutdown();
    try {
      QUOTA_CHANNEL.awaitTermination(1000L, TimeUnit.MILLISECONDS);
    } catch (InterruptedException ignored) {
    }
  }

  @Test
  void testEchoErrors() {
    String existId = UUID.randomUUID().toString();
    EchoServiceBlockingStub blockingStub = EchoServiceGrpc.newBlockingStub(ECHO_CHANNEL);
    StatusRuntimeException resourceExhausted = assertThrows(StatusRuntimeException.class, () ->
        blockingStub.echo(EchoRequest.newBuilder()
            .setId(existId)
        .setPayload(ByteString.copyFrom("test", StandardCharsets.UTF_8))
        .build()));
    assertEquals("RESOURCE_EXHAUSTED: quota exhausted for quotaID: " + existId, resourceExhausted.getMessage());
    assertEquals(Code.RESOURCE_EXHAUSTED, resourceExhausted.getStatus().getCode());

    StatusRuntimeException emptyQuotaId = assertThrows(StatusRuntimeException.class, () ->
      blockingStub.echo(EchoRequest.newBuilder()
          .setPayload(ByteString.copyFrom("test", StandardCharsets.UTF_8))
          .build()));

    assertEquals("INVALID_ARGUMENT: invalid request: Id is required but empty", emptyQuotaId.getMessage());
    assertEquals(Code.INVALID_ARGUMENT, emptyQuotaId.getStatus().getCode());
  }

  @Test
  void testQuota() {
    QuotaServiceBlockingStub blockingStub = QuotaServiceGrpc.newBlockingStub(QUOTA_CHANNEL);

    String existId = UUID.randomUUID().toString();
    Quota quota = Quota.newBuilder()
        .setId(existId)
        .setSize(100L)
        .setRefreshMillisecondsInterval(3000L)
        .build();

    CreateQuotaRequest createRequest = CreateQuotaRequest.newBuilder()
        .setQuota(quota)
        .build();

    CreateQuotaResponse created = blockingStub.create(createRequest);

    assertNotNull(created.getQuota());
    assertEquals(existId, created.getQuota().getId());

    DescribeQuotaRequest describeRequest = DescribeQuotaRequest.newBuilder()
        .setId(existId)
        .build();

    DescribeQuotaResponse queried = blockingStub.describe(describeRequest);
    assertNotNull(queried.getQuota());
    assertEquals(existId, queried.getQuota().getId());

    ListQuotasResponse listResponse = blockingStub.list(ListQuotasRequest.newBuilder().build());
    assertNotNull(listResponse.getListList());
    assertEquals(1, listResponse.getListCount());
    Quota item = listResponse.getList(0);
    assertNotNull(item);
    assertEquals(existId, item.getId());

    DeleteQuotaRequest deleteRequest = DeleteQuotaRequest.newBuilder()
        .setId(existId)
        .build();
    DeleteQuotaResponse deleteResponse = blockingStub.delete(deleteRequest);
    assertNotNull(deleteResponse.getQuota());
    assertEquals(existId, deleteResponse.getQuota().getId());
  }

  private static ManagedChannel channel(String address) {
    return Grpc.newChannelBuilder(address, InsecureChannelCredentials.create()).build();
  }
}
