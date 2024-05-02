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

public class ShallowUserTest extends ModelTest<ShallowUser> {

  @Override
  Class<ShallowUser> getType() {
    return ShallowUser.class;
  }

  @Override
  String getResourceName() {
    return "shallow_user.json";
  }

  @Override
  void assertThatEquals(ShallowUser got) {
    assertThat(got).isNotNull();
    assertThat(got.getUserId()).isEqualTo(1);
    assertThat(got.getAcceptRate()).isEqualTo(55);
    assertThat(got.getReputation()).isEqualTo(9001);
    assertThat(got.getUserType()).isEqualTo("registered");
    assertThat(got.getProfileImage())
        .isEqualTo(
            "https://www.gravatar.com/avatar/a007be5a61f6aa8f3e85ae2fc18dd66e?d=identicon&r=PG");
    assertThat(got.getDisplayName()).isEqualTo("Example User");
    assertThat(got.getLink()).isEqualTo("http://example.stackexchange.com/users/1/example-user");
  }
}
