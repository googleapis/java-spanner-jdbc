/*
 * Copyright 2022 Google LLC
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import com.google.cloud.spanner.MockSpannerServiceImpl.SimulatedExecutionTime;
import com.google.cloud.spanner.connection.AbstractMockServerTest;
import com.google.cloud.spanner.jdbc.JdbcSqlExceptionFactory.JdbcSqlTimeoutException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests setting a statement timeout. This test is by default not included in unit test runs, as the
 * minimum timeout value in JDBC is 1 second, which again makes this test relatively slow.
 */
@RunWith(JUnit4.class)
public class JdbcStatementTimeoutTest extends AbstractMockServerTest {

  @Test
  public void testExecuteTimeout() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      try (Statement statement = connection.createStatement()) {
        // First verify that execute does not time out by default.
        assertFalse(statement.execute(INSERT_STATEMENT.getSql()));
        int result = statement.getUpdateCount();
        assertEquals(1, result);

        // Simulate that executeSql takes 2 seconds and set a statement timeout of 1 second.
        mockSpanner.setExecuteSqlExecutionTime(
            SimulatedExecutionTime.ofMinimumAndRandomTime(2000, 0));
        statement.setQueryTimeout(1);
        assertThrows(
            JdbcSqlTimeoutException.class, () -> statement.execute(INSERT_STATEMENT.getSql()));
      }
    }
  }

  @Test
  public void testExecuteQueryTimeout() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      try (Statement statement = connection.createStatement()) {
        // First verify that executeQuery does not time out by default.
        try (ResultSet resultSet = statement.executeQuery(SELECT_RANDOM_STATEMENT.getSql())) {
          int count = 0;
          while (resultSet.next()) {
            count++;
          }
          assertEquals(RANDOM_RESULT_SET_ROW_COUNT, count);
        }

        // Simulate that executeStreamingSql takes 2 seconds and set a statement timeout of 1
        // second.
        mockSpanner.setExecuteStreamingSqlExecutionTime(
            SimulatedExecutionTime.ofMinimumAndRandomTime(2000, 0));
        statement.setQueryTimeout(1);
        assertThrows(
            JdbcSqlTimeoutException.class,
            () -> statement.executeQuery(SELECT_RANDOM_STATEMENT.getSql()));
      }
    }
  }

  @Test
  public void testExecuteUpdateTimeout() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      try (Statement statement = connection.createStatement()) {
        // First verify that executeUpdate does not time out by default.
        assertEquals(1, statement.executeUpdate(INSERT_STATEMENT.getSql()));

        // Simulate that executeSql takes 2 seconds and set a statement timeout of 1 second.
        mockSpanner.setExecuteSqlExecutionTime(
            SimulatedExecutionTime.ofMinimumAndRandomTime(2000, 0));
        statement.setQueryTimeout(1);
        assertThrows(
            JdbcSqlTimeoutException.class,
            () -> statement.executeUpdate(INSERT_STATEMENT.getSql()));
      }
    }
  }

  @Test
  public void testExecuteBatchTimeout() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      try (Statement statement = connection.createStatement()) {
        // First verify that batch dml does not time out by default.
        statement.addBatch(INSERT_STATEMENT.getSql());
        int[] result = statement.executeBatch();
        assertArrayEquals(new int[] {1}, result);

        // Simulate that executeBatchDml takes 2 seconds and set a statement timeout of 1 second.
        mockSpanner.setExecuteBatchDmlExecutionTime(
            SimulatedExecutionTime.ofMinimumAndRandomTime(2000, 0));
        statement.setQueryTimeout(1);
        statement.addBatch(INSERT_STATEMENT.getSql());
        assertThrows(JdbcSqlTimeoutException.class, statement::executeBatch);
      }
    }
  }
}
