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
import com.contentful.java.cma.model.CMAAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Assets Module.
 */
public final class ModuleAssets extends AbsModule<ServiceAssets> {
  final Async async;

  public ModuleAssets(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceAssets createService(Retrofit retrofit) {
    return retrofit.create(ServiceAssets.class);
  }

  /**
   * Archive an Asset.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   */
  public CMAAsset archive(CMAAsset asset) {
    assertNotNull(asset, "asset");
    String assetId = getResourceIdOrThrow(asset, "asset");
    String spaceId = getSpaceIdOrThrow(asset, "asset");
    return service.archive(spaceId, assetId, new Byte[0]).toBlocking().first();
  }

  /**
   * Create a new Asset.
   * In case the given {@code asset} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param spaceId Space ID
   * @param asset   Asset
   * @return {@link CMAAsset} result instance
   */
  @SuppressWarnings("unchecked")
  public CMAAsset create(String spaceId, CMAAsset asset) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(asset, "asset");

    String assetId = asset.getResourceId();
    HashMap sys = asset.getSys();
    asset.setSys(null);

    try {
      CMAAsset result;
      if (assetId == null) {
        result = service.create(spaceId, asset).toBlocking().first();
      } else {
        result = service.create(spaceId, assetId, asset).toBlocking().first();
      }
      asset.setSys(sys);
      return result;
    } catch (RuntimeException e) {
      asset.setSys(sys);
      throw (e);
    }
  }

  /**
   * Delete an Asset.
   *
   * @param spaceId Space ID
   * @param assetId Asset ID
   * @return A string representing the result of the delete operation
   */
  public String delete(String spaceId, String assetId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(assetId, "assetId");
    return service.delete(spaceId, assetId).toBlocking().first();
  }

  /**
   * Fetch all Assets from a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAAsset> fetchAll(String spaceId) {
    return fetchAll(spaceId, null);
  }

  /**
   * Fetch all Assets from a Space with a query.
   *
   * @param spaceId Space ID
   * @param query   Query
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAAsset> fetchAll(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId, query).toBlocking().first();
  }

  /**
   * Fetch an Asset with the given {@code assetId} from a Space.
   *
   * @param spaceId Space ID
   * @param assetId Asset ID
   * @return {@link CMAAsset} result instance
   */
  public CMAAsset fetchOne(String spaceId, String assetId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(assetId, "assetId");
    return service.fetchOne(spaceId, assetId).toBlocking().first();
  }

  /**
   * Process an Asset.
   *
   * @param asset  Asset
   * @param locale Locale
   * @return Retrofit {@link Response} instance
   */
  public String process(CMAAsset asset, String locale) {
    assertNotNull(asset, "asset");
    String assetId = getResourceIdOrThrow(asset, "asset");
    String spaceId = getSpaceIdOrThrow(asset, "asset");
    return service.process(spaceId, assetId, locale, new Byte[0]).toBlocking().first();
  }

  /**
   * Publish an Asset.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   */
  public CMAAsset publish(CMAAsset asset) {
    assertNotNull(asset, "asset");
    String assetId = getResourceIdOrThrow(asset, "asset");
    String spaceId = getSpaceIdOrThrow(asset, "asset");
    return service.publish(asset.getVersion(), spaceId, assetId, new Byte[0]).toBlocking().first();
  }

  /**
   * Un-Archive an Asset.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   */
  public CMAAsset unArchive(CMAAsset asset) {
    assertNotNull(asset, "asset");
    String assetId = getResourceIdOrThrow(asset, "asset");
    String spaceId = getSpaceIdOrThrow(asset, "asset");
    return service.unArchive(spaceId, assetId).toBlocking().first();
  }

  /**
   * Un-Publish an Asset.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   */
  public CMAAsset unPublish(CMAAsset asset) {
    assertNotNull(asset, "asset");
    String assetId = getResourceIdOrThrow(asset, "asset");
    String spaceId = getSpaceIdOrThrow(asset, "asset");
    return service.unPublish(spaceId, assetId).toBlocking().first();
  }

  /**
   * Update an Asset.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   */
  public CMAAsset update(CMAAsset asset) {
    assertNotNull(asset, "asset");
    String assetId = getResourceIdOrThrow(asset, "asset");
    String spaceId = getSpaceIdOrThrow(asset, "asset");
    Integer version = getVersionOrThrow(asset, "update");

    CMAAsset update = new CMAAsset();
    update.setFields(asset.getFields());
    return service.update(version, spaceId, assetId, update).toBlocking().first();
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
     * Archive an Asset.
     *
     * @param asset    Asset
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAAsset> archive(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.archive(asset);
        }
      }, callback);
    }

    /**
     * Create a new Asset.
     * In case the given {@code asset} has an ID associated with it, that ID will be used,
     * otherwise the server will auto-generate an ID that will be contained in the response upon
     * success.
     *
     * @param spaceId  Space ID
     * @param asset    Asset
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAAsset> create(final String spaceId, final CMAAsset asset,
                                        CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.create(spaceId, asset);
        }
      }, callback);
    }

    /**
     * Delete an Asset.
     *
     * @param spaceId  Space ID
     * @param assetId  Asset ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<String> delete(final String spaceId, final String assetId,
                                      CMACallback<String> callback) {
      return defer(new DefFunc<String>() {
        @Override String method() {
          return ModuleAssets.this.delete(spaceId, assetId);
        }
      }, callback);
    }

    /**
     * Fetch all Assets from a Space.
     *
     * @param spaceId  Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAArray<CMAAsset>> fetchAll(String spaceId,
                                                    CMACallback<CMAArray<CMAAsset>> callback) {
      return fetchAll(spaceId, new HashMap<String, String>(), callback);
    }

    /**
     * Fetch all Assets from a Space with a query.
     *
     * @param spaceId  Space ID
     * @param query    Query
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAArray<CMAAsset>> fetchAll(final String spaceId,
                                                    final Map<String, String> query,
                                                    CMACallback<CMAArray<CMAAsset>> callback) {
      return defer(new DefFunc<CMAArray<CMAAsset>>() {
        @Override CMAArray<CMAAsset> method() {
          return ModuleAssets.this.fetchAll(spaceId, query);
        }
      }, callback);
    }

    /**
     * Fetch an Asset with the given {@code assetId} from a Space.
     *
     * @param spaceId  Space ID
     * @param assetId  Asset ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAAsset> fetchOne(final String spaceId, final String assetId,
                                          CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.fetchOne(spaceId, assetId);
        }
      }, callback);
    }

    /**
     * Process an Asset.
     *
     * @param asset    Asset
     * @param locale   Locale
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<String> process(final CMAAsset asset, final String locale,
                                       CMACallback<String> callback) {
      return defer(new DefFunc<String>() {
        @Override String method() {
          return ModuleAssets.this.process(asset, locale);
        }
      }, callback);
    }

    /**
     * Publish an Asset.
     *
     * @param asset    Asset
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAAsset> publish(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.publish(asset);
        }
      }, callback);
    }

    /**
     * Un-Archive an Asset.
     *
     * @param asset    Asset
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAAsset> unArchive(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.unArchive(asset);
        }
      }, callback);
    }

    /**
     * Un-Publish an Asset.
     *
     * @param asset    Asset
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAAsset> unPublish(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.unPublish(asset);
        }
      }, callback);
    }

    /**
     * Update an Asset.
     *
     * @param asset    Asset
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAAsset> update(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.update(asset);
        }
      }, callback);
    }
  }
}
