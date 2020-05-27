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

package com.google.cloud.spanner.jdbc.it;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.google.cloud.spanner.IntegrationTest;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.connection.SpannerPool;
import com.google.cloud.spanner.jdbc.ITAbstractJdbcTest;
import com.google.cloud.spanner.jdbc.JdbcSqlException;
import com.google.rpc.Code;
import com.google.spanner.v1.ExecuteSqlRequest.QueryOptions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * {@link QueryOptions} can be used with the JDBC driver on three different levels:
 *
 * <ol>
 *   <li>Specify {@link QueryOptions} in environment variables.
 *   <li>Specify {@link QueryOptions} in the connection URL or by setting these using <code>
 *       SET &lt;query_option&gt; = '&lt;value&gt;'</code> statements.
 *   <li>Specify {@link QueryOptions} in a query hint.
 * </ol>
 *
 * This class tests all three possibilities.
 */
@Category(IntegrationTest.class)
@RunWith(JUnit4.class)
public class ITJdbcQueryOptionsTest extends ITAbstractJdbcTest {
  private String connectionUriSuffix;

  @Before
  public void resetConnectionUriSuffix() {
    connectionUriSuffix = "";
  }

  @After
  public void closeSpannerPool() {
    // Close the pool after each test to prevent side effects from one test case to affect following
    // test cases.
    SpannerPool.closeSpannerPool();
  }

  @Override
  protected void appendConnectionUri(StringBuilder uri) {
    uri.append(connectionUriSuffix);
  }

  private void verifyOptimizerVersion(Connection connection, String expectedVersion)
      throws SQLException {
    try (ResultSet rs =
        connection.createStatement().executeQuery("SHOW VARIABLE OPTIMIZER_VERSION")) {
      assertThat(rs.next()).isTrue();
      assertThat(rs.getString("OPTIMIZER_VERSION")).isEqualTo(expectedVersion);
      assertThat(rs.next()).isFalse();
    }
  }

  @Test
  public void connectionUrl() throws SQLException {
    this.connectionUriSuffix = ";optimizerVersion=1";
    try (Connection connection = createConnection()) {
      verifyOptimizerVersion(connection, "1");
      try (ResultSet rs = connection.createStatement().executeQuery("SELECT 1")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(1L);
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void connectionUrlWithInvalidOptimizerVersion() throws SQLException {
    this.connectionUriSuffix = ";optimizerVersion=9999999";
    try (Connection connection = createConnection()) {
      try (ResultSet rs = connection.createStatement().executeQuery("SELECT 1")) {
        fail("missing expected exception");
      } catch (SQLException e) {
        assertThat((Throwable) e).isInstanceOf(JdbcSqlException.class);
        JdbcSqlException je = (JdbcSqlException) e;
        assertThat(je.getCode()).isEqualTo(Code.INVALID_ARGUMENT);
      }
    }
  }

  @Test
  public void setOptimizerVersion() throws SQLException {
    try (Connection connection = createConnection()) {
      verifyOptimizerVersion(connection, "");
      connection.createStatement().execute("SET OPTIMIZER_VERSION='1'");
      verifyOptimizerVersion(connection, "1");
      try (ResultSet rs = connection.createStatement().executeQuery("SELECT 1")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(1L);
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void setLatestOptimizerVersion() throws SQLException {
    try (Connection connection = createConnection()) {
      verifyOptimizerVersion(connection, "");
      connection.createStatement().execute("SET OPTIMIZER_VERSION='LATEST'");
      verifyOptimizerVersion(connection, "LATEST");
      try (ResultSet rs = connection.createStatement().executeQuery("SELECT 1")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(1L);
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void setInvalidOptimizerVersion() throws SQLException {
    try (Connection connection = createConnection()) {
      connection.createStatement().execute("SET OPTIMIZER_VERSION='9999999'");
      try (ResultSet rs = connection.createStatement().executeQuery("SELECT 1")) {
        fail("missing expected exception");
      } catch (SQLException e) {
        assertThat((Throwable) e).isInstanceOf(JdbcSqlException.class);
        JdbcSqlException je = (JdbcSqlException) e;
        assertThat(je.getCode()).isEqualTo(Code.INVALID_ARGUMENT);
      }
    }
  }

  @Test
  public void optimizerVersionInQueryHint() throws SQLException {
    try (Connection connection = createConnection()) {
      verifyOptimizerVersion(connection, "");
      try (ResultSet rs =
          connection.createStatement().executeQuery("@{optimizer_version=1} SELECT 1")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(1L);
        assertThat(rs.next()).isFalse();
      }
      try (ResultSet rs =
          connection.createStatement().executeQuery("@{optimizer_version=latest} SELECT 1")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(1L);
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void optimizerVersionInEnvironment() throws SQLException {
    try {
      SpannerOptions.useEnvironment(
          new SpannerOptions.SpannerEnvironment() {
            @Override
            public String getOptimizerVersion() {
              return "1";
            }
          });
      try (Connection connection = createConnection()) {
        // Environment query options are not visible to the connection.
        verifyOptimizerVersion(connection, "");
        try (ResultSet rs = connection.createStatement().executeQuery("SELECT 1")) {
          assertThat(rs.next()).isTrue();
          assertThat(rs.getLong(1)).isEqualTo(1L);
          assertThat(rs.next()).isFalse();
        }
      }
      // Close the pool to force re-initialization of any cached connections.
      SpannerPool.closeSpannerPool();

      // Now set an invalid version on the environment. The query will now fail.
      SpannerOptions.useEnvironment(
          new SpannerOptions.SpannerEnvironment() {
            @Override
            public String getOptimizerVersion() {
              return "9999999";
            }
          });
      try (Connection connection = createConnection()) {
        try (ResultSet rs = connection.createStatement().executeQuery("SELECT 1")) {
          fail("missing expected exception");
        } catch (SQLException e) {
          assertThat((Throwable) e).isInstanceOf(JdbcSqlException.class);
          JdbcSqlException je = (JdbcSqlException) e;
          assertThat(je.getCode()).isEqualTo(Code.INVALID_ARGUMENT);
        }
      }
    } finally {
      SpannerOptions.useDefaultEnvironment();
    }
  }
}
