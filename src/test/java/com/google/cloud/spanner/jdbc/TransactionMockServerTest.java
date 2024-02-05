/*
 * Copyright 2024 Google LLC
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

import com.google.cloud.spanner.connection.AbstractMockServerTest;
import com.google.cloud.spanner.connection.SpannerPool;
import com.google.spanner.v1.CommitRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TransactionMockServerTest extends AbstractMockServerTest {

  @AfterClass
  public static void closeSpannerPool() {
    SpannerPool.closeSpannerPool();
  }

  @After
  public void clearRequests() {
    mockSpanner.clearRequests();
  }

  private String createUrl() {
    return String.format(
        "jdbc:cloudspanner://localhost:%d/projects/%s/instances/%s/databases/%s?usePlainText=true;autoCommit=false",
        getPort(), "proj", "inst", "db");
  }

  private Connection createConnection() throws SQLException {
    return DriverManager.getConnection(createUrl());
  }

  @Test
  public void testCommittingEmptyTransactionIsNoOp() throws SQLException {
    try (Connection connection = createConnection()) {
      connection.commit();
    }

    assertEquals(0, mockSpanner.countRequestsOfType(CommitRequest.class));
  }

  @Test
  public void testCommittingEmptyExplicitTransactionIsNoOp() throws SQLException {
    try (Connection connection = createConnection()) {
      connection.setAutoCommit(true);
      try (Statement statement = connection.createStatement()) {
        statement.execute("begin transaction");
        statement.execute("commit");
      }
    }

    assertEquals(0, mockSpanner.countRequestsOfType(CommitRequest.class));
  }

  @Test
  public void testRollingBackEmptyTransactionIsNoOp() throws SQLException {
    try (Connection connection = createConnection()) {
      connection.rollback();
    }

    assertEquals(0, mockSpanner.countRequestsOfType(CommitRequest.class));
  }

  @Test
  public void testRollingBackEmptyExplicitTransactionIsNoOp() throws SQLException {
    try (Connection connection = createConnection()) {
      connection.setAutoCommit(true);
      try (Statement statement = connection.createStatement()) {
        statement.execute("begin transaction");
        statement.execute("rollback");
      }
    }

    assertEquals(0, mockSpanner.countRequestsOfType(CommitRequest.class));
  }
}
