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
import com.contentful.java.cma.model.CMAEditorInterface;
import com.contentful.java.cma.model.CMASystem;

import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Editor Interfaces Module.
 */
public final class ModuleEditorInterfaces extends AbsModule<ServiceEditorInterfaces> {
  final Async async;

  /**
   * Create this module.
   *
   * @param retrofit         the retrofit instance to be used to create the service.
   * @param callbackExecutor to tell on which thread it should run.
   * @param spaceId          the space to be used when not given.
   * @param environmentId    the environment to be used when not given.
   */
  public ModuleEditorInterfaces(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId) {
    super(retrofit, callbackExecutor, spaceId, environmentId);
    this.async = new Async();
  }

  @Override protected ServiceEditorInterfaces createService(Retrofit retrofit) {
    return retrofit.create(ServiceEditorInterfaces.class);
  }

  /**
   * @param contentTypeId the contentTypeId this editor interface is valid on.
   * @return the editor interface for a specific content type on a specific space.
   */
  public CMAEditorInterface fetchOne(String contentTypeId) {
    return fetchOne(spaceId, environmentId, contentTypeId);
  }

  /**
   * @param spaceId       the id of the space this environment is part of.
   * @param environmentId the id of the environment this editor interface is valid on.
   * @param contentTypeId the contentTypeId this editor interface is valid on.
   * @return the editor interface for a specific content type on a specific space.
   */
  public CMAEditorInterface fetchOne(String spaceId, String environmentId, String contentTypeId) {
    return service.fetchOne(spaceId, environmentId, contentTypeId).blockingFirst();
  }

  /**
   * Update an editor interface.
   *
   * @param editor the editor interface to be updated on Contentful.
   * @return the updated editor interface.
   * @throws IllegalArgumentException if editors spaceId is null.
   * @throws IllegalArgumentException if editors contentType is null.
   * @throws IllegalArgumentException if editors contentTypeId is null.
   * @throws IllegalArgumentException if editors version is not set.
   */
  public CMAEditorInterface update(CMAEditorInterface editor) {
    final String spaceId = getSpaceIdOrThrow(editor, "editor");
    if (editor.getSystem().getContentType() == null) {
      throw new IllegalArgumentException("ContentType of editor interface may not be null!");
    }
    if (editor.getSystem().getContentType().getId() == null) {
      throw new IllegalArgumentException("Id of ContentType of editor interface may not be null!");
    }
    final String contentTypeId = editor.getSystem().getContentType().getId();
    final String environmentId = editor.getEnvironmentId();
    final Integer version = getVersionOrThrow(editor, "update");

    CMASystem old = editor.getSystem();
    editor.setSystem(null);

    try {
      return service.update(spaceId, environmentId, contentTypeId, editor, version).blockingFirst();
    } finally {
      editor.setSystem(old);
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
     * Fetch editor interface to given content type in a given space.
     *
     * @param contentTypeId the id of the content type controlled by this editor interface.
     * @param callback      the callback to be informed about success or failure.
     * @return the callback.
     */
    public CMACallback<CMAEditorInterface> fetchOne(
        final String contentTypeId,
        CMACallback<CMAEditorInterface> callback) {
      return defer(new DefFunc<CMAEditorInterface>() {
        @Override CMAEditorInterface method() {
          return ModuleEditorInterfaces.this.fetchOne(contentTypeId);
        }
      }, callback);
    }

    /**
     * Fetch editor interface to given content type in a given space.
     *
     * @param spaceId       the space this environment is defined on.
     * @param environmentId the environment this editor interface is defined on.
     * @param contentTypeId the id of the content type controlled by this editor interface.
     * @param callback      the callback to be informed about success or failure.
     * @return the callback.
     */
    public CMACallback<CMAEditorInterface> fetchOne(
        final String spaceId,
        final String environmentId,
        final String contentTypeId,
        CMACallback<CMAEditorInterface> callback) {
      return defer(new DefFunc<CMAEditorInterface>() {
        @Override CMAEditorInterface method() {
          return ModuleEditorInterfaces.this.fetchOne(spaceId, environmentId, contentTypeId);
        }
      }, callback);
    }

    /**
     * Update the given editor interface.
     *
     * @param editor   the editor interface to be updated.
     * @param callback the callback to be informed about success or failure.
     * @return the callback.
     * @throws IllegalArgumentException if editors spaceId is null.
     * @throws IllegalArgumentException if editors contentTypeId is null.
     * @throws IllegalArgumentException if editors version is not set.
     */
    public CMACallback<CMAEditorInterface> update(
        final CMAEditorInterface editor,
        CMACallback<CMAEditorInterface> callback) {
      return defer(new DefFunc<CMAEditorInterface>() {
        @Override CMAEditorInterface method() {
          return ModuleEditorInterfaces.this.update(editor);
        }
      }, callback);
    }
  }
}
