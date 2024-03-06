package org.apache.beam.io.requestresponse.it;

import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.joda.time.Duration;

public interface BigTableITOptions extends GcpOptions {
    @Description("Test duration")
    Duration getDuration();

    void setDuration(Duration value);

    @Description("Generate data interval in seconds")
    @Default.Long(1L)
    Long getGenerateDataIntervalSeconds();

    void setGenerateDataIntervalSeconds(Long value);

    @Description("BigTable family name")
    String getBigTableFamilyName();

    void setBigTableFamilyName(String value);

    @Description("Number of elements to generate per impulse")
    Integer getNumElementsPerImpulse();

    void setNumElementsPerImpulse(Integer value);

    @Description("Size per mutation")
    Integer getMutationSize();

    void setMutationSize(Integer value);
}
