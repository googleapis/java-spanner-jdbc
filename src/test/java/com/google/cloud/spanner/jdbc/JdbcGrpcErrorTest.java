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
import static org.junit.Assert.fail;

import com.google.cloud.spanner.MockSpannerServiceImpl;
import com.google.cloud.spanner.MockSpannerServiceImpl.SimulatedExecutionTime;
import com.google.cloud.spanner.MockSpannerServiceImpl.StatementResult;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.admin.database.v1.MockDatabaseAdminImpl;
import com.google.cloud.spanner.admin.instance.v1.MockInstanceAdminImpl;
import com.google.cloud.spanner.connection.SpannerPool;
import com.google.cloud.spanner.jdbc.JdbcSqlExceptionFactory.JdbcSqlExceptionImpl;
import com.google.protobuf.ListValue;
import com.google.protobuf.Value;
import com.google.rpc.Code;
import com.google.spanner.v1.ResultSetMetadata;
import com.google.spanner.v1.StructType;
import com.google.spanner.v1.StructType.Field;
import com.google.spanner.v1.Type;
import com.google.spanner.v1.TypeCode;
import io.grpc.Server;
import io.grpc.Status;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Test that the JDBC driver propagates {@link SQLException}s when a gRPC error occurs. */
@RunWith(JUnit4.class)
public class JdbcGrpcErrorTest {
  private static final Statement SELECT1 = Statement.of("SELECT 1 AS COL1");
  private static final ResultSetMetadata SELECT1_METADATA =
      ResultSetMetadata.newBuilder()
          .setRowType(
              StructType.newBuilder()
                  .addFields(
                      Field.newBuilder()
                          .setName("COL1")
                          .setType(Type.newBuilder().setCode(TypeCode.INT64).build())
                          .build())
                  .build())
          .build();
  private static final com.google.spanner.v1.ResultSet SELECT1_RESULTSET =
      com.google.spanner.v1.ResultSet.newBuilder()
          .addRows(
              ListValue.newBuilder()
                  .addValues(Value.newBuilder().setStringValue("1").build())
                  .build())
          .setMetadata(SELECT1_METADATA)
          .build();
  private static final Statement UPDATE_STATEMENT =
      Statement.of("UPDATE FOO SET BAR=1 WHERE BAZ=2");
  private static final int UPDATE_COUNT = 1;
  private static final Statement INVALID_UPDATE_STATEMENT =
      Statement.of("UPDATE NON_EXISING_TABLE SET FOO=1 WHERE BAR=2");

  private static MockSpannerServiceImpl mockSpanner;
  private static MockInstanceAdminImpl mockInstanceAdmin;
  private static MockDatabaseAdminImpl mockDatabaseAdmin;
  private static Server server;
  private static InetSocketAddress address;

  // FAILED_PRECONDITION is chosen as the test error code as it should never be retryable.
  private final Exception serverException =
      Status.FAILED_PRECONDITION.withDescription("test exception").asRuntimeException();
  private final SpannerJdbcExceptionMatcher<JdbcSqlExceptionImpl> testExceptionMatcher =
      SpannerJdbcExceptionMatcher.matchCodeAndMessage(
          JdbcSqlExceptionImpl.class, Code.FAILED_PRECONDITION, "test exception");

  @BeforeClass
  public static void startStaticServer() throws IOException {
    mockSpanner = new MockSpannerServiceImpl();
    mockSpanner.setAbortProbability(0.0D); // We don't want any unpredictable aborted transactions.
    mockSpanner.putStatementResult(StatementResult.query(SELECT1, SELECT1_RESULTSET));
    mockSpanner.putStatementResult(StatementResult.update(UPDATE_STATEMENT, UPDATE_COUNT));
    mockSpanner.putStatementResult(
        StatementResult.exception(
            INVALID_UPDATE_STATEMENT,
            Status.NOT_FOUND.withDescription("Unknown table name").asRuntimeException()));
    mockInstanceAdmin = new MockInstanceAdminImpl();
    mockDatabaseAdmin = new MockDatabaseAdminImpl();
    address = new InetSocketAddress("localhost", 0);
    server =
        NettyServerBuilder.forAddress(address)
            .addService(mockSpanner)
            .addService(mockInstanceAdmin)
            .addService(mockDatabaseAdmin)
            .build()
            .start();
  }

  @AfterClass
  public static void stopServer() throws Exception {
    SpannerPool.closeSpannerPool();
    server.shutdown();
    server.awaitTermination();
  }

  @After
  public void reset() {
    // Close Spanner pool to prevent reusage of the same Spanner instance (and thereby the same
    // session pool).
    SpannerPool.closeSpannerPool();
    mockSpanner.removeAllExecutionTimes();
    mockSpanner.reset();
  }

  private String createUrl() {
    return String.format(
        "jdbc:cloudspanner://localhost:%d/projects/%s/instances/%s/databases/%s?usePlainText=true;minSessions=0",
        server.getPort(), "proj", "inst", "db");
  }

  private Connection createConnection() throws SQLException {
    return DriverManager.getConnection(createUrl());
  }

  @Test
  public void autocommitBeginTransaction() {
    mockSpanner.setBeginTransactionExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      // This triggers a retry with an explicit BeginTransaction RPC.
      mockSpanner.abortNextStatement();
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void autocommitBeginPDMLTransaction() {
    mockSpanner.setBeginTransactionExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.createStatement().execute("SET AUTOCOMMIT_DML_MODE='PARTITIONED_NON_ATOMIC'");
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void transactionalBeginTransaction() {
    mockSpanner.setBeginTransactionExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      // This triggers a retry with an explicit BeginTransaction RPC.
      mockSpanner.abortNextStatement();
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void readOnlyBeginTransaction() {
    mockSpanner.setBeginTransactionExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      connection.setReadOnly(true);
      connection.createStatement().executeQuery(SELECT1.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void autocommitExecuteSql() {
    mockSpanner.setExecuteSqlExecutionTime(SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void autocommitPDMLExecuteSql() {
    mockSpanner.setExecuteStreamingSqlExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.createStatement().execute("SET AUTOCOMMIT_DML_MODE='PARTITIONED_NON_ATOMIC'");
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void transactionalExecuteSql() {
    mockSpanner.setExecuteSqlExecutionTime(SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void autocommitExecuteBatchDml() throws SQLException {
    mockSpanner.setExecuteBatchDmlExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      try (java.sql.Statement statement = connection.createStatement()) {
        statement.addBatch(UPDATE_STATEMENT.getSql());
        statement.addBatch(UPDATE_STATEMENT.getSql());
        statement.executeBatch();
        fail("missing expected exception");
      } catch (SQLException e) {
        assertThat(testExceptionMatcher.matches(e)).isTrue();
      }
    }
  }

  @Test
  public void transactionalExecuteBatchDml() throws SQLException {
    mockSpanner.setExecuteBatchDmlExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      try (java.sql.Statement statement = connection.createStatement()) {
        statement.addBatch(UPDATE_STATEMENT.getSql());
        statement.addBatch(UPDATE_STATEMENT.getSql());
        statement.executeBatch();
        fail("missing expected exception");
      } catch (SQLException e) {
        assertThat(testExceptionMatcher.matches(e)).isTrue();
      }
    }
  }

  @Test
  public void autocommitCommit() {
    mockSpanner.setCommitExecutionTime(SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void transactionalCommit() {
    mockSpanner.setCommitExecutionTime(SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      connection.commit();
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void autocommitRollback() {
    // The JDBC driver should throw the exception of the SQL statement and ignore any errors from
    // the rollback() method.
    mockSpanner.setRollbackExecutionTime(SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.createStatement().executeUpdate(INVALID_UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(
              SpannerJdbcExceptionMatcher.matchCodeAndMessage(
                      JdbcSqlExceptionImpl.class, Code.NOT_FOUND, "Unknown table name")
                  .matches(e))
          .isTrue();
    }
  }

  @Test
  public void transactionalRollback() throws SQLException {
    // Rollback exceptions are ignored by the client library and not propagated to the JDBC driver.
    // This method will therefore not throw any errors.
    mockSpanner.setRollbackExecutionTime(SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      connection.rollback();
    }
  }

  @Test
  public void autocommitExecuteStreamingSql() {
    mockSpanner.setExecuteStreamingSqlExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      try (java.sql.ResultSet rs = connection.createStatement().executeQuery(SELECT1.getSql())) {
        rs.next();
        fail("missing expected exception");
      }
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void transactionalExecuteStreamingSql() {
    mockSpanner.setExecuteStreamingSqlExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      try (java.sql.ResultSet rs = connection.createStatement().executeQuery(SELECT1.getSql())) {
        rs.next();
        fail("missing expected exception");
      }
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void readOnlyExecuteStreamingSql() {
    mockSpanner.setExecuteStreamingSqlExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      connection.setReadOnly(true);
      try (java.sql.ResultSet rs = connection.createStatement().executeQuery(SELECT1.getSql())) {
        rs.next();
        fail("missing expected exception");
      }
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void autocommitCreateSession() {
    mockSpanner.setBatchCreateSessionsExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void transactionalCreateSession() {
    mockSpanner.setBatchCreateSessionsExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      connection.createStatement().executeUpdate(UPDATE_STATEMENT.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }

  @Test
  public void readOnlyCreateSession() {
    mockSpanner.setBatchCreateSessionsExecutionTime(
        SimulatedExecutionTime.ofException(serverException));
    try (java.sql.Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      connection.setReadOnly(true);
      connection.createStatement().executeQuery(SELECT1.getSql());
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(testExceptionMatcher.matches(e)).isTrue();
    }
  }
}
