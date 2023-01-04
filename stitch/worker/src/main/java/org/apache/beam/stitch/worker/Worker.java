package org.apache.beam.stitch.worker;

import static org.apache.beam.vendor.guava.v26_0_jre.com.google.common.base.Preconditions.checkState;

import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.beam.fn.harness.ExternalWorkerService;
import org.apache.beam.sdk.fn.server.GrpcFnServer;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker {
  private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
  private final ExternalWorkerService externalWorkerService;
  @Nullable
  private GrpcFnServer<ExternalWorkerService> server;
  Worker(PipelineOptions options) {
    externalWorkerService = new ExternalWorkerService(options);
  }
  public static void main(String[] args) throws Exception {
    PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();
    Worker worker = new Worker(options);
    worker.start();
    worker.blockUntilShutdown();
  }

  private void start() throws Exception {
    server = externalWorkerService.start();
    Optional<String> safeUrl = Optional.ofNullable(server.getApiServiceDescriptor().getUrl());
    checkState(safeUrl.isPresent());
    LOG.info("Worker service started: {}", safeUrl.get());
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      // Use stderr here since the logger may have been reset by its JVM shutdown hook.
      System.err.println("*** shutting down gRPC worker service since JVM is shutting down");
      try {
        Worker.this.stop();
      } catch (InterruptedException e) {
        throw new IllegalStateException(e);
      }
      System.err.println("*** server shut down");
    }));
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.getServer().awaitTermination();
    }
  }

  private void stop() throws InterruptedException {
    if (server != null) {
      try {
        server.close();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
