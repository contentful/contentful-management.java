package com.contentful.java.cma;

import java.util.HashMap;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Assets Module.
 */
class AssetsModule extends AbsModule {
  public AssetsModule(CMAClient client) {
    super(client);
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
    return client.service.assetsArchive(spaceId, assetId);
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
  @SuppressWarnings("unchecked")
  public CMAAsset create(String spaceId, CMAAsset asset) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(asset, "asset");

    String assetId = asset.getResourceId();
    HashMap sys = asset.sys;
    asset.sys = null;

    try {
      CMAAsset result;
      if (assetId == null) {
        result = client.service.assetsCreate(spaceId, asset);
      } else {
        result = client.service.assetsCreate(spaceId, assetId, asset);
      }
      asset.sys = sys;
      return result;
    } catch (RetrofitError e) {
      asset.sys = sys;
      throw(e);
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
    return client.service.assetsDelete(spaceId, assetId);
  }

  /**
   * Fetch all Assets from a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAAsset> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return client.service.assetsFetchAll(spaceId);
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
    return client.service.assetsFetchOne(spaceId, assetId);
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
    return client.service.assetsProcess(spaceId, assetId, locale);
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
    return client.service.assetsPublish(asset.getVersion(), spaceId, assetId);
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
    return client.service.assetsUnArchive(spaceId, assetId);
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
    return client.service.assetsUnPublish(spaceId, assetId);
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
    return client.service.assetsUpdate(asset.getVersion(), spaceId, assetId, update);
  }
}
