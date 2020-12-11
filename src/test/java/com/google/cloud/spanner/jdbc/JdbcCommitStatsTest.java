/*
 * Copyright 2020 Google LLC
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

import com.google.cloud.spanner.CommitResponse;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.connection.AbstractMockServerTest;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JdbcCommitStatsTest extends AbstractMockServerTest {
  @Test
  public void testDefaultReturnCommitStats() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE RETURN_COMMIT_STATS")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getBoolean("RETURN_COMMIT_STATS")).isFalse();
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void testReturnCommitStatsInConnectionUrl() throws SQLException {
    try (java.sql.Connection connection =
        DriverManager.getConnection(
            String.format("jdbc:%s;returnCommitStats=true", getBaseUrl()))) {
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE RETURN_COMMIT_STATS")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getBoolean("RETURN_COMMIT_STATS")).isTrue();
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void testSetReturnCommitStats() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      connection.createStatement().execute("SET RETURN_COMMIT_STATS=true");
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE RETURN_COMMIT_STATS")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getBoolean("RETURN_COMMIT_STATS")).isTrue();
        assertThat(rs.next()).isFalse();
      }
      connection.createStatement().execute("SET RETURN_COMMIT_STATS=false");
      try (java.sql.ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE RETURN_COMMIT_STATS")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getBoolean("RETURN_COMMIT_STATS")).isFalse();
        assertThat(rs.next()).isFalse();
      }
    }
  }

  @Test
  public void testSetAndUseReturnCommitStats() throws SQLException {
    try (CloudSpannerJdbcConnection connection =
        createJdbcConnection().unwrap(CloudSpannerJdbcConnection.class)) {
      connection.setReturnCommitStats(true);
      connection.bufferedWrite(Mutation.newInsertBuilder("FOO").set("ID").to(1L).build());
      connection.commit();
      CommitResponse response = connection.getCommitResponse();
      assertThat(response).isNotNull();
      assertThat(response.getCommitStats()).isNotNull();
      assertThat(response.getCommitStats().getMutationCount()).isAtLeast(1L);
    }
  }

  @Test
  public void testSetAndUseReturnCommitStatsUsingSql() throws SQLException {
    try (java.sql.Connection connection = createJdbcConnection()) {
      connection.createStatement().execute("SET RETURN_COMMIT_STATS=true");
      // Use a Mutation as the mock server only returns a non-zero mutation count for mutations, and
      // not for DML statements.
      connection
          .unwrap(CloudSpannerJdbcConnection.class)
          .bufferedWrite(Mutation.newInsertBuilder("FOO").set("ID").to(1L).build());
      connection.commit();
      try (ResultSet rs =
          connection.createStatement().executeQuery("SHOW VARIABLE COMMIT_RESPONSE")) {
        assertThat(rs.next()).isTrue();
        assertThat(rs.getTimestamp("COMMIT_TIMESTAMP")).isNotNull();
        assertThat(rs.getLong("MUTATION_COUNT")).isAtLeast(1L);
        assertThat(rs.next()).isFalse();
      }
    }
  }
}
