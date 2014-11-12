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

import com.contentful.java.cma.RxExtensions.DefFunc;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Base Module.
 */
abstract class AbsModule<T> {
  final T service;

  AbsModule(T retrofitService) {
    this.service = retrofitService;
  }

  void assertNotNull(Object object, String param) {
    if (object == null) {
      throw new IllegalArgumentException(String.format(
          "%s may not be null.", param));
    }
  }

  String getResourceIdOrThrow(CMAResource resource, String param) {
    String resourceId = resource.getResourceId();
    if (resourceId == null) {
      throw new IllegalArgumentException(String.format(
          "%s.setId() was not called.", param));
    }
    return resourceId;
  }

  String getSpaceIdOrThrow(CMAResource resource, String param) {
    String spaceId = resource.getSpaceId();
    if (spaceId == null) {
      throw new IllegalArgumentException(String.format(
          "%s must have a space associated.", param));
    }
    return spaceId;
  }

  <R> CMACallback<R> defer(DefFunc<R> func, CMACallback<R> callback) {
    assertNotNull(callback, "callback");
    Observable.defer(func)
        .observeOn(Schedulers.io())
        .subscribe(
            new RxExtensions.ActionSuccess<R>(callback),
            new RxExtensions.ActionError(callback));
    return callback;
  }
}
