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
import com.contentful.java.cma.model.CMAAsset;
import com.contentful.java.cma.model.CMASystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Assets Module.
 */
public final class ModuleAssets extends AbsModule<ServiceAssets> {
  final Async async;

  public ModuleAssets(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
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
   * @throws IllegalArgumentException if asset is null.
   * @throws IllegalArgumentException if asset id is null.
   * @throws IllegalArgumentException if asset space id is null.
   */
  public CMAAsset archive(CMAAsset asset) {
    assertNotNull(asset, "asset");
    final String assetId = getResourceIdOrThrow(asset, "asset");
    final String spaceId = getSpaceIdOrThrow(asset, "asset");
    final String environmentId = asset.getEnvironmentId();

    return service.archive(spaceId, environmentId, assetId).blockingFirst();
  }

  /**
   * Create a new Asset.
   * <p>
   * In case the given {@code asset} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param asset         Asset
   * @return {@link CMAAsset} result instance
   * @throws IllegalArgumentException if asset is null.
   * @throws IllegalArgumentException if asset space id is null.
   * @throws IllegalArgumentException if asset environment id is null.
   */
  public CMAAsset create(String spaceId, String environmentId, CMAAsset asset) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(asset, "asset");

    final String assetId = asset.getId();

    final CMASystem sys = asset.getSystem();
    asset.setSystem(null);

    try {
      if (assetId == null) {
        return service.create(spaceId, environmentId, asset).blockingFirst();
      } else {
        return service.create(spaceId, environmentId, assetId, asset).blockingFirst();
      }
    } finally {
      asset.setSystem(sys);
    }
  }

  /**
   * Create a new Asset in the configured space and environment.
   * <p>
   * In case the given {@code asset} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   * @throws IllegalArgumentException if asset is null.
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMAAsset create(CMAAsset asset) {
    return create(spaceId, environmentId, asset);
  }

  /**
   * Delete an Asset.
   *
   * @param asset Asset
   * @return An integer representing the result of the delete operation
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if assetId is null.
   */
  public Integer delete(CMAAsset asset) {
    final String assetId = getResourceIdOrThrow(asset, "asset");
    final String spaceId = getSpaceIdOrThrow(asset, "asset");
    final String environmentId = asset.getEnvironmentId();

    return service.delete(spaceId, environmentId, assetId).blockingFirst().code();
  }

  /**
   * Fetch all Assets using configured space id and environment id.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}
   *
   * @return all assets of this space environment.
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMAArray<CMAAsset> fetchAll() {
    return fetchAll(spaceId, environmentId, new HashMap<>());
  }

  /**
   * Fetch all Assets matching a query using configured space id and environment id.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}
   *
   * @param map the query to narrow down the results.
   * @return matching assets.
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMAArray<CMAAsset> fetchAll(Map<String, String> map) {
    return fetchAll(spaceId, environmentId, map);
  }

  /**
   * Fetch all Assets from the given space and environment.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if environment id is null.
   */
  public CMAArray<CMAAsset> fetchAll(String spaceId, String environmentId) {
    return fetchAll(spaceId, environmentId, new HashMap<>());
  }

  /**
   * Fetch all Assets matching the given query from the given space and environment.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param query         specifying details about which assets to fetch.
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAAsset> fetchAll(
      String spaceId,
      String environmentId,
      Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);

    return service.fetchAll(spaceId, environmentId, query).blockingFirst();
  }

  /**
   * Fetch an Asset with the given {@code assetId} from the configured space and environment.
   *
   * @param assetId Asset ID
   * @return {@link CMAAsset} result instance
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMAAsset fetchOne(String assetId) {
    return fetchOne(spaceId, environmentId, assetId);
  }

  /**
   * Fetch an Asset with the given {@code assetId} from the given space and environment.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param assetId       Asset ID
   * @return {@link CMAAsset} result instance
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if assetId is null.
   */
  public CMAAsset fetchOne(String spaceId, String environmentId, String assetId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(assetId, "assetId");

    return service.fetchOne(spaceId, environmentId, assetId).blockingFirst();
  }

  /**
   * Process an Asset.
   *
   * @param asset  Asset
   * @param locale Locale
   * @return integer representing the success (204 = no content) of processing
   * @throws IllegalArgumentException if asset is null.
   * @throws IllegalArgumentException if asset has no id.
   * @throws IllegalArgumentException if asset has no space.
   * @throws IllegalArgumentException if locale is null.
   */
  public Integer process(CMAAsset asset, String locale) {
    assertNotNull(asset, "asset");
    final String assetId = getResourceIdOrThrow(asset, "asset");
    final String spaceId = getSpaceIdOrThrow(asset, "asset");
    final String environmentId = asset.getEnvironmentId();

    return service.process(spaceId, environmentId, assetId, locale).blockingFirst().code();
  }

  /**
   * Publish an Asset.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   * @throws IllegalArgumentException if asset is null.
   * @throws IllegalArgumentException if asset has no id.
   * @throws IllegalArgumentException if asset has no space id.
   */
  public CMAAsset publish(CMAAsset asset) {
    assertNotNull(asset, "asset");
    final String assetId = getResourceIdOrThrow(asset, "asset");
    final String spaceId = getSpaceIdOrThrow(asset, "asset");
    final String environmentId = asset.getEnvironmentId();

    return service.publish(
        asset.getSystem().getVersion(),
        spaceId,
        environmentId,
        assetId
    ).blockingFirst();
  }

  /**
   * Un-Archive an Asset.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   * @throws IllegalArgumentException if asset is null.
   * @throws IllegalArgumentException if asset id is empty.
   * @throws IllegalArgumentException if asset's space id is empty.
   */
  public CMAAsset unArchive(CMAAsset asset) {
    assertNotNull(asset, "asset");

    final String assetId = getResourceIdOrThrow(asset, "asset");
    final String spaceId = getSpaceIdOrThrow(asset, "asset");
    final String environmentId = asset.getEnvironmentId();

    return service.unArchive(spaceId, environmentId, assetId).blockingFirst();
  }

  /**
   * Un-Publish an Asset.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   * @throws IllegalArgumentException if asset is null.
   * @throws IllegalArgumentException if asset's id is not set.
   * @throws IllegalArgumentException if asset's space id is not set.
   */
  public CMAAsset unPublish(CMAAsset asset) {
    assertNotNull(asset, "asset");

    final String assetId = getResourceIdOrThrow(asset, "asset");
    final String spaceId = getSpaceIdOrThrow(asset, "asset");
    final String environmentId = asset.getEnvironmentId();

    return service.unPublish(spaceId, environmentId, assetId).blockingFirst();
  }

  /**
   * Update an Asset.
   *
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   * @throws IllegalArgumentException if asset is null.
   * @throws IllegalArgumentException if asset's id is null.
   * @throws IllegalArgumentException if asset's space id is null.
   * @throws IllegalArgumentException if asset's version is null.
   */
  public CMAAsset update(CMAAsset asset) {
    assertNotNull(asset, "asset");
    final String assetId = getResourceIdOrThrow(asset, "asset");
    final String spaceId = getSpaceIdOrThrow(asset, "asset");
    final String environmentId = asset.getEnvironmentId();
    final Integer version = getVersionOrThrow(asset, "update");

    final CMASystem sys = asset.getSystem();
    asset.setSystem(null);

    try {
      return service.update(version, spaceId, environmentId, assetId, asset).blockingFirst();
    } finally {
      asset.setSystem(sys);
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
     * Archive an Asset.
     *
     * @param asset    Asset
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if asset is null.
     * @throws IllegalArgumentException if asset id is null.
     * @throws IllegalArgumentException if asset space id is null.
     */
    public CMACallback<CMAAsset> archive(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.archive(asset);
        }
      }, callback);
    }

    /**
     * Create a new Asset in the configured space and environment.
     * <p>
     * In case the given {@code asset} has an ID associated with it, that ID will be used,
     * otherwise the server will auto-generate an ID that will be contained in the response upon
     * success.
     *
     * @param asset    Asset
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if asset is null.
     * @throws IllegalArgumentException if asset id is null.
     * @throws IllegalArgumentException if asset space id is null.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMAAsset> create(final CMAAsset asset,
                                        CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.create(asset);
        }
      }, callback);
    }

    /**
     * Create a new Asset in the given space and environment.
     * <p>
     * In case the given {@code asset} has an ID associated with it, that ID will be used,
     * otherwise the server will auto-generate an ID that will be contained in the response upon
     * success.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param asset         Asset
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if asset is null.
     * @throws IllegalArgumentException if asset id is null.
     * @throws IllegalArgumentException if asset space id is null.
     * @throws IllegalArgumentException if asset environment id is null.
     */
    public CMACallback<CMAAsset> create(
        final String spaceId,
        final String environmentId,
        final CMAAsset asset,
        CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.create(spaceId, environmentId, asset);
        }
      }, callback);
    }

    /**
     * Delete an Asset.
     *
     * @param asset    Asset
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if assetId is null.
     */
    public CMACallback<Integer> delete(final CMAAsset asset,
                                       CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleAssets.this.delete(asset);
        }
      }, callback);
    }

    /**
     * Fetch all Assets from the configured space and environmentS.
     *
     * @param callback Callback
     * @return the given CMACallback instance.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMAArray<CMAAsset>> fetchAll(
        CMACallback<CMAArray<CMAAsset>> callback) {
      return defer(new DefFunc<CMAArray<CMAAsset>>() {
        @Override CMAArray<CMAAsset> method() {
          return ModuleAssets.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch all Assets.
     *
     * @param query    The query to narrow down the results.
     * @param callback Callback
     * @return the given CMACallback instance
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMAArray<CMAAsset>> fetchAll(
        final Map<String, String> query,
        CMACallback<CMAArray<CMAAsset>> callback) {
      return defer(new DefFunc<CMAArray<CMAAsset>>() {
        @Override CMAArray<CMAAsset> method() {
          return ModuleAssets.this.fetchAll(query);
        }
      }, callback);
    }

    /**
     * Fetch all Assets from a Space's Environment.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param callback      Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAArray<CMAAsset>> fetchAll(
        String spaceId,
        String environmentId,
        CMACallback<CMAArray<CMAAsset>> callback) {
      return defer(new DefFunc<CMAArray<CMAAsset>>() {
        @Override CMAArray<CMAAsset> method() {
          return ModuleAssets.this.fetchAll(spaceId, environmentId);
        }
      }, callback);
    }

    /**
     * Fetch all Assets from a Space's Environment with a query.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param query         Query
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAAsset>> fetchAll(
        final String spaceId,
        final String environmentId,
        final Map<String, String> query,
        CMACallback<CMAArray<CMAAsset>> callback) {
      return defer(new DefFunc<CMAArray<CMAAsset>>() {
        @Override CMAArray<CMAAsset> method() {
          return ModuleAssets.this.fetchAll(spaceId, environmentId, query);
        }
      }, callback);
    }

    /**
     * Fetch an Asset with the given {@code assetId} from the configured space.
     *
     * @param assetId  Asset ID
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if configured space id is null.
     * @throws IllegalArgumentException if configured environment id is null.
     * @throws IllegalArgumentException if asset id is null.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMAAsset> fetchOne(
        final String assetId,
        CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.fetchOne(assetId);
        }
      }, callback);
    }

    /**
     * Fetch an Asset with the given {@code assetId} from a the given space and environment.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param assetId       Asset ID
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if environmentId is null.
     * @throws IllegalArgumentException if assetId is null.
     */
    public CMACallback<CMAAsset> fetchOne(
        final String spaceId,
        final String environmentId,
        final String assetId,
        CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.fetchOne(spaceId, environmentId, assetId);
        }
      }, callback);
    }

    /**
     * Process an Asset.
     *
     * @param asset    Asset
     * @param locale   Locale
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if asset is null.
     * @throws IllegalArgumentException if asset has no id.
     * @throws IllegalArgumentException if asset has no space.
     * @throws IllegalArgumentException if locale is null.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<Integer> process(
        final CMAAsset asset,
        final String locale,
        CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleAssets.this.process(asset, locale);
        }
      }, callback);
    }

    /**
     * Publish an Asset.
     *
     * @param asset    Asset
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if asset is null.
     * @throws IllegalArgumentException if asset has no id.
     * @throws IllegalArgumentException if asset has no space id.
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
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if asset is null.
     * @throws IllegalArgumentException if asset id is empty.
     * @throws IllegalArgumentException if asset's space id is empty.
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
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if asset is null.
     * @throws IllegalArgumentException if asset's id is not set.
     * @throws IllegalArgumentException if asset's space id is not set.
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
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if asset is null.
     * @throws IllegalArgumentException if asset's id is null.
     * @throws IllegalArgumentException if asset's space id is null.
     * @throws IllegalArgumentException if asset's version is null.
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
