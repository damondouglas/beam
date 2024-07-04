package org.apache.beam.runners.prism;

import org.apache.beam.sdk.util.ReleaseInfo;
import org.apache.beam.vendor.grpc.v1p60p1.com.google.common.base.Strings;
import org.apache.beam.vendor.guava.v32_1_2_jre.com.google.common.io.ByteStreams;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

class PrismLocator {
    static final String OS_NAME_PROPERTY = "os.name";
    static final String ARCH_PROPERTY = "os.arch";
    static final String USER_HOME_PROPERTY = "user.home";

    private static final String ZIP_EXT = "zip";
    private static final String SHA512_EXT = "sha512";
    private static final Pattern ZIP_EXT_PATTERN = Pattern.compile("^(.*)\\.zip$");
    private static final ReleaseInfo RELEASE_INFO = ReleaseInfo.getReleaseInfo();
    private static final String PRISM_BIN_PATH = ".apache_beam/cache/prism/bin";
    private static final String GITHUB_DOWNLOAD_PREFIX = "https://github.com/apache/beam/releases/download/";
    private static final String GITHUB_TAG_PREFIX = "https://github.com/apache/beam/releases/tag/";

    private final PrismPipelineOptions options;

    PrismLocator(PrismPipelineOptions options) {
        this.options = options;
    }

    String resolve() {
        Goal goal = buildGoal();
        for (GoalVisitor visitor : Arrays.asList(
                new DownloadVisitor(),
                new ZipVisitor(),
                new CopyVisitor(),
                new ValidateVisitor()
        )) {
            goal = visitor.visit(goal);
        }
        return goal.to.toString();
    }

    Goal buildGoal() {
        Path from = buildGoalFrom();
        Path to = buildGoalTo();
        return new Goal(from, to);
    }

    String buildFileName(String... extensions) {
        String version = getSDKVersion();
        String resultWithoutExtension = String.format("apache_beam-v%s-prism-%s-%s", version, os(), arch());
        if (extensions.length == 0) {
            return resultWithoutExtension;
        }
        return resultWithoutExtension + "." + String.join(".", extensions);
    }

    private String getSDKVersion() {
        if (Strings.isNullOrEmpty(options.getPrismVersionOverride())) {
            return RELEASE_INFO.getSdkVersion();
        }
        return options.getPrismVersionOverride();
    }

    Path buildExpectedLocalPath() {
        return Paths.get(userHome(), PRISM_BIN_PATH, buildFileName());
    }

    private Path buildGoalFrom() {
        if (Strings.isNullOrEmpty(options.getPrismLocation())) {
            return buildDefaultDownloadPath();
        }
        return Paths.get(options.getPrismLocation());
    }

    private Path buildGoalTo() {
        if (Strings.isNullOrEmpty(options.getPrismLocation())) {
            return buildExpectedLocalPath();
        }

        Path path = Paths.get(options.getPrismLocation());
        if (!Files.isRegularFile(path)) {
            return buildExpectedLocalPath();
        }

        return path;
    }

    private Path buildDefaultDownloadPath() {
        return Paths.get(GITHUB_DOWNLOAD_PREFIX, buildFileName(ZIP_EXT));
    }

    private static String os() {
        return mustGetPropertyAsLowerCase(OS_NAME_PROPERTY);
    }

    private static String arch() {
        return mustGetPropertyAsLowerCase(ARCH_PROPERTY);
    }

    private static String userHome() {
        return mustGetPropertyAsLowerCase(USER_HOME_PROPERTY);
    }

    private static String mustGetPropertyAsLowerCase(String name) {
        return checkStateNotNull(System.getProperty(name), "System property: " + name + " not set").toLowerCase();
    }

    interface GoalVisitor {
        Goal visit(Goal goal);
    }

    static class Goal {
        private final Path from;
        private final Path to;

        Goal(Path from, Path path) {
            this.from = from;
            to = path;
        }

        Path getFrom() {
            return from;
        }

        Path getTo() {
            return to;
        }
    }

    static class DownloadVisitor implements GoalVisitor {
        @Override
        public Goal visit(Goal goal) {

            if (!isHttp(goal.from)) {
                return goal;
            }

            try {
                createDirectoryIfNeeded(goal.to);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            copy(goal.from, goal.to);

            if (!goal.from.startsWith(GITHUB_DOWNLOAD_PREFIX)) {
                return goal;
            }

            String fromWithSha512 = goal.from + "." + SHA512_EXT;
            String toWithSha512 = goal.to + "." + SHA512_EXT;
            copy(Paths.get(fromWithSha512), Paths.get(toWithSha512));

            String to = goal.to.toString();
            Matcher matcher = ZIP_EXT_PATTERN.matcher(to);
            if (matcher.matches()) {
                to = matcher.replaceAll("");
            }

            return new Goal(goal.to, Paths.get(to));
        }
    }

    static class CopyVisitor implements GoalVisitor {
        @Override
        public Goal visit(Goal goal) {
            if (isHttp(goal.from)) {
                return goal;
            }
            if (!goal.from.toFile().exists()) {
                throw new RuntimeException("file: " + goal.from.toFile() + " does not exist");
            }
            try {
                createDirectoryIfNeeded(goal.to);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            copy(goal.from, goal.to);

            return goal;
        }
    }

    static class ZipVisitor implements GoalVisitor {
        @Override
        public Goal visit(Goal goal) {
            if (isHttp(goal.from)) {
                return goal;
            }
            try {
                ZipInputStream in = new ZipInputStream(goal.from.toUri().toURL().openStream());
                FileOutputStream out = new FileOutputStream(goal.to.toFile());
                ByteStreams.copy(in, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return goal;
        }
    }

    static class ValidateVisitor implements GoalVisitor {
        @Override
        public Goal visit(Goal goal) {
            return goal;
//            File file = path.toFile();
//            if (!file.exists()) {
//                throw new IllegalStateException("Prism location does not exist: " + file.getAbsolutePath());
//            }
        }
    }

    private static boolean isHttp(Path path) {
        return path.startsWith("http");
    }

    private static void createDirectoryIfNeeded(Path path) throws IOException {
        if (isHttp(path)) {
            return;
        }
        Path parent = path.getParent();
        if (parent == null) {
            return;
        }
        if (parent.toFile().exists()) {
            return;
        }
        Files.createDirectory(parent);
    }

    private static void copy(Path from, Path to) {
        if (from.startsWith(GITHUB_TAG_PREFIX)) {
            throw new RuntimeException("URL: " + from + " is not an Apache Beam GitHub Release page URL or download URL.");
        }
        try {
            try(InputStream in = from.toUri().toURL().openStream();
                FileOutputStream out = new FileOutputStream(to.toFile())
            ) {
                ByteStreams.copy(in, out);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
