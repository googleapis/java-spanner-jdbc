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

package com.google.cloud.spanner.sample;

import com.google.cloud.spanner.jdbc.JdbcSqlException;
import com.google.rpc.Code;
import java.util.Objects;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

@Configuration
public class JdbcConfiguration {

  /** Returns true if the current database is a Cloud Spanner PostgreSQL database. */
  public static boolean isCloudSpannerPG(JdbcOperations operations) {
    try {
      Long value =
          operations.queryForObject(
              "select 1 "
                  + "from information_schema.database_options "
                  + "where schema_name='public' "
                  + "and option_name='database_dialect' "
                  + "and option_value='POSTGRESQL'",
              Long.class);
      // Shouldn't really be anything else than 1 if the query succeeded, but this avoids complaints
      // from the compiler.
      if (Objects.equals(1L, value)) {
        return true;
      }
    } catch (IncorrectResultSizeDataAccessException exception) {
      // This indicates that it is a valid Cloud Spanner database, but not one that uses the
      // PostgreSQL dialect.
      throw new RuntimeException(
          "The selected Cloud Spanner database does not use the PostgreSQL dialect");
    } catch (DataAccessException exception) {
      if (exception.getCause() instanceof JdbcSqlException) {
        JdbcSqlException jdbcSqlException = (JdbcSqlException) exception.getCause();
        if (jdbcSqlException.getCode() == Code.PERMISSION_DENIED
            || jdbcSqlException.getCode() == Code.NOT_FOUND) {
          throw new RuntimeException(
              "Failed to get the dialect of the Cloud Spanner database. "
                  + "Please check that the selected database exists and that you have permission to access it. "
                  + "Cause: "
                  + exception.getCause().getMessage(),
              exception.getCause());
        }
      }
      // ignore and fall through
    } catch (Throwable exception) {
      // ignore and fall through
    }
    return false;
  }
}
