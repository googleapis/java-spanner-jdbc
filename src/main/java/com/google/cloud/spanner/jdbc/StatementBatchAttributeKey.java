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

package com.google.cloud.spanner.jdbc;

import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.jdbc.StatementBatchAttributeKey.StatementBatch;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.AttributeType;
import io.opentelemetry.semconv.SemanticAttributes;
import java.util.List;
import java.util.stream.Collectors;

class StatementBatchAttributeKey implements AttributeKey<StatementBatch> {
  @SuppressWarnings("deprecation")
  static final StatementBatchAttributeKey INSTANCE =
      new StatementBatchAttributeKey(SemanticAttributes.DB_STATEMENT.getKey());

  static class StatementBatch {
    private final List<Statement> statements;

    StatementBatch(List<Statement> statements) {
      this.statements = statements;
    }

    @Override
    public String toString() {
      return statements.stream().map(Statement::getSql).collect(Collectors.joining(";\n"));
    }
  }

  private final String key;

  private StatementBatchAttributeKey(String key) {
    this.key = key;
  }

  @Override
  public String getKey() {
    return this.key;
  }

  @Override
  public AttributeType getType() {
    return AttributeType.STRING;
  }
}
