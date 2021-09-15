/*
 * Copyright (C) 2019 Contentful GmbH
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
import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMANotWithEnvironmentsException;
import com.contentful.java.cma.model.CMASystem;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Api Keys Module.
 */
public class ModuleApiKeys extends AbsModule<ServiceApiKeys> {
  final Async async;

  /**
   * Create api keys module.
   *
   * @param retrofit                the retrofit instance to be used to create the service.
   * @param callbackExecutor        to tell on which thread it should run.
   * @param spaceId                 the space to be used when not given.
   * @param environmentId           the environment to be used when not given.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModuleApiKeys(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    this.async = new Async();
  }

  @Override protected ServiceApiKeys createService(Retrofit retrofit) {
    return retrofit.create(ServiceApiKeys.class);
  }

  /**
   * Fetch all delivery API keys from the configured space.
   *
   * @return a list of delivery api keys.
   * @throws IllegalArgumentException        if configured space Id is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMAApiKey> fetchAll() {
    throwIfEnvironmentIdIsSet();
    return fetchAll(spaceId);
  }

  /**
   * Fetch all delivery api keys from the given space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId the id of the space to host the api keys.
   * @return a list of delivery api keys.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAApiKey> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId).blockingFirst();
  }

  /**
   * Query for specific api keys from the configured space.
   *
   * @param query the terms to query for specific keys.
   * @return a list of delivery api keys.
   * @throws IllegalArgumentException        if configured space Id is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMAApiKey> fetchAll(Map<String, String> query) {
    throwIfEnvironmentIdIsSet();
    return fetchAll(spaceId, query);
  }

  /**
   * Query for specific api keys, overriding the configuration set.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId the id of the space to host the api keys.
   * @param query   the terms to query for specific keys.
   * @return a list of delivery api keys.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAApiKey> fetchAll(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");

    return service.fetchAll(spaceId, query).blockingFirst();
  }

  /**
   * Fetch only one delivery api key from the configured space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param keyId the id of the key itself.
   * @return one delivery api key.
   * @throws IllegalArgumentException        if configured spaceId is null.
   * @throws IllegalArgumentException        if keyId is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAApiKey fetchOne(String keyId) {
    return fetchOne(spaceId, keyId);
  }

  /**
   * Fetch only one delivery api key.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId the id of the space this is valid on.
   * @param keyId   the id of the key itself.
   * @return one delivery api key.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if keyId is null.
   */
  public CMAApiKey fetchOne(String spaceId, String keyId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(keyId, "keyId");

    return service.fetchOne(spaceId, keyId).blockingFirst();
  }


  /**
   * Fetch only one preview api key.
   *
   * @param spaceId the id of the space this is valid on.
   * @param keyId   the id of the key itself.
   * @return one preview api key.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if keyId is null.
   */
  public CMAApiKey fetchOnePreview(String spaceId, String keyId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(keyId, "keyId");

    return service.fetchOnePreview(spaceId, keyId).blockingFirst();
  }

  /**
   * Create a new delivery api key from the configured space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param key the key to be created.
   * @return the just created key, containing the delivery token.
   * @throws IllegalArgumentException        if configured spaceId is null.
   * @throws IllegalArgumentException        if key is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAApiKey create(CMAApiKey key) {
    return create(spaceId, key);
  }

  /**
   * Updates a delivery api key from the configured space.
   *
   * @param key the key to be updated.
   * @return the just updated key.
   * @throws IllegalArgumentException if key is null.
   * @throws IllegalArgumentException if keys id is null.
   * @throws IllegalArgumentException if keys spaceId is null.
   */
  public CMAApiKey update(CMAApiKey key) {
    assertNotNull(key, "key");
    final String keyId = getResourceIdOrThrow(key, "key");
    final String spaceId = getSpaceIdOrThrow(key, "key");
    final Integer version = getVersionOrThrow(key, "update");

    final CMASystem system = key.getSystem();
    key.setSystem(null);

    final String token = key.getAccessToken();
    key.setAccessToken(null);

    final CMALink previewKey = key.getPreviewApiKey();
    key.setPreviewApiKey(null);

    try {
      return service.update(version, spaceId, keyId, key).blockingFirst();
    } finally {
      key.setPreviewApiKey(previewKey);
      key.setAccessToken(token);
      key.setSystem(system);
    }
  }

  /**
   * Create a new delivery api key.
   *
   * @param spaceId the id of the space this is valid on.
   * @param key     the key to be created.
   * @return the just created key, containing the delivery token.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if key is null.
   */
  public CMAApiKey create(String spaceId, CMAApiKey key) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(key, "key");

    return service.create(spaceId, key).blockingFirst();
  }

  /**
   * Delete a given api key from the configured space.
   *
   * @param key the key to be deleted.
   * @return 204 upon success.
   * @throws IllegalArgumentException if key is null.
   * @throws IllegalArgumentException if key's spaceId is null.
   */
  public int delete(CMAApiKey key) {
    assertNotNull(key, "key");
    final String space = getSpaceIdOrThrow(key, "key");
    final String id = getResourceIdOrThrow(key, "key");

    return service.delete(space, id).blockingFirst().code();
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
     * Fetch all delivery api keys.
     *
     * @param callback the callback to be informed about success or failure.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAArray<CMAApiKey>> fetchAll(CMACallback<CMAArray<CMAApiKey>> callback) {
      return defer(new DefFunc<CMAArray<CMAApiKey>>() {
        @Override CMAArray<CMAApiKey> method() {
          return ModuleApiKeys.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch all delivery api keys.
     *
     * @param spaceId  the id of the space this is valid on.
     * @param callback the callback to be informed about success or failure.
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
     * @param query    the terms to query for specific keys.
     * @param callback the callback to be informed about success or failure.
     * @return the callback to be informed about success of failure.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAArray<CMAApiKey>> fetchAll(final Map<String, String> query,
                                                     CMACallback<CMAArray<CMAApiKey>> callback) {
      return defer(new DefFunc<CMAArray<CMAApiKey>>() {
        @Override CMAArray<CMAApiKey> method() {
          return ModuleApiKeys.this.fetchAll(query);
        }
      }, callback);
    }

    /**
     * Query for specific api keys.
     *
     * @param spaceId  the id of the space to host the api keys.
     * @param query    the terms to query for specific keys.
     * @param callback the callback to be informed about success or failure.
     * @return the callback to be informed about success of failure.
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
     * @param keyId    the id of the key itself.
     * @param callback the callback to be called once the key is available.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws IllegalArgumentException        if keyId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAApiKey> fetchOne(final String keyId,
                                           CMACallback<CMAApiKey> callback) {
      return defer(new DefFunc<CMAApiKey>() {
        @Override CMAApiKey method() {
          return ModuleApiKeys.this.fetchOne(keyId);
        }
      }, callback);
    }

    /**
     * Fetch only one delivery api key.
     *
     * @param spaceId  the id of the space this is valid on.
     * @param keyId    the id of the key itself.
     * @param callback the callback to be called once the key is available.
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
     * Fetch only one preview api key.
     *
     * @param spaceId  the id of the space this is valid on.
     * @param keyId    the id of the key itself.
     * @param callback the callback to be called once the key is available.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if keyId is null.
     */
    public CMACallback<CMAApiKey> fetchOnePreview(final String spaceId,
                                                  final String keyId,
                                                  CMACallback<CMAApiKey> callback) {
      return defer(new DefFunc<CMAApiKey>() {
        @Override CMAApiKey method() {
          return ModuleApiKeys.this.fetchOnePreview(spaceId, keyId);
        }
      }, callback);
    }

    /**
     * Create a new delivery api key.
     *
     * @param key      the key to be created.
     * @param callback the callback to be called once the key is available.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws IllegalArgumentException        if key is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAApiKey> create(final CMAApiKey key,
                                         CMACallback<CMAApiKey> callback) {
      return defer(new DefFunc<CMAApiKey>() {
        @Override CMAApiKey method() {
          return ModuleApiKeys.this.create(key);
        }
      }, callback);
    }

    /**
     * Create a new delivery api key.
     *
     * @param spaceId  the id of the space this is valid on.
     * @param key      the key to be created.
     * @param callback the callback to be called once the key is available.
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

    /**
     * Update an existing delivery api key.
     *
     * @param key      the key to be updated.
     * @param callback the callback to be called once the key is available.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws IllegalArgumentException        if key is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAApiKey> update(final CMAApiKey key,
                                         CMACallback<CMAApiKey> callback) {
      return defer(new DefFunc<CMAApiKey>() {
        @Override CMAApiKey method() {
          return ModuleApiKeys.this.update(key);
        }
      }, callback);
    }

    /**
     * Delete a given delivery api key.
     *
     * @param key      the key to be deleted.
     * @param callback the callback to be called once the key is available.
     * @return the callback to be informed about success or failure.
     * @throws IllegalArgumentException        if key is null.
     * @throws IllegalArgumentException        if key's spaceId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<Integer> delete(final CMAApiKey key,
                                       CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleApiKeys.this.delete(key);
        }
      }, callback);
    }
  }
}
