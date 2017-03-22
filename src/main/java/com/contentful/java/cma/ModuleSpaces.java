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
import com.contentful.java.cma.model.CMALocale;
import com.contentful.java.cma.model.CMASpace;
import com.contentful.java.cma.model.CMASystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Spaces Module.
 */
public final class ModuleSpaces extends AbsModule<ServiceSpaces> {
  final Async async;

  public ModuleSpaces(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceSpaces createService(Retrofit retrofit) {
    return retrofit.create(ServiceSpaces.class);
  }

  /**
   * Create a Space.
   *
   * @param spaceName Space name
   * @return {@link CMASpace} result instance
   */
  public CMASpace create(String spaceName) {
    assertNotNull(spaceName, "spaceName");
    return service.create(new CMASpace().setName(spaceName).setSystem(null)).toBlocking().first();
  }

  /**
   * Create a Space in an Organization.
   *
   * @param spaceName      Space name
   * @param organizationId organization ID
   * @return {@link CMASpace} result instance
   */
  public CMASpace create(String spaceName, String organizationId) {
    assertNotNull(spaceName, "spaceName");
    assertNotNull(organizationId, "organizationId");
    return service.create(organizationId, new CMASpace().setName(spaceName)).toBlocking().first();
  }

  /**
   * Delete a Space.
   *
   * @param spaceId Space ID
   * @return String representing the result (203, or an error code)
   */
  public String delete(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.delete(spaceId).toBlocking().first();
  }

  /**
   * Fetch all Spaces.
   *
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMASpace> fetchAll() {
    return fetchAll(new HashMap<String, String>());
  }

  /**
   * Fetch all Spaces, using specific queries.
   *
   * @param query filter the results
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMASpace> fetchAll(Map<String, String> query) {
    DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
    return service.fetchAll(query).toBlocking().first();
  }

  /**
   * Fetch a Space with a given {@code spaceId}.
   *
   * @param spaceId Space ID
   * @return {@link CMASpace} result instance
   */
  public CMASpace fetchOne(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchOne(spaceId).toBlocking().first();
  }

  /**
   * Fetch Locales for a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMALocale> fetchLocales(String spaceId) {
    return fetchLocales(spaceId, new HashMap<String, String>());
  }

  /**
   * Fetch specific Locales for a Space.
   *
   * @param spaceId Space ID
   * @param query   the filters to be applied for the locales to be fetched
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMALocale> fetchLocales(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
    return service.fetchLocales(spaceId, query).toBlocking().first();
  }

  /**
   * Update a Space.
   *
   * @param space Space
   * @return {@link CMASpace} result instance
   */
  public CMASpace update(CMASpace space) {
    assertNotNull(space, "space");
    assertNotNull(space.getName(), "space.name");
    String spaceId = getResourceIdOrThrow(space, "space");
    Integer version = getVersionOrThrow(space, "update");

    final CMASystem system = space.getSystem();
    space.setSystem(null);

    try {
      return service.update(version, spaceId, space).toBlocking().first();
    } finally {
      space.setSystem(system);
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
     * Create a Space.
     *
     * @param spaceName Space name
     * @param callback  Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMASpace> create(final String spaceName, CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.create(spaceName);
        }
      }, callback);
    }

    /**
     * Create a Space in an Organization.
     *
     * @param spaceName      Space name
     * @param organizationId organization ID
     * @param callback       Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMASpace> create(final String spaceName, final String organizationId,
                                        CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.create(spaceName, organizationId);
        }
      }, callback);
    }

    /**
     * Delete a Space.
     *
     * @param spaceId  Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<String> delete(final String spaceId, CMACallback<String> callback) {
      return defer(new DefFunc<String>() {
        @Override String method() {
          return ModuleSpaces.this.delete(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch all Spaces.
     * <p>
     * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}.
     *
     * @param callback Inform about results on the callback.
     * @return the given {@link CMACallback} instance.
     */
    public CMACallback<CMAArray<CMASpace>> fetchAll(CMACallback<CMAArray<CMASpace>> callback) {
      return fetchAll(new HashMap<String, String>(), callback);
    }

    /**
     * Fetch all Spaces using a non empty query.
     *
     * @param query    used to narrow down on the space requested.
     * @param callback callback to be called, once the result is present.
     * @return the given {@link CMACallback} instance
     */
    public CMACallback<CMAArray<CMASpace>> fetchAll(final Map<String, String> query,
                                                    CMACallback<CMAArray<CMASpace>> callback) {
      return defer(new DefFunc<CMAArray<CMASpace>>() {
        @Override CMAArray<CMASpace> method() {
          DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
          return ModuleSpaces.this.fetchAll(query);
        }
      }, callback);
    }

    /**
     * Fetch Locales for a Space.
     *
     * @param spaceId  Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAArray<CMALocale>> fetchLocales(final String spaceId,
                                                         CMACallback<
                                                             CMAArray<CMALocale>> callback) {
      return defer(new DefFunc<CMAArray<CMALocale>>() {
        @Override CMAArray<CMALocale> method() {
          return ModuleSpaces.this.fetchLocales(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch a Space with a given {@code spaceId}.
     *
     * @param spaceId  Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMASpace> fetchOne(final String spaceId, CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.fetchOne(spaceId);
        }
      }, callback);
    }

    /**
     * Update a Space.
     *
     * @param space    Space
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMASpace> update(final CMASpace space, CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.update(space);
        }
      }, callback);
    }
  }
}
