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

package com.google.cloud.spanner.jdbc.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ResourceExhaustedException;
import com.google.cloud.opentelemetry.trace.TraceConfiguration;
import com.google.cloud.opentelemetry.trace.TraceExporter;
import com.google.cloud.spanner.Database;
import com.google.cloud.spanner.ParallelIntegrationTest;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.SpannerOptionsHelper;
import com.google.cloud.spanner.jdbc.JdbcDriver;
import com.google.cloud.spanner.testing.EmulatorSpannerHelper;
import com.google.cloud.trace.v1.TraceServiceClient;
import com.google.cloud.trace.v1.TraceServiceClient.ListTracesPagedResponse;
import com.google.cloud.trace.v1.TraceServiceSettings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.devtools.cloudtrace.v1.ListTracesRequest;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Category(ParallelIntegrationTest.class)
@RunWith(JUnit4.class)
public class ITOpenTelemetryTest extends ITAbstractJdbcTest {
  static {
    SpannerOptionsHelper.resetActiveTracingFramework();
    SpannerOptions.enableOpenTelemetryMetrics();
    SpannerOptions.enableOpenTelemetryTraces();
  }

  @ClassRule public static JdbcIntegrationTestEnv env = new JdbcIntegrationTestEnv();

  private static OpenTelemetrySdk openTelemetry;

  private Database database;

  @BeforeClass
  public static void setupOpenTelemetry() throws IOException {
    assumeFalse("This test requires credentials", EmulatorSpannerHelper.isUsingEmulator());

    SpannerOptions options = env.getTestHelper().getOptions();
    TraceConfiguration.Builder traceConfigurationBuilder = TraceConfiguration.builder();
    if (options.getCredentials() != null) {
      traceConfigurationBuilder.setCredentials(options.getCredentials());
    }
    TraceConfiguration traceConfiguration =
        traceConfigurationBuilder.setProjectId(options.getProjectId()).build();
    SpanExporter traceExporter = TraceExporter.createWithConfiguration(traceConfiguration);

    String serviceName =
        "java-spanner-jdbc-integration-tests-" + ThreadLocalRandom.current().nextInt();
    openTelemetry =
        OpenTelemetrySdk.builder()
            .setTracerProvider(
                SdkTracerProvider.builder()
                    // Always sample in this test to ensure we know what we get.
                    .setSampler(Sampler.alwaysOn())
                    .setResource(Resource.builder().put("service.name", serviceName).build())
                    .addSpanProcessor(BatchSpanProcessor.builder(traceExporter).build())
                    .build())
            .build();
  }

  @AfterClass
  public static void closeOpenTelemetry() {
    if (openTelemetry != null) {
      openTelemetry.close();
    }
  }

  @Before
  public void setup() {
    database =
        env.getOrCreateDatabase(
            getDialect(),
            ImmutableList.of("create table test (id int64, value string(max)) primary key (id)"));
  }

  @Test
  public void testGlobalOpenTelemetry() throws SQLException, IOException, InterruptedException {
    GlobalOpenTelemetry.resetForTest();
    GlobalOpenTelemetry.set(openTelemetry);
    Properties info = new Properties();
    info.put("enableExtendedTracing", "true");
    try (Connection connection = createConnection(env, database, info)) {
      testOpenTelemetry(connection);
    } finally {
      GlobalOpenTelemetry.resetForTest();
    }
  }

  @Test
  public void testOpenTelemetryInProperties()
      throws SQLException, IOException, InterruptedException {
    // Make sure there is no Global OpenTelemetry.
    GlobalOpenTelemetry.resetForTest();
    Properties info = new Properties();
    info.put(JdbcDriver.OPEN_TELEMETRY_PROPERTY_KEY, openTelemetry);
    info.put("enableExtendedTracing", String.valueOf(true));
    try (Connection connection = createConnection(env, database, info)) {
      testOpenTelemetry(connection);
    }
  }

  private void testOpenTelemetry(Connection connection)
      throws SQLException, IOException, InterruptedException {
    try (Statement statement = connection.createStatement()) {
      // Generate a unique select statement so it is easy to find in the traces.
      UUID uuid = UUID.randomUUID();
      String sql = "select '" + uuid + "'";

      // Test executeQuery(String)
      try (ResultSet resultSet = statement.executeQuery(sql)) {
        assertQueryResult(resultSet, sql, uuid);
      }

      // Test execute(String)
      uuid = UUID.randomUUID();
      sql = "select '" + uuid + "'";
      assertTrue(statement.execute(sql));
      assertQueryResult(statement.getResultSet(), sql, uuid);

      // Test executeUpdate(String)
      uuid = UUID.randomUUID();
      sql =
          String.format(
              "insert into test (id, value) values (%d, '%s')",
              ThreadLocalRandom.current().nextLong(), uuid);
      assertUpdateResult(statement.executeUpdate(sql), sql);

      // Test executeLargeUpdate(String)
      uuid = UUID.randomUUID();
      sql =
          String.format(
              "insert into test (id, value) values (%d, '%s')",
              ThreadLocalRandom.current().nextLong(), uuid);
      assertUpdateResult(statement.executeLargeUpdate(sql), sql);
    }

    UUID uuid = UUID.randomUUID();
    String sql = "select '" + uuid + "'";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      try (ResultSet resultSet = statement.executeQuery()) {
        assertQueryResult(resultSet, sql, uuid);
      }
    }

    uuid = UUID.randomUUID();
    sql = "select '" + uuid + "'";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      assertTrue(statement.execute());
      assertQueryResult(statement.getResultSet(), sql, uuid);
    }

    uuid = UUID.randomUUID();
    sql = String.format("insert into test (id, value) values (?, '%s')", uuid);
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, ThreadLocalRandom.current().nextLong());
      String spannerSql = sql.replace("?", "@p1");
      assertUpdateResult(statement.executeUpdate(), spannerSql);
    }

    uuid = UUID.randomUUID();
    sql = String.format("insert into test (id, value) values (?, '%s')", uuid);
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, ThreadLocalRandom.current().nextLong());
      String spannerSql = sql.replace("?", "@p1");
      assertUpdateResult(statement.executeLargeUpdate(), spannerSql);
    }
  }

  private void assertQueryResult(ResultSet resultSet, String sql, UUID uuid)
      throws SQLException, IOException, InterruptedException {
    assertTrue(resultSet.next());
    assertEquals(uuid.toString(), resultSet.getString(1));
    assertFalse(resultSet.next());

    flushOpenTelemetry();
    assertTrace(sql);
  }

  private void assertUpdateResult(long updateCount, String sql)
      throws IOException, InterruptedException {
    assertEquals(1, updateCount);

    flushOpenTelemetry();
    assertTrace(sql);
  }

  private void assertTrace(String sql) throws IOException, InterruptedException {
    TraceServiceSettings settings =
        env.getTestHelper().getOptions().getCredentials() == null
            ? TraceServiceSettings.newBuilder().build()
            : TraceServiceSettings.newBuilder()
                .setCredentialsProvider(
                    FixedCredentialsProvider.create(
                        env.getTestHelper().getOptions().getCredentials()))
                .build();
    try (TraceServiceClient client = TraceServiceClient.create(settings)) {
      // It can take a few seconds before the trace is visible.
      Thread.sleep(5000L);
      boolean foundTrace = false;
      for (int attempts = 0; attempts < 20; attempts++) {
        // Ignore deprecation for now, as there is no alternative offered (yet?).
        //noinspection deprecation
        ListTracesPagedResponse response =
            client.listTraces(
                ListTracesRequest.newBuilder()
                    .setProjectId(database.getId().getInstanceId().getProject())
                    .setFilter("db.statement:\"" + sql + "\"")
                    .build());
        int size = Iterables.size(response.iterateAll());
        if (size != 0) {
          assertEquals(1, Iterables.size(response.iterateAll()));
          foundTrace = true;
          break;
        } else {
          Thread.sleep(5000L);
        }
      }
      assertTrue(foundTrace);
    } catch (ResourceExhaustedException resourceExhaustedException) {
      if (resourceExhaustedException
          .getMessage()
          .contains("Quota exceeded for quota metric 'Read requests (free)'")) {
        // Ignore and allow the test to succeed.
        System.out.println("RESOURCE_EXHAUSTED error ignored");
      } else {
        throw resourceExhaustedException;
      }
    }
  }

  private void flushOpenTelemetry() {
    openTelemetry.getSdkTracerProvider().forceFlush().join(10, TimeUnit.SECONDS);
  }
}
