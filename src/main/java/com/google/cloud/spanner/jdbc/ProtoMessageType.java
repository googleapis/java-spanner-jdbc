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

import com.google.spanner.v1.TypeCode;
import java.sql.PreparedStatement;
import java.sql.SQLType;

/**
 * Custom SQL type for Spanner PROTO data type. This type (or the vendor type number) must be used
 * when setting a PROTO parameter using {@link PreparedStatement#setObject(int, Object, SQLType)}.
 */
public class ProtoMessageType implements SQLType {
  public static final ProtoMessageType INSTANCE = new ProtoMessageType();
  /**
   * Spanner does not have any type numbers, but the code values are unique. Add 100,000 to avoid
   * conflicts with the type numbers in java.sql.Types.
   */
  public static final int VENDOR_TYPE_NUMBER = 100_000 + TypeCode.PROTO_VALUE;

  private ProtoMessageType() {}

  @Override
  public String getName() {
    return "PROTO";
  }

  @Override
  public String getVendor() {
    return ProtoMessageType.class.getPackage().getName();
  }

  @Override
  public Integer getVendorTypeNumber() {
    return VENDOR_TYPE_NUMBER;
  }

  public String toString() {
    return getName();
  }
}
