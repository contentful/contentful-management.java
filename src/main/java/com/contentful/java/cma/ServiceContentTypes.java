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
import com.contentful.java.cma.model.CMAContentType;
import com.contentful.java.cma.model.CMASnapshot;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import io.reactivex.Flowable;

/**
 * ContentTypes Service.
 */
interface ServiceContentTypes {
  @POST("/spaces/{space}/content_types")
  Flowable<CMAContentType> create(
      @Path("space") String spaceId,
      @Body CMAContentType contentType);

  @PUT("/spaces/{space}/content_types/{content_type}")
  Flowable<CMAContentType> create(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId,
      @Body CMAContentType contentType);

  @DELETE("/spaces/{space}/content_types/{content_type}")
  Flowable<String> delete(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @GET("/spaces/{space}/content_types")
  Flowable<CMAArray<CMAContentType>> fetchAll(
      @Path("space") String spaceId,
      @QueryMap Map<String, String> query);

  @GET("/spaces/{spaceId}/content_types/{contentTypeId}/snapshots")
  Flowable<CMAArray<CMASnapshot>> fetchAllSnapshots(
      @Path("spaceId") String spaceId,
      @Path("contentTypeId") String contentTypeId);

  @GET("/spaces/{space}/content_types/{content_type}")
  Flowable<CMAContentType> fetchOne(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @GET("/spaces/{spaceId}/content_types/{contentTypeId}/snapshots/{snapshotId}")
  Flowable<CMASnapshot> fetchOneSnapshot(
      @Path("spaceId") String spaceId,
      @Path("contentTypeId") String contentTypeId,
      @Path("snapshotId") String snapshotId);

  @PUT("/spaces/{space}/content_types/{content_type}/published")
  Flowable<CMAContentType> publish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @DELETE("/spaces/{space}/content_types/{content_type}/published")
  Flowable<CMAContentType> unPublish(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @PUT("/spaces/{space}/content_types/{content_type}")
  Flowable<CMAContentType> update(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId,
      @Body CMAContentType resource);
}
