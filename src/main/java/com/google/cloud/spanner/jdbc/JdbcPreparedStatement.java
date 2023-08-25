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

import com.google.cloud.spanner.Options.QueryOption;
import com.google.cloud.spanner.ReadContext.QueryAnalyzeMode;
import com.google.cloud.spanner.ResultSets;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.Type;
import com.google.cloud.spanner.connection.AbstractStatementParser.ParametersInfo;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.rpc.Code;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.annotation.Nullable;

/** Implementation of {@link PreparedStatement} for Cloud Spanner. */
class JdbcPreparedStatement extends AbstractJdbcPreparedStatement {
  private static final char POS_PARAM_CHAR = '?';
  private final String sql;
  private final String sqlWithoutComments;
  private final ParametersInfo parameters;
  private final @Nullable ImmutableList<String> generatedKeysColumns;

  JdbcPreparedStatement(
      JdbcConnection connection, String sql, @Nullable ImmutableList<String> generatedKeysColumns)
      throws SQLException {
    super(connection);
    this.sql = sql;
    try {
      this.sqlWithoutComments = parser.removeCommentsAndTrim(this.sql);
      this.parameters =
          parser.convertPositionalParametersToNamedParameters(POS_PARAM_CHAR, sqlWithoutComments);
    } catch (SpannerException e) {
      throw JdbcSqlExceptionFactory.of(e);
    }
    this.generatedKeysColumns = generatedKeysColumns;
  }

  ParametersInfo getParametersInfo() {
    return parameters;
  }

  @VisibleForTesting
  Statement createStatement() throws SQLException {
    ParametersInfo paramInfo = getParametersInfo();
    Statement.Builder builder = Statement.newBuilder(paramInfo.sqlWithNamedParameters);
    for (int index = 1; index <= getParameters().getHighestIndex(); index++) {
      getParameters().bindParameterValue(builder.bind("p" + index), index);
    }
    return builder.build();
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    checkClosed();
    return executeQuery(createStatement());
  }

  ResultSet executeQueryWithOptions(QueryOption... options) throws SQLException {
    checkClosed();
    return executeQuery(createStatement(), options);
  }

  @Override
  public int executeUpdate() throws SQLException {
    long count = internalExecuteLargeUpdate(createStatement(), generatedKeysColumns);
    if (count > Integer.MAX_VALUE) {
      throw JdbcSqlExceptionFactory.of(
          "update count too large for executeUpdate: " + count, Code.OUT_OF_RANGE);
    }
    return (int) count;
  }

  @Override
  public long executeLargeUpdate() throws SQLException {
    return internalExecuteLargeUpdate(createStatement(), generatedKeysColumns);
  }

  @Override
  public boolean execute() throws SQLException {
    return executeStatement(createStatement(), generatedKeysColumns);
  }

  @Override
  public void addBatch() throws SQLException {
    checkClosed();
    checkAndSetBatchType(sql);
    batchedStatements.add(createStatement());
  }

  @Override
  public JdbcParameterMetaData getParameterMetaData() throws SQLException {
    checkClosed();
    return new JdbcParameterMetaData(this);
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    checkClosed();
    if (getConnection().getParser().isUpdateStatement(sql)) {
      // Return metadata for an empty result set as DML statements do not return any results (as a
      // result set).
      com.google.cloud.spanner.ResultSet resultSet =
          ResultSets.forRows(Type.struct(), ImmutableList.of());
      resultSet.next();
      return new JdbcResultSetMetaData(JdbcResultSet.of(resultSet), this);
    }
    try (ResultSet rs = analyzeQuery(createStatement(), QueryAnalyzeMode.PLAN)) {
      return rs.getMetaData();
    }
  }
}
