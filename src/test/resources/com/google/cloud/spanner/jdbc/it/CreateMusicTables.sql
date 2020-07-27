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

START BATCH DDL;

CREATE TABLE TableWithAllColumnTypes (
  ColInt64		INT64		NOT NULL,
  ColFloat64	FLOAT64		NOT NULL,
  ColBool		BOOL		NOT NULL,
  ColString		STRING(100) NOT NULL,
  ColStringMax	STRING(MAX)	NOT NULL,
  ColBytes		BYTES(100)	NOT NULL,
  ColBytesMax	BYTES(MAX)	NOT NULL,
  ColDate		DATE		NOT NULL,
  ColTimestamp	TIMESTAMP	NOT NULL,
  ColCommitTS	TIMESTAMP	NOT NULL OPTIONS (allow_commit_timestamp=true),
  ColNumeric	NUMERIC		NOT NULL,
  
  ColInt64Array		ARRAY<INT64>,
  ColFloat64Array	ARRAY<FLOAT64>,
  ColBoolArray		ARRAY<BOOL>,
  ColStringArray	ARRAY<STRING(100)>,
  ColStringMaxArray	ARRAY<STRING(MAX)>,
  ColBytesArray		ARRAY<BYTES(100)>,
  ColBytesMaxArray	ARRAY<BYTES(MAX)>,
  ColDateArray		ARRAY<DATE>,
  ColTimestampArray	ARRAY<TIMESTAMP>,
  ColNumericArray	ARRAY<NUMERIC>
) PRIMARY KEY (ColInt64)
;

RUN BATCH;