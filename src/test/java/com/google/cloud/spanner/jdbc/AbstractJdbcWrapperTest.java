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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.google.cloud.Timestamp;
import com.google.rpc.Code;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AbstractJdbcWrapperTest {
  /** Create a concrete sub class to use for testing. */
  private static class TestWrapper extends AbstractJdbcWrapper {
    @Override
    public boolean isClosed() {
      return false;
    }
  }

  /** Add a sub class for the test class for testing wrapping. */
  private static class SubTestWrapper extends TestWrapper {}

  @Test
  public void testIsWrapperFor() {
    TestWrapper subject = new TestWrapper();
    assertThat(subject.isWrapperFor(TestWrapper.class)).isTrue();
    assertThat(subject.isWrapperFor(SubTestWrapper.class)).isFalse();
    assertThat(subject.isWrapperFor(Object.class)).isTrue();
    assertThat(subject.isWrapperFor(getClass())).isFalse();

    subject = new SubTestWrapper();
    assertThat(subject.isWrapperFor(TestWrapper.class)).isTrue();
    assertThat(subject.isWrapperFor(SubTestWrapper.class)).isTrue();
    assertThat(subject.isWrapperFor(Object.class)).isTrue();
    assertThat(subject.isWrapperFor(getClass())).isFalse();
  }

  @Test
  public void testUnwrap() {
    TestWrapper subject = new TestWrapper();
    assertThat(unwrapSucceeds(subject, TestWrapper.class)).isTrue();
    assertThat(unwrapSucceeds(subject, SubTestWrapper.class)).isFalse();
    assertThat(unwrapSucceeds(subject, Object.class)).isTrue();
    assertThat(unwrapSucceeds(subject, getClass())).isFalse();
  }

  private interface CheckedCastChecker<V> {
    boolean cast(V val);
  }

  private static final class CheckedCastToByteChecker implements CheckedCastChecker<Long> {
    @Override
    public boolean cast(Long val) {
      try {
        AbstractJdbcWrapper.checkedCastToByte(val);
        return true;
      } catch (SQLException e) {
        return false;
      }
    }
  }

  @Test
  public void testCheckedCastToByte() {
    CheckedCastToByteChecker checker = new CheckedCastToByteChecker();
    assertThat(checker.cast(0L)).isTrue();
    assertThat(checker.cast(1L)).isTrue();
    assertThat(checker.cast(Long.valueOf(Byte.MAX_VALUE))).isTrue();
    assertThat(checker.cast(Long.valueOf(Byte.MAX_VALUE) + 1L)).isFalse();
    assertThat(checker.cast(Long.MAX_VALUE)).isFalse();
    assertThat(checker.cast(-1L)).isTrue();
    assertThat(checker.cast(Long.valueOf(Byte.MIN_VALUE))).isTrue();
    assertThat(checker.cast(Long.valueOf(Byte.MIN_VALUE) - 1L)).isFalse();
    assertThat(checker.cast(Long.MIN_VALUE)).isFalse();
  }

  private static final class CheckedCastToShortChecker implements CheckedCastChecker<Long> {
    @Override
    public boolean cast(Long val) {
      try {
        AbstractJdbcWrapper.checkedCastToShort(val);
        return true;
      } catch (SQLException e) {
        return false;
      }
    }
  }

  @Test
  public void testCheckedCastToShort() {
    CheckedCastToShortChecker checker = new CheckedCastToShortChecker();
    assertThat(checker.cast(0L)).isTrue();
    assertThat(checker.cast(1L)).isTrue();
    assertThat(checker.cast(Long.valueOf(Short.MAX_VALUE))).isTrue();
    assertThat(checker.cast(Long.valueOf(Short.MAX_VALUE) + 1L)).isFalse();
    assertThat(checker.cast(Long.MAX_VALUE)).isFalse();
    assertThat(checker.cast(-1L)).isTrue();
    assertThat(checker.cast(Long.valueOf(Short.MIN_VALUE))).isTrue();
    assertThat(checker.cast(Long.valueOf(Short.MIN_VALUE) - 1L)).isFalse();
    assertThat(checker.cast(Long.MIN_VALUE)).isFalse();
  }

  private static final class CheckedCastToIntChecker implements CheckedCastChecker<Long> {
    @Override
    public boolean cast(Long val) {
      try {
        AbstractJdbcWrapper.checkedCastToInt(val);
        return true;
      } catch (SQLException e) {
        return false;
      }
    }
  }

  @Test
  public void testCheckedCastToInt() {
    CheckedCastToIntChecker checker = new CheckedCastToIntChecker();
    assertThat(checker.cast(0L)).isTrue();
    assertThat(checker.cast(1L)).isTrue();
    assertThat(checker.cast(Long.valueOf(Integer.MAX_VALUE))).isTrue();
    assertThat(checker.cast(Long.valueOf(Integer.MAX_VALUE) + 1L)).isFalse();
    assertThat(checker.cast(Long.MAX_VALUE)).isFalse();
    assertThat(checker.cast(-1L)).isTrue();
    assertThat(checker.cast(Long.valueOf(Integer.MIN_VALUE))).isTrue();
    assertThat(checker.cast(Long.valueOf(Integer.MIN_VALUE) - 1L)).isFalse();
    assertThat(checker.cast(Long.MIN_VALUE)).isFalse();
  }

  private static final class CheckedCastToFloatChecker implements CheckedCastChecker<Double> {
    @Override
    public boolean cast(Double val) {
      try {
        AbstractJdbcWrapper.checkedCastToFloat(val);
        return true;
      } catch (SQLException e) {
        return false;
      }
    }
  }

  @Test
  public void testCheckedCastToFloat() {
    CheckedCastToFloatChecker checker = new CheckedCastToFloatChecker();
    assertThat(checker.cast(0D)).isTrue();
    assertThat(checker.cast(1D)).isTrue();
    assertThat(checker.cast(Double.valueOf(Float.MAX_VALUE))).isTrue();
    assertThat(checker.cast(Double.valueOf(Float.MAX_VALUE) * 2.0D)).isFalse();
    assertThat(checker.cast(Double.MAX_VALUE)).isFalse();
    assertThat(checker.cast(-1D)).isTrue();
    assertThat(checker.cast(Double.valueOf(Float.MIN_VALUE))).isTrue();
    assertThat(checker.cast(Double.valueOf(-Float.MAX_VALUE * 2))).isFalse();
    assertThat(checker.cast(-Double.MAX_VALUE)).isFalse();
  }

  private boolean unwrapSucceeds(AbstractJdbcWrapper subject, Class<?> iface) {
    try {
      subject.unwrap(iface);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  @Test
  public void testParseDouble() throws SQLException {
    assertThat(AbstractJdbcWrapper.parseDouble("3.14")).isEqualTo(3.14D);
    try {
      AbstractJdbcWrapper.parseDouble("not a number");
      fail("missing expected SQLException");
    } catch (SQLException e) {
      assertThat((Exception) e).isInstanceOf(JdbcSqlException.class);
      assertThat(((JdbcSqlException) e).getCode()).isEqualTo(Code.INVALID_ARGUMENT);
    }
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testParseDate() throws SQLException {
    assertThat(AbstractJdbcWrapper.parseDate("2020-06-01")).isEqualTo(new Date(2020 - 1900, 5, 1));
    try {
      AbstractJdbcWrapper.parseDate("01-06-2020");
      fail("missing expected SQLException");
    } catch (SQLException e) {
      assertThat((Exception) e).isInstanceOf(JdbcSqlException.class);
      assertThat(((JdbcSqlException) e).getCode()).isEqualTo(Code.INVALID_ARGUMENT);
    }
  }

  @Test
  public void testParseDateWithCalendar() throws SQLException {
    assertThat(
            AbstractJdbcWrapper.parseDate(
                "2020-06-01", Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"))))
        .isEqualTo(
            new Date(
                Timestamp.parseTimestamp("2020-06-01T00:00:00-07:00").toSqlTimestamp().getTime()));
    assertThat(
            AbstractJdbcWrapper.parseDate(
                "2020-06-01", Calendar.getInstance(TimeZone.getTimeZone("Europe/Amsterdam"))))
        .isEqualTo(
            new Date(
                Timestamp.parseTimestamp("2020-06-01T00:00:00+02:00").toSqlTimestamp().getTime()));
    try {
      AbstractJdbcWrapper.parseDate("01-06-2020", Calendar.getInstance());
      fail("missing expected SQLException");
    } catch (SQLException e) {
      assertThat((Exception) e).isInstanceOf(JdbcSqlException.class);
      assertThat(((JdbcSqlException) e).getCode()).isEqualTo(Code.INVALID_ARGUMENT);
    }
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testParseTime() throws SQLException {
    assertThat(AbstractJdbcWrapper.parseTime("10:31:05")).isEqualTo(new Time(10, 31, 5));
    try {
      AbstractJdbcWrapper.parseTime("10.31.05");
      fail("missing expected SQLException");
    } catch (SQLException e) {
      assertThat((Exception) e).isInstanceOf(JdbcSqlException.class);
      assertThat(((JdbcSqlException) e).getCode()).isEqualTo(Code.INVALID_ARGUMENT);
    }
  }

  @Test
  public void testParseTimeWithCalendar() throws SQLException {
    assertThat(
            AbstractJdbcWrapper.parseTime(
                "10:31:05", Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"))))
        .isEqualTo(
            new Time(
                Timestamp.parseTimestamp("1970-01-01T10:31:05-08:00").toSqlTimestamp().getTime()));
    assertThat(
            AbstractJdbcWrapper.parseTime(
                "10:31:05", Calendar.getInstance(TimeZone.getTimeZone("Pacific/Auckland"))))
        .isEqualTo(
            new Time(
                Timestamp.parseTimestamp("1970-01-01T10:31:05+12:00").toSqlTimestamp().getTime()));
    try {
      AbstractJdbcWrapper.parseTime("10.31.05", Calendar.getInstance());
      fail("missing expected SQLException");
    } catch (SQLException e) {
      assertThat((Exception) e).isInstanceOf(JdbcSqlException.class);
      assertThat(((JdbcSqlException) e).getCode()).isEqualTo(Code.INVALID_ARGUMENT);
    }
  }

  @Test
  public void testParseTimestamp() throws SQLException {
    assertThat(AbstractJdbcWrapper.parseTimestamp("2020-06-01T10:31:05Z"))
        .isEqualTo(Timestamp.parseTimestamp("2020-06-01T10:31:05Z").toSqlTimestamp());
    assertThat(AbstractJdbcWrapper.parseTimestamp("2020-06-01T10:31:05.123Z"))
        .isEqualTo(Timestamp.parseTimestamp("2020-06-01T10:31:05.123Z").toSqlTimestamp());
    assertThat(AbstractJdbcWrapper.parseTimestamp("2020-06-01T10:31Z"))
        .isEqualTo(Timestamp.parseTimestamp("2020-06-01T10:31:00Z").toSqlTimestamp());
    assertThat(AbstractJdbcWrapper.parseTimestamp("2020-06-01T10:31"))
        .isEqualTo(Timestamp.parseTimestamp("2020-06-01T10:31:00Z").toSqlTimestamp());
    assertThat(AbstractJdbcWrapper.parseTimestamp("1970-01-01T00:00:00Z"))
        .isEqualTo(Timestamp.ofTimeMicroseconds(0L).toSqlTimestamp());
    try {
      AbstractJdbcWrapper.parseTimestamp("2020-06-01T10");
      fail("missing expected SQLException");
    } catch (SQLException e) {
      assertThat((Exception) e).isInstanceOf(JdbcSqlException.class);
      assertThat(((JdbcSqlException) e).getCode()).isEqualTo(Code.INVALID_ARGUMENT);
    }
  }

  @Test
  public void testParseTimestampWithCalendar() throws SQLException {
    assertThat(
            AbstractJdbcWrapper.parseTimestamp(
                "2020-02-01T10:31:05Z",
                Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"))))
        .isEqualTo(Timestamp.parseTimestamp("2020-02-01T10:31:05-08:00").toSqlTimestamp());
    assertThat(
            AbstractJdbcWrapper.parseTimestamp(
                "2020-06-01T10:31:05Z",
                Calendar.getInstance(TimeZone.getTimeZone("Europe/Amsterdam"))))
        .isEqualTo(Timestamp.parseTimestamp("2020-06-01T10:31:05+02:00").toSqlTimestamp());
    try {
      AbstractJdbcWrapper.parseTimestamp(
          "2020-06-01T10", Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles")));
      fail("missing expected SQLException");
    } catch (SQLException e) {
      assertThat((Exception) e).isInstanceOf(JdbcSqlException.class);
      assertThat(((JdbcSqlException) e).getCode()).isEqualTo(Code.INVALID_ARGUMENT);
    }
  }
}
