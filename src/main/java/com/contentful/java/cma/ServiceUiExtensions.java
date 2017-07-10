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
import com.contentful.java.cma.model.CMAUiExtension;

import java.util.Map;

import retrofit2.Response;
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
 * Ui Extensions Service.
 */
interface ServiceUiExtensions {
  @GET("/spaces/{spaceId}/extensions")
  Flowable<CMAArray<CMAUiExtension>> fetchAll(@Path("spaceId") String spaceId);

  @GET("/spaces/{spaceId}/extensions")
  Flowable<CMAArray<CMAUiExtension>> fetchAll(
      @Path("spaceId") String spaceId,
      @QueryMap Map<String, String> query
  );

  @GET("/spaces/{spaceId}/extensions/{extensionId}")
  Flowable<CMAUiExtension> fetchOne(
      @Path("spaceId") String spaceId,
      @Path("extensionId") String extensionId
  );

  @POST("/spaces/{spaceId}/extensions")
  Flowable<CMAUiExtension> create(
      @Path("spaceId") String spaceId,
      @Body CMAUiExtension extension
  );

  @PUT("/spaces/{spaceId}/extensions/{extensionId}")
  Flowable<CMAUiExtension> create(
      @Path("spaceId") String spaceId,
      @Path("extensionId") String extensionId,
      @Body CMAUiExtension extension
  );

  @PUT("/spaces/{spaceId}/extensions/{extensionId}")
  Flowable<CMAUiExtension> update(
      @Path("spaceId") String spaceId,
      @Path("extensionId") String extensionId,
      @Body CMAUiExtension extension,
      @Header("X-Contentful-Version") Integer version
  );

  @DELETE("/spaces/{spaceId}/extensions/{extensionId}")
  Flowable<Response<Void>> delete(
      @Path("spaceId") String spaceId,
      @Path("extensionId") String extensionId,
      @Header("X-Contentful-Version") Integer version
  );
}
