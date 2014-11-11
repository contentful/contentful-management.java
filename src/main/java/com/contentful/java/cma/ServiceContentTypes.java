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
interface ServiceContentTypes {
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
}
