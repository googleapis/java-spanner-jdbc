# Changelog

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
