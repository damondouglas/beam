package org.apache.beam.io.requestresponse.it;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.PeriodicImpulse;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.joda.time.Duration;
import org.joda.time.Instant;

public class BigTableIT {

    public static void main(String[] args) {
        BigTableITOptions options = PipelineOptionsFactory.fromArgs(args).as(BigTableITOptions.class);
        Pipeline pipeline = Pipeline.create(options);
        Duration interval = Duration.standardSeconds(options.getGenerateDataIntervalSeconds());
        Duration stopAfter = options.getDuration();
        pipeline.apply(PeriodicImpulse.create().withInterval(interval).stopAfter(stopAfter))
                .apply(MapElements.into(TypeDescriptors.strings()).via(Instant::toString))
                .apply(ParDo.of(new DoFn<String, String>() {
                    @ProcessElement
                    public void process(
                            @Element String element
                    ) {
                        System.out.println(element);
                    }
                }));
        pipeline.run();
    }
}
