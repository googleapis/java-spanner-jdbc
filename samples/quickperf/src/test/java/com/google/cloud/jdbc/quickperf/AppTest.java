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

package com.google.cloud.jdbc.quickperf;

import com.google.cloud.NoCredentials;
import com.google.cloud.spanner.Database;
import com.google.cloud.spanner.DatabaseAdminClient;
import com.google.cloud.spanner.InstanceConfigId;
import com.google.cloud.spanner.InstanceInfo;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.SpannerExceptionFactory;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.admin.instance.v1.InstanceAdminClient;
import com.google.cloud.spanner.connection.SpannerPool;
import com.google.common.collect.ImmutableList;
import com.google.spanner.admin.database.v1.CreateDatabaseMetadata;
import com.google.spanner.admin.database.v1.CreateDatabaseRequest;

import io.grpc.InternalConfigSelector.Result;

import com.google.spanner.admin.instance.v1.CreateInstanceRequest;
import com.google.spanner.admin.instance.v1.Instance;
import com.google.spanner.admin.instance.v1.InstanceConfigName;
import com.google.spanner.admin.instance.v1.ProjectName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.SpringApplication;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.junit.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.api.gax.longrunning.OperationFuture;

public class AppTest {
    private static String TEST_FILE = "src/test/resources/testfile.json";

    private static GenericContainer<?> emulator;

    private static String projectId = "test";
    private static String instanceId = "test";
    private static String databaseId = "quickperftest";

    private static DatabaseAdminClient dbAdminClient;

    @BeforeClass
    public static void setup() throws InterruptedException, ExecutionException {
        System.out.println("Starting Emulator");
        emulator = new GenericContainer<>(
                DockerImageName.parse("gcr.io/cloud-spanner-emulator/emulator:latest"))
                .withExposedPorts(9010)
                .waitingFor(Wait.forListeningPort());

        emulator.setPortBindings(ImmutableList.of("9010:9010"));
        emulator.start();
        System.out.println("Finished starting Emulator");

        List<String> ddlList = Arrays.asList(
                "CREATE TABLE GroupMgmt ("
                        + "group_id INT64,"
                        + "grpname STRING(MAX),"
                        + ") PRIMARY KEY(group_id)",
                "CREATE TABLE Users ("
                        + "user_id INT64,"
                        + "name STRING(MAX),"
                        + ") PRIMARY KEY(user_id)",
                "CREATE TABLE membership ("
                        + "user_id INT64,"
                        + "group_id INT64,"
                        + "enrolled TIMESTAMP NOT NULL OPTIONS ("
                        + " allow_commit_timestamp = true"
                        + "),"
                        + ") PRIMARY KEY(user_id, group_id)");

        SpannerOptions options = SpannerOptions.newBuilder()
                .setProjectId(projectId)
                .setEmulatorHost("localhost:" + emulator.getMappedPort(9010))
                .setCredentials(NoCredentials.getInstance())
                .build();

        Spanner spanner = options.getService();
        dbAdminClient = spanner.getDatabaseAdminClient();

        createInstance(projectId, instanceId);

        OperationFuture<Database, CreateDatabaseMetadata> op = dbAdminClient.createDatabase(
                instanceId,
                databaseId,
                ddlList);

        System.out.println("Creating database " + databaseId);
        Database dbOperation = op.get();
        System.out.println("Created database [" + databaseId + "]");

        // create test file
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setProject(projectId);
        projectConfig.setInstance(instanceId);
        projectConfig.setDatabase(databaseId);
        projectConfig.setThreads(1);
        projectConfig.setIterations(1000);
        projectConfig.setQuery("INSERT INTO Users (user_id, name) VALUES(?,?)");
        projectConfig.setWriteMetricToFile(false);
        projectConfig.setIsEmulator(true);

        QueryParam param1 = new QueryParam(1, "#i");
        QueryParam param2 = new QueryParam(2, "#s");
        projectConfig.setQueryParams(Arrays.asList(param1, param2));

        // Write the JSON to a file
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            mapper.writeValue(new File(TEST_FILE), projectConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void cleanup() throws IOException {
        dbAdminClient.dropDatabase(instanceId, databaseId);

        // delete test file
        Path path = Paths.get(TEST_FILE);
        Files.delete(path);
    }

    @Test
    public void testRunApplication()
            throws StreamReadException, DatabindException, SQLException, InterruptedException, IOException {

        System.setProperty("spanner.emulator", "true");
        System.setProperty("spanner.host", "//localhost:" + emulator.getMappedPort(9010));
        SpringApplication.run(AppTest.class).close();

        String[] userConfig = { "-c" + TEST_FILE };
        QuickPerf.main(userConfig);

        String url = String.format(
                "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s?autoConfigEmulator=true",
                projectId, instanceId, databaseId);

        Connection connection = DriverManager.getConnection(url);

        testQuery(connection, "SELECT count(*) FROM Users", 1000);
    }

    private void testQuery(Connection connection, String query, int expected) {
        try (Statement statement = connection.createStatement()) {
            boolean hasResults = statement.execute(query);

            if (hasResults == false) {
                assertTrue(false);
            }

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                int value = rs.getInt(1);
                assertEquals(expected, value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void createInstance(String projectId, String instanceId) {
        int nodeCount = 1;
        String displayName = "Test Instance";

        System.out.printf("Start creating instance");

        Instance instance = Instance.newBuilder()
                .setDisplayName(displayName)
                .setNodeCount(nodeCount)
                .setConfig(
                        InstanceConfigName.of(projectId, "regional-us-central1").toString())
                .build();

        try (Spanner spanner = SpannerOptions.newBuilder()
                .setProjectId(projectId)
                .setEmulatorHost("localhost:" + emulator.getMappedPort(9010))
                .setCredentials(NoCredentials.getInstance())
                .build().getService();

                InstanceAdminClient instanceAdminClient = spanner.createInstanceAdminClient()) {

            Instance createdInstance = instanceAdminClient.createInstanceAsync(
                    CreateInstanceRequest.newBuilder()
                            .setParent(ProjectName.of(projectId).toString())
                            .setInstanceId(instanceId)
                            .setInstance(instance)
                            .build())
                    .get();
            System.out.printf("Instance %s was successfully created%n", createdInstance.getName());
        } catch (ExecutionException e) {
            System.out.printf(
                    "Error: Creating instance %s failed with error message %s%n",
                    instance.getName(), e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Error: Waiting for createInstance operation to finish was interrupted");
        }
    }
}
