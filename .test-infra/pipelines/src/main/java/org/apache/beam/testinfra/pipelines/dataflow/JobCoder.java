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
package org.apache.beam.testinfra.pipelines.dataflow;

import static org.apache.beam.sdk.util.Preconditions.checkStateNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.values.Row;

public class JobCoder extends CustomCoder<Job> {

  public static JobCoder of() {
    return new JobCoder();
  }

  @Override
  public void encode(Job value, OutputStream outStream) throws CoderException, IOException {
    Row row = checkStateNotNull(Job.TO_ROW_FN.apply(value));
    Job.ROW_CODER.encode(row, outStream);
  }

  @Override
  public Job decode(InputStream inStream) throws CoderException, IOException {
    Row row = Job.ROW_CODER.decode(inStream);
    return checkStateNotNull(Job.FROM_ROW_FN.apply(row));
  }
}
