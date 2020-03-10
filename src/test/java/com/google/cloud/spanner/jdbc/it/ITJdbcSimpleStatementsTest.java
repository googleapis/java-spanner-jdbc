/*
 * Copyright 2019 Google LLC
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
import com.google.cloud.spanner.jdbc.ITAbstractJdbcTest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Test executing simple statements through JDBC. */
@RunWith(JUnit4.class)
@Category(IntegrationTest.class)
public class ITJdbcSimpleStatementsTest extends ITAbstractJdbcTest {
  @Test
  public void testSelect1() throws SQLException {
    try (Connection connection = createConnection()) {
      try (ResultSet rs = connection.createStatement().executeQuery("select 1")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getInt(1)).isEqualTo(1);
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void testSelect1PreparedStatement() throws SQLException {
    try (Connection connection = createConnection()) {
      try (PreparedStatement ps = connection.prepareStatement("select 1")) {
        try (ResultSet rs = ps.executeQuery()) {
          assertThat(rs.next()).isTrue();
          assertThat(rs.getInt(1)).isEqualTo(1);
          assertThat(rs.next()).isFalse();
        }
      }
    }
  }

  @Test
  public void testPreparedStatement() throws SQLException {
    String sql =
        "select * from (select 1 as number union all select 2 union all select 3) numbers where number=?";
    try (Connection connection = createConnection()) {
      try (PreparedStatement ps = connection.prepareStatement(sql)) {
        for (int i = 1; i <= 3; i++) {
          ps.setInt(1, i);
          try (ResultSet rs = ps.executeQuery()) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getInt(1)).isEqualTo(i);
            assertThat(rs.next()).isFalse();
          }
        }
      }
    }
  }

  @Test
  public void testBatchedDdlStatements() throws SQLException {
    // Execute a successful batch of DDL statements.
    try (Connection connection = createConnection()) {
      try (Statement statement = connection.createStatement()) {
        statement.addBatch(
            "CREATE TABLE FOO1 (ID INT64 NOT NULL, NAME STRING(100)) PRIMARY KEY (ID)");
        statement.addBatch(
            "CREATE TABLE FOO2 (ID INT64 NOT NULL, NAME STRING(100)) PRIMARY KEY (ID)");
        int[] updateCounts = statement.executeBatch();
        assertThat(updateCounts)
            .asList()
            .containsExactly(Statement.SUCCESS_NO_INFO, Statement.SUCCESS_NO_INFO);
      }
      try (ResultSet rs =
          connection
              .createStatement()
              .executeQuery(
                  "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='' AND TABLE_NAME LIKE 'FOO%'")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(2L);
      }
    }
    // Execute a batch of DDL statements that contains a statement that will fail.
    try (Connection connection = createConnection()) {
      // First create a couple of test records that will cause the index creation to fail.
      try (Statement statement = connection.createStatement()) {
        statement.executeUpdate("INSERT INTO FOO1 (ID, NAME) VALUES (1,'TEST')");
        statement.executeUpdate("INSERT INTO FOO1 (ID, NAME) VALUES (2,'TEST')");
      }
      boolean gotExpectedException = false;
      try (Statement statement = connection.createStatement()) {
        statement.addBatch(
            "CREATE TABLE FOO3 (ID INT64 NOT NULL, NAME STRING(100)) PRIMARY KEY (ID)");
        statement.addBatch("CREATE UNIQUE INDEX IDX_FOO1_UNIQUE ON FOO1 (NAME)");
        statement.executeBatch();
      } catch (SQLException e) {
        gotExpectedException = true;
      }
      assertThat(gotExpectedException).isTrue();
      // The table should have been created, the index should not.
      try (ResultSet rs =
          connection
              .createStatement()
              .executeQuery(
                  "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='' AND TABLE_NAME LIKE 'FOO%'")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(3L);
      }
      try (ResultSet rs =
          connection
              .createStatement()
              .executeQuery(
                  "SELECT COUNT(*) FROM INFORMATION_SCHEMA.INDEXES WHERE TABLE_SCHEMA='' AND INDEX_NAME='IDX_FOO1_UNIQUE'")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getLong(1)).isEqualTo(0L);
      }
    }
  }

  @Test
  public void testAddBatchWhenAlreadyInBatch() {
    try (Connection connection = createConnection()) {
      connection.createStatement().execute("START BATCH DML");
      connection.createStatement().addBatch("INSERT INTO Singers (SingerId) VALUES (-1)");
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(e.getMessage())
          .contains(
              "Calling addBatch() is not allowed when a DML or DDL batch has been started on the connection.");
    }
  }
}
