/*
 * Copyright (C) 2021 Contentful GmbH
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
import com.contentful.java.cma.model.CMAPreviewEnvironment;
import retrofit2.Retrofit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Preview Environments Module.
 */
public class ModulePreviewEnvironments extends AbsModule<ServicePreviewEnvironments> {
  final Async async;

  /**
   * Create preview environments module.
   *
   * @param retrofit                the retrofit instance to be used to create the service.
   * @param callbackExecutor        to tell on which thread it should run.
   * @param spaceId                 the space to be used when not given.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModulePreviewEnvironments(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, null, environmentIdConfigured);
    this.async = new Async();
  }

  @Override
  protected ServicePreviewEnvironments createService(Retrofit retrofit) {
    return retrofit.create(ServicePreviewEnvironments.class);
  }

  /**
   * Fetch all preview environments of the configured space.
   *
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if configured space id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMAPreviewEnvironment> fetchAll() {
    return fetchAll(spaceId, new HashMap<>());
  }

  /**
   * Fetch all preview environments of the given space with query
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)}.
   *
   * @param spaceId space ID
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if environment's space id is null.
   */
  public CMAArray<CMAPreviewEnvironment> fetchAll(String spaceId,
                                   Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
    return service.fetchAll(spaceId, query).blockingFirst();
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
  public class Async {

    /**
     * Fetch all preview environments
     * <p>
     *
     * @param callback Inform about results on the callback.
     * @return the given {@link CMACallback} instance.
     * @throws IllegalArgumentException if configured space id is null.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAArray<CMAPreviewEnvironment>> fetchAll(
        CMACallback<CMAArray<CMAPreviewEnvironment>> callback) {
      return defer(new DefFunc<CMAArray<CMAPreviewEnvironment>>() {
        @Override
        CMAArray<CMAPreviewEnvironment> method() {
          return ModulePreviewEnvironments.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch all preview environments of the given space.
     * <p>
     * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)}.
     *
     * @param spaceId  Id of the space
     * @param callback Inform about results on the callback.
     * @return the given {@link CMACallback} instance.
     * @throws IllegalArgumentException if space id is null.
     */
    public CMACallback<CMAArray<CMAPreviewEnvironment>> fetchAll(
        final String spaceId,
        final Map<String, String> query,
        CMACallback<CMAArray<CMAPreviewEnvironment>> callback) {
      return defer(new DefFunc<CMAArray<CMAPreviewEnvironment>>() {
        @Override
        CMAArray<CMAPreviewEnvironment> method() {
          return ModulePreviewEnvironments.this.fetchAll(spaceId, query);
        }
      }, callback);
    }
  }
}
