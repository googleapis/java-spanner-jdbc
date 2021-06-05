# Google Google Cloud Spanner JDBC Client for Java

Java idiomatic client for [Google Cloud Spanner JDBC][product-docs].

[![Maven][maven-version-image]][maven-version-link]
![Stability][stability-image]

- [Product Documentation][product-docs]
- [Client Library Documentation][javadocs]

## Quickstart


If you are using Maven, add this to your pom.xml file:

```xml
<dependency>
  <groupId>com.google.cloud</groupId>
  <artifactId>google-cloud-spanner-jdbc</artifactId>
  <version>2.2.1</version>
</dependency>
```

If you are using Gradle without BOM, add this to your dependencies
```Groovy
compile 'com.google.cloud:google-cloud-spanner-jdbc:2.2.1'
```

If you are using SBT, add this to your dependencies
```Scala
libraryDependencies += "com.google.cloud" % "google-cloud-spanner-jdbc" % "2.2.1"
```

## Authentication

See the [Authentication][authentication] section in the base directory's README.

## Getting Started

### Prerequisites

You will need a [Google Cloud Platform Console][developer-console] project with the Google Cloud Spanner JDBC [API enabled][enable-api].

[Follow these instructions][create-project] to get your project set up. You will also need to set up the local development environment by
[installing the Google Cloud SDK][cloud-sdk] and running the following commands in command line:
`gcloud auth login` and `gcloud config set project [YOUR PROJECT ID]`.

### Installation and setup

You'll need to obtain the `google-cloud-spanner-jdbc` library.  See the [Quickstart](#quickstart) section
to add `google-cloud-spanner-jdbc` as a dependency in your code.

## About Google Cloud Spanner JDBC


[Google Cloud Spanner JDBC][product-docs] 

See the [Google Cloud Spanner JDBC client library docs][javadocs] to learn how to
use this Google Cloud Spanner JDBC Client Library.


### Creating a JDBC Connection

The following example shows how to create a JDBC connection to Cloud Spanner and execute a simple query.

```java
String projectId = "my-project";
String instanceId = "my-instance";
String databaseId = "my-database";

try (Connection connection =
    DriverManager.getConnection(
        String.format(
            "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s",
            projectId, instanceId, databaseId))) {
  try (Statement statement = connection.createStatement()) {
    try (ResultSet rs = statement.executeQuery("SELECT CURRENT_TIMESTAMP()")) {
      while (rs.next()) {
        System.out.printf(
            "Connected to Cloud Spanner at [%s]%n", rs.getTimestamp(1).toString());
      }
    }
  }
}
```

### Connection URL Properties

The Cloud Spanner JDBC driver supports the following connection URL properties. Note that all of
these can also be supplied in a Properties instance that is passed to the
`DriverManager#getConnection(String url, Properties properties)` method.

#### Commonly Used Properties
- credentials (String): URL for the credentials file to use for the connection. If you do not specify any credentials at all, the default credentials of the environment as returned by `GoogleCredentials#getApplicationDefault()` is used. Example: `jdbc:cloudspanner:/projects/my-project/instances/my-instance/databases/my-db;credentials=/path/to/credentials.json`
- autocommit (boolean): Sets the initial autocommit mode for the connection. Default is true.
- readonly (boolean): Sets the initial readonly mode for the connection. Default is false.
- autoConfigEmulator (boolean): Automatically configure the connection to try to connect to the Cloud Spanner emulator. You do not need to specify any host or port in the connection string as long as the emulator is running on the default host/port (localhost:9010). The instance and database in the connection string will automatically be created if these do not yet exist on the emulator. This means that you do not need to execute any `gcloud` commands on the emulator to create the instance and database before you can connect to it. Example: `jdbc:cloudspanner:/projects/test-project/instances/test-instance/databases/test-db;autoConfigEmulator=true`
- usePlainText (boolean): Sets whether the JDBC connection should establish an unencrypted connection to a (local) server. This option can only be used when connecting to a local emulator that does not require an encrypted connection, and that does not require authentication. Example: `jdbc:cloudspanner://localhost:9010/projects/test-project/instances/test-instance/databases/test-db;usePlainText=true`
- optimizerVersion (String): Sets the default query optimizer version to use for this connection. See also https://cloud.google.com/spanner/docs/query-optimizer/query-optimizer-versions.

#### Advanced Properties
- minSessions (int): Sets the minimum number of sessions in the backing session pool. Defaults to 100.
- maxSessions (int): Sets the maximum number of sessions in the backing session pool. Defaults to 400.
- numChannels (int): Sets the number of gRPC channels to use. Defaults to 4.
- retryAbortsInternally (boolean): The JDBC driver will by default automatically retry aborted transactions internally. This is done by keeping track of all statements and the results of these during a transaction, and if the transaction is aborted by Cloud Spanner, it will replay the statements on a new transaction and compare the results with the initial attempt. Disable this option if you want to handle aborted transactions in your own application.
- oauthToken (string): A valid pre-existing OAuth token to use for authentication for this connection. Setting this property will take precedence over any value set for a credentials file.
- lenient (boolean): Enable this to force the JDBC driver to ignore unknown properties in the connection URL. Some applications automatically add additional properties to the URL that are not recognized by the JDBC driver. Normally, the JDBC driver will reject this, unless `lenient` mode is enabled.

### Jar with Dependencies
A single jar with all dependencies can be downloaded from https://repo1.maven.org/maven2/com/google/cloud/google-cloud-spanner-jdbc/latest
or be built with the command `mvn package` (select the jar that is named `google-cloud-spanner-jdbc-<version>-single-jar-with-dependencies.jar`).

### Creating a Shaded Jar

A jar with all dependencies included is automatically generated when you execute `mvn package`.
The dependencies in this jar are not shaded. To create a jar with shaded dependencies you must
activate the `shade` profile like this:

 ```
 mvn package -Pshade
 ```





## Troubleshooting

To get help, follow the instructions in the [shared Troubleshooting document][troubleshooting].

## Java Versions

Java 8 or above is required for using this client.

## Versioning


This library follows [Semantic Versioning](http://semver.org/).


## Contributing


Contributions to this library are always welcome and highly encouraged.

See [CONTRIBUTING][contributing] for more information how to get started.

Please note that this project is released with a Contributor Code of Conduct. By participating in
this project you agree to abide by its terms. See [Code of Conduct][code-of-conduct] for more
information.

## License

Apache 2.0 - See [LICENSE][license] for more information.

## CI Status

Java Version | Status
------------ | ------
Java 8 | [![Kokoro CI][kokoro-badge-image-2]][kokoro-badge-link-2]
Java 8 OSX | [![Kokoro CI][kokoro-badge-image-3]][kokoro-badge-link-3]
Java 8 Windows | [![Kokoro CI][kokoro-badge-image-4]][kokoro-badge-link-4]
Java 11 | [![Kokoro CI][kokoro-badge-image-5]][kokoro-badge-link-5]

Java is a registered trademark of Oracle and/or its affiliates.

[product-docs]: https://cloud.google.com/spanner/docs/use-oss-jdbc
[javadocs]: https://googleapis.dev/java/google-cloud-spanner-jdbc/latest/index.html
[kokoro-badge-image-1]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java7.svg
[kokoro-badge-link-1]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java7.html
[kokoro-badge-image-2]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java8.svg
[kokoro-badge-link-2]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java8.html
[kokoro-badge-image-3]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java8-osx.svg
[kokoro-badge-link-3]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java8-osx.html
[kokoro-badge-image-4]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java8-win.svg
[kokoro-badge-link-4]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java8-win.html
[kokoro-badge-image-5]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java11.svg
[kokoro-badge-link-5]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-spanner-jdbc/java11.html
[stability-image]: https://img.shields.io/badge/stability-ga-green
[maven-version-image]: https://img.shields.io/maven-central/v/com.google.cloud/google-cloud-spanner-jdbc.svg
[maven-version-link]: https://search.maven.org/search?q=g:com.google.cloud%20AND%20a:google-cloud-spanner-jdbc&core=gav
[authentication]: https://github.com/googleapis/google-cloud-java#authentication
[developer-console]: https://console.developers.google.com/
[create-project]: https://cloud.google.com/resource-manager/docs/creating-managing-projects
[cloud-sdk]: https://cloud.google.com/sdk/
[troubleshooting]: https://github.com/googleapis/google-cloud-common/blob/master/troubleshooting/readme.md#troubleshooting
[contributing]: https://github.com/googleapis/java-spanner-jdbc/blob/master/CONTRIBUTING.md
[code-of-conduct]: https://github.com/googleapis/java-spanner-jdbc/blob/master/CODE_OF_CONDUCT.md#contributor-code-of-conduct
[license]: https://github.com/googleapis/java-spanner-jdbc/blob/master/LICENSE


[libraries-bom]: https://github.com/GoogleCloudPlatform/cloud-opensource-java/wiki/The-Google-Cloud-Platform-Libraries-BOM
[shell_img]: https://gstatic.com/cloudssh/images/open-btn.png
