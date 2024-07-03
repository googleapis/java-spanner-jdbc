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
import static org.junit.Assert.assertTrue;

import com.google.cloud.spanner.Dialect;
import com.google.cloud.spanner.MockServerHelper;
import com.google.cloud.spanner.MockSpannerServiceImpl.StatementResult;
import com.google.cloud.spanner.connection.AbstractMockServerTest;
import com.google.cloud.spanner.connection.SpannerPool;
import com.google.spanner.v1.CreateSessionRequest;
import com.google.spanner.v1.ExecuteSqlRequest;
import com.google.spanner.v1.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MultiplexedSessionsMockServerTest extends AbstractMockServerTest {
  private static final String SELECT_RANDOM_SQL = SELECT_RANDOM_STATEMENT.getSql();

  private static final String INSERT_SQL = INSERT_STATEMENT.getSql();

  @Parameter public Dialect dialect;

  private Dialect currentDialect;

  @Parameters(name = "dialect = {0}")
  public static Object[] data() {
    return Dialect.values();
  }

  @Before
  public void setupDialect() {
    if (this.dialect != currentDialect) {
      mockSpanner.putStatementResult(StatementResult.detectDialectResult(this.dialect));
      this.currentDialect = dialect;
    }
  }

  @After
  public void clearRequests() {
    mockSpanner.clearRequests();
    SpannerPool.closeSpannerPool();
  }

  private String createUrl() {
    return String.format(
        "jdbc:cloudspanner://localhost:%d/projects/%s/instances/%s/databases/%s?usePlainText=true",
        getPort(), "proj", "inst", "db" + (dialect == Dialect.POSTGRESQL ? "pg" : ""));
  }

  private Connection createConnection() throws SQLException {
    return DriverManager.getConnection(createUrl());
  }

  @Test
  public void testUsesMultiplexedSessionForQueryInAutoCommit() throws SQLException {
    try (Connection connection = createConnection()) {
      assertTrue(connection.getAutoCommit());
      try (ResultSet resultSet = connection.createStatement().executeQuery(SELECT_RANDOM_SQL)) {
        //noinspection StatementWithEmptyBody
        while (resultSet.next()) {
          // Just consume the results
        }
      }
    }
    // Verify that one multiplexed session was created and used.
    assertEquals(1, mockSpanner.countRequestsOfType(CreateSessionRequest.class));
    CreateSessionRequest request = mockSpanner.getRequestsOfType(CreateSessionRequest.class).get(0);
    assertTrue(request.getSession().getMultiplexed());
    assertEquals(1, mockSpanner.countRequestsOfType(ExecuteSqlRequest.class));
    String sessionId = mockSpanner.getRequestsOfType(ExecuteSqlRequest.class).get(0).getSession();
    Session session = MockServerHelper.getSession(mockSpanner, sessionId);
    assertNotNull(session);
    assertTrue(session.getMultiplexed());
  }

  @Test
  public void testUsesRegularSessionForDmlInAutoCommit() throws SQLException {
    try (Connection connection = createConnection()) {
      assertTrue(connection.getAutoCommit());
      assertEquals(1, connection.createStatement().executeUpdate(INSERT_SQL));
    }
    // The JDBC connection creates a multiplexed session by default, because it executes a query to
    // check what dialect the database uses. This query is executed using a multiplexed session.
    assertEquals(1, mockSpanner.countRequestsOfType(CreateSessionRequest.class));
    CreateSessionRequest request = mockSpanner.getRequestsOfType(CreateSessionRequest.class).get(0);
    assertTrue(request.getSession().getMultiplexed());
    // Verify that a regular session was used for the insert statement.
    assertEquals(1, mockSpanner.countRequestsOfType(ExecuteSqlRequest.class));
    assertEquals(
        INSERT_SQL, mockSpanner.getRequestsOfType(ExecuteSqlRequest.class).get(0).getSql());
    String sessionId = mockSpanner.getRequestsOfType(ExecuteSqlRequest.class).get(0).getSession();
    Session session = MockServerHelper.getSession(mockSpanner, sessionId);
    assertNotNull(session);
    assertFalse(session.getMultiplexed());
  }

  @Test
  public void testUsesRegularSessionForQueryInTransaction() throws SQLException {
    try (Connection connection = createConnection()) {
      connection.setAutoCommit(false);
      assertFalse(connection.getAutoCommit());

      try (ResultSet resultSet = connection.createStatement().executeQuery(SELECT_RANDOM_SQL)) {
        //noinspection StatementWithEmptyBody
        while (resultSet.next()) {
          // Just consume the results
        }
      }
      connection.commit();
    }
    // The JDBC connection creates a multiplexed session by default, because it executes a query to
    // check what dialect the database uses. This query is executed using a multiplexed session.
    assertEquals(1, mockSpanner.countRequestsOfType(CreateSessionRequest.class));
    CreateSessionRequest request = mockSpanner.getRequestsOfType(CreateSessionRequest.class).get(0);
    assertTrue(request.getSession().getMultiplexed());
    // Verify that a regular session was used for the select statement.
    assertEquals(1, mockSpanner.countRequestsOfType(ExecuteSqlRequest.class));
    assertEquals(
        SELECT_RANDOM_SQL, mockSpanner.getRequestsOfType(ExecuteSqlRequest.class).get(0).getSql());
    String sessionId = mockSpanner.getRequestsOfType(ExecuteSqlRequest.class).get(0).getSession();
    Session session = MockServerHelper.getSession(mockSpanner, sessionId);
    assertNotNull(session);
    assertFalse(session.getMultiplexed());
  }

  @Test
  public void testUsesMultiplexedSessionInCombinationWithSessionPoolOptions() throws SQLException {
    // Create a connection that uses a session pool with MinSessions=0.
    // This should stop any regular sessions from being created.
    // TODO: Modify this test once https://github.com/googleapis/java-spanner/pull/3197 has been
    //       released.
    try (Connection connection = DriverManager.getConnection(createUrl() + ";minSessions=0")) {
      assertTrue(connection.getAutoCommit());
      try (ResultSet resultSet = connection.createStatement().executeQuery(SELECT_RANDOM_SQL)) {
        //noinspection StatementWithEmptyBody
        while (resultSet.next()) {
          // Just consume the results
        }
      }
    }
    // TODO: Remove this line once https://github.com/googleapis/java-spanner/pull/3197 has been
    //       released.
    // Adding 'minSessions=X' or 'maxSessions=x' to the connection URL currently disables the use of
    // multiplexed sessions due to a bug in the Spanner Java client.
    assertEquals(0, mockSpanner.countRequestsOfType(CreateSessionRequest.class));

    // Verify that one multiplexed session was created and used.
    // TODO: Uncomment
    //    assertEquals(1, mockSpanner.countRequestsOfType(CreateSessionRequest.class));
    //    CreateSessionRequest request =
    // mockSpanner.getRequestsOfType(CreateSessionRequest.class).get(0);
    //    assertTrue(request.getSession().getMultiplexed());
    //    // There should be no regular sessions in use.
    //    assertEquals(0, mockSpanner.countRequestsOfType(BatchCreateSessionsRequest.class));
  }
}
