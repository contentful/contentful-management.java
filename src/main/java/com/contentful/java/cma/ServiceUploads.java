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

import com.contentful.java.cma.model.CMAUpload;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * UploadsService.
 * <p>
 * This service will be used to wrap the uploading rest calls to Contentfuls endpoint.
 */
interface ServiceUploads {
  @POST("spaces/{spaceId}/uploads")
  Flowable<CMAUpload> create(
      @Path("spaceId") String spaceId,
      @Body RequestBody payload);

  @GET("spaces/{spaceId}/uploads/{uploadId}")
  Flowable<CMAUpload> fetchOne(
      @Path("spaceId") String spaceId,
      @Path("uploadId") String uploadId);

  @DELETE("spaces/{spaceId}/uploads/{uploadId}")
  Flowable<Response<Void>> delete(
      @Path("spaceId") String spaceId,
      @Path("uploadId") String uploadId);
}
