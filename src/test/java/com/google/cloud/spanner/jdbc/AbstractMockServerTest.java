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

package com.google.cloud.spanner.jdbc;

import com.google.cloud.spanner.MockSpannerServiceImpl;
import com.google.cloud.spanner.MockSpannerServiceImpl.StatementResult;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.admin.database.v1.MockDatabaseAdminImpl;
import com.google.cloud.spanner.admin.instance.v1.MockInstanceAdminImpl;
import com.google.cloud.spanner.jdbc.ITAbstractSpannerTest.AbortInterceptor;
import com.google.cloud.spanner.jdbc.ITAbstractSpannerTest.ITConnection;
import com.google.protobuf.AbstractMessage;
import com.google.protobuf.ListValue;
import com.google.protobuf.Value;
import com.google.spanner.v1.ExecuteSqlRequest;
import com.google.spanner.v1.ResultSetMetadata;
import com.google.spanner.v1.StructType;
import com.google.spanner.v1.StructType.Field;
import com.google.spanner.v1.Type;
import com.google.spanner.v1.TypeCode;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class AbstractMockServerTest {
  static final long COUNT_BEFORE_INSERT = 0L;
  static final long COUNT_AFTER_INSERT = 1L;
  static final Statement SELECT_COUNT_STATEMENT =
      Statement.of("SELECT COUNT(*) AS C FROM TEST WHERE ID=1");
  private static final ResultSetMetadata SELECT_COUNT_METADATA =
      ResultSetMetadata.newBuilder()
          .setRowType(
              StructType.newBuilder()
                  .addFields(
                      Field.newBuilder()
                          .setName("C")
                          .setType(Type.newBuilder().setCode(TypeCode.INT64).build())
                          .build())
                  .build())
          .build();
  static final com.google.spanner.v1.ResultSet SELECT_COUNT_RESULTSET_BEFORE_INSERT =
      com.google.spanner.v1.ResultSet.newBuilder()
          .addRows(
              ListValue.newBuilder()
                  .addValues(
                      Value.newBuilder()
                          .setStringValue(String.valueOf(COUNT_BEFORE_INSERT))
                          .build())
                  .build())
          .setMetadata(SELECT_COUNT_METADATA)
          .build();
  static final com.google.spanner.v1.ResultSet SELECT_COUNT_RESULTSET_AFTER_INSERT =
      com.google.spanner.v1.ResultSet.newBuilder()
          .addRows(
              ListValue.newBuilder()
                  .addValues(
                      Value.newBuilder().setStringValue(String.valueOf(COUNT_AFTER_INSERT)).build())
                  .build())
          .setMetadata(SELECT_COUNT_METADATA)
          .build();
  static final Statement INSERT_STATEMENT =
      Statement.of("INSERT INTO TEST (ID, NAME) VALUES (1, 'test aborted')");
  static final int UPDATE_COUNT = 1;

  static MockSpannerServiceImpl mockSpanner;
  static MockInstanceAdminImpl mockInstanceAdmin;
  static MockDatabaseAdminImpl mockDatabaseAdmin;
  private static Server server;
  private static InetSocketAddress address;

  @BeforeClass
  public static void startStaticServer() throws IOException {
    mockSpanner = new MockSpannerServiceImpl();
    mockSpanner.setAbortProbability(0.0D); // We don't want any unpredictable aborted transactions.
    mockInstanceAdmin = new MockInstanceAdminImpl();
    mockDatabaseAdmin = new MockDatabaseAdminImpl();
    address = new InetSocketAddress("localhost", 0);
    server =
        NettyServerBuilder.forAddress(address)
            .addService(mockSpanner)
            .addService(mockInstanceAdmin)
            .addService(mockDatabaseAdmin)
            .build()
            .start();
    mockSpanner.putStatementResult(
        StatementResult.query(SELECT_COUNT_STATEMENT, SELECT_COUNT_RESULTSET_BEFORE_INSERT));
    mockSpanner.putStatementResult(StatementResult.update(INSERT_STATEMENT, UPDATE_COUNT));
  }

  @AfterClass
  public static void stopServer() throws Exception {
    SpannerPool.closeSpannerPool();
    server.shutdown();
    server.awaitTermination();
  }

  @Before
  public void setupResults() {
    mockSpanner.reset();
  }

  @After
  public void closeSpannerPool() {
    SpannerPool.closeSpannerPool();
  }

  java.sql.Connection createJdbcConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:" + getBaseUrl());
  }

  ITConnection createConnection() {
    return createConnection(
        Collections.<StatementExecutionInterceptor>emptyList(),
        Collections.<TransactionRetryListener>emptyList());
  }

  ITConnection createConnection(
      AbortInterceptor interceptor, TransactionRetryListener transactionRetryListener) {
    return createConnection(
        Arrays.<StatementExecutionInterceptor>asList(interceptor),
        Arrays.<TransactionRetryListener>asList(transactionRetryListener));
  }

  ITConnection createConnection(
      List<StatementExecutionInterceptor> interceptors,
      List<TransactionRetryListener> transactionRetryListeners) {
    StringBuilder url = new StringBuilder(getBaseUrl());
    ConnectionOptions.Builder builder =
        ConnectionOptions.newBuilder()
            .setUri(url.toString())
            .setStatementExecutionInterceptors(interceptors);
    ConnectionOptions options = builder.build();
    ITConnection connection = createITConnection(options);
    for (TransactionRetryListener listener : transactionRetryListeners) {
      connection.addTransactionRetryListener(listener);
    }
    return connection;
  }

  String getBaseUrl() {
    return String.format(
        "cloudspanner://localhost:%d/projects/proj/instances/inst/databases/db?usePlainText=true;autocommit=false;retryAbortsInternally=true",
        server.getPort());
  }

  ExecuteSqlRequest getLastExecuteSqlRequest() {
    List<AbstractMessage> requests = mockSpanner.getRequests();
    for (int i = requests.size() - 1; i >= 0; i--) {
      if (requests.get(i) instanceof ExecuteSqlRequest) {
        return (ExecuteSqlRequest) requests.get(i);
      }
    }
    throw new IllegalStateException("No ExecuteSqlRequest found in requests");
  }

  private ITConnection createITConnection(ConnectionOptions options) {
    return new ITConnectionImpl(options);
  }
}
