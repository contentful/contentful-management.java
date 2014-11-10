package com.contentful.java.cma;

import java.util.HashMap;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Content Types Module.
 */
class ContentTypesModule extends AbsModule {
  ContentTypesModule(CMAClient client) {
    super(client);
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
  public CMAContentType create(String spaceId, CMAContentType contentType) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(contentType, "contentType");

    String contentTypeId = contentType.getResourceId();
    HashMap sys = contentType.sys;
    contentType.sys = null;

    try {
      CMAContentType result;
      if (contentTypeId == null) {
        result = client.service.contentTypesCreate(spaceId, contentType);
      } else {
        result = client.service.contentTypesCreate(spaceId, contentTypeId, contentType);
      }
      contentType.sys = sys;
      return result;
    } catch (RetrofitError e) {
      contentType.sys = sys;
      throw(e);
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
    return client.service.contentTypesDelete(spaceId, contentTypeId);
  }

  /**
   * Fetch all Content Types from a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAContentType> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return client.service.contentTypesFetchOne(spaceId);
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
    return client.service.contentTypesFetchOne(spaceId, contentTypeId);
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
    return client.service.contentTypesPublish(contentType.getVersion(), spaceId, contentTypeId);
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
    return client.service.contentTypesUnPublish(spaceId, contentTypeId);
  }

  /**
   * Update a Content Type.
   *
   * @param contentType Content Type
   * @return {@link CMAContentType} result instance
   */
  public CMAContentType update(CMAContentType contentType) {
    assertNotNull(contentType, "contentType");
    assertNotNull(contentType.name, "contentType.name");
    String contentTypeId = getResourceIdOrThrow(contentType, "contentType");
    String spaceId = getSpaceIdOrThrow(contentType, "contentType");
    return client.service.contentTypesUpdate(contentType.getVersion(), spaceId, contentTypeId,
        contentType);
  }
}
