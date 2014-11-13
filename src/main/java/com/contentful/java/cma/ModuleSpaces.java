/*
 * Copyright (C) 2014 Contentful GmbH
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
import retrofit.client.Response;

/**
 * Spaces Module.
 */
class ModuleSpaces extends AbsModule<ServiceSpaces> {
  final Async async;

  ModuleSpaces(ServiceSpaces retrofitService) {
    super(retrofitService);
    this.async = new Async();
  }

  /**
   * Create a Space.
   *
   * @param spaceName Space name
   * @return {@link CMASpace} result instance
   */
  public CMASpace create(String spaceName) {
    assertNotNull(spaceName, "spaceName");
    return service.create(new CMASpace().setName(spaceName));
  }

  /**
   * Create a Space in an Organization.
   *
   * @param spaceName Space name
   * @param organizationId organization ID
   * @return {@link CMASpace} result instance
   */
  public CMASpace create(String spaceName, String organizationId) {
    assertNotNull(spaceName, "spaceName");
    assertNotNull(organizationId, "organizationId");
    return service.create(organizationId, new CMASpace().setName(spaceName));
  }

  /**
   * Delete a Space.
   *
   * @param spaceId Space ID
   * @return Retrofit {@link Response} instance
   */
  public Response delete(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.delete(spaceId);
  }

  /**
   * Fetch all Spaces.
   *
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMASpace> fetchAll() {
    return service.fetchAll();
  }

  /**
   * Fetch a Space with a given {@code spaceId}.
   *
   * @param spaceId Space ID
   * @return {@link CMASpace} result instance
   */
  public CMASpace fetchOne(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchOne(spaceId);
  }

  /**
   * Fetch Locales for a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMALocale> fetchLocales(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchLocales(spaceId);
  }

  /**
   * Update a Space.
   *
   * @param space Space
   * @return {@link CMASpace} result instance
   */
  public CMASpace update(CMASpace space) {
    assertNotNull(space, "space");
    assertNotNull(space.name, "space.name");
    String spaceId = getResourceIdOrThrow(space, "space");

    CMASpace update = new CMASpace();
    update.name = space.name;
    return service.update(space.getVersion(), spaceId, update);
  }

  /**
   * Returns a module with a set of asynchronous methods.
   */
  public Async async() {
    return async;
  }

  /**
   * Async module.
   */
  final class Async {
    /**
     * Create a Space.
     *
     * @param spaceName Space name
     * @param callback Callback
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
     * @param spaceName Space name
     * @param organizationId organization ID
     * @param callback Callback
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
     * @param spaceId Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<Response> delete(final String spaceId, CMACallback<Response> callback) {
      return defer(new DefFunc<Response>() {
        @Override Response method() {
          return ModuleSpaces.this.delete(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch all Spaces.
     *
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAArray<CMASpace>> fetchAll(CMACallback<CMAArray<CMASpace>> callback) {
      return defer(new DefFunc<CMAArray<CMASpace>>() {
        @Override CMAArray<CMASpace> method() {
          return ModuleSpaces.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch Locales for a Space.
     *
     * @param spaceId Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAArray<CMALocale>> fetchLocales(final String spaceId,
        CMACallback<CMAArray<CMALocale>> callback) {
      return defer(new DefFunc<CMAArray<CMALocale>>() {
        @Override CMAArray<CMALocale> method() {
          return ModuleSpaces.this.fetchLocales(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch a Space with a given {@code spaceId}.
     *
     * @param spaceId Space ID
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
     * @param space Space
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
