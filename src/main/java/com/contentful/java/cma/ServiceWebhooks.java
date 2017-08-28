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
import com.contentful.java.cma.model.CMAWebhook;
import com.contentful.java.cma.model.CMAWebhookCall;
import com.contentful.java.cma.model.CMAWebhookCallDetail;
import com.contentful.java.cma.model.CMAWebhookHealth;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Webhooks Service.
 * <p>
 * Use this class to create a retrofit service for {@link ModuleWebhooks}.
 */
interface ServiceWebhooks {
  @POST("spaces/{space}/webhook_definitions")
  Flowable<CMAWebhook> create(
      @Path("space") String spaceId,
      @Body CMAWebhook webhook);

  @PUT("spaces/{space}/webhook_definitions/{webhook}")
  Flowable<CMAWebhook> create(
      @Path("space") String spaceId,
      @Path("webhook") String webhookId,
      @Body CMAWebhook webhook);

  @DELETE("spaces/{space}/webhook_definitions/{webhook}")
  Flowable<Response<Void>> delete(
      @Path("space") String spaceId,
      @Path("webhook") String webhookId);

  @GET("spaces/{space}/webhook_definitions")
  Flowable<CMAArray<CMAWebhook>> fetchAll(
      @Path("space") String spaceId);

  @GET("spaces/{space}/webhook_definitions")
  Flowable<CMAArray<CMAWebhook>> fetchAll(
      @Path("space") String spaceId,
      @QueryMap Map<String, String> query
  );

  @GET("spaces/{space}/webhook_definitions/{webhook}")
  Flowable<CMAWebhook> fetchOne(
      @Path("space") String spaceId,
      @Path("webhook") String webhookId);

  @PUT("spaces/{space}/webhook_definitions/{webhook}")
  Flowable<CMAWebhook> update(
      @Header("X-Contentful-Version") Integer version,
      @Path("space") String spaceId,
      @Path("webhook") String webhookId,
      @Body CMAWebhook webhook);

  @GET("spaces/{space}/webhooks/{webhook}/calls")
  Flowable<CMAArray<CMAWebhookCall>> calls(
      @Path("space") String spaceId,
      @Path("webhook") String webhookId);

  @GET("spaces/{space}/webhooks/{webhook}/calls/{call}")
  Flowable<CMAWebhookCallDetail> callDetails(
      @Path("space") String spaceId,
      @Path("webhook") String webhookId,
      @Path("call") String callId);

  @GET("spaces/{space}/webhooks/{webhook}/health")
  Flowable<CMAWebhookHealth> health(
      @Path("space") String spaceId,
      @Path("webhook") String webhookId);
}
