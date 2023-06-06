package com.google.cloud.spanner.jdbc.it;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeFalse;

import com.google.cloud.spanner.Database;
import com.google.cloud.spanner.DatabaseAdminClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Dialect;
import com.google.cloud.spanner.IntegrationTestEnv;
import com.google.cloud.spanner.ParallelIntegrationTest;
import com.google.cloud.spanner.connection.ConnectionOptions;
import com.google.cloud.spanner.jdbc.ProtoEnumType;
import com.google.cloud.spanner.jdbc.ProtoMessageType;
import com.google.cloud.spanner.jdbc.it.SingerProto.Genre;
import com.google.cloud.spanner.jdbc.it.SingerProto.SingerInfo;
import com.google.cloud.spanner.testing.EmulatorSpannerHelper;
import com.google.cloud.spanner.testing.RemoteSpannerHelper;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Category(ParallelIntegrationTest.class)
@RunWith(JUnit4.class)
public class ITProtoColumnsTest {
  @ClassRule public static IntegrationTestEnv env = new IntegrationTestEnv();
  private static final Duration OPERATION_TIMEOUT = Duration.ofMinutes(10);
  private static Database database;
  private static String url;

  @BeforeClass
  public static void setup() throws Exception {
    assumeFalse(
        "Proto columns is not supported in the emulator", EmulatorSpannerHelper.isUsingEmulator());
    RemoteSpannerHelper testHelper = env.getTestHelper();
    final String projectId = testHelper.getInstanceId().getProject();
    final String instanceId = testHelper.getInstanceId().getInstance();
    final String databaseId = testHelper.getUniqueDatabaseId();
    final DatabaseAdminClient databaseAdminClient = testHelper.getClient().getDatabaseAdminClient();
    InputStream in =
        ITProtoColumnsTest.class
            .getClassLoader()
            .getResourceAsStream("com/google/cloud/spanner/jdbc/it/descriptors.pb");
    assertNotNull(in);
    final Database databaseToCreate =
        databaseAdminClient
            .newDatabaseBuilder(DatabaseId.of(projectId, instanceId, databaseId))
            .setDialect(Dialect.GOOGLE_STANDARD_SQL)
            .setProtoDescriptors(in)
            .build();
    final String host = SpannerTestHost.getHost();

    database =
        databaseAdminClient
            .createDatabase(
                databaseToCreate,
                Arrays.asList(
                    "CREATE PROTO BUNDLE ("
                        + "spanner.examples.music.SingerInfo,"
                        + "spanner.examples.music.Genre,"
                        + ")",
                    "CREATE TABLE Singers ("
                        + "  SingerId   INT64 NOT NULL,"
                        + "  FirstName  STRING(1024),"
                        + "  LastName   STRING(1024),"
                        + "  SingerInfo spanner.examples.music.SingerInfo,"
                        + "  SingerGenre spanner.examples.music.Genre,"
                        + "  SingerNationality STRING(1024) AS (SingerInfo.nationality) STORED,"
                        + "  ) PRIMARY KEY (SingerNationality, SingerGenre)",
                    "CREATE TABLE Types ("
                        + "  RowID INT64 NOT NULL,"
                        + "  ProtoMessage    spanner.examples.music.SingerInfo,"
                        + "  ProtoEnum   spanner.examples.music.Genre,"
                        + "  ProtoMessageArray   ARRAY<spanner.examples.music.SingerInfo>,"
                        + "  ProtoEnumArray  ARRAY<spanner.examples.music.Genre>,"
                        + "  ) PRIMARY KEY (RowID)",
                    "CREATE INDEX SingerByNationalityAndGenre ON Singers(SingerNationality, SingerGenre)"
                        + "  STORING (SingerId, FirstName, LastName)"))
            .get(OPERATION_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);

    in.close();
    url = "jdbc:cloudspanner://" + host + "/" + database.getId();
  }

  @AfterClass
  public static void teardown() {
    if (database != null) {
      database.drop();
    }
    ConnectionOptions.closeSpanner();
  }

  @Test
  public void testNonNullElements() throws Exception {
    assumeFalse(
        "Proto columns is not supported in the emulator", EmulatorSpannerHelper.isUsingEmulator());
    SingerInfo singerInfo =
        SingerInfo.newBuilder()
            .setSingerId(1)
            .setNationality("Country1")
            .setGenre(Genre.ROCK)
            .build();
    Genre singerGenre = Genre.ROCK;
    SingerInfo[] singerInfoArray = new SingerInfo[] {singerInfo, null};
    Genre[] singerGenreArray = new Genre[] {Genre.ROCK, null};
    try (Connection connection = DriverManager.getConnection(url);
        PreparedStatement ps =
            connection.prepareStatement(
                "INSERT INTO Types"
                    + " (RowID, ProtoMessage, ProtoEnum, ProtoMessageArray, ProtoEnumArray) VALUES (?, ?, ?, ?, ?)")) {
      ps.setInt(1, 1);
      ps.setObject(2, singerInfo, ProtoMessageType.INSTANCE);
      ps.setObject(3, singerGenre, ProtoEnumType.INSTANCE);
      ps.setArray(4, connection.createArrayOf("PROTO", singerInfoArray));
      ps.setArray(5, connection.createArrayOf("ENUM", singerGenreArray));

      final int updateCount = ps.executeUpdate();
      assertEquals(1, updateCount);
    }

    try (Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Types WHERE RowID = 1")) {

      resultSet.next();
      assertEquals(1, resultSet.getInt("RowID"));
      assertEquals(singerInfo, resultSet.getObject("ProtoMessage", SingerInfo.class));
      assertEquals(singerGenre, resultSet.getObject("ProtoEnum", Genre.class));
      assertArrayEquals(
          singerInfoArray, resultSet.getObject("ProtoMessageArray", SingerInfo[].class));
      assertArrayEquals(singerGenreArray, resultSet.getObject("ProtoEnumArray", Genre[].class));

      assertThat(resultSet.next(), is(false));
    }
  }

  @Test
  public void testNullElements() throws Exception {
    assumeFalse(
        "Proto columns is not supported in the emulator", EmulatorSpannerHelper.isUsingEmulator());
    try (Connection connection = DriverManager.getConnection(url);
        PreparedStatement ps =
            connection.prepareStatement(
                "INSERT INTO Types"
                    + " (RowID, ProtoMessage, ProtoEnum, ProtoMessageArray, ProtoEnumArray) VALUES (?, ?, ?, ?, ?)")) {
      ps.setInt(1, 2);
      ps.setObject(2, null, ProtoMessageType.INSTANCE);
      ps.setObject(3, null, ProtoEnumType.INSTANCE);
      ps.setArray(4, connection.createArrayOf("PROTO", null));
      ps.setArray(5, connection.createArrayOf("ENUM", null));

      final int updateCount = ps.executeUpdate();
      assertEquals(1, updateCount);
    }

    try (Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Types WHERE RowID = 2")) {

      resultSet.next();
      assertEquals(2, resultSet.getInt("RowID"));
      assertNull(resultSet.getObject("ProtoMessage", SingerInfo.class));
      assertNull(resultSet.getObject("ProtoEnum", Genre.class));
      assertNull(resultSet.getObject("ProtoMessageArray", SingerInfo[].class));
      assertNull(resultSet.getObject("ProtoEnumArray", Genre[].class));

      assertThat(resultSet.next(), is(false));
    }
  }

  @Test
  public void testInterCompatibility() throws Exception {
    assumeFalse(
        "Proto columns is not supported in the emulator", EmulatorSpannerHelper.isUsingEmulator());
    SingerInfo singerInfo =
        SingerInfo.newBuilder()
            .setSingerId(1)
            .setNationality("Country1")
            .setGenre(Genre.ROCK)
            .build();
    Genre singerGenre = Genre.ROCK;
    byte[] singerInfoBytes = singerInfo.toByteArray();
    long singerGenreConst = singerGenre.getNumber();
    try (Connection connection = DriverManager.getConnection(url);
        PreparedStatement ps =
            connection.prepareStatement(
                "INSERT INTO Types" + " (RowID, ProtoMessage, ProtoEnum) VALUES (?, ?, ?)")) {
      ps.setInt(1, 3);
      ps.setObject(2, singerInfoBytes);
      ps.setObject(3, singerGenreConst);

      final int updateCount = ps.executeUpdate();
      assertEquals(1, updateCount);
    }

    try (Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Types WHERE RowID = 3")) {

      resultSet.next();
      assertEquals(3, resultSet.getInt("RowID"));
      assertEquals(singerInfo, resultSet.getObject("ProtoMessage", SingerInfo.class));
      assertEquals(singerGenre, resultSet.getObject("ProtoEnum", Genre.class));
      assertArrayEquals(singerInfoBytes, resultSet.getBytes("ProtoMessage"));
      assertEquals(singerGenreConst, resultSet.getInt("ProtoEnum"));

      assertThat(resultSet.next(), is(false));
    }
  }
}
