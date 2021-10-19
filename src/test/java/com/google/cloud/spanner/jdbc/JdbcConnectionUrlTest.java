/*
 * Copyright 2021 Google LLC
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
import static org.junit.Assert.assertTrue;

import com.google.cloud.spanner.connection.AbstractMockServerTest;
import com.google.spanner.v1.BatchCreateSessionsRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class JdbcConnectionUrlTest {

  public static class ConnectionMinSessionsTest extends AbstractMockServerTest {
    @AfterClass
    public static void reset() {
      mockSpanner.reset();
    }

    protected String getBaseUrl() {
      return super.getBaseUrl() + ";minSessions=1";
    }

    @Test
    public void testMinSessions() throws InterruptedException, TimeoutException, SQLException {
      try (Connection connection = createJdbcConnection()) {
        mockSpanner.waitForRequestsToContain(
            input -> input instanceof BatchCreateSessionsRequest
                && ((BatchCreateSessionsRequest) input).getSessionCount() == 1,
            5000L);
      }
    }
  }

  public static class ConnectionMaxSessionsTest extends AbstractMockServerTest {

    @AfterClass
    public static void reset() {
      mockSpanner.reset();
    }

    protected String getBaseUrl() {
      return super.getBaseUrl() + ";maxSessions=1";
    }

    @Test
    public void testMaxSessions()
        throws InterruptedException, TimeoutException, ExecutionException, SQLException {
      ExecutorService executor1 = Executors.newSingleThreadExecutor();
      ExecutorService executor2 = Executors.newSingleThreadExecutor();

      try (Connection connection1 = createJdbcConnection();
          Connection connection2 = createJdbcConnection()) {
        final CountDownLatch latch = new CountDownLatch(1);
        Future<Void> fut1 =
            executor1.submit(
                () -> {
                  assertTrue(latch.await(5L, TimeUnit.SECONDS));
                  connection1.createStatement().executeUpdate(INSERT_STATEMENT.getSql());
                  connection1.commit();
                  return null;
                });
        Future<Void> fut2 =
            executor2.submit(
                () -> {
                  latch.countDown();
                  connection2.createStatement().executeUpdate(INSERT_STATEMENT.getSql());
                  connection2.commit();
                  return null;
                });
        // Wait until both finishes.
        fut1.get(5L, TimeUnit.SECONDS);
        fut2.get(5L, TimeUnit.SECONDS);
      } finally {
        executor1.shutdown();
        executor2.shutdown();
      }
      assertThat(mockSpanner.numSessionsCreated()).isEqualTo(1);
    }
  }
}
