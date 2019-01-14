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

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAEnvironment;
import com.contentful.java.cma.model.CMASystem;

import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Environments Module.
 */
public final class ModuleEnvironments extends AbsModule<ServiceEnvironments> {
  final Async async;

  /**
   * Create environments module.
   *
   * @param retrofit                the retrofit instance to be used to create the service.
   * @param callbackExecutor        to tell on which thread it should run.
   * @param spaceId                 the space to be used when not given.
   * @param environmentId           the environment to be used when not given.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModuleEnvironments(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    this.async = new Async();
  }

  @Override protected ServiceEnvironments createService(Retrofit retrofit) {
    return retrofit.create(ServiceEnvironments.class);
  }

  /**
   * Create an environment in the configured space.
   *
   * @param environment CMAEnvironment
   * @return {@link CMAEnvironment} result instance
   * @throws IllegalArgumentException if the configured space id is null.
   * @throws IllegalArgumentException if environment is null.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAEnvironment create(CMAEnvironment environment) {
    return create(spaceId, environment);
  }

  /**
   * Create an environment using the given space id.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)}.
   *
   * @param spaceId     the id of the space to be used
   * @param environment CMAEnvironment
   * @return {@link CMAEnvironment} result instance
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if environment is null.
   */
  public CMAEnvironment create(String spaceId, CMAEnvironment environment) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environment, "environment");
    final String environmentId = environment.getId();

    final CMASystem system = environment.getSystem();
    environment.setSystem(null);

    try {
      if (environmentId == null) {
        return service.create(spaceId, environment).blockingFirst();
      } else {
        return service.create(spaceId, environmentId, environment).blockingFirst();
      }
    } finally {
      environment.setSystem(system);
    }
  }

  /**
   * Delete an environment.
   *
   * @param environment the environment to be deleted
   * @return Integer representing the result (204, or an error code)
   * @throws IllegalArgumentException if environment's space id is null.
   * @throws IllegalArgumentException if environment's id is null.
   * @throws IllegalArgumentException if environment's version is null.
   */
  public Integer delete(CMAEnvironment environment) {
    assertNotNull(environment.getSpaceId(), "spaceId");
    assertNotNull(environment.getId(), "environmentId");

    return service.delete(
        getVersionOrThrow(environment, "version"),
        environment.getSpaceId(),
        environment.getId()
    ).blockingFirst().code();
  }

  /**
   * Fetch all environments of the configured space.
   *
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if configured space id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMAEnvironment> fetchAll() {
    return fetchAll(spaceId);
  }

  /**
   * Fetch all environments of the given space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)}.
   *
   * @param spaceId space ID
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if environment's space id is null.
   */
  public CMAArray<CMAEnvironment> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId).blockingFirst();
  }

  /**
   * Fetch an environment with a given {@code environmentId} from the configured space.
   *
   * @param environmentId environment ID
   * @return {@link CMAEnvironment} result instance
   * @throws IllegalArgumentException if the configured space id is null.
   * @throws IllegalArgumentException if environment's id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAEnvironment fetchOne(String environmentId) {
    return fetchOne(spaceId, environmentId);
  }

  /**
   * Fetch an environment with a given {@code environmentId} and space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)}.
   *
   * @param spaceId       space ID
   * @param environmentId environment ID
   * @return {@link CMAEnvironment} result instance
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if environment's id is null.
   */
  public CMAEnvironment fetchOne(String spaceId, String environmentId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    return service.fetchOne(spaceId, environmentId).blockingFirst();
  }

  /**
   * Update an environment.
   *
   * @param environment environment
   * @return {@link CMAEnvironment} result instance
   * @throws IllegalArgumentException if environment is null.
   * @throws IllegalArgumentException if environment's name is null.
   * @throws IllegalArgumentException if environment's space id is null.
   * @throws IllegalArgumentException if environment's environment id is null.
   * @throws IllegalArgumentException if environment's version is null.
   */
  public CMAEnvironment update(CMAEnvironment environment) {
    assertNotNull(environment, "environment");
    assertNotNull(environment.getName(), "environment.name");

    final String environmentId = getResourceIdOrThrow(environment, "environment id");
    final Integer version = getVersionOrThrow(environment, "update");
    final String spaceId = getSpaceIdOrThrow(environment, "environment");

    final CMASystem system = environment.getSystem();
    environment.setSystem(null);

    try {
      return service.update(
          version,
          spaceId,
          environmentId,
          environment
      ).blockingFirst();
    } finally {
      environment.setSystem(system);
    }
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
  public final class Async {
    /**
     * Create an environment in the configured space.
     *
     * @param environment CMAEnvironment
     * @param callback    Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if the configured space id is null.
     * @throws IllegalArgumentException if environment is null.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAEnvironment> create(
        final CMAEnvironment environment,
        CMACallback<CMAEnvironment> callback) {
      return defer(new DefFunc<CMAEnvironment>() {
        @Override CMAEnvironment method() {
          return ModuleEnvironments.this.create(environment);
        }
      }, callback);
    }

    /**
     * Create an environment using the given space id.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)}.
     *
     * @param spaceId     Id of the space to host environment in
     * @param environment CMAEnvironment
     * @param callback    Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if environment is null.
     */
    public CMACallback<CMAEnvironment> create(
        final String spaceId,
        final CMAEnvironment environment,
        CMACallback<CMAEnvironment> callback) {
      return defer(new DefFunc<CMAEnvironment>() {
        @Override CMAEnvironment method() {
          return ModuleEnvironments.this.create(spaceId, environment);
        }
      }, callback);
    }

    /**
     * Delete an environment.
     *
     * @param environment environment to be deleted
     * @param callback    Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if environment's space id is null.
     * @throws IllegalArgumentException if environment's id is null.
     */
    public CMACallback<Integer> delete(final CMAEnvironment environment,
                                       CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleEnvironments.this.delete(environment);
        }
      }, callback);
    }

    /**
     * Fetch all environments of the configured space.
     * <p>
     * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}.
     *
     * @param callback Inform about results on the callback.
     * @return the given {@link CMACallback} instance.
     * @throws IllegalArgumentException if configured space id is null.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAArray<CMAEnvironment>> fetchAll(
        CMACallback<CMAArray<CMAEnvironment>> callback) {
      return defer(new DefFunc<CMAArray<CMAEnvironment>>() {
        @Override CMAArray<CMAEnvironment> method() {
          return ModuleEnvironments.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch all environments of the given space.
     * <p>
     * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)}.
     *
     * @param spaceId  Id of the space to host environment in
     * @param callback Inform about results on the callback.
     * @return the given {@link CMACallback} instance.
     * @throws IllegalArgumentException if space id is null.
     */
    public CMACallback<CMAArray<CMAEnvironment>> fetchAll(
        final String spaceId,
        CMACallback<CMAArray<CMAEnvironment>> callback) {
      return defer(new DefFunc<CMAArray<CMAEnvironment>>() {
        @Override CMAArray<CMAEnvironment> method() {
          return ModuleEnvironments.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch an environment with a given environmentId and the configured space.
     *
     * @param environmentId environment ID
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if environment's space id is null.
     * @throws IllegalArgumentException if environment's id is null.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMAEnvironment> fetchOne(
        final String environmentId,
        CMACallback<CMAEnvironment> callback) {
      return defer(new DefFunc<CMAEnvironment>() {
        @Override CMAEnvironment method() {
          return ModuleEnvironments.this.fetchOne(environmentId);
        }
      }, callback);
    }

    /**
     * Fetch an environment with a given {@code environmentId} from the space given.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)}.
     *
     * @param spaceId       Id of the space to host environment in
     * @param environmentId environment ID
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if environment id is null.
     */
    public CMACallback<CMAEnvironment> fetchOne(
        final String spaceId,
        final String environmentId,
        CMACallback<CMAEnvironment> callback) {
      return defer(new DefFunc<CMAEnvironment>() {
        @Override CMAEnvironment method() {
          return ModuleEnvironments.this.fetchOne(spaceId, environmentId);
        }
      }, callback);
    }

    /**
     * Update an environment.
     *
     * @param environment environment
     * @param callback    Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if environment is null.
     * @throws IllegalArgumentException if environment's space id is null.
     * @throws IllegalArgumentException if environment's name is null.
     * @throws IllegalArgumentException if environment's environment id is null.
     * @throws IllegalArgumentException if environment's version is null.
     */
    public CMACallback<CMAEnvironment> update(
        final CMAEnvironment environment,
        CMACallback<CMAEnvironment> callback) {
      return defer(new DefFunc<CMAEnvironment>() {
        @Override CMAEnvironment method() {
          return ModuleEnvironments.this.update(environment);
        }
      }, callback);
    }
  }
}
