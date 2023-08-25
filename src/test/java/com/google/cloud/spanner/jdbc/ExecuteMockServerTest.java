/*
 * Copyright 2023 Google LLC
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.cloud.spanner.MockSpannerServiceImpl.StatementResult;
import com.google.cloud.spanner.connection.AbstractMockServerTest;
import com.google.longrunning.Operation;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.google.protobuf.ListValue;
import com.google.protobuf.Value;
import com.google.rpc.Code;
import com.google.spanner.admin.database.v1.UpdateDatabaseDdlMetadata;
import com.google.spanner.v1.ResultSetMetadata;
import com.google.spanner.v1.ResultSetStats;
import com.google.spanner.v1.StructType;
import com.google.spanner.v1.StructType.Field;
import com.google.spanner.v1.Type;
import com.google.spanner.v1.TypeCode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for verifying that the methods execute, executeQuery, and executeUpdate work as
 * intended.
 */
@RunWith(JUnit4.class)
public class ExecuteMockServerTest extends AbstractMockServerTest {
  private static final String QUERY = "select * from my_table";
  private static final String DML = "insert into my_table (id, value) values (1, 'One')";
  private static final String LARGE_DML = "update my_table set value='new value' where true";
  private static final String DML_RETURNING =
      "insert into my_table (id, value) values (1, 'One') THEN RETURN *";
  private static final String DDL = "create table my_table";
  private static final long LARGE_UPDATE_COUNT = 2L * Integer.MAX_VALUE;

  @Before
  public void setupResults() {
    super.setupResults();
    com.google.spanner.v1.ResultSet resultSet =
        com.google.spanner.v1.ResultSet.newBuilder()
            .setMetadata(
                ResultSetMetadata.newBuilder()
                    .setRowType(
                        StructType.newBuilder()
                            .addFields(
                                Field.newBuilder()
                                    .setType(Type.newBuilder().setCode(TypeCode.INT64).build())
                                    .setName("id")
                                    .build())
                            .addFields(
                                Field.newBuilder()
                                    .setType(Type.newBuilder().setCode(TypeCode.STRING).build())
                                    .setName("value")
                                    .build())
                            .build())
                    .build())
            .addRows(
                ListValue.newBuilder()
                    .addValues(Value.newBuilder().setStringValue("1").build())
                    .addValues(Value.newBuilder().setStringValue("One").build())
                    .build())
            .build();
    mockSpanner.putStatementResult(
        StatementResult.query(com.google.cloud.spanner.Statement.of(QUERY), resultSet));
    mockSpanner.putStatementResult(
        StatementResult.update(com.google.cloud.spanner.Statement.of(DML), 1L));
    mockSpanner.putStatementResult(
        StatementResult.update(
            com.google.cloud.spanner.Statement.of(LARGE_DML), LARGE_UPDATE_COUNT));
    mockSpanner.putStatementResult(
        StatementResult.query(
            com.google.cloud.spanner.Statement.of(DML_RETURNING),
            resultSet
                .toBuilder()
                .setStats(ResultSetStats.newBuilder().setRowCountExact(1L).build())
                .build()));
    mockDatabaseAdmin.addResponse(
        Operation.newBuilder()
            .setDone(true)
            .setResponse(Any.pack(Empty.getDefaultInstance()))
            .setMetadata(Any.pack(UpdateDatabaseDdlMetadata.getDefaultInstance()))
            .build());
  }

  private String createUrl() {
    return String.format(
        "jdbc:cloudspanner://localhost:%d/projects/%s/instances/%s/databases/%s?usePlainText=true",
        getPort(), "proj", "inst", "db");
  }

  private Connection createConnection() throws SQLException {
    return DriverManager.getConnection(createUrl());
  }

  @Test
  public void testStatementExecuteQuery() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(QUERY)) {
        verifyResultSet(resultSet);
      }
      try (ResultSet resultSet = statement.executeQuery(DML_RETURNING)) {
        verifyResultSet(resultSet);
      }
      verifyException(() -> statement.executeQuery(DML));
      verifyException(() -> statement.executeQuery(LARGE_DML));
      verifyException(() -> statement.executeQuery(DDL));
    }
  }

  @Test
  public void testStatementExecuteUpdate() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      assertEquals(1, statement.executeUpdate(DML));
      assertEquals(0, statement.executeUpdate(DDL));
      verifyOverflow(() -> statement.executeUpdate(LARGE_DML));
      verifyException(() -> statement.executeUpdate(QUERY));
      verifyException(() -> statement.executeUpdate(DML_RETURNING));
    }
  }

  @Test
  public void testStatementExecuteUpdateReturnGeneratedKeys() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      // TODO: Add tests for RETURN_GENERATED_KEYS when that is supported.
      assertEquals(1, statement.executeUpdate(DML, Statement.NO_GENERATED_KEYS));
      assertEquals(0, statement.executeUpdate(DDL, Statement.NO_GENERATED_KEYS));
      verifyOverflow(() -> statement.executeUpdate(LARGE_DML, Statement.NO_GENERATED_KEYS));
      verifyException(() -> statement.executeUpdate(QUERY, Statement.NO_GENERATED_KEYS));
      verifyException(() -> statement.executeUpdate(DML_RETURNING, Statement.NO_GENERATED_KEYS));
    }
  }

  @Test
  public void testStatementExecuteUpdateReturnColumnNames() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      assertEquals(1, statement.executeUpdate(DML, new String[] {"id"}));
      assertEquals(0, statement.executeUpdate(DDL, new String[] {"id"}));
      verifyOverflow(() -> statement.executeUpdate(LARGE_DML, new String[] {"id"}));
      verifyException(() -> statement.executeUpdate(QUERY, new String[] {"id"}));
      verifyException(() -> statement.executeUpdate(DML_RETURNING, new String[] {"id"}));
    }
  }

  @Test
  public void testStatementExecuteUpdateReturnColumnIndexes() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      assertEquals(1, statement.executeUpdate(DML, new int[] {1}));
      assertEquals(0, statement.executeUpdate(DDL, new int[] {1}));
      verifyOverflow(() -> statement.executeUpdate(LARGE_DML, new int[] {1}));
      verifyException(() -> statement.executeUpdate(QUERY, new int[] {1}));
      verifyException(() -> statement.executeUpdate(DML_RETURNING, new int[] {1}));
    }
  }

  @Test
  public void testStatementLargeExecuteUpdate() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      assertEquals(1L, statement.executeLargeUpdate(DML));
      assertEquals(0L, statement.executeLargeUpdate(DDL));
      assertEquals(LARGE_UPDATE_COUNT, statement.executeLargeUpdate(LARGE_DML));
      verifyException(() -> statement.executeLargeUpdate(QUERY));
      verifyException(() -> statement.executeLargeUpdate(DML_RETURNING));
    }
  }

  @Test
  public void testStatementExecuteLargeUpdateReturnGeneratedKeys() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      // TODO: Add tests for RETURN_GENERATED_KEYS when that is supported.
      assertEquals(1, statement.executeLargeUpdate(DML, Statement.NO_GENERATED_KEYS));
      assertEquals(0, statement.executeLargeUpdate(DDL, Statement.NO_GENERATED_KEYS));
      assertEquals(
          LARGE_UPDATE_COUNT, statement.executeLargeUpdate(LARGE_DML, Statement.NO_GENERATED_KEYS));
      verifyException(() -> statement.executeLargeUpdate(QUERY, Statement.NO_GENERATED_KEYS));
      verifyException(
          () -> statement.executeLargeUpdate(DML_RETURNING, Statement.NO_GENERATED_KEYS));
    }
  }

  @Test
  public void testStatementExecuteLargeUpdateReturnColumnNames() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      assertEquals(1, statement.executeLargeUpdate(DML, new String[] {"id"}));
      assertEquals(0, statement.executeLargeUpdate(DDL, new String[] {"id"}));
      assertEquals(
          LARGE_UPDATE_COUNT, statement.executeLargeUpdate(LARGE_DML, new String[] {"id"}));
      verifyException(() -> statement.executeLargeUpdate(QUERY, new String[] {"id"}));
      verifyException(() -> statement.executeLargeUpdate(DML_RETURNING, new String[] {"id"}));
    }
  }

  @Test
  public void testStatementExecuteLargeUpdateReturnColumnIndexes() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      assertEquals(1, statement.executeLargeUpdate(DML, new int[] {1}));
      assertEquals(0, statement.executeLargeUpdate(DDL, new int[] {1}));
      assertEquals(LARGE_UPDATE_COUNT, statement.executeLargeUpdate(LARGE_DML, new int[] {1}));
      verifyException(() -> statement.executeLargeUpdate(QUERY, new int[] {1}));
      verifyException(() -> statement.executeLargeUpdate(DML_RETURNING, new int[] {1}));
    }
  }

  @Test
  public void testStatementExecute() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      verifyUpdateCount(statement, () -> statement.execute(DML), 1L);
      verifyUpdateCount(statement, () -> statement.execute(LARGE_DML), LARGE_UPDATE_COUNT);
      verifyUpdateCount(statement, () -> statement.execute(DDL), Statement.SUCCESS_NO_INFO);
      verifyResultSet(statement, () -> statement.execute(QUERY));
      verifyResultSet(statement, () -> statement.execute(DML_RETURNING));
    }
  }

  @Test
  public void testStatementExecuteReturnGeneratedKeys() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      // TODO: Add tests for RETURN_GENERATED_KEYS when that is supported.
      verifyUpdateCount(statement, () -> statement.execute(DML, Statement.NO_GENERATED_KEYS), 1L);
      verifyUpdateCount(
          statement,
          () -> statement.execute(LARGE_DML, Statement.NO_GENERATED_KEYS),
          LARGE_UPDATE_COUNT);
      verifyUpdateCount(
          statement,
          () -> statement.execute(DDL, Statement.NO_GENERATED_KEYS),
          Statement.SUCCESS_NO_INFO);
      verifyResultSet(statement, () -> statement.execute(QUERY, Statement.NO_GENERATED_KEYS));
      verifyResultSet(
          statement, () -> statement.execute(DML_RETURNING, Statement.NO_GENERATED_KEYS));
    }
  }

  @Test
  public void testStatementExecuteReturnColumnNames() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      verifyUpdateCount(statement, () -> statement.execute(DML, new String[] {"id"}), 1L);
      verifyUpdateCount(
          statement, () -> statement.execute(LARGE_DML, new String[] {"id"}), LARGE_UPDATE_COUNT);
      verifyUpdateCount(
          statement, () -> statement.execute(DDL, new String[] {"id"}), Statement.SUCCESS_NO_INFO);
      verifyResultSet(statement, () -> statement.execute(QUERY, new String[] {"id"}));
      verifyResultSet(statement, () -> statement.execute(DML_RETURNING, new String[] {"id"}));
    }
  }

  @Test
  public void testStatementExecuteReturnColumnIndexes() throws SQLException {
    try (Connection connection = createConnection();
        Statement statement = connection.createStatement()) {
      verifyUpdateCount(statement, () -> statement.execute(DML, new int[] {1}), 1L);
      verifyUpdateCount(
          statement, () -> statement.execute(LARGE_DML, new int[] {1}), LARGE_UPDATE_COUNT);
      verifyUpdateCount(
          statement, () -> statement.execute(DDL, new int[] {1}), Statement.SUCCESS_NO_INFO);
      verifyResultSet(statement, () -> statement.execute(QUERY, new int[] {1}));
      verifyResultSet(statement, () -> statement.execute(DML_RETURNING, new int[] {1}));
    }
  }

  @Test
  public void testPreparedStatementExecuteQuery() throws SQLException {
    try (Connection connection = createConnection()) {
      try (ResultSet resultSet = connection.prepareStatement(QUERY).executeQuery()) {
        verifyResultSet(resultSet);
      }
      try (ResultSet resultSet = connection.prepareStatement(DML_RETURNING).executeQuery()) {
        verifyResultSet(resultSet);
      }
      verifyException(() -> connection.prepareStatement(DML).executeQuery());
      verifyException(() -> connection.prepareStatement(LARGE_DML).executeQuery());
      verifyException(() -> connection.prepareStatement(DDL).executeQuery());
    }
  }

  @Test
  public void testPreparedStatementExecuteUpdate() throws SQLException {
    try (Connection connection = createConnection()) {
      assertEquals(1, connection.prepareStatement(DML).executeUpdate());
      // TODO: Enable the next statement once PreparedStatement supports executing DDL through the
      //       executeUpdate() method.
      // assertEquals(0, connection.prepareStatement(DDL).executeUpdate());
      verifyOverflow(() -> connection.prepareStatement(LARGE_DML).executeUpdate());
      verifyException(() -> connection.prepareStatement(QUERY).executeUpdate());
      verifyException(
          () -> connection.prepareStatement(DML_RETURNING).executeUpdate(),
          Code.FAILED_PRECONDITION);
    }
  }

  @Test
  public void testPreparedStatementExecuteUpdateReturnGeneratedKeys() throws SQLException {
    try (Connection connection = createConnection()) {
      // TODO: Add tests for RETURN_GENERATED_KEYS when that is supported.
      assertEquals(
          1, connection.prepareStatement(DML, Statement.NO_GENERATED_KEYS).executeUpdate());
      // TODO: Enable the next statement once PreparedStatement supports executing DDL through the
      //       executeUpdate() method.
      // assertEquals(0, connection.prepareStatement(DDL,
      // Statement.NO_GENERATED_KEYS).executeUpdate());
      verifyOverflow(
          () ->
              connection.prepareStatement(LARGE_DML, Statement.NO_GENERATED_KEYS).executeUpdate());
      verifyException(
          () -> connection.prepareStatement(QUERY, Statement.NO_GENERATED_KEYS).executeUpdate());
      verifyException(
          () ->
              connection
                  .prepareStatement(DML_RETURNING, Statement.NO_GENERATED_KEYS)
                  .executeUpdate(),
          Code.FAILED_PRECONDITION);
    }
  }

  @Test
  public void testPreparedStatementExecuteUpdateReturnColumnNames() throws SQLException {
    try (Connection connection = createConnection()) {
      assertEquals(1, connection.prepareStatement(DML, new String[] {"id"}).executeUpdate());
      // TODO: Enable the next statement once PreparedStatement supports executing DDL through the
      //       executeUpdate() method.
      // assertEquals(0, connection.prepareStatement(DDL, new String[] {"id"}).executeUpdate());
      verifyOverflow(
          () -> connection.prepareStatement(LARGE_DML, new String[] {"id"}).executeUpdate());
      verifyException(
          () -> connection.prepareStatement(QUERY, new String[] {"id"}).executeUpdate());
      verifyException(
          () -> connection.prepareStatement(DML_RETURNING, new String[] {"id"}).executeUpdate(),
          Code.FAILED_PRECONDITION);
    }
  }

  @Test
  public void testPreparedStatementExecuteUpdateReturnColumnIndexes() throws SQLException {
    try (Connection connection = createConnection()) {
      assertEquals(1, connection.prepareStatement(DML, new int[] {1}).executeUpdate());
      // TODO: Enable the next statement once PreparedStatement supports executing DDL through the
      //       executeUpdate() method.
      // assertEquals(0, connection.prepareStatement(DDL, new int[] {1}).executeUpdate());
      verifyOverflow(() -> connection.prepareStatement(LARGE_DML, new int[] {1}).executeUpdate());
      verifyException(() -> connection.prepareStatement(QUERY, new int[] {1}).executeUpdate());
      verifyException(
          () -> connection.prepareStatement(DML_RETURNING, new int[] {1}).executeUpdate(),
          Code.FAILED_PRECONDITION);
    }
  }

  @Test
  public void testPreparedStatementLargeExecuteUpdate() throws SQLException {
    try (Connection connection = createConnection()) {
      assertEquals(1L, connection.prepareStatement(DML).executeLargeUpdate());
      // TODO: Enable the next statement once PreparedStatement supports executing DDL through the
      //       executeUpdate() method.
      // assertEquals(0L, connection.prepareStatement(DDL).executeLargeUpdate());
      assertEquals(LARGE_UPDATE_COUNT, connection.prepareStatement(LARGE_DML).executeLargeUpdate());
      verifyException(() -> connection.prepareStatement(QUERY).executeLargeUpdate());
      verifyException(
          () -> connection.prepareStatement(DML_RETURNING).executeLargeUpdate(),
          Code.FAILED_PRECONDITION);
    }
  }

  @Test
  public void testPreparedStatementExecuteLargeUpdateReturnGeneratedKeys() throws SQLException {
    try (Connection connection = createConnection()) {
      // TODO: Add tests for RETURN_GENERATED_KEYS when that is supported.
      assertEquals(
          1, connection.prepareStatement(DML, Statement.NO_GENERATED_KEYS).executeLargeUpdate());
      // TODO: Enable the next statement once PreparedStatement supports executing DDL through the
      //       executeUpdate() method.
      // assertEquals(0, connection.prepareStatement(DDL,
      // Statement.NO_GENERATED_KEYS).executeLargeUpdate());
      assertEquals(
          LARGE_UPDATE_COUNT,
          connection.prepareStatement(LARGE_DML, Statement.NO_GENERATED_KEYS).executeLargeUpdate());
      verifyException(
          () ->
              connection.prepareStatement(QUERY, Statement.NO_GENERATED_KEYS).executeLargeUpdate());
      verifyException(
          () ->
              connection
                  .prepareStatement(DML_RETURNING, Statement.NO_GENERATED_KEYS)
                  .executeLargeUpdate(),
          Code.FAILED_PRECONDITION);
    }
  }

  @Test
  public void testPreparedStatementExecuteLargeUpdateReturnColumnNames() throws SQLException {
    try (Connection connection = createConnection()) {
      assertEquals(1, connection.prepareStatement(DML, new String[] {"id"}).executeLargeUpdate());
      // TODO: Enable the next statement once PreparedStatement supports executing DDL through the
      //       executeUpdate() method.
      // assertEquals(0, connection.prepareStatement(DDL, new String[]
      // {"id"}).executeLargeUpdate());
      assertEquals(
          LARGE_UPDATE_COUNT,
          connection.prepareStatement(LARGE_DML, new String[] {"id"}).executeLargeUpdate());
      verifyException(
          () -> connection.prepareStatement(QUERY, new String[] {"id"}).executeLargeUpdate());
      verifyException(
          () ->
              connection.prepareStatement(DML_RETURNING, new String[] {"id"}).executeLargeUpdate(),
          Code.FAILED_PRECONDITION);
    }
  }

  @Test
  public void testPreparedStatementExecuteLargeUpdateReturnColumnIndexes() throws SQLException {
    try (Connection connection = createConnection()) {
      assertEquals(1, connection.prepareStatement(DML, new int[] {1}).executeLargeUpdate());
      // TODO: Enable the next statement once PreparedStatement supports executing DDL through the
      //       executeUpdate() method.
      // assertEquals(0, connection.prepareStatement(DDL, new int[] {1}).executeLargeUpdate());
      assertEquals(
          LARGE_UPDATE_COUNT,
          connection.prepareStatement(LARGE_DML, new int[] {1}).executeLargeUpdate());
      verifyException(() -> connection.prepareStatement(QUERY, new int[] {1}).executeLargeUpdate());
      verifyException(
          () -> connection.prepareStatement(DML_RETURNING, new int[] {1}).executeLargeUpdate(),
          Code.FAILED_PRECONDITION);
    }
  }

  @Test
  public void testPreparedStatementExecute() throws SQLException {
    try (Connection connection = createConnection()) {
      verifyPreparedUpdateCount(connection.prepareStatement(DML), PreparedStatement::execute, 1L);
      verifyPreparedUpdateCount(
          connection.prepareStatement(LARGE_DML), PreparedStatement::execute, LARGE_UPDATE_COUNT);
      verifyPreparedUpdateCount(
          connection.prepareStatement(DDL), PreparedStatement::execute, Statement.SUCCESS_NO_INFO);
      verifyPreparedResultSet(connection.prepareStatement(QUERY), PreparedStatement::execute);
      verifyPreparedResultSet(
          connection.prepareStatement(DML_RETURNING), PreparedStatement::execute);
    }
  }

  @Test
  public void testPreparedStatementExecuteReturnGeneratedKeys() throws SQLException {
    try (Connection connection = createConnection()) {
      // TODO: Add tests for RETURN_GENERATED_KEYS when that is supported.
      verifyPreparedUpdateCount(
          connection.prepareStatement(DML, Statement.NO_GENERATED_KEYS),
          PreparedStatement::execute,
          1L);
      verifyPreparedUpdateCount(
          connection.prepareStatement(LARGE_DML, Statement.NO_GENERATED_KEYS),
          PreparedStatement::execute,
          LARGE_UPDATE_COUNT);
      verifyPreparedUpdateCount(
          connection.prepareStatement(DDL, Statement.NO_GENERATED_KEYS),
          PreparedStatement::execute,
          Statement.SUCCESS_NO_INFO);
      verifyPreparedResultSet(
          connection.prepareStatement(QUERY, Statement.NO_GENERATED_KEYS),
          PreparedStatement::execute);
      verifyPreparedResultSet(
          connection.prepareStatement(DML_RETURNING, Statement.NO_GENERATED_KEYS),
          PreparedStatement::execute);
    }
  }

  @Test
  public void testPreparedStatementExecuteReturnColumnNames() throws SQLException {
    try (Connection connection = createConnection()) {
      verifyPreparedUpdateCount(
          connection.prepareStatement(DML, new String[] {"id"}), PreparedStatement::execute, 1L);
      verifyPreparedUpdateCount(
          connection.prepareStatement(LARGE_DML, new String[] {"id"}),
          PreparedStatement::execute,
          LARGE_UPDATE_COUNT);
      verifyPreparedUpdateCount(
          connection.prepareStatement(DDL, new String[] {"id"}),
          PreparedStatement::execute,
          Statement.SUCCESS_NO_INFO);
      verifyPreparedResultSet(
          connection.prepareStatement(QUERY, new String[] {"id"}), PreparedStatement::execute);
      verifyPreparedResultSet(
          connection.prepareStatement(DML_RETURNING, new String[] {"id"}),
          PreparedStatement::execute);
    }
  }

  @Test
  public void testPreparedStatementExecuteReturnColumnIndexes() throws SQLException {
    try (Connection connection = createConnection()) {
      verifyPreparedUpdateCount(
          connection.prepareStatement(DML, new int[] {1}), PreparedStatement::execute, 1L);
      verifyPreparedUpdateCount(
          connection.prepareStatement(LARGE_DML, new int[] {1}),
          PreparedStatement::execute,
          LARGE_UPDATE_COUNT);
      verifyPreparedUpdateCount(
          connection.prepareStatement(DDL, new int[] {1}),
          PreparedStatement::execute,
          Statement.SUCCESS_NO_INFO);
      verifyPreparedResultSet(
          connection.prepareStatement(QUERY, new int[] {1}), PreparedStatement::execute);
      verifyPreparedResultSet(
          connection.prepareStatement(DML_RETURNING, new int[] {1}), PreparedStatement::execute);
    }
  }

  private void verifyResultSet(ResultSet resultSet) throws SQLException {
    assertNotNull(resultSet.getMetaData());
    assertEquals(2, resultSet.getMetaData().getColumnCount());
    assertTrue(resultSet.next());
    assertFalse(resultSet.next());
  }

  private void verifyException(ThrowingRunnable runnable) {
    verifyException(runnable, Code.INVALID_ARGUMENT);
  }

  private void verifyOverflow(ThrowingRunnable runnable) {
    verifyException(runnable, Code.OUT_OF_RANGE);
  }

  private void verifyException(ThrowingRunnable runnable, Code code) {
    SQLException exception = assertThrows(SQLException.class, runnable);
    assertTrue(exception instanceof JdbcSqlException);
    JdbcSqlException sqlException = (JdbcSqlException) exception;
    assertEquals(code, sqlException.getCode());
  }

  interface ThrowingFunction<T> {
    T apply() throws SQLException;
  }

  interface ThrowingPreparedFunction<T> {
    T apply(PreparedStatement statement) throws SQLException;
  }

  private void verifyPreparedUpdateCount(
      PreparedStatement statement, ThrowingPreparedFunction<Boolean> function, long updateCount)
      throws SQLException {
    verifyUpdateCount(statement, () -> function.apply(statement), updateCount);
  }

  private void verifyUpdateCount(
      Statement statement, ThrowingFunction<Boolean> function, long updateCount)
      throws SQLException {
    assertFalse(function.apply());
    if (updateCount > Integer.MAX_VALUE) {
      verifyOverflow(statement::getUpdateCount);
    } else {
      assertEquals((int) updateCount, statement.getUpdateCount());
    }
    assertEquals(updateCount, statement.getLargeUpdateCount());
    assertNull(statement.getResultSet());
    assertFalse(statement.getMoreResults());
    assertEquals(-1, statement.getUpdateCount());
    assertEquals(-1L, statement.getLargeUpdateCount());
  }

  private void verifyPreparedResultSet(
      PreparedStatement statement, ThrowingPreparedFunction<Boolean> function) throws SQLException {
    verifyResultSet(statement, () -> function.apply(statement));
  }

  private void verifyResultSet(Statement statement, ThrowingFunction<Boolean> function)
      throws SQLException {
    assertTrue(function.apply());
    assertEquals(-1, statement.getUpdateCount());
    assertEquals(-1L, statement.getLargeUpdateCount());
    assertNotNull(statement.getResultSet());
    try (ResultSet resultSet = statement.getResultSet()) {
      assertTrue(resultSet.next());
      assertFalse(resultSet.next());
    }

    assertFalse(statement.getMoreResults());
    assertEquals(-1, statement.getUpdateCount());
    assertEquals(-1L, statement.getLargeUpdateCount());
  }
}
