package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMARole;
import com.contentful.java.cma.model.CMASystem;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This module contains all the role options available to this SDK.
 */
public class ModuleRoles extends AbsModule<ServiceRoles> {
  private final Async async;

  /**
   * Create a new role module.
   *
   * @param retrofit         the retrofit instance to be used to create the service.
   * @param callbackExecutor to tell on which thread it should run.
   */
  public ModuleRoles(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    async = new Async();
  }

  /**
   * Initialize retrofit with the current service.
   *
   * @param retrofit the instance to use the service with.
   * @return a populated instance of the service to be used.
   */
  @Override protected ServiceRoles createService(Retrofit retrofit) {
    return retrofit.create(ServiceRoles.class);
  }

  /**
   * Use all functionalities of this module asynchronously.
   *
   * @return an asynchronous handling instance.
   */
  public Async async() {
    return async;
  }

  /**
   * Fetch all roles of this space.
   *
   * @param spaceId the space identifier identifying the space.
   * @return the array of roles.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMARole> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId).blockingFirst();
  }

  /**
   * Fetch specific roles of this space.
   *
   * @param spaceId the space identifier identifying the space.
   * @param query   the search criteria to search for.
   * @return the array of roles.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMARole> fetchAll(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    if (query == null) {
      return service.fetchAll(spaceId).blockingFirst();
    } else {
      return service.fetchAll(spaceId, query).blockingFirst();
    }
  }

  /**
   * Fetches one role by its id from Contentful.
   *
   * @param spaceId the space this role is hosted by.
   * @param roleId  the id of the role to be found.
   * @return null if no role was found, otherwise the found role.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if role id is null.
   */
  public CMARole fetchOne(String spaceId, String roleId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(roleId, "roleId");

    return service.fetchOne(spaceId, roleId).blockingFirst();
  }

  /**
   * Create a new role.
   *
   * @param spaceId the space id to host the role.
   * @param role    the new role to be created.
   * @return the newly created role.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if role is null.
   */
  public CMARole create(String spaceId, CMARole role) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(role, "role");

    final CMASystem sys = role.getSystem();
    role.setSystem(null);

    try {
      return service.create(spaceId, role).blockingFirst();
    } finally {
      role.setSystem(sys);
    }
  }

  /**
   * Update the given role instance on Contentful.
   * <p>
   * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
   * get an exception thrown.
   *
   * @param role the role fetched from Contentful, updated by caller, to be updated.
   * @return the updated role.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if role is null.
   * @throws IllegalArgumentException if role id is null.
   * @throws IllegalArgumentException if role does not have a version attached.
   */
  public CMARole update(CMARole role) {
    assertNotNull(role, "role");

    final String id = getResourceIdOrThrow(role, "role");
    final String spaceId = getSpaceIdOrThrow(role, "role");
    final Integer version = getVersionOrThrow(role, "update");

    final CMASystem sys = role.getSystem();
    role.setSystem(null);

    try {
      return service.update(spaceId, id, role, version).blockingFirst();
    } finally {
      role.setSystem(sys);
    }
  }

  /**
   * Delete the given role instance.
   * <p>
   * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
   * get an exception thrown.
   *
   * @param role the role fetched from Contentful, updated by caller, to be deleted.
   * @return the code of the response (200 means success).
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if role id is null.
   */
  public int delete(CMARole role) {
    final String id = getResourceIdOrThrow(role, "role");
    final String spaceId = getSpaceIdOrThrow(role, "role");

    final CMASystem sys = role.getSystem();
    role.setSystem(null);

    try {
      final Response<Void> response = service.delete(spaceId, id).blockingFirst();
      return response.code();
    } finally {
      role.setSystem(sys);
    }
  }

  /**
   * Handler for asynchronous requests.
   */
  public final class Async {
    /**
     * Fetch all roles of this space, asynchronously.
     *
     * @param spaceId  the space identifier identifying the space.
     * @param callback a callback to be called, once the results are present.
     * @return a callback for the array fetched.
     * @throws IllegalArgumentException if spaceId is null.
     * @see ModuleRoles#fetchAll(String)
     */
    public CMACallback<CMAArray<CMARole>> fetchAll(
        final String spaceId,
        final CMACallback<CMAArray<CMARole>> callback) {
      return defer(new DefFunc<CMAArray<CMARole>>() {
        @Override CMAArray<CMARole> method() {
          return ModuleRoles.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch specific roles of this space.
     *
     * @param spaceId  the space identifier identifying the space.
     * @param query    the search criteria to search for.
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @see ModuleRoles#fetchAll(String)
     */
    public CMACallback<CMAArray<CMARole>> fetchAll(
        final String spaceId,
        final Map<String, String> query,
        final CMACallback<CMAArray<CMARole>> callback) {
      return defer(new DefFunc<CMAArray<CMARole>>() {
        @Override CMAArray<CMARole> method() {
          return ModuleRoles.this.fetchAll(spaceId, query);
        }
      }, callback);
    }

    /**
     * Fetches one role by its id from Contentful asynchronously.
     *
     * @param spaceId  the space this role is hosted by.
     * @param roleId   the id of the role to be found.
     * @param callback a callback to be called, once the results are present.
     * @return a callback handling the response.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if role id is null.
     * @see ModuleRoles#fetchOne(String, String)
     */
    public CMACallback<CMARole> fetchOne(
        final String spaceId,
        final String roleId,
        final CMACallback<CMARole> callback) {
      return defer(new DefFunc<CMARole>() {
        @Override CMARole method() {
          return ModuleRoles.this.fetchOne(
              spaceId, roleId
          );
        }
      }, callback);
    }

    /**
     * Asynchronously create a new role.
     *
     * @param spaceId  the space id to host the role.
     * @param role     the new role to be created.
     * @param callback a callback to be called, once the results are present.
     * @return a callback for the responses.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if role is null.
     * @see ModuleRoles#create(String, CMARole)
     */
    public CMACallback<CMARole> create(
        final String spaceId,
        final CMARole role,
        final CMACallback<CMARole> callback) {
      return defer(new DefFunc<CMARole>() {
        @Override CMARole method() {
          return ModuleRoles.this.create(
              spaceId, role
          );
        }
      }, callback);
    }

    /**
     * Update the given role instance on Contentful, asynchronously.
     * <p>
     * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
     * get an exception thrown.
     *
     * @param role     the role fetched from Contentful, updated by caller, to be updated.
     * @param callback a callback to be called, once the results are present.
     * @return the updated role callback.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if role is null.
     * @throws IllegalArgumentException if role id is null.
     * @throws IllegalArgumentException if role does not have a version attached.
     * @see ModuleRoles#update(CMARole)
     */
    public CMACallback<CMARole> update(
        final CMARole role,
        final CMACallback<CMARole> callback) {
      return defer(new DefFunc<CMARole>() {
        @Override CMARole method() {
          return ModuleRoles.this.update(
              role
          );
        }
      }, callback);
    }

    /**
     * Delete the given role instance asynchronously.
     * <p>
     * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
     * get an exception thrown.
     *
     * @param role     the role fetched from Contentful, updated by caller, to be deleted.
     * @param callback a callback to be called, once the results are present.
     * @return a callback for asynchronous interaction.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if role id is null.
     * @see ModuleRoles#delete(CMARole)
     */
    public CMACallback<Integer> delete(
        final CMARole role,
        final CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleRoles.this.delete(
              role
          );
        }
      }, callback);
    }
  }
}
