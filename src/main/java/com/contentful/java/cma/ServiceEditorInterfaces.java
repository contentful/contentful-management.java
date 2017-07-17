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

import com.contentful.java.cma.model.CMAEditorInterface;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import io.reactivex.Flowable;

/**
 * Editor Interfaces Service.
 */
interface ServiceEditorInterfaces {
  @GET("/spaces/{spaceId}/content_types/{contentTypeId}/editor_interface")
  Flowable<CMAEditorInterface> fetchOne(
      @Path("spaceId") String spaceId,
      @Path("contentTypeId") String contentTypeId
  );

  @PUT("/spaces/{spaceId}/content_types/{contentTypeId}/editor_interface")
  Flowable<CMAEditorInterface> update(
      @Path("spaceId") String spaceId,
      @Path("contentTypeId") String contentTypeId,
      @Body CMAEditorInterface editor,
      @Header("X-Contentful-Version") Integer version
  );
}
