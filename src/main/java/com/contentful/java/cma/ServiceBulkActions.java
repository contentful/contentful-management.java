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

import com.contentful.java.cma.model.CMABulkAction;
import com.contentful.java.cma.model.CMAPayload;
import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Spaces Service.
 */
interface ServiceBulkActions {
  @GET("spaces/{space}/environments/{environmentId}/bulk_actions/actions/{bulk_action_id}")
  Flowable<CMABulkAction> fetch(
          @Path("space") String spaceId,
          @Path("environmentId") String environmentId,
          @Path("bulk_action_id") String bulkActionId);

  @POST("spaces/{space}/environments/{environmentId}/bulk_actions/publish")
  Flowable<CMABulkAction> publish(
          @Path("space") String spaceId,
          @Path("environmentId") String environmentId,
          @Body CMAPayload payload);

  @POST("spaces/{space}/environments/{environmentId}/bulk_actions/unpublish")
  Flowable<CMABulkAction> unpublish(
          @Path("space") String spaceId,
          @Path("environmentId") String environmentId,
          @Body CMAPayload payload);

  @POST("spaces/{space}/environments/{environmentId}/bulk_actions/validate")
  Flowable<CMABulkAction> validate(
          @Path("space") String spaceId,
          @Path("environmentId") String environmentId,
          @Body CMAPayload payload);

}
