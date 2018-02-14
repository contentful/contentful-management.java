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
import com.contentful.java.cma.model.CMASystem;
import com.contentful.java.cma.model.CMAUiExtension;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Ui Extensions Module.
 */
public final class ModuleUiExtensions extends AbsModule<ServiceUiExtensions> {
  final Async async;

  /**
   * Create this module.
   *
   * @param retrofit         the retrofit instance to be used to create the service.
   * @param callbackExecutor to tell on which thread it should run.
   */
  public ModuleUiExtensions(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceUiExtensions createService(Retrofit retrofit) {
    return retrofit.create(ServiceUiExtensions.class);
  }

  /**
   * @param spaceId     the id of the space this is valid on.
   * @param extensionId the id of the extension to be fetched.
   * @return the ui extension for a specific space.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if extensionId is null.
   */
  public CMAUiExtension fetchOne(String spaceId, String extensionId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(extensionId, "extensionId");

    return service.fetchOne(spaceId, extensionId).blockingFirst();
  }

  /**
   * Fetch ui extensions from a given space.
   *
   * @param spaceId the id of the space this is valid on.
   * @return all the ui extensions for a specific space.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAUiExtension> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");

    return service.fetchAll(spaceId).blockingFirst();
  }

  /**
   * Fetch all ui extensions from a given space.
   *
   * @param spaceId the id of the space this is valid on.
   * @param query   controls what to return.
   * @return specific ui extensions for a specific space.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAUiExtension> fetchAll(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");

    if (query == null) {
      return service.fetchAll(spaceId).blockingFirst();
    } else {
      return service.fetchAll(spaceId, query).blockingFirst();
    }
  }

  /**
   * Update a ui extension.
   *
   * @param extension the ui extension to be updated.
   * @return the updated ui extension.
   * @throws IllegalArgumentException if extension is null.
   * @throws IllegalArgumentException if extension's version is null.
   * @throws IllegalArgumentException if extension's id is null.
   * @throws IllegalArgumentException if extension's spaceId is null.
   */
  public CMAUiExtension update(CMAUiExtension extension) {
    assertNotNull(extension, "extension");
    final Integer version = getVersionOrThrow(extension, "update");
    final String id = getResourceIdOrThrow(extension, "extension");
    final String spaceId = getSpaceIdOrThrow(extension, "extension");

    final CMASystem system = extension.getSystem();
    extension.setSystem(null);

    try {
      return service.update(spaceId, id, extension, version).blockingFirst();
    } finally {
      extension.setSystem(system);
    }
  }

  /**
   * Delete a ui extension.
   *
   * @param extension the extension to be deleted.
   * @return the http code of the action.
   * @throws IllegalArgumentException if extension is null.
   * @throws IllegalArgumentException if extension's id is null.
   * @throws IllegalArgumentException if extension's version is null.
   * @throws IllegalArgumentException if extension's spaceId is null.
   */
  public Integer delete(CMAUiExtension extension) {
    assertNotNull(extension, "extension");
    final Integer version = getVersionOrThrow(extension, "update");
    final String spaceId = getSpaceIdOrThrow(extension, "extension");
    final String extensionId = getResourceIdOrThrow(extension, "extension");

    final CMASystem system = extension.getSystem();
    extension.setSystem(null);

    try {
      return service.delete(spaceId, extensionId, version).blockingFirst().code();
    } finally {
      extension.setSystem(system);
    }
  }

  /**
   * Create a new ui extension.
   *
   * @param spaceId   the id of the space this ui extension should be created in.
   * @param extension the ui extension to be added.
   * @return the created ui extension.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if extension is null.
   * @throws IllegalArgumentException if extension's id is null.
   * @throws IllegalArgumentException if extension's version is null.
   * @throws IllegalArgumentException if extension's spaceId is null.
   */
  public CMAUiExtension create(String spaceId, CMAUiExtension extension) {
    assertNotNull(extension, "extension");
    assertNotNull(spaceId, "spaceId");

    final String id = extension.getId();

    final CMASystem system = extension.getSystem();
    extension.setSystem(null);

    try {
      if (id == null) {
        return service.create(spaceId, extension).blockingFirst();
      } else {
        return service.create(spaceId, id, extension).blockingFirst();
      }
    } finally {
      extension.setSystem(system);
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
     * Fetch ui extension to given content type from a given space.
     *
     * @param spaceId     the id of the space this is valid on.
     * @param extensionId the id of the extension to be fetched.
     * @param callback    the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if extensionId is null.
     */
    public CMACallback<CMAUiExtension> fetchOne(
        final String spaceId,
        final String extensionId,
        CMACallback<CMAUiExtension> callback) {
      return defer(new DefFunc<CMAUiExtension>() {
        @Override CMAUiExtension method() {
          return ModuleUiExtensions.this.fetchOne(spaceId, extensionId);
        }
      }, callback);
    }

    /**
     * Fetch ui extensions from a given space.
     *
     * @param spaceId  the id of the space this is valid on.
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAUiExtension>> fetchAll(
        final String spaceId,
        CMACallback<CMAArray<CMAUiExtension>> callback) {
      return defer(new DefFunc<CMAArray<CMAUiExtension>>() {
        @Override CMAArray<CMAUiExtension> method() {
          return ModuleUiExtensions.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch specific ui extensions from a given space.
     *
     * @param spaceId  the id of the space to search in.
     * @param query    the query identifying specific ui extensions.
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAUiExtension>> fetchAll(
        final String spaceId,
        final Map<String, String> query,
        CMACallback<CMAArray<CMAUiExtension>> callback) {
      return defer(new DefFunc<CMAArray<CMAUiExtension>>() {
        @Override CMAArray<CMAUiExtension> method() {
          return ModuleUiExtensions.this.fetchAll(spaceId, query);
        }
      }, callback);
    }

    /**
     * Update a ui extension.
     *
     * @param extension the ui extension to be updated.
     * @param callback  the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if extension is null.
     * @throws IllegalArgumentException if extension's version is null.
     * @throws IllegalArgumentException if extension's id is null.
     * @throws IllegalArgumentException if extension's spaceId is null.
     */
    public CMACallback<CMAUiExtension> update(
        final CMAUiExtension extension,
        CMACallback<CMAUiExtension> callback) {
      return defer(new DefFunc<CMAUiExtension>() {
        @Override CMAUiExtension method() {
          return ModuleUiExtensions.this.update(extension);
        }
      }, callback);
    }

    /**
     * Create a new ui extension.
     *
     * @param spaceId   the id of the space this ui extension should be created in.
     * @param extension the ui extension to be added.
     * @param callback  the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if extension is null.
     * @throws IllegalArgumentException if extension's version is null.
     * @throws IllegalArgumentException if extension's id is null.
     * @throws IllegalArgumentException if extension's spaceId is null.
     */
    public CMACallback<CMAUiExtension> create(
        final String spaceId,
        final CMAUiExtension extension,
        CMACallback<CMAUiExtension> callback) {
      return defer(new DefFunc<CMAUiExtension>() {
        @Override CMAUiExtension method() {
          return ModuleUiExtensions.this.create(spaceId, extension);
        }
      }, callback);
    }

    /**
     * Delete a ui extension.
     *
     * @param extension the ui extension to be deleted.
     * @param callback  the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if extension is null.
     * @throws IllegalArgumentException if extension's id is null.
     * @throws IllegalArgumentException if extension's version is null.
     * @throws IllegalArgumentException if extension's spaceId is null.
     */
    public CMACallback<Integer> delete(
        final CMAUiExtension extension,
        CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleUiExtensions.this.delete(extension);
        }
      }, callback);
    }
  }
}