package org.apache.beam.testinfra.pipelines.echo;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import com.google.protobuf.ByteString;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.beam.testinfra.pipelines.proto.echo.v1.Echo.EchoRequest;
import org.apache.beam.testinfra.pipelines.proto.echo.v1.Echo.EchoResponse;
import org.apache.beam.testinfra.pipelines.proto.echo.v1.EchoServiceGrpc;
import org.apache.beam.testinfra.pipelines.proto.echo.v1.EchoServiceGrpc.EchoServiceBlockingStub;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public class EchoIO extends PTransform<PCollection<String>, PCollectionTuple> {

  public static EchoIO create(String echoServiceAddress, String id) {
    return new EchoIO(echoServiceAddress, id);
  }

  public static final TupleTag<String> OUTPUT = new TupleTag<String>(){};
  public static final TupleTag<String> ERROR = new TupleTag<String>(){};

  private final String echoServiceAddress;
  private final String id;

  private EchoIO(String echoServiceAddress, String id) {
    this.echoServiceAddress = echoServiceAddress;
    this.id = id;
  }

  @Override
  public PCollectionTuple expand(PCollection<String> input) {
    return input.apply("echo/" + id, ParDo.of(new EchoFn(this))
        .withOutputTags(OUTPUT, TupleTagList.of(ERROR)));
  }

  private static class EchoFn extends DoFn<String, String> {

    private Counter success = Metrics.counter(EchoIO.class, "echo_success");
    private Counter resourceExhausted = Metrics.counter(EchoIO.class, "echo_failure_resource_exhausted");
    private Counter errorUnspecified = Metrics.counter(EchoIO.class, "echo_failure_unspecified");
    private final EchoIO spec;
    private transient @MonotonicNonNull ManagedChannel channel;
    private transient @MonotonicNonNull EchoServiceBlockingStub blockingStub;

    private EchoFn(EchoIO spec) {
      this.spec = spec;
    }

    @Setup
    public void setup() {
      channel = Grpc.newChannelBuilder(spec.echoServiceAddress, InsecureChannelCredentials.create()).build();
      blockingStub = EchoServiceGrpc.newBlockingStub(checkStateNotNull(channel));
    }

    @Teardown
    public void teardown() {
      ManagedChannel safeChannel = checkStateNotNull(channel);
      safeChannel.shutdown();
      try {
        safeChannel.awaitTermination(1L, TimeUnit.SECONDS);
      } catch (InterruptedException ignored) {}
    }

    @ProcessElement
    public void process(@Element String element, MultiOutputReceiver receiver) {
      try {
        EchoResponse response = checkStateNotNull(blockingStub).echo(EchoRequest.newBuilder()
            .setId(spec.id)
            .setPayload(ByteString.copyFrom(element, StandardCharsets.UTF_8))
            .build());
        receiver.get(OUTPUT).output(response.getPayload().toString());
        success.inc();
      } catch(StatusRuntimeException e) {
        receiver.get(ERROR).output(Optional.ofNullable(e.getMessage()).orElse(""));
        getFailureCounter(e).run();
      }
    }

    private Runnable getFailureCounter(StatusRuntimeException e) {
      if (e.getStatus().getCode().equals(Code.RESOURCE_EXHAUSTED)) {
        return resourceExhausted::inc;
      }
      return errorUnspecified::inc;
    }
  }
}
