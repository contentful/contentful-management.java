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
import com.contentful.java.cma.model.CMATag;
import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Body;
import retrofit2.http.DELETE;

import java.util.Map;

/**
 * Spaces Service.
 */
interface ServiceContentTags {

  @GET("/spaces/{space_id}/environments/{environment_id}/tags")
  Flowable<CMAArray<CMATag>> fetchAll(
      @Path("space_id") String spaceId,
      @Path("environment_id") String environmentID,
      @QueryMap Map<String, String> query
  );
  @PUT("/spaces/{space_id}/environments/{environment_id}/tags/{tag_id}")
  Flowable<CMATag> create(
          @Path("space_id") String spaceId,
          @Path("environment_id") String environmentID,
          @Path("tag_id") String tagId,
          @Body CMATag tag);

  @GET("/spaces/{space_id}/environments/{environment_id}/tags/{tag_id}")
  Flowable<CMATag> fetchOne(
      @Path("space_id") String spaceId,
      @Path("environment_id") String environmentID,
      @Path("tag_id") String tagId
  );

  @PUT("/spaces/{space_id}/environments/{environment_id}/tags/{tag_id}")
  Flowable<CMATag> update(
          @Path("space_id") String spaceId,
          @Path("environment_id") String environmentID,
          @Path("tag_id") String tagId,
          @Body CMATag tag);

  @DELETE("/spaces/{space_id}/environments/{environment_id}/tags/{tag_id}")
  Flowable<Response<Void>> delete(
          @Path("space_id") String spaceId,
          @Path("environment_id") String environmentID,
          @Path("tag_id") String tagId);
}
