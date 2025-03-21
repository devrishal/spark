/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.protobuf

import scala.collection.JavaConverters._

import org.apache.spark.annotation.Experimental
import org.apache.spark.sql.Column

// scalastyle:off: object.name
object functions {
// scalastyle:on: object.name

  /**
   * Converts a binary column of Protobuf format into its corresponding catalyst value. The
   * Protobuf definition is provided through Protobuf <i>descriptor file</i>.
   *
   * @param data
   *   the binary column.
   * @param messageName
   *   the protobuf message name to look for in descriptor file.
   * @param descFilePath
   *   the protobuf descriptor file.
   * @param options
   * @since 3.4.0
   */
  @Experimental
  def from_protobuf(
      data: Column,
      messageName: String,
      descFilePath: String,
      options: java.util.Map[String, String]): Column = {
    new Column(
      ProtobufDataToCatalyst(data.expr, messageName, Some(descFilePath), options.asScala.toMap)
    )
  }

  /**
   * Converts a binary column of Protobuf format into its corresponding catalyst value. The
   * Protobuf definition is provided through Protobuf <i>descriptor file</i>.
   *
   * @param data
   *   the binary column.
   * @param messageName
   *   the protobuf MessageName to look for in descriptor file.
   * @param descFilePath
   *   the protobuf descriptor file.
   * @since 3.4.0
   */
  @Experimental
  def from_protobuf(data: Column, messageName: String, descFilePath: String): Column = {
    new Column(ProtobufDataToCatalyst(data.expr, messageName, descFilePath = Some(descFilePath)))
    // TODO: Add an option for user to provide descriptor file content as a buffer. This
    //       gives flexibility in how the content is fetched.
  }

  /**
   * Converts a binary column of Protobuf format into its corresponding catalyst value.
   * `messageClassName` points to Protobuf Java class. The jar containing Java class should be
   * shaded. Specifically, `com.google.protobuf.*` should be shaded to
   * `org.sparkproject.spark-protobuf.protobuf.*`.
   * https://github.com/rangadi/shaded-protobuf-classes is useful to create shaded jar from
   * Protobuf files.
   *
   * @param data
   *   the binary column.
   * @param messageClassName
   *   The full name for Protobuf Java class. E.g. <code>com.example.protos.ExampleEvent</code>.
   *   The jar with these classes needs to be shaded as described above.
   * @since 3.4.0
   */
  @Experimental
  def from_protobuf(data: Column, messageClassName: String): Column = {
    new Column(ProtobufDataToCatalyst(data.expr, messageClassName))
  }

  /**
   * Converts a binary column of Protobuf format into its corresponding catalyst value.
   * `messageClassName` points to Protobuf Java class. The jar containing Java class should be
   * shaded. Specifically, `com.google.protobuf.*` should be shaded to
   * `org.sparkproject.spark-protobuf.protobuf.*`.
   * https://github.com/rangadi/shaded-protobuf-classes is useful to create shaded jar from
   * Protobuf files.
   *
   * @param data
   *   the binary column.
   * @param messageClassName
   *   The full name for Protobuf Java class. E.g. <code>com.example.protos.ExampleEvent</code>.
   *   The jar with these classes needs to be shaded as described above.
   * @param options
   * @since 3.4.0
   */
  @Experimental
  def from_protobuf(
    data: Column,
    messageClassName: String,
    options: java.util.Map[String, String]): Column = {
    new Column(ProtobufDataToCatalyst(data.expr, messageClassName, None, options.asScala.toMap))
  }

  /**
   * Converts a column into binary of protobuf format. The Protobuf definition is provided
   * through Protobuf <i>descriptor file</i>.
   *
   * @param data
   *   the data column.
   * @param messageName
   *   the protobuf MessageName to look for in descriptor file.
   * @param descFilePath
   *   the protobuf descriptor file.
   * @since 3.4.0
   */
  @Experimental
  def to_protobuf(data: Column, messageName: String, descFilePath: String): Column = {
    new Column(CatalystDataToProtobuf(data.expr, messageName, Some(descFilePath)))
  }

  /**
   * Converts a column into binary of protobuf format. The Protobuf definition is provided
   * through Protobuf <i>descriptor file</i>.
   *
   * @param data
   *   the data column.
   * @param messageName
   *   the protobuf MessageName to look for in descriptor file.
   * @param descFilePath
   *   the protobuf descriptor file.
   * @param options
   * @since 3.4.0
   */
  @Experimental
  def to_protobuf(
    data: Column,
    messageName: String,
    descFilePath: String,
    options: java.util.Map[String, String]): Column = {
    new Column(
      CatalystDataToProtobuf(data.expr, messageName, Some(descFilePath), options.asScala.toMap)
    )
  }

  /**
   * Converts a column into binary of protobuf format.
   * `messageClassName` points to Protobuf Java class. The jar containing Java class should be
   * shaded. Specifically, `com.google.protobuf.*` should be shaded to
   * `org.sparkproject.spark-protobuf.protobuf.*`.
   * https://github.com/rangadi/shaded-protobuf-classes is useful to create shaded jar from
   * Protobuf files.
   *
   * @param data
   *   the data column.
   * @param messageClassName
   *   The full name for Protobuf Java class. E.g. <code>com.example.protos.ExampleEvent</code>.
   *   The jar with these classes needs to be shaded as described above.
   * @since 3.4.0
   */
  @Experimental
  def to_protobuf(data: Column, messageClassName: String): Column = {
    new Column(CatalystDataToProtobuf(data.expr, messageClassName))
  }

  /**
   * Converts a column into binary of protobuf format.
   * `messageClassName` points to Protobuf Java class. The jar containing Java class should be
   * shaded. Specifically, `com.google.protobuf.*` should be shaded to
   * `org.sparkproject.spark-protobuf.protobuf.*`.
   * https://github.com/rangadi/shaded-protobuf-classes is useful to create shaded jar from
   * Protobuf files.
   *
   * @param data
   *   the data column.
   * @param messageClassName
   *   The full name for Protobuf Java class. E.g. <code>com.example.protos.ExampleEvent</code>.
   *   The jar with these classes needs to be shaded as described above.
   * @param options
   * @since 3.4.0
   */
  @Experimental
  def to_protobuf(data: Column, messageClassName: String, options: java.util.Map[String, String])
  : Column = {
    new Column(CatalystDataToProtobuf(data.expr, messageClassName, None, options.asScala.toMap))
  }
}
