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
interface ServiceSpaces {
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
