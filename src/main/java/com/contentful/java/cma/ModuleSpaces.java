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
   * @throws IllegalArgumentException if spaceName is null.
   */
  public CMASpace create(String spaceName) {
    assertNotNull(spaceName, "spaceName");
    return service.create(new CMASpace().setName(spaceName).setSystem(null)).toBlocking().first();
  }

  /**
   * Create a Space.
   *
   * @param space CMASpace
   * @return {@link CMASpace} result instance
   * @throws IllegalArgumentException if space is null.
   */
  public CMASpace create(CMASpace space) {
    assertNotNull(space, "space");

    final CMASystem system = space.getSystem();
    space.setSystem(null);

    try {
      return service.create(space).toBlocking().first();
    } finally {
      space.setSystem(system);
    }
  }

  /**
   * Create a Space in an Organization.
   *
   * @param spaceName      Space name
   * @param organizationId organization ID
   * @return {@link CMASpace} result instance
   * @throws IllegalArgumentException if spaceName is null.
   * @throws IllegalArgumentException if organizationId is null.
   */
  public CMASpace create(String spaceName, String organizationId) {
    assertNotNull(spaceName, "spaceName");
    assertNotNull(organizationId, "organizationId");

    return service.create(organizationId, new CMASpace().setName(spaceName).setSystem(null))
        .toBlocking().first();
  }

  /**
   * Create a Space in an organization.
   *
   * @param space          Space
   * @param organizationId organization ID
   * @return {@link CMASpace} result instance
   * @throws IllegalArgumentException if space is null.
   * @throws IllegalArgumentException if space's name is null.
   * @throws IllegalArgumentException if organizationId is null.
   */
  public CMASpace create(CMASpace space, String organizationId) {
    assertNotNull(space, "space");
    assertNotNull(space.getName(), "spaceName");
    assertNotNull(organizationId, "organizationId");

    final CMASystem system = space.getSystem();
    space.setSystem(null);

    try {
      return service.create(organizationId, space).toBlocking().first();
    } finally {
      space.setSystem(system);
    }
  }

  /**
   * Delete a Space.
   *
   * @param spaceId Space ID
   * @return String representing the result (203, or an error code)
   * @throws IllegalArgumentException if space's id is null.
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
   * @throws IllegalArgumentException if space's id is null.
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
   * @throws IllegalArgumentException if space's id is null.
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
   * @throws IllegalArgumentException if space's id is null.
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
   * @throws IllegalArgumentException if space is null.
   * @throws IllegalArgumentException if space's name is null.
   * @throws IllegalArgumentException if space's space id is null.
   * @throws IllegalArgumentException if space's version is null.
   */
  public CMASpace update(CMASpace space) {
    assertNotNull(space, "space");
    assertNotNull(space.getName(), "space.name");
    final String spaceId = getResourceIdOrThrow(space, "space");
    final Integer version = getVersionOrThrow(space, "update");

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
     * @throws IllegalArgumentException if spaceName is null.
     */
    public CMACallback<CMASpace> create(final String spaceName, CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.create(spaceName);
        }
      }, callback);
    }

    /**
     * Create a Space.
     *
     * @param space    CMASpace
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if space is null.
     */
    public CMACallback<CMASpace> create(final CMASpace space, CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.create(space);
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
     * @throws IllegalArgumentException if spaceName is null.
     * @throws IllegalArgumentException if organizationId is null.
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
     * Create a Space in an Organization.
     *
     * @param space          CMASpace
     * @param organizationId organization ID
     * @param callback       Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if space is null.
     * @throws IllegalArgumentException if space's name is null.
     * @throws IllegalArgumentException if organizationId is null.
     */
    public CMACallback<CMASpace> create(final CMASpace space, final String organizationId,
                                        CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.create(space, organizationId);
        }
      }, callback);
    }

    /**
     * Delete a Space.
     *
     * @param spaceId  Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if space's id is null.
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
     * @throws IllegalArgumentException if space's id is null.
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
     * Fetch Locales for a Space, using a query.
     *
     * @param spaceId  Space ID
     * @param query    the query to be used.
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if space's id is null.
     */
    public CMACallback<CMAArray<CMALocale>> fetchLocales(final String spaceId,
                                                         final Map<String, String> query,
                                                         CMACallback<
                                                             CMAArray<CMALocale>> callback) {
      return defer(new DefFunc<CMAArray<CMALocale>>() {
        @Override CMAArray<CMALocale> method() {
          return ModuleSpaces.this.fetchLocales(spaceId, query);
        }
      }, callback);
    }

    /**
     * Fetch a Space with a given {@code spaceId}.
     *
     * @param spaceId  Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if space's id is null.
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
     * @throws IllegalArgumentException if space is null.
     * @throws IllegalArgumentException if space's name is null.
     * @throws IllegalArgumentException if space's space id is null.
     * @throws IllegalArgumentException if space's version is null.
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
