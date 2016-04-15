/*
 * Copyright (C) 2014 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma;

/**
 * Callback to be used with any of the {@code CMAClient} asynchronous methods.
 *
 * Implement the {@link #onSuccess} method for cases where the request was successful, the result
 * object should be delivered as a parameter.
 *
 * It is also possible, but not mandatory to override {@link #onFailure} and provide an
 * implementation for handling errors.
 *
 * @param <T> the type of object to be expected as a result. For methods that return a collection
 *            of resources it is required to use {@code CMAArray} as the type.
 *
 *            Callback can be cancelled at any point using the {@link #cancel()} method, that will
 *            prevent any future calls to {@link #onSuccess} and
 *            {@link #onFailure(RuntimeException)}.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class CMACallback<T> {
  private boolean cancelled;

  /**
   * Callback to be invoked in case the request was successful.
   *
   * @param result result object
   */
  protected abstract void onSuccess(T result);

  /**
   * Callback to be invoked in case the request was unsuccessful.
   *
   * @param exception{@link RuntimeException} instance
   */
  protected void onFailure(RuntimeException exception) {
    // Do nothing.
  }

  /**
   * Cancels this callback. This will prevent any future calls to {@link #onSuccess(Object)} and
   * {@link #onFailure(RuntimeException)} methods. This action cannot be reversed.
   */
  public synchronized void cancel() {
    this.cancelled = true;
  }

  /**
   * @return true in case this callback instance was previously canceled.
   */
  public synchronized boolean isCancelled() {
    return cancelled;
  }
}
