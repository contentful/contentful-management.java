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

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Entries Service.
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
