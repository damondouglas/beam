package org.apache.beam.testinfra.pipelines.apioveruse;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation.Required;

public interface ApiOverUseStudyOptions extends PipelineOptions {

  @Description("The API service host i.e. 0.0.0.0:1234")
  @Required
  String getEchoServiceAddress();
  void setEchoServiceAddress(String value);
}
