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

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.SpannerExceptionFactory;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.admin.database.v1.DatabaseAdminClient;
import com.google.cloud.spanner.admin.database.v1.DatabaseAdminSettings;
import com.google.cloud.spanner.jdbc.CloudSpannerJdbcConnection;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.spanner.admin.database.v1.CreateDatabaseRequest;
import com.google.spanner.admin.database.v1.DatabaseDialect;
import com.google.spanner.admin.instance.v1.InstanceName;
import com.google.spanner.v1.DatabaseName;
import io.grpc.ManagedChannelBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class JdbcSample {
  static class Singer {

    final long singerId;
    final String firstName;
    final String lastName;

    Singer(long singerId, String firstName, String lastName) {
      this.singerId = singerId;
      this.firstName = firstName;
      this.lastName = lastName;
    }
  }

  static class Album {

    final long singerId;
    final long albumId;
    final String albumTitle;

    Album(long singerId, long albumId, String albumTitle) {
      this.singerId = singerId;
      this.albumId = albumId;
      this.albumTitle = albumTitle;
    }
  }

  // [START spanner_insert_data]
  // [START spanner_postgresql_insert_data]
  static final List<Singer> SINGERS =
      Arrays.asList(
          new Singer(1, "Marc", "Richards"),
          new Singer(2, "Catalina", "Smith"),
          new Singer(3, "Alice", "Trentor"),
          new Singer(4, "Lea", "Martin"),
          new Singer(5, "David", "Lomond"));

  static final List<Album> ALBUMS =
      Arrays.asList(
          new Album(1, 1, "Total Junk"),
          new Album(1, 2, "Go, Go, Go"),
          new Album(2, 1, "Green"),
          new Album(2, 2, "Forever Hold Your Peace"),
          new Album(2, 3, "Terrified"));

  // [END spanner_insert_data]
  // [END spanner_postgresql_insert_data]

  // [START spanner_create_database]
  static void createDatabase(
      DatabaseAdminClient dbAdminClient,
      InstanceName instanceName,
      String databaseId,
      Properties properties)
      throws SQLException {
    // Use the Spanner admin client to create a database.
    CreateDatabaseRequest createDatabaseRequest =
        CreateDatabaseRequest.newBuilder()
            .setCreateStatement("CREATE DATABASE `" + databaseId + "`")
            .setParent(instanceName.toString())
            .build();
    try {
      dbAdminClient.createDatabaseAsync(createDatabaseRequest).get();
    } catch (ExecutionException e) {
      throw SpannerExceptionFactory.asSpannerException(e.getCause());
    } catch (InterruptedException e) {
      throw SpannerExceptionFactory.propagateInterrupt(e);
    }

    // Connect to the database with the JDBC driver and create two test tables.
    DatabaseName databaseName =
        DatabaseName.of(instanceName.getProject(), instanceName.getInstance(), databaseId);
    try (Connection connection =
        DriverManager.getConnection("jdbc:cloudspanner:/" + databaseName, properties)) {
      try (Statement statement = connection.createStatement()) {
        // Create the tables in one batch.
        statement.addBatch(
            "CREATE TABLE Singers ("
                + "  SingerId   INT64 NOT NULL,"
                + "  FirstName  STRING(1024),"
                + "  LastName   STRING(1024),"
                + "  SingerInfo BYTES(MAX),"
                + "  FullName STRING(2048) AS "
                + "  (ARRAY_TO_STRING([FirstName, LastName], \" \")) STORED"
                + ") PRIMARY KEY (SingerId)");
        statement.addBatch(
            "CREATE TABLE Albums ("
                + "  SingerId     INT64 NOT NULL,"
                + "  AlbumId      INT64 NOT NULL,"
                + "  AlbumTitle   STRING(MAX)"
                + ") PRIMARY KEY (SingerId, AlbumId),"
                + "  INTERLEAVE IN PARENT Singers ON DELETE CASCADE");
        statement.executeBatch();
      }
    }
    System.out.println("Created database [" + databaseName + "]");
  }
  // [END spanner_create_database]

  // [START spanner_postgresql_create_database]
  static void createPostgreSQLDatabase(
      DatabaseAdminClient dbAdminClient,
      InstanceName instanceName,
      String databaseId,
      Properties properties)
      throws SQLException {
    // Use the Spanner admin client to create a database.
    CreateDatabaseRequest createDatabaseRequest =
        CreateDatabaseRequest.newBuilder()
            // PostgreSQL database names and other identifiers should be quoted using double quotes.
            .setCreateStatement("create database \"" + databaseId + "\"")
            .setParent(instanceName.toString())
            .setDatabaseDialect(DatabaseDialect.POSTGRESQL)
            .build();
    try {
      dbAdminClient.createDatabaseAsync(createDatabaseRequest).get();
    } catch (ExecutionException e) {
      throw SpannerExceptionFactory.asSpannerException(e.getCause());
    } catch (InterruptedException e) {
      throw SpannerExceptionFactory.propagateInterrupt(e);
    }

    // Connect to the database with the JDBC driver and create two test tables.
    DatabaseName databaseName =
        DatabaseName.of(instanceName.getProject(), instanceName.getInstance(), databaseId);
    try (Connection connection =
        DriverManager.getConnection("jdbc:cloudspanner:/" + databaseName, properties)) {
      try (Statement statement = connection.createStatement()) {
        // Create the tables in one batch.
        statement.addBatch(
            "create table singers ("
                + "  singer_id   bigint primary key not null,"
                + "  first_name  varchar(1024),"
                + "  last_name   varchar(1024),"
                + "  singer_info bytea,"
                + "  full_name   varchar(2048) generated always as (\n"
                + "      case when first_name is null then last_name\n"
                + "          when last_name  is null then first_name\n"
                + "          else first_name || ' ' || last_name\n"
                + "      end) stored"
                + ")");
        statement.addBatch(
            "create table albums ("
                + "  singer_id     bigint not null,"
                + "  album_id      bigint not null,"
                + "  album_title   varchar,"
                + "  primary key (singer_id, album_id)"
                + ") interleave in parent singers on delete cascade");
        statement.executeBatch();
      }
    }
    System.out.println("Created database [" + databaseName + "]");
  }
  // [END spanner_postgresql_create_database]

  // [START create_jdbc_connection]
  static void createConnection(
      String project, String instance, String database, Properties properties) throws SQLException {
    // Connection properties can be specified both with in a Properties object
    // and in the connection URL.
    properties.put("numChannels", "8");
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s"
                    + ";minSessions=400;maxSessions=400",
                project, instance, database),
            properties)) {
      try (ResultSet resultSet =
          connection.createStatement().executeQuery("select 'Hello World!'")) {
        while (resultSet.next()) {
          System.out.println(resultSet.getString(1));
        }
      }
    }
  }
  // [END create_jdbc_connection]

  // [START spanner_dml_getting_started_insert]
  static void writeDataWithDml(
      String project, String instance, String database, Properties properties) throws SQLException {
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s",
                project, instance, database),
            properties)) {
      // Add 4 rows in one statement.
      // JDBC always uses '?' as a parameter placeholder.
      try (PreparedStatement preparedStatement =
          connection.prepareStatement(
              "INSERT INTO Singers (SingerId, FirstName, LastName) VALUES "
                  + "(?, ?, ?), "
                  + "(?, ?, ?), "
                  + "(?, ?, ?), "
                  + "(?, ?, ?)")) {

        // Note that JDBC parameters start at index 1.
        int paramIndex = 0;
        preparedStatement.setLong(++paramIndex, 12L);
        preparedStatement.setString(++paramIndex, "Melissa");
        preparedStatement.setString(++paramIndex, "Garcia");

        preparedStatement.setLong(++paramIndex, 13L);
        preparedStatement.setString(++paramIndex, "Russel");
        preparedStatement.setString(++paramIndex, "Morales");

        preparedStatement.setLong(++paramIndex, 14L);
        preparedStatement.setString(++paramIndex, "Jacqueline");
        preparedStatement.setString(++paramIndex, "Long");

        preparedStatement.setLong(++paramIndex, 15L);
        preparedStatement.setString(++paramIndex, "Dylan");
        preparedStatement.setString(++paramIndex, "Shaw");

        int updateCount = preparedStatement.executeUpdate();
        System.out.printf("%d records inserted.\n", updateCount);
      }
    }
  }
  // [END spanner_dml_getting_started_insert]

  // [START spanner_postgresql_dml_getting_started_insert]
  static void writeDataWithDmlPostgreSQL(
      String project, String instance, String database, Properties properties) throws SQLException {
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s",
                project, instance, database),
            properties)) {
      // Add 4 rows in one statement.
      // JDBC always uses '?' as a parameter placeholder.
      try (PreparedStatement preparedStatement =
          connection.prepareStatement(
              "INSERT INTO singers (singer_id, first_name, last_name) VALUES "
                  + "(?, ?, ?), "
                  + "(?, ?, ?), "
                  + "(?, ?, ?), "
                  + "(?, ?, ?)")) {

        // Note that JDBC parameters start at index 1.
        int paramIndex = 0;
        preparedStatement.setLong(++paramIndex, 12L);
        preparedStatement.setString(++paramIndex, "Melissa");
        preparedStatement.setString(++paramIndex, "Garcia");

        preparedStatement.setLong(++paramIndex, 13L);
        preparedStatement.setString(++paramIndex, "Russel");
        preparedStatement.setString(++paramIndex, "Morales");

        preparedStatement.setLong(++paramIndex, 14L);
        preparedStatement.setString(++paramIndex, "Jacqueline");
        preparedStatement.setString(++paramIndex, "Long");

        preparedStatement.setLong(++paramIndex, 15L);
        preparedStatement.setString(++paramIndex, "Dylan");
        preparedStatement.setString(++paramIndex, "Shaw");

        int updateCount = preparedStatement.executeUpdate();
        System.out.printf("%d records inserted.\n", updateCount);
      }
    }
  }
  // [END spanner_postgresql_dml_getting_started_insert]

  // [START spanner_dml_batch]
  static void writeDataWithDmlBatch(
      String project, String instance, String database, Properties properties) throws SQLException {
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s",
                project, instance, database),
            properties)) {
      // Add multiple rows in one DML batch.
      // JDBC always uses '?' as a parameter placeholder.
      try (PreparedStatement preparedStatement =
          connection.prepareStatement(
              "INSERT INTO Singers (SingerId, FirstName, LastName) VALUES (?, ?, ?)")) {
        ImmutableList<Singer> singers = ImmutableList.of(
            new Singer(16L, "Sarah", "Wilson"),
            new Singer(17L, "Ethan", "Miller"),
            new Singer(18L, "Maya", "Patel"));

        for (Singer singer : singers) {
          // Note that JDBC parameters start at index 1.
          preparedStatement.setLong(1, singer.singerId);
          preparedStatement.setString(2, singer.firstName);
          preparedStatement.setString(3, singer.lastName);
          preparedStatement.addBatch();
        }

        int[] updateCounts = preparedStatement.executeBatch();
        System.out.printf("%d records inserted.\n", Arrays.stream(updateCounts).sum());
      }
    }
  }
  // [END spanner_dml_batch]

  // [START spanner_postgresql_dml_batch]
  static void writeDataWithDmlBatchPostgreSQL(
      String project, String instance, String database, Properties properties) throws SQLException {
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s",
                project, instance, database),
            properties)) {
      // Add multiple rows in one DML batch.
      // JDBC always uses '?' as a parameter placeholder.
      try (PreparedStatement preparedStatement =
          connection.prepareStatement(
              "INSERT INTO singers (singer_id, first_name, last_name) VALUES (?, ?, ?)")) {
        ImmutableList<Singer> singers = ImmutableList.of(
            new Singer(16L, "Sarah", "Wilson"),
            new Singer(17L, "Ethan", "Miller"),
            new Singer(18L, "Maya", "Patel"));

        for (Singer singer : singers) {
          // Note that JDBC parameters start at index 1.
          preparedStatement.setLong(1, singer.singerId);
          preparedStatement.setString(2, singer.firstName);
          preparedStatement.setString(3, singer.lastName);
          preparedStatement.addBatch();
        }

        int[] updateCounts = preparedStatement.executeBatch();
        System.out.printf("%d records inserted.\n", Arrays.stream(updateCounts).sum());
      }
    }
  }
  // [END spanner_postgresql_dml_batch]

  // [START spanner_insert_data]
  static void writeDataWithMutations(
      String project, String instance, String database, Properties properties) throws SQLException {
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s",
                project, instance, database),
            properties)) {
      // Unwrap the CloudSpannerJdbcConnection interface from the java.sql.Connection.
      CloudSpannerJdbcConnection cloudSpannerJdbcConnection =
          connection.unwrap(CloudSpannerJdbcConnection.class);

      List<Mutation> mutations = new ArrayList<>();
      for (Singer singer : SINGERS) {
        mutations.add(
            Mutation.newInsertBuilder("Singers")
                .set("SingerId")
                .to(singer.singerId)
                .set("FirstName")
                .to(singer.firstName)
                .set("LastName")
                .to(singer.lastName)
                .build());
      }
      for (Album album : ALBUMS) {
        mutations.add(
            Mutation.newInsertBuilder("Albums")
                .set("SingerId")
                .to(album.singerId)
                .set("AlbumId")
                .to(album.albumId)
                .set("AlbumTitle")
                .to(album.albumTitle)
                .build());
      }
      // Apply the mutations atomically to Spanner.
      cloudSpannerJdbcConnection.write(mutations);
      System.out.printf("Inserted %d rows.\n", mutations.size());
    }
  }
  // [END spanner_insert_data]

  // [START spanner_postgresql_insert_data]
  static void writeDataWithMutationsPostgreSQL(
      String project, String instance, String database, Properties properties) throws SQLException {
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s",
                project, instance, database),
            properties)) {
      // Unwrap the CloudSpannerJdbcConnection interface from the java.sql.Connection.
      CloudSpannerJdbcConnection cloudSpannerJdbcConnection =
          connection.unwrap(CloudSpannerJdbcConnection.class);

      List<Mutation> mutations = new ArrayList<>();
      for (Singer singer : SINGERS) {
        mutations.add(
            Mutation.newInsertBuilder("singers")
                .set("singer_id")
                .to(singer.singerId)
                .set("first_name")
                .to(singer.firstName)
                .set("last_name")
                .to(singer.lastName)
                .build());
      }
      for (Album album : ALBUMS) {
        mutations.add(
            Mutation.newInsertBuilder("albums")
                .set("singer_id")
                .to(album.singerId)
                .set("album_id")
                .to(album.albumId)
                .set("album_title")
                .to(album.albumTitle)
                .build());
      }
      // Apply the mutations atomically to Spanner.
      cloudSpannerJdbcConnection.write(mutations);
      System.out.printf("Inserted %d rows.\n", mutations.size());
    }
  }
  // [END spanner_postgresql_insert_data]

  public static void main(String[] args) throws Exception {
    if (args.length != 3 && args.length != 4) {
      printUsageAndExit();
    }
    try (DatabaseAdminClient dbAdminClient = createDatabaseAdminClient()) {
      final String command = args[0];
      DatabaseId databaseId =
          DatabaseId.of(SpannerOptions.getDefaultInstance().getProjectId(), args[1], args[2]);

      run(dbAdminClient, command, databaseId);
    }
    System.out.println();
    System.out.println("Finished running sample");
  }

  static DatabaseAdminClient createDatabaseAdminClient() throws Exception {
    String emulatorHost = System.getenv("SPANNER_EMULATOR_HOST");
    if (!Strings.isNullOrEmpty(emulatorHost)) {
      return DatabaseAdminClient.create(
          DatabaseAdminSettings.newBuilder()
              .setTransportChannelProvider(
                  InstantiatingGrpcChannelProvider.newBuilder()
                      .setEndpoint(emulatorHost)
                      .setChannelConfigurator(ManagedChannelBuilder::usePlaintext)
                      .build())
              .setCredentialsProvider(NoCredentialsProvider.create())
              .build());
    }
    return DatabaseAdminClient.create();
  }

  static Properties createProperties() {
    Properties properties = new Properties();
    String emulatorHost = System.getenv("SPANNER_EMULATOR_HOST");
    if (!Strings.isNullOrEmpty(emulatorHost)) {
      properties.put("autoConfigEmulator", "true");
      properties.put("endpoint", emulatorHost);
    }
    return properties;
  }

  static void run(DatabaseAdminClient dbAdminClient, String command, DatabaseId database)
      throws Exception {
    switch (command) {
      case "createdatabase":
        createDatabase(
            dbAdminClient,
            InstanceName.of(
                database.getInstanceId().getProject(), database.getInstanceId().getInstance()),
            database.getDatabase(),
            createProperties());
        break;
      case "createpgdatabase":
        createPostgreSQLDatabase(
            dbAdminClient,
            InstanceName.of(
                database.getInstanceId().getProject(), database.getInstanceId().getInstance()),
            database.getDatabase(),
            createProperties());
        break;
      default:
        printUsageAndExit();
    }
  }

  static void printUsageAndExit() {
    System.err.println("Usage:");
    System.err.println("    JdbcSample <command> <instance_id> <database_id>");
    System.err.println();
    System.err.println("Examples:");
    System.err.println("    JdbcSample createdatabase my-instance example-db");
    System.exit(1);
  }
}
