/*
 * Copyright (C) 2021 Contentful GmbH
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

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMABulkAction;
import com.contentful.java.cma.model.CMAPayload;
import retrofit2.Retrofit;

import java.util.concurrent.Executor;

/**
 * Bulk actions Module.
 */
public class ModuleBulkActions extends AbsModule<ServiceBulkActions> {
  final Async async;

  /**
   * Create bulk actions module.
   *
   * @param retrofit                the retrofit instance to be used to create the service.
   * @param callbackExecutor        to tell on which thread it should run.
   * @param spaceId                 the space to be used when not given.
   * @param environmentId           the environment to be used when not given.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModuleBulkActions(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    this.async = new Async();
  }

  @Override
  protected ServiceBulkActions createService(Retrofit retrofit) {
    return retrofit.create(ServiceBulkActions.class);
  }

  /**
   * Fetch a bulk action using the given bulkActionId.
   *
   * @return {@link CMABulkAction} result instance
   * @throws IllegalArgumentException if space id is null.
   */
  public CMABulkAction fetch(String spaceId,
                             String environmentId,
                             String bulkActionId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(bulkActionId, "bulkActionId");

    return service.fetch(spaceId, environmentId, bulkActionId).blockingFirst();
  }

  /**
   * Publish a bulk action.
   *
   * @return {@link CMABulkAction} result instance
   * @throws IllegalArgumentException if spaceId's space id is null.
   * @throws IllegalArgumentException if payload's id is null.
   */
  public CMABulkAction publish(String spaceId,
                                String environmentId,
                               CMAPayload payload) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(payload, "payload");

    return service.publish(spaceId, environmentId, payload).blockingFirst();
  }

  /**
   * Unpublish a bulk action.
   *
   * @return {@link CMABulkAction} result instance
   * @throws IllegalArgumentException if spaceId's space id is null.
   * @throws IllegalArgumentException if payload's id is null.
   */
  public CMABulkAction unpublish(String spaceId,
                                 String environmentId,
                               CMAPayload payload) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(payload, "payload");

    return service.unpublish(spaceId, environmentId, payload).blockingFirst();
  }


  /**
   * Validate a bulk action.
   *
   * @return {@link CMABulkAction} result instance
   * @throws IllegalArgumentException if spaceId's space id is null.
   * @throws IllegalArgumentException if payload's id is null.
   */
  public CMABulkAction validate(String spaceId,
                                String environmentId,
                                 CMAPayload payload) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(payload, "payload");

    return service.validate(spaceId, environmentId, payload).blockingFirst();
  }

  /**
   * @return a module with a set of asynchronous methods.
   */
  public Async async() {
    return async;
  }

  /**
   * Async module.
   */
  public class Async {
    /**
     * Fetch a bulk action in the configured space.
     *
     * @param callback    Callback
     * @return the given CMACallback instance
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMABulkAction> fetch(
            String spaceId,
            String environmentId,
            String bulkActionId,
            CMACallback<CMABulkAction> callback) {
      return defer(new DefFunc<CMABulkAction>() {
        @Override
        CMABulkAction method() {
          return ModuleBulkActions.this.fetch(spaceId,
                  environmentId,
                  bulkActionId);
        }
      }, callback);
    }

    /**
     * Publish a bulk action in the configured space.
     *
     * @param callback    Callback
     * @return the given CMACallback instance
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMABulkAction> publish(
            String spaceId,
            String environmentId,
            CMAPayload payload,
            CMACallback<CMABulkAction> callback) {
      return defer(new DefFunc<CMABulkAction>() {
        @Override
        CMABulkAction method() {
          return ModuleBulkActions.this.publish(spaceId,
                  environmentId,
                  payload);
        }
      }, callback);
    }


    /**
     * UpPublish a bulk action in the configured space.
     *
     * @param callback    Callback
     * @return the given CMACallback instance
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMABulkAction> unpublish(
            String spaceId,
            String environmentId,
            CMAPayload payload,
            CMACallback<CMABulkAction> callback) {
      return defer(new DefFunc<CMABulkAction>() {
        @Override
        CMABulkAction method() {
          return ModuleBulkActions.this.unpublish(spaceId,
                  environmentId,
                  payload);
        }
      }, callback);
    }


    /**
     * Validate a bulk action in the configured space.
     *
     * @param callback    Callback
     * @return the given CMACallback instance
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMABulkAction> validate(
            String spaceId,
            String environmentId,
            CMAPayload payload,
            CMACallback<CMABulkAction> callback) {
      return defer(new DefFunc<CMABulkAction>() {
        @Override
        CMABulkAction method() {
          return ModuleBulkActions.this.validate(spaceId,
                  environmentId,
                  payload);
        }
      }, callback);
    }
  }
}
