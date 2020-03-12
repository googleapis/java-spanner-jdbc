/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.spanner.jdbc;

import static com.google.common.truth.Truth.assertThat;

import com.google.cloud.spanner.SpannerOptions;
import com.google.common.base.MoreObjects;
import com.google.spanner.v1.ExecuteSqlRequest;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JdbcQueryOptionsTest extends AbstractMockServerTest {
  @Test
  public void testDefaultOptimizerVersion() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE OPTIMIZER_VERSION")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getString("OPTIMIZER_VERSION")).isEqualTo("");
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void testOptimizerVersionInConnectionUrl() throws SQLException {
    try (java.sql.Connection connection =
        DriverManager.getConnection(
            String.format("jdbc:%s;optimizerVersion=%s", getBaseUrl(), "100"))) {
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE OPTIMIZER_VERSION")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getString("OPTIMIZER_VERSION")).isEqualTo("100");
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void testSetOptimizerVersion() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      connection.createStatement().execute("SET OPTIMIZER_VERSION='20'");
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE OPTIMIZER_VERSION")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getString("OPTIMIZER_VERSION")).isEqualTo("20");
        assertThat(rs.next()).isFalse();
      }
      connection.createStatement().execute("SET OPTIMIZER_VERSION='latest'");
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE OPTIMIZER_VERSION")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getString("OPTIMIZER_VERSION")).isEqualTo("latest");
        assertThat(rs.next()).isFalse();
      }
      connection.createStatement().execute("SET OPTIMIZER_VERSION=''");
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE OPTIMIZER_VERSION")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getString("OPTIMIZER_VERSION")).isEqualTo("");
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void testSetAndUseOptimizerVersion() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      connection.createStatement().execute("SET OPTIMIZER_VERSION='20'");
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery(SELECT_COUNT_STATEMENT.getSql())) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(COUNT_BEFORE_INSERT);
        assertThat(rs.next()).isFalse();
        // Verify that the last ExecuteSqlRequest that the server received specified optimizer
        // version 20.
        ExecuteSqlRequest request = getLastExecuteSqlRequest();
        assertThat(request.getQueryOptions().getOptimizerVersion()).isEqualTo("20");
      }

      // Do another query, but now with optimizer version 'latest'.
      connection.createStatement().execute("SET OPTIMIZER_VERSION='latest'");
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery(SELECT_COUNT_STATEMENT.getSql())) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(COUNT_BEFORE_INSERT);
        assertThat(rs.next()).isFalse();
        // Verify that the last ExecuteSqlRequest that the server received specified optimizer
        // version 'latest'.
        ExecuteSqlRequest request = getLastExecuteSqlRequest();
        assertThat(request.getQueryOptions().getOptimizerVersion()).isEqualTo("latest");
      }

      // Set the optimizer version to ''. This will do a fallback to the default, meaning that it
      // will be read from the environment variable SPANNER_OPTIMIZER_VERSION as we have nothing set
      // on the connection URL.
      connection.createStatement().execute("SET OPTIMIZER_VERSION=''");
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery(SELECT_COUNT_STATEMENT.getSql())) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(COUNT_BEFORE_INSERT);
        assertThat(rs.next()).isFalse();
        // Verify that the last ExecuteSqlRequest that the server received specified an optimizer
        // version equal to the environment default.
        ExecuteSqlRequest request = getLastExecuteSqlRequest();
        assertThat(request.getQueryOptions().getOptimizerVersion())
            .isEqualTo(MoreObjects.firstNonNull(System.getenv("SPANNER_OPTIMIZER_VERSION"), ""));
      }
    }
  }

  @Test
  public void testUseOptimizerVersionFromConnectionUrl() throws SQLException {
    try (java.sql.Connection connection =
        DriverManager.getConnection(String.format("jdbc:%s;optimizerVersion=10", getBaseUrl()))) {
      // Do a query and verify that the version from the connection URL is used.
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery(SELECT_COUNT_STATEMENT.getSql())) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(COUNT_BEFORE_INSERT);
        assertThat(rs.next()).isFalse();
        // The optimizer version used should be '10' from the connection URL.
        ExecuteSqlRequest request = getLastExecuteSqlRequest();
        assertThat(request.getQueryOptions().getOptimizerVersion()).isEqualTo("10");
      }
    }
  }

  @Test
  public void testUseOptimizerVersionFromEnvironment() throws SQLException {
    try {
      SpannerOptions.useEnvironment(
          new SpannerOptions.SpannerEnvironment() {
            @Override
            public String getOptimizerVersion() {
              return "20";
            }
          });
      try (java.sql.Connection connection =
          DriverManager.getConnection(String.format("jdbc:%s", getBaseUrl()))) {
        // Do a query and verify that the version from the environment is used.
        try (java.sql.ResultSet rs =
            connection.createStatement().executeQuery(SELECT_COUNT_STATEMENT.getSql())) {
          assertThat(rs.next()).isTrue();
          assertThat(rs.getLong(1)).isEqualTo(COUNT_BEFORE_INSERT);
          assertThat(rs.next()).isFalse();
          // Verify query options from the environment.
          ExecuteSqlRequest request = getLastExecuteSqlRequest();
          assertThat(request.getQueryOptions().getOptimizerVersion()).isEqualTo("20");
        }
        // Now set one of the query options on the connection. That option should be used in
        // combination with the other option from the environment.
        connection.createStatement().execute("SET OPTIMIZER_VERSION='30'");
        try (java.sql.ResultSet rs =
            connection.createStatement().executeQuery(SELECT_COUNT_STATEMENT.getSql())) {
          assertThat(rs.next()).isTrue();
          assertThat(rs.getLong(1)).isEqualTo(COUNT_BEFORE_INSERT);
          assertThat(rs.next()).isFalse();

          ExecuteSqlRequest request = getLastExecuteSqlRequest();
          // Optimizer version should come from the connection.
          assertThat(request.getQueryOptions().getOptimizerVersion()).isEqualTo("30");
        }
      }
    } finally {
      SpannerOptions.useDefaultEnvironment();
    }
  }
}
