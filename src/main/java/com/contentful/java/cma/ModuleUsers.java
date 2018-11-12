/*
 * Copyright (C) 2018 Contentful GmbH
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
import com.contentful.java.cma.model.CMAUser;

import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Users Module.
 */
public final class ModuleUsers extends AbsModule<ServiceUsers> {
  final Async async;

  /**
   * Create module.
   *
   * @param retrofit         the retrofit instance to be used to create the service.
   * @param callbackExecutor to tell on which thread it should run.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModuleUsers(
      Retrofit retrofit,
      Executor callbackExecutor,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, null, null, environmentIdConfigured);
    this.async = new Async();
  }

  @Override protected ServiceUsers createService(Retrofit retrofit) {
    return retrofit.create(ServiceUsers.class);
  }

  /**
   * Fetch your user information.
   *
   * @return {@link CMAUser} result instance
   */
  public CMAUser fetchMe() {
    return service.fetchMe().blockingFirst();
  }

  /**
   * @return a module with a set of asynchronous methods.
   */
  public Async async() {
    return async;
  }

  /**
   * Async module.
   */
  public final class Async {
    /**
     * Fetch your user information.
     *
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     */
    public CMACallback<CMAUser> fetchMe(CMACallback<CMAUser> callback) {
      return defer(new DefFunc<CMAUser>() {
        @Override CMAUser method() {
          return ModuleUsers.this.fetchMe();
        }
      }, callback);
    }
  }
}
