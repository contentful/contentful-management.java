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
import com.contentful.java.cma.model.CMAPersonalAccessToken;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Personal Access Token Service.
 */
interface ServicePersonalAccessTokens {
  @GET("/users/me/access_tokens")
  Observable<CMAArray<CMAPersonalAccessToken>> fetchAll();

  @GET("/users/me/access_tokens/{tokenId}")
  Observable<CMAPersonalAccessToken> fetchOne(@Path("tokenId") String tokenId);

  @POST("/users/me/access_tokens")
  Observable<CMAPersonalAccessToken> create(@Body CMAPersonalAccessToken token);

  @PUT("/users/me/access_tokens/{tokenId}/revoked")
  Observable<CMAPersonalAccessToken> revoke(@Path("tokenId") String tokenId);
}
