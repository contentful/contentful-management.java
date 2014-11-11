package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import java.util.HashMap;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Content Types Module.
 */
class ContentTypesModule extends AbsModule<ServiceContentTypes> {
  final Async async;

  ContentTypesModule(ServiceContentTypes retrofitService) {
    super(retrofitService);
    this.async = new Async();
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
  @SuppressWarnings("unchecked") public CMAContentType create(String spaceId,
      CMAContentType contentType) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(contentType, "contentType");

    String contentTypeId = contentType.getResourceId();
    HashMap sys = contentType.sys;
    contentType.sys = null;

    try {
      CMAContentType result;
      if (contentTypeId == null) {
        result = service.contentTypesCreate(spaceId, contentType);
      } else {
        result = service.contentTypesCreate(spaceId, contentTypeId, contentType);
      }
      contentType.sys = sys;
      return result;
    } catch (RetrofitError e) {
      contentType.sys = sys;
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
    return service.contentTypesDelete(spaceId, contentTypeId);
  }

  /**
   * Fetch all Content Types from a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAContentType> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.contentTypesFetchOne(spaceId);
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
    return service.contentTypesFetchOne(spaceId, contentTypeId);
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
    return service.contentTypesPublish(contentType.getVersion(), spaceId, contentTypeId);
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
    return service.contentTypesUnPublish(spaceId, contentTypeId);
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
    return service.contentTypesUpdate(contentType.getVersion(), spaceId, contentTypeId,
        contentType);
  }

  public Async async() {
    return async;
  }

  final class Async {
    public CMACallback<CMAContentType> create(final String spaceId,
        final CMAContentType contentType, CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ContentTypesModule.this.create(spaceId, contentType);
        }
      }, callback);
    }

    public CMACallback<Response> delete(final String spaceId, final String contentTypeId,
        CMACallback<Response> callback) {
      return defer(new DefFunc<Response>() {
        @Override Response method() {
          return ContentTypesModule.this.delete(spaceId, contentTypeId);
        }
      }, callback);
    }

    public CMACallback<CMAArray<CMAContentType>> fetchAll(final String spaceId,
        CMACallback<CMAArray<CMAContentType>> callback) {
      return defer(new DefFunc<CMAArray<CMAContentType>>() {
        @Override CMAArray<CMAContentType> method() {
          return ContentTypesModule.this.fetchAll(spaceId);
        }
      }, callback);
    }

    public CMACallback<CMAContentType> fetchOne(final String spaceId, final String contentTypeId,
        CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ContentTypesModule.this.fetchOne(spaceId, contentTypeId);
        }
      }, callback);
    }

    public CMACallback<CMAContentType> publish(final CMAContentType contentType,
        CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ContentTypesModule.this.publish(contentType);
        }
      }, callback);
    }

    public CMACallback<CMAContentType> unPublish(final CMAContentType contentType,
        CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ContentTypesModule.this.unPublish(contentType);
        }
      }, callback);
    }

    public CMACallback<CMAContentType> update(final CMAContentType contentType,
        CMACallback<CMAContentType> callback) {
      return defer(new DefFunc<CMAContentType>() {
        @Override CMAContentType method() {
          return ContentTypesModule.this.update(contentType);
        }
      }, callback);
    }
  }
}
