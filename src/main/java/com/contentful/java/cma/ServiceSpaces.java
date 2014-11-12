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
 * ServiceSpaces.
 */
interface ServiceSpaces {
  @POST("/spaces")
  CMASpace spacesCreate(
      @Body CMASpace space);

  @POST("/spaces")
  CMASpace spacesCreate(
      @Header("X-Contentful-Organization") String organization,
      @Body CMASpace space);

  @DELETE("/spaces/{space}")
  Response spacesDelete(
      @Path("space") String spaceId);

  @GET("/spaces/{space}")
  CMASpace spacesFetchOne(
      @Path("space") String spaceId);

  @GET("/spaces")
  CMAArray<CMASpace> spacesFetchAll();

  @PUT("/spaces/{space}")
  CMASpace spacesUpdate(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Body CMASpace space);
}
