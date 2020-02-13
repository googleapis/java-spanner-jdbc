# Generic Connection API for Google Cloud Spanner

Generic Connection API for
[Google Cloud Spanner](https://cloud.google.com/spanner/).

ONLY FOR GOOGLE INTERNAL USE

## Quickstart

[//]: # ({x-version-update-start:google-cloud-spanner-connection-api:released})
If you are using Maven, add this to your pom.xml file
```xml
<dependency>
  <groupId>com.google.cloud</groupId>
  <artifactId>google-cloud-spanner-connection-api</artifactId>
  <version>1.0.0</version>
</dependency>
```
If you are using Gradle, add this to your dependencies
```Groovy
compile 'com.google.cloud:google-cloud-spanner-connection-api:1.0.0'
```
If you are using SBT, add this to your dependencies
```Scala
libraryDependencies += "com.google.cloud" % "google-cloud-spanner-connection-api" % "1.0.0"
```
[//]: # ({x-version-update-end})

## Getting Started
You can access Google Cloud Spanner through the Connection API like this:

```java
ConnectionOptions options = ConnectionOptions.newBuilder()
      .setUri("cloudspanner:/projects/my_project_id/instances/my_instance_id/databases/my_database_name?autocommit=false")
      .setCredentialsUrl("/home/cloudspanner-keys/my-key.json")
      .build();
try(Connection connection = options.getConnection()) {
  try(ResultSet rs = connection.executeQuery(Statement.of("SELECT SingerId, AlbumId, MarketingBudget FROM Albums"))) {
    while(rs.next()) {
      // do something
    }
  }
}
```

### Connection URL
The connection URL must be specified in the following format:

```
cloudspanner:[//host[:port]]/projects/project-id[/instances/instance-id[/databases/database-name]][\?property-name=property-value[;property-name=property-value]*]?
```

The property-value strings should be url-encoded.

The project-id part of the URI may be filled with the placeholder DEFAULT_PROJECT_ID. This
placeholder will be replaced by the default project id of the environment that is requesting a
connection.

## Java Versions

Java 7 or above is required for using this Connection API.

## Versioning

This library follows [Semantic Versioning](http://semver.org/).

## Contributing

Contributions to this library are always welcome and highly encouraged.

See `google-cloud`'s [CONTRIBUTING] documentation and the
[shared documentation](https://github.com/googleapis/google-cloud-common/blob/master/contributing/readme.md#how-to-contribute-to-gcloud)
for more information on how to get started.

Please note that this project is released with a Contributor Code of Conduct.
By participating in this project you agree to abide by its terms. See
[Code of Conduct][code-of-conduct] for more information.

## License

Apache 2.0 - See [LICENSE] for more information.


[CONTRIBUTING]:https://github.com/googleapis/google-cloud-java/blob/master/CONTRIBUTING.md
[code-of-conduct]:https://github.com/googleapis/google-cloud-java/blob/master/CODE_OF_CONDUCT.md#contributor-code-of-conduct
[LICENSE]: https://github.com/googleapis/google-cloud-java/blob/master/LICENSE
[cloud-platform]: https://cloud.google.com/

[cloud-spanner]: https://cloud.google.com/spanner/
[cloud-spanner-docs]: https://cloud.google.com/spanner/docs/overview
