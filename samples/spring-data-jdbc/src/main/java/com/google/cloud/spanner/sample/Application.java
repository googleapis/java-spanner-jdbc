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
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
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
  
  private final OpenTelemetry openTelemetry;

  public Application(
      SingerService singerService,
      DatabaseSeeder databaseSeeder,
      SingerRepository singerRepository,
      AlbumRepository albumRepository,
      TrackRepository trackRepository,
      OpenTelemetry openTelemetry) {
    this.databaseSeeder = databaseSeeder;
    this.singerService = singerService;
    this.singerRepository = singerRepository;
    this.albumRepository = albumRepository;
    this.trackRepository = trackRepository;
    this.openTelemetry = openTelemetry;
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
            new Album(databaseSeeder.randomTitle()),
            new Album(databaseSeeder.randomTitle()),
            new Album(databaseSeeder.randomTitle()));
    logger.info(
        "Inserted singer {} {} {}",
        insertedSinger.getId(),
        insertedSinger.getFirstName(),
        insertedSinger.getLastName());

    // Create a new track record and insert it into the database.
    Album album = albumRepository.getFirst().orElseThrow();
    Track track = new Track(album, 1, databaseSeeder.randomTitle());
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
  }
}
