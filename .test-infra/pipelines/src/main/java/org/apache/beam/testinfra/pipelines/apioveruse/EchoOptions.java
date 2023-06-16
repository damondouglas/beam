package org.apache.beam.testinfra.pipelines.apioveruse;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation.Required;

public interface EchoOptions extends PipelineOptions {
  @Description("The Echo API service host i.e. localhost:8080")
  @Required
  String getEchoServiceAddress();
  void setEchoServiceAddress(String value);
}
