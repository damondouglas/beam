package org.apache.beam.testinfra.pipelines.apioveruse;

import java.util.UUID;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.testinfra.pipelines.echo.EchoIO;

public class ControlGroup {

  public static void main(String[] args) {
    String id = UUID.randomUUID().toString();
    ApiOverUseStudyOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(ApiOverUseStudyOptions.class);
    Pipeline p = Pipeline.create(options);
    PCollection<String> payload = p.apply("payload", Create.of("a", "b", "c"));
    PCollectionTuple echo = payload.apply("EchoIO", EchoIO.create(options.getEchoServiceAddress(), id));
    echo.get(EchoIO.ERROR).apply("debug", ParDo.of(
        new DoFn<String, String>() {
          @ProcessElement
          public void process(@Element String element) {
            System.out.println(element);
          }
        }
    ));
    p.run();
  }
}
