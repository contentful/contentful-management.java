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
 * Created by tomxor on 11/11/14.
 */
interface ServiceAssets {
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
}
