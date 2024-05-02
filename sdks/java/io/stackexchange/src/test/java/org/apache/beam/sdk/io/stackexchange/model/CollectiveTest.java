/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.sdk.io.stackexchange.model;

import static com.google.common.truth.Truth.assertThat;

public class CollectiveTest extends ModelTest<Collective> {

  @Override
  Class<Collective> getType() {
    return Collective.class;
  }

  @Override
  String getResourceName() {
    return "collective.json";
  }

  @Override
  void assertThatEquals(Collective got) {
    assertThat(got).isNotNull();
    assertThat(got.getDescription()).isEqualTo("A fake Collective for demonstration purposes");
    assertThat(got.getExternalLinks().size()).isEqualTo(2);
    CollectiveExternalLink link = got.getExternalLinks().get(0);
    assertThat(link).isNotNull();
    assertThat(link.getLink()).isNotEmpty();
    assertThat(link.getType()).isNotEmpty();
    assertThat(got.getName()).isEqualTo("Audio Bubble");
    assertThat(got.getSlug()).isEqualTo("audiobubble");
    assertThat(got.getTags()).isNotEmpty();
  }
}
