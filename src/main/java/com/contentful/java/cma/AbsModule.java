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

import com.contentful.java.cma.RxExtensions.ActionError;
import com.contentful.java.cma.RxExtensions.ActionSuccess;
import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAResource;
import java.util.concurrent.Executor;
import retrofit.RestAdapter;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Base Module.
 */
abstract class AbsModule<T> {
  final T service;
  final Executor callbackExecutor;

  AbsModule(RestAdapter restAdapter, Executor callbackExecutor) {
    this.service = createService(restAdapter);
    this.callbackExecutor = callbackExecutor;
  }

  protected abstract T createService(RestAdapter restAdapter);

  /**
   * Asserts that the given {@code object} with name {@code param} is not null, throws
   * {@link IllegalArgumentException} otherwise.
   */
  void assertNotNull(Object object, String param) {
    if (object == null) {
      throw new IllegalArgumentException(String.format(
          "%s may not be null.", param));
    }
  }

  /**
   * Extracts the resource ID from the given {@code resource} of name {@code param}.
   * Throws {@link IllegalArgumentException} if the value is not present.
   */
  String getResourceIdOrThrow(CMAResource resource, String param) {
    String resourceId = resource.getResourceId();
    if (resourceId == null) {
      throw new IllegalArgumentException(String.format(
          "%s.setId() was not called.", param));
    }
    return resourceId;
  }

  /**
   * Extracts the Space ID from the given {@code resource} of name {@code param}.
   * Throws {@link IllegalArgumentException} if the value is not present.
   */
  String getSpaceIdOrThrow(CMAResource resource, String param) {
    String spaceId = resource.getSpaceId();
    if (spaceId == null) {
      throw new IllegalArgumentException(String.format(
          "%s must have a space associated.", param));
    }
    return spaceId;
  }

  /**
   * Extracts the version number for the given {@code resource}.
   * Throws {@link IllegalArgumentException} if the value is not present.
   */
  Integer getVersionOrThrow(CMAResource resource, String action) {
    Integer version = resource.getVersion();
    if (version == null) {
      throw new IllegalArgumentException(String.format(
          "Cannot perform %s action on a resource that has no version associated.",
          action));
    }
    return version;
  }

  /**
   * Creates an Observable with the given {@code func} function and subscribes to it
   * with a set of pre-defined actions. The provided {@code callback} will be passed to these
   * actions in order to populate the events.
   */
  <R> CMACallback<R> defer(DefFunc<R> func, CMACallback<R> callback) {
    assertNotNull(callback, "callback");
    Observable.defer(func)
        .subscribeOn(Schedulers.io())
        .subscribe(
            new ActionSuccess<R>(callbackExecutor, callback),
            new ActionError(callbackExecutor, callback));
    return callback;
  }
}
