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
import com.contentful.java.cma.model.CMAAsset;

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
 * Assets Service.
 */
interface ServiceAssets {
  @PUT("spaces/{space}/assets/{asset}/archived")
  Flowable<CMAAsset> archive(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @POST("spaces/{space}/assets")
  Flowable<CMAAsset> create(
      @Path("space") String spaceId,
      @Body CMAAsset asset);

  @PUT("spaces/{space}/assets/{asset}")
  Flowable<CMAAsset> create(
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Body CMAAsset asset);

  @DELETE("spaces/{space}/assets/{asset}")
  Flowable<String> delete(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @GET("spaces/{space}/assets")
  Flowable<CMAArray<CMAAsset>> fetchAll(
      @Path("space") String spaceId,
      @QueryMap Map<String, String> query);

  @GET("spaces/{space}/assets/{asset}")
  Flowable<CMAAsset> fetchOne(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @PUT("spaces/{space}/assets/{asset}/files/{locale}/process")
  Flowable<String> process(
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Path("locale") String locale);

  @PUT("spaces/{space}/assets/{asset}/published")
  Flowable<CMAAsset> publish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @DELETE("spaces/{space}/assets/{asset}/archived")
  Flowable<CMAAsset> unArchive(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @DELETE("spaces/{space}/assets/{asset}/published")
  Flowable<CMAAsset> unPublish(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @PUT("spaces/{space}/assets/{asset}")
  Flowable<CMAAsset> update(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Body CMAAsset asset);
}
