# Changelog

## [1.16.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.15.0...v1.16.0) (2020-07-08)


### Features

* publish shaded jar on maven central ([#83](https://www.github.com/googleapis/java-spanner-jdbc/issues/83)) ([2a7c53e](https://www.github.com/googleapis/java-spanner-jdbc/commit/2a7c53e5d503eefc42e7927e58430f8d24fe5b48))
* **deps:** adopt flatten plugin and google-cloud-shared-dependencies ([#162](https://www.github.com/googleapis/java-spanner-jdbc/issues/162)) ([6715a8b](https://www.github.com/googleapis/java-spanner-jdbc/commit/6715a8b24066595036c6228df18cca084a4bb1ad))


### Bug Fixes

* add missing documentation for connection properties ([#155](https://www.github.com/googleapis/java-spanner-jdbc/issues/155)) ([9b53df4](https://www.github.com/googleapis/java-spanner-jdbc/commit/9b53df4ea33926d9fa0955a4445c2ea6790ac3dc)), closes [#152](https://www.github.com/googleapis/java-spanner-jdbc/issues/152)
* ResultSet#get(...) methods should auto convert values ([#143](https://www.github.com/googleapis/java-spanner-jdbc/issues/143)) ([bc7d5bd](https://www.github.com/googleapis/java-spanner-jdbc/commit/bc7d5bd6205b23c99d01d2895ffb5c48ba423ea3))
* return empty catalog name ([#174](https://www.github.com/googleapis/java-spanner-jdbc/issues/174)) ([cedd167](https://www.github.com/googleapis/java-spanner-jdbc/commit/cedd167c5973fe50e0205ae641f6580ebd627884))
* test allowed a too old staleness ([#131](https://www.github.com/googleapis/java-spanner-jdbc/issues/131)) ([8a5e443](https://www.github.com/googleapis/java-spanner-jdbc/commit/8a5e44321b6587e1f719f4189dfe2af3482e47cc))


### Dependencies

* update core dependencies ([#105](https://www.github.com/googleapis/java-spanner-jdbc/issues/105)) ([d7c7095](https://www.github.com/googleapis/java-spanner-jdbc/commit/d7c7095e0f22cd477f56419e8300d67d48eb8484))
* update core dependencies to v1.29.0 ([#121](https://www.github.com/googleapis/java-spanner-jdbc/issues/121)) ([1324769](https://www.github.com/googleapis/java-spanner-jdbc/commit/13247691db249a6bdd56ac1f5b03837ebfb0624f))
* update core dependencies to v1.93.4 ([#111](https://www.github.com/googleapis/java-spanner-jdbc/issues/111)) ([a44b498](https://www.github.com/googleapis/java-spanner-jdbc/commit/a44b498be79189aa3ae2f9a32c4105a41d81922b))
* update core dependencies to v29 (major) ([#114](https://www.github.com/googleapis/java-spanner-jdbc/issues/114)) ([143e6b6](https://www.github.com/googleapis/java-spanner-jdbc/commit/143e6b645fd09b91e9b3d8d1db2e522a04103c1e))
* update dependency com.google.api:api-common to v1.9.0 ([#100](https://www.github.com/googleapis/java-spanner-jdbc/issues/100)) ([dc0793c](https://www.github.com/googleapis/java-spanner-jdbc/commit/dc0793ce0ea3ec1faaebcf59989e0b0977deffcf))
* update dependency com.google.api.grpc:proto-google-common-protos to v1.18.0 ([#128](https://www.github.com/googleapis/java-spanner-jdbc/issues/128)) ([3f00adb](https://www.github.com/googleapis/java-spanner-jdbc/commit/3f00adbe0d1d317dfefe5ec3ee5a0be9fe0f5923))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.2 ([#175](https://www.github.com/googleapis/java-spanner-jdbc/issues/175)) ([a553f79](https://www.github.com/googleapis/java-spanner-jdbc/commit/a553f7919314152583b6aae9a98a450d3c50a8fc))
* update dependency com.google.cloud.samples:shared-configuration to v1.0.13 ([#99](https://www.github.com/googleapis/java-spanner-jdbc/issues/99)) ([63717c2](https://www.github.com/googleapis/java-spanner-jdbc/commit/63717c2fc9cc2b3a43a6b0412fefca7bbfd7e12d))
* update dependency org.threeten:threetenbp to v1.4.3 ([#94](https://www.github.com/googleapis/java-spanner-jdbc/issues/94)) ([cb7229f](https://www.github.com/googleapis/java-spanner-jdbc/commit/cb7229fbbf3e1d71b8a4331eb5ab889af5d4fd31))
* update dependency org.threeten:threetenbp to v1.4.4 ([#124](https://www.github.com/googleapis/java-spanner-jdbc/issues/124)) ([4d3daa4](https://www.github.com/googleapis/java-spanner-jdbc/commit/4d3daa484394f790e07557175aa7311b248da6f7))
* upgrade to latest bom and remove dependency exclusions ([#168](https://www.github.com/googleapis/java-spanner-jdbc/issues/168)) ([291189c](https://www.github.com/googleapis/java-spanner-jdbc/commit/291189cec8e9d166fb8df9d26a6381be9cbded9d))

## [1.15.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.14.0...v1.15.0) (2020-03-24)


### Features

* add support for QueryOptions ([#76](https://www.github.com/googleapis/java-spanner-jdbc/issues/76)) ([b3f4cf7](https://www.github.com/googleapis/java-spanner-jdbc/commit/b3f4cf7852a2fd5f22660cc3f25a6253b9a118ab))


### Dependencies

* update spanner.version to v1.52.0 ([#95](https://www.github.com/googleapis/java-spanner-jdbc/issues/95)) ([cdf9d30](https://www.github.com/googleapis/java-spanner-jdbc/commit/cdf9d30e8ca387d87a6ffe00fa09818d135547f4))

## [1.14.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.13.0...v1.14.0) (2020-03-18)


### Features

* add support for foreign keys ([#78](https://www.github.com/googleapis/java-spanner-jdbc/issues/78)) ([9e770f2](https://www.github.com/googleapis/java-spanner-jdbc/commit/9e770f281c03a1e9c034e5ff3ddee44fa20a7b30)), closes [#77](https://www.github.com/googleapis/java-spanner-jdbc/issues/77)


### Bug Fixes

* add missing netty-shaded lib for über-jar ([#80](https://www.github.com/googleapis/java-spanner-jdbc/issues/80)) ([3d6f356](https://www.github.com/googleapis/java-spanner-jdbc/commit/3d6f35669671194e6772fe327ce48f27e5bf4643))
* fix deprecation warnings in JDBC (test) files ([#81](https://www.github.com/googleapis/java-spanner-jdbc/issues/81)) ([a5e031d](https://www.github.com/googleapis/java-spanner-jdbc/commit/a5e031d3183f8fe88a621500f235ca2b0242f50b))
* include Spanner gRPC test dependencies ([#63](https://www.github.com/googleapis/java-spanner-jdbc/issues/63)) ([a34bfc0](https://www.github.com/googleapis/java-spanner-jdbc/commit/a34bfc0ff1c2ddeef077dbfae4c56bdd53febcb2))


### Dependencies

* update core dependencies ([1ae098e](https://www.github.com/googleapis/java-spanner-jdbc/commit/1ae098e924c2a488cfddd0a3aee9511274b7a515))
* update core dependencies ([#40](https://www.github.com/googleapis/java-spanner-jdbc/issues/40)) ([18c3a1b](https://www.github.com/googleapis/java-spanner-jdbc/commit/18c3a1b069cb507a91d0320e64a8bf8ae8efe394))
* update core dependencies ([#73](https://www.github.com/googleapis/java-spanner-jdbc/issues/73)) ([cfa1539](https://www.github.com/googleapis/java-spanner-jdbc/commit/cfa153997599c36f1243e87f1ea0760694657dfe))
* update core dependencies to v1.27.1 ([#61](https://www.github.com/googleapis/java-spanner-jdbc/issues/61)) ([181991b](https://www.github.com/googleapis/java-spanner-jdbc/commit/181991bda1f66de707d27dad9658b9177626595a))
* update core dependencies to v1.27.2 ([#71](https://www.github.com/googleapis/java-spanner-jdbc/issues/71)) ([12425fc](https://www.github.com/googleapis/java-spanner-jdbc/commit/12425fcb4382449e4a7a0edad4c812b7ce15aa71))
* update core dependencies to v1.54.0 ([#72](https://www.github.com/googleapis/java-spanner-jdbc/issues/72)) ([5676021](https://www.github.com/googleapis/java-spanner-jdbc/commit/567602177e05fa198eaa011fbca05cfe4b72fb13))
* update core dependencies to v1.92.5 ([#53](https://www.github.com/googleapis/java-spanner-jdbc/issues/53)) ([604ee2b](https://www.github.com/googleapis/java-spanner-jdbc/commit/604ee2b75204ad52eaf724c3fb71e8c13540af7c))
* update core transport dependencies to v1.34.1 ([#43](https://www.github.com/googleapis/java-spanner-jdbc/issues/43)) ([2b6f04d](https://www.github.com/googleapis/java-spanner-jdbc/commit/2b6f04da3aeebac778fb664c4564fb8b58bf3be4))
* update core transport dependencies to v1.34.2 ([#62](https://www.github.com/googleapis/java-spanner-jdbc/issues/62)) ([8739015](https://www.github.com/googleapis/java-spanner-jdbc/commit/8739015f62289adb92fd55b19a5bff8762da20a9))
* update dependency com.google.api-client:google-api-client-bom to v1.30.8 ([#46](https://www.github.com/googleapis/java-spanner-jdbc/issues/46)) ([ef891b0](https://www.github.com/googleapis/java-spanner-jdbc/commit/ef891b000045d1f39f91b6a0ed3abaab19c5f05e))
* update dependency com.google.api-client:google-api-client-bom to v1.30.9 ([#74](https://www.github.com/googleapis/java-spanner-jdbc/issues/74)) ([3b62299](https://www.github.com/googleapis/java-spanner-jdbc/commit/3b622999b9f9645a6086e5efd3206f4d7b0806bc))
* update dependency com.google.truth:truth to v1.0.1 ([#32](https://www.github.com/googleapis/java-spanner-jdbc/issues/32)) ([5205863](https://www.github.com/googleapis/java-spanner-jdbc/commit/52058636e10951e883523204f0f161db8a972d62))
* update protobuf.version to v3.11.3 ([#48](https://www.github.com/googleapis/java-spanner-jdbc/issues/48)) ([0779fcb](https://www.github.com/googleapis/java-spanner-jdbc/commit/0779fcb0bfe935c3c302fa8442f733c7e3629761))
* update protobuf.version to v3.11.4 ([#64](https://www.github.com/googleapis/java-spanner-jdbc/issues/64)) ([f485cff](https://www.github.com/googleapis/java-spanner-jdbc/commit/f485cfffa0de27ce35f5d16c689c31c6ea22138e))
* update spanner.version to v1.51.0 ([#75](https://www.github.com/googleapis/java-spanner-jdbc/issues/75)) ([4fff168](https://www.github.com/googleapis/java-spanner-jdbc/commit/4fff168eae61fb55933cf3afd67f24ca65dfde54))

## [1.13.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/1.12.0...v1.13.0) (2020-01-28)


### Features

* allow using existing OAuth token for JDBC connection ([#37](https://www.github.com/googleapis/java-spanner-jdbc/issues/37)) ([b368b84](https://www.github.com/googleapis/java-spanner-jdbc/commit/b368b8407b3e2884458d956a9207cb4e12e37848)), closes [#29](https://www.github.com/googleapis/java-spanner-jdbc/issues/29)


### Bug Fixes

* add support for WITH clauses ([#42](https://www.github.com/googleapis/java-spanner-jdbc/issues/42)) ([7f4bea4](https://www.github.com/googleapis/java-spanner-jdbc/commit/7f4bea43c42df68258f776944ea744069a1a218e))
* allow dots and colons in project id ([#36](https://www.github.com/googleapis/java-spanner-jdbc/issues/36)) ([5957008](https://www.github.com/googleapis/java-spanner-jdbc/commit/59570085403fa7002616dd535df4666a384c3438)), closes [#33](https://www.github.com/googleapis/java-spanner-jdbc/issues/33)


### Dependencies

* update core dependencies ([#25](https://www.github.com/googleapis/java-spanner-jdbc/issues/25)) ([9f4f4ad](https://www.github.com/googleapis/java-spanner-jdbc/commit/9f4f4ad1b076bd3131296c9f7f6558f2bc885d42))
* update dependency org.threeten:threetenbp to v1.4.1 ([7cc951b](https://www.github.com/googleapis/java-spanner-jdbc/commit/7cc951b340b072e7853df868aaf7c17f854a69f5))
