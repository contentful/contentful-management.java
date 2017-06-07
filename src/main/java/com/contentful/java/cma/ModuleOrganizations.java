/*
 * Copyright (C) 2017 Contentful GmbH
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
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAOrganization;

import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Organizations Module.
 */
public final class ModuleOrganizations extends AbsModule<ServiceOrganizations> {
  final Async async;

  public ModuleOrganizations(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceOrganizations createService(Retrofit retrofit) {
    return retrofit.create(ServiceOrganizations.class);
  }

  /**
   * Fetch all organizations the token has access to.
   *
   * @return {@link CMAOrganization} result instance
   */
  public CMAArray<CMAOrganization> fetchAll() {
    return service.fetchAll().toBlocking().first();
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
     * Fetch all organizations accessible.
     *
     * @return {@link CMAOrganization} result instance
     */
    public CMACallback<CMAArray<CMAOrganization>> fetchAll(
        CMACallback<CMAArray<CMAOrganization>> callback) {
      return defer(new DefFunc<CMAArray<CMAOrganization>>() {
        @Override CMAArray<CMAOrganization> method() {
          return ModuleOrganizations.this.fetchAll();
        }
      }, callback);
    }
  }
}
