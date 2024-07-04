package org.apache.beam.runners.prism;

import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.util.ReleaseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.truth.Truth.assertThat;
import static org.apache.beam.runners.prism.PrismLocator.ARCH_PROPERTY;
import static org.apache.beam.runners.prism.PrismLocator.OS_NAME_PROPERTY;

/**
 * Tests for {@link PrismLocator}.
 */
@RunWith(JUnit4.class)
public class PrismLocatorTest {

    @Test
    public void givenNoOptionsSet_buildGoal() {
        PrismLocator underTest = new PrismLocator(options());
        PrismLocator.Goal goal = underTest.buildGoal();
        String from = goal.getFrom().toString();
        String to = goal.getTo().toString();
        assertThat(from).contains("github.com/apache/beam/releases/download/");
        assertThat(from).endsWith(".zip");
        assertThat(to).contains(".apache_beam/cache/prism/bin/");
        assertThat(to).endsWith(".zip");
    }

    @Test
    public void givenNoOptionsSet_buildFileName_yieldsDefaults() {
        PrismLocator underTest = new PrismLocator(options());
        System.setProperty(OS_NAME_PROPERTY, "someos");
        System.setProperty(ARCH_PROPERTY, "somearch");
        String version = ReleaseInfo.getReleaseInfo().getSdkVersion();
        assertThat(underTest.buildFileName("zip")).isEqualTo("apache_beam-v" + version + "-prism-someos-somearch.zip");
    }

    @Test
    public void givenVersionOverride_buildFileName_includesVersionOverride() {
        PrismPipelineOptions options = options();
        options.setPrismVersionOverride("2.57.0");
        PrismLocator underTest = new PrismLocator(options);
        System.setProperty(OS_NAME_PROPERTY, "someos");
        System.setProperty(ARCH_PROPERTY, "somearch");
        assertThat(underTest.buildFileName("zip")).isEqualTo("apache_beam-v2.57.0-prism-someos-somearch.zip");
    }

    @Test
    public void givenVersionOverride_buildExpectedLocalPath_includesVersionOverride() {
        PrismPipelineOptions options = options();
        options.setPrismVersionOverride("2.57.0");
        PrismLocator underTest = new PrismLocator(options);
        String got = underTest.buildExpectedLocalPath().toString();
        assertThat(got).contains(".apache_beam/cache/prism/bin/");
        assertThat(got).contains("2.57.0");
    }

    private static PrismPipelineOptions options() {
        return PipelineOptionsFactory.create().as(PrismPipelineOptions.class);
    }
}