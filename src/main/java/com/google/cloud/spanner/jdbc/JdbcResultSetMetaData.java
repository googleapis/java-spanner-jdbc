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

import com.google.cloud.spanner.ResultSet;
import com.google.common.base.Preconditions;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/** Implementation of {@link ResultSetMetaData} for Cloud Spanner */
class JdbcResultSetMetaData extends AbstractJdbcWrapper implements ResultSetMetaData {
  /**
   * The default column display size for columns with a data type of variable size that is used when
   * the actual column size is not known.
   */
  private static final int DEFAULT_COL_DISPLAY_SIZE_FOR_VARIABLE_LENGTH_COLS = 50;

  private final ResultSet spannerResultSet;
  private final Statement statement;

  JdbcResultSetMetaData(JdbcResultSet jdbcResultSet, Statement statement) {
    Preconditions.checkNotNull(jdbcResultSet);
    this.spannerResultSet = jdbcResultSet.spanner;
    this.statement = statement;
  }

  @Override
  public boolean isClosed() {
    return false;
  }

  @Override
  public int getColumnCount() {
    return spannerResultSet.getColumnCount();
  }

  @Override
  public boolean isAutoIncrement(int column) {
    return false;
  }

  @Override
  public boolean isCaseSensitive(int column) {
    int type = getColumnType(column);
    return type == Types.NVARCHAR || type == Types.BINARY;
  }

  @Override
  public boolean isSearchable(int column) {
    return true;
  }

  @Override
  public boolean isCurrency(int column) {
    return false;
  }

  @Override
  public int isNullable(int column) {
    return columnNullableUnknown;
  }

  @Override
  public boolean isSigned(int column) {
    int type = getColumnType(column);
    return type == Types.DOUBLE || type == Types.BIGINT || type == Types.NUMERIC;
  }

  @Override
  public int getColumnDisplaySize(int column) {
    int colType = getColumnType(column);
    switch (colType) {
      case Types.ARRAY:
        return DEFAULT_COL_DISPLAY_SIZE_FOR_VARIABLE_LENGTH_COLS;
      case Types.BOOLEAN:
        return 5;
      case Types.BINARY:
        int binaryLength = getPrecision(column);
        return binaryLength == 0 ? DEFAULT_COL_DISPLAY_SIZE_FOR_VARIABLE_LENGTH_COLS : binaryLength;
      case Types.DATE:
        return 10;
      case Types.REAL:
        return JdbcDataType.FLOAT32.getDefaultColumnDisplaySize();
      case Types.FLOAT:
      case Types.DOUBLE:
        return 14;
      case Types.BIGINT:
        return 10;
      case Types.NUMERIC:
        return 14;
      case Types.NVARCHAR:
        int length = getPrecision(column);
        return length == 0 ? DEFAULT_COL_DISPLAY_SIZE_FOR_VARIABLE_LENGTH_COLS : length;
      case Types.TIMESTAMP:
        return 16;
      default:
        return 10;
    }
  }

  @Override
  public String getColumnLabel(int column) {
    return spannerResultSet.getType().getStructFields().get(column - 1).getName();
  }

  @Override
  public String getColumnName(int column) {
    return spannerResultSet.getType().getStructFields().get(column - 1).getName();
  }

  @Override
  public String getSchemaName(int column) throws SQLException {
    return statement.getConnection().getSchema();
  }

  @Override
  public int getPrecision(int column) {
    int colType = getColumnType(column);
    switch (colType) {
      case Types.BOOLEAN:
        return 1;
      case Types.DATE:
        return 10;
      case Types.REAL:
        return JdbcDataType.FLOAT32.getPrecision();
      case Types.FLOAT:
      case Types.DOUBLE:
        return 14;
      case Types.BIGINT:
      case Types.INTEGER:
        return 10;
      case Types.NUMERIC:
        return 14;
      case Types.TIMESTAMP:
        return 24;
      default:
        // For column types with variable size, such as text columns, we should return the length
        // in characters. We could try to fetch it from INFORMATION_SCHEMA, but that would mean
        // parsing the SQL statement client side in order to figure out which column it actually
        // is. For now we just return the default column display size.
        return DEFAULT_COL_DISPLAY_SIZE_FOR_VARIABLE_LENGTH_COLS;
    }
  }

  @Override
  public int getScale(int column) {
    int colType = getColumnType(column);
    if (colType == Types.REAL) {
      return JdbcDataType.FLOAT32.getScale();
    }
    if (colType == Types.DOUBLE || colType == Types.NUMERIC) {
      return 15;
    }
    return 0;
  }

  @Override
  public String getTableName(int column) {
    return "";
  }

  @Override
  public String getCatalogName(int column) throws SQLException {
    return statement.getConnection().getCatalog();
  }

  @Override
  public int getColumnType(int column) {
    return extractColumnType(spannerResultSet.getColumnType(column - 1));
  }

  @Override
  public String getColumnTypeName(int column) {
    return spannerResultSet.getColumnType(column - 1).getCode().name();
  }

  @Override
  public boolean isReadOnly(int column) {
    return false;
  }

  @Override
  public boolean isWritable(int column) {
    return !isReadOnly(column);
  }

  @Override
  public boolean isDefinitelyWritable(int column) {
    return false;
  }

  @Override
  public String getColumnClassName(int column) {
    return getClassName(spannerResultSet.getColumnType(column - 1));
  }

  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();
    for (int col = 1; col <= getColumnCount(); col++) {
      res.append("Col ").append(col).append(": ");
      res.append(getColumnName(col)).append(" ").append(getColumnTypeName(col));
      res.append("\n");
    }
    return res.toString();
  }
}
