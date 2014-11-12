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
    return service.spacesCreate(new CMASpace().setName(spaceName));
  }

  /**
   * Create a Space in an Organization.
   *
   * @param spaceName Space name
   * @param organizationId Organization ID
   * @return {@link CMASpace} result instance
   */
  public CMASpace create(String spaceName, String organizationId) {
    assertNotNull(spaceName, "spaceName");
    assertNotNull(organizationId, "organizationId");
    return service.spacesCreate(organizationId, new CMASpace().setName(spaceName));
  }

  /**
   * Delete a Space.
   *
   * @param spaceId Space ID
   * @return Retrofit {@link Response} instance
   */
  public Response delete(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.spacesDelete(spaceId);
  }

  /**
   * Fetch all Spaces.
   *
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMASpace> fetchAll() {
    return service.spacesFetchAll();
  }

  /**
   * Fetch a Space with a given {@code spaceId}.
   *
   * @param spaceId Space ID
   * @return {@link CMASpace} result instance
   */
  public CMASpace fetchOne(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.spacesFetchOne(spaceId);
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
    return service.spacesUpdate(space.getVersion(), spaceId, update);
  }

  public Async async() {
    return async;
  }

  final class Async {
    public CMACallback<CMASpace> create(final String spaceName, CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.create(spaceName);
        }
      }, callback);
    }

    public CMACallback<CMASpace> create(final String spaceName, final String organizationId,
        CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.create(spaceName, organizationId);
        }
      }, callback);
    }

    public CMACallback<Response> delete(final String spaceId, CMACallback<Response> callback) {
      return defer(new DefFunc<Response>() {
        @Override Response method() {
          return ModuleSpaces.this.delete(spaceId);
        }
      }, callback);
    }

    public CMACallback<CMAArray<CMASpace>> fetchAll(CMACallback<CMAArray<CMASpace>> callback) {
      return defer(new DefFunc<CMAArray<CMASpace>>() {
        @Override CMAArray<CMASpace> method() {
          return ModuleSpaces.this.fetchAll();
        }
      }, callback);
    }

    public CMACallback<CMASpace> fetchOne(final String spaceId, CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.fetchOne(spaceId);
        }
      }, callback);
    }

    public CMACallback<CMASpace> update(final CMASpace space, CMACallback<CMASpace> callback) {
      return defer(new DefFunc<CMASpace>() {
        @Override CMASpace method() {
          return ModuleSpaces.this.update(space);
        }
      }, callback);
    }
  }
}
