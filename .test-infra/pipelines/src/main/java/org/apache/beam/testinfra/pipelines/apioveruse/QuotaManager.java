package org.apache.beam.testinfra.pipelines.apioveruse;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import com.google.auto.value.AutoValue;
import io.grpc.ChannelCredentials;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaOuterClass.Quota;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaServiceGrpc;
import org.apache.beam.testinfra.pipelines.proto.quota.v1.QuotaServiceGrpc.QuotaServiceBlockingStub;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Strings;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

@AutoValue
abstract class QuotaManager {

  static Quota from(QuotaOptions options) {
    return Quota.newBuilder()
        .setId(options.getQuotaId())
        .setSize(options.getQuotaSize())
        .setRefreshMillisecondsInterval(options.getQuotaRefreshIntervalMilliseconds())
        .build();
  }
  static Builder builder(QuotaOptions options) {
    return builder()
        .setQuotaServiceAddress(options.getQuotaServiceAddress());
  }

  static Builder builder() {
    return new AutoValue_QuotaManager.Builder();
  }

  private static final ChannelCredentials DEFAULT_CREDENTIALS = InsecureChannelCredentials.create();
  private @MonotonicNonNull QuotaServiceBlockingStub cachedBlockingStub;
  private @MonotonicNonNull ManagedChannel cachedChannel;

  abstract String getQuotaServiceAddress();

  abstract ChannelCredentials getCredentials();

  QuotaServiceBlockingStub getOrCreateBlockingStub() {
    if (cachedBlockingStub == null) {
      cachedBlockingStub = QuotaServiceGrpc.newBlockingStub(getOrCreateChannel());
    }
    return cachedBlockingStub;
  }

  private ManagedChannel getOrCreateChannel() {
    if (cachedChannel == null || cachedChannel.isShutdown()) {
      cachedChannel = Grpc.newChannelBuilder(getQuotaServiceAddress(), getCredentials()).build();
    }
    return cachedChannel;
  }

  public void close() {
    ManagedChannel safeChannel = checkStateNotNull(cachedChannel);
    safeChannel.shutdown();
    try {
      safeChannel.awaitTermination(3L, TimeUnit.SECONDS);
    } catch(InterruptedException ignored) {}
  }

  @AutoValue.Builder
  static abstract class Builder {

    abstract Builder setQuotaServiceAddress(String value);

    abstract Builder setCredentials(ChannelCredentials value);
    abstract Optional<ChannelCredentials> getCredentials();

    abstract QuotaManager autoBuild();

    final QuotaManager build() {
      if (!getCredentials().isPresent()) {
        setCredentials(DEFAULT_CREDENTIALS);
      }
      return autoBuild();
    }
  }
}
