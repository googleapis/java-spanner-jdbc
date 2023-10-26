/*
 * Copyright 2023 Google LLC
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

import static org.junit.Assert.assertNotNull;

import com.google.common.base.Stopwatch;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

public class LatencyChecker {
  
  public static void main(String[] args) throws Exception {
    for (String url : new String[] {
        "jdbc:cloudspanner:/projects/cloud-spanner-pg-adapter/instances/pgadapter-ycsb-regional-test/databases/pgadapter-ycsb-test?numChannels=20",
        "jdbc:postgresql://localhost:5432/pgadapter-ycsb-test",
    }) {  

      try (Connection connection = DriverManager.getConnection(url)) {
        System.out.println(url);
        System.out.println("Autocommit: " + connection.getAutoCommit());
        long total = 0L;
        long min = Long.MAX_VALUE;
        for (int run = 0; run < 50; run++) {
          Stopwatch watch = Stopwatch.createStarted();
          try (PreparedStatement statement = connection.prepareStatement(
              "select * from usertable where ycsb_key=?")) {
            statement.setString(1, "user1000053778378872380");
            try (ResultSet resultSet = statement.executeQuery()) {
              while (resultSet.next()) {
                for (int col = 1; col < resultSet.getMetaData().getColumnCount(); col++) {
                  assertNotNull(resultSet.getObject(col));
                  // System.out.println(resultSet.getObject(col));
                }
              }
            }
          }
          System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
          if (run > 0) {
            total += watch.elapsed(TimeUnit.MILLISECONDS);
          }
          min = Math.min(watch.elapsed(TimeUnit.MILLISECONDS), min);
        }
        System.out.println("Total: " + total);
        System.out.println("Min: " + min);
      }
    }
  }

}
