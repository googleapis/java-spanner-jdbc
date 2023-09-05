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

import java.util.Objects;
import javax.annotation.Nonnull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.PostgresDialect;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@Configuration
public class JdbcConfiguration extends AbstractJdbcConfiguration {

  /** Override the dialect auto-detection, so it also returns PostgreSQL for Cloud Spanner. */
  @Override
  public Dialect jdbcDialect(@Nonnull NamedParameterJdbcOperations operations) {
    if (isCloudSpannerPG(operations.getJdbcOperations())) {
      return PostgresDialect.INSTANCE;
    }
    return super.jdbcDialect(operations);
  }

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
    } catch (Throwable ignore) {
      // ignore and fall through
    }
    return false;
  }
}
