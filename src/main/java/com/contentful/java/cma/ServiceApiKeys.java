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

import com.contentful.java.cma.model.CMAApiKey;
import com.contentful.java.cma.model.CMAArray;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Api Token Service.
 */
interface ServiceApiKeys {
  @GET("/spaces/{spaceId}/api_keys")
  Observable<CMAArray<CMAApiKey>> fetchAll(@Path("spaceId") String spaceId);

  @GET("/spaces/{spaceId}/api_keys")
  Observable<CMAArray<CMAApiKey>> fetchAll(
      @Path("spaceId") String spaceId,
      @QueryMap Map<String, String> query
  );

  @GET("/spaces/{spaceId}/api_keys/{keyId}")
  Observable<CMAApiKey> fetchOne(@Path("spaceId") String spaceId,
                                 @Path("keyId") String keyId);

  @POST("/spaces/{spaceId}/api_keys")
  Observable<CMAApiKey> create(@Path("spaceId") String spaceId,
                               @Body CMAApiKey key);

}
