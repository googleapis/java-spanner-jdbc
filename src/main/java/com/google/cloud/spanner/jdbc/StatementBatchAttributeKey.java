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
