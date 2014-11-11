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
interface ServiceEntries {
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
}
