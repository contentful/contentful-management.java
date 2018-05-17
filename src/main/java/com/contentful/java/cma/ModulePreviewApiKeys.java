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
import com.contentful.java.cma.model.CMANotWithEnvironmentsException;
import com.contentful.java.cma.model.CMAPreviewApiKey;

import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Preview Api Keys Module.
 */
public final class ModulePreviewApiKeys extends AbsModule<ServicePreviewApiKeys> {
  final Async async;

  public ModulePreviewApiKeys(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    this.async = new Async();
  }

  @Override protected ServicePreviewApiKeys createService(Retrofit retrofit) {
    return retrofit.create(ServicePreviewApiKeys.class);
  }

  /**
   * Fetch all preview API keys from the configured space.
   *
   * @return a list of preview api keys.
   * @throws IllegalArgumentException        if configured space Id is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMAPreviewApiKey> fetchAll() {
    throwIfEnvironmentIdIsSet();
    return fetchAll(spaceId);
  }

  /**
   * Fetch all preview api keys from the given space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId the id of the space to host the api keys.
   * @return a list of preview api keys.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAPreviewApiKey> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId).blockingFirst();
  }

  /**
   * Fetch only one preview api key from the configured space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param keyId the id of the key itself.
   * @return one preview api key.
   * @throws IllegalArgumentException        if configured spaceId is null.
   * @throws IllegalArgumentException        if keyId is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAPreviewApiKey fetchOne(String keyId) {
    return fetchOne(spaceId, keyId);
  }

  /**
   * Fetch only one preview api key.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId the id of the space this is valid on.
   * @param keyId   the id of the key itself.
   * @return one preview api key.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if keyId is null.
   */
  public CMAPreviewApiKey fetchOne(String spaceId, String keyId) {
    assertNotNull(spaceId, "entry");
    assertNotNull(keyId, "keyId");

    return service.fetchOne(spaceId, keyId).blockingFirst();
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
     * Fetch all preview api keys.
     *
     * @param callback the callback to be informed about success or failure.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAArray<CMAPreviewApiKey>> fetchAll(
        CMACallback<CMAArray<CMAPreviewApiKey>> callback) {
      return defer(new DefFunc<CMAArray<CMAPreviewApiKey>>() {
        @Override CMAArray<CMAPreviewApiKey> method() {
          return ModulePreviewApiKeys.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch all preview api keys.
     *
     * @param spaceId  the id of the space this is valid on.
     * @param callback the callback to be informed about success or failure.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAPreviewApiKey>> fetchAll(
        final String spaceId,
        CMACallback<CMAArray<CMAPreviewApiKey>> callback) {
      return defer(new DefFunc<CMAArray<CMAPreviewApiKey>>() {
        @Override CMAArray<CMAPreviewApiKey> method() {
          return ModulePreviewApiKeys.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch only one preview api key.
     *
     * @param keyId    the id of the key itself.
     * @param callback the callback to be called once the key is available.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws IllegalArgumentException        if keyId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAPreviewApiKey> fetchOne(final String keyId,
                                                  CMACallback<CMAPreviewApiKey> callback) {
      return defer(new DefFunc<CMAPreviewApiKey>() {
        @Override CMAPreviewApiKey method() {
          return ModulePreviewApiKeys.this.fetchOne(keyId);
        }
      }, callback);
    }

    /**
     * Fetch only one preview api key.
     *
     * @param spaceId  the id of the space this is valid on.
     * @param keyId    the id of the key itself.
     * @param callback the callback to be called once the key is available.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if keyId is null.
     */
    public CMACallback<CMAPreviewApiKey> fetchOne(final String spaceId,
                                                  final String keyId,
                                                  CMACallback<CMAPreviewApiKey> callback) {
      return defer(new DefFunc<CMAPreviewApiKey>() {
        @Override CMAPreviewApiKey method() {
          return ModulePreviewApiKeys.this.fetchOne(spaceId, keyId);
        }
      }, callback);
    }
  }
}
