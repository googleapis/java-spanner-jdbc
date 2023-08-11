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

package com.google.cloud.spanner.jdbc;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.cloud.spanner.Dialect;
import com.google.cloud.spanner.ErrorCode;
import com.google.cloud.spanner.SpannerExceptionFactory;
import com.google.cloud.spanner.Type;
import com.google.cloud.spanner.connection.AbstractStatementParser;
import com.google.cloud.spanner.connection.Connection;
import com.google.cloud.spanner.connection.StatementResult;
import com.google.cloud.spanner.connection.StatementResult.ResultType;
import com.google.cloud.spanner.jdbc.JdbcSqlExceptionFactory.JdbcSqlExceptionImpl;
import com.google.common.collect.ImmutableList;
import com.google.rpc.Code;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.stubbing.Answer;

@RunWith(Parameterized.class)
public class JdbcStatementTest {
  private static final String SELECT = "SELECT 1";
  private static final String UPDATE = "UPDATE FOO SET BAR=1 WHERE BAZ=2";
  private static final String LARGE_UPDATE = "UPDATE FOO SET BAR=1 WHERE 1=1";
  private static final String DML_RETURNING_GSQL = "UPDATE FOO SET BAR=1 WHERE 1=1 THEN RETURN *";
  private static final String DML_RETURNING_PG = "UPDATE FOO SET BAR=1 WHERE 1=1 RETURNING *";
  private static final String DDL = "CREATE INDEX FOO ON BAR(ID)";

  @Parameter public Dialect dialect;

  @Parameters(name = "dialect = {0}")
  public static Object[] data() {
    return Dialect.values();
  }

  private String getDmlReturningSql() {
    if (dialect == Dialect.GOOGLE_STANDARD_SQL) {
      return DML_RETURNING_GSQL;
    }
    return DML_RETURNING_PG;
  }

  @SuppressWarnings("unchecked")
  private JdbcStatement createStatement() throws SQLException {
    Connection spanner = mock(Connection.class);
    when(spanner.getDialect()).thenReturn(dialect);

    final String DML_RETURNING_SQL = getDmlReturningSql();

    com.google.cloud.spanner.ResultSet resultSet = mock(com.google.cloud.spanner.ResultSet.class);
    when(resultSet.next()).thenReturn(true, false);
    when(resultSet.getColumnType(0)).thenReturn(Type.int64());
    when(resultSet.getLong(0)).thenReturn(1L);

    StatementResult selectResult = mock(StatementResult.class);
    when(selectResult.getResultType()).thenReturn(ResultType.RESULT_SET);
    when(selectResult.getResultSet()).thenReturn(resultSet);
    when(spanner.execute(com.google.cloud.spanner.Statement.of(SELECT))).thenReturn(selectResult);

    StatementResult updateResult = mock(StatementResult.class);
    when(updateResult.getResultType()).thenReturn(ResultType.UPDATE_COUNT);
    when(updateResult.getUpdateCount()).thenReturn(1L);
    when(spanner.execute(com.google.cloud.spanner.Statement.of(UPDATE))).thenReturn(updateResult);

    StatementResult largeUpdateResult = mock(StatementResult.class);
    when(largeUpdateResult.getResultType()).thenReturn(ResultType.UPDATE_COUNT);
    when(largeUpdateResult.getUpdateCount()).thenReturn(Integer.MAX_VALUE + 1L);
    when(spanner.execute(com.google.cloud.spanner.Statement.of(LARGE_UPDATE)))
        .thenReturn(largeUpdateResult);

    com.google.cloud.spanner.ResultSet dmlReturningResultSet =
        mock(com.google.cloud.spanner.ResultSet.class);
    when(dmlReturningResultSet.next()).thenReturn(true, false);
    when(dmlReturningResultSet.getColumnCount()).thenReturn(1);
    when(dmlReturningResultSet.getColumnType(0)).thenReturn(Type.int64());
    when(dmlReturningResultSet.getLong(0)).thenReturn(1L);

    StatementResult dmlReturningResult = mock(StatementResult.class);
    when(dmlReturningResult.getResultType()).thenReturn(ResultType.RESULT_SET);
    when(dmlReturningResult.getResultSet()).thenReturn(dmlReturningResultSet);
    when(spanner.execute(com.google.cloud.spanner.Statement.of(DML_RETURNING_SQL)))
        .thenReturn(dmlReturningResult);

    StatementResult ddlResult = mock(StatementResult.class);
    when(ddlResult.getResultType()).thenReturn(ResultType.NO_RESULT);
    when(spanner.execute(com.google.cloud.spanner.Statement.of(DDL))).thenReturn(ddlResult);

    when(spanner.executeQuery(com.google.cloud.spanner.Statement.of(SELECT))).thenReturn(resultSet);
    when(spanner.executeQuery(com.google.cloud.spanner.Statement.of(UPDATE)))
        .thenThrow(
            SpannerExceptionFactory.newSpannerException(ErrorCode.INVALID_ARGUMENT, "not a query"));
    when(spanner.executeQuery(com.google.cloud.spanner.Statement.of(DML_RETURNING_SQL)))
        .thenReturn(dmlReturningResultSet);
    when(spanner.executeQuery(com.google.cloud.spanner.Statement.of(DDL)))
        .thenThrow(
            SpannerExceptionFactory.newSpannerException(ErrorCode.INVALID_ARGUMENT, "not a query"));

    when(spanner.executeUpdate(com.google.cloud.spanner.Statement.of(UPDATE))).thenReturn(1L);
    when(spanner.executeUpdate(com.google.cloud.spanner.Statement.of(SELECT)))
        .thenThrow(
            SpannerExceptionFactory.newSpannerException(
                ErrorCode.INVALID_ARGUMENT, "not an update"));
    when(spanner.executeUpdate(com.google.cloud.spanner.Statement.of(DDL)))
        .thenThrow(
            SpannerExceptionFactory.newSpannerException(
                ErrorCode.INVALID_ARGUMENT, "not an update"));
    when(spanner.executeUpdate(com.google.cloud.spanner.Statement.of(DML_RETURNING_SQL)))
        .thenThrow(
            SpannerExceptionFactory.newSpannerException(
                ErrorCode.FAILED_PRECONDITION, "cannot execute dml returning over executeUpdate"));

    when(spanner.executeBatchUpdate(anyList()))
        .thenAnswer(
            (Answer<long[]>)
                invocation -> {
                  List<com.google.cloud.spanner.Statement> statements =
                      (List<com.google.cloud.spanner.Statement>) invocation.getArguments()[0];
                  if (statements.isEmpty()
                      || AbstractStatementParser.getInstance(dialect)
                          .isDdlStatement(statements.get(0).getSql())) {
                    return new long[0];
                  }
                  long[] res =
                      new long
                          [((List<com.google.cloud.spanner.Statement>) invocation.getArguments()[0])
                              .size()];
                  Arrays.fill(res, 1L);
                  return res;
                });

    JdbcConnection connection = mock(JdbcConnection.class);
    when(connection.getDialect()).thenReturn(dialect);
    when(connection.getParser()).thenReturn(AbstractStatementParser.getInstance(dialect));
    when(connection.getSpannerConnection()).thenReturn(spanner);
    return new JdbcStatement(connection);
  }

  @Test
  public void testQueryTimeout() throws SQLException {
    final String select = "SELECT 1";
    JdbcConnection connection = mock(JdbcConnection.class);
    when(connection.getDialect()).thenReturn(dialect);
    Connection spanner = mock(Connection.class);
    when(connection.getSpannerConnection()).thenReturn(spanner);
    StatementResult result = mock(StatementResult.class);
    when(result.getResultType()).thenReturn(ResultType.RESULT_SET);
    when(result.getResultSet()).thenReturn(mock(com.google.cloud.spanner.ResultSet.class));
    when(spanner.execute(com.google.cloud.spanner.Statement.of(select))).thenReturn(result);
    try (Statement statement = new JdbcStatement(connection)) {
      assertThat(statement.getQueryTimeout()).isEqualTo(0);
      statement.setQueryTimeout(1);
      assertThat(statement.getQueryTimeout()).isEqualTo(1);
      statement.setQueryTimeout(99);
      assertThat(statement.getQueryTimeout()).isEqualTo(99);
      statement.setQueryTimeout(0);
      assertThat(statement.getQueryTimeout()).isEqualTo(0);
    }

    when(spanner.getStatementTimeout(TimeUnit.SECONDS)).thenReturn(1L);
    when(spanner.getStatementTimeout(TimeUnit.MILLISECONDS)).thenReturn(1000L);
    when(spanner.getStatementTimeout(TimeUnit.MICROSECONDS)).thenReturn(1000000L);
    when(spanner.getStatementTimeout(TimeUnit.NANOSECONDS)).thenReturn(1000000000L);
    when(spanner.hasStatementTimeout()).thenReturn(true);
    try (Statement statement = new JdbcStatement(connection)) {
      assertThat(statement.getQueryTimeout()).isEqualTo(0);
      statement.execute(select);
      // statement has no timeout, so it should also not be set on the connection
      verify(spanner, never()).setStatementTimeout(1L, TimeUnit.SECONDS);
    }
    try (Statement statement = new JdbcStatement(connection)) {
      // now set a query timeout that should temporarily applied to the connection
      statement.setQueryTimeout(2);
      statement.execute(select);
      // assert that it is temporarily set to 2 seconds, and then back to the original 1 second
      // value
      verify(spanner).setStatementTimeout(2L, TimeUnit.SECONDS);
      verify(spanner).setStatementTimeout(1L, TimeUnit.SECONDS);
    }
  }

  @Test
  public void testExecuteWithSelectStatement() throws SQLException {
    Statement statement = createStatement();
    boolean res = statement.execute(SELECT);
    assertThat(res).isTrue();
    assertThat(statement.getUpdateCount()).isEqualTo(JdbcConstants.STATEMENT_RESULT_SET);
    try (ResultSet rs = statement.getResultSet()) {
      assertThat(rs).isNotNull();
      assertThat(rs.next()).isTrue();
      assertThat(rs.getLong(1)).isEqualTo(1L);
    }
  }

  @Test
  public void testExecuteWithUpdateStatement() throws SQLException {
    Statement statement = createStatement();
    boolean res = statement.execute(UPDATE);
    assertThat(res).isFalse();
    assertThat(statement.getResultSet()).isNull();
    assertThat(statement.getUpdateCount()).isEqualTo(1);
    assertThat(statement.execute(LARGE_UPDATE)).isFalse();
    assertThat(statement.getResultSet()).isNull();
    try {
      statement.getUpdateCount();
      fail("missing expected exception");
    } catch (JdbcSqlExceptionImpl e) {
      assertThat(e.getCode()).isEqualTo(Code.OUT_OF_RANGE);
    }
    assertThat(statement.getLargeUpdateCount()).isEqualTo(Integer.MAX_VALUE + 1L);
  }

  @Test
  public void testExecuteWithDdlStatement() throws SQLException {
    Statement statement = createStatement();
    boolean res = statement.execute(DDL);
    assertThat(res).isFalse();
    assertThat(statement.getResultSet()).isNull();
    assertThat(statement.getUpdateCount()).isEqualTo(JdbcConstants.STATEMENT_NO_RESULT);
  }

  @Test
  public void testExecuteWithDmlReturningStatement() throws SQLException {
    Statement statement = createStatement();
    boolean res = statement.execute(getDmlReturningSql());
    assertTrue(res);
    assertEquals(statement.getUpdateCount(), JdbcConstants.STATEMENT_RESULT_SET);
    try (ResultSet rs = statement.getResultSet()) {
      assertNotNull(rs);
      assertTrue(rs.next());
      assertEquals(rs.getLong(1), 1L);
      assertFalse(rs.next());
    }
  }

  @Test
  public void testExecuteWithGeneratedKeys() throws SQLException {
    Statement statement = createStatement();
    assertThat(statement.execute(UPDATE, Statement.NO_GENERATED_KEYS)).isFalse();
    ResultSet keys = statement.getGeneratedKeys();
    assertThat(keys.next()).isFalse();
    try {
      statement.execute(UPDATE, Statement.RETURN_GENERATED_KEYS);
      fail("missing expected exception");
    } catch (SQLFeatureNotSupportedException e) {
      // Ignore, this is the expected exception.
    }
  }

  @Test
  public void testExecuteQuery() throws SQLException {
    Statement statement = createStatement();
    try (ResultSet rs = statement.executeQuery(SELECT)) {
      assertThat(rs).isNotNull();
      assertThat(rs.next()).isTrue();
      assertThat(rs.getLong(1)).isEqualTo(1L);
    }
  }

  @Test
  public void testExecuteQueryWithUpdateStatement() {
    try {
      Statement statement = createStatement();
      statement.executeQuery(UPDATE);
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(
              JdbcExceptionMatcher.matchCodeAndMessage(Code.INVALID_ARGUMENT, "not a query")
                  .matches(e))
          .isTrue();
    }
  }

  @Test
  public void testExecuteQueryWithDmlReturningStatement() throws SQLException {
    Statement statement = createStatement();
    try (ResultSet rs = statement.executeQuery(getDmlReturningSql())) {
      assertNotNull(rs);
      assertTrue(rs.next());
      assertEquals(rs.getLong(1), 1L);
      assertFalse(rs.next());
    }
  }

  @Test
  public void testExecuteQueryWithDdlStatement() {
    try {
      Statement statement = createStatement();
      statement.executeQuery(DDL);
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(
              JdbcExceptionMatcher.matchCodeAndMessage(Code.INVALID_ARGUMENT, "not a query")
                  .matches(e))
          .isTrue();
    }
  }

  @Test
  public void testExecuteUpdate() throws SQLException {
    Statement statement = createStatement();
    assertThat(statement.executeUpdate(UPDATE)).isEqualTo(1);
    try {
      statement.executeUpdate(LARGE_UPDATE);
      fail("missing expected exception");
    } catch (JdbcSqlExceptionImpl e) {
      assertThat(e.getCode()).isEqualTo(Code.OUT_OF_RANGE);
    }
  }

  @Test
  public void testInternalExecuteUpdate() throws SQLException {
    JdbcConnection connection = mock(JdbcConnection.class);
    when(connection.getDialect()).thenReturn(dialect);
    Connection spannerConnection = mock(Connection.class);
    when(connection.getSpannerConnection()).thenReturn(spannerConnection);
    com.google.cloud.spanner.Statement updateStatement =
        com.google.cloud.spanner.Statement.of(UPDATE);
    com.google.cloud.spanner.Statement largeUpdateStatement =
        com.google.cloud.spanner.Statement.of(LARGE_UPDATE);
    when(spannerConnection.executeUpdate(updateStatement)).thenReturn(1L);
    when(spannerConnection.executeUpdate(largeUpdateStatement)).thenReturn(Integer.MAX_VALUE + 1L);
    try (JdbcStatement statement = new JdbcStatement(connection)) {
      assertThat(statement.executeUpdate(updateStatement)).isEqualTo(1);
      try {
        statement.executeUpdate(largeUpdateStatement);
        fail("missing expected exception");
      } catch (JdbcSqlExceptionImpl e) {
        assertThat(e.getCode()).isEqualTo(Code.OUT_OF_RANGE);
      }
    }
  }

  @Test
  public void testInternalExecuteLargeUpdate() throws SQLException {
    JdbcConnection connection = mock(JdbcConnection.class);
    when(connection.getDialect()).thenReturn(dialect);
    Connection spannerConnection = mock(Connection.class);
    when(connection.getSpannerConnection()).thenReturn(spannerConnection);
    com.google.cloud.spanner.Statement updateStatement =
        com.google.cloud.spanner.Statement.of(UPDATE);
    com.google.cloud.spanner.Statement largeUpdateStatement =
        com.google.cloud.spanner.Statement.of(LARGE_UPDATE);
    when(spannerConnection.executeUpdate(updateStatement)).thenReturn(1L);
    when(spannerConnection.executeUpdate(largeUpdateStatement)).thenReturn(Integer.MAX_VALUE + 1L);
    try (JdbcStatement statement = new JdbcStatement(connection)) {
      assertThat(statement.executeLargeUpdate(updateStatement)).isEqualTo(1);
      assertThat(statement.executeLargeUpdate(largeUpdateStatement))
          .isEqualTo(Integer.MAX_VALUE + 1L);
    }
  }

  @Test
  public void testExecuteLargeUpdate() throws SQLException {
    Statement statement = createStatement();
    assertThat(statement.executeLargeUpdate(UPDATE)).isEqualTo(1L);
    assertThat(statement.executeLargeUpdate(LARGE_UPDATE)).isEqualTo(Integer.MAX_VALUE + 1L);

    assertThat(statement.executeLargeUpdate(UPDATE, Statement.NO_GENERATED_KEYS)).isEqualTo(1L);
    assertThat(statement.executeLargeUpdate(LARGE_UPDATE, Statement.NO_GENERATED_KEYS))
        .isEqualTo(Integer.MAX_VALUE + 1L);

    assertThat(statement.executeLargeUpdate(UPDATE, new int[0])).isEqualTo(1L);
    assertThat(statement.executeLargeUpdate(LARGE_UPDATE, new int[0]))
        .isEqualTo(Integer.MAX_VALUE + 1L);

    assertThat(statement.executeLargeUpdate(UPDATE, new String[0])).isEqualTo(1L);
    assertThat(statement.executeLargeUpdate(LARGE_UPDATE, new String[0]))
        .isEqualTo(Integer.MAX_VALUE + 1L);
  }

  @Test
  public void testExecuteUpdateWithSelectStatement() {
    try {
      Statement statement = createStatement();
      statement.executeUpdate(SELECT);
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(
              JdbcExceptionMatcher.matchCodeAndMessage(
                      Code.INVALID_ARGUMENT,
                      "The statement is not a non-returning DML or DDL statement")
                  .matches(e))
          .isTrue();
    }
  }

  @Test
  public void testExecuteUpdateWithDmlReturningStatement() {
    try {
      Statement statement = createStatement();
      SQLException e =
          assertThrows(SQLException.class, () -> statement.executeUpdate(getDmlReturningSql()));
      assertTrue(
          JdbcExceptionMatcher.matchCodeAndMessage(
                  Code.INVALID_ARGUMENT,
                  "The statement is not a non-returning DML or DDL statement")
              .matches(e));
    } catch (SQLException e) {
      // ignore exception.
    }
  }

  @Test
  public void testExecuteUpdateWithDdlStatement() throws SQLException {
    Statement statement = createStatement();
    assertThat(statement.executeUpdate(DDL)).isEqualTo(0);
  }

  @Test
  public void testExecuteUpdateWithGeneratedKeys() throws SQLException {
    Statement statement = createStatement();
    assertThat(statement.executeUpdate(UPDATE, Statement.NO_GENERATED_KEYS)).isEqualTo(1);
    ResultSet keys = statement.getGeneratedKeys();
    assertThat(keys.next()).isFalse();
    try {
      statement.executeUpdate(UPDATE, Statement.RETURN_GENERATED_KEYS);
      fail("missing expected exception");
    } catch (SQLFeatureNotSupportedException e) {
      // Ignore, this is the expected exception.
    }
  }

  @Test
  public void testMoreResults() throws SQLException {
    Statement statement = createStatement();
    assertThat(statement.execute(SELECT)).isTrue();
    ResultSet rs = statement.getResultSet();
    assertThat(statement.getMoreResults()).isFalse();
    assertThat(statement.getResultSet()).isNull();
    assertThat(rs.isClosed()).isTrue();

    assertThat(statement.execute(SELECT)).isTrue();
    rs = statement.getResultSet();
    assertThat(statement.getMoreResults(Statement.KEEP_CURRENT_RESULT)).isFalse();
    assertThat(statement.getResultSet()).isNull();
    assertThat(rs.isClosed()).isFalse();
  }

  @Test
  public void testNoBatchMixing() {
    try (Statement statement = createStatement()) {
      statement.addBatch("INSERT INTO FOO (ID, NAME) VALUES (1, 'FOO')");
      statement.addBatch("CREATE TABLE FOO (ID INT64, NAME STRING(100)) PRIMARY KEY (ID)");
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(
              JdbcExceptionMatcher.matchCodeAndMessage(
                      Code.INVALID_ARGUMENT,
                      "Mixing DML and DDL statements in a batch is not allowed.")
                  .matches(e))
          .isTrue();
    }
  }

  @Test
  public void testNoBatchQuery() {
    try (Statement statement = createStatement()) {
      statement.addBatch("SELECT * FROM FOO");
    } catch (SQLException e) {
      assertThat(
              JdbcExceptionMatcher.matchCodeAndMessage(
                      Code.INVALID_ARGUMENT,
                      "The statement is not suitable for batching. Only DML and DDL statements are allowed for batching.")
                  .matches(e))
          .isTrue();
    }
  }

  @Test
  public void testDmlBatch() throws SQLException {
    try (Statement statement = createStatement()) {
      // Verify that multiple batches can be executed on the same statement.
      for (int i = 0; i < 2; i++) {
        statement.addBatch("INSERT INTO FOO (ID, NAME) VALUES (1, 'TEST')");
        statement.addBatch("INSERT INTO FOO (ID, NAME) VALUES (2, 'TEST')");
        statement.addBatch("INSERT INTO FOO (ID, NAME) VALUES (3, 'TEST')");
        assertThat(statement.executeBatch()).asList().containsExactly(1, 1, 1);
      }
    }
  }

  @Test
  public void testDmlBatchWithDmlReturning() throws SQLException {
    try (Statement statement = createStatement()) {
      // Verify that multiple batches can be executed on the same statement.
      for (int i = 0; i < 2; i++) {
        statement.addBatch(getDmlReturningSql());
        statement.addBatch(getDmlReturningSql());
        statement.addBatch(getDmlReturningSql());
        assertArrayEquals(statement.executeBatch(), new int[] {1, 1, 1});
      }
    }
  }

  @Test
  public void testLargeDmlBatch() throws SQLException {
    try (Statement statement = createStatement()) {
      // Verify that multiple batches can be executed on the same statement.
      for (int i = 0; i < 2; i++) {
        statement.addBatch("INSERT INTO FOO (ID, NAME) VALUES (1, 'TEST')");
        statement.addBatch("INSERT INTO FOO (ID, NAME) VALUES (2, 'TEST')");
        statement.addBatch("INSERT INTO FOO (ID, NAME) VALUES (3, 'TEST')");
        assertThat(statement.executeLargeBatch()).asList().containsExactly(1L, 1L, 1L);
      }
    }
  }

  @Test
  public void testConvertUpdateCounts() {
    JdbcConnection connection = mock(JdbcConnection.class);
    when(connection.getDialect()).thenReturn(dialect);
    try (JdbcStatement statement = new JdbcStatement(connection)) {
      int[] updateCounts = statement.convertUpdateCounts(new long[] {1L, 2L, 3L});
      assertThat(updateCounts).asList().containsExactly(1, 2, 3);
      updateCounts = statement.convertUpdateCounts(new long[] {0L, 0L, 0L});
      assertThat(updateCounts).asList().containsExactly(0, 0, 0);

      statement.convertUpdateCounts(new long[] {1L, Integer.MAX_VALUE + 1L});
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(JdbcExceptionMatcher.matchCode(Code.OUT_OF_RANGE).matches(e)).isTrue();
    }
  }

  @Test
  public void testConvertUpdateCountsToSuccessNoInfo() throws SQLException {
    JdbcConnection connection = mock(JdbcConnection.class);
    when(connection.getDialect()).thenReturn(dialect);
    try (JdbcStatement statement = new JdbcStatement(connection)) {
      long[] updateCounts = new long[3];
      statement.convertUpdateCountsToSuccessNoInfo(new long[] {1L, 2L, 3L}, updateCounts);
      assertThat(updateCounts)
          .asList()
          .containsExactly(
              (long) Statement.SUCCESS_NO_INFO,
              (long) Statement.SUCCESS_NO_INFO,
              (long) Statement.SUCCESS_NO_INFO);

      statement.convertUpdateCountsToSuccessNoInfo(new long[] {0L, 0L, 0L}, updateCounts);
      assertThat(updateCounts)
          .asList()
          .containsExactly(
              (long) Statement.EXECUTE_FAILED,
              (long) Statement.EXECUTE_FAILED,
              (long) Statement.EXECUTE_FAILED);

      statement.convertUpdateCountsToSuccessNoInfo(new long[] {1L, 0L, 2L}, updateCounts);
      assertThat(updateCounts)
          .asList()
          .containsExactly(
              (long) Statement.SUCCESS_NO_INFO,
              (long) Statement.EXECUTE_FAILED,
              (long) Statement.SUCCESS_NO_INFO);

      statement.convertUpdateCountsToSuccessNoInfo(
          new long[] {1L, Integer.MAX_VALUE + 1L, 2L}, updateCounts);
      assertThat(updateCounts)
          .asList()
          .containsExactly(
              (long) Statement.SUCCESS_NO_INFO,
              (long) Statement.SUCCESS_NO_INFO,
              (long) Statement.SUCCESS_NO_INFO);
    }
  }

  @Test
  public void testAddReturningToStatement() throws SQLException {
    JdbcConnection connection = mock(JdbcConnection.class);
    when(connection.getDialect()).thenReturn(dialect);
    when(connection.getParser()).thenReturn(AbstractStatementParser.getInstance(dialect));
    try (JdbcStatement statement = new JdbcStatement(connection)) {
      assertAddReturningSame(statement, "insert into test (id, value) values (1, 'One')", null);
      assertAddReturningSame(
          statement, "insert into test (id, value) values (1, 'One')", ImmutableList.of());
      assertAddReturningEquals(
          statement,
          dialect == Dialect.POSTGRESQL
              ? "insert into test (id, value) values (1, 'One')\nRETURNING \"id\""
              : "insert into test (id, value) values (1, 'One')\nTHEN RETURN `id`",
          "insert into test (id, value) values (1, 'One')",
          ImmutableList.of("id"));
      // Requesting generated keys for a DML statement that already contains a returning clause is a
      // no-op.
      assertAddReturningSame(
          statement,
          "insert into test (id, value) values (1, 'One') "
              + statement.getReturningClause()
              + " value",
          ImmutableList.of("id"));
      // Requesting generated keys for a query is a no-op.
      assertAddReturningSame(statement, "select id, value from test", ImmutableList.of("id"));

      // Update statements may also request generated keys.
      assertAddReturningSame(statement, "update test set value='Two' where id=1", null);
      assertAddReturningSame(
          statement, "update test set value='Two' where id=1", ImmutableList.of());
      assertAddReturningEquals(
          statement,
          dialect == Dialect.POSTGRESQL
              ? "update test set value='Two' where id=1\nRETURNING \"value\""
              : "update test set value='Two' where id=1\nTHEN RETURN `value`",
          "update test set value='Two' where id=1",
          ImmutableList.of("value"));
      // Requesting generated keys for a DML statement that already contains a returning clause is a
      // no-op.
      assertAddReturningSame(
          statement,
          "update test set value='Two' where id=1 " + statement.getReturningClause() + " value",
          ImmutableList.of("value"));

      // Delete statements may also request generated keys.
      assertAddReturningSame(statement, "delete test where id=1", null);
      assertAddReturningSame(statement, "delete test where id=1", ImmutableList.of());
      assertAddReturningEquals(
          statement,
          dialect == Dialect.POSTGRESQL
              ? "delete test where id=1\nRETURNING \"value\""
              : "delete test where id=1\nTHEN RETURN `value`",
          "delete test where id=1",
          ImmutableList.of("value"));
      // Requesting generated keys for a DML statement that already contains a returning clause is a
      // no-op.
      assertAddReturningSame(
          statement,
          "delete test where id=1 " + statement.getReturningClause() + " value",
          ImmutableList.of("value"));

      // Requesting generated keys for DDL is a no-op.
      assertAddReturningSame(
          statement,
          dialect == Dialect.POSTGRESQL
              ? "create table test (id bigint primary key, value varchar)"
              : "create table test (id int64, value string(max)) primary key (id)",
          ImmutableList.of("id"));
    }
  }

  private void assertAddReturningSame(
      JdbcStatement statement, String sql, @Nullable ImmutableList<String> generatedKeysColumns)
      throws SQLException {
    com.google.cloud.spanner.Statement spannerStatement =
        com.google.cloud.spanner.Statement.of(sql);
    assertSame(
        spannerStatement,
        statement.addReturningToStatement(spannerStatement, generatedKeysColumns));
  }

  private void assertAddReturningEquals(
      JdbcStatement statement,
      String expectedSql,
      String sql,
      @Nullable ImmutableList<String> generatedKeysColumns)
      throws SQLException {
    com.google.cloud.spanner.Statement spannerStatement =
        com.google.cloud.spanner.Statement.of(sql);
    assertEquals(
        com.google.cloud.spanner.Statement.of(expectedSql),
        statement.addReturningToStatement(spannerStatement, generatedKeysColumns));
  }
}
