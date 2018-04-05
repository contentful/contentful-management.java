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
import com.contentful.java.cma.model.CMAAsset;

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
 * Assets Service.
 */
interface ServiceAssets {
  @PUT("spaces/{space}/environments/{environment}/assets/{asset}/archived")
  Flowable<CMAAsset> archive(
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Path("asset") String assetId);

  @POST("spaces/{space}/environments/{environment}/assets")
  Flowable<CMAAsset> create(
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Body CMAAsset asset);

  @PUT("spaces/{space}/environments/{environment}/assets/{asset}")
  Flowable<CMAAsset> create(
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Path("asset") String assetId,
      @Body CMAAsset asset);

  @DELETE("spaces/{space}/environments/{environment}/assets/{asset}")
  Flowable<Response<Void>> delete(
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Path("asset") String assetId);

  @GET("spaces/{space}/environments/{environment}/assets")
  Flowable<CMAArray<CMAAsset>> fetchAll(
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @QueryMap Map<String, String> query);

  @GET("spaces/{space}/environments/{environment}/assets/{asset}")
  Flowable<CMAAsset> fetchOne(
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Path("asset") String assetId);

  @PUT("spaces/{space}/environments/{environment}/assets/{asset}/files/{locale}/process")
  Flowable<Response<Void>> process(
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Path("asset") String assetId,
      @Path("locale") String locale);

  @PUT("spaces/{space}/environments/{environment}/assets/{asset}/published")
  Flowable<CMAAsset> publish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Path("asset") String assetId);

  @DELETE("spaces/{space}/environments/{environment}/assets/{asset}/archived")
  Flowable<CMAAsset> unArchive(
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Path("asset") String assetId);

  @DELETE("spaces/{space}/environments/{environment}/assets/{asset}/published")
  Flowable<CMAAsset> unPublish(
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Path("asset") String assetId);

  @PUT("spaces/{space}/environments/{environment}/assets/{asset}")
  Flowable<CMAAsset> update(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("environment") String environment,
      @Path("asset") String assetId,
      @Body CMAAsset asset);
}
