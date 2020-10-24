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
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMASpace;
import com.contentful.java.cma.model.CMASystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Spaces Module.
 */
public class ModuleSpaces extends AbsModule<ServiceSpaces> {
  final Async async;

  /**
   * Create the spaces module.
   *
   * @param retrofit                the retrofit instance to be used to create the service.
   * @param callbackExecutor        to tell on which thread it should run.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModuleSpaces(
      Retrofit retrofit,
      Executor callbackExecutor,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, null, null, environmentIdConfigured);
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
    return service.create(new CMASpace().setName(spaceName).setSystem(null)).blockingFirst();
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
      return service.create(space).blockingFirst();
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
        .blockingFirst();
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
      return service.create(organizationId, space).blockingFirst();
    } finally {
      space.setSystem(system);
    }
  }

  /**
   * Delete a Space.
   *
   * @param spaceId Space ID
   * @return Integer representing the result (204, or an error code)
   * @throws IllegalArgumentException if space's id is null.
   */
  public Integer delete(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.delete(spaceId).blockingFirst().code();
  }

  /**
   * Delete a Space.
   *
   * @param space Space
   * @return Integer representing the result (204, or an error code)
   * @throws IllegalArgumentException if space's id is null.
   */
  public Integer delete(CMASpace space) {
    assertNotNull(space.getId(), "spaceId");
    return service.delete(space.getId()).blockingFirst().code();
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
    Map<String, String> enhancedQuery =
      DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
    return service.fetchAll(enhancedQuery).blockingFirst();
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
    return service.fetchOne(spaceId).blockingFirst();
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
      return service.update(version, spaceId, space).blockingFirst();
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
  public class Async {
    /**
     * Create a Space.
     *
     * @param spaceName Space name
     * @param callback  Callback
     * @return the given CMACallback instance
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
     * @return the given CMACallback instance
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
     * @return the given CMACallback instance
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
     * @return the given CMACallback instance
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
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if space's id is null.
     */
    public CMACallback<Integer> delete(final String spaceId,
                                       CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleSpaces.this.delete(spaceId);
        }
      }, callback);
    }

    /**
     * Delete a Space.
     *
     * @param space    Space
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if space's id is null.
     */
    public CMACallback<Integer> delete(final CMASpace space,
                                       CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleSpaces.this.delete(space);
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
          Map<String, String> enhancedQuery =
            DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
          return ModuleSpaces.this.fetchAll(enhancedQuery);
        }
      }, callback);
    }

    /**
     * Fetch a Space with a given {@code spaceId}.
     *
     * @param spaceId  Space ID
     * @param callback Callback
     * @return the given CMACallback instance
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
     * @return the given CMACallback instance
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
