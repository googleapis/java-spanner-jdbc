/*
 * Copyright 2019 Google LLC
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

import com.google.api.core.InternalApi;
import com.google.cloud.Timestamp;
import com.google.cloud.spanner.AbortedDueToConcurrentModificationException;
import com.google.cloud.spanner.AbortedException;

/** Use {@link com.google.cloud.spanner.connection.TransactionRetryListener} */
@InternalApi
@Deprecated
public interface TransactionRetryListener {
  /** Use {@link com.google.cloud.spanner.connection.TransactionRetryListener.RetryResult} */
  @InternalApi
  @Deprecated
  enum RetryResult {
    /** The retry executed successfully and the transaction will continue. */
    RETRY_SUCCESSFUL,
    /** The retry was aborted by Spanner and another retry attempt will be started. */
    RETRY_ABORTED_AND_RESTARTING,
    /**
     * The retry was aborted by the {@link java.sql.Connection} because of a concurrent
     * modification. The transaction cannot continue and will throw an {@link
     * AbortedDueToConcurrentModificationException}.
     */
    RETRY_ABORTED_DUE_TO_CONCURRENT_MODIFICATION,
    /**
     * The retry was aborted by Spanner and the maximum number of retry attempts allowed has been
     * exceeded. The transaction cannot continue and will throw an {@link AbortedException}.
     */
    RETRY_ABORTED_AND_MAX_ATTEMPTS_EXCEEDED,
    /**
     * An unexpected error occurred during transaction retry, the transaction cannot continue and
     * will throw an exception.
     */
    RETRY_ERROR
  }

  /**
   * This method is called when a retry is about to start.
   *
   * @param transactionStarted The start date/time of the transaction that is retrying.
   * @param transactionId An internally assigned ID of the transaction (unique during the lifetime
   *     of the JVM) that can be used to identify the transaction for logging purposes.
   * @param retryAttempt The number of retry attempts the current transaction has executed,
   *     <strong>including</strong> the current retry attempt.
   */
  void retryStarting(Timestamp transactionStarted, long transactionId, int retryAttempt);

  /**
   * This method is called when a retry has finished.
   *
   * @param transactionStarted The start date/time of the transaction that is retrying.
   * @param transactionId An internally assigned ID of the transaction (unique during the lifetime
   *     of the JVM) that can be used to identify the transaction for logging purposes.
   * @param retryAttempt The number of retry attempts the current transaction has executed,
   *     <strong>including</strong> the current retry attempt.
   * @param result The result of the retry indicating whether the retry was successful or not.
   */
  void retryFinished(
      Timestamp transactionStarted,
      long transactionId,
      int retryAttempt,
      TransactionRetryListener.RetryResult result);
}
