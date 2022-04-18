/*
 * Copyright 2022 Google LLC
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

package com.google.cloud.spanner.jdbc.it;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.google.cloud.spanner.Dialect;
import com.google.cloud.spanner.ParallelIntegrationTest;
import com.google.cloud.spanner.jdbc.ITAbstractJdbcTest;
import com.google.cloud.spanner.jdbc.JdbcSqlException;
import com.google.rpc.Code;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/** Test executing role statements through JDBC. */
@RunWith(Parameterized.class)
@Category(ParallelIntegrationTest.class)
public class ITJdbcRolesTest extends ITAbstractJdbcTest {
  @Parameterized.Parameters(name = "Dialect = {0}")
  public static List<DialectTestParameter> data() {
    List<DialectTestParameter> params = new ArrayList<>();
    //        params.add(new DialectTestParameter(Dialect.GOOGLE_STANDARD_SQL));
    params.add(new DialectTestParameter(Dialect.POSTGRESQL));
    return params;
  }

  @Parameterized.Parameter public DialectTestParameter dialect;

  @Override
  public Dialect getDialect() {
    return dialect.dialect;
  }

  @Test
  public void testRoleDdlStatements() throws SQLException {
    final String creatorRoleParent = "parent";
    String createTableFoo1 =
        "CREATE TABLE FOO1 (ID INT64 NOT NULL, NAME STRING(100)) PRIMARY KEY (ID)";
    final String createRole = String.format("CREATE ROLE %s", creatorRoleParent);
    String grantPrivilegeToRole =
        String.format("GRANT SELECT ON TABLE FOO1 TO ROLE %s", creatorRoleParent);
    final String selectFromFoo1 = "SELECT count(*) FROM FOO1";
    if (dialect.dialect == Dialect.POSTGRESQL) {
      createTableFoo1 = "CREATE TABLE FOO1 (ID BIGINT PRIMARY KEY, NAME VARCHAR (100))";
      grantPrivilegeToRole = String.format("GRANT SELECT ON TABLE FOO1 TO %s", creatorRoleParent);
    }
    // Create Table and role.
    try (Connection connection = createConnection(getDialect())) {
      try (Statement statement = connection.createStatement()) {
        statement.addBatch(createTableFoo1);
        statement.addBatch(createRole);
        int[] updateCounts = statement.executeBatch();
        assertThat(updateCounts)
            .asList()
            .containsExactly(Statement.SUCCESS_NO_INFO, Statement.SUCCESS_NO_INFO);
      }
    }

    // Grant select privilege to role.
    try (Connection connection = createConnection(getDialect())) {
      try (Statement statement = connection.createStatement()) {
        statement.execute(grantPrivilegeToRole);
      }
    }

    // Execute select query with creator role set.
    try (Connection connection = createConnection(getDialect(), creatorRoleParent)) {
      try (ResultSet rs = connection.createStatement().executeQuery(selectFromFoo1)) {
        assertThat(rs.next()).isTrue();
      }
    }

    final String revokePrivilegeFromRole =
        String.format("REVOKE SELECT ON TABLE FOO1 FROM ROLE %s", creatorRoleParent);

    // Revoke select privilege from role.
    try (Connection connection = createConnection(getDialect())) {
      try (Statement statement = connection.createStatement()) {
        statement.execute(revokePrivilegeFromRole);
      }
    }

    // Execute select query with creator role set. Expect permission denied error.
    try (Connection connection = createConnection(getDialect(), creatorRoleParent)) {
      try (ResultSet rs = connection.createStatement().executeQuery(selectFromFoo1)) {
        fail("Missing expected exception.");
      } catch (SQLException e) {
        assertThat((Throwable) e).isInstanceOf(JdbcSqlException.class);
        JdbcSqlException je = (JdbcSqlException) e;
        assertThat(je.getCode()).isEqualTo(Code.PERMISSION_DENIED);
      }
    }
  }
}
