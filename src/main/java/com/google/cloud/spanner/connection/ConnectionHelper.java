/*
 * Copyright 2024 Google LLC
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

package com.google.cloud.spanner.connection;

import com.google.cloud.spanner.Spanner;

/** Static helper class to get the {@link Spanner} instance from the underlying connection. */
public class ConnectionHelper {

  /** Private constructor to prevent instantiation. */
  private ConnectionHelper() {}

  public static Spanner getSpanner(Connection connection) {
    // TODO: Remove once getSpanner() has been added to the public interface.
    return ((ConnectionImpl) connection).getSpanner();
  }
}
