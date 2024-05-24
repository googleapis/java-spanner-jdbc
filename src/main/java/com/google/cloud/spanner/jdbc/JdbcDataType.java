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
import com.google.cloud.spanner.Struct;
import com.google.cloud.spanner.Type;
import com.google.cloud.spanner.Type.Code;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Enum for mapping Cloud Spanner data types to Java classes and JDBC SQL {@link Types}. */
enum JdbcDataType {
  BOOL {
    @Override
    public int getSqlType() {
      return Types.BOOLEAN;
    }

    @Override
    public Class<Boolean> getJavaClass() {
      return Boolean.class;
    }

    @Override
    public Code getCode() {
      return Code.BOOL;
    }

    @Override
    public List<Boolean> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getBooleanList(columnIndex);
    }

    @Override
    public Type getSpannerType() {
      return Type.bool();
    }
  },
  BYTES {
    @Override
    public int getSqlType() {
      return Types.BINARY;
    }

    @Override
    public Class<byte[]> getJavaClass() {
      return byte[].class;
    }

    @Override
    public Code getCode() {
      return Code.BYTES;
    }

    @Override
    public List<byte[]> getArrayElements(ResultSet rs, int columnIndex) {
      return JdbcTypeConverter.toJavaByteArrays(rs.getBytesList(columnIndex));
    }

    @Override
    public Type getSpannerType() {
      return Type.bytes();
    }
  },
  DATE {
    @Override
    public int getSqlType() {
      return Types.DATE;
    }

    @Override
    public Class<Date> getJavaClass() {
      return Date.class;
    }

    @Override
    public Code getCode() {
      return Code.DATE;
    }

    @Override
    public List<Date> getArrayElements(ResultSet rs, int columnIndex) {
      return JdbcTypeConverter.toSqlDates(rs.getDateList(columnIndex));
    }

    @Override
    public Type getSpannerType() {
      return Type.date();
    }
  },
  FLOAT32 {
    @Override
    public int getSqlType() {
      return Types.REAL;
    }

    @Override
    public int getScale() {
      return 7;
    }

    @Override
    public int getPrecision() {
      return 7;
    }

    @Override
    public int getDefaultColumnDisplaySize() {
      return 7;
    }

    @Override
    public Class<Float> getJavaClass() {
      return Float.class;
    }

    @Override
    public Code getCode() {
      return Code.FLOAT32;
    }

    @Override
    public List<Float> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getFloatList(columnIndex);
    }

    @Override
    public Type getSpannerType() {
      return Type.float32();
    }
  },
  FLOAT64 {
    private final Set<Class<?>> classes = new HashSet<>(Arrays.asList(Float.class, Double.class));

    @Override
    public int getSqlType() {
      return Types.DOUBLE;
    }

    @Override
    public Class<Double> getJavaClass() {
      return Double.class;
    }

    @Override
    public Set<Class<?>> getSupportedJavaClasses() {
      return classes;
    }

    @Override
    public Code getCode() {
      return Code.FLOAT64;
    }

    @Override
    public List<Double> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getDoubleList(columnIndex);
    }

    @Override
    public Type getSpannerType() {
      return Type.float64();
    }
  },
  INT64 {
    private final Set<Class<?>> classes =
        new HashSet<>(Arrays.asList(Byte.class, Integer.class, Long.class));

    @Override
    public int getSqlType() {
      return Types.BIGINT;
    }

    @Override
    public Class<Long> getJavaClass() {
      return Long.class;
    }

    @Override
    public Set<Class<?>> getSupportedJavaClasses() {
      return classes;
    }

    @Override
    public Code getCode() {
      return Code.INT64;
    }

    @Override
    public List<Long> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getLongList(columnIndex);
    }

    @Override
    public Type getSpannerType() {
      return Type.int64();
    }
  },
  NUMERIC {
    @Override
    public int getSqlType() {
      return Types.NUMERIC;
    }

    @Override
    public Class<BigDecimal> getJavaClass() {
      return BigDecimal.class;
    }

    @Override
    public Code getCode() {
      return Code.NUMERIC;
    }

    @Override
    public List<BigDecimal> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getBigDecimalList(columnIndex);
    }

    @Override
    public Type getSpannerType() {
      return Type.numeric();
    }
  },
  PG_NUMERIC {
    @Override
    public int getSqlType() {
      return Types.NUMERIC;
    }

    @Override
    public Class<BigDecimal> getJavaClass() {
      return BigDecimal.class;
    }

    @Override
    public Code getCode() {
      return Code.PG_NUMERIC;
    }

    @Override
    public List<BigDecimal> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getValue(columnIndex).getNumericArray();
    }

    @Override
    public Type getSpannerType() {
      return Type.pgNumeric();
    }
  },
  STRING {
    @Override
    public int getSqlType() {
      return Types.NVARCHAR;
    }

    @Override
    public Class<String> getJavaClass() {
      return String.class;
    }

    @Override
    public Code getCode() {
      return Code.STRING;
    }

    @Override
    public List<String> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getStringList(columnIndex);
    }

    @Override
    public Type getSpannerType() {
      return Type.string();
    }
  },
  JSON {
    @Override
    public int getSqlType() {
      return JsonType.VENDOR_TYPE_NUMBER;
    }

    @Override
    public Class<String> getJavaClass() {
      return String.class;
    }

    @Override
    public Code getCode() {
      return Code.JSON;
    }

    @Override
    public List<String> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getJsonList(columnIndex);
    }

    @Override
    public Type getSpannerType() {
      return Type.json();
    }
  },
  PG_JSONB {
    @Override
    public int getSqlType() {
      return PgJsonbType.VENDOR_TYPE_NUMBER;
    }

    @Override
    public Class<String> getJavaClass() {
      return String.class;
    }

    @Override
    public Code getCode() {
      return Code.PG_JSONB;
    }

    @Override
    public List<String> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getPgJsonbList(columnIndex);
    }

    @Override
    public String getTypeName() {
      return "JSONB";
    }

    @Override
    public Type getSpannerType() {
      return Type.pgJsonb();
    }
  },
  TIMESTAMP {
    @Override
    public int getSqlType() {
      return Types.TIMESTAMP;
    }

    @Override
    public Class<Timestamp> getJavaClass() {
      return Timestamp.class;
    }

    @Override
    public Code getCode() {
      return Code.TIMESTAMP;
    }

    @Override
    public List<Timestamp> getArrayElements(ResultSet rs, int columnIndex) {
      return JdbcTypeConverter.toSqlTimestamps(rs.getTimestampList(columnIndex));
    }

    @Override
    public Type getSpannerType() {
      return Type.timestamp();
    }
  },
  STRUCT {
    @Override
    public int getSqlType() {
      return Types.STRUCT;
    }

    @Override
    public Class<Struct> getJavaClass() {
      return Struct.class;
    }

    @Override
    public Code getCode() {
      return Code.STRUCT;
    }

    @Override
    public List<Struct> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getStructList(columnIndex);
    }

    @Override
    public Type getSpannerType() {
      return Type.struct();
    }
  },
  PROTO {
    @Override
    public int getSqlType() {
      return ProtoMessageType.VENDOR_TYPE_NUMBER;
    }

    @Override
    public Class<byte[]> getJavaClass() {
      return byte[].class;
    }

    @Override
    public Code getCode() {
      return Code.PROTO;
    }

    @Override
    public List<byte[]> getArrayElements(ResultSet rs, int columnIndex) {
      return JdbcTypeConverter.toJavaByteArrays(rs.getBytesList(columnIndex));
    }

    @Override
    public Type getSpannerType() {
      return Type.bytes();
    }
  },
  ENUM {
    private final Set<Class<?>> classes = new HashSet<>(Arrays.asList(Integer.class, Long.class));

    @Override
    public int getSqlType() {
      return ProtoEnumType.VENDOR_TYPE_NUMBER;
    }

    @Override
    public Class<Long> getJavaClass() {
      return Long.class;
    }

    @Override
    public Set<Class<?>> getSupportedJavaClasses() {
      return classes;
    }

    @Override
    public Code getCode() {
      return Code.ENUM;
    }

    @Override
    public List<Long> getArrayElements(ResultSet rs, int columnIndex) {
      return rs.getLongList(columnIndex);
    }

    @Override
    public Type getSpannerType() {
      return Type.int64();
    }
  };

  public abstract int getSqlType();

  public abstract Code getCode();

  public abstract Type getSpannerType();

  // TODO: Implement and use this method for all types.
  public int getPrecision() {
    throw new UnsupportedOperationException();
  }

  // TODO: Implement and use this method for all types.
  public int getScale() {
    throw new UnsupportedOperationException();
  }

  // TODO: Implement and use this method for all types.
  public int getDefaultColumnDisplaySize() {
    throw new UnsupportedOperationException();
  }

  /**
   * @param rs the result set to look up the elements
   * @param columnIndex zero based column index
   * @return The corresponding array elements of the type in the given result set
   */
  public abstract List<?> getArrayElements(ResultSet rs, int columnIndex);

  public String getTypeName() {
    return name();
  }

  public abstract Class<?> getJavaClass();

  public Set<? extends Class<?>> getSupportedJavaClasses() {
    return Collections.singleton(getJavaClass());
  }

  public static JdbcDataType getType(Class<?> clazz) {
    for (JdbcDataType type : JdbcDataType.values()) {
      if (type.getSupportedJavaClasses().contains(clazz)) {
        return type;
      }
    }
    return null;
  }

  public static JdbcDataType getType(Code code) {
    for (JdbcDataType type : JdbcDataType.values()) {
      if (type.getCode() == code) {
        return type;
      }
    }
    return null;
  }
}
