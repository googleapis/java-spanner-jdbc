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

import com.google.cloud.spanner.sample.entities.Album;
import com.google.cloud.spanner.sample.entities.Singer;
import com.google.cloud.spanner.sample.entities.Track;
import com.google.cloud.spanner.sample.repositories.AlbumRepository;
import com.google.cloud.spanner.sample.repositories.SingerRepository;
import com.google.cloud.spanner.sample.repositories.TrackRepository;
import com.google.cloud.spanner.sample.service.SingerService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args).close();
  }

  private final DatabaseSeeder databaseSeeder;

  private final SingerService singerService;

  private final SingerRepository singerRepository;

  private final AlbumRepository albumRepository;

  private final TrackRepository trackRepository;

  private final Tracer tracer;

  private final DataSource dataSource;

  public Application(
      SingerService singerService,
      DatabaseSeeder databaseSeeder,
      SingerRepository singerRepository,
      AlbumRepository albumRepository,
      TrackRepository trackRepository,
      Tracer tracer,
      DataSource dataSource) {
    this.databaseSeeder = databaseSeeder;
    this.singerService = singerService;
    this.singerRepository = singerRepository;
    this.albumRepository = albumRepository;
    this.trackRepository = trackRepository;
    this.tracer = tracer;
    this.dataSource = dataSource;
  }

  @Override
  public void run(String... args) {
    // Set the system property 'drop_schema' to true to drop any existing database
    // schema when the application is executed.
    if (Boolean.parseBoolean(System.getProperty("drop_schema", "false"))) {
      logger.info("Dropping existing schema if it exists");
      databaseSeeder.dropDatabaseSchemaIfExists();
    }

    logger.info("Creating database schema if it does not already exist");
    databaseSeeder.createDatabaseSchemaIfNotExists();
    logger.info("Deleting existing test data");
    databaseSeeder.deleteTestData();
    logger.info("Inserting fresh test data");
    databaseSeeder.insertTestData();

    Iterable<Singer> allSingers = singerRepository.findAll();
    for (Singer singer : allSingers) {
      logger.info(
          "Found singer: {} with {} albums",
          singer,
          albumRepository.countAlbumsBySingerId(singer.getId()));
      for (Album album : albumRepository.findAlbumsBySingerId(singer.getId())) {
        logger.info("\tAlbum: {}, released at {}", album, album.getReleaseDate());
      }
    }

    // Create a new singer and three albums in a transaction.
    Singer insertedSinger =
        singerService.createSingerAndAlbums(
            new Singer("Amethyst", "Jiang"),
            new Album(DatabaseSeeder.randomTitle()),
            new Album(DatabaseSeeder.randomTitle()),
            new Album(DatabaseSeeder.randomTitle()));
    logger.info(
        "Inserted singer {} {} {}",
        insertedSinger.getId(),
        insertedSinger.getFirstName(),
        insertedSinger.getLastName());

    // Create a new track record and insert it into the database.
    Album album = albumRepository.getFirst().orElseThrow();
    Track track = new Track(album, 1, DatabaseSeeder.randomTitle());
    track.setSampleRate(3.14d);
    // Spring Data JDBC supports the same base CRUD operations on entities as for example
    // Spring Data JPA.
    trackRepository.save(track);

    // List all singers that have a last name starting with an 'J'.
    logger.info("All singers with a last name starting with an 'J':");
    for (Singer singer : singerRepository.findSingersByLastNameStartingWith("J")) {
      logger.info("\t{}", singer.getFullName());
    }

    // The singerService.listSingersWithLastNameStartingWith(..) method uses a read-only
    // transaction. You should prefer read-only transactions to read/write transactions whenever
    // possible, as read-only transactions do not take locks.
    logger.info("All singers with a last name starting with an 'A', 'B', or 'C'.");
    for (Singer singer : singerService.listSingersWithLastNameStartingWith("A", "B", "C")) {
      logger.info("\t{}", singer.getFullName());
    }

    // Create two transactions that conflict with each other to trigger a transaction retry.
    Span span = tracer.spanBuilder("update-singers").startSpan();
    try (Scope ignore = span.makeCurrent();
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();
        Statement statement1 = connection1.createStatement();
        Statement statement2 = connection2.createStatement()) {
      statement1.execute("begin");
      statement1.execute("set spanner.transaction_tag='update-singer-1'");
      statement2.execute("begin");
      statement1.execute("set spanner.transaction_tag='update-singer-2'");
      long id = 0L;
      statement1.execute("set spanner.statement_tag='fetch-singer-id'");
      try (ResultSet resultSet = statement1.executeQuery("select id from singers limit 1")) {
        while (resultSet.next()) {
          id = resultSet.getLong(1);
        }
      }
      String sql = "update singers set active=not active where id=?";
      statement1.execute("set spanner.statement_tag='update-singer-1'");
      try (PreparedStatement preparedStatement = connection1.prepareStatement(sql)) {
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
      }
      statement2.execute("set spanner.statement_tag='update-singer-2'");
      try (PreparedStatement preparedStatement = connection2.prepareStatement(sql)) {
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
      }
      statement1.execute("commit");
      statement2.execute("commit");
    } catch (SQLException exception) {
      span.recordException(exception);
      throw new RuntimeException(exception);
    } finally {
      span.end();
    }
  }
}
