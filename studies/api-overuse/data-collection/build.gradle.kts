import com.github.jengelman.gradle.plugins.shadow.transformers.ApacheLicenseResourceTransformer
import org.apache.beam.gradle.BeamModulePlugin

plugins {
  java
  com.diffplug.spotless
  org.apache.beam.module
}

group = "org.apache.beam"

val autoValueVersion = "1.10"
val beamVersion = "2.46.0"
val jupiterVersion = "5.9.0"
val mockitoVersion = "5.1.1"
val slf4jVersion = "1.7.32"

spotless {
  java {
    importOrder()
    licenseHeader(BeamModulePlugin.getJavaLicenseHeader())
    removeUnusedImports()
    googleJavaFormat()
  }
}

repositories {
  mavenCentral()
  maven {
    // Required for Beam to resolve confluent dependency error
    url = uri("https://packages.confluent.io/maven/")
  }
}

dependencies {
  implementation(platform("org.apache.beam:beam-sdks-java-bom:$beamVersion"))
  implementation("org.apache.beam:beam-runners-direct-java")
  implementation("org.apache.beam:beam-runners-google-cloud-dataflow-java")
  implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform")

  implementation("org.slf4j:slf4j-jdk14:$slf4jVersion")

  compileOnly("com.google.auto.value:auto-value-annotations:$autoValueVersion")
  annotationProcessor("com.google.auto.value:auto-value:$autoValueVersion")

  testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  testCompileOnly("com.google.auto.value:auto-value-annotations:$autoValueVersion")
  testAnnotationProcessor("com.google.auto.value:auto-value:$autoValueVersion")
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}
