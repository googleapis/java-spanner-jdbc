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

/*
 * Script for testing setting invalid values for the different connection and transaction options
 */

@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for AUTOCOMMIT: on'
set autocommit = on;
@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.readonly: on'
set spanner.readonly = on;

SET AUTOCOMMIT = TRUE;
@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.autocommit_dml_mode: 'non_atomic''
set spanner.autocommit_dml_mode='non_atomic';

@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.read_only_staleness: 'weak''
set spanner.read_only_staleness='weak';

@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.read_only_staleness: 'strong 2018-11-15T13:09:25Z''
set spanner.read_only_staleness='strong 2018-11-15T13:09:25Z';

@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.read_only_staleness: 'MIN_READ_TIMESTAMP''
set spanner.read_only_staleness='MIN_READ_TIMESTAMP';
@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.read_only_staleness: 'MIN_READ_TIMESTAMP 10s''
set spanner.read_only_staleness='MIN_READ_TIMESTAMP 10s';
-- Missing timezone in timestamp
@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.read_only_staleness: 'MIN_READ_TIMESTAMP 2018-11-15T13:09:25''
set spanner.read_only_staleness='MIN_READ_TIMESTAMP 2018-11-15T13:09:25';

@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.read_only_staleness: 'MAX_STALENESS''
set spanner.read_only_staleness='MAX_STALENESS';
@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.read_only_staleness: 'MAX_STALENESS 2018-11-15T13:09:25Z''
set spanner.read_only_staleness='MAX_STALENESS 2018-11-15T13:09:25Z';
-- Missing time unit
@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for spanner.read_only_staleness: 'MAX_STALENESS 10''
set spanner.read_only_staleness='MAX_STALENESS 10';

@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for STATEMENT_TIMEOUT: -1'
set statement_timeout=-1;
@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for STATEMENT_TIMEOUT: '1''
set statement_timeout='1';

@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for TRANSACTION: readonly'
set transaction readonly;
