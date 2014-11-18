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
import com.contentful.java.cma.model.CMAContentType;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * ContentTypes Service.
 */
interface ServiceContentTypes {
  @POST("/spaces/{space}/content_types")
  CMAContentType create(
      @Path("space") String spaceId,
      @Body CMAContentType contentType);

  @PUT("/spaces/{space}/content_types/{content_type}")
  CMAContentType create(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId,
      @Body CMAContentType contentType);

  @DELETE("/spaces/{space}/content_types/{content_type}")
  Response delete(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @GET("/spaces/{space}/content_types")
  CMAArray<CMAContentType> fetchAll(
      @Path("space") String spaceId);

  @GET("/spaces/{space}/content_types/{content_type}")
  CMAContentType fetchOne(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @PUT("/spaces/{space}/content_types/{content_type}/published")
  CMAContentType publish(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @DELETE("/spaces/{space}/content_types/{content_type}/published")
  CMAContentType unPublish(
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId);

  @PUT("/spaces/{space}/content_types/{content_type}")
  CMAContentType update(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("content_type") String contentTypeId,
      @Body CMAContentType resource);
}
