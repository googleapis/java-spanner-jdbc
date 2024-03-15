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

package com.example.spanner.jdbc;

import static com.example.spanner.jdbc.JdbcSample.createConnection;
import static com.example.spanner.jdbc.JdbcSample.createDatabase;
import static com.example.spanner.jdbc.JdbcSample.createPostgreSQLDatabase;
import static com.example.spanner.jdbc.JdbcSample.writeDataWithDml;
import static com.example.spanner.jdbc.JdbcSample.writeDataWithDmlPostgreSQL;
import static org.junit.Assert.assertEquals;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.cloud.spanner.admin.database.v1.DatabaseAdminClient;
import com.google.cloud.spanner.admin.database.v1.DatabaseAdminSettings;
import com.google.cloud.spanner.admin.instance.v1.InstanceAdminClient;
import com.google.cloud.spanner.admin.instance.v1.InstanceAdminSettings;
import com.google.cloud.spanner.connection.SpannerPool;
import com.google.spanner.admin.instance.v1.Instance;
import com.google.spanner.admin.instance.v1.InstanceConfig;
import com.google.spanner.admin.instance.v1.InstanceName;
import com.google.spanner.admin.instance.v1.ProjectName;
import com.google.spanner.v1.DatabaseName;
import io.grpc.ManagedChannelBuilder;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@RunWith(JUnit4.class)
public class JdbcSampleTest {
  private static final String PROJECT_ID = "emulator-project";
  private static final String INSTANCE_ID = "test-instance";
  private static final String DATABASE_ID = "test-database";
  private static final String PG_DATABASE_ID = "pg-test-database";

  private static final ProjectName PROJECT_NAME = ProjectName.of(PROJECT_ID);

  private static final InstanceName INSTANCE_NAME = InstanceName.of(PROJECT_ID, INSTANCE_ID);

  private static GenericContainer<?> emulator;

  private static Properties properties;

  @BeforeClass
  public static void setup() throws Exception {
    emulator =
        new GenericContainer<>(
                DockerImageName.parse("gcr.io/cloud-spanner-emulator/emulator:latest"))
            .withExposedPorts(9010)
            .waitingFor(Wait.forListeningPort());
    emulator.start();
    try (InstanceAdminClient client =
        InstanceAdminClient.create(
            InstanceAdminSettings.newBuilder()
                .setTransportChannelProvider(
                    InstantiatingGrpcChannelProvider.newBuilder()
                        .setEndpoint(emulator.getHost() + ":" + emulator.getMappedPort(9010))
                        .setChannelConfigurator(ManagedChannelBuilder::usePlaintext)
                        .build())
                .setCredentialsProvider(NoCredentialsProvider.create())
                .build())) {
      InstanceConfig config =
          client.listInstanceConfigs(PROJECT_NAME).iterateAll().iterator().next();
      client
          .createInstanceAsync(
              PROJECT_NAME,
              INSTANCE_ID,
              Instance.newBuilder()
                  .setConfig(config.getName())
                  .setDisplayName("Test Instance")
                  .setNodeCount(1)
                  .build())
          .get();
    }
    // Create properties for the JDBC driver to connect to the emulator.
    properties = new Properties();
    properties.put("autoConfigEmulator", "true");
    properties.put("endpoint", emulator.getHost() + ":" + emulator.getMappedPort(9010));
  }

  @AfterClass
  public static void cleanup() {
    SpannerPool.closeSpannerPool();
    emulator.stop();
  }

  DatabaseAdminClient createDatabaseAdminClient() throws Exception {
    return DatabaseAdminClient.create(
        DatabaseAdminSettings.newBuilder()
            .setTransportChannelProvider(
                InstantiatingGrpcChannelProvider.newBuilder()
                    .setEndpoint(emulator.getHost() + ":" + emulator.getMappedPort(9010))
                    .setChannelConfigurator(ManagedChannelBuilder::usePlaintext)
                    .build())
            .setCredentialsProvider(NoCredentialsProvider.create())
            .build());
  }

  @Test
  public void testGoogleSQLSamples() throws Exception {
    String result;
    try (DatabaseAdminClient client = createDatabaseAdminClient()) {
      result = runSample(() -> createDatabase(client, INSTANCE_NAME, DATABASE_ID, properties));
    }
    assertEquals(
        "Created database [" + DatabaseName.of(PROJECT_ID, INSTANCE_ID, DATABASE_ID) + "]\n",
        result);

    result = runSample(() -> createConnection(PROJECT_ID, INSTANCE_ID, DATABASE_ID, properties));
    assertEquals("Hello World!\n", result);

    result = runSample(() -> writeDataWithDml(PROJECT_ID, INSTANCE_ID, DATABASE_ID, properties));
    assertEquals("4 records inserted.\n", result);
  }

  @Test
  public void testPostgreSQLSamples() throws Exception {
    String result;
    try (DatabaseAdminClient client = createDatabaseAdminClient()) {
      result =
          runSample(
              () -> createPostgreSQLDatabase(client, INSTANCE_NAME, PG_DATABASE_ID, properties));
    }
    assertEquals(
        "Created database [" + DatabaseName.of(PROJECT_ID, INSTANCE_ID, PG_DATABASE_ID) + "]\n",
        result);

    result = runSample(() -> createConnection(PROJECT_ID, INSTANCE_ID, PG_DATABASE_ID, properties));
    assertEquals("Hello World!\n", result);

    result =
        runSample(
            () -> writeDataWithDmlPostgreSQL(PROJECT_ID, INSTANCE_ID, PG_DATABASE_ID, properties));
    assertEquals("4 records inserted.\n", result);
  }

  interface Sample {
    void run() throws Exception;
  }

  String runSample(Sample sample) throws Exception {
    PrintStream stdOut = System.out;
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bout);
    System.setOut(out);
    try {
      sample.run();
    } finally {
      System.setOut(stdOut);
    }
    return bout.toString();
  }
}
