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

import com.google.cloud.spanner.Dialect;
import com.google.cloud.spanner.MockSpannerServiceImpl.StatementResult;
import com.google.cloud.spanner.connection.AbstractMockServerTest;
import com.google.cloud.spanner.connection.SpannerPool;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RandomResultSetTest extends AbstractMockServerTest {
  @Parameter public Dialect dialect;

  @Parameters(name = "dialect = {0}")
  public static Object[] data() {
    return Dialect.values();
  }

  @Before
  public void setupDialect() {
    mockSpanner.putStatementResult(StatementResult.detectDialectResult(dialect));
  }

  @After
  public void reset() {
    // This ensures that each test gets a fresh Spanner instance. This is necessary to get a new
    // dialect result for each connection.
    SpannerPool.closeSpannerPool();
  }

  @Test
  public void testSelectRandomResults() throws SQLException {
    try (Connection connection = createJdbcConnection()) {
      try (ResultSet resultSet =
          connection.createStatement().executeQuery(SELECT_RANDOM_STATEMENT.getSql())) {
        ResultSetMetaData metadata = resultSet.getMetaData();
        int col = 0;
        assertEquals(Types.BOOLEAN, metadata.getColumnType(++col));
        assertEquals(Types.BIGINT, metadata.getColumnType(++col));
        assertEquals(Types.DOUBLE, metadata.getColumnType(++col));
        assertEquals(Types.REAL, metadata.getColumnType(++col));
        assertEquals(Types.NVARCHAR, metadata.getColumnType(++col));
        assertEquals(Types.BINARY, metadata.getColumnType(++col));
        assertEquals(Types.DATE, metadata.getColumnType(++col));
        assertEquals(Types.TIMESTAMP, metadata.getColumnType(++col));

        assertEquals(Types.ARRAY, metadata.getColumnType(++col));
        assertEquals(Types.ARRAY, metadata.getColumnType(++col));
        assertEquals(Types.ARRAY, metadata.getColumnType(++col));
        assertEquals(Types.ARRAY, metadata.getColumnType(++col));
        assertEquals(Types.ARRAY, metadata.getColumnType(++col));
        assertEquals(Types.ARRAY, metadata.getColumnType(++col));
        assertEquals(Types.ARRAY, metadata.getColumnType(++col));
        assertEquals(Types.ARRAY, metadata.getColumnType(++col));

        int rowCount = 0;
        while (resultSet.next()) {
          // Verify that we can get all columns as an object.
          for (col = 1; col <= resultSet.getMetaData().getColumnCount(); col++) {
            resultSet.getObject(col);
          }

          // Verify that we can get the results as the actual type.
          col = 0;
          resultSet.getBoolean(++col);
          resultSet.getLong(++col);
          resultSet.getDouble(++col);
          resultSet.getFloat(++col);
          resultSet.getString(++col);
          resultSet.getBytes(++col);
          resultSet.getDate(++col);
          resultSet.getTimestamp(++col);

          rowCount++;
        }
        assertEquals(RANDOM_RESULT_SET_ROW_COUNT, rowCount);
      }
    }
  }
}
