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
import com.contentful.java.cma.model.CMAContentType;
import com.contentful.java.cma.model.CMASnapshot;
import com.contentful.java.cma.model.CMASystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Content Types Module.
 */
public final class ModuleContentTypes extends AbsModule<ServiceContentTypes> {
  final Async async;

  public ModuleContentTypes(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceContentTypes createService(Retrofit retrofit) {
    return retrofit.create(ServiceContentTypes.class);
  }

  /**
   * Create a new Content Type.
   * In case the given {@code contentType} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param spaceId     Space ID
   * @param contentType Content Type
   * @return {@link CMAContentType} result instance
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if contentType is null.
   */
  @SuppressWarnings("unchecked")
  public CMAContentType create(String spaceId,
                               CMAContentType contentType) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(contentType, "contentType");

    final String contentTypeId = contentType.getId();

    final CMASystem sys = contentType.getSystem();
    contentType.setSystem(null);

    try {
      if (contentTypeId == null) {
        return service.create(spaceId, contentType).blockingFirst();
      } else {
        return service.create(spaceId, contentTypeId, contentType).blockingFirst();
      }
    } finally {
      contentType.setSystem(sys);
    }
  }

  /**
   * Delete a Content Type.
   *
   * @param spaceId       Space ID
   * @param contentTypeId Content Type ID
   * @return Integer representing the result of the operation
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if contentTypeId is null.
   */
  public Integer delete(String spaceId, String contentTypeId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(contentTypeId, "contentTypeId");
    return service.delete(spaceId, contentTypeId).blockingFirst().code();
  }

  /**
   * Fetch all Content Types from a Space, using default query parameter.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAContentType> fetchAll(String spaceId) {
    return fetchAll(spaceId, new HashMap<String, String>());
  }

  /**
   * Fetch all Content Types from a Space with query parameters.
   *
   * @param spaceId Space ID
   * @param query   Query to narrow down the content_types to be searched for
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAContentType> fetchAll(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
    return service.fetchAll(spaceId, query).blockingFirst();
  }

  /**
   * Fetch a Content Type with the given {@code contentTypeId} from a Space.
   *
   * @param spaceId       Space ID
   * @param contentTypeId Content Type ID
   * @return {@link CMAContentType} result instance
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if contentTypeId is null.
   */
  public CMAContentType fetchOne(String spaceId, String contentTypeId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(contentTypeId, "contentTypeId");
    return service.fetchOne(spaceId, contentTypeId).blockingFirst();
  }

  /**
   * Publish a Content Type.
   *
   * @param contentType Content Type
   * @return {@link CMAContentType} result instance
   * @throws IllegalArgumentException if contentType is null.
   * @throws IllegalArgumentException if contentType's id is null.
   * @throws IllegalArgumentException if contentType's space id is null.
   */
  public CMAContentType publish(CMAContentType contentType) {
    assertNotNull(contentType, "contentType");

    final String contentTypeId = getResourceIdOrThrow(contentType, "contentType");
    final String spaceId = getSpaceIdOrThrow(contentType, "contentType");

    return service.publish(
        contentType.getVersion(),
        spaceId,
        contentTypeId
    ).blockingFirst();
  }

  /**
   * Un-Publish a Content Type.
   *
   * @param contentType Content Type
   * @return {@link CMAContentType} result instance
   * @throws IllegalArgumentException if contentType is null.
   * @throws IllegalArgumentException if contentType's id is null.
   * @throws IllegalArgumentException if contentType's space id is null.
   */
  public CMAContentType unPublish(CMAContentType contentType) {
    assertNotNull(contentType, "contentType");

    final String contentTypeId = getResourceIdOrThrow(contentType, "contentType");
    final String spaceId = getSpaceIdOrThrow(contentType, "contentType");

    return service.unPublish(spaceId, contentTypeId).blockingFirst();
  }

  /**
   * Update a Content Type.
   *
   * @param contentType Content Type
   * @return {@link CMAContentType} result instance
   * @throws IllegalArgumentException if contentType is null.
   * @throws IllegalArgumentException if contentType's name is null.
   * @throws IllegalArgumentException if contentType's id is null.
   * @throws IllegalArgumentException if contentType's space id is null.
   * @throws IllegalArgumentException if contentType's version is null.
   */
  public CMAContentType update(CMAContentType contentType) {
    assertNotNull(contentType, "contentType");
    assertNotNull(contentType.getName(), "contentType.name");

    final String contentTypeId = getResourceIdOrThrow(contentType, "contentType");
    final String spaceId = getSpaceIdOrThrow(contentType, "contentType");
    final Integer version = getVersionOrThrow(contentType, "update");

    final CMASystem system = contentType.getSystem();
    contentType.setSystem(null);

    try {
      return service.update(
          version,
          spaceId,
          contentTypeId,
          contentType
      ).blockingFirst();
    } finally {
      contentType.setSystem(system);
    }
  }

  /**
   * Fetch all snapshots of this content type.
   *
   * @param contentType the contentType whose snapshots to be returned.
   * @return an array of snapshots.
   * @throws IllegalArgumentException if contentType is null.
   * @throws IllegalArgumentException if contentType's id is null.
   * @throws IllegalArgumentException if contentType's space id is null.
   */
  public CMAArray<CMASnapshot> fetchAllSnapshots(CMAContentType contentType) {
    assertNotNull(contentType, "contentType");

    final String contentTypeId = getResourceIdOrThrow(contentType, "contentType");
    final String spaceId = getSpaceIdOrThrow(contentType, "contentType");

    return service.fetchAllSnapshots(spaceId, contentTypeId).blockingFirst();
  }

  /**
   * Fetch a specific snapshot of this content type.
   *
   * @param contentType the contentType whose snapshot to be returned.
   * @param snapshotId  the snapshot to be returned.
   * @return an array of snapshots.
   * @throws IllegalArgumentException if contentType is null.
   * @throws IllegalArgumentException if contentType's id is null.
   * @throws IllegalArgumentException if contentType's space id is null.
   * @throws IllegalArgumentException if snapshotId is null.
   */
  public CMASnapshot fetchOneSnapshot(CMAContentType contentType,
                                      String snapshotId) {
    assertNotNull(contentType, "contentType");
    assertNotNull(snapshotId, "snapshotId");

    final String contentTypeId = getResourceIdOrThrow(contentType, "contentType");
    final String spaceId = getSpaceIdOrThrow(contentType, "contentType");

    return service.fetchOneSnapshot(spaceId, contentTypeId, snapshotId).blockingFirst();
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
     * Create a new Content Type.
     * In case the given {@code contentType} has an ID associated with it, that ID will be used,
     * otherwise the server will auto-generate an ID that will be contained in the response upon
     * success.
     *
     * @param spaceId     Space ID
     * @param contentType Content Type
     * @param callback    Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if contentType is null.
     * @throws IllegalArgumentException if contentTypeId is null.
     */
    public CMACallback<CMAContentType> create(final String spaceId,
                                              final CMAContentType contentType,
                                              CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ModuleContentTypes.this.create(spaceId, contentType);
        }
      }, callback);
    }

    /**
     * Delete a Content Type.
     *
     * @param spaceId       Space ID
     * @param contentTypeId Content Type ID
     * @param callback      Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if contentTypeId is null.
     */
    public CMACallback<Integer> delete(final String spaceId,
                                       final String contentTypeId,
                                       CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleContentTypes.this.delete(spaceId, contentTypeId);
        }
      }, callback);
    }

    /**
     * Fetch all Content Types from a Space.
     *
     * @param spaceId  Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAContentType>> fetchAll(final String spaceId,
                                                          CMACallback<
                                                              CMAArray<CMAContentType>> callback) {
      return fetchAll(spaceId, new HashMap<String, String>(), callback);
    }

    /**
     * Fetch all Content Types from a Space with a query.
     *
     * @param spaceId  Space ID
     * @param query    Query
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAContentType>> fetchAll(final String spaceId,
                                                          final Map<String, String> query,
                                                          CMACallback<
                                                              CMAArray<CMAContentType>> callback) {
      return defer(new DefFunc<CMAArray<CMAContentType>>() {
        @Override CMAArray<CMAContentType> method() {
          DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
          return ModuleContentTypes.this.fetchAll(spaceId, query);
        }
      }, callback);
    }

    /**
     * Fetch a Content Type with the given {@code contentTypeId} from a Space.
     *
     * @param spaceId       Space ID
     * @param contentTypeId Content Type ID
     * @param callback      Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if contentTypeId is null.
     */
    public CMACallback<CMAContentType> fetchOne(final String spaceId, final String contentTypeId,
                                                CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ModuleContentTypes.this.fetchOne(spaceId, contentTypeId);
        }
      }, callback);
    }

    /**
     * Publish a Content Type.
     *
     * @param contentType Content Type
     * @param callback    Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if contentType is null.
     * @throws IllegalArgumentException if contentType's id is null.
     * @throws IllegalArgumentException if contentType's space id is null.
     */
    public CMACallback<CMAContentType> publish(final CMAContentType contentType,
                                               CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ModuleContentTypes.this.publish(contentType);
        }
      }, callback);
    }

    /**
     * Un-Publish a Content Type.
     *
     * @param contentType Content Type
     * @param callback    Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if contentType is null.
     * @throws IllegalArgumentException if contentType's id is null.
     * @throws IllegalArgumentException if contentType's space id is null.
     */
    public CMACallback<CMAContentType> unPublish(final CMAContentType contentType,
                                                 CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ModuleContentTypes.this.unPublish(contentType);
        }
      }, callback);
    }

    /**
     * Update a Content Type.
     *
     * @param contentType Content Type
     * @param callback    Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if contentType is null.
     * @throws IllegalArgumentException if contentType's name is null.
     * @throws IllegalArgumentException if contentType's id is null.
     * @throws IllegalArgumentException if contentType's space id is null.
     * @throws IllegalArgumentException if contentType's version is null.
     */
    public CMACallback<CMAContentType> update(final CMAContentType contentType,
                                              CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ModuleContentTypes.this.update(contentType);
        }
      }, callback);
    }

    /**
     * Fetch all snapshots of this content type.
     *
     * @param contentType the contentType whose snapshots to be returned.
     * @return the callback.
     * @throws IllegalArgumentException if contentType is null.
     * @throws IllegalArgumentException if contentType's id is null.
     * @throws IllegalArgumentException if contentType's space id is null.
     */
    public CMACallback<CMAArray<CMASnapshot>> fetchAllSnapshots(
        final CMAContentType contentType,
        CMACallback<CMAArray<CMASnapshot>> callback) {
      return defer(new DefFunc<CMAArray<CMASnapshot>>() {
        @Override CMAArray<CMASnapshot> method() {
          return ModuleContentTypes.this.fetchAllSnapshots(contentType);
        }
      }, callback);
    }

    /**
     * Fetch a specific snapshot of this content type.
     *
     * @param contentType the contentType whose snapshot to be returned.
     * @param snapshotId  the snapshot to be returned.
     * @return a callback to inform about transaction results.
     * @throws IllegalArgumentException if contentType is null.
     * @throws IllegalArgumentException if contentType's id is null.
     * @throws IllegalArgumentException if contentType's space id is null.
     * @throws IllegalArgumentException if snapshotId is null.
     */
    public CMACallback<CMASnapshot> fetchOneSnapshot(
        final CMAContentType contentType,
        final String snapshotId,
        CMACallback<CMASnapshot> callback) {
      return defer(new DefFunc<CMASnapshot>() {
        @Override CMASnapshot method() {
          return ModuleContentTypes.this.fetchOneSnapshot(contentType, snapshotId);
        }
      }, callback);
    }
  }
}
