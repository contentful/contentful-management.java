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
import com.contentful.java.cma.model.CMAScheduledAction;
import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Scheduled actions service.
 */
interface ServiceScheduledActions {
  @GET("spaces/{space}/scheduled_actions")
  Flowable<CMAArray<CMAScheduledAction>> fetchAll(
          @Path("space") String spaceId,
          @Query("entity.sys.id") String entityId,
          @Query("environment.sys.id") String environmentId
  );

  @GET("spaces/{space}/scheduled_actions/{scheduled_action_id}")
  Flowable<CMAScheduledAction> fetchOne(
          @Path("space") String spaceId,
          @Path("scheduled_action_id") String scheduledActionId,
          @Query("entity.sys.id") String entityId,
          @Query("environment.sys.id") String environmentId
  );

  @POST("spaces/{space}/scheduled_actions/{scheduled_action_id}")
  Flowable<CMAScheduledAction> update(
          @Path("space") String spaceId,
          @Path("scheduled_action_id") String scheduledActionId,
          @Body CMAScheduledAction scheduledAction
  );

  @POST("spaces/{space}/scheduled_actions")
  Flowable<CMAScheduledAction> create(
          @Path("space") String spaceId,
          @Body CMAScheduledAction scheduledAction
  );
  @DELETE("spaces/{space}/scheduled_actions/{scheduled_action_id}")
  Flowable<CMAScheduledAction> cancel(
          @Path("space") String spaceId,
          @Path("scheduled_action_id") String scheduledActionId,
          @Query("environment.sys.id") String environmentId
  );

}
