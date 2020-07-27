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

import com.google.cloud.spanner.Type;
import com.google.cloud.spanner.Type.Code;
import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/** Implementation of {@link ResultSet} for Cloud Spanner */
class JdbcResultSet extends AbstractJdbcResultSet {

  static JdbcResultSet of(com.google.cloud.spanner.ResultSet resultSet) {
    Preconditions.checkNotNull(resultSet);
    return new JdbcResultSet(null, resultSet);
  }

  static JdbcResultSet of(Statement statement, com.google.cloud.spanner.ResultSet resultSet) {
    Preconditions.checkNotNull(statement);
    Preconditions.checkNotNull(resultSet);
    return new JdbcResultSet(statement, resultSet);
  }

  private boolean closed = false;
  private final Statement statement;
  private boolean wasNull = false;
  private boolean nextReturnedFalse = false;
  private boolean nextCalledForMetaData = false;
  private boolean nextCalledForMetaDataResult = false;
  private long currentRow = 0L;

  private JdbcResultSet(Statement statement, com.google.cloud.spanner.ResultSet spanner) {
    super(spanner);
    this.statement = statement;
  }

  void checkClosedAndValidRow() throws SQLException {
    checkClosed();
    if (currentRow == 0L) {
      throw JdbcSqlExceptionFactory.of(
          "ResultSet is before first row. Call next() first.",
          com.google.rpc.Code.FAILED_PRECONDITION);
    }
    if (nextReturnedFalse) {
      throw JdbcSqlExceptionFactory.of(
          "ResultSet is after last row. There is no more data available.",
          com.google.rpc.Code.FAILED_PRECONDITION);
    }
  }

  @Override
  public boolean next() throws SQLException {
    checkClosed();
    currentRow++;
    if (nextCalledForMetaData) {
      nextReturnedFalse = !nextCalledForMetaDataResult;
      nextCalledForMetaData = false;
    } else {
      nextReturnedFalse = !spanner.next();
    }

    return !nextReturnedFalse;
  }

  @Override
  public void close() throws SQLException {
    spanner.close();
    this.closed = true;
  }

  @Override
  public boolean wasNull() throws SQLException {
    checkClosedAndValidRow();
    return wasNull;
  }

  private boolean isNull(int columnIndex) {
    wasNull = spanner.isNull(columnIndex - 1);
    return wasNull;
  }

  SQLException createInvalidToGetAs(String sqlType, Code type) {
    return JdbcSqlExceptionFactory.of(
        String.format("Invalid column type to get as %s: %s", sqlType, type.name()),
        com.google.rpc.Code.INVALID_ARGUMENT);
  }

  @Override
  public String getString(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case BOOL:
        return isNull ? null : String.valueOf(spanner.getBoolean(spannerIndex));
      case BYTES:
        return isNull ? null : spanner.getBytes(spannerIndex).toBase64();
      case DATE:
        return isNull ? null : spanner.getDate(spannerIndex).toString();
      case FLOAT64:
        return isNull ? null : Double.toString(spanner.getDouble(spannerIndex));
      case INT64:
        return isNull ? null : Long.toString(spanner.getLong(spannerIndex));
      case NUMERIC:
        return isNull ? null : spanner.getBigDecimal(spannerIndex).toString();
      case STRING:
        return isNull ? null : spanner.getString(spannerIndex);
      case TIMESTAMP:
        return isNull ? null : spanner.getTimestamp(spannerIndex).toString();
      case STRUCT:
      case ARRAY:
      default:
        throw createInvalidToGetAs("string", type);
    }
  }

  @Override
  public boolean getBoolean(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case BOOL:
        return isNull ? false : spanner.getBoolean(spannerIndex);
      case FLOAT64:
        return isNull ? false : spanner.getDouble(spannerIndex) != 0D;
      case INT64:
        return isNull ? false : spanner.getLong(spannerIndex) != 0L;
      case NUMERIC:
        return isNull ? false : !spanner.getBigDecimal(spannerIndex).equals(BigDecimal.ZERO);
      case STRING:
        return isNull ? false : Boolean.valueOf(spanner.getString(spannerIndex));
      case BYTES:
      case DATE:
      case STRUCT:
      case TIMESTAMP:
      case ARRAY:
      default:
        throw createInvalidToGetAs("boolean", type);
    }
  }

  @Override
  public byte getByte(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case BOOL:
        return isNull ? (byte) 0 : (spanner.getBoolean(spannerIndex) ? (byte) 1 : 0);
      case FLOAT64:
        return isNull
            ? (byte) 0
            : checkedCastToByte(Double.valueOf(spanner.getDouble(spannerIndex)).longValue());
      case INT64:
        return isNull ? (byte) 0 : checkedCastToByte(spanner.getLong(spannerIndex));
      case NUMERIC:
        return isNull ? (byte) 0 : checkedCastToByte(spanner.getBigDecimal(spannerIndex));
      case STRING:
        return isNull ? (byte) 0 : checkedCastToByte(parseLong(spanner.getString(spannerIndex)));
      case BYTES:
      case DATE:
      case STRUCT:
      case TIMESTAMP:
      case ARRAY:
      default:
        throw createInvalidToGetAs("byte", type);
    }
  }

  @Override
  public short getShort(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case BOOL:
        return isNull ? 0 : (spanner.getBoolean(spannerIndex) ? (short) 1 : 0);
      case FLOAT64:
        return isNull
            ? 0
            : checkedCastToShort(Double.valueOf(spanner.getDouble(spannerIndex)).longValue());
      case INT64:
        return isNull ? 0 : checkedCastToShort(spanner.getLong(spannerIndex));
      case NUMERIC:
        return isNull ? 0 : checkedCastToShort(spanner.getBigDecimal(spannerIndex));
      case STRING:
        return isNull ? 0 : checkedCastToShort(parseLong(spanner.getString(spannerIndex)));
      case BYTES:
      case DATE:
      case STRUCT:
      case TIMESTAMP:
      case ARRAY:
      default:
        throw createInvalidToGetAs("short", type);
    }
  }

  @Override
  public int getInt(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case BOOL:
        return isNull ? 0 : (spanner.getBoolean(spannerIndex) ? 1 : 0);
      case FLOAT64:
        return isNull
            ? 0
            : checkedCastToInt(Double.valueOf(spanner.getDouble(spannerIndex)).longValue());
      case INT64:
        return isNull ? 0 : checkedCastToInt(spanner.getLong(spannerIndex));
      case NUMERIC:
        return isNull ? 0 : checkedCastToInt(spanner.getBigDecimal(spannerIndex));
      case STRING:
        return isNull ? 0 : checkedCastToInt(parseLong(spanner.getString(spannerIndex)));
      case BYTES:
      case DATE:
      case STRUCT:
      case TIMESTAMP:
      case ARRAY:
      default:
        throw createInvalidToGetAs("int", type);
    }
  }

  @Override
  public long getLong(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case BOOL:
        return isNull ? 0L : (spanner.getBoolean(spannerIndex) ? 1L : 0L);
      case FLOAT64:
        return isNull ? 0L : Double.valueOf(spanner.getDouble(spannerIndex)).longValue();
      case INT64:
        return isNull ? 0L : spanner.getLong(spannerIndex);
      case NUMERIC:
        return isNull ? 0L : checkedCastToLong(spanner.getBigDecimal(spannerIndex));
      case STRING:
        return isNull ? 0L : parseLong(spanner.getString(spannerIndex));
      case BYTES:
      case DATE:
      case STRUCT:
      case TIMESTAMP:
      case ARRAY:
      default:
        throw createInvalidToGetAs("long", type);
    }
  }

  @Override
  public float getFloat(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case BOOL:
        return isNull ? 0 : (spanner.getBoolean(spannerIndex) ? (float) 1 : 0);
      case FLOAT64:
        return isNull ? 0 : checkedCastToFloat(spanner.getDouble(spannerIndex));
      case INT64:
        return isNull ? 0 : checkedCastToFloat(spanner.getLong(spannerIndex));
      case NUMERIC:
        return isNull ? 0 : spanner.getBigDecimal(spannerIndex).floatValue();
      case STRING:
        return isNull ? 0 : checkedCastToFloat(parseDouble(spanner.getString(spannerIndex)));
      case BYTES:
      case DATE:
      case STRUCT:
      case TIMESTAMP:
      case ARRAY:
      default:
        throw createInvalidToGetAs("float", type);
    }
  }

  @Override
  public double getDouble(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case BOOL:
        return isNull ? 0 : (spanner.getBoolean(spannerIndex) ? (double) 1 : 0);
      case FLOAT64:
        return isNull ? 0 : spanner.getDouble(spannerIndex);
      case INT64:
        return isNull ? 0 : spanner.getLong(spannerIndex);
      case NUMERIC:
        return isNull ? 0 : spanner.getBigDecimal(spannerIndex).doubleValue();
      case STRING:
        return isNull ? 0 : parseDouble(spanner.getString(spannerIndex));
      case BYTES:
      case DATE:
      case STRUCT:
      case TIMESTAMP:
      case ARRAY:
      default:
        throw createInvalidToGetAs("double", type);
    }
  }

  @Override
  public byte[] getBytes(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    return isNull(columnIndex) ? null : spanner.getBytes(columnIndex - 1).toByteArray();
  }

  @Override
  public Date getDate(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case DATE:
        return isNull ? null : JdbcTypeConverter.toSqlDate(spanner.getDate(spannerIndex));
      case STRING:
        return isNull ? null : parseDate(spanner.getString(spannerIndex));
      case TIMESTAMP:
        return isNull
            ? null
            : new Date(spanner.getTimestamp(spannerIndex).toSqlTimestamp().getTime());
      case BOOL:
      case FLOAT64:
      case INT64:
      case NUMERIC:
      case BYTES:
      case STRUCT:
      case ARRAY:
      default:
        throw createInvalidToGetAs("date", type);
    }
  }

  @Override
  public Time getTime(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case STRING:
        return isNull ? null : parseTime(spanner.getString(spannerIndex));
      case TIMESTAMP:
        return isNull ? null : JdbcTypeConverter.toSqlTime(spanner.getTimestamp(spannerIndex));
      case BOOL:
      case DATE:
      case FLOAT64:
      case INT64:
      case NUMERIC:
      case BYTES:
      case STRUCT:
      case ARRAY:
      default:
        throw createInvalidToGetAs("time", type);
    }
  }

  @Override
  public Timestamp getTimestamp(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case DATE:
        return isNull ? null : JdbcTypeConverter.toSqlTimestamp(spanner.getDate(spannerIndex));
      case STRING:
        return isNull ? null : parseTimestamp(spanner.getString(spannerIndex));
      case TIMESTAMP:
        return isNull ? null : JdbcTypeConverter.toSqlTimestamp(spanner.getTimestamp(spannerIndex));
      case BOOL:
      case FLOAT64:
      case INT64:
      case NUMERIC:
      case BYTES:
      case STRUCT:
      case ARRAY:
      default:
        throw createInvalidToGetAs("timestamp", type);
    }
  }

  private InputStream getInputStream(String val, Charset charset) {
    if (val == null) return null;
    byte[] b = val.getBytes(charset);
    return new ByteArrayInputStream(b);
  }

  @Override
  public InputStream getAsciiStream(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    return getInputStream(getString(columnIndex), StandardCharsets.US_ASCII);
  }

  @Override
  @Deprecated
  public InputStream getUnicodeStream(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    return getInputStream(getString(columnIndex), StandardCharsets.UTF_16LE);
  }

  @Override
  public InputStream getBinaryStream(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    byte[] val = getBytes(columnIndex);
    return val == null ? null : new ByteArrayInputStream(val);
  }

  @Override
  public String getString(String columnLabel) throws SQLException {
    return getString(findColumn(columnLabel));
  }

  @Override
  public boolean getBoolean(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getBoolean(findColumn(columnLabel));
  }

  @Override
  public byte getByte(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getByte(findColumn(columnLabel));
  }

  @Override
  public short getShort(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getShort(findColumn(columnLabel));
  }

  @Override
  public int getInt(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getInt(findColumn(columnLabel));
  }

  @Override
  public long getLong(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getLong(findColumn(columnLabel));
  }

  @Override
  public float getFloat(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getFloat(findColumn(columnLabel));
  }

  @Override
  public double getDouble(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getDouble(findColumn(columnLabel));
  }

  @Override
  public byte[] getBytes(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getBytes(findColumn(columnLabel));
  }

  @Override
  public Date getDate(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getDate(findColumn(columnLabel));
  }

  @Override
  public Time getTime(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getTime(findColumn(columnLabel));
  }

  @Override
  public Timestamp getTimestamp(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getTimestamp(findColumn(columnLabel));
  }

  @Override
  public InputStream getAsciiStream(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getAsciiStream(findColumn(columnLabel));
  }

  @Override
  @Deprecated
  public InputStream getUnicodeStream(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getUnicodeStream(findColumn(columnLabel));
  }

  @Override
  public InputStream getBinaryStream(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getBinaryStream(findColumn(columnLabel));
  }

  @Override
  public JdbcResultSetMetaData getMetaData() throws SQLException {
    checkClosed();
    if (isBeforeFirst()) {
      // do a call to next() on the underlying resultset to initialize metadata
      nextCalledForMetaData = true;
      nextCalledForMetaDataResult = spanner.next();
    }
    return new JdbcResultSetMetaData(this, statement);
  }

  @Override
  public Object getObject(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getObject(findColumn(columnLabel));
  }

  @Override
  public Object getObject(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    Type type = spanner.getColumnType(columnIndex - 1);
    return isNull(columnIndex) ? null : getObject(type, columnIndex);
  }

  private Object getObject(Type type, int columnIndex) throws SQLException {
    if (type == Type.bool()) return getBoolean(columnIndex);
    if (type == Type.bytes()) return getBytes(columnIndex);
    if (type == Type.date()) return getDate(columnIndex);
    if (type == Type.float64()) return getDouble(columnIndex);
    if (type == Type.int64()) return getLong(columnIndex);
    if (type == Type.numeric()) return getBigDecimal(columnIndex);
    if (type == Type.string()) return getString(columnIndex);
    if (type == Type.timestamp()) return getTimestamp(columnIndex);
    if (type.getCode() == Code.ARRAY) return getArray(columnIndex);
    throw JdbcSqlExceptionFactory.of(
        "Unknown type: " + type.toString(), com.google.rpc.Code.INVALID_ARGUMENT);
  }

  @Override
  public int findColumn(String columnLabel) throws SQLException {
    checkClosed();
    try {
      return spanner.getColumnIndex(columnLabel) + 1;
    } catch (IllegalArgumentException e) {
      throw JdbcSqlExceptionFactory.of(
          "no column with label " + columnLabel + " found", com.google.rpc.Code.INVALID_ARGUMENT);
    }
  }

  @Override
  public Reader getCharacterStream(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    String val = getString(columnIndex);
    return val == null ? null : new StringReader(val);
  }

  @Override
  public Reader getCharacterStream(String columnLabel) throws SQLException {
    return getCharacterStream(findColumn(columnLabel));
  }

  @Override
  public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    return getBigDecimal(columnIndex, false, 0);
  }

  @Override
  public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getBigDecimal(findColumn(columnLabel), false, 0);
  }

  @Override
  @Deprecated
  public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
    checkClosedAndValidRow();
    return getBigDecimal(columnIndex, true, scale);
  }

  @Override
  @Deprecated
  public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
    checkClosedAndValidRow();
    return getBigDecimal(findColumn(columnLabel), true, scale);
  }

  private BigDecimal getBigDecimal(int columnIndex, boolean fixedScale, int scale)
      throws SQLException {
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    boolean isNull = isNull(columnIndex);
    BigDecimal res;
    switch (type) {
      case BOOL:
        res =
            isNull
                ? null
                : (spanner.getBoolean(columnIndex - 1) ? BigDecimal.ONE : BigDecimal.ZERO);
        break;
      case FLOAT64:
        res = isNull ? null : BigDecimal.valueOf(spanner.getDouble(spannerIndex));
        break;
      case INT64:
        res = isNull ? null : BigDecimal.valueOf(spanner.getLong(spannerIndex));
        break;
      case NUMERIC:
        res = isNull ? null : spanner.getBigDecimal(spannerIndex);
        break;
      case STRING:
        try {
          res = isNull ? null : new BigDecimal(spanner.getString(spannerIndex));
          break;
        } catch (NumberFormatException e) {
          throw JdbcSqlExceptionFactory.of(
              "The column does not contain a valid BigDecimal",
              com.google.rpc.Code.INVALID_ARGUMENT,
              e);
        }
      case BYTES:
      case DATE:
      case TIMESTAMP:
      case STRUCT:
      case ARRAY:
      default:
        throw createInvalidToGetAs("BigDecimal", type);
    }
    if (res != null && fixedScale) {
      res = res.setScale(scale, RoundingMode.HALF_UP);
    }
    return res;
  }

  @Override
  public boolean isBeforeFirst() throws SQLException {
    checkClosed();
    return currentRow == 0L;
  }

  @Override
  public boolean isAfterLast() throws SQLException {
    checkClosed();
    return nextReturnedFalse;
  }

  @Override
  public boolean isFirst() throws SQLException {
    checkClosed();
    return currentRow == 1L;
  }

  @Override
  public int getRow() throws SQLException {
    checkClosed();
    return checkedCastToInt(currentRow);
  }

  @Override
  public Statement getStatement() throws SQLException {
    checkClosed();
    return statement;
  }

  @Override
  public Array getArray(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getArray(findColumn(columnLabel));
  }

  @Override
  public Array getArray(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    if (isNull(columnIndex)) return null;
    Type type = spanner.getColumnType(columnIndex - 1);
    if (type.getCode() != Code.ARRAY)
      throw JdbcSqlExceptionFactory.of(
          "Column with index " + columnIndex + " does not contain an array",
          com.google.rpc.Code.INVALID_ARGUMENT);
    JdbcDataType dataType = JdbcDataType.getType(type.getArrayElementType().getCode());
    List<? extends Object> elements = dataType.getArrayElements(spanner, columnIndex - 1);

    return JdbcArray.createArray(dataType, elements);
  }

  @Override
  public Date getDate(int columnIndex, Calendar cal) throws SQLException {
    checkClosedAndValidRow();
    if (isNull(columnIndex)) {
      return null;
    }
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case DATE:
        return JdbcTypeConverter.toSqlDate(spanner.getDate(spannerIndex), cal);
      case STRING:
        return parseDate(spanner.getString(spannerIndex), cal);
      case TIMESTAMP:
        return new Date(
            JdbcTypeConverter.getAsSqlTimestamp(spanner.getTimestamp(spannerIndex), cal).getTime());
      case BOOL:
      case FLOAT64:
      case INT64:
      case NUMERIC:
      case BYTES:
      case STRUCT:
      case ARRAY:
      default:
        throw createInvalidToGetAs("date", type);
    }
  }

  @Override
  public Date getDate(String columnLabel, Calendar cal) throws SQLException {
    return getDate(findColumn(columnLabel), cal);
  }

  @Override
  public Time getTime(int columnIndex, Calendar cal) throws SQLException {
    checkClosedAndValidRow();
    boolean isNull = isNull(columnIndex);
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case STRING:
        return isNull ? null : parseTime(spanner.getString(spannerIndex), cal);
      case TIMESTAMP:
        return isNull ? null : JdbcTypeConverter.toSqlTime(spanner.getTimestamp(spannerIndex), cal);
      case BOOL:
      case DATE:
      case FLOAT64:
      case INT64:
      case NUMERIC:
      case BYTES:
      case STRUCT:
      case ARRAY:
      default:
        throw createInvalidToGetAs("time", type);
    }
  }

  @Override
  public Time getTime(String columnLabel, Calendar cal) throws SQLException {
    return getTime(findColumn(columnLabel), cal);
  }

  @Override
  public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
    checkClosedAndValidRow();
    if (isNull(columnIndex)) {
      return null;
    }
    int spannerIndex = columnIndex - 1;
    Code type = spanner.getColumnType(spannerIndex).getCode();
    switch (type) {
      case DATE:
        return JdbcTypeConverter.toSqlTimestamp(spanner.getDate(spannerIndex), cal);
      case STRING:
        return parseTimestamp(spanner.getString(spannerIndex), cal);
      case TIMESTAMP:
        return JdbcTypeConverter.getAsSqlTimestamp(spanner.getTimestamp(spannerIndex), cal);
      case BOOL:
      case FLOAT64:
      case INT64:
      case NUMERIC:
      case BYTES:
      case STRUCT:
      case ARRAY:
      default:
        throw createInvalidToGetAs("timestamp", type);
    }
  }

  @Override
  public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
    return getTimestamp(findColumn(columnLabel), cal);
  }

  @Override
  public URL getURL(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    try {
      return isNull(columnIndex) ? null : new URL(getString(columnIndex));
    } catch (MalformedURLException e) {
      throw JdbcSqlExceptionFactory.of(
          "Invalid URL: " + spanner.getString(columnIndex - 1),
          com.google.rpc.Code.INVALID_ARGUMENT);
    }
  }

  @Override
  public URL getURL(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getURL(findColumn(columnLabel));
  }

  @Override
  public int getHoldability() throws SQLException {
    checkClosed();
    return CLOSE_CURSORS_AT_COMMIT;
  }

  @Override
  public boolean isClosed() throws SQLException {
    return closed;
  }

  @Override
  public String getNString(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    return getString(columnIndex);
  }

  @Override
  public String getNString(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getString(columnLabel);
  }

  @Override
  public Reader getNCharacterStream(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    return getCharacterStream(columnIndex);
  }

  @Override
  public Reader getNCharacterStream(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    return getCharacterStream(columnLabel);
  }

  @Override
  public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
    checkClosedAndValidRow();
    return convertObject(getObject(columnIndex), type, spanner.getColumnType(columnIndex - 1));
  }

  @Override
  public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
    checkClosedAndValidRow();
    return convertObject(getObject(columnLabel), type, spanner.getColumnType(columnLabel));
  }

  @Override
  public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
    checkClosedAndValidRow();
    return convertObject(getObject(columnIndex), map, spanner.getColumnType(columnIndex - 1));
  }

  @Override
  public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
    checkClosedAndValidRow();
    return convertObject(getObject(columnLabel), map, spanner.getColumnType(columnLabel));
  }

  @Override
  public Blob getBlob(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    byte[] val = getBytes(columnIndex);
    return val == null ? null : new JdbcBlob(val);
  }

  @Override
  public Blob getBlob(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    byte[] val = getBytes(columnLabel);
    return val == null ? null : new JdbcBlob(val);
  }

  @Override
  public Clob getClob(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    String val = getString(columnIndex);
    return val == null ? null : new JdbcClob(val);
  }

  @Override
  public Clob getClob(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    String val = getString(columnLabel);
    return val == null ? null : new JdbcClob(val);
  }

  @Override
  public NClob getNClob(int columnIndex) throws SQLException {
    checkClosedAndValidRow();
    String val = getString(columnIndex);
    return val == null ? null : new JdbcClob(val);
  }

  @Override
  public NClob getNClob(String columnLabel) throws SQLException {
    checkClosedAndValidRow();
    String val = getString(columnLabel);
    return val == null ? null : new JdbcClob(val);
  }

  @SuppressWarnings("unchecked")
  private <T> T convertObject(Object o, Class<T> javaType, Type type) throws SQLException {
    return (T) JdbcTypeConverter.convert(o, type, javaType);
  }

  private Object convertObject(Object o, Map<String, Class<?>> map, Type type) throws SQLException {
    if (map == null)
      throw JdbcSqlExceptionFactory.of("Map may not be null", com.google.rpc.Code.INVALID_ARGUMENT);
    if (o == null) return null;
    Class<?> javaType = map.get(type.getCode().name());
    if (javaType == null) return o;
    return JdbcTypeConverter.convert(o, type, javaType);
  }
}
