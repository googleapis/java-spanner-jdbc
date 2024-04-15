/*
 * Copyright 2024 Google LLC
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

import com.google.cloud.spanner.connection.AbstractMockServerTest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StatementResourcesTest extends AbstractMockServerTest {

  private String createUrl() {
    return String.format(
        "jdbc:cloudspanner://localhost:%d/projects/%s/instances/%s/databases/%s"
            + "?usePlainText=true;minSessions=2;maxSessions=2",
        getPort(), "proj", "inst", "db");
  }

  @Test
  public void testQueryWithExecuteAndNoRead() throws SQLException {
    try (Connection connection = DriverManager.getConnection(createUrl())) {
      try (Statement statement = connection.createStatement()) {
        statement.execute("SELECT 1");
        statement.execute("SELECT 1");
        statement.execute("SELECT 1");
      }
    }
  }

}
