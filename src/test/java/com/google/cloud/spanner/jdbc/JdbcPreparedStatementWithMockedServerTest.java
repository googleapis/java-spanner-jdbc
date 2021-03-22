/*
 * Copyright 2021 Google LLC
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.cloud.spanner.ErrorCode;
import com.google.cloud.spanner.MockSpannerServiceImpl;
import com.google.cloud.spanner.MockSpannerServiceImpl.StatementResult;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.connection.SpannerPool;
import com.google.cloud.spanner.jdbc.JdbcSqlExceptionFactory.JdbcSqlBatchUpdateException;
import io.grpc.Server;
import io.grpc.Status;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JdbcPreparedStatementWithMockedServerTest {
  private static MockSpannerServiceImpl mockSpanner;
  private static Server server;
  private static InetSocketAddress address;

  @Parameter public boolean executeLarge;

  @Parameters(name = "executeLarge = {0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {{false}, {true}});
  }

  @BeforeClass
  public static void startStaticServer() throws IOException {
    mockSpanner = new MockSpannerServiceImpl();
    mockSpanner.setAbortProbability(0.0D);
    address = new InetSocketAddress("localhost", 0);
    server = NettyServerBuilder.forAddress(address).addService(mockSpanner).build().start();
  }

  @AfterClass
  public static void stopServer() throws Exception {
    server.shutdown();
    server.awaitTermination();
  }

  @After
  public void reset() {
    SpannerPool.closeSpannerPool();
    mockSpanner.removeAllExecutionTimes();
    mockSpanner.reset();
  }

  private String createUrl() {
    return String.format(
        "jdbc:cloudspanner://localhost:%d/projects/%s/instances/%s/databases/%s?usePlainText=true",
        server.getPort(), "proj", "inst", "db");
  }

  private Connection createConnection() throws SQLException {
    return DriverManager.getConnection(createUrl());
  }

  @Test
  public void testExecuteBatch() throws SQLException {
    Statement.Builder insertBuilder =
        Statement.newBuilder("INSERT INTO Test (Col1, Col2) VALUES (@p1, @p2)");
    mockSpanner.putStatementResult(
        StatementResult.update(
            insertBuilder.bind("p1").to(1L).bind("p2").to("test 1").build(), 1L));
    mockSpanner.putStatementResult(
        StatementResult.update(
            insertBuilder.bind("p1").to(2L).bind("p2").to("test 2").build(), 1L));
    try (Connection connection = createConnection()) {
      try (PreparedStatement statement =
          connection.prepareStatement("INSERT INTO Test (Col1, Col2) VALUES (?, ?)")) {
        statement.setLong(1, 1L);
        statement.setString(2, "test 1");
        statement.addBatch();
        statement.setLong(1, 2L);
        statement.setString(2, "test 2");
        statement.addBatch();
        if (executeLarge) {
          assertThat(statement.executeLargeBatch()).asList().containsExactly(1L, 1L);
        } else {
          assertThat(statement.executeBatch()).asList().containsExactly(1, 1);
        }
      }
    }
  }

  @Test
  public void testExecuteBatch_withOverflow() throws SQLException {
    Statement.Builder insertBuilder =
        Statement.newBuilder("INSERT INTO Test (Col1, Col2) VALUES (@p1, @p2)");
    mockSpanner.putStatementResult(
        StatementResult.update(
            insertBuilder.bind("p1").to(1L).bind("p2").to("test 1").build(), 1L));
    mockSpanner.putStatementResult(
        StatementResult.update(
            insertBuilder.bind("p1").to(2L).bind("p2").to("test 2").build(),
            Integer.MAX_VALUE + 1L));
    try (Connection connection = createConnection()) {
      try (PreparedStatement statement =
          connection.prepareStatement("INSERT INTO Test (Col1, Col2) VALUES (?, ?)")) {
        statement.setLong(1, 1L);
        statement.setString(2, "test 1");
        statement.addBatch();
        statement.setLong(1, 2L);
        statement.setString(2, "test 2");
        statement.addBatch();
        if (executeLarge) {
          assertThat(statement.executeLargeBatch())
              .asList()
              .containsExactly(1L, Integer.MAX_VALUE + 1L);
        } else {
          try {
            statement.executeBatch();
            fail("missing expected OutOfRange exception");
          } catch (SQLException e) {
            assertTrue(e instanceof JdbcSqlException);
            JdbcSqlException sqlException = (JdbcSqlException) e;
            assertEquals(
                ErrorCode.OUT_OF_RANGE.getGrpcStatusCode().value(), sqlException.getErrorCode());
          }
        }
      }
    }
  }

  @Test
  public void testExecuteBatch_withException() throws SQLException {
    Statement.Builder insertBuilder =
        Statement.newBuilder("INSERT INTO Test (Col1, Col2) VALUES (@p1, @p2)");
    mockSpanner.putStatementResult(
        StatementResult.update(
            insertBuilder.bind("p1").to(1L).bind("p2").to("test 1").build(), 1L));
    mockSpanner.putStatementResult(
        StatementResult.exception(
            insertBuilder.bind("p1").to(2L).bind("p2").to("test 2").build(),
            Status.ALREADY_EXISTS.asRuntimeException()));
    try (Connection connection = createConnection()) {
      try (PreparedStatement statement =
          connection.prepareStatement("INSERT INTO Test (Col1, Col2) VALUES (?, ?)")) {
        statement.setLong(1, 1L);
        statement.setString(2, "test 1");
        statement.addBatch();
        statement.setLong(1, 2L);
        statement.setString(2, "test 2");
        statement.addBatch();
        try {
          if (executeLarge) {
            statement.executeLargeBatch();
          } else {
            statement.executeBatch();
          }
        } catch (JdbcSqlBatchUpdateException e) {
          if (executeLarge) {
            assertThat(e.getLargeUpdateCounts()).asList().containsExactly(1L);
          } else {
            assertThat(e.getUpdateCounts()).asList().containsExactly(1);
          }
        }
      }
    }
  }
}
