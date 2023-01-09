/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * License); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
  java
}

val fullClassName = "org.apache.beam.sdk.expansion.service.ExpansionService"

repositories {
  mavenCentral()
  maven {
    // Required for Beam to resolve kafka dependency
    url = uri("https://packages.confluent.io/maven/")
  }
}

val autoServiceVersion = "1.0.1"
val beamVersion = "2.43.0"
val slf4jVersion = "2.0.3"

dependencies {
  implementation("org.apache.beam:beam-sdks-java-core:$beamVersion")
  implementation("org.apache.beam:beam-runners-direct-java:$beamVersion")
  implementation("org.apache.beam:beam-runners-google-cloud-dataflow-java:$beamVersion")
  implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:$beamVersion")
  annotationProcessor("com.google.auto.service:auto-service:$autoServiceVersion")
  compileOnly("com.google.auto.service:auto-service:$autoServiceVersion")
  implementation("org.slf4j:slf4j-simple:$slf4jVersion")
}

tasks.register<JavaExec>("run") {
  group = "application"
  description = "Run expansion service"
  mainClass.set(fullClassName)
  classpath = sourceSets["main"].runtimeClasspath
  args = listOf("8080", "--javaClassLookupAllowlistFile='*")
}
