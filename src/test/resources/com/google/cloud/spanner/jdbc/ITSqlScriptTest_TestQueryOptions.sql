/*
 * Copyright 2020 Google LLC
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
 * Test setting and getting the optimizer version to use.
 */

-- Set and get valid values.
@EXPECT NO_RESULT
SET OPTIMIZER_VERSION = '1';

@EXPECT RESULT_SET 'OPTIMIZER_VERSION','1'
SHOW VARIABLE OPTIMIZER_VERSION;

@EXPECT NO_RESULT
SET OPTIMIZER_VERSION = '555';

@EXPECT RESULT_SET 'OPTIMIZER_VERSION','555'
SHOW VARIABLE OPTIMIZER_VERSION;

@EXPECT NO_RESULT
SET OPTIMIZER_VERSION = 'LATEST';

@EXPECT RESULT_SET 'OPTIMIZER_VERSION','LATEST'
SHOW VARIABLE OPTIMIZER_VERSION;

@EXPECT NO_RESULT
SET OPTIMIZER_VERSION = '';

@EXPECT RESULT_SET 'OPTIMIZER_VERSION',''
SHOW VARIABLE OPTIMIZER_VERSION;

-- Try to set invalid values.
@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for OPTIMIZER_VERSION: 'None''
SET OPTIMIZER_VERSION = 'None';

@EXPECT EXCEPTION INVALID_ARGUMENT 'INVALID_ARGUMENT: Unknown value for OPTIMIZER_VERSION: 'v1''
SET OPTIMIZER_VERSION = 'v1';
