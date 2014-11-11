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
