/*
 * Copyright (C) 2018 Contentful GmbH
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
import com.contentful.java.cma.model.CMASnapshot;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Entries Service.
 */
interface ServiceEntries {
  @PUT("/spaces/{space}/entries/{entry}/archived")
  Flowable<CMAEntry> archive(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @DELETE("/spaces/{space}/entries/{entry}/archived")
  Flowable<CMAEntry> unArchive(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @POST("/spaces/{space}/entries")
  Flowable<CMAEntry> create(
      @Path("space") String spaceId,
      @Header("X-Contentful-Content-Type") String contentType,
      @Body CMAEntry entry);

  @PUT("/spaces/{space}/entries/{entry}")
  Flowable<CMAEntry> create(
      @Path("space") String spaceId,
      @Header("X-Contentful-Content-Type") String contentType,
      @Path("entry") String entryId,
      @Body CMAEntry entry);

  @DELETE("/spaces/{space}/entries/{entry}")
  Flowable<Response<Void>> delete(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @GET("/spaces/{space}/entries/{entry}")
  Flowable<CMAEntry> fetchOne(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @GET("/spaces/{spaceId}/entries/{entryId}/snapshots/{snapshotId}")
  Flowable<CMASnapshot> fetchOneSnapshot(
      @Path("spaceId") String spaceId,
      @Path("entryId") String entryId,
      @Path("snapshotId") String snapshotId);

  @GET("/spaces/{space}/entries")
  Flowable<CMAArray<CMAEntry>> fetchAll(
      @Path("space") String spaceId,
      @QueryMap Map<String, String> query);

  @GET("/spaces/{spaceId}/entries/{entryId}/snapshots")
  Flowable<CMAArray<CMASnapshot>> fetchAllSnapshots(
      @Path("spaceId") String spaceId,
      @Path("entryId") String entryId);

  @PUT("/spaces/{space}/entries/{entry}/published")
  Flowable<CMAEntry> publish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @DELETE("/spaces/{space}/entries/{entry}/published")
  Flowable<CMAEntry> unPublish(
      @Path("space") String spaceId,
      @Path("entry") String entryId);

  @PUT("/spaces/{space}/entries/{entry}")
  Flowable<CMAEntry> update(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("entry") String entryId,
      @Body CMAEntry entry);
}
