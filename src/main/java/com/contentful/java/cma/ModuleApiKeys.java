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
import com.contentful.java.cma.model.CMAApiKey;
import com.contentful.java.cma.model.CMAArray;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Api Keys Module.
 */
public final class ModuleApiKeys extends AbsModule<ServiceApiKeys> {
  final Async async;

  public ModuleApiKeys(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceApiKeys createService(Retrofit retrofit) {
    return retrofit.create(ServiceApiKeys.class);
  }

  /**
   * Fetch all delivery api keys.
   *
   * @param spaceId the id of the space to host the api keys.
   * @return a list of delivery api keys.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAApiKey> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId).toBlocking().first();
  }

  /**
   * Query for specific api keys.
   *
   * @param spaceId the id of the space to host the api keys.
   * @param query   the terms to query for specific keys.
   * @return a list of delivery api keys.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAApiKey> fetchAll(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    if (query == null) {
      return service.fetchAll(spaceId).toBlocking().first();
    } else {
      return service.fetchAll(spaceId, query).toBlocking().first();
    }
  }

  /**
   * Fetch only one delivery api key.
   *
   * @return one delivery api key.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if keyId is null.
   */
  public CMAApiKey fetchOne(String spaceId, String keyId) {
    assertNotNull(spaceId, "entry");
    assertNotNull(keyId, "keyId");

    return service.fetchOne(spaceId, keyId).toBlocking().first();
  }

  /**
   * Create a new delivery api key.
   *
   * @return the just created key, containing the delivery token.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if key is null.
   */
  public CMAApiKey create(String spaceId, CMAApiKey key) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(key, "key");

    return service.create(spaceId, key).toBlocking().first();
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
     * Fetch all delivery api keys.
     *
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAApiKey>> fetchAll(final String spaceId,
                                                     CMACallback<CMAArray<CMAApiKey>> callback) {
      return defer(new DefFunc<CMAArray<CMAApiKey>>() {
        @Override CMAArray<CMAApiKey> method() {
          return ModuleApiKeys.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Query for specific api keys.
     *
     * @param spaceId the id of the space to host the api keys.
     * @param query   the terms to query for specific keys.
     * @return a list of delivery api keys.
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAApiKey>> fetchAll(final String spaceId,
                                                     final Map<String, String> query,
                                                     CMACallback<CMAArray<CMAApiKey>> callback) {
      return defer(new DefFunc<CMAArray<CMAApiKey>>() {
        @Override CMAArray<CMAApiKey> method() {
          return ModuleApiKeys.this.fetchAll(spaceId, query);
        }
      }, callback);
    }

    /**
     * Fetch only one delivery api key.
     *
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if keyId is null.
     */
    public CMACallback<CMAApiKey> fetchOne(final String spaceId,
                                           final String keyId,
                                           CMACallback<CMAApiKey> callback) {
      return defer(new DefFunc<CMAApiKey>() {
        @Override CMAApiKey method() {
          return ModuleApiKeys.this.fetchOne(spaceId, keyId);
        }
      }, callback);
    }

    /**
     * Create a new delivery api key.
     *
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if key is null.
     */
    public CMACallback<CMAApiKey> create(final String spaceId,
                                         final CMAApiKey key,
                                         CMACallback<CMAApiKey> callback) {
      return defer(new DefFunc<CMAApiKey>() {
        @Override CMAApiKey method() {
          return ModuleApiKeys.this.create(spaceId, key);
        }
      }, callback);
    }
  }
}
