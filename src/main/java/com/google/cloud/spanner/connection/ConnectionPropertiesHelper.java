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

import com.google.api.core.InternalApi;
import com.google.common.collect.ImmutableList;
import java.sql.DriverPropertyInfo;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// TODO: Remove this class when the Connection API has made the list of properties public.
@InternalApi
public class ConnectionPropertiesHelper {
  public static ImmutableList<ConnectionProperty<?>> VALID_CONNECTION_PROPERTIES =
      ImmutableList.copyOf(
          ConnectionProperties.CONNECTION_PROPERTIES.values().stream()
              .sorted(Comparator.comparing(ConnectionProperty::getName))
              .collect(Collectors.toList()));

  public static DriverPropertyInfo toDriverPropertyInfo(
      String connectionUri, ConnectionProperty<?> connectionProperty) {
    DriverPropertyInfo result =
        new DriverPropertyInfo(
            connectionProperty.getName(),
            parseUriProperty(
                connectionUri,
                connectionProperty.getName(),
                connectionProperty.getDefaultValue() == null
                    ? null
                    : connectionProperty.getDefaultValue().toString()));
    result.description = connectionProperty.getDescription();
    result.choices =
        connectionProperty.getValidValues() == null
            ? null
            : Arrays.stream(connectionProperty.getValidValues())
                .map(Object::toString)
                .toArray(String[]::new);
    return result;
  }

  public static String getConnectionPropertyName(ConnectionProperty<?> connectionProperty) {
    return connectionProperty.getName();
  }

  private static String parseUriProperty(String uri, String property, String defaultValue) {
    Pattern pattern = Pattern.compile(String.format("(?is)(?:;|\\?)%s=(.*?)(?:;|$)", property));
    Matcher matcher = pattern.matcher(uri);
    if (matcher.find() && matcher.groupCount() == 1) {
      return matcher.group(1);
    }
    return defaultValue;
  }

  private ConnectionPropertiesHelper() {}
}
