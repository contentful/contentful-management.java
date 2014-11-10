package com.contentful.java.cma;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * CMA Retrofit Service.
 */
interface CMAService {
  /** Assets */
  @PUT("/spaces/{space}/assets/{asset}/archived")
  CMAAsset assetsArchive(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @POST("/spaces/{space}/assets")
  CMAAsset assetsCreate(
      @Path("space") String spaceId,
      @Body CMAAsset asset);

  @PUT("/spaces/{space}/assets/{asset}")
  CMAAsset assetsCreate(
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Body CMAAsset asset);

  @DELETE("/spaces/{space}/assets/{asset}")
  Response assetsDelete(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @GET("/spaces/{space}/assets")
  CMAArray<CMAAsset> assetsFetchAll(
      @Path("space") String spaceId);

  @GET("/spaces/{space}/assets/{asset}")
  CMAAsset assetsFetchOne(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @PUT("/spaces/{space}/assets/{asset}/files/{locale}/process")
  Response assetsProcess(
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Path("locale") String locale);

  @PUT("/spaces/{space}/assets/{asset}/published")
  CMAAsset assetsPublish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @DELETE("/spaces/{space}/assets/{asset}/archived")
  CMAAsset assetsUnArchive(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @DELETE("/spaces/{space}/assets/{asset}/published")
  CMAAsset assetsUnPublish(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @PUT("/spaces/{space}/assets/{asset}")
  CMAAsset assetsUpdate(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Body CMAAsset asset);

  /** Content Types */
  @POST("/spaces/{space}/content_types")
  CMAContentType contentTypesCreate(
      @Path("space") String spaceId,
      @Body CMAContentType contentType);

  @PUT("/spaces/{space}/content_types/{content_type}")
  CMAContentType contentTypesCreate(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId,
      @Body CMAContentType contentType);

  @DELETE("/spaces/{space}/content_types/{content_type}")
  Response contentTypesDelete(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @GET("/spaces/{space}/content_types/{content_type}")
  CMAContentType contentTypesFetchOne(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @GET("/spaces/{space}/content_types")
  CMAArray<CMAContentType> contentTypesFetchOne(
      @Path("space") String spaceId);

  @PUT("/spaces/{space}/content_types/{content_type}/published")
  CMAContentType contentTypesPublish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @DELETE("/spaces/{space}/content_types/{content_type}/published")
  CMAContentType contentTypesUnPublish(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @PUT("/spaces/{space}/content_types/{content_type}")
  CMAContentType contentTypesUpdate(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId,
      @Body CMAContentType resource);

  /** Entries */
  @PUT("/spaces/{space}/entries/{entry}/archived")
  CMAEntry entriesArchive(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @POST("/spaces/{space}/entries")
  CMAEntry entriesCreate(
      @Path("space") String spaceId,
      @Body CMAEntry entry);

  @PUT("/spaces/{space}/entries/{entry}")
  CMAEntry entriesCreate(
      @Path("space") String spaceId,
      @Path("entry") String entryId,
      @Body CMAEntry entry);

  @DELETE("/spaces/{space}/entries/{entry}")
  Response entriesDelete(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @GET("/spaces/{space}/entries/{entry}")
  CMAEntry entriesFetchOne(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @GET("/spaces/{space}/entries")
  CMAArray<CMAEntry> entriesFetchAll(
      @Path("space") String spaceId);

  @PUT("/spaces/{space}/entries/{entry}/published")
  CMAEntry entriesPublish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @DELETE("/spaces/{space}/entries/{entry}/archived")
  CMAEntry entriesUnArchive(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @DELETE("/spaces/{space}/entries/{entry}/published")
  CMAEntry entriesUnPublish(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @PUT("/spaces/{space}/entries/{entry}")
  CMAEntry entriesUpdate(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("entry") String entryId,
      @Body CMAEntry entry);

  /** Spaces */
  @POST("/spaces")
  CMASpace spacesCreate(
      @Body CMASpace space);

  @POST("/spaces")
  CMASpace spacesCreate(
      @Header("X-Contentful-Organization") String organization,
      @Body CMASpace space);

  @DELETE("/spaces/{space}")
  Response spacesDelete(
      @Path("space") String spaceId);

  @GET("/spaces/{space}")
  CMASpace spacesFetchOne(
      @Path("space") String spaceId);

  @GET("/spaces")
  CMAArray<CMASpace> spacesFetchAll();

  @PUT("/spaces/{space}")
  CMASpace spacesUpdate(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Body CMASpace space);
}
