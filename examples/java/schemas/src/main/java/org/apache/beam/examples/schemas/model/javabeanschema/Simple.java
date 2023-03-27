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
package org.apache.beam.examples.schemas.model.javabeanschema;

import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.beam.sdk.schemas.JavaBeanSchema;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.joda.time.Instant;

/**
 * Example illustrating modeling a java class's {@link Schema} containing getters and setters using
 * {@link JavaBeanSchema}. Modeling a {@link Schema} using {@link JavaBeanSchema} requires:
 *
 * <ul>
 *   <li>public getters and setters
 *   <li>correct .equals() and .hashCode methods
 * </ul>
 */
@DefaultSchema(JavaBeanSchema.class)
public class Simple {

  public static Simple of(
      Boolean aBoolean, Integer anInteger, Double aDouble, String aString, Instant anInstant) {
    Simple simple = new Simple();
    simple.setABoolean(aBoolean);
    simple.setAString(aString);
    simple.setAnInteger(anInteger);
    simple.setADouble(aDouble);
    simple.setAnInstant(anInstant);

    return simple;
  }

  private String aString = "";
  private Integer anInteger = 0;

  private Double aDouble = 0.0;

  private Boolean aBoolean = false;

  private Instant anInstant = Instant.EPOCH;

  public Simple() {}

  public String getAString() {
    return aString;
  }

  public void setAString(String aString) {
    this.aString = aString;
  }

  public Integer getAnInteger() {
    return anInteger;
  }

  public void setAnInteger(Integer anInteger) {
    this.anInteger = anInteger;
  }

  public Double getADouble() {
    return aDouble;
  }

  public void setADouble(Double aDouble) {
    this.aDouble = aDouble;
  }

  public Boolean getABoolean() {
    return aBoolean;
  }

  public void setABoolean(Boolean aBoolean) {
    this.aBoolean = aBoolean;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Simple simple = (Simple) o;

    if (!Objects.equals(aString, simple.aString)) {
      return false;
    }
    if (!Objects.equals(anInteger, simple.anInteger)) {
      return false;
    }
    if (!Objects.equals(aDouble, simple.aDouble)) {
      return false;
    }
    if (!Objects.equals(anInstant, simple.anInstant)) {
      return false;
    }
    return Objects.equals(aBoolean, simple.aBoolean);
  }

  @Override
  public int hashCode() {
    int result = aString != null ? aString.hashCode() : 0;
    result = 31 * result + (anInteger != null ? anInteger.hashCode() : 0);
    result = 31 * result + (aDouble != null ? aDouble.hashCode() : 0);
    result = 31 * result + (aBoolean != null ? aBoolean.hashCode() : 0);
    result = 31 * result * (anInstant != null ? anInstant.hashCode() : 0);
    return result;
  }

  public Instant getAnInstant() {
    return anInstant;
  }

  public void setAnInstant(Instant anInstant) {
    this.anInstant = anInstant;
  }
}
