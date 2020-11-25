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

package com.google.cloud.spanner.jdbc;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.google.cloud.spanner.MockSpannerServiceImpl;
import com.google.cloud.spanner.connection.ConnectionOptions;
import com.google.cloud.spanner.connection.ConnectionOptions.ConnectionProperty;
import com.google.cloud.spanner.connection.SpannerPool;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.rpc.Code;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JdbcDriverTest {
  /**
   * Make sure the JDBC driver class is loaded. This is needed when running the test using Maven.
   */
  static {
    try {
      Class.forName("com.google.cloud.spanner.jdbc.JdbcDriver");
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(
          "JdbcDataSource failed to load com.google.cloud.spanner.jdbc.JdbcDriver", e);
    }
  }

  private static MockSpannerServiceImpl mockSpanner;
  private static Server server;
  private static InetSocketAddress address;
  private static final String TEST_KEY_PATH =
      JdbcDriverTest.class.getResource("test-key.json").getFile();

  @BeforeClass
  public static void startStaticServer() throws IOException {
    mockSpanner = new MockSpannerServiceImpl();
    address = new InetSocketAddress("localhost", 0);
    server = NettyServerBuilder.forAddress(address).addService(mockSpanner).build().start();
  }

  @AfterClass
  public static void stopServer() throws Exception {
    SpannerPool.closeSpannerPool();
    server.shutdown();
    server.awaitTermination();
  }

  @Test
  public void testClientLibToken() {
    assertThat(JdbcDriver.getClientLibToken()).isEqualTo("sp-jdbc");
  }

  @Test
  public void testRegister() throws SQLException {
    assertThat(JdbcDriver.isRegistered()).isTrue();
    JdbcDriver.deregister();
    assertThat(JdbcDriver.isRegistered()).isFalse();
    try {
      JdbcDriver.getRegisteredDriver();
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(e.getErrorCode()).isEqualTo(Code.FAILED_PRECONDITION_VALUE);
    }
    JdbcDriver.register();
    assertThat(JdbcDriver.isRegistered()).isTrue();
    assertThat(JdbcDriver.getRegisteredDriver()).isNotNull();
  }

  @Test
  public void testConnect() throws SQLException {
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner://localhost:%d/projects/some-company.com:test-project/instances/static-test-instance/databases/test-database;usePlainText=true;credentials=%s",
                server.getPort(), TEST_KEY_PATH))) {
      assertThat(connection.isClosed()).isFalse();
    }
  }

  @Test(expected = SQLException.class)
  public void testInvalidConnect() throws SQLException {
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner://localhost:%d/projects/some-company.com:test-project/instances/static-test-instance/databases/test-database;usePlainText=true;credentialsUrl=%s",
                server.getPort(), TEST_KEY_PATH))) {
      assertThat(connection.isClosed()).isFalse();
    }
  }

  @Test
  public void testConnectWithCredentialsAndOAuthToken() throws SQLException {
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner://localhost:%d/projects/test-project/instances/static-test-instance/databases/test-database;usePlainText=true;credentials=%s;OAuthToken=%s",
                server.getPort(), TEST_KEY_PATH, "some-token"))) {
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat(e.getMessage()).contains("Cannot specify both credentials and an OAuth token");
    }
  }

  @Test
  public void testGetPropertyInfo() throws SQLException {
    DriverPropertyInfo[] props =
        JdbcDriver.getRegisteredDriver()
            .getPropertyInfo(
                "jdbc:cloudspanner:/projects/p/instances/i/databases/d", new Properties());
    assertThat(props).hasLength(ConnectionOptions.VALID_PROPERTIES.size());

    Collection<String> validConnectionPropertyNames =
        Collections2.transform(
            ConnectionOptions.VALID_PROPERTIES,
            new Function<ConnectionProperty, String>() {
              @Override
              public String apply(ConnectionProperty input) {
                return input.getName();
              }
            });
    Collection<String> driverPropertyNames =
        Collections2.transform(
            ImmutableList.copyOf(props),
            new Function<DriverPropertyInfo, String>() {
              @Override
              public String apply(DriverPropertyInfo input) {
                return input.name;
              }
            });
    assertThat(driverPropertyNames).containsExactlyElementsIn(validConnectionPropertyNames);
  }

  @Test
  public void testLenient() throws SQLException {
    // With lenient=true the driver should accept unknown properties and only generate a warning.
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner://localhost:%d/projects/p/instances/i/databases/d?usePlainText=true;credentials=%s;lenient=true;foo=bar",
                server.getPort(), TEST_KEY_PATH))) {
      assertThat(connection.isClosed()).isFalse();
      assertThat((Throwable) connection.getWarnings()).isNotNull();
      assertThat(connection.getWarnings().getMessage()).contains("foo");
    }

    // Without lenient the driver should throw an exception for unknown properties.
    try (Connection connection =
        DriverManager.getConnection(
            String.format(
                "jdbc:cloudspanner://localhost:%d/projects/p/instances/i/databases/d?usePlainText=true;credentials=%s;foo=bar",
                server.getPort(), TEST_KEY_PATH))) {
      fail("missing expected exception");
    } catch (SQLException e) {
      assertThat((Throwable) e).isInstanceOf(JdbcSqlException.class);
      JdbcSqlException jdbc = (JdbcSqlException) e;
      assertThat(jdbc.getMessage()).contains("foo");
      assertThat(jdbc.getCode()).isEqualTo(Code.INVALID_ARGUMENT);
    }
  }
}
