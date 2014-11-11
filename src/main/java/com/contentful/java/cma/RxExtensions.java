package com.contentful.java.cma;

import retrofit.RetrofitError;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by tomxor on 11/11/14.
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
