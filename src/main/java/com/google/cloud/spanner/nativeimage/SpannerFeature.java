/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.spanner.nativeimage;

import com.google.api.gax.nativeimage.NativeImageUtils;
import com.oracle.svm.core.annotate.AutomaticFeature;
import com.oracle.svm.core.configure.ResourcesRegistry;
import org.graalvm.nativeimage.ImageSingletons;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.impl.ConfigurationCondition;

/** Registers Spanner library classes for reflection. */
@AutomaticFeature
final class SpannerFeature implements Feature {

  private static final String SPANNER_CLASS = "com.google.spanner.v1.SpannerGrpc";
  private static final String SPANNER_TEST_CLASS = "com.google.cloud.spanner.GceTestEnvConfig";
  private static final String MOCK_CLASS = "com.google.cloud.spanner.MockDatabaseAdminServiceImpl";
  private static final String CLIENT_SIDE_IMPL_CLASS =
      "com.google.cloud.spanner.connection.ClientSideStatementImpl";
  private static final String CLIENT_SIDE_VALUE_CONVERTER =
      "com.google.cloud.spanner.connection.ClientSideStatementValueConverters";
  private static final String CONNECTION_IMPL =
      "com.google.cloud.spanner.connection.ConnectionImpl";
  private static final String CLIENT_SIDE_STATEMENTS =
      "com.google.cloud.spanner.connection.ClientSideStatements";
  private static final String CONNECTION_STATEMENT_EXECUTOR =
      "com.google.cloud.spanner.connection.ConnectionStatementExecutor";
  private static final String CLIENT_SIDE_STATEMENT_NO_PARAM_EXECUTOR =
      "com.google.cloud.spanner.connection.ClientSideStatementNoParamExecutor";
  private static final String CLIENT_SIDE_STATEMENT_SET_EXECUTOR =
      "com.google.cloud.spanner.connection.ClientSideStatementSetExecutor";
  private static final String ABSTRACT_STATEMENT_PARSER =
      "com.google.cloud.spanner.connection.AbstractStatementParser";
  private static final String STATEMENT_PARSER =
      "com.google.cloud.spanner.connection.SpannerStatementParser";
  private static final String POSTGRESQL_STATEMENT_PARSER =
      "com.google.cloud.spanner.connection.PostgreSQLStatementParser";
  private static final String DIALECT = "com.google.cloud.spanner.Dialect";

  @Override
  public void beforeAnalysis(BeforeAnalysisAccess access) {
    registerSpannerTestClasses(access);
    if (access.findClassByName(CLIENT_SIDE_IMPL_CLASS) != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, CLIENT_SIDE_IMPL_CLASS);
    }
    if (access.findClassByName(CLIENT_SIDE_STATEMENT_NO_PARAM_EXECUTOR) != null) {
      NativeImageUtils.registerClassForReflection(access, CLIENT_SIDE_STATEMENT_NO_PARAM_EXECUTOR);
    }
    if (access.findClassByName(CLIENT_SIDE_STATEMENT_SET_EXECUTOR) != null) {
      NativeImageUtils.registerClassForReflection(access, CLIENT_SIDE_STATEMENT_SET_EXECUTOR);
    }
    if (access.findClassByName(CLIENT_SIDE_VALUE_CONVERTER) != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, CLIENT_SIDE_VALUE_CONVERTER);
    }
    if (access.findClassByName(CLIENT_SIDE_STATEMENTS) != null) {
      NativeImageUtils.registerClassForReflection(access, CLIENT_SIDE_STATEMENTS);
    }
    if (access.findClassByName(CONNECTION_STATEMENT_EXECUTOR) != null) {
      NativeImageUtils.registerClassForReflection(access, CONNECTION_STATEMENT_EXECUTOR);
    }
    if (access.findClassByName(CONNECTION_IMPL) != null) {
      NativeImageUtils.registerClassForReflection(access, CONNECTION_IMPL);
    }
    if (access.findClassByName(ABSTRACT_STATEMENT_PARSER) != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, ABSTRACT_STATEMENT_PARSER);
      NativeImageUtils.registerClassForReflection(access, "com.google.cloud.spanner.Dialect");
    }
    if (access.findClassByName(STATEMENT_PARSER) != null) {
      NativeImageUtils.registerConstructorsForReflection(access, STATEMENT_PARSER);
    }
    if (access.findClassByName(POSTGRESQL_STATEMENT_PARSER) != null) {
      NativeImageUtils.registerConstructorsForReflection(access, POSTGRESQL_STATEMENT_PARSER);
    }

    // JDBC Classes
    if (access.findClassByName("com.google.cloud.spanner.Statement") != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, "com.google.cloud.spanner.Statement");
    }
    if (access.findClassByName("com.google.spanner.v1.ExecuteSqlRequest.QueryOptions") != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, "com.google.spanner.v1.ExecuteSqlRequest.QueryOptions");
    }
    if (access.findClassByName("com.google.cloud.spanner.jdbc.JdbcConnection") != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, "com.google.cloud.spanner.jdbc.JdbcConnection");
    }
    if (access.findClassByName("com.google.cloud.spanner.Options") != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, "com.google.cloud.spanner.Options");
    }
    if (access.findClassByName("com.google.cloud.spanner.jdbc.ClientsideStatementValueConverters") != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, "com.google.cloud.spanner.jdbc.ClientsideStatementValueConverters");
    }
    if (access.findClassByName("com.google.cloud.spanner.jdbc.ConnectionStatementExecutorImpl") != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, "com.google.cloud.spanner.jdbc.ConnectionStatementExecutorImpl");
    }
    if (access.findClassByName("com.google.cloud.spanner.jdbc.StatementParser") != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, "com.google.cloud.spanner.jdbc.StatementParser");
    }
    if (access.findClassByName("com.google.cloud.spanner.jdbc.StatementResult") != null) {
      NativeImageUtils.registerClassHierarchyForReflection(access, "com.google.cloud.spanner.jdbc.StatementResult");
    }

    Class<?> spannerClass = access.findClassByName(SPANNER_CLASS);
    if (spannerClass != null) {
      NativeImageUtils.registerClassHierarchyForReflection(
          access, "com.google.spanner.admin.database.v1.Database");
      NativeImageUtils.registerClassHierarchyForReflection(
          access, "com.google.spanner.admin.instance.v1.Instance");
      NativeImageUtils.registerClassForReflection(
          access, "com.google.spanner.admin.database.v1.RestoreInfo");

      // Resources
      ResourcesRegistry resourcesRegistry = ImageSingletons.lookup(ResourcesRegistry.class);
      resourcesRegistry.addResources(
          ConfigurationCondition.alwaysTrue(),
          "\\Qcom/google/cloud/spanner/connection/ClientSideStatements.json\\E");
      resourcesRegistry.addResources(
          ConfigurationCondition.alwaysTrue(),
          "\\Qcom/google/cloud/spanner/connection/PG_ClientSideStatements.json\\E");
      resourcesRegistry.addResources(
          "\\Qcom/google/cloud/spanner/spi/v1/grpc-gcp-apiconfig.json\\E");
      resourcesRegistry.addResources(
          ConfigurationCondition.alwaysTrue(),
          "\\Qcom/google/cloud/spanner/connection/ITSqlScriptTest_TestQueryOptions.sql\\E");
    }
  }

  private void registerSpannerTestClasses(BeforeAnalysisAccess access) {
    Class<?> spannerTestClass = access.findClassByName(SPANNER_TEST_CLASS);
    if (spannerTestClass != null) {
      NativeImageUtils.registerConstructorsForReflection(access, SPANNER_TEST_CLASS);
    }
    Class<?> mockClass = access.findClassByName(MOCK_CLASS);
    if (mockClass != null) {
      NativeImageUtils.registerClassForReflection(
          access, "com.google.cloud.spanner.MockDatabaseAdminServiceImpl$MockBackup");
    }
  }
}
