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

package com.google.cloud.spanner.jdbc.it;

import static org.junit.Assert.assertTrue;

import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.ParallelIntegrationTest;
import com.google.cloud.spanner.connection.ConnectionOptions;
import com.google.cloud.spanner.connection.SqlScriptVerifier;
import com.google.cloud.spanner.jdbc.CloudSpannerJdbcConnection;
import com.google.cloud.spanner.jdbc.ITAbstractJdbcTest;
import com.google.cloud.spanner.jdbc.JdbcSqlScriptVerifier;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** This test class runs a SQL script for testing a connection in read-only mode. */
@Category(ParallelIntegrationTest.class)
@RunWith(JUnit4.class)
public class ITJdbcReadOnlyTest extends ITAbstractJdbcTest {
  private static final long TEST_ROWS_COUNT = 1000L;

  @Override
  protected void appendConnectionUri(StringBuilder url) {
    url.append(";readOnly=true");
  }

  @Before
  public void createTestTables() throws Exception {
    try (CloudSpannerJdbcConnection connection = createConnection()) {
      if (!(tableExists(connection, "NUMBERS") && tableExists(connection, "PRIME_NUMBERS"))) {
        // create tables
        JdbcSqlScriptVerifier verifier = new JdbcSqlScriptVerifier(new ITJdbcConnectionProvider());
        verifier.verifyStatementsInFile(
            "ITReadOnlySpannerTest_CreateTables.sql", SqlScriptVerifier.class, false);

        // fill tables with data
        connection.setAutoCommit(false);
        connection.setReadOnly(false);
        for (long number = 1L; number <= TEST_ROWS_COUNT; number++) {
          connection.bufferedWrite(
              Mutation.newInsertBuilder("NUMBERS")
                  .set("number")
                  .to(number)
                  .set("name")
                  .to(Long.toBinaryString(number))
                  .build());
        }
        for (long number = 1L; number <= TEST_ROWS_COUNT; number++) {
          if (BigInteger.valueOf(number).isProbablePrime(Integer.MAX_VALUE)) {
            connection.bufferedWrite(
                Mutation.newInsertBuilder("PRIME_NUMBERS")
                    .set("prime_number")
                    .to(number)
                    .set("binary_representation")
                    .to(Long.toBinaryString(number))
                    .build());
          }
        }
        connection.commit();
      }
    }
  }

  @After
  public void closeSpanner() {
    ConnectionOptions.closeSpanner();
  }

  @Test
  public void testSqlScript() throws Exception {
    // Wait 100ms to ensure that staleness tests in the script succeed.
    Thread.sleep(100L);
    JdbcSqlScriptVerifier verifier = new JdbcSqlScriptVerifier(new ITJdbcConnectionProvider());
    verifier.verifyStatementsInFile("ITReadOnlySpannerTest.sql", SqlScriptVerifier.class, false);
  }

  @Test
  public void testMultipleOpenResultSets() throws InterruptedException, SQLException {
    try (Connection connection = createConnection()) {
      final java.sql.ResultSet rs1 =
          connection.createStatement().executeQuery("SELECT * FROM PRIME_NUMBERS");
      final java.sql.ResultSet rs2 =
          connection.createStatement().executeQuery("SELECT * FROM NUMBERS");
      ExecutorService exec = Executors.newFixedThreadPool(2);
      exec.submit(
          new Runnable() {
            @Override
            public void run() {
              try {
                while (rs1.next()) {}
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
      exec.submit(
          new Runnable() {
            @Override
            public void run() {
              try {
                while (rs2.next()) {}
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
          });
      exec.shutdown();
      assertTrue(exec.awaitTermination(1000L, TimeUnit.SECONDS));
      rs1.close();
      rs2.close();
    }
  }
}
