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
import java.util.HashMap;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Assets Module.
 */
class ModuleAssets extends AbsModule<ServiceAssets> {
  final Async async;

  ModuleAssets(ServiceAssets service) {
    super(service);
    this.async = new Async();
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
    return service.assetsArchive(spaceId, assetId);
  }

  /**
   * Create a new Asset.
   * In case the given {@code asset} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param spaceId Space ID
   * @param asset Asset
   * @return {@link CMAAsset} result instance
   */
  @SuppressWarnings("unchecked") public CMAAsset create(String spaceId, CMAAsset asset) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(asset, "asset");

    String assetId = asset.getResourceId();
    HashMap sys = asset.sys;
    asset.sys = null;

    try {
      CMAAsset result;
      if (assetId == null) {
        result = service.assetsCreate(spaceId, asset);
      } else {
        result = service.assetsCreate(spaceId, assetId, asset);
      }
      asset.sys = sys;
      return result;
    } catch (RetrofitError e) {
      asset.sys = sys;
      throw (e);
    }
  }

  /**
   * Delete an Asset.
   *
   * @param spaceId Space ID
   * @param assetId Asset ID
   * @return Retrofit {@link Response} instance
   */
  public Response delete(String spaceId, String assetId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(assetId, "assetId");
    return service.assetsDelete(spaceId, assetId);
  }

  /**
   * Fetch all Assets from a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAAsset> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.assetsFetchAll(spaceId);
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
    assertNotNull(assetId, "entryId");
    return service.assetsFetchOne(spaceId, assetId);
  }

  /**
   * Process an Asset.
   *
   * @param asset Asset
   * @return Retrofit {@link Response} instance
   */
  public Response process(CMAAsset asset, String locale) {
    assertNotNull(asset, "asset");
    String assetId = getResourceIdOrThrow(asset, "asset");
    String spaceId = getSpaceIdOrThrow(asset, "asset");
    return service.assetsProcess(spaceId, assetId, locale);
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
    return service.assetsPublish(asset.getVersion(), spaceId, assetId);
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
    return service.assetsUnArchive(spaceId, assetId);
  }

  /**
   * Un-Publish an Asset.
   *
   * @param asset Asset
   * @return {@link CMAEntry} result instance
   */
  public CMAAsset unPublish(CMAAsset asset) {
    assertNotNull(asset, "asset");
    String assetId = getResourceIdOrThrow(asset, "asset");
    String spaceId = getSpaceIdOrThrow(asset, "asset");
    return service.assetsUnPublish(spaceId, assetId);
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

    CMAAsset update = new CMAAsset();
    update.fields = asset.fields;
    return service.assetsUpdate(asset.getVersion(), spaceId, assetId, update);
  }

  public Async async() {
    return async;
  }

  final class Async {
    public CMACallback<CMAAsset> archive(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.archive(asset);
        }
      }, callback);
    }

    public CMACallback<CMAAsset> create(final String spaceId, final CMAAsset asset,
        CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.create(spaceId, asset);
        }
      }, callback);
    }

    public CMACallback<Response> delete(final String spaceId, final String assetId,
        CMACallback<Response> callback) {
      return defer(new DefFunc<Response>() {
        @Override Response method() {
          return ModuleAssets.this.delete(spaceId, assetId);
        }
      }, callback);
    }

    public CMACallback<CMAArray<CMAAsset>> fetchAll(final String spaceId,
        CMACallback<CMAArray<CMAAsset>> callback) {
      return defer(new DefFunc<CMAArray<CMAAsset>>() {
        @Override CMAArray<CMAAsset> method() {
          return ModuleAssets.this.fetchAll(spaceId);
        }
      }, callback);
    }

    public CMACallback<CMAAsset> fetchOne(final String spaceId, final String assetId,
        CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.fetchOne(spaceId, assetId);
        }
      }, callback);
    }

    public CMACallback<Response> process(final CMAAsset asset, final String locale,
        CMACallback<Response> callback) {
      return defer(new DefFunc<Response>() {
        @Override Response method() {
          return ModuleAssets.this.process(asset, locale);
        }
      }, callback);
    }

    public CMACallback<CMAAsset> publish(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.publish(asset);
        }
      }, callback);
    }

    public CMACallback<CMAAsset> unArchive(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.unArchive(asset);
        }
      }, callback);
    }

    public CMACallback<CMAAsset> unPublish(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.unPublish(asset);
        }
      }, callback);
    }

    public CMACallback<CMAAsset> update(final CMAAsset asset, CMACallback<CMAAsset> callback) {
      return defer(new DefFunc<CMAAsset>() {
        @Override CMAAsset method() {
          return ModuleAssets.this.update(asset);
        }
      }, callback);
    }
  }
}
