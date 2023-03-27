<!--
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
-->

# Overview

This directory contains examples on how to model and use Beam
[Schema](https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/schemas/Schema.html)s using the Java SDK.
Since the package names and classes do not sort in a convenient order, this README provides a recommended order
of viewing.

# Prerequisites

This README and these examples assume a basic knowledge of Apache Beam, particularly the beginning sections of
[Beam Programming Guide](https://beam.apache.org/documentation/programming-guide/) up to and include the Schema section.

# What is a Schema

A Beam
[Schema](https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/schemas/Schema.html) describes
the fields of a common data structure in the Beam model called a
[Row](https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/values/Row.html).  A Row is an
immutable tuple-like data structure that represents one element in a
[PCollection](https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/values/PCollection.html).

If you've worked with databases, think of a Row as a record in a table or resulting from a query.  Correspondingly,
think of a Schema as the record's field names and their data types, such as boolean, integer, etc.  The Beam model
employs a generalized application of a [Row](https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/values/Row.html)
and its [Schema](https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/schemas/Schema.html)
to describe both database and non-database elements. As you progress through this guide, this concept and its value will
become more clear.

# Modeling a Schema

Examples illustrate various methods to model a Beam
[Schema](https://beam.apache.org/releases/javadoc/current/org/apache/beam/sdk/schemas/Schema.html).

## Imperative method

This example demonstrates an imperative method to model a Schema for illustrative purposes only and recommends declarative
approaches as discussed in the [Declarative methods](#declarative-methods) section.

See [ImperativeSchemaExampleTest.java](src/test/java/org/apache/beam/examples/schemas/model/imperative/ImperativeSchemaExampleTest.java).

## Declarative methods

These examples demonstrate declarative methods to modeling a Schema.

### AutoValueSchema

This examples demonstrate modeling a Schema from an AutoValue class.

#### Flat user types

These examples illustrate modeling from flat Schema primitive type containing custom Java classes.

See
[PrimitiveTypesContainingTest](src/test/java/org/apache/beam/examples/schemas/model/autovalueschema/PrimitiveTypesContainingTest.java)
and
[NullableTypesContainingTest](src/test/java/org/apache/beam/examples/schemas/model/autovalueschema/NullableTypesContainingTest.java).

#### Time type containing classes

This example demonstrates modeling a Schema from a Java class containing time and duration types.

See
[TimeTypesContainingTest](src/test/java/org/apache/beam/examples/schemas/model/autovalueschema/TimeTypesContainingTest.java).

#### Repeated type containing classes

This example demonstrates modeling a Schema from a Java class containing repeated types.

See
[RepeatedTypesContainingTest](src/test/java/org/apache/beam/examples/schemas/model/autovalueschema/RepeatedTypesContainingTest.java).

#### Nested type containing classes

This example demonstrates modeling a Schema from a Java class containing another Java class.

See
[NestedTypeContainingTest](src/test/java/org/apache/beam/examples/schemas/model/autovalueschema/NestedTypeContainingTest.java).

### JavaBeanSchema

This example demonstrates modeling a Schema from a Java class using its getters and setters.

See
[SimpleTest](src/test/java/org/apache/beam/examples/schemas/model/javabeanschema/SimpleTest.java)

# Schema and Row in a PTransform / DoFn context

# Encoding-related Transforms using Schema and Row

## Avro

## Json

# Dataframe-like Operations

## AddFields, DropFields, and Select

## Convert

## Filter

## Group and CoGroup

## Join

## RenameFields
