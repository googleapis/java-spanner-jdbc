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

import com.google.cloud.spanner.Dialect;
import com.google.cloud.spanner.MockSpannerServiceImpl.StatementResult;
import com.google.cloud.spanner.PartitionOptions;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.connection.AbstractMockServerTest;
import com.google.cloud.spanner.connection.RandomResultSetGenerator;
import com.google.cloud.spanner.connection.SpannerPool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PartitionedQueryMockServerTest extends AbstractMockServerTest {
  @Parameter public Dialect dialect;

  @Parameters(name = "dialect = {0}")
  public static Object[] data() {
    return Dialect.values();
  }

  @Before
  public void setupDialect() {
    mockSpanner.putStatementResult(StatementResult.detectDialectResult(this.dialect));
  }

  @After
  public void clearRequests() {
    mockSpanner.clearRequests();
    SpannerPool.closeSpannerPool();
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
  public void testPartitionedQueryUsingSql() throws SQLException {
    int numRows = 5;
    int maxPartitions = 4;
    RandomResultSetGenerator generator = new RandomResultSetGenerator(numRows);
    Statement statement =
        Statement.newBuilder(
                String.format(
                    "select * from my_table where active=%s",
                    dialect == Dialect.POSTGRESQL ? "$1" : "@p1"))
            .bind("p1")
            .to(true)
            .build();
    mockSpanner.putStatementResult(StatementResult.query(statement, generator.generate()));
    String prefix = dialect == Dialect.POSTGRESQL ? "spanner." : "";

    try (Connection connection = createConnection()) {
      // This will automatically enable Data Boost for any partitioned query that is executed on
      // this connection. The property is ignored for any query that is not a partitioned query.
      connection.createStatement().execute(String.format("set %sdata_boost_enabled=true", prefix));
      // Sets the maximum number of partitions that should be used by Cloud Spanner.
      // This is just a hint that can be ignored by Cloud Spanner, but the mock server that is used
      // for testing respects this hint.
      connection
          .createStatement()
          .execute(String.format("set %smax_partitions=%d", prefix, maxPartitions));

      List<String> partitionIds = new ArrayList<>();
      try (PreparedStatement partitionStatement =
          connection.prepareStatement("partition select * from my_table where active=?")) {
        partitionStatement.setBoolean(1, true);
        try (ResultSet partitions = partitionStatement.executeQuery()) {
          while (partitions.next()) {
            partitionIds.add(partitions.getString(1));
          }
        }
      }

      for (String partitionId : partitionIds) {
        // Execute `run partition '<partition-id>'` to get the results of a single partition.
        // This can be done from any JDBC connection, including from other hosts.
        try (ResultSet resultSet =
            connection
                .createStatement()
                .executeQuery(String.format("run partition '%s'", partitionId))) {
          int rowCount = 0;
          while (resultSet.next()) {
            rowCount++;
          }
          assertEquals(numRows, rowCount);
        }
      }

      for (String partitionId : partitionIds) {
        // The partition ID may also be set as a query parameter.
        try (PreparedStatement runStatement = connection.prepareStatement("run partition")) {
          runStatement.setString(1, partitionId);
          int rowCount = 0;
          try (ResultSet resultSet = runStatement.executeQuery()) {
            while (resultSet.next()) {
              rowCount++;
            }
            assertEquals(numRows, rowCount);
          }
        }
      }

      // You can also run a query directly as a partitioned query on a single JDBC connection. This
      // will not give you the full benefits of a partitioned query, as the entire query is handled
      // by a single host and is returned as a single result set. The result set uses multiple
      // threads internally to execute the separate partitions.
      // This gives users a simple way to access the Data Boost feature that should be accessible
      // from most generic frameworks and tools that work with JDBC.
      try (PreparedStatement partitionStatement =
          connection.prepareStatement(
              "run partitioned query select * from my_table where active=?")) {
        partitionStatement.setBoolean(1, true);
        try (ResultSet results = partitionStatement.executeQuery()) {
          int rowCount = 0;
          while (results.next()) {
            rowCount++;
          }
          // The mock server is not smart enough to actually partition the query and only return
          // a fraction of the rows per partition. The total row count will therefore be equal to
          // the number of partitions multiplied by the number of rows.
          assertEquals(numRows * maxPartitions, rowCount);

          // Partitioned queries return a result set with some additional metadata that can be
          // inspected to determine the number of partitions and the degree of parallelism that the
          // query used.
          CloudSpannerJdbcPartitionedQueryResultSet partitionedQueryResultSet =
              results.unwrap(CloudSpannerJdbcPartitionedQueryResultSet.class);
          assertEquals(maxPartitions, partitionedQueryResultSet.getNumPartitions());
          assertEquals(1, partitionedQueryResultSet.getParallelism());
        }
      }
    }
  }

  @Test
  public void testPartitionedQueryStatement() throws SQLException {
    int numRows = 5;
    int maxPartitions = 4;
    RandomResultSetGenerator generator = new RandomResultSetGenerator(numRows);
    Statement statement = Statement.of("select * from my_table where active=true");
    mockSpanner.putStatementResult(StatementResult.query(statement, generator.generate()));

    try (Connection connection = createConnection()) {
      CloudSpannerJdbcConnection cloudSpannerJdbcConnection =
          connection.unwrap(CloudSpannerJdbcConnection.class);
      // This will automatically enable Data Boost for any partitioned query that is executed on
      // this connection. The property is ignored for any query that is not a partitioned query.
      cloudSpannerJdbcConnection.setDataBoostEnabled(true);
      // Sets the maximum number of partitions that should be used by Cloud Spanner.
      // This is just a hint that can be ignored by Cloud Spanner, but the mock server that is used
      // for testing respects this hint.
      cloudSpannerJdbcConnection.setMaxPartitions(maxPartitions);

      List<String> partitionIds = new ArrayList<>();
      try (ResultSet partitions =
          connection
              .createStatement()
              .unwrap(CloudSpannerJdbcStatement.class)
              .partitionQuery(
                  "select * from my_table where active=true",
                  PartitionOptions.getDefaultInstance())) {
        while (partitions.next()) {
          partitionIds.add(partitions.getString(1));
        }
      }

      for (String partitionId : partitionIds) {
        // Execute `run partition '<partition-id>'` to get the results of a single partition.
        // This can be done from any JDBC connection, including from other hosts.
        try (ResultSet resultSet =
            connection
                .createStatement()
                .unwrap(CloudSpannerJdbcStatement.class)
                .runPartition(partitionId)) {
          int rowCount = 0;
          while (resultSet.next()) {
            rowCount++;
          }
          assertEquals(numRows, rowCount);
        }
      }

      // You can also run a query directly as a partitioned query on a single JDBC connection. This
      // will not give you the full benefits of a partitioned query, as the entire query is handled
      // by a single host and is returned as a single result set. The result set uses multiple
      // threads internally to execute the separate partitions.
      // This gives users a simple way to access the Data Boost feature that should be accessible
      // from most generic frameworks and tools that work with JDBC.
      try (CloudSpannerJdbcPartitionedQueryResultSet results =
          connection
              .createStatement()
              .unwrap(CloudSpannerJdbcStatement.class)
              .runPartitionedQuery(
                  "select * from my_table where active=true",
                  PartitionOptions.getDefaultInstance())) {
        int rowCount = 0;
        while (results.next()) {
          rowCount++;
        }
        // The mock server is not smart enough to actually partition the query and only return
        // a fraction of the rows per partition. The total row count will therefore be equal to
        // the number of partitions multiplied by the number of rows.
        assertEquals(numRows * maxPartitions, rowCount);

        // Partitioned queries return a result set with some additional metadata that can be
        // inspected to determine the number of partitions and the degree of parallelism that the
        // query used.
        assertEquals(maxPartitions, results.getNumPartitions());
        assertEquals(1, results.getParallelism());
      }
    }
  }

  @Test
  public void testPartitionedQueryPreparedStatement() throws SQLException {
    int numRows = 5;
    int maxPartitions = 4;
    RandomResultSetGenerator generator = new RandomResultSetGenerator(numRows);
    Statement statement =
        Statement.newBuilder(
                String.format(
                    "select * from my_table where active=%s",
                    dialect == Dialect.POSTGRESQL ? "$1" : "@p1"))
            .bind("p1")
            .to(true)
            .build();
    mockSpanner.putStatementResult(StatementResult.query(statement, generator.generate()));

    try (Connection connection = createConnection()) {
      CloudSpannerJdbcConnection cloudSpannerJdbcConnection =
          connection.unwrap(CloudSpannerJdbcConnection.class);
      // This will automatically enable Data Boost for any partitioned query that is executed on
      // this connection. The property is ignored for any query that is not a partitioned query.
      cloudSpannerJdbcConnection.setDataBoostEnabled(true);
      // Sets the maximum number of partitions that should be used by Cloud Spanner.
      // This is just a hint that can be ignored by Cloud Spanner, but the mock server that is used
      // for testing respects this hint.
      cloudSpannerJdbcConnection.setMaxPartitions(maxPartitions);

      List<String> partitionIds = new ArrayList<>();
      try (PreparedStatement partitionStatement =
          connection.prepareStatement("select * from my_table where active=?")) {
        partitionStatement.setBoolean(1, true);
        try (ResultSet partitions =
            partitionStatement
                .unwrap(CloudSpannerJdbcPreparedStatement.class)
                .partitionQuery(PartitionOptions.getDefaultInstance())) {
          while (partitions.next()) {
            partitionIds.add(partitions.getString(1));
          }
        }
      }

      for (String partitionId : partitionIds) {
        // The partition ID can also be set as a query parameter.
        // The statement text in this case is ignored and can be an empty string.
        try (PreparedStatement runStatement = connection.prepareStatement("")) {
          runStatement.setString(1, partitionId);
          int rowCount = 0;
          try (ResultSet resultSet =
              runStatement.unwrap(CloudSpannerJdbcPreparedStatement.class).runPartition()) {
            while (resultSet.next()) {
              rowCount++;
            }
            assertEquals(numRows, rowCount);
          }
        }
      }

      // You can also run a query directly as a partitioned query on a single JDBC connection. This
      // will not give you the full benefits of a partitioned query, as the entire query is handled
      // by a single host and is returned as a single result set. The result set uses multiple
      // threads internally to execute the separate partitions.
      // This gives users a simple way to access the Data Boost feature that should be accessible
      // from most generic frameworks and tools that work with JDBC.
      try (PreparedStatement preparedStatement =
          connection.prepareStatement("select * from my_table where active=?")) {
        preparedStatement.setBoolean(1, true);
        try (CloudSpannerJdbcPartitionedQueryResultSet results =
            preparedStatement
                .unwrap(CloudSpannerJdbcPreparedStatement.class)
                .runPartitionedQuery(PartitionOptions.getDefaultInstance())) {
          int rowCount = 0;
          while (results.next()) {
            rowCount++;
          }
          // The mock server is not smart enough to actually partition the query and only return
          // a fraction of the rows per partition. The total row count will therefore be equal to
          // the number of partitions multiplied by the number of rows.
          assertEquals(numRows * maxPartitions, rowCount);

          // Partitioned queries return a result set with some additional metadata that can be
          // inspected to determine the number of partitions and the degree of parallelism that the
          // query used.
          assertEquals(maxPartitions, results.getNumPartitions());
          assertEquals(1, results.getParallelism());
        }
      }
    }
  }
}
