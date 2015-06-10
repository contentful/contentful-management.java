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

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAAsset;
import java.util.Map;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Assets Service.
 */
interface ServiceAssets {
  @PUT("/spaces/{space}/assets/{asset}/archived")
  CMAAsset archive(
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Body Object body);

  @POST("/spaces/{space}/assets")
  CMAAsset create(
      @Path("space") String spaceId,
      @Body CMAAsset asset);

  @PUT("/spaces/{space}/assets/{asset}")
  CMAAsset create(
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Body CMAAsset asset);

  @DELETE("/spaces/{space}/assets/{asset}")
  Response delete(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @GET("/spaces/{space}/assets")
  CMAArray<CMAAsset> fetchAll(
      @Path("space") String spaceId,
      @QueryMap Map<String, String> query);

  @GET("/spaces/{space}/assets/{asset}")
  CMAAsset fetchOne(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @PUT("/spaces/{space}/assets/{asset}/files/{locale}/process")
  Response process(
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Path("locale") String locale,
      @Body Object body);

  @PUT("/spaces/{space}/assets/{asset}/published")
  CMAAsset publish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Body Object body);

  @DELETE("/spaces/{space}/assets/{asset}/archived")
  CMAAsset unArchive(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @DELETE("/spaces/{space}/assets/{asset}/published")
  CMAAsset unPublish(
      @Path("space") String spaceId,
      @Path("asset") String assetId);

  @PUT("/spaces/{space}/assets/{asset}")
  CMAAsset update(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("asset") String assetId,
      @Body CMAAsset asset);
}
