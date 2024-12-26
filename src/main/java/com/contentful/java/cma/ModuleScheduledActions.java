/*
 * Copyright (C) 2024 Contentful GmbH
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

import java.util.concurrent.Executor;

import com.contentful.java.cma.model.CMASystem;
import retrofit2.Retrofit;

/**
 * Scheduled Actions Module.
 */
public class ModuleScheduledActions extends AbsModule<ServiceScheduledActions> {
  final Async async;

  /**
   * Create scheduled actions module.
   *
   * @param retrofit                the retrofit instance to be used to create the service.
   * @param callbackExecutor        to tell on which thread it should run.
   * @param spaceId                 the space to be used when not given.
   * @param environmentId           the environment to be used when not given.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModuleScheduledActions(
          Retrofit retrofit,
          Executor callbackExecutor,
          String spaceId,
          String environmentId,
          boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    this.async = new Async();
  }

  @Override protected ServiceScheduledActions createService(Retrofit retrofit) {
    return retrofit.create(ServiceScheduledActions.class);
  }

  /**
   * Fetch all scheduled actions.
   *
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId or environmentId is null.
   */
  public CMAArray<CMAScheduledAction> fetchAll() {
    return fetchAll(spaceId, environmentId);
  }

  /**
   * Fetch all scheduled actions in the given space and environment.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @return {@link CMAArray} of scheduled actions matching the query.
   * @throws IllegalArgumentException if spaceId, entityId, or environmentId is null.
   */
  public CMAArray<CMAScheduledAction> fetchAll(
          String spaceId,
          String environmentId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");

    return service.fetchAll(spaceId, environmentId).blockingFirst();
  }

  /**
   * Fetch a scheduled action by ID.
   *
   * @param scheduledActionId Scheduled action ID
   * @param entityId          Entity ID
   * @return {@link CMAScheduledAction} result instance
   * @throws IllegalArgumentException if spaceId, environmentId, or actionId is null.
   */
  public CMAScheduledAction fetchOne(
          String scheduledActionId,
          String entityId) {
    return fetchOne(spaceId, environmentId, scheduledActionId, entityId);
  }

  /**
   * Fetch a scheduled action by ID from a specific space and environment.
   *
   * @param spaceId           Space ID
   * @param environmentId     Environment ID
   * @param scheduledActionId Scheduled action ID
   * @param entityId          Entity ID
   * @return {@link CMAScheduledAction} result instance
   * @throws IllegalArgumentException if spaceId, environmentId, actionId, or entityId is null.
   */
  public CMAScheduledAction fetchOne(
          String spaceId,
          String environmentId,
          String scheduledActionId,
          String entityId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(scheduledActionId, "scheduledActionId");
    assertNotNull(entityId, "entityId");

    return service.fetchOne(spaceId, scheduledActionId, entityId, environmentId).blockingFirst();
  }

  /**
   * Create a new scheduled action.
   *
   * @return {@link CMAScheduledAction} result instance
   */
  public CMAScheduledAction create(CMAScheduledAction scheduledAction) {
    assertNotNull(scheduledAction, "scheduledAction");
    scheduledAction.setSystem(null);
    final CMASystem system = scheduledAction.getSystem();
    scheduledAction.setSystem(null);
    try {
      return service.create(spaceId, scheduledAction).blockingFirst();
    } finally {
      scheduledAction.setSystem(system);
    }
  }

  /**
   * Update an existing scheduled action.
   *
   * @param scheduledActionId Scheduled action ID
   * @return {@link CMAScheduledAction} result instance
   */
  public CMAScheduledAction update(String scheduledActionId, CMAScheduledAction scheduledAction) {
    assertNotNull(scheduledActionId, "scheduledActionId");
    assertNotNull(scheduledAction, "scheduledAction");
    final Integer version = getVersionOrThrow(scheduledAction, "publish");
    final CMASystem system = scheduledAction.getSystem();
    scheduledAction.setSystem(null);
    try {
      return service.update(version, spaceId, scheduledActionId, scheduledAction).blockingFirst();
    } finally {
      scheduledAction.setSystem(system);
    }
  }

  /**
   * Cancel a scheduled action.
   *
   * @param scheduledActionId Scheduled action ID
   * @return {@link CMAScheduledAction} result instance
   */
  public CMAScheduledAction cancel(String scheduledActionId) {
    assertNotNull(scheduledActionId, "scheduledActionId");

    return service.cancel(spaceId, scheduledActionId, environmentId).blockingFirst();
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
     * Fetch all scheduled actions asynchronously.
     *
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if spaceId or environmentId is null.
     */
    public CMACallback<CMAArray<CMAScheduledAction>> fetchAll(
            CMACallback<CMAArray<CMAScheduledAction>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAScheduledAction>>() {
        @Override CMAArray<CMAScheduledAction> method() {
          return ModuleScheduledActions.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch a scheduled action by ID asynchronously.
     *
     * @param scheduledActionId Scheduled action ID
     * @param entityId          Entity ID
     * @param callback          Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAScheduledAction> fetchOne(
            final String scheduledActionId,
            final String entityId,
            CMACallback<CMAScheduledAction> callback) {
      return defer(new RxExtensions.DefFunc<CMAScheduledAction>() {
        @Override CMAScheduledAction method() {
          return ModuleScheduledActions.this.fetchOne(scheduledActionId, entityId);
        }
      }, callback);
    }

    /**
     * Create a new scheduled action asynchronously.
     *
     * @param callback Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAScheduledAction> create(
            final CMAScheduledAction scheduledAction,
            CMACallback<CMAScheduledAction> callback) {
      return defer(new RxExtensions.DefFunc<CMAScheduledAction>() {
        @Override CMAScheduledAction method() {
          return ModuleScheduledActions.this.create(scheduledAction);
        }
      }, callback);
    }

    /**
     * Update an existing scheduled action asynchronously.
     *
     * @param scheduledActionId Scheduled action ID
     * @param callback          Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAScheduledAction> update(
            final String scheduledActionId,
            final CMAScheduledAction scheduledAction,
            CMACallback<CMAScheduledAction> callback) {
      return defer(new RxExtensions.DefFunc<CMAScheduledAction>() {
        @Override CMAScheduledAction method() {
          return ModuleScheduledActions.this.update(scheduledActionId, scheduledAction);
        }
      }, callback);
    }


    /**
     * Cancel a scheduled action asynchronously.
     *
     * @param scheduledActionId Scheduled action ID
     * @param callback          Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAScheduledAction> cancel(
            final String scheduledActionId,
            CMACallback<CMAScheduledAction> callback) {
      return defer(new RxExtensions.DefFunc<CMAScheduledAction>() {
        @Override CMAScheduledAction method() {
          return ModuleScheduledActions.this.cancel(scheduledActionId);
        }
      }, callback);
    }
  }
}