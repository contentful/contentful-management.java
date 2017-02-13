/*
 * Copyright (C) 2017 Contentful GmbH
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

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAEntry;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Entries Service.
 */
interface ServiceEntries {
  @PUT("/spaces/{space}/entries/{entry}/archived")
  Observable<CMAEntry> archive(
      @Path("space") String spaceId,
      @Path("entry") String entryId,
      @Body Object body);

  @POST("/spaces/{space}/entries")
  Observable<CMAEntry> create(
      @Path("space") String spaceId,
      @Header("X-Contentful-Content-Type") String contentType,
      @Body CMAEntry entry);

  @PUT("/spaces/{space}/entries/{entry}")
  Observable<CMAEntry> create(
      @Path("space") String spaceId,
      @Header("X-Contentful-Content-Type") String contentType,
      @Path("entry") String entryId,
      @Body CMAEntry entry);

  @DELETE("/spaces/{space}/entries/{entry}")
  Observable<String> delete(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @GET("/spaces/{space}/entries/{entry}")
  Observable<CMAEntry> fetchOne(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @GET("/spaces/{space}/entries")
  Observable<CMAArray<CMAEntry>> fetchAll(
      @Path("space") String spaceId,
      @QueryMap Map<String, String> query);

  @PUT("/spaces/{space}/entries/{entry}/published")
  Observable<CMAEntry> publish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("entry") String entryId,
      @Body Object body);

  @DELETE("/spaces/{space}/entries/{entry}/archived")
  Observable<CMAEntry> unArchive(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @DELETE("/spaces/{space}/entries/{entry}/published")
  Observable<CMAEntry> entriesUnPublish(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @PUT("/spaces/{space}/entries/{entry}")
  Observable<CMAEntry> update(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("entry") String entryId,
      @Body CMAEntry entry);
}
