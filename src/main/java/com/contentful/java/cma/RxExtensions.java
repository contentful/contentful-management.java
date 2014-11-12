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

import retrofit.RetrofitError;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * RxExtensions.
 */
class RxExtensions {
  abstract static class AbsAction<T> implements Action1<T> {
    final CMACallback<T> callback;

    public AbsAction(CMACallback<T> callback) {
      this.callback = callback;
    }
  }

  static class ActionSuccess<T> extends AbsAction<T> {
    public ActionSuccess(CMACallback<T> callback) {
      super(callback);
    }

    @Override public void call(T t) {
      if (!callback.isCancelled()) {
        callback.onSuccess(t);
      }
    }
  }

  static class ActionError extends AbsAction<Throwable> {
    @SuppressWarnings("unchecked")
    public ActionError(CMACallback callback) {
      super(callback);
    }

    @Override public void call(Throwable t) {
      if (!callback.isCancelled()) {
        if (t instanceof RetrofitError) {
          callback.onFailure((RetrofitError) t);
        } else {
          callback.onFailure(RetrofitError.unexpectedError(null, t));
        }
      }
    }
  }

  abstract static class DefFunc<R> implements Func0<Observable<R>> {
    @Override public final Observable<R> call() {
      return Observable.just(method());
    }

    abstract R method();
  }
}
