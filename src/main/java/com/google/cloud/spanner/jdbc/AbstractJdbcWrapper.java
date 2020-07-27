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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.Wrapper;
import java.util.Calendar;

/** Base class for all Cloud Spanner JDBC classes that implement the {@link Wrapper} interface. */
abstract class AbstractJdbcWrapper implements Wrapper {
  static final String OTHER_NAME = "OTHER";

  /**
   * Extract {@link java.sql.Types} code from Spanner {@link Type}.
   *
   * @param type The Cloud Spanner type to convert. May not be <code>null</code>.
   */
  static int extractColumnType(Type type) {
    Preconditions.checkNotNull(type);
    if (type.equals(Type.bool())) return Types.BOOLEAN;
    if (type.equals(Type.bytes())) return Types.BINARY;
    if (type.equals(Type.date())) return Types.DATE;
    if (type.equals(Type.float64())) return Types.DOUBLE;
    if (type.equals(Type.int64())) return Types.BIGINT;
    if (type.equals(Type.numeric())) return Types.NUMERIC;
    if (type.equals(Type.string())) return Types.NVARCHAR;
    if (type.equals(Type.timestamp())) return Types.TIMESTAMP;
    if (type.getCode() == Code.ARRAY) return Types.ARRAY;
    return Types.OTHER;
  }

  /** Extract Spanner type name from {@link java.sql.Types} code. */
  static String getSpannerTypeName(int sqlType) {
    if (sqlType == Types.BOOLEAN) return Type.bool().getCode().name();
    if (sqlType == Types.BINARY) return Type.bytes().getCode().name();
    if (sqlType == Types.DATE) return Type.date().getCode().name();
    if (sqlType == Types.DOUBLE || sqlType == Types.FLOAT) return Type.float64().getCode().name();
    if (sqlType == Types.BIGINT
        || sqlType == Types.INTEGER
        || sqlType == Types.SMALLINT
        || sqlType == Types.TINYINT) return Type.int64().getCode().name();
    if (sqlType == Types.NUMERIC || sqlType == Types.DECIMAL)
      return Type.numeric().getCode().name();
    if (sqlType == Types.NVARCHAR) return Type.string().getCode().name();
    if (sqlType == Types.TIMESTAMP) return Type.timestamp().getCode().name();
    if (sqlType == Types.ARRAY) return Code.ARRAY.name();

    return OTHER_NAME;
  }

  /** Get corresponding Java class name from {@link java.sql.Types} code. */
  static String getClassName(int sqlType) {
    if (sqlType == Types.BOOLEAN) return Boolean.class.getName();
    if (sqlType == Types.BINARY) return Byte[].class.getName();
    if (sqlType == Types.DATE) return Date.class.getName();
    if (sqlType == Types.DOUBLE || sqlType == Types.FLOAT) return Double.class.getName();
    if (sqlType == Types.BIGINT
        || sqlType == Types.INTEGER
        || sqlType == Types.SMALLINT
        || sqlType == Types.TINYINT) return Long.class.getName();
    if (sqlType == Types.NUMERIC || sqlType == Types.DECIMAL) return BigDecimal.class.getName();
    if (sqlType == Types.NVARCHAR) return String.class.getName();
    if (sqlType == Types.TIMESTAMP) return Timestamp.class.getName();
    if (sqlType == Types.ARRAY) return Object.class.getName();

    return null;
  }

  /**
   * Get corresponding Java class name from Spanner {@link Type}.
   *
   * @param type The Cloud Spanner type to convert. May not be <code>null</code>.
   */
  static String getClassName(Type type) {
    Preconditions.checkNotNull(type);
    if (type == Type.bool()) return Boolean.class.getName();
    if (type == Type.bytes()) return byte[].class.getName();
    if (type == Type.date()) return Date.class.getName();
    if (type == Type.float64()) return Double.class.getName();
    if (type == Type.int64()) return Long.class.getName();
    if (type == Type.numeric()) return BigDecimal.class.getName();
    if (type == Type.string()) return String.class.getName();
    if (type == Type.timestamp()) return Timestamp.class.getName();
    if (type.getCode() == Code.ARRAY) {
      if (type.getArrayElementType() == Type.bool()) return Boolean[].class.getName();
      if (type.getArrayElementType() == Type.bytes()) return byte[][].class.getName();
      if (type.getArrayElementType() == Type.date()) return Date[].class.getName();
      if (type.getArrayElementType() == Type.float64()) return Double[].class.getName();
      if (type.getArrayElementType() == Type.int64()) return Long[].class.getName();
      if (type.getArrayElementType() == Type.numeric()) return BigDecimal[].class.getName();
      if (type.getArrayElementType() == Type.string()) return String[].class.getName();
      if (type.getArrayElementType() == Type.timestamp()) return Timestamp[].class.getName();
    }
    return null;
  }

  /** Standard error message for out-of-range values. */
  private static final String OUT_OF_RANGE_MSG = "Value out of range for %s: %s";

  /** Cast value and throw {@link SQLException} if out-of-range. */
  static byte checkedCastToByte(long val) throws SQLException {
    if (val > Byte.MAX_VALUE || val < Byte.MIN_VALUE) {
      throw JdbcSqlExceptionFactory.of(
          String.format(OUT_OF_RANGE_MSG, "byte", val), com.google.rpc.Code.OUT_OF_RANGE);
    }
    return (byte) val;
  }

  /** Cast value and throw {@link SQLException} if out-of-range. */
  static byte checkedCastToByte(BigDecimal val) throws SQLException {
    try {
      return val.byteValueExact();
    } catch (ArithmeticException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format(OUT_OF_RANGE_MSG, "byte", val), com.google.rpc.Code.OUT_OF_RANGE);
    }
  }

  /** Cast value and throw {@link SQLException} if out-of-range. */
  static short checkedCastToShort(long val) throws SQLException {
    if (val > Short.MAX_VALUE || val < Short.MIN_VALUE) {
      throw JdbcSqlExceptionFactory.of(
          String.format(OUT_OF_RANGE_MSG, "short", val), com.google.rpc.Code.OUT_OF_RANGE);
    }
    return (short) val;
  }

  /** Cast value and throw {@link SQLException} if out-of-range. */
  static short checkedCastToShort(BigDecimal val) throws SQLException {
    try {
      return val.shortValueExact();
    } catch (ArithmeticException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format(OUT_OF_RANGE_MSG, "short", val), com.google.rpc.Code.OUT_OF_RANGE);
    }
  }

  /** Cast value and throw {@link SQLException} if out-of-range. */
  static int checkedCastToInt(long val) throws SQLException {
    if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE) {
      throw JdbcSqlExceptionFactory.of(
          String.format(OUT_OF_RANGE_MSG, "int", val), com.google.rpc.Code.OUT_OF_RANGE);
    }
    return (int) val;
  }

  /** Cast value and throw {@link SQLException} if out-of-range. */
  static int checkedCastToInt(BigDecimal val) throws SQLException {
    try {
      return val.intValueExact();
    } catch (ArithmeticException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format(OUT_OF_RANGE_MSG, "int", val), com.google.rpc.Code.OUT_OF_RANGE);
    }
  }

  /** Cast value and throw {@link SQLException} if out-of-range. */
  static float checkedCastToFloat(double val) throws SQLException {
    if (val > Float.MAX_VALUE || val < -Float.MAX_VALUE) {
      throw JdbcSqlExceptionFactory.of(
          String.format(OUT_OF_RANGE_MSG, "float", val), com.google.rpc.Code.OUT_OF_RANGE);
    }
    return (float) val;
  }

  /**
   * Parses the given string value as a long. Throws {@link SQLException} if the string is not a
   * valid long value.
   */
  static long parseLong(String val) throws SQLException {
    Preconditions.checkNotNull(val);
    try {
      return Long.valueOf(val);
    } catch (NumberFormatException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format("%s is not a valid number", val), com.google.rpc.Code.INVALID_ARGUMENT, e);
    }
  }

  /** Cast value and throw {@link SQLException} if out-of-range. */
  static BigInteger checkedCastToBigInteger(BigDecimal val) throws SQLException {
    try {
      return val.toBigIntegerExact();
    } catch (ArithmeticException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format(OUT_OF_RANGE_MSG, "BigInteger", val), com.google.rpc.Code.OUT_OF_RANGE);
    }
  }

  /** Cast value and throw {@link SQLException} if out-of-range. */
  static long checkedCastToLong(BigDecimal val) throws SQLException {
    try {
      return val.longValueExact();
    } catch (ArithmeticException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format(OUT_OF_RANGE_MSG, "long", val), com.google.rpc.Code.OUT_OF_RANGE);
    }
  }

  /**
   * Parses the given string value as a double. Throws {@link SQLException} if the string is not a
   * valid double value.
   */
  static double parseDouble(String val) throws SQLException {
    Preconditions.checkNotNull(val);
    try {
      return Double.valueOf(val);
    } catch (NumberFormatException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format("%s is not a valid number", val), com.google.rpc.Code.INVALID_ARGUMENT, e);
    }
  }

  /**
   * Parses the given string value as a {@link Date} value. Throws {@link SQLException} if the
   * string is not a valid {@link Date} value.
   */
  static Date parseDate(String val) throws SQLException {
    Preconditions.checkNotNull(val);
    try {
      return JdbcTypeConverter.toSqlDate(com.google.cloud.Date.parseDate(val));
    } catch (IllegalArgumentException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format("%s is not a valid date", val), com.google.rpc.Code.INVALID_ARGUMENT, e);
    }
  }

  /**
   * Parses the given string value as a {@link Date} value in the timezone of the given {@link
   * Calendar}. Throws {@link SQLException} if the string is not a valid {@link Date} value.
   */
  static Date parseDate(String val, Calendar cal) throws SQLException {
    Preconditions.checkNotNull(val);
    Preconditions.checkNotNull(cal);
    try {
      return JdbcTypeConverter.toSqlDate(com.google.cloud.Date.parseDate(val), cal);
    } catch (IllegalArgumentException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format("%s is not a valid date", val), com.google.rpc.Code.INVALID_ARGUMENT, e);
    }
  }

  /**
   * Parses the given string value as a {@link Time} value. Throws {@link SQLException} if the
   * string is not a valid {@link Time} value.
   */
  static Time parseTime(String val) throws SQLException {
    Preconditions.checkNotNull(val);
    try {
      return Time.valueOf(val);
    } catch (IllegalArgumentException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format("%s is not a valid time", val), com.google.rpc.Code.INVALID_ARGUMENT, e);
    }
  }

  /**
   * Parses the given string value as a {@link Time} value in the timezone of the given {@link
   * Calendar}. Throws {@link SQLException} if the string is not a valid {@link Time} value.
   */
  static Time parseTime(String val, Calendar cal) throws SQLException {
    Preconditions.checkNotNull(val);
    Preconditions.checkNotNull(cal);
    try {
      return JdbcTypeConverter.parseSqlTime(val, cal);
    } catch (IllegalArgumentException e) {
      throw JdbcSqlExceptionFactory.of(
          String.format("%s is not a valid time", val), com.google.rpc.Code.INVALID_ARGUMENT, e);
    }
  }

  /**
   * Parses the given string value as a {@link Timestamp} value. Throws {@link SQLException} if the
   * string is not a valid {@link Timestamp} value.
   */
  static Timestamp parseTimestamp(String val) throws SQLException {
    Preconditions.checkNotNull(val);
    try {
      return JdbcTypeConverter.toSqlTimestamp(com.google.cloud.Timestamp.parseTimestamp(val));
    } catch (Exception e) {
      throw JdbcSqlExceptionFactory.of(
          String.format("%s is not a valid timestamp", val),
          com.google.rpc.Code.INVALID_ARGUMENT,
          e);
    }
  }

  /**
   * Parses the given string value as a {@link Timestamp} value in the timezone of the given {@link
   * Calendar}. Throws {@link SQLException} if the string is not a valid {@link Timestamp} value.
   */
  static Timestamp parseTimestamp(String val, Calendar cal) throws SQLException {
    Preconditions.checkNotNull(val);
    Preconditions.checkNotNull(cal);
    try {
      return JdbcTypeConverter.setTimestampInCalendar(
          com.google.cloud.Timestamp.parseTimestamp(val).toSqlTimestamp(), cal);
    } catch (Exception e) {
      throw JdbcSqlExceptionFactory.of(
          String.format("%s is not a valid timestamp", val),
          com.google.rpc.Code.INVALID_ARGUMENT,
          e);
    }
  }

  /** Should return true if this object has been closed */
  public abstract boolean isClosed() throws SQLException;

  /** Throws a {@link SQLException} if this object is closed */
  void checkClosed() throws SQLException {
    if (isClosed()) {
      throw JdbcSqlExceptionFactory.of(
          "This " + getClass().getName() + " has been closed",
          com.google.rpc.Code.FAILED_PRECONDITION);
    }
  }

  /**
   * Throws a {@link SQLException} if this object is closed and otherwise a {@link
   * SQLFeatureNotSupportedException} with the given message
   */
  <T> T checkClosedAndThrowUnsupported(String message) throws SQLException {
    checkClosed();
    throw JdbcSqlExceptionFactory.unsupported(message);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return iface != null && iface.isAssignableFrom(getClass());
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (isWrapperFor(iface)) {
      return iface.cast(this);
    }
    throw JdbcSqlExceptionFactory.of(
        "Cannot unwrap to " + iface.getName(), com.google.rpc.Code.INVALID_ARGUMENT);
  }
}
