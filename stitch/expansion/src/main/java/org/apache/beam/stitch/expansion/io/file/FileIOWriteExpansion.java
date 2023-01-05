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
package org.apache.beam.stitch.expansion.io.file;

import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;

public class FileIOWriteExpansion extends PTransform<PCollection<String>, PDone> {

  final String filenamePrefix;

  public FileIOWriteExpansion(String filenamePrefix) {
    this.filenamePrefix = filenamePrefix;
  }

  @Override
  public PDone expand(PCollection<String> input) {
    input.apply("write", TextIO.write().to(filenamePrefix));
    return PDone.in(input.getPipeline());
  }
}
