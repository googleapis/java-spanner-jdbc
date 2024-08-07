#!/bin/bash

# Generate Data
cd ../..

mvn -q exec:java -Dexec.mainClass="com.google.cloud.jdbc.quickperf.QuickPerf" \
-Dexec.args="-c exampleconfigs/users/users_config.json"

mvn  -q exec:java -Dexec.mainClass="com.google.cloud.jdbc.quickperf.QuickPerf" \
-Dexec.args="-c exampleconfigs/users/groupmgt_config.json"

mvn  -q exec:java -Dexec.mainClass="com.google.cloud.jdbc.quickperf.QuickPerf" \
-Dexec.args="-c exampleconfigs/users/membership_config.json"

# load test random users
mvn  -q exec:java -Dexec.mainClass="com.google.cloud.jdbc.quickperf.QuickPerf" \
-Dexec.args="-c exampleconfigs/users/loadtestusers.json"