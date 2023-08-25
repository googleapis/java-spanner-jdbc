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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.cloud.spanner.MockSpannerServiceImpl.StatementResult;
import com.google.cloud.spanner.SessionPoolOptions;
import com.google.cloud.spanner.connection.AbstractMockServerTest;
import com.google.cloud.spanner.connection.ConnectionOptions;
import com.google.protobuf.ListValue;
import com.google.protobuf.Value;
import com.google.rpc.Code;
import com.google.spanner.v1.ResultSetMetadata;
import com.google.spanner.v1.StructType;
import com.google.spanner.v1.StructType.Field;
import com.google.spanner.v1.Type;
import com.google.spanner.v1.TypeCode;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for verifying that the methods execute, executeQuery, and executeUpdate work as
 * intended.
 */
@RunWith(JUnit4.class)
public class ExecuteMockServerTest extends AbstractMockServerTest {
  private static final String QUERY = "select * from my_table";

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
  }

  private String createUrl() {
    return String.format(
        "jdbc:cloudspanner://localhost:%d/projects/%s/instances/%s/databases/%s?usePlainText=true",
        getPort(), "proj", "inst", "db");
  }

  @Test
  public void testInvalidExecuteUpdate_shouldNotLeakSession() throws SQLException {
    int maxSessions = 1;
    try (Connection connection =
        new JdbcConnection(
            createUrl(),
            ConnectionOptions.newBuilder()
                .setUri(createUrl().substring("jdbc:".length()))
                .setSessionPoolOptions(
                    SessionPoolOptions.newBuilder()
                        .setMinSessions(1)
                        .setMaxSessions(maxSessions)
                        .setFailIfPoolExhausted()
                        .build())
                .build())) {

      for (int i = 0; i < (maxSessions + 1); i++) {
        SQLException exception =
            assertThrows(
                SQLException.class, () -> connection.createStatement().executeUpdate(QUERY));
        assertTrue(exception instanceof JdbcSqlException);
        JdbcSqlException jdbcSqlException = (JdbcSqlException) exception;
        // This would be RESOURCE_EXHAUSTED if the query leaked a session.
        assertEquals(Code.INVALID_ARGUMENT, jdbcSqlException.getCode());
      }
    }
  }
}
