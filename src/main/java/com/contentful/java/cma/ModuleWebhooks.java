/*
 * Copyright (C) 2018 Contentful GmbH
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
import com.contentful.java.cma.model.CMANotWithEnvironmentsException;
import com.contentful.java.cma.model.CMASystem;
import com.contentful.java.cma.model.CMAWebhook;
import com.contentful.java.cma.model.CMAWebhookCall;
import com.contentful.java.cma.model.CMAWebhookCallDetail;
import com.contentful.java.cma.model.CMAWebhookHealth;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * This module is intended to ease communication with the webhooks module from Contentful.
 * <p>
 * All methods here are available twice: Once synchronously and asynchronously through
 * {@link ModuleWebhooks#async()}.
 */
public class ModuleWebhooks extends AbsModule<ServiceWebhooks> {
  final Async async;

  /**
   * Create this module.
   *
   * @param retrofit         An instance to an already setup Retrofit module
   * @param callbackExecutor Use this executor for this module.
   */
  public ModuleWebhooks(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    this.async = new Async();
  }

  /**
   * Internal method used for creating the service.
   *
   * @param retrofit A {@link Retrofit} instance to create the service from.
   * @return The service, to be used for calls to the backend.
   */
  @Override protected ServiceWebhooks createService(Retrofit retrofit) {
    return retrofit.create(ServiceWebhooks.class);
  }

  /**
   * Use this method if you require asynchronous requests through retrofit.
   *
   * @return An instance of this classes Async representation.
   */
  public Async async() {
    return async;
  }

  /**
   * Create a new webhook.
   * <p>
   * This will create a new ID and return the newly created webhook as a return value.
   *
   * @param webhook A representation of the Webhook to be used.
   * @return The webhook returned from the backend, containing created its ID and more.
   * @throws IllegalArgumentException        if configured space id is null.
   * @throws IllegalArgumentException        if webhook is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAWebhook create(CMAWebhook webhook) {
    throwIfEnvironmentIdIsSet();

    return create(spaceId, webhook);
  }

  /**
   * Create a new webhook.
   * <p>
   * This will create a new ID and return the newly created webhook as a return value.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId Which space should be used?
   * @param webhook A representation of the Webhook to be used.
   * @return The webhook returned from the backend, containing created its ID and more.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if webhook is null.
   */
  public CMAWebhook create(String spaceId, CMAWebhook webhook) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(webhook, "webhook");
    final String webhookId = webhook.getId();

    final CMASystem system = webhook.getSystem();
    webhook.setSystem(null);
    try {
      if (webhookId == null) {
        return service.create(spaceId, webhook).blockingFirst();
      } else {
        return service.create(spaceId, webhookId, webhook).blockingFirst();
      }
    } finally {
      webhook.setSystem(system);
    }
  }

  /**
   * Delete a given Webhook.
   *
   * @param webhook actual webhook to be deleted.
   * @return the response code of the request (aka 200 if successful)
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if webhookId is null.
   */
  public Integer delete(CMAWebhook webhook) {
    final String webhookId = getResourceIdOrThrow(webhook, "webhook");
    final String spaceId = getSpaceIdOrThrow(webhook, "webhook");

    return service.delete(spaceId, webhookId).blockingFirst().code();
  }

  /**
   * Retrieve all the webhooks defined the configured space.
   *
   * @return An {@link CMAArray} containing all found webhooks for this space.
   * @throws IllegalArgumentException        if configured spaceId is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMAWebhook> fetchAll() {
    throwIfEnvironmentIdIsSet();

    return fetchAll(spaceId);
  }

  /**
   * Retrieve specific webhooks matching a query for this space.
   *
   * @param query Specifying the criteria on which webhooks to return.
   * @return An {@link CMAArray} containing all found webhooks for this space.
   * @throws IllegalArgumentException        if configured spaceId is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMAWebhook> fetchAll(Map<String, String> query) {
    throwIfEnvironmentIdIsSet();

    return fetchAll(spaceId, query);
  }

  /**
   * Retrieve all the webhooks defined for this space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId The id of the space to be asked for all of its spaces.
   * @return An {@link CMAArray} containing all found webhooks for this space.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAWebhook> fetchAll(String spaceId) {
    return fetchAll(spaceId, null);
  }

  /**
   * Retrieve specific webhooks matching a query for this space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId The id of the space to be asked for all of its spaces.
   * @param query   Specifying the criteria on which webhooks to return.
   * @return A {@link CMAArray} containing all found webhooks for this space.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAWebhook> fetchAll(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");

    if (query == null) {
      return service.fetchAll(spaceId).blockingFirst();
    } else {
      return service.fetchAll(spaceId, query).blockingFirst();
    }
  }

  /**
   * Retrieve exactly one webhook, whose id you know.
   *
   * @param webhookId The id of the webhook to be returned.
   * @return The webhook found, or null, if no such webhook is available.
   * @throws IllegalArgumentException        if configured spaceId is null.
   * @throws IllegalArgumentException        if webhookId is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAWebhook fetchOne(String webhookId) {
    throwIfEnvironmentIdIsSet();

    return fetchOne(spaceId, webhookId);
  }

  /**
   * Retrieve exactly one webhook, whose id you know.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId   The id of the space to be hosting this webhook.
   * @param webhookId The id of the webhook to be returned.
   * @return The webhook found, or null, if no such webhook is available.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if webhookId is null.
   */
  public CMAWebhook fetchOne(String spaceId, String webhookId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(webhookId, "webhookId");

    return service.fetchOne(spaceId, webhookId).blockingFirst();
  }

  /**
   * Change the content of a webhook.
   * <p>
   * This will take the webhooks id and update it on the backend.
   *
   * @param webhook The webhook retrieved beforehand to be changed.
   * @return The from the backend returned, changed webhook.
   * @throws IllegalArgumentException if webhook is null.
   * @throws IllegalArgumentException if webhookId is null.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if version is null.
   */
  public CMAWebhook update(CMAWebhook webhook) {
    assertNotNull(webhook, "webhook");

    final String webhookId = getResourceIdOrThrow(webhook, "webhook");
    final String spaceId = getSpaceIdOrThrow(webhook, "webhook");
    final Integer version = getVersionOrThrow(webhook, "webhook");

    return service.update(version, spaceId, webhookId, webhook).blockingFirst();
  }

  /**
   * Get more information about a specific webhook.
   *
   * @param webhook webhook to be asked for more detail.
   * @return A detailed object for the given webhook.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if webhook is null.
   * @see CMAWebhookCall
   */
  public CMAArray<CMAWebhookCall> calls(CMAWebhook webhook) {
    final String spaceId = getSpaceIdOrThrow(webhook, "webhook");
    final String webhookId = getResourceIdOrThrow(webhook, "webhook");

    return service.calls(spaceId, webhookId).blockingFirst();
  }

  /**
   * Get more information about one specific call to one specific webhook, hosted by one specific
   * space.
   *
   * @param call A call to be get more information about.
   * @return A Call Detail to be used to gather more information about this call.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if webhook is null.
   * @throws IllegalArgumentException if callId is null.
   */
  public CMAWebhookCallDetail callDetails(CMAWebhookCall call) {
    final String spaceId = getSpaceIdOrThrow(call, "call");
    final String callId = getResourceIdOrThrow(call, "call");
    assertNotNull(call.getSystem().getCreatedBy().getId(), "webhook.sys.createdBy");
    final String webhookId = call.getSystem().getCreatedBy().getId();

    return service.callDetails(spaceId, webhookId, callId).blockingFirst();
  }

  /**
   * Return a general understanding of the health of the webhook.
   *
   * @param webhook Which webhook should be asked for its health?
   * @return A health indicator summarizing healthy/total calls to the Webhook.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if webhook is null.
   */
  public CMAWebhookHealth health(CMAWebhook webhook) {
    final String spaceId = getSpaceIdOrThrow(webhook, "webhook");
    final String webhookId = getResourceIdOrThrow(webhook, "webhook");

    return service.health(spaceId, webhookId).blockingFirst();
  }

  /**
   * Async module.
   * <p>
   * Use {@link ModuleWebhooks#async()} to retrieve this class, to be able to do asynchronous
   * requests to Contentful.
   */
  public final class Async {
    /**
     * Asynchronous variant of {@link ModuleWebhooks#create(String, CMAWebhook)}
     *
     * @param webhook  data to be used for creation.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if configured space id is null.
     * @throws IllegalArgumentException        if webhook is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAWebhook> create(
        final CMAWebhook webhook,
        CMACallback<CMAWebhook> callback) {
      return defer(new RxExtensions.DefFunc<CMAWebhook>() {
        @Override CMAWebhook method() {
          return ModuleWebhooks.this.create(webhook);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#create(String, CMAWebhook)}
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId  id of the space to be used.
     * @param webhook  data to be used for creation.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if webhook is null.
     */
    public CMACallback<CMAWebhook> create(
        final String spaceId,
        final CMAWebhook webhook,
        CMACallback<CMAWebhook> callback) {
      return defer(new RxExtensions.DefFunc<CMAWebhook>() {
        @Override CMAWebhook method() {
          return ModuleWebhooks.this.create(spaceId, webhook);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#delete(CMAWebhook)}
     *
     * @param webhook  webhook to be deleted.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if webhookId is null.
     */
    public CMACallback<Integer> delete(
        final CMAWebhook webhook,
        CMACallback<Integer> callback) {
      return defer(new RxExtensions.DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleWebhooks.this.delete(webhook);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#fetchAll()}
     *
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if spaceId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAArray<CMAWebhook>> fetchAll(CMACallback<CMAArray<CMAWebhook>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAWebhook>>() {
        @Override CMAArray<CMAWebhook> method() {
          return ModuleWebhooks.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#fetchAll(String)}
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId  id of the space to be used.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAWebhook>> fetchAll(
        final String spaceId,
        CMACallback<CMAArray<CMAWebhook>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAWebhook>>() {
        @Override CMAArray<CMAWebhook> method() {
          return ModuleWebhooks.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#fetchAll(Map)}
     *
     * @param query    description map of which webhooks to be returned.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAArray<CMAWebhook>> fetchAll(
        final Map<String, String> query,
        CMACallback<CMAArray<CMAWebhook>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAWebhook>>() {
        @Override CMAArray<CMAWebhook> method() {
          return ModuleWebhooks.this.fetchAll(query);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#fetchAll(String, Map)}
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId  id of the space to be used.
     * @param query    description map of which webhooks to be returned.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAWebhook>> fetchAll(
        final String spaceId,
        final Map<String, String> query,
        CMACallback<CMAArray<CMAWebhook>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAWebhook>>() {
        @Override CMAArray<CMAWebhook> method() {
          return ModuleWebhooks.this.fetchAll(spaceId, query);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#fetchOne(String)}
     *
     * @param webhookId id of the webhook to be retrieved.
     * @param callback  the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if spaceId is null.
     * @throws IllegalArgumentException        if webhookId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAWebhook> fetchOne(
        final String webhookId,
        CMACallback<CMAWebhook> callback) {
      return defer(new RxExtensions.DefFunc<CMAWebhook>() {
        @Override CMAWebhook method() {
          return ModuleWebhooks.this.fetchOne(webhookId);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#fetchOne(String, String)}
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId   id of the space to be used.
     * @param webhookId id of the webhook to be retrieved.
     * @param callback  the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if webhookId is null.
     */
    public CMACallback<CMAWebhook> fetchOne(
        final String spaceId,
        final String webhookId,
        CMACallback<CMAWebhook> callback) {
      return defer(new RxExtensions.DefFunc<CMAWebhook>() {
        @Override CMAWebhook method() {
          return ModuleWebhooks.this.fetchOne(spaceId, webhookId);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#update(CMAWebhook)}
     *
     * @param webhook  data to be used for update.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException if webhook is null.
     * @throws IllegalArgumentException if webhookId is null.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if version is null.
     */
    public CMACallback<CMAWebhook> update(final CMAWebhook webhook,
                                          CMACallback<CMAWebhook> callback) {
      return defer(new RxExtensions.DefFunc<CMAWebhook>() {
        @Override CMAWebhook method() {
          return ModuleWebhooks.this.update(webhook);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#calls(CMAWebhook)}
     *
     * @param webhook  to be used to retrieve calls from.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if webhook is null.
     */
    public CMACallback<CMAArray<CMAWebhookCall>> calls(
        final CMAWebhook webhook,
        CMACallback<CMAArray<CMAWebhookCall>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAWebhookCall>>() {
        @Override CMAArray<CMAWebhookCall> method() {
          return ModuleWebhooks.this.calls(webhook);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#callDetails(CMAWebhookCall)}
     *
     * @param call     call to get more details about.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if webhook is null.
     * @throws IllegalArgumentException if callId is null.
     */
    public CMACallback<CMAWebhookCallDetail> callDetails(
        final CMAWebhookCall call,
        CMACallback<CMAWebhookCallDetail> callback) {
      return defer(new RxExtensions.DefFunc<CMAWebhookCallDetail>() {
        @Override CMAWebhookCallDetail method() {
          return ModuleWebhooks.this.callDetails(call);
        }
      }, callback);
    }

    /**
     * Asynchronous variant of {@link ModuleWebhooks#health(CMAWebhook)}
     *
     * @param webhook  webhook to be used for healthy check.
     * @param callback the callback to be called once finished.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if webhook is null.
     */
    public CMACallback<CMAWebhookHealth> health(final CMAWebhook webhook,
                                                CMACallback<CMAWebhookHealth> callback) {
      return defer(new RxExtensions.DefFunc<CMAWebhookHealth>() {
        @Override CMAWebhookHealth method() {
          return ModuleWebhooks.this.health(webhook);
        }
      }, callback);
    }
  }
}
