# Changelog

## [2.19.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.19.0...v2.19.1) (2024-06-05)


### Bug Fixes

* Cleanup unused methods ([#1635](https://github.com/googleapis/java-spanner-jdbc/issues/1635)) ([ad0a35c](https://github.com/googleapis/java-spanner-jdbc/commit/ad0a35c82fd880ce5b705f4cb749c35664ccc604))


### Dependencies

* Update dependency com.google.cloud:sdk-platform-java-config to v3.31.0 ([#1630](https://github.com/googleapis/java-spanner-jdbc/issues/1630)) ([98b530d](https://github.com/googleapis/java-spanner-jdbc/commit/98b530dce7a6131a562afdfca6b8b7c73fb83d7a))
* Update dependency org.mybatis.dynamic-sql:mybatis-dynamic-sql to v1.5.2 ([#1633](https://github.com/googleapis/java-spanner-jdbc/issues/1633)) ([7c62ee3](https://github.com/googleapis/java-spanner-jdbc/commit/7c62ee30e81a29654934d1f77f36816e92b3470d))

## [2.19.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.19.0...v2.19.1) (2024-06-01)


### Dependencies

* Update dependency com.google.cloud:sdk-platform-java-config to v3.31.0 ([#1630](https://github.com/googleapis/java-spanner-jdbc/issues/1630)) ([98b530d](https://github.com/googleapis/java-spanner-jdbc/commit/98b530dce7a6131a562afdfca6b8b7c73fb83d7a))

## [2.19.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.18.1...v2.19.0) (2024-05-31)


### Features

* Add Proto Columns support in JDBC ([#1252](https://github.com/googleapis/java-spanner-jdbc/issues/1252)) ([3efa9ac](https://github.com/googleapis/java-spanner-jdbc/commit/3efa9ac7906bab35d72f2951b9945a873f48f013))


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.68.0 ([#1620](https://github.com/googleapis/java-spanner-jdbc/issues/1620)) ([255eeef](https://github.com/googleapis/java-spanner-jdbc/commit/255eeefa1ce682a46a4d0467ce297acfb40b0d25))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.68.1 ([#1624](https://github.com/googleapis/java-spanner-jdbc/issues/1624)) ([f4a83ba](https://github.com/googleapis/java-spanner-jdbc/commit/f4a83ba0d4702caa433a8390ff75c955d7669b17))
* Update dependency org.springframework.boot:spring-boot-starter-data-jdbc to v3.3.0 ([#1616](https://github.com/googleapis/java-spanner-jdbc/issues/1616)) ([6912711](https://github.com/googleapis/java-spanner-jdbc/commit/691271199bed294f5ba272285282be975e8bbead))
* Update dependency org.springframework.boot:spring-boot-starter-parent to v3.3.0 ([#1617](https://github.com/googleapis/java-spanner-jdbc/issues/1617)) ([155d6c6](https://github.com/googleapis/java-spanner-jdbc/commit/155d6c65c4ae3c9306ce86864bb4a4b4c9569473))

## [2.18.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.18.0...v2.18.1) (2024-05-22)


### Dependencies

* Remove open-telemetry dependency import ([#1608](https://github.com/googleapis/java-spanner-jdbc/issues/1608)) ([fcb32ef](https://github.com/googleapis/java-spanner-jdbc/commit/fcb32efb4945807e0933341874644f042b7f33af))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.67.0 ([#1613](https://github.com/googleapis/java-spanner-jdbc/issues/1613)) ([12080e0](https://github.com/googleapis/java-spanner-jdbc/commit/12080e0579269fd9b893440d2f6ff3e784fb52ec))
* Update dependency com.google.cloud:sdk-platform-java-config to v3.30.1 ([#1609](https://github.com/googleapis/java-spanner-jdbc/issues/1609)) ([46aed65](https://github.com/googleapis/java-spanner-jdbc/commit/46aed65c39cd9c6e60733fdf9cfd11f770e77019))
* Update dependency io.opentelemetry:opentelemetry-bom to v1.38.0 ([#1606](https://github.com/googleapis/java-spanner-jdbc/issues/1606)) ([b45bed5](https://github.com/googleapis/java-spanner-jdbc/commit/b45bed575bc53898205aa183d7d8554b336a68f9))
* Update dependency org.springframework.data:spring-data-bom to v2023.1.6 ([#1610](https://github.com/googleapis/java-spanner-jdbc/issues/1610)) ([aac170d](https://github.com/googleapis/java-spanner-jdbc/commit/aac170db26f8018b44f97d17f92faecb11117269))
* Update dependency org.springframework.data:spring-data-bom to v2024 ([#1611](https://github.com/googleapis/java-spanner-jdbc/issues/1611)) ([6669c7a](https://github.com/googleapis/java-spanner-jdbc/commit/6669c7af373160fa9396a6dbcf2adff091d3c46a))
* Update dependency org.testcontainers:testcontainers to v1.19.8 ([#1604](https://github.com/googleapis/java-spanner-jdbc/issues/1604)) ([e155a46](https://github.com/googleapis/java-spanner-jdbc/commit/e155a4624a021cb7959e72f7eb138ff8c685eaef))

## [2.18.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.17.1...v2.18.0) (2024-05-04)


### Features

* Allow DDL with autocommit=false ([#1600](https://github.com/googleapis/java-spanner-jdbc/issues/1600)) ([a61c25d](https://github.com/googleapis/java-spanner-jdbc/commit/a61c25d8f90460ec507b383dbaee0ca686104ba8))
* Support concurrent transactions on the emulator ([#1601](https://github.com/googleapis/java-spanner-jdbc/issues/1601)) ([7123991](https://github.com/googleapis/java-spanner-jdbc/commit/71239912a8078569dcd985314810131e593c0ed7))


### Bug Fixes

* ClassCastException in Spring Data JDBC sample ([#1595](https://github.com/googleapis/java-spanner-jdbc/issues/1595)) ([e96a86a](https://github.com/googleapis/java-spanner-jdbc/commit/e96a86a4b82ac4b47bd1ce25e810f01299597339))


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.66.0 ([#1599](https://github.com/googleapis/java-spanner-jdbc/issues/1599)) ([84ea11a](https://github.com/googleapis/java-spanner-jdbc/commit/84ea11ac27635dbe6fb101134767d14488dde8c2))
* Update dependency com.google.cloud:sdk-platform-java-config to v3.30.0 ([#1597](https://github.com/googleapis/java-spanner-jdbc/issues/1597)) ([40a7212](https://github.com/googleapis/java-spanner-jdbc/commit/40a721237c79882e55d86d48402c64cc09782522))
* Update dependency org.mybatis.dynamic-sql:mybatis-dynamic-sql to v1.5.1 ([#1596](https://github.com/googleapis/java-spanner-jdbc/issues/1596)) ([f54beb2](https://github.com/googleapis/java-spanner-jdbc/commit/f54beb20d6bbe3f4974385c4758ba77d31d25ce3))

## [2.17.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.17.0...v2.17.1) (2024-04-30)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.65.1 ([#1588](https://github.com/googleapis/java-spanner-jdbc/issues/1588)) ([1b1218a](https://github.com/googleapis/java-spanner-jdbc/commit/1b1218adaa25ecec3def66b7031c7f0e5e8c23b0))

## [2.17.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.16.3...v2.17.0) (2024-04-21)


### Features

* Concurrent transactions on emulator ([#1578](https://github.com/googleapis/java-spanner-jdbc/issues/1578)) ([0234fb3](https://github.com/googleapis/java-spanner-jdbc/commit/0234fb3eb43695c657b845946e6d1122827dcae5))
* Support endpoint connection URL property ([#1582](https://github.com/googleapis/java-spanner-jdbc/issues/1582)) ([b589c96](https://github.com/googleapis/java-spanner-jdbc/commit/b589c96a3187390e9ffa576d1e0ee285e223c559))
* Support max_commit_delay ([#1581](https://github.com/googleapis/java-spanner-jdbc/issues/1581)) ([06e43c0](https://github.com/googleapis/java-spanner-jdbc/commit/06e43c05a65f25da9a4dbff73e2e75c1b5ec155b))
* Support statement tags in hints ([#1579](https://github.com/googleapis/java-spanner-jdbc/issues/1579)) ([0c3aec1](https://github.com/googleapis/java-spanner-jdbc/commit/0c3aec1a5eec212e9ecacc7bec7f2a8980c9af78))

## [2.16.3](https://github.com/googleapis/java-spanner-jdbc/compare/v2.16.2...v2.16.3) (2024-04-20)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.65.0 ([#1573](https://github.com/googleapis/java-spanner-jdbc/issues/1573)) ([ecc18f1](https://github.com/googleapis/java-spanner-jdbc/commit/ecc18f1307933bb0f4f576ce2df81af226f8a2cb))

## [2.16.2](https://github.com/googleapis/java-spanner-jdbc/compare/v2.16.1...v2.16.2) (2024-04-19)


### Bug Fixes

* Release ResultSet on Statement#close()  ([#1567](https://github.com/googleapis/java-spanner-jdbc/issues/1567)) ([2258ae3](https://github.com/googleapis/java-spanner-jdbc/commit/2258ae3331a7e89036a202f243b9284108301fc0))


### Dependencies

* Update actions/checkout action to v4 ([#1547](https://github.com/googleapis/java-spanner-jdbc/issues/1547)) ([736e3af](https://github.com/googleapis/java-spanner-jdbc/commit/736e3afa54149dd11803bd715569afd9ec8e87f2))
* Update actions/checkout action to v4 ([#1561](https://github.com/googleapis/java-spanner-jdbc/issues/1561)) ([6053d79](https://github.com/googleapis/java-spanner-jdbc/commit/6053d79816546130eca7a7016dc9299c079e411f))
* Update actions/checkout digest to b4ffde6 ([#1546](https://github.com/googleapis/java-spanner-jdbc/issues/1546)) ([18c5ad4](https://github.com/googleapis/java-spanner-jdbc/commit/18c5ad4d4124f095547d50c0d2e154bc06380642))
* Update actions/github-script action to v7 ([#1548](https://github.com/googleapis/java-spanner-jdbc/issues/1548)) ([d1d422c](https://github.com/googleapis/java-spanner-jdbc/commit/d1d422cdf0a74231c468262662fdf5ce4d27b8ef))
* Update actions/setup-java action to v4 ([#1549](https://github.com/googleapis/java-spanner-jdbc/issues/1549)) ([cb2b911](https://github.com/googleapis/java-spanner-jdbc/commit/cb2b911b0b332e97f85974ec880a5ab7a12a7578))
* Update actions/setup-java action to v4 ([#1563](https://github.com/googleapis/java-spanner-jdbc/issues/1563)) ([01d4de1](https://github.com/googleapis/java-spanner-jdbc/commit/01d4de1df21144e9c3bcf0b4e5192b12cd19dc82))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.63.0 ([#1552](https://github.com/googleapis/java-spanner-jdbc/issues/1552)) ([ac75b9f](https://github.com/googleapis/java-spanner-jdbc/commit/ac75b9faf0eaeb499428ecefda1f3285b3d28e67))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.64.0 ([#1565](https://github.com/googleapis/java-spanner-jdbc/issues/1565)) ([b57662f](https://github.com/googleapis/java-spanner-jdbc/commit/b57662fb65b74b329103ef63265192d7026b2c2d))
* Update dependency com.google.cloud:sdk-platform-java-config to v3.28.1 ([#1560](https://github.com/googleapis/java-spanner-jdbc/issues/1560)) ([afcbe5e](https://github.com/googleapis/java-spanner-jdbc/commit/afcbe5ea5701a799729543c9564759570f05feb8))
* Update dependency com.google.cloud:sdk-platform-java-config to v3.29.0 ([#1572](https://github.com/googleapis/java-spanner-jdbc/issues/1572)) ([3d43707](https://github.com/googleapis/java-spanner-jdbc/commit/3d437076f2c699e261daf7dcb470085765dba14f))
* Update dependency io.opentelemetry:opentelemetry-bom to v1.37.0 ([#1562](https://github.com/googleapis/java-spanner-jdbc/issues/1562)) ([22f766f](https://github.com/googleapis/java-spanner-jdbc/commit/22f766f098944c23084776c70dbd9dba21efa59c))
* Update dependency org.springframework.boot:spring-boot-starter-data-jdbc to v3.2.5 ([#1569](https://github.com/googleapis/java-spanner-jdbc/issues/1569)) ([784ac1e](https://github.com/googleapis/java-spanner-jdbc/commit/784ac1e68ac29628fe55d7b9e772326f10ffeaec))
* Update dependency org.springframework.boot:spring-boot-starter-parent to v3.2.5 ([#1570](https://github.com/googleapis/java-spanner-jdbc/issues/1570)) ([f54d4dd](https://github.com/googleapis/java-spanner-jdbc/commit/f54d4dd1211508785cb899e0a3c9b585c0908421))
* Update dependency org.springframework.data:spring-data-bom to v2023.1.5 ([#1564](https://github.com/googleapis/java-spanner-jdbc/issues/1564)) ([dbbcca3](https://github.com/googleapis/java-spanner-jdbc/commit/dbbcca342a83476b1f942aab23f21469cf6c8304))
* Update stcarolas/setup-maven action to v5 ([#1550](https://github.com/googleapis/java-spanner-jdbc/issues/1550)) ([121d08e](https://github.com/googleapis/java-spanner-jdbc/commit/121d08e16db0bbb1f6041a201d620829e7121f4d))


### Documentation

* Create samples for quickstart guide ([#1536](https://github.com/googleapis/java-spanner-jdbc/issues/1536)) ([194c820](https://github.com/googleapis/java-spanner-jdbc/commit/194c8205dee9cc4144b18e219df43027b9f15cf2))

## [2.16.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.16.0...v2.16.1) (2024-03-22)


### Dependencies

* Bump Spanner client to 6.62.0 ([#1539](https://github.com/googleapis/java-spanner-jdbc/issues/1539)) ([ca274fb](https://github.com/googleapis/java-spanner-jdbc/commit/ca274fb22cbbb974fe71fffbe9f8c3a56f40628c))
* Update dependency com.google.cloud:sdk-platform-java-config to v3.28.1 ([#1537](https://github.com/googleapis/java-spanner-jdbc/issues/1537)) ([4d1d38e](https://github.com/googleapis/java-spanner-jdbc/commit/4d1d38ea4312d637d1cad61b8230d12cdbf0ba51))
* Update dependency org.postgresql:postgresql to v42.7.3 ([#1532](https://github.com/googleapis/java-spanner-jdbc/issues/1532)) ([b09da60](https://github.com/googleapis/java-spanner-jdbc/commit/b09da609950b94157564f4f9987a3ce60221568e))
* Update dependency org.springframework.boot:spring-boot-starter-data-jdbc to v3.2.4 ([#1540](https://github.com/googleapis/java-spanner-jdbc/issues/1540)) ([21faff8](https://github.com/googleapis/java-spanner-jdbc/commit/21faff8d72ad209835971162caadb694c602175d))
* Update dependency org.springframework.boot:spring-boot-starter-parent to v3.2.4 ([#1541](https://github.com/googleapis/java-spanner-jdbc/issues/1541)) ([2c76488](https://github.com/googleapis/java-spanner-jdbc/commit/2c76488e5e80ecad2824311ed399142495bfc2e4))
* Update dependency org.springframework.data:spring-data-bom to v2023.1.4 ([#1533](https://github.com/googleapis/java-spanner-jdbc/issues/1533)) ([ec7d3b0](https://github.com/googleapis/java-spanner-jdbc/commit/ec7d3b04fd0bf97ea17ebd1849d02fdfdb31ded3))

## [2.16.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.15.5...v2.16.0) (2024-03-07)


### Features

* Support float32 ([#1518](https://github.com/googleapis/java-spanner-jdbc/issues/1518)) ([635ac41](https://github.com/googleapis/java-spanner-jdbc/commit/635ac41e054814cf3b58d37cbc42b01ac183b2a1))


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.27.0 ([#1521](https://github.com/googleapis/java-spanner-jdbc/issues/1521)) ([a8eecfb](https://github.com/googleapis/java-spanner-jdbc/commit/a8eecfb3a731505ba309b4a359dea7b88990c88a))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.60.1 ([#1514](https://github.com/googleapis/java-spanner-jdbc/issues/1514)) ([cf8fe9e](https://github.com/googleapis/java-spanner-jdbc/commit/cf8fe9eea2423f64867c9ecd790916b81165e575))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.61.0 ([#1523](https://github.com/googleapis/java-spanner-jdbc/issues/1523)) ([0c1e281](https://github.com/googleapis/java-spanner-jdbc/commit/0c1e28177131f30405b09cf38fa7a78645a3508a))
* Update dependency com.google.cloud:sdk-platform-java-config to v3.27.0 ([#1522](https://github.com/googleapis/java-spanner-jdbc/issues/1522)) ([28986f9](https://github.com/googleapis/java-spanner-jdbc/commit/28986f9dddabfe2d89b2e3155885434413c3941e))
* Update dependency com.spotify.fmt:fmt-maven-plugin to v2.23 ([#1527](https://github.com/googleapis/java-spanner-jdbc/issues/1527)) ([1e7a4f7](https://github.com/googleapis/java-spanner-jdbc/commit/1e7a4f73339479134868206730c275fe752e8d0d))
* Update dependency org.springframework.boot:spring-boot-starter-data-jdbc to v3.2.3 ([#1512](https://github.com/googleapis/java-spanner-jdbc/issues/1512)) ([e5825c9](https://github.com/googleapis/java-spanner-jdbc/commit/e5825c9a4aa9df68b1ca911430ef37cb6d3549c4))
* Update dependency org.springframework.boot:spring-boot-starter-parent to v3.2.3 ([#1513](https://github.com/googleapis/java-spanner-jdbc/issues/1513)) ([8cfaa1a](https://github.com/googleapis/java-spanner-jdbc/commit/8cfaa1a523c32ebed307f747fd939bd900c28b34))

## [2.15.5](https://github.com/googleapis/java-spanner-jdbc/compare/v2.15.4...v2.15.5) (2024-02-21)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.60.0 ([#1508](https://github.com/googleapis/java-spanner-jdbc/issues/1508)) ([0f7e59c](https://github.com/googleapis/java-spanner-jdbc/commit/0f7e59c7aabdb9fe69fde23f35c5451d19332076))
* Update dependency org.postgresql:postgresql to v42.7.2 ([#1503](https://github.com/googleapis/java-spanner-jdbc/issues/1503)) ([dd5142f](https://github.com/googleapis/java-spanner-jdbc/commit/dd5142faab667817b9fe6bff7f0388c5e43e7dee))
* Update dependency org.postgresql:postgresql to v42.7.2 [security] ([#1507](https://github.com/googleapis/java-spanner-jdbc/issues/1507)) ([caacd05](https://github.com/googleapis/java-spanner-jdbc/commit/caacd056f245f1cf57119653627b0c5097730f41))
* Update dependency org.springframework.data:spring-data-bom to v2023.1.3 ([#1499](https://github.com/googleapis/java-spanner-jdbc/issues/1499)) ([34151b6](https://github.com/googleapis/java-spanner-jdbc/commit/34151b6cae097c97fc49839e2594f5adbd6393c3))

## [2.15.4](https://github.com/googleapis/java-spanner-jdbc/compare/v2.15.3...v2.15.4) (2024-02-15)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.25.0 ([#1492](https://github.com/googleapis/java-spanner-jdbc/issues/1492)) ([21988ab](https://github.com/googleapis/java-spanner-jdbc/commit/21988ab03ddff378caf536eccf94289bb745434c))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.58.0 ([#1487](https://github.com/googleapis/java-spanner-jdbc/issues/1487)) ([c4889fa](https://github.com/googleapis/java-spanner-jdbc/commit/c4889fa9ba8d9527cd2a54488a06cde7ef94266e))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.59.0 ([#1495](https://github.com/googleapis/java-spanner-jdbc/issues/1495)) ([091170b](https://github.com/googleapis/java-spanner-jdbc/commit/091170b80a4cd95f36eaa8ae7a9d2f6c33f9b862))
* Update dependency com.google.cloud:sdk-platform-java-config to v3.25.0 ([#1493](https://github.com/googleapis/java-spanner-jdbc/issues/1493)) ([0e1694f](https://github.com/googleapis/java-spanner-jdbc/commit/0e1694f573268b6c1c67be2d683afcbbd7cd6c61))

## [2.15.3](https://github.com/googleapis/java-spanner-jdbc/compare/v2.15.2...v2.15.3) (2024-02-09)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.24.0 ([#1481](https://github.com/googleapis/java-spanner-jdbc/issues/1481)) ([e3f58d1](https://github.com/googleapis/java-spanner-jdbc/commit/e3f58d1cad8806e120dda99e16536c7bbf837fe4))

## [2.15.2](https://github.com/googleapis/java-spanner-jdbc/compare/v2.15.1...v2.15.2) (2024-01-29)


### Dependencies

* Bump Spanner client to 6.57.0 ([#1474](https://github.com/googleapis/java-spanner-jdbc/issues/1474)) ([ce5180a](https://github.com/googleapis/java-spanner-jdbc/commit/ce5180a900a6b55bba2d32f401d957f6924f8ea8))
* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.22.0 ([#1463](https://github.com/googleapis/java-spanner-jdbc/issues/1463)) ([d505321](https://github.com/googleapis/java-spanner-jdbc/commit/d505321ab58f3d597550d9891c11eb7a7f17c536))
* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.23.0 ([#1470](https://github.com/googleapis/java-spanner-jdbc/issues/1470)) ([58cf2ba](https://github.com/googleapis/java-spanner-jdbc/commit/58cf2baa1a8cecc4f84d60001d288c535e82b66d))
* Update dependency com.spotify.fmt:fmt-maven-plugin to v2.22 ([#1460](https://github.com/googleapis/java-spanner-jdbc/issues/1460)) ([bbbff2f](https://github.com/googleapis/java-spanner-jdbc/commit/bbbff2ff98a0bc15ea3b5640b767e872f753452c))
* Update dependency com.spotify.fmt:fmt-maven-plugin to v2.22.1 ([#1467](https://github.com/googleapis/java-spanner-jdbc/issues/1467)) ([449bae5](https://github.com/googleapis/java-spanner-jdbc/commit/449bae5079ce122be711490f8f1009f272082d13))
* Update dependency org.springframework.boot:spring-boot-starter-data-jdbc to v3.2.2 ([#1424](https://github.com/googleapis/java-spanner-jdbc/issues/1424)) ([e14af90](https://github.com/googleapis/java-spanner-jdbc/commit/e14af9025a64a94cf431cc2d03bcace7eb753b32))
* Update dependency org.springframework.boot:spring-boot-starter-parent to v3.2.2 ([#1425](https://github.com/googleapis/java-spanner-jdbc/issues/1425)) ([9ccb04a](https://github.com/googleapis/java-spanner-jdbc/commit/9ccb04afaaf355734e955f82716c94d229b958d3))
* Update dependency org.springframework.data:spring-data-bom to v2023.1.2 ([#1421](https://github.com/googleapis/java-spanner-jdbc/issues/1421)) ([dc53309](https://github.com/googleapis/java-spanner-jdbc/commit/dc53309d7846e97ffb1ee2e5cdc0e6c13b648049))

## [2.15.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.15.0...v2.15.1) (2024-01-09)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.21.0 ([#1454](https://github.com/googleapis/java-spanner-jdbc/issues/1454)) ([d1c1d2c](https://github.com/googleapis/java-spanner-jdbc/commit/d1c1d2cf1ad86d987fc285eb97d9b9ca3c53f753))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.56.0 ([#1455](https://github.com/googleapis/java-spanner-jdbc/issues/1455)) ([90fad38](https://github.com/googleapis/java-spanner-jdbc/commit/90fad3870fb3b704d041d1a79f2e8fc6f32631d1))

## [2.15.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.14.6...v2.15.0) (2023-12-22)


### Features

* Support PreparedStatement#getParameterMetaData() ([#1218](https://github.com/googleapis/java-spanner-jdbc/issues/1218)) ([721ff45](https://github.com/googleapis/java-spanner-jdbc/commit/721ff4552104efba47c19ef511282071c3b334c3))


### Performance Improvements

* Optimize isValid implementation ([#1444](https://github.com/googleapis/java-spanner-jdbc/issues/1444)) ([914e973](https://github.com/googleapis/java-spanner-jdbc/commit/914e973ad7fd638fabc3ec130b7618c51f01f401)), closes [#1443](https://github.com/googleapis/java-spanner-jdbc/issues/1443)


### Dependencies

* Update dependency org.postgresql:postgresql to v42.7.1 ([#1441](https://github.com/googleapis/java-spanner-jdbc/issues/1441)) ([5997555](https://github.com/googleapis/java-spanner-jdbc/commit/59975553826360b86492e50b9d49c29aecc28bab))

## [2.14.6](https://github.com/googleapis/java-spanner-jdbc/compare/v2.14.5...v2.14.6) (2023-12-04)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.55.0 ([#1434](https://github.com/googleapis/java-spanner-jdbc/issues/1434)) ([4168611](https://github.com/googleapis/java-spanner-jdbc/commit/4168611a973f3cd35a4cb1ce56d0eefe5e1dd571))

## [2.14.5](https://github.com/googleapis/java-spanner-jdbc/compare/v2.14.4...v2.14.5) (2023-11-30)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.20.0 ([#1430](https://github.com/googleapis/java-spanner-jdbc/issues/1430)) ([5da2d71](https://github.com/googleapis/java-spanner-jdbc/commit/5da2d71c69036a8a4f4033b0bb00c39f98715fd1))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.54.0 ([#1420](https://github.com/googleapis/java-spanner-jdbc/issues/1420)) ([d3f5361](https://github.com/googleapis/java-spanner-jdbc/commit/d3f5361bbe03eca85c1bdba5af0a716dc923a231))
* Update dependency org.mybatis.spring.boot:mybatis-spring-boot-starter to v3.0.3 ([#1426](https://github.com/googleapis/java-spanner-jdbc/issues/1426)) ([8667ee8](https://github.com/googleapis/java-spanner-jdbc/commit/8667ee8c16841b9a526ac7f1bd025f13f9149dc7))
* Update dependency org.postgresql:postgresql to v42.7.0 ([#1422](https://github.com/googleapis/java-spanner-jdbc/issues/1422)) ([d107b25](https://github.com/googleapis/java-spanner-jdbc/commit/d107b25b2d6ad1dcf91a94118bd96d5f975be116))

## [2.14.4](https://github.com/googleapis/java-spanner-jdbc/compare/v2.14.3...v2.14.4) (2023-11-09)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.53.0 ([#1415](https://github.com/googleapis/java-spanner-jdbc/issues/1415)) ([f2b578d](https://github.com/googleapis/java-spanner-jdbc/commit/f2b578dc38bd9328022fd1ecd627d8e1d20c2dbc))

## [2.14.3](https://github.com/googleapis/java-spanner-jdbc/compare/v2.14.2...v2.14.3) (2023-11-01)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.19.0 ([#1411](https://github.com/googleapis/java-spanner-jdbc/issues/1411)) ([eb14ea0](https://github.com/googleapis/java-spanner-jdbc/commit/eb14ea005dbb810ee9e303971338edf0edfedb75))

## [2.14.2](https://github.com/googleapis/java-spanner-jdbc/compare/v2.14.1...v2.14.2) (2023-10-24)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.18.0 ([#1402](https://github.com/googleapis/java-spanner-jdbc/issues/1402)) ([764c9d2](https://github.com/googleapis/java-spanner-jdbc/commit/764c9d244df16307fe3b13e647759b777bb145ab))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.52.1 ([#1401](https://github.com/googleapis/java-spanner-jdbc/issues/1401)) ([cf577ee](https://github.com/googleapis/java-spanner-jdbc/commit/cf577ee89faa84444b37a7b0b7fe931223461836))
* Update dependency org.springframework.boot:spring-boot-starter-data-jdbc to v3.1.5 ([#1399](https://github.com/googleapis/java-spanner-jdbc/issues/1399)) ([ca4b183](https://github.com/googleapis/java-spanner-jdbc/commit/ca4b183c828b1d3cc4496e98ec3b609f7f80c2a8))
* Update dependency org.springframework.boot:spring-boot-starter-parent to v3.1.5 ([#1400](https://github.com/googleapis/java-spanner-jdbc/issues/1400)) ([9b139aa](https://github.com/googleapis/java-spanner-jdbc/commit/9b139aa200a9ddf436b089555d25274f5ef7ee4a))

## [2.14.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.14.0...v2.14.1) (2023-10-14)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.51.0 ([#1393](https://github.com/googleapis/java-spanner-jdbc/issues/1393)) ([74d106f](https://github.com/googleapis/java-spanner-jdbc/commit/74d106f2590abc592b501093b6a30d2493e49868))
* Update dependency org.springframework.data:spring-data-bom to v2023.0.5 ([#1391](https://github.com/googleapis/java-spanner-jdbc/issues/1391)) ([c5a7ee4](https://github.com/googleapis/java-spanner-jdbc/commit/c5a7ee439f5d1f2242ffa2f3522b589cefb25744))

## [2.14.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.13.4...v2.14.0) (2023-10-12)


### Features

* Support default schema and catalog for PostgreSQL databases ([#1375](https://github.com/googleapis/java-spanner-jdbc/issues/1375)) ([2737ece](https://github.com/googleapis/java-spanner-jdbc/commit/2737ecec00abd51b796e13375f2ebdfbf8e1b201))


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.17.0 ([#1383](https://github.com/googleapis/java-spanner-jdbc/issues/1383)) ([f0209a7](https://github.com/googleapis/java-spanner-jdbc/commit/f0209a7be923465a30effc30ac23294299e0cd72))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.50.0 ([#1386](https://github.com/googleapis/java-spanner-jdbc/issues/1386)) ([8401ef8](https://github.com/googleapis/java-spanner-jdbc/commit/8401ef868493a83d2c8b8d68a33a118d2b94f769))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.50.1 ([#1388](https://github.com/googleapis/java-spanner-jdbc/issues/1388)) ([8ae3919](https://github.com/googleapis/java-spanner-jdbc/commit/8ae3919e352dbe02a03a41cfbc440619d299454c))

## [2.13.4](https://github.com/googleapis/java-spanner-jdbc/compare/v2.13.3...v2.13.4) (2023-09-28)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.49.0 ([#1376](https://github.com/googleapis/java-spanner-jdbc/issues/1376)) ([cc28b44](https://github.com/googleapis/java-spanner-jdbc/commit/cc28b44595d9a3cb112a35081fda98200d529ebf))

## [2.13.3](https://github.com/googleapis/java-spanner-jdbc/compare/v2.13.2...v2.13.3) (2023-09-27)


### Dependencies

* Remove specific JDBC version from samples ([#1371](https://github.com/googleapis/java-spanner-jdbc/issues/1371)) ([b30e391](https://github.com/googleapis/java-spanner-jdbc/commit/b30e391792f2c2811038b35a065b35104bc614e7))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.48.0 ([#1370](https://github.com/googleapis/java-spanner-jdbc/issues/1370)) ([376e1c3](https://github.com/googleapis/java-spanner-jdbc/commit/376e1c3ccdd71351a5d6151ce19b9f88df163776))
* Update dependency com.spotify.fmt:fmt-maven-plugin to v2.21.1 ([#1372](https://github.com/googleapis/java-spanner-jdbc/issues/1372)) ([bf64add](https://github.com/googleapis/java-spanner-jdbc/commit/bf64add3e9ce8148d2fc3ad010b8abd446208e4f))
* Update dependency org.springframework.boot:spring-boot-starter-parent to v3.1.4 ([#1366](https://github.com/googleapis/java-spanner-jdbc/issues/1366)) ([749d2c3](https://github.com/googleapis/java-spanner-jdbc/commit/749d2c3698c900560b6f85247b0a41a85cd55ac8))
* Update dependency org.springframework.data:spring-data-bom to v2023.0.4 ([#1367](https://github.com/googleapis/java-spanner-jdbc/issues/1367)) ([916ad4a](https://github.com/googleapis/java-spanner-jdbc/commit/916ad4a9e07b3afc15e53664f175db9e58f06376))


### Documentation

* Add sample for Spring Data MyBatis ([#1352](https://github.com/googleapis/java-spanner-jdbc/issues/1352)) ([ce52d07](https://github.com/googleapis/java-spanner-jdbc/commit/ce52d07c308bcde0ed1b0c9f4d3556db2590f722))

## [2.13.2](https://github.com/googleapis/java-spanner-jdbc/compare/v2.13.1...v2.13.2) (2023-09-26)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.16.0 ([#1358](https://github.com/googleapis/java-spanner-jdbc/issues/1358)) ([c4c4925](https://github.com/googleapis/java-spanner-jdbc/commit/c4c492576d3e6c192a1855e8d6b3474bb2ad0c22))
* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.16.1 ([#1363](https://github.com/googleapis/java-spanner-jdbc/issues/1363)) ([d574dbb](https://github.com/googleapis/java-spanner-jdbc/commit/d574dbb761fa7d0a7d1977844b48b8e4904f1bb0))
* Update dependency com.spotify.fmt:fmt-maven-plugin to v2.21.1 ([#1359](https://github.com/googleapis/java-spanner-jdbc/issues/1359)) ([70af99e](https://github.com/googleapis/java-spanner-jdbc/commit/70af99e96451fb0158abb45580eaae09ad0b6210))

## [2.13.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.13.0...v2.13.1) (2023-09-21)


### Dependencies

* Update dependency org.springframework.boot:spring-boot-starter-data-jdbc to v3.1.4 ([#1353](https://github.com/googleapis/java-spanner-jdbc/issues/1353)) ([88cd905](https://github.com/googleapis/java-spanner-jdbc/commit/88cd905bece9c8da7f26b637392e35ab2536edeb))

## [2.13.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.12.1...v2.13.0) (2023-09-15)


### Features

* Support partitioned queries ([#1300](https://github.com/googleapis/java-spanner-jdbc/issues/1300)) ([c50da41](https://github.com/googleapis/java-spanner-jdbc/commit/c50da41e688ff48f8967c0f114f5bac8eaac49f9))


### Bug Fixes

* Comments should be sent to Spanner for PostgreSQL databases ([#1331](https://github.com/googleapis/java-spanner-jdbc/issues/1331)) ([7c9e781](https://github.com/googleapis/java-spanner-jdbc/commit/7c9e781bf45b112266e278e1df1586e56043698e))


### Documentation

* Create Spring Data JDBC sample ([#1334](https://github.com/googleapis/java-spanner-jdbc/issues/1334)) ([cefea55](https://github.com/googleapis/java-spanner-jdbc/commit/cefea55086eb191f71a1a493e046cb136f9f9f87))


### Dependencies

* Update actions/checkout action to v4 - abandoned ([#1333](https://github.com/googleapis/java-spanner-jdbc/issues/1333)) ([ce82b42](https://github.com/googleapis/java-spanner-jdbc/commit/ce82b42d3abb8de0f8b3ee2915c2008673775ea1))
* Update dependency org.springframework.data:spring-data-bom to v2023.0.4 ([#1347](https://github.com/googleapis/java-spanner-jdbc/issues/1347)) ([893f61a](https://github.com/googleapis/java-spanner-jdbc/commit/893f61ab04e32c690f1ff9fc813bd2ba6ebca328))

## [2.12.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.12.0...v2.12.1) (2023-09-12)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.46.0 ([#1338](https://github.com/googleapis/java-spanner-jdbc/issues/1338)) ([0bcb5dc](https://github.com/googleapis/java-spanner-jdbc/commit/0bcb5dc9d0c6e4d3878ceb748c09e87c75d88675))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.47.0 ([#1341](https://github.com/googleapis/java-spanner-jdbc/issues/1341)) ([0010650](https://github.com/googleapis/java-spanner-jdbc/commit/00106505771ed75f83ceaf181f45f19e4251cd78))

## [2.12.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.9...v2.12.0) (2023-08-28)


### Features

* Return generated keys ([#1310](https://github.com/googleapis/java-spanner-jdbc/issues/1310)) ([9b5ab37](https://github.com/googleapis/java-spanner-jdbc/commit/9b5ab377587de09004474cb1cf488919fc83d6cb))


### Bug Fixes

* Session leak for invalid update ([#1323](https://github.com/googleapis/java-spanner-jdbc/issues/1323)) ([a7d0fbb](https://github.com/googleapis/java-spanner-jdbc/commit/a7d0fbb529ff71b45d6ddbbad8fc3be43e7c966f))

## [2.11.9](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.8...v2.11.9) (2023-08-21)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.45.3 ([#1325](https://github.com/googleapis/java-spanner-jdbc/issues/1325)) ([d96d278](https://github.com/googleapis/java-spanner-jdbc/commit/d96d278f58942c38b30dc0a4e1f9f92aefdc760b))

## [2.11.8](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.7...v2.11.8) (2023-08-15)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.45.2 ([#1318](https://github.com/googleapis/java-spanner-jdbc/issues/1318)) ([e924178](https://github.com/googleapis/java-spanner-jdbc/commit/e9241787b94cb614f658f5e6c977ffc008fd3397))

## [2.11.7](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.6...v2.11.7) (2023-08-13)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.45.1 ([#1312](https://github.com/googleapis/java-spanner-jdbc/issues/1312)) ([2e99e35](https://github.com/googleapis/java-spanner-jdbc/commit/2e99e357c9688c89881433e77b3167924442abaa))

## [2.11.6](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.5...v2.11.6) (2023-08-05)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.14.0 ([#1303](https://github.com/googleapis/java-spanner-jdbc/issues/1303)) ([8afb5ba](https://github.com/googleapis/java-spanner-jdbc/commit/8afb5ba0db1e21e317799db8e82f96820c01c6dd))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.45.0 ([#1305](https://github.com/googleapis/java-spanner-jdbc/issues/1305)) ([aac0d68](https://github.com/googleapis/java-spanner-jdbc/commit/aac0d68c11bada6d338483208be631cbb9ef8ed7))

## [2.11.5](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.4...v2.11.5) (2023-07-27)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.13.1 ([#1291](https://github.com/googleapis/java-spanner-jdbc/issues/1291)) ([aa40c60](https://github.com/googleapis/java-spanner-jdbc/commit/aa40c60df7dcf7fbf45c4c942be2dc31130a3d6b))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.44.0 ([#1295](https://github.com/googleapis/java-spanner-jdbc/issues/1295)) ([ce257f8](https://github.com/googleapis/java-spanner-jdbc/commit/ce257f8acad39581770a28a81d902251673db79f))

## [2.11.4](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.3...v2.11.4) (2023-07-12)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.43.2 ([#1284](https://github.com/googleapis/java-spanner-jdbc/issues/1284)) ([5d7c3a5](https://github.com/googleapis/java-spanner-jdbc/commit/5d7c3a55a3b37d7268222672fe21a75d019512fb))

## [2.11.3](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.2...v2.11.3) (2023-07-09)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.13.0 ([#1278](https://github.com/googleapis/java-spanner-jdbc/issues/1278)) ([acb2626](https://github.com/googleapis/java-spanner-jdbc/commit/acb2626a6d34f876da8e552cf98ef5a75c57b758))

## [2.11.2](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.1...v2.11.2) (2023-06-26)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.43.1 ([#1271](https://github.com/googleapis/java-spanner-jdbc/issues/1271)) ([c549901](https://github.com/googleapis/java-spanner-jdbc/commit/c5499012dff10c5999cf046f3e1076e17c973662))

## [2.11.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.11.0...v2.11.1) (2023-06-26)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.12.0 ([#1267](https://github.com/googleapis/java-spanner-jdbc/issues/1267)) ([bb23df0](https://github.com/googleapis/java-spanner-jdbc/commit/bb23df01bf401310c91c3fc2069a2ea16c70f5a4))

## [2.11.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.10.0...v2.11.0) (2023-06-12)


### Features

* Support untyped NULL value parameters ([#1224](https://github.com/googleapis/java-spanner-jdbc/issues/1224)) ([80d2b9d](https://github.com/googleapis/java-spanner-jdbc/commit/80d2b9d3e4c3265522bbb20766bff1f164617711))


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.11.0 ([#1254](https://github.com/googleapis/java-spanner-jdbc/issues/1254)) ([41f40fc](https://github.com/googleapis/java-spanner-jdbc/commit/41f40fce634cea205d5e5a9c1eb567ecb97ff655))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.42.3 ([#1248](https://github.com/googleapis/java-spanner-jdbc/issues/1248)) ([397d573](https://github.com/googleapis/java-spanner-jdbc/commit/397d5738a8126aaf090d533d0f20efb74a77a788))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.43.0 ([#1255](https://github.com/googleapis/java-spanner-jdbc/issues/1255)) ([ffe36b6](https://github.com/googleapis/java-spanner-jdbc/commit/ffe36b6b2087157c8d895fa348cff614435a4735))

## [2.10.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.16...v2.10.0) (2023-05-30)


### Features

* Support Savepoint ([#1212](https://github.com/googleapis/java-spanner-jdbc/issues/1212)) ([6833696](https://github.com/googleapis/java-spanner-jdbc/commit/683369633627367342b3a40e3abba4fa81069724))


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.10.1 ([#1239](https://github.com/googleapis/java-spanner-jdbc/issues/1239)) ([8f7e7a7](https://github.com/googleapis/java-spanner-jdbc/commit/8f7e7a79be6d7326d7e6bdd6018bb76a695cb1b8))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.42.2 ([#1237](https://github.com/googleapis/java-spanner-jdbc/issues/1237)) ([97961b2](https://github.com/googleapis/java-spanner-jdbc/commit/97961b2c501d428575e283485386e04f4673d968))

## [2.9.16](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.15...v2.9.16) (2023-05-15)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.42.0 ([#1231](https://github.com/googleapis/java-spanner-jdbc/issues/1231)) ([011570f](https://github.com/googleapis/java-spanner-jdbc/commit/011570ffb1d18fafe0a322d5582c5f7206349e09))

## [2.9.15](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.14...v2.9.15) (2023-05-15)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.9.0 ([#1227](https://github.com/googleapis/java-spanner-jdbc/issues/1227)) ([329f258](https://github.com/googleapis/java-spanner-jdbc/commit/329f25862a0f7118cc0419568eca3a72a053a055))

## [2.9.14](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.13...v2.9.14) (2023-05-02)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.41.0 ([#1216](https://github.com/googleapis/java-spanner-jdbc/issues/1216)) ([9d0be37](https://github.com/googleapis/java-spanner-jdbc/commit/9d0be37fcea0ab90f8408b433295f7640f059f0a))

## [2.9.13](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.12...v2.9.13) (2023-04-27)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.8.0 ([#1211](https://github.com/googleapis/java-spanner-jdbc/issues/1211)) ([a7e762d](https://github.com/googleapis/java-spanner-jdbc/commit/a7e762dddc2e0165bbf8e3e722df3b46c2b9a089))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.40.1 ([#1207](https://github.com/googleapis/java-spanner-jdbc/issues/1207)) ([44ea7f8](https://github.com/googleapis/java-spanner-jdbc/commit/44ea7f8520230c7a9c632e42b7b668f179d4aa95))

## [2.9.12](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.11...v2.9.12) (2023-04-19)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.7.0 ([#1204](https://github.com/googleapis/java-spanner-jdbc/issues/1204)) ([370e237](https://github.com/googleapis/java-spanner-jdbc/commit/370e237da3cd04a0ad50ba306cc2bcb7e3a8ec22))

## [2.9.11](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.10...v2.9.11) (2023-04-03)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.38.1 ([#1162](https://github.com/googleapis/java-spanner-jdbc/issues/1162)) ([e114284](https://github.com/googleapis/java-spanner-jdbc/commit/e114284185fba67b983b0839b787affcfd741bb4))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.38.2 ([#1165](https://github.com/googleapis/java-spanner-jdbc/issues/1165)) ([8cbf519](https://github.com/googleapis/java-spanner-jdbc/commit/8cbf519905456b69e223d1f3d3e7d29b83ffd477))

## [2.9.10](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.9...v2.9.10) (2023-03-29)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.6.0 ([#1160](https://github.com/googleapis/java-spanner-jdbc/issues/1160)) ([392819e](https://github.com/googleapis/java-spanner-jdbc/commit/392819e7705cd6a2b978b69c09fdb79f534a1215))

## [2.9.9](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.8...v2.9.9) (2023-03-20)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.5.0 ([#1153](https://github.com/googleapis/java-spanner-jdbc/issues/1153)) ([81969b5](https://github.com/googleapis/java-spanner-jdbc/commit/81969b512072443dac418dc82180cb49ee4113c1))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.38.0 ([#1154](https://github.com/googleapis/java-spanner-jdbc/issues/1154)) ([fe81de9](https://github.com/googleapis/java-spanner-jdbc/commit/fe81de965c170315554f438488ac8b1ef4667999))

## [2.9.8](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.7...v2.9.8) (2023-03-04)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.4.0 ([#1145](https://github.com/googleapis/java-spanner-jdbc/issues/1145)) ([34864e7](https://github.com/googleapis/java-spanner-jdbc/commit/34864e73fc78399d08be7972c220e4794ac16dfb))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.37.0 ([#1146](https://github.com/googleapis/java-spanner-jdbc/issues/1146)) ([541a1f0](https://github.com/googleapis/java-spanner-jdbc/commit/541a1f0323c5d7d72cb33acd696660dd94224858))

## [2.9.7](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.6...v2.9.7) (2023-02-21)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.36.1 ([#1138](https://github.com/googleapis/java-spanner-jdbc/issues/1138)) ([b7b9916](https://github.com/googleapis/java-spanner-jdbc/commit/b7b99166febdb833e7af17a6c3f38ac7e3f8f767))

## [2.9.6](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.5...v2.9.6) (2023-02-20)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.3.0 ([#1133](https://github.com/googleapis/java-spanner-jdbc/issues/1133)) ([57d8c6d](https://github.com/googleapis/java-spanner-jdbc/commit/57d8c6d93d681644fac1e18cc9ba1ab80595feb4))

## [2.9.5](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.4...v2.9.5) (2023-02-09)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.36.0 ([#1121](https://github.com/googleapis/java-spanner-jdbc/issues/1121)) ([dcb5826](https://github.com/googleapis/java-spanner-jdbc/commit/dcb5826b17b30303b8d6115bf55a4ede0e13ef84))

## [2.9.4](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.3...v2.9.4) (2023-02-08)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.2.0 ([#1118](https://github.com/googleapis/java-spanner-jdbc/issues/1118)) ([56ed82b](https://github.com/googleapis/java-spanner-jdbc/commit/56ed82b26e40ccfa88e046eb39f8b146bd647e16))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.35.2 ([#1114](https://github.com/googleapis/java-spanner-jdbc/issues/1114)) ([6575d0c](https://github.com/googleapis/java-spanner-jdbc/commit/6575d0c980fa894a5d50b978f8cc93974e804eb2))
* Use perfmark-api version via shared dependencies BOM ([#1111](https://github.com/googleapis/java-spanner-jdbc/issues/1111)) ([beb5298](https://github.com/googleapis/java-spanner-jdbc/commit/beb52985676b0e981072ee7df60a4cf9f6139624))

## [2.9.3](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.2...v2.9.3) (2023-01-25)


### Bug Fixes

* **java:** Skip fixing poms for special modules ([#1744](https://github.com/googleapis/java-spanner-jdbc/issues/1744)) ([#1108](https://github.com/googleapis/java-spanner-jdbc/issues/1108)) ([2915d76](https://github.com/googleapis/java-spanner-jdbc/commit/2915d766bf6578f1921f8ffe38d6b9be61c63813))


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.1.2 ([#1109](https://github.com/googleapis/java-spanner-jdbc/issues/1109)) ([3614824](https://github.com/googleapis/java-spanner-jdbc/commit/3614824692899165d38ffd12de8cf3543560ea5c))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.35.1 ([#1106](https://github.com/googleapis/java-spanner-jdbc/issues/1106)) ([3bd7d6b](https://github.com/googleapis/java-spanner-jdbc/commit/3bd7d6b72ee87b3b83a50e2459f2d4e3b0fd87a2))

## [2.9.2](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.1...v2.9.2) (2023-01-14)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.35.0 ([#1097](https://github.com/googleapis/java-spanner-jdbc/issues/1097)) ([d8d11c3](https://github.com/googleapis/java-spanner-jdbc/commit/d8d11c3d46197680ffa28c85261736651a64eb63))

## [2.9.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.9.0...v2.9.1) (2023-01-12)


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.1.1 ([#1090](https://github.com/googleapis/java-spanner-jdbc/issues/1090)) ([ef9ccf8](https://github.com/googleapis/java-spanner-jdbc/commit/ef9ccf84b78057edc338def2cea69b85ec6a75bf))

## [2.9.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.8.0...v2.9.0) (2022-12-14)


### Features

* Add tests for DML with Returning clause ([#936](https://github.com/googleapis/java-spanner-jdbc/issues/936)) ([8a86467](https://github.com/googleapis/java-spanner-jdbc/commit/8a86467c6db7a4e99fdf23cdbce2d78382f8cde9))


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.1.0 ([#1069](https://github.com/googleapis/java-spanner-jdbc/issues/1069)) ([c2ff33a](https://github.com/googleapis/java-spanner-jdbc/commit/c2ff33a8f6a7051e1639fee235e1b6bba5916c3a))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.34.1 ([#1072](https://github.com/googleapis/java-spanner-jdbc/issues/1072)) ([0045a5e](https://github.com/googleapis/java-spanner-jdbc/commit/0045a5e51c5a40523f70ec22c9b75bc8707dccb7))

## [2.8.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.13...v2.8.0) (2022-11-17)


### Features

* Jsonb data type support ([#926](https://github.com/googleapis/java-spanner-jdbc/issues/926)) ([cefc290](https://github.com/googleapis/java-spanner-jdbc/commit/cefc290d343a2a973e1efbeee33c349fbf98060c))


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.0.6 ([#1044](https://github.com/googleapis/java-spanner-jdbc/issues/1044)) ([3b00962](https://github.com/googleapis/java-spanner-jdbc/commit/3b0096215b63df415a44df1e2f7cb765f9022630))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.33.0 ([#1051](https://github.com/googleapis/java-spanner-jdbc/issues/1051)) ([e728ac1](https://github.com/googleapis/java-spanner-jdbc/commit/e728ac1eee0987a59ce57bc4c2f76e0c42b840a9))
* Update dependency io.perfmark:perfmark-api to v0.26.0 ([#1045](https://github.com/googleapis/java-spanner-jdbc/issues/1045)) ([87d578c](https://github.com/googleapis/java-spanner-jdbc/commit/87d578c7408586118f1941b976330357ca658d15))

## [2.7.13](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.12...v2.7.13) (2022-10-31)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.32.0 ([#1033](https://github.com/googleapis/java-spanner-jdbc/issues/1033)) ([bbbd2c6](https://github.com/googleapis/java-spanner-jdbc/commit/bbbd2c65d75a5805965fa7f9b9dc820e62f34ad3))

## [2.7.12](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.11...v2.7.12) (2022-10-27)


### Bug Fixes

* **java:** Initialize classes at build-time to address native image 22.2.0 issues ([#1026](https://github.com/googleapis/java-spanner-jdbc/issues/1026)) ([8010da5](https://github.com/googleapis/java-spanner-jdbc/commit/8010da5ef539d509eb7f96d9b89edf35b2e809ad))

## [2.7.11](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.10...v2.7.11) (2022-10-20)


### Dependencies

* Update dependency org.graalvm.buildtools:junit-platform-native to v0.9.16 ([#1017](https://github.com/googleapis/java-spanner-jdbc/issues/1017)) ([ee7888c](https://github.com/googleapis/java-spanner-jdbc/commit/ee7888c1720aa84ca2d4278a9e52f111c298ea9d))
* Update dependency org.graalvm.buildtools:native-maven-plugin to v0.9.16 ([#1018](https://github.com/googleapis/java-spanner-jdbc/issues/1018)) ([3d5b100](https://github.com/googleapis/java-spanner-jdbc/commit/3d5b1004eb3d4e61f954b523ef2c45d59f0fbfe0))

## [2.7.10](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.9...v2.7.10) (2022-10-18)


### Dependencies

* Update dependency org.graalvm.buildtools:junit-platform-native to v0.9.15 ([#1009](https://github.com/googleapis/java-spanner-jdbc/issues/1009)) ([8a6da6c](https://github.com/googleapis/java-spanner-jdbc/commit/8a6da6cbd95a23f6601775fb7147567ca1017119))
* Update dependency org.graalvm.buildtools:native-maven-plugin to v0.9.15 ([#1010](https://github.com/googleapis/java-spanner-jdbc/issues/1010)) ([b686d20](https://github.com/googleapis/java-spanner-jdbc/commit/b686d20f86f2408d714e8e0335332635f10971cb))

## [2.7.9](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.8...v2.7.9) (2022-10-06)


### Dependencies

* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.31.2 ([#1002](https://github.com/googleapis/java-spanner-jdbc/issues/1002)) ([f2ac8e3](https://github.com/googleapis/java-spanner-jdbc/commit/f2ac8e392e972d326bc5702855385471febea87c))

## [2.7.8](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.7...v2.7.8) (2022-10-03)


### Bug Fixes

* Upgrade native image plugin to 0.9.14 to unblock graalvm 22.2 update ([#998](https://github.com/googleapis/java-spanner-jdbc/issues/998)) ([e5ed330](https://github.com/googleapis/java-spanner-jdbc/commit/e5ed3300780eec0a32c0f135415063f65fb5ca82))


### Dependencies

* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.0.4 ([#1001](https://github.com/googleapis/java-spanner-jdbc/issues/1001)) ([feafe10](https://github.com/googleapis/java-spanner-jdbc/commit/feafe1076c7e272bfe69fe055f742072b5aac763))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.30.1 ([#973](https://github.com/googleapis/java-spanner-jdbc/issues/973)) ([205f312](https://github.com/googleapis/java-spanner-jdbc/commit/205f312ab3466e9efb3fc3c79ddc9644adcca527))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.30.2 ([#976](https://github.com/googleapis/java-spanner-jdbc/issues/976)) ([037a33a](https://github.com/googleapis/java-spanner-jdbc/commit/037a33a932d2a50412bc53cc05cf512a9d2a4548))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.31.0 ([#1000](https://github.com/googleapis/java-spanner-jdbc/issues/1000)) ([59d69fb](https://github.com/googleapis/java-spanner-jdbc/commit/59d69fb6548d7e1d23fc8285faa975befd00323a))
* Update dependency org.graalvm.buildtools:junit-platform-native to v0.9.14 ([#977](https://github.com/googleapis/java-spanner-jdbc/issues/977)) ([32c881e](https://github.com/googleapis/java-spanner-jdbc/commit/32c881ea6530d23385f0c001e99d767146544210))
* Update dependency org.junit.vintage:junit-vintage-engine to v5.9.1 ([#974](https://github.com/googleapis/java-spanner-jdbc/issues/974)) ([fab57d9](https://github.com/googleapis/java-spanner-jdbc/commit/fab57d9adea37ca4c3aa9aeb8d11e086ba86e538))

## [2.7.7](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.6...v2.7.7) (2022-09-16)


### Bug Fixes

* Types.BOOLEAN and Types.BIT should have identical behaviour for nullability ([#920](https://github.com/googleapis/java-spanner-jdbc/issues/920)) ([42e5903](https://github.com/googleapis/java-spanner-jdbc/commit/42e590343ccbe294301a7b9933bd5db1830c8877))


### Dependencies

* Google-cloud-spanner-bom 6.30.0 ([#967](https://github.com/googleapis/java-spanner-jdbc/issues/967)) ([9385a5d](https://github.com/googleapis/java-spanner-jdbc/commit/9385a5dc52704de6fb1fe1c31f2c4ba2ad84d547))
* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.0.2 ([#963](https://github.com/googleapis/java-spanner-jdbc/issues/963)) ([811d96b](https://github.com/googleapis/java-spanner-jdbc/commit/811d96b0a7f90df7ec4d680aad085eec9d331a5e))
* Update dependency com.google.cloud:google-cloud-shared-dependencies to v3.0.3 ([#966](https://github.com/googleapis/java-spanner-jdbc/issues/966)) ([f8b88cd](https://github.com/googleapis/java-spanner-jdbc/commit/f8b88cd95c2bf02ce178a387ad82b149a9f4dbca))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.29.0 ([#943](https://github.com/googleapis/java-spanner-jdbc/issues/943)) ([9754023](https://github.com/googleapis/java-spanner-jdbc/commit/9754023b4d16aa78361d5be465d24b0481b84293))
* Update dependency com.google.cloud:google-cloud-spanner-bom to v6.29.1 ([#961](https://github.com/googleapis/java-spanner-jdbc/issues/961)) ([a3e1fc6](https://github.com/googleapis/java-spanner-jdbc/commit/a3e1fc6b627e8f4da0191787e432d52b8174067a))

## [2.7.6](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.5...v2.7.6) (2022-08-11)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v3 ([#921](https://github.com/googleapis/java-spanner-jdbc/issues/921)) ([2aa0a40](https://github.com/googleapis/java-spanner-jdbc/commit/2aa0a40619e9f743877cff410db17968c374dc52))

## [2.7.5](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.4...v2.7.5) (2022-08-05)


### Bug Fixes

* enable longpaths support for windows test ([#1485](https://github.com/googleapis/java-spanner-jdbc/issues/1485)) ([#908](https://github.com/googleapis/java-spanner-jdbc/issues/908)) ([2e53ade](https://github.com/googleapis/java-spanner-jdbc/commit/2e53adef54a6336bbdafcdb2dd5ee2011c07dc6f))
* pr to troubleshoot native image tests ([#912](https://github.com/googleapis/java-spanner-jdbc/issues/912)) ([4e78071](https://github.com/googleapis/java-spanner-jdbc/commit/4e78071c7451b194439b7b0b300488ec50c9cd1e))


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.27.0 ([#898](https://github.com/googleapis/java-spanner-jdbc/issues/898)) ([c536dd6](https://github.com/googleapis/java-spanner-jdbc/commit/c536dd64f61f7a1f74d3c876156ffb2d99172ef1))
* update dependency org.graalvm.buildtools:junit-platform-native to v0.9.13 ([#922](https://github.com/googleapis/java-spanner-jdbc/issues/922)) ([125a972](https://github.com/googleapis/java-spanner-jdbc/commit/125a972f902dab45833baeee1538bc773a69d4b6))
* update dependency org.junit.vintage:junit-vintage-engine to v5.9.0 ([#924](https://github.com/googleapis/java-spanner-jdbc/issues/924)) ([9d6d313](https://github.com/googleapis/java-spanner-jdbc/commit/9d6d3135a3e2fbc3e377f5459924ed8869498c36))

## [2.7.4](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.3...v2.7.4) (2022-06-30)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.13.0 ([#899](https://github.com/googleapis/java-spanner-jdbc/issues/899)) ([0286068](https://github.com/googleapis/java-spanner-jdbc/commit/02860683f00678804c58ae2c0b213bc934441397))

## [2.7.3](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.2...v2.7.3) (2022-05-31)


### Bug Fixes

* **java:** adding resource and reflection configurations for native image testing ([#809](https://github.com/googleapis/java-spanner-jdbc/issues/809)) ([6126d4f](https://github.com/googleapis/java-spanner-jdbc/commit/6126d4f55dde76c8b945999008bbee78203a1b75))

## [2.7.2](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.1...v2.7.2) (2022-05-31)


### Dependencies

* google-cloud-spanner-bom 6.25.5 ([#887](https://github.com/googleapis/java-spanner-jdbc/issues/887)) ([2ec08bf](https://github.com/googleapis/java-spanner-jdbc/commit/2ec08bf722d68013b72d648c7facf80a7342dafc))

### [2.7.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.7.0...v2.7.1) (2022-05-27)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.25.2 ([#881](https://github.com/googleapis/java-spanner-jdbc/issues/881)) ([b1980b6](https://github.com/googleapis/java-spanner-jdbc/commit/b1980b68ec73bcf137546d167679bb7ae063cebf))

## [2.7.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.6.4...v2.7.0) (2022-05-24)


### Features

* add build scripts for native image testing in Java 17 ([#1440](https://github.com/googleapis/java-spanner-jdbc/issues/1440)) ([#875](https://github.com/googleapis/java-spanner-jdbc/issues/875)) ([600e401](https://github.com/googleapis/java-spanner-jdbc/commit/600e4017e0b2e52e7a2f42ffca88b1326be03a31))


### Dependencies

* bump Spanner to 6.23.3 ([#862](https://github.com/googleapis/java-spanner-jdbc/issues/862)) ([b7b8efa](https://github.com/googleapis/java-spanner-jdbc/commit/b7b8efa80cdef9a827c85d469176463001c14b94)), closes [#788](https://github.com/googleapis/java-spanner-jdbc/issues/788)
* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.12.0 ([#874](https://github.com/googleapis/java-spanner-jdbc/issues/874)) ([d403f54](https://github.com/googleapis/java-spanner-jdbc/commit/d403f5414cf593e239140f5bbf89b06608167fbf))
* update opencensus.version to v0.31.1 ([#865](https://github.com/googleapis/java-spanner-jdbc/issues/865)) ([61ba9be](https://github.com/googleapis/java-spanner-jdbc/commit/61ba9be15018c198a00d8f2e69121470c3da2ce0))

### [2.6.4](https://github.com/googleapis/java-spanner-jdbc/compare/v2.6.3...v2.6.4) (2022-04-21)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.10.0 ([#798](https://github.com/googleapis/java-spanner-jdbc/issues/798)) ([a77024c](https://github.com/googleapis/java-spanner-jdbc/commit/a77024cd0611e69c501d466c6759329f69cb1953))

### [2.6.3](https://github.com/googleapis/java-spanner-jdbc/compare/v2.6.2...v2.6.3) (2022-03-29)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.9.0 ([#789](https://github.com/googleapis/java-spanner-jdbc/issues/789)) ([5fd7287](https://github.com/googleapis/java-spanner-jdbc/commit/5fd7287861029acc1179cac8b604e435aa8b0666))

### [2.6.2](https://github.com/googleapis/java-spanner-jdbc/compare/v2.6.1...v2.6.2) (2022-03-14)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.21.2 ([#783](https://github.com/googleapis/java-spanner-jdbc/issues/783)) ([1625ad0](https://github.com/googleapis/java-spanner-jdbc/commit/1625ad0e5a827e6cbf68bf6a3bd18eb4b02fc62b))

### [2.6.1](https://github.com/googleapis/java-spanner-jdbc/compare/v2.6.0...v2.6.1) (2022-03-02)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.8.0 ([#770](https://github.com/googleapis/java-spanner-jdbc/issues/770)) ([4f32b2d](https://github.com/googleapis/java-spanner-jdbc/commit/4f32b2d6031870c386b37f574d2107f446ed7db7))

## [2.6.0](https://github.com/googleapis/java-spanner-jdbc/compare/v2.5.11...v2.6.0) (2022-02-24)


### Features

* add support for PostgreSQL dialect ([#739](https://github.com/googleapis/java-spanner-jdbc/issues/739)) ([f9daa19](https://github.com/googleapis/java-spanner-jdbc/commit/f9daa19453b33252bf61160ff9cde1c37284ca2b))


### Bug Fixes

* create specific metadata queries for PG ([#759](https://github.com/googleapis/java-spanner-jdbc/issues/759)) ([caffda0](https://github.com/googleapis/java-spanner-jdbc/commit/caffda03e528da6a3c2c17b7058eb5d29f5086f9))


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.20.0 ([#758](https://github.com/googleapis/java-spanner-jdbc/issues/758)) ([311d1ca](https://github.com/googleapis/java-spanner-jdbc/commit/311d1cabff7e7e2f5cf2cdcdda90ba536eadfa68))

### [2.5.11](https://github.com/googleapis/java-spanner-jdbc/compare/v2.5.10...v2.5.11) (2022-02-11)


### Dependencies

* update actions/github-script action to v6 ([#745](https://github.com/googleapis/java-spanner-jdbc/issues/745)) ([2ccd5b8](https://github.com/googleapis/java-spanner-jdbc/commit/2ccd5b8ac878c81535c14e404aeaf67e6e41a464))

### [2.5.10](https://github.com/googleapis/java-spanner-jdbc/compare/v2.5.9...v2.5.10) (2022-02-09)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.18.0 ([#734](https://github.com/googleapis/java-spanner-jdbc/issues/734)) ([52f407a](https://github.com/googleapis/java-spanner-jdbc/commit/52f407a5e73d13fdeb9b5438d6e5cbd026cb3942))

### [2.5.9](https://github.com/googleapis/java-spanner-jdbc/compare/v2.5.8...v2.5.9) (2022-02-03)


### Bug Fixes

* **java:** replace excludedGroup with exclude ([#720](https://github.com/googleapis/java-spanner-jdbc/issues/720)) ([7f13c88](https://github.com/googleapis/java-spanner-jdbc/commit/7f13c88f8c9e509de8c82cb788ab9b4964806381))


### Dependencies

* **java:** update actions/github-script action to v5 ([#1339](https://github.com/googleapis/java-spanner-jdbc/issues/1339)) ([#725](https://github.com/googleapis/java-spanner-jdbc/issues/725)) ([4f96ec1](https://github.com/googleapis/java-spanner-jdbc/commit/4f96ec1c864b176564ac3200565b5ea524d8adfb))
* update actions/github-script action to v5 ([#724](https://github.com/googleapis/java-spanner-jdbc/issues/724)) ([5c1d6ff](https://github.com/googleapis/java-spanner-jdbc/commit/5c1d6ff72ba81dac101904f1ebd63e4a09b47c64))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.7.0 ([#728](https://github.com/googleapis/java-spanner-jdbc/issues/728)) ([b0a32d8](https://github.com/googleapis/java-spanner-jdbc/commit/b0a32d807cdf2458b60437ade38fa46511254701))
* update opencensus.version to v0.31.0 ([#727](https://github.com/googleapis/java-spanner-jdbc/issues/727)) ([fce0770](https://github.com/googleapis/java-spanner-jdbc/commit/fce077056fbf55395a736dc1f58f8ecbc89eb10d))

### [2.5.8](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.5.7...v2.5.8) (2022-01-07)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.17.4 ([#709](https://www.github.com/googleapis/java-spanner-jdbc/issues/709)) ([bd12d7c](https://www.github.com/googleapis/java-spanner-jdbc/commit/bd12d7c33b18ceb1df417df8e275ffa745b195b2))

### [2.5.7](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.5.6...v2.5.7) (2022-01-07)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.6.0 ([#704](https://www.github.com/googleapis/java-spanner-jdbc/issues/704)) ([bae659c](https://www.github.com/googleapis/java-spanner-jdbc/commit/bae659cac5a010c17767cdf4b3569e654efb605c))

### [2.5.6](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.5.5...v2.5.6) (2021-12-17)


### Bug Fixes

* **java:** add -ntp flag to native image testing command ([#1299](https://www.github.com/googleapis/java-spanner-jdbc/issues/1299)) ([#688](https://www.github.com/googleapis/java-spanner-jdbc/issues/688)) ([4438aca](https://www.github.com/googleapis/java-spanner-jdbc/commit/4438aca73b9c8b33fa1edd23f823d87a093a6d59))


### Dependencies

* update OpenCensus API to 0.30.0 ([#694](https://www.github.com/googleapis/java-spanner-jdbc/issues/694)) ([345f136](https://www.github.com/googleapis/java-spanner-jdbc/commit/345f1366a7dd96f3b28afd353c5c23ebeff60c6b))

### [2.5.5](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.5.4...v2.5.5) (2021-12-03)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.5.1 ([#684](https://www.github.com/googleapis/java-spanner-jdbc/issues/684)) ([a2582d3](https://www.github.com/googleapis/java-spanner-jdbc/commit/a2582d3fbd3f0ea093477914e3a09af235e76595))

### [2.5.4](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.5.3...v2.5.4) (2021-11-17)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.16.0 ([#673](https://www.github.com/googleapis/java-spanner-jdbc/issues/673)) ([b4cc056](https://www.github.com/googleapis/java-spanner-jdbc/commit/b4cc0568e440b6a377cb4d8224c46057cd3ce1ee))

### [2.5.3](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.5.2...v2.5.3) (2021-11-15)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.5.0 ([#668](https://www.github.com/googleapis/java-spanner-jdbc/issues/668)) ([d453234](https://www.github.com/googleapis/java-spanner-jdbc/commit/d45323445d3e4a0753bed6cfe858fa891bca468e))

### [2.5.2](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.5.1...v2.5.2) (2021-11-11)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.15.2 ([#664](https://www.github.com/googleapis/java-spanner-jdbc/issues/664)) ([9f22c33](https://www.github.com/googleapis/java-spanner-jdbc/commit/9f22c331ee4c7340ed6f1b9f91a44ce1e4c5b792))

### [2.5.1](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.5.0...v2.5.1) (2021-10-27)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.15.1 ([#652](https://www.github.com/googleapis/java-spanner-jdbc/issues/652)) ([37d42d9](https://www.github.com/googleapis/java-spanner-jdbc/commit/37d42d91e49da9d30ca0d06b6a01bbe918fc3ab6))

## [2.5.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.4.5...v2.5.0) (2021-10-25)


### Features

* support VIEW in metadata queries ([#633](https://www.github.com/googleapis/java-spanner-jdbc/issues/633)) ([b929191](https://www.github.com/googleapis/java-spanner-jdbc/commit/b929191a7b6699f9daf9a7c06097e9794c79ff8d)), closes [#632](https://www.github.com/googleapis/java-spanner-jdbc/issues/632)


### Bug Fixes

* **java:** java 17 dependency arguments ([#1266](https://www.github.com/googleapis/java-spanner-jdbc/issues/1266)) ([#645](https://www.github.com/googleapis/java-spanner-jdbc/issues/645)) ([0474502](https://www.github.com/googleapis/java-spanner-jdbc/commit/0474502936ff1a43244fcb830fecfc5f42895899))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.4.0 ([#641](https://www.github.com/googleapis/java-spanner-jdbc/issues/641)) ([ab26010](https://www.github.com/googleapis/java-spanner-jdbc/commit/ab26010ba107d4ba9591d661743ad542ae3b227f))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.13.0 ([#637](https://www.github.com/googleapis/java-spanner-jdbc/issues/637)) ([d981c8c](https://www.github.com/googleapis/java-spanner-jdbc/commit/d981c8c744829aa039b16df0b150caf49a99f1cc))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.14.0 ([#647](https://www.github.com/googleapis/java-spanner-jdbc/issues/647)) ([3cda837](https://www.github.com/googleapis/java-spanner-jdbc/commit/3cda83737c25c8878d19bee1727de84e086065b6))
* upgrade Mockito to support Java17 ([#635](https://www.github.com/googleapis/java-spanner-jdbc/issues/635)) ([d78792f](https://www.github.com/googleapis/java-spanner-jdbc/commit/d78792f7acdd2fdac8a655fba06789ba50457679))

### [2.4.5](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.4.4...v2.4.5) (2021-09-29)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.12.5 ([#622](https://www.github.com/googleapis/java-spanner-jdbc/issues/622)) ([b255c54](https://www.github.com/googleapis/java-spanner-jdbc/commit/b255c5434141a900dca30c1e5dbe465b10b88718))

### [2.4.4](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.4.3...v2.4.4) (2021-09-23)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.3.0 ([#614](https://www.github.com/googleapis/java-spanner-jdbc/issues/614)) ([259e395](https://www.github.com/googleapis/java-spanner-jdbc/commit/259e395a2bd1db967a5686b321e752fcab92b500))

### [2.4.3](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.4.2...v2.4.3) (2021-09-14)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.2.1 ([#606](https://www.github.com/googleapis/java-spanner-jdbc/issues/606)) ([36c1791](https://www.github.com/googleapis/java-spanner-jdbc/commit/36c17916e2891d6c13ea6437a328dae8e16ffc13))

### [2.4.2](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.4.1...v2.4.2) (2021-09-02)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.12.2 ([#591](https://www.github.com/googleapis/java-spanner-jdbc/issues/591)) ([0f39b23](https://www.github.com/googleapis/java-spanner-jdbc/commit/0f39b23d66c8d3fb9314affbb67a47c394db3b46))

### [2.4.1](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.4.0...v2.4.1) (2021-08-31)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.2.0 ([#586](https://www.github.com/googleapis/java-spanner-jdbc/issues/586)) ([5c681bf](https://www.github.com/googleapis/java-spanner-jdbc/commit/5c681bf4a1c46535759cf6a0820798141051c1d0))

## [2.4.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.3.5...v2.4.0) (2021-08-27)


### Features

* support JSON data type ([#447](https://www.github.com/googleapis/java-spanner-jdbc/issues/447)) ([ca1c906](https://www.github.com/googleapis/java-spanner-jdbc/commit/ca1c906e1ed3cad6444068ab9c8465401d6d3074))


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.12.1 ([#577](https://www.github.com/googleapis/java-spanner-jdbc/issues/577)) ([a78b177](https://www.github.com/googleapis/java-spanner-jdbc/commit/a78b177f97c298a5b43fcadbca125e957e9f781a))

### [2.3.5](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.3.4...v2.3.5) (2021-08-24)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.12.0 ([#568](https://www.github.com/googleapis/java-spanner-jdbc/issues/568)) ([c032204](https://www.github.com/googleapis/java-spanner-jdbc/commit/c032204445d61c60385232216cdb52c217a85725))

### [2.3.4](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.3.3...v2.3.4) (2021-08-23)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2.1.0 ([#563](https://www.github.com/googleapis/java-spanner-jdbc/issues/563)) ([b0959a4](https://www.github.com/googleapis/java-spanner-jdbc/commit/b0959a412bae1a8024de92d5f9699d49863fb088))

### [2.3.3](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.3.2...v2.3.3) (2021-08-19)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.11.1 ([#556](https://www.github.com/googleapis/java-spanner-jdbc/issues/556)) ([36f0d32](https://www.github.com/googleapis/java-spanner-jdbc/commit/36f0d32aec65f098f7091d44a0e4acc98104aeb9))

### [2.3.2](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.3.1...v2.3.2) (2021-08-12)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.11.0 ([#549](https://www.github.com/googleapis/java-spanner-jdbc/issues/549)) ([2639e40](https://www.github.com/googleapis/java-spanner-jdbc/commit/2639e40ebbde19966653f06f8b664106568f6bac))

### [2.3.1](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.3.0...v2.3.1) (2021-08-12)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.10.1 ([#538](https://www.github.com/googleapis/java-spanner-jdbc/issues/538)) ([75507c4](https://www.github.com/googleapis/java-spanner-jdbc/commit/75507c4a42c3b051e8a14c1233b3b2526c0d3ccc))

## [2.3.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.2.6...v2.3.0) (2021-08-11)


### Features

* add support for tagging to JDBC connection ([#270](https://www.github.com/googleapis/java-spanner-jdbc/issues/270)) ([a4bd82c](https://www.github.com/googleapis/java-spanner-jdbc/commit/a4bd82c8e4ce8b7179b943ac06b049598276f1b4))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v2 ([#544](https://www.github.com/googleapis/java-spanner-jdbc/issues/544)) ([366430d](https://www.github.com/googleapis/java-spanner-jdbc/commit/366430dc270edd09de1a0749ba360f312897b1aa))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.10.0 ([#537](https://www.github.com/googleapis/java-spanner-jdbc/issues/537)) ([8655ae5](https://www.github.com/googleapis/java-spanner-jdbc/commit/8655ae5955f5385a9d6445e13264427d73c4d37e))

### [2.2.6](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.2.5...v2.2.6) (2021-07-06)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.9.1 ([#525](https://www.github.com/googleapis/java-spanner-jdbc/issues/525)) ([37023b8](https://www.github.com/googleapis/java-spanner-jdbc/commit/37023b8295c97304aa0a55a28f71905fcbf5b93f))

### [2.2.5](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.2.4...v2.2.5) (2021-07-06)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.9.0 ([#521](https://www.github.com/googleapis/java-spanner-jdbc/issues/521)) ([8d840ac](https://www.github.com/googleapis/java-spanner-jdbc/commit/8d840ac855f4466c1d53a3b38d964e213708e5e5))

### [2.2.4](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.2.3...v2.2.4) (2021-07-02)


### Bug Fixes

* Add `shopt -s nullglob` to dependencies script ([#514](https://www.github.com/googleapis/java-spanner-jdbc/issues/514)) ([ae51b24](https://www.github.com/googleapis/java-spanner-jdbc/commit/ae51b241148606ffddeb0a703b853de67710e48b))
* prevent relocating urls that start with com like /computeMetadata/ ([#511](https://www.github.com/googleapis/java-spanner-jdbc/issues/511)) ([1178a1d](https://www.github.com/googleapis/java-spanner-jdbc/commit/1178a1d35b4b0032acf71b3dbf862d4f9fb9399c))
* Update dependencies.sh to not break on mac ([#506](https://www.github.com/googleapis/java-spanner-jdbc/issues/506)) ([e205c0c](https://www.github.com/googleapis/java-spanner-jdbc/commit/e205c0c8eba6ac23d747c433b42d8e2365528bd8))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v1.4.0 ([#518](https://www.github.com/googleapis/java-spanner-jdbc/issues/518)) ([045b858](https://www.github.com/googleapis/java-spanner-jdbc/commit/045b8586a7ca7b0e2bd341b27ca3e8a3530c992a))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.7.0 ([#513](https://www.github.com/googleapis/java-spanner-jdbc/issues/513)) ([e1affe3](https://www.github.com/googleapis/java-spanner-jdbc/commit/e1affe358a812a45b9d2c0c9ccd0b00e3aa3791e))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.8.0 ([#517](https://www.github.com/googleapis/java-spanner-jdbc/issues/517)) ([c9013ff](https://www.github.com/googleapis/java-spanner-jdbc/commit/c9013ff48269b158121e4c65c545be30752c31fb))

### [2.2.3](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.2.2...v2.2.3) (2021-06-15)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.6.1 ([#502](https://www.github.com/googleapis/java-spanner-jdbc/issues/502)) ([41a9cd4](https://www.github.com/googleapis/java-spanner-jdbc/commit/41a9cd49fed468f410ad226555f7b9ba46d857b3))

### [2.2.2](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.2.1...v2.2.2) (2021-06-10)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v6.6.0 ([#498](https://www.github.com/googleapis/java-spanner-jdbc/issues/498)) ([5849a97](https://www.github.com/googleapis/java-spanner-jdbc/commit/5849a970087d3fa1d1b42092b4568602563a1dbd))

### [2.2.1](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.2.0...v2.2.1) (2021-06-04)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v1.3.0 ([#490](https://www.github.com/googleapis/java-spanner-jdbc/issues/490)) ([bf0c9d6](https://www.github.com/googleapis/java-spanner-jdbc/commit/bf0c9d6bf612b50a59ea2d530430ccace79aaf35))

## [2.2.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.1.0...v2.2.0) (2021-05-26)


### Features

* add `gcf-owl-bot[bot]` to `ignoreAuthors` ([#474](https://www.github.com/googleapis/java-spanner-jdbc/issues/474)) ([c14f17b](https://www.github.com/googleapis/java-spanner-jdbc/commit/c14f17b411b15e778a68ce998de04732b159d7ac))


### Documentation

* document connection properties in README ([#478](https://www.github.com/googleapis/java-spanner-jdbc/issues/478)) ([3ccc543](https://www.github.com/googleapis/java-spanner-jdbc/commit/3ccc5433bec261b18d2536b04590e7645e47ed9b)), closes [#456](https://www.github.com/googleapis/java-spanner-jdbc/issues/456)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v1.2.0 ([#473](https://www.github.com/googleapis/java-spanner-jdbc/issues/473)) ([a6cc069](https://www.github.com/googleapis/java-spanner-jdbc/commit/a6cc0697ed5916c665f007a1bf16660b8b91f9f9))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.5.0 ([#483](https://www.github.com/googleapis/java-spanner-jdbc/issues/483)) ([e7fec30](https://www.github.com/googleapis/java-spanner-jdbc/commit/e7fec30f2f2c5518821d5348d448f102301d65c3))

## [2.1.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.0.2...v2.1.0) (2021-05-18)


### Features

* allow get/set Spanner Value instances ([#454](https://www.github.com/googleapis/java-spanner-jdbc/issues/454)) ([d6935b8](https://www.github.com/googleapis/java-spanner-jdbc/commit/d6935b863349c58cfdd44d6ce20dba6f5dbc1472)), closes [#452](https://www.github.com/googleapis/java-spanner-jdbc/issues/452)


### Bug Fixes

* NPE was thrown when getting an array of structs from a ResultSet ([#445](https://www.github.com/googleapis/java-spanner-jdbc/issues/445)) ([1dfb37b](https://www.github.com/googleapis/java-spanner-jdbc/commit/1dfb37b27ee661718fe80be0bf260c40f4b15582)), closes [#444](https://www.github.com/googleapis/java-spanner-jdbc/issues/444)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v1.1.0 ([#463](https://www.github.com/googleapis/java-spanner-jdbc/issues/463)) ([f148c71](https://www.github.com/googleapis/java-spanner-jdbc/commit/f148c71bef2b762d7b4475ba7f28443c7938c394))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.4.0 ([#453](https://www.github.com/googleapis/java-spanner-jdbc/issues/453)) ([7dac8b3](https://www.github.com/googleapis/java-spanner-jdbc/commit/7dac8b3e43625aa28be214bd735fc3386770de04))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.4.4 ([#464](https://www.github.com/googleapis/java-spanner-jdbc/issues/464)) ([eeb31c0](https://www.github.com/googleapis/java-spanner-jdbc/commit/eeb31c050fda116203d9da5c4a80c7f1c6a6cac4))

### [2.0.2](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.0.1...v2.0.2) (2021-04-26)


### Bug Fixes

* release scripts from issuing overlapping phases ([#434](https://www.github.com/googleapis/java-spanner-jdbc/issues/434)) ([b2eec0f](https://www.github.com/googleapis/java-spanner-jdbc/commit/b2eec0f079e64f5c21b89bbc0b02e3e981d6469a))
* typo ([#431](https://www.github.com/googleapis/java-spanner-jdbc/issues/431)) ([a0b158b](https://www.github.com/googleapis/java-spanner-jdbc/commit/a0b158bf9931d610779dec51ca61107078e9398e))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.21.1 ([#438](https://www.github.com/googleapis/java-spanner-jdbc/issues/438)) ([aa56b5c](https://www.github.com/googleapis/java-spanner-jdbc/commit/aa56b5c1d5e3b1ccdaa0d5b877deccbda5aa0061))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v1 ([#441](https://www.github.com/googleapis/java-spanner-jdbc/issues/441)) ([df7f0e7](https://www.github.com/googleapis/java-spanner-jdbc/commit/df7f0e796c03f9607e57b4b6ba999c92ea14c58d))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.2.1 ([#430](https://www.github.com/googleapis/java-spanner-jdbc/issues/430)) ([212d9d0](https://www.github.com/googleapis/java-spanner-jdbc/commit/212d9d05c4f28ade71ab5484792188b11a5bcd8b))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.3.3 ([#439](https://www.github.com/googleapis/java-spanner-jdbc/issues/439)) ([a128c4c](https://www.github.com/googleapis/java-spanner-jdbc/commit/a128c4cbe0e6b66f9276b71f7733a46645186e88))

### [2.0.1](https://www.github.com/googleapis/java-spanner-jdbc/compare/v2.0.0...v2.0.1) (2021-04-13)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.21.0 ([#423](https://www.github.com/googleapis/java-spanner-jdbc/issues/423)) ([e0cf14a](https://www.github.com/googleapis/java-spanner-jdbc/commit/e0cf14a4dd087532924f49bb8e0431e1d681c7e8))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6.2.0 ([#420](https://www.github.com/googleapis/java-spanner-jdbc/issues/420)) ([fdd8809](https://www.github.com/googleapis/java-spanner-jdbc/commit/fdd880943394e4760c26eadc3a87d5a298591eb1))

## [2.0.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.21.0...v2.0.0) (2021-03-24)


### ⚠ BREAKING CHANGES

* upgrade to Java 8 and JDBC 4.2 (#397)

### Features

* upgrade to Java 8 and JDBC 4.2 ([#397](https://www.github.com/googleapis/java-spanner-jdbc/issues/397)) ([7eedfbc](https://www.github.com/googleapis/java-spanner-jdbc/commit/7eedfbc78dad15e598d4b678027094ce1467e7f1))


### Performance Improvements

* use PLAN mode to get result metadata ([#388](https://www.github.com/googleapis/java-spanner-jdbc/issues/388)) ([8c7b665](https://www.github.com/googleapis/java-spanner-jdbc/commit/8c7b665c0c16dbec65da5040da038a320efa0a4a))


### Documentation

* add reference to jar-with-dependencies in readme ([#404](https://www.github.com/googleapis/java-spanner-jdbc/issues/404)) ([12c3235](https://www.github.com/googleapis/java-spanner-jdbc/commit/12c3235f4799cd2e74d523d1149c55573437c7ad)), closes [#399](https://www.github.com/googleapis/java-spanner-jdbc/issues/399)


### Dependencies

* update dependency com.google.cloud:google-cloud-spanner-bom to v5.1.0 ([#393](https://www.github.com/googleapis/java-spanner-jdbc/issues/393)) ([9b36a54](https://www.github.com/googleapis/java-spanner-jdbc/commit/9b36a546963b044fc9eaf60667ab013afca6bc54))
* update dependency com.google.cloud:google-cloud-spanner-bom to v5.2.0 ([#398](https://www.github.com/googleapis/java-spanner-jdbc/issues/398)) ([8482652](https://www.github.com/googleapis/java-spanner-jdbc/commit/8482652e6d8933903ab8ccaece8bbe3224d080b5))
* update dependency com.google.cloud:google-cloud-spanner-bom to v6 ([#403](https://www.github.com/googleapis/java-spanner-jdbc/issues/403)) ([3e0fbd1](https://www.github.com/googleapis/java-spanner-jdbc/commit/3e0fbd1706fc269cd7bfcd2258181487cc40cece))

## [1.21.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.20.1...v1.21.0) (2021-03-10)


### Features

* add support for CommitStats ([#261](https://www.github.com/googleapis/java-spanner-jdbc/issues/261)) ([b32e7ae](https://www.github.com/googleapis/java-spanner-jdbc/commit/b32e7aebd4c8d24d052e4616b5dd7735878e01c3))
* allow using UUID in PreparedStatement ([#365](https://www.github.com/googleapis/java-spanner-jdbc/issues/365)) ([4cbee6d](https://www.github.com/googleapis/java-spanner-jdbc/commit/4cbee6dcc2dfea2515437b55b0ecfc956205d739)), closes [#364](https://www.github.com/googleapis/java-spanner-jdbc/issues/364)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.20.1 ([#384](https://www.github.com/googleapis/java-spanner-jdbc/issues/384)) ([f0cdf11](https://www.github.com/googleapis/java-spanner-jdbc/commit/f0cdf117e20325601f1d1c13641267add5b39955))
* update dependency com.google.cloud:google-cloud-spanner-bom to v5 ([#386](https://www.github.com/googleapis/java-spanner-jdbc/issues/386)) ([910c50c](https://www.github.com/googleapis/java-spanner-jdbc/commit/910c50c611cb30b96a2d5d0472afd2d8e3687013))

### [1.20.1](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.20.0...v1.20.1) (2021-02-26)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.20.0 ([#374](https://www.github.com/googleapis/java-spanner-jdbc/issues/374)) ([398d886](https://www.github.com/googleapis/java-spanner-jdbc/commit/398d8864a72029a62b0a3adfaeafaeae76af3e1a))
* update dependency com.google.cloud:google-cloud-spanner-bom to v4.0.2 ([#369](https://www.github.com/googleapis/java-spanner-jdbc/issues/369)) ([3a984c2](https://www.github.com/googleapis/java-spanner-jdbc/commit/3a984c26076f187a710b59c8487e330636319f7c))

## [1.20.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.19.0...v1.20.0) (2021-02-23)


### Features

* allow setting min/max sessions ([#335](https://www.github.com/googleapis/java-spanner-jdbc/issues/335)) ([a5862a5](https://www.github.com/googleapis/java-spanner-jdbc/commit/a5862a5572721fc898cf9b5f4ab8b99631848110))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.19.0 ([fa3721b](https://www.github.com/googleapis/java-spanner-jdbc/commit/fa3721b5d42e0f71247937366f1fa9e3a454beef))
* update dependency com.google.cloud:google-cloud-spanner-bom to v4 ([#359](https://www.github.com/googleapis/java-spanner-jdbc/issues/359)) ([49aa337](https://www.github.com/googleapis/java-spanner-jdbc/commit/49aa337dae08c4c39a7c32c14f92a7c858ad3dc8))
* update dependency com.google.cloud:google-cloud-spanner-bom to v4.0.1 ([#366](https://www.github.com/googleapis/java-spanner-jdbc/issues/366)) ([fa3721b](https://www.github.com/googleapis/java-spanner-jdbc/commit/fa3721b5d42e0f71247937366f1fa9e3a454beef))

## [1.19.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.18.3...v1.19.0) (2021-02-15)


### Features

* allow unknown properties in connection url with lenient mode ([#284](https://www.github.com/googleapis/java-spanner-jdbc/issues/284)) ([0e557ef](https://www.github.com/googleapis/java-spanner-jdbc/commit/0e557ef7657cae04d263daa6717ee34290338b7a))
* Support Array conversion to ResultSet ([#326](https://www.github.com/googleapis/java-spanner-jdbc/issues/326)) ([6ea0a26](https://www.github.com/googleapis/java-spanner-jdbc/commit/6ea0a26ca82565858d8049cc5403a4475edcce33))
* support creating shaded jars ([#333](https://www.github.com/googleapis/java-spanner-jdbc/issues/333)) ([8b4e50d](https://www.github.com/googleapis/java-spanner-jdbc/commit/8b4e50d10a9121334be3d8b5ed0d8fc9ff63c182)), closes [#316](https://www.github.com/googleapis/java-spanner-jdbc/issues/316)
* support default ClientInfo properties ([#324](https://www.github.com/googleapis/java-spanner-jdbc/issues/324)) ([250c4c1](https://www.github.com/googleapis/java-spanner-jdbc/commit/250c4c127f75cc4979e511e2459813f22fec67de))


### Bug Fixes

* getting resultset metadata twice could skip row ([#323](https://www.github.com/googleapis/java-spanner-jdbc/issues/323)) ([f8149af](https://www.github.com/googleapis/java-spanner-jdbc/commit/f8149afb63b9a66e89119290c594b50e599f351a))
* Return entire stack trace for deadline exceeded error ([#347](https://www.github.com/googleapis/java-spanner-jdbc/issues/347)) ([2f94976](https://www.github.com/googleapis/java-spanner-jdbc/commit/2f94976514bfd08afaacc25e802ef1c9717aa75a))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.18.0 ([#320](https://www.github.com/googleapis/java-spanner-jdbc/issues/320)) ([e1cd90d](https://www.github.com/googleapis/java-spanner-jdbc/commit/e1cd90d8afbfa725a92186b85bd446413c8ed4bc))
* update dependency com.google.cloud:google-cloud-spanner-bom to v3.3.1 ([#319](https://www.github.com/googleapis/java-spanner-jdbc/issues/319)) ([7cd990b](https://www.github.com/googleapis/java-spanner-jdbc/commit/7cd990b5ba49f05fba4b1a1ce49f8de133b04868))
* update dependency com.google.cloud:google-cloud-spanner-bom to v3.3.2 ([#325](https://www.github.com/googleapis/java-spanner-jdbc/issues/325)) ([9d65dab](https://www.github.com/googleapis/java-spanner-jdbc/commit/9d65dab248efb5c8e8c5ad56775731891e225b3e))


### Documentation

* fix javadoc formatting ([#343](https://www.github.com/googleapis/java-spanner-jdbc/issues/343)) ([2ac1964](https://www.github.com/googleapis/java-spanner-jdbc/commit/2ac19641d9496eca33f57a034367a4f17bc14f1c))

### [1.18.3](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.18.2...v1.18.3) (2020-12-16)


### Dependencies

* update spanner to 3.1.2 ([#306](https://www.github.com/googleapis/java-spanner-jdbc/issues/306)) ([596e8ed](https://www.github.com/googleapis/java-spanner-jdbc/commit/596e8ed01dc8ffc01c37b233f688d163b8693f85))

### [1.18.2](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.18.1...v1.18.2) (2020-12-16)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.17.0 ([#302](https://www.github.com/googleapis/java-spanner-jdbc/issues/302)) ([9a2efa1](https://www.github.com/googleapis/java-spanner-jdbc/commit/9a2efa14ad402130ca542d5b8b9f9bbb58587404))

### [1.18.1](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.18.0...v1.18.1) (2020-12-14)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.16.1 ([5d5e5fc](https://www.github.com/googleapis/java-spanner-jdbc/commit/5d5e5fccef229e4edd9d34a93553a85a1e97b14f))

## [1.18.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.17.3...v1.18.0) (2020-12-10)


### Features

* expose more methods from Connection in JDBC ([#255](https://www.github.com/googleapis/java-spanner-jdbc/issues/255)) ([697837c](https://www.github.com/googleapis/java-spanner-jdbc/commit/697837ce0ce646a9ca45a0afc3a2e1e368c712f7)), closes [#253](https://www.github.com/googleapis/java-spanner-jdbc/issues/253)
* report whether column is generated in JDBC metadata ([#291](https://www.github.com/googleapis/java-spanner-jdbc/issues/291)) ([9aa9a1f](https://www.github.com/googleapis/java-spanner-jdbc/commit/9aa9a1f8f673554ae71e78937007166f220dd255)), closes [#290](https://www.github.com/googleapis/java-spanner-jdbc/issues/290)


### Documentation

* add connection example to readme ([#281](https://www.github.com/googleapis/java-spanner-jdbc/issues/281)) ([00314e6](https://www.github.com/googleapis/java-spanner-jdbc/commit/00314e643ee6570ed6025630616ad0df70789447))
* fix product docs link ([#282](https://www.github.com/googleapis/java-spanner-jdbc/issues/282)) ([0065a9b](https://www.github.com/googleapis/java-spanner-jdbc/commit/0065a9b319b09e71bf285f85c33514442a163dea))


### Dependencies

* do not re-declare grpc dependencies as test dependencies ([#278](https://www.github.com/googleapis/java-spanner-jdbc/issues/278)) ([4bc59f8](https://www.github.com/googleapis/java-spanner-jdbc/commit/4bc59f8d7f27cee0bbc54b91271e2aadd7cb31da))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.16.0 ([#286](https://www.github.com/googleapis/java-spanner-jdbc/issues/286)) ([2d804f5](https://www.github.com/googleapis/java-spanner-jdbc/commit/2d804f5b52271356598588764c77d1a13f3b7183))
* update dependency com.google.cloud:google-cloud-spanner-bom to v3 ([#260](https://www.github.com/googleapis/java-spanner-jdbc/issues/260)) ([40cdbc0](https://www.github.com/googleapis/java-spanner-jdbc/commit/40cdbc01c91c153c8c3fd36cf7bf91d80b187f03))
* update dependency com.google.cloud:google-cloud-spanner-bom to v3.0.5 ([#287](https://www.github.com/googleapis/java-spanner-jdbc/issues/287)) ([9cef4d5](https://www.github.com/googleapis/java-spanner-jdbc/commit/9cef4d57f6b63caba71ba77160677f73569a8fea))

### [1.17.3](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.17.2...v1.17.3) (2020-11-17)


### Documentation

* add package-info to jdbc ([#264](https://www.github.com/googleapis/java-spanner-jdbc/issues/264)) ([a2d26a1](https://www.github.com/googleapis/java-spanner-jdbc/commit/a2d26a1a9d1595c5a4d766419d3f46619d8d6c71))
* add simple connection sample to readme ([#263](https://www.github.com/googleapis/java-spanner-jdbc/issues/263)) ([3a305ba](https://www.github.com/googleapis/java-spanner-jdbc/commit/3a305ba00b9739ceb17c879f82319b0a6b2a3f9f))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.15.0 ([#237](https://www.github.com/googleapis/java-spanner-jdbc/issues/237)) ([6487a24](https://www.github.com/googleapis/java-spanner-jdbc/commit/6487a24def74e630ee43ec058a267f8d0889c336))
* use google-cloud-spanner-bom ([#258](https://www.github.com/googleapis/java-spanner-jdbc/issues/258)) ([c9906c9](https://www.github.com/googleapis/java-spanner-jdbc/commit/c9906c9440d1574ae74d735aad8b3c255704d59d))

### [1.17.2](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.17.1...v1.17.2) (2020-10-29)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.10.0 ([#219](https://www.github.com/googleapis/java-spanner-jdbc/issues/219)) ([0ab5c5b](https://www.github.com/googleapis/java-spanner-jdbc/commit/0ab5c5b5bee3324cb641f0505068ff99bf3d204d))
* update dependency junit:junit to v4.13.1 ([#232](https://www.github.com/googleapis/java-spanner-jdbc/issues/232)) ([a6c09d7](https://www.github.com/googleapis/java-spanner-jdbc/commit/a6c09d73d707bece320f59dcab98dfde6802a5b3))

### [1.17.1](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.17.0...v1.17.1) (2020-09-21)


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.9.0 ([#199](https://www.github.com/googleapis/java-spanner-jdbc/issues/199)) ([59a7d07](https://www.github.com/googleapis/java-spanner-jdbc/commit/59a7d07c284210033bd1d587b09c44d9c271a52e))

## [1.17.0](https://www.github.com/googleapis/java-spanner-jdbc/compare/v1.16.0...v1.17.0) (2020-09-04)


### Features

* add support for NUMERIC type ([#185](https://www.github.com/googleapis/java-spanner-jdbc/issues/185)) ([4579249](https://www.github.com/googleapis/java-spanner-jdbc/commit/457924980ab0f10fcbb61a0cf1442069f4d0b8b4))


### Dependencies

* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.3 ([#180](https://www.github.com/googleapis/java-spanner-jdbc/issues/180)) ([b446d48](https://www.github.com/googleapis/java-spanner-jdbc/commit/b446d48e40973ef03ec1d3c470a338c371b967a1))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.4 ([#187](https://www.github.com/googleapis/java-spanner-jdbc/issues/187)) ([ddb96f2](https://www.github.com/googleapis/java-spanner-jdbc/commit/ddb96f2424c11d0cde3a4b702a1e3599c0489e96))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.6 ([#189](https://www.github.com/googleapis/java-spanner-jdbc/issues/189)) ([2259332](https://www.github.com/googleapis/java-spanner-jdbc/commit/2259332c7657cd160aef889f88649713dd2fe61e))

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
