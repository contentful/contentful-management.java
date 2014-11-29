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
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAContentType;
import java.util.HashMap;
import java.util.concurrent.Executor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Content Types Module.
 */
public final class ModuleContentTypes extends AbsModule<ServiceContentTypes> {
  final Async async;

  public ModuleContentTypes(RestAdapter restAdapter, Executor callbackExecutor) {
    super(restAdapter, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceContentTypes createService(RestAdapter restAdapter) {
    return restAdapter.create(ServiceContentTypes.class);
  }

  /**
   * Create a new Content Type.
   * In case the given {@code contentType} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param spaceId Space ID
   * @param contentType Content Type
   * @return {@link CMAContentType} result instance
   */
  @SuppressWarnings("unchecked")
  public CMAContentType create(String spaceId,
      CMAContentType contentType) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(contentType, "contentType");

    String contentTypeId = contentType.getResourceId();
    HashMap sys = contentType.getSys();
    contentType.setSys(null);

    try {
      CMAContentType result;
      if (contentTypeId == null) {
        result = service.create(spaceId, contentType);
      } else {
        result = service.create(spaceId, contentTypeId, contentType);
      }
      contentType.setSys(sys);
      return result;
    } catch (RetrofitError e) {
      contentType.setSys(sys);
      throw (e);
    }
  }

  /**
   * Delete a Content Type.
   *
   * @param spaceId Space ID
   * @param contentTypeId Content Type ID
   * @return Retrofit {@link Response} instance
   */
  public Response delete(String spaceId, String contentTypeId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(contentTypeId, "contentTypeId");
    return service.delete(spaceId, contentTypeId);
  }

  /**
   * Fetch all Content Types from a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAContentType> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId);
  }

  /**
   * Fetch a Content Type with the given {@code contentTypeId} from a Space.
   *
   * @param spaceId Space ID
   * @param contentTypeId Content Type ID
   * @return {@link CMAContentType} result instance
   */
  public CMAContentType fetchOne(String spaceId, String contentTypeId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(contentTypeId, "contentTypeId");
    return service.fetchOne(spaceId, contentTypeId);
  }

  /**
   * Publish a Content Type.
   *
   * @param contentType Content Type
   * @return {@link CMAContentType} result instance
   */
  public CMAContentType publish(CMAContentType contentType) {
    assertNotNull(contentType, "contentType");
    String contentTypeId = getResourceIdOrThrow(contentType, "contentType");
    String spaceId = getSpaceIdOrThrow(contentType, "contentType");
    return service.publish(contentType.getVersion(), spaceId, contentTypeId);
  }

  /**
   * Un-Publish a Content Type.
   *
   * @param contentType Content Type
   * @return {@link CMAContentType} result instance
   */
  public CMAContentType unPublish(CMAContentType contentType) {
    assertNotNull(contentType, "contentType");
    String contentTypeId = getResourceIdOrThrow(contentType, "contentType");
    String spaceId = getSpaceIdOrThrow(contentType, "contentType");
    return service.unPublish(spaceId, contentTypeId);
  }

  /**
   * Update a Content Type.
   *
   * @param contentType Content Type
   * @return {@link CMAContentType} result instance
   */
  public CMAContentType update(CMAContentType contentType) {
    assertNotNull(contentType, "contentType");
    assertNotNull(contentType.getName(), "contentType.name");
    String contentTypeId = getResourceIdOrThrow(contentType, "contentType");
    String spaceId = getSpaceIdOrThrow(contentType, "contentType");
    return service.update(contentType.getVersion(), spaceId, contentTypeId, contentType);
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
  public final class Async {
    /**
     * Create a new Content Type.
     * In case the given {@code contentType} has an ID associated with it, that ID will be used,
     * otherwise the server will auto-generate an ID that will be contained in the response upon
     * success.
     *
     * @param spaceId Space ID
     * @param contentType Content Type
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAContentType> create(final String spaceId,
        final CMAContentType contentType, CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ModuleContentTypes.this.create(spaceId, contentType);
        }
      }, callback);
    }

    /**
     * Delete a Content Type.
     *
     * @param spaceId Space ID
     * @param contentTypeId Content Type ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<Response> delete(final String spaceId, final String contentTypeId,
        CMACallback<Response> callback) {
      return defer(new DefFunc<Response>() {
        @Override Response method() {
          return ModuleContentTypes.this.delete(spaceId, contentTypeId);
        }
      }, callback);
    }

    /**
     * Fetch all Content Types from a Space.
     *
     * @param spaceId Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAArray<CMAContentType>> fetchAll(final String spaceId,
        CMACallback<CMAArray<CMAContentType>> callback) {
      return defer(new DefFunc<CMAArray<CMAContentType>>() {
        @Override CMAArray<CMAContentType> method() {
          return ModuleContentTypes.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch a Content Type with the given {@code contentTypeId} from a Space.
     *
     * @param spaceId Space ID
     * @param contentTypeId Content Type ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAContentType> update(final CMAContentType contentType,
        CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ModuleContentTypes.this.update(contentType);
        }
      }, callback);
    }
  }
}
