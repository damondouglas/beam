package org.apache.beam.testinfra.pipelines.dataflow;

import com.google.api.services.dataflow.Dataflow.Projects.Jobs.List;
import com.google.api.services.dataflow.model.ListJobsResponse;
import java.io.IOException;
import org.apache.beam.sdk.schemas.JavaBeanSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.testinfra.pipelines.requests.Requester;

@DefaultSchema(JavaBeanSchema.class)
public class ListJobsRequest implements Requester<ListJobsResponse> {

  private final List request;

  public ListJobsRequest(List request) {
    this.request = request;
  }

  public List getRequest() {
    return request;
  }

  @Override
  public ListJobsResponse execute() throws IOException {
    return request.execute();
  }
}
