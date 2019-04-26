/*
 * Copyright (C) 2019 Contentful GmbH
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
import com.contentful.java.cma.model.CMAEnvironment;
import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Environments Service.
 */
interface ServiceEnvironments {
  @POST("/spaces/{spaceId}/environments")
  Flowable<CMAEnvironment> create(
      @Path("spaceId") String spaceId,
      @Body CMAEnvironment environment);

  @POST("/spaces/{spaceId}/environments")
  Flowable<CMAEnvironment> clone(
      @Path("spaceId") String spaceId,
      @Header("X-Contentful-Source-Environment") String sourceEnvironmentId,
      @Body CMAEnvironment environment);

  @PUT("/spaces/{spaceId}/environments/{environmentId}")
  Flowable<CMAEnvironment> create(
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId,
      @Body CMAEnvironment environment);

  @PUT("/spaces/{spaceId}/environments/{environmentId}")
  Flowable<CMAEnvironment> clone(
      @Path("spaceId") String spaceId,
      @Header("X-Contentful-Source-Environment") String sourceEnvironmentId,
      @Path("environmentId") String environmentId,
      @Body CMAEnvironment environment);

  @DELETE("/spaces/{spaceId}/environments/{environmentId}")
  Flowable<Response<Void>> delete(
      @Header("X-Contentful-Version") Integer version,
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId);

  @GET("/spaces/{spaceId}/environments")
  Flowable<CMAArray<CMAEnvironment>> fetchAll(
      @Path("spaceId") String spaceId
  );

  @GET("/spaces/{spaceId}/environments/{environmentId}")
  Flowable<CMAEnvironment> fetchOne(
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId
  );

  @PUT("/spaces/{spaceId}/environments/{environmentId}")
  Flowable<CMAEnvironment> update(
      @Header("X-Contentful-Version") Integer version,
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId,
      @Body CMAEnvironment environment);
}
