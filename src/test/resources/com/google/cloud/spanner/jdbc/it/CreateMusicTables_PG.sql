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

START BATCH DDL;

CREATE TABLE Singers (
                         SingerId   BIGINT PRIMARY KEY,
                         FirstName  VARCHAR(1024),
                         LastName   VARCHAR(1024),
                         SingerInfo BYTEA,
                         BirthDate  VARCHAR
);

CREATE INDEX SingersByFirstLastName ON Singers(FirstName, LastName);

CREATE TABLE Albums (
                        SingerId  BIGINT NOT NULL,
                        AlbumId   BIGINT NOT NULL,
                        AlbumTitle VARCHAR,
                        MarketingBudget BIGINT,
                        PRIMARY KEY(SingerId, AlbumId),
                        FOREIGN KEY(SingerId) REFERENCES Singers(SingerID)
);

CREATE INDEX AlbumsByAlbumTitle ON Albums(AlbumTitle);

CREATE TABLE Songs (
                       SingerId  BIGINT NOT NULL,
                       AlbumId   BIGINT NOT NULL,
                       TrackId   BIGINT NOT NULL,
                       SongName  VARCHAR,
                       Duration  BIGINT,
                       SongGenre VARCHAR(25),
                       PRIMARY KEY(SingerId, AlbumId, TrackId),
                       FOREIGN KEY(AlbumId) REFERENCES Albums(AlbumId)
);

CREATE UNIQUE INDEX SongsBySingerAlbumSongNameDesc ON Songs(SingerId, AlbumId, SongName DESC);

CREATE INDEX SongsBySongName ON Songs(SongName);

CREATE TABLE Concerts (
                          VenueId      BIGINT NOT NULL,
                          SingerId     BIGINT NOT NULL,
                          ConcertDate  VARCHAR NOT NULL,
                          BeginTime    VARCHAR,
                          EndTime      VARCHAR,
                          PRIMARY KEY(VenueId, SingerId, ConcertDate)
);

CREATE TABLE TableWithAllColumnTypes (
                                         ColInt64	 BIGINT PRIMARY KEY,
                                         ColFloat8	 FLOAT8		NOT NULL,
                                         ColBool		 BOOL		NOT NULL,
                                         ColString	 VARCHAR(100) NOT NULL,
                                         ColBytes	BYTEA	NOT NULL,
                                         ColNumeric	NUMERIC		NOT NULL
);

RUN BATCH;
