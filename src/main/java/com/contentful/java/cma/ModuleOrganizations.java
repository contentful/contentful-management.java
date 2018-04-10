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
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAOrganization;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Organizations Module.
 */
public final class ModuleOrganizations extends AbsModule<ServiceOrganizations> {
  final Async async;

  public ModuleOrganizations(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor, null, null);
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
    return service.fetchAll().blockingFirst();
  }

  /**
   * Fetch specific organizations the token has access to.
   *
   * @param query the criteria to narrow down the search result.
   * @return {@link CMAOrganization} result instance
   */
  public CMAArray<CMAOrganization> fetchAll(Map<String, String> query) {
    if (query == null) {
      return service.fetchAll().blockingFirst();
    } else {
      return service.fetchAll(query).blockingFirst();
    }
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
     * @param callback the callback to be informed about success or failure.
     * @return {@link CMAOrganization} result callback.
     */
    public CMACallback<CMAArray<CMAOrganization>> fetchAll(
        CMACallback<CMAArray<CMAOrganization>> callback) {
      return defer(new DefFunc<CMAArray<CMAOrganization>>() {
        @Override CMAArray<CMAOrganization> method() {
          return ModuleOrganizations.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch specific organizations accessible.
     *
     * @param query    the definition of organizations to be returned.
     * @param callback the callback to be informed about success or failure.
     * @return {@link CMAOrganization} result callback.
     */
    public CMACallback<CMAArray<CMAOrganization>> fetchAll(
        final Map<String, String> query,
        CMACallback<CMAArray<CMAOrganization>> callback) {
      return defer(new DefFunc<CMAArray<CMAOrganization>>() {
        @Override CMAArray<CMAOrganization> method() {
          return ModuleOrganizations.this.fetchAll(query);
        }
      }, callback);
    }
  }
}
