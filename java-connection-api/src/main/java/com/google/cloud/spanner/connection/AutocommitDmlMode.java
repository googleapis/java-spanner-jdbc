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

package com.google.cloud.spanner.connection;

/** Enum used to define the behavior of DML statements in autocommit mode */
enum AutocommitDmlMode {
  TRANSACTIONAL,
  PARTITIONED_NON_ATOMIC;

  private final String statementString;

  private AutocommitDmlMode() {
    this.statementString = name();
  }

  /**
   * Use this method to get the correct format for use in a SQL statement. Autocommit dml mode must
   * be wrapped between single quotes in SQL statements: <code>
   * SET AUTOCOMMIT_DML_MODE='TRANSACTIONAL'</code> This method returns the value
   * <strong>without</strong> the single quotes.
   *
   * @return a string representation of this {@link AutocommitDmlMode} that can be used in a SQL
   *     statement.
   */
  public String getStatementString() {
    return statementString;
  }
}
