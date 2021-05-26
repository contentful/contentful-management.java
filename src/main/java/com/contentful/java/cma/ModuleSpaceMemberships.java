package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMANotWithEnvironmentsException;
import com.contentful.java.cma.model.CMASpaceMembership;
import com.contentful.java.cma.model.CMASystem;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This module contains all the space membership options available to this SDK.
 */
public class ModuleSpaceMemberships extends AbsModule<ServiceSpaceMemberships> {
  private final Async async;

  /**
   * Create a new membership module.
   *
   * @param retrofit                the retrofit instance to be used to create the service.
   * @param callbackExecutor        to tell on which thread it should run.
   * @param spaceId                 the id of the space to be used if not specified in the module
   *                                call.
   * @param environmentId           the id of the environment used, if not specified before.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModuleSpaceMemberships(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    async = new Async();
  }

  /**
   * Initialize retrofit with the current service.
   *
   * @param retrofit the instance to use the service with.
   * @return a populated instance of the service to be used.
   */
  @Override protected ServiceSpaceMemberships createService(Retrofit retrofit) {
    return retrofit.create(ServiceSpaceMemberships.class);
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
   * Fetch all memberships of the configured space.
   *
   * @return the array of memberships.
   * @throws IllegalArgumentException        if configured spaceId is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMASpaceMembership> fetchAll() {
    throwIfEnvironmentIdIsSet();

    return fetchAll(spaceId);
  }

  /**
   * Fetch all memberships of the given space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId the space identifier identifying the space.
   * @return the array of memberships.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMASpaceMembership> fetchAll(String spaceId) {
    return fetchAll(spaceId, null);
  }

  /**
   * Fetch all memberships of the configured space.
   *
   * @param query define which space memberships to return.
   * @return the array of memberships.
   * @throws IllegalArgumentException        if spaceId is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMASpaceMembership> fetchAll(Map<String, String> query) {
    throwIfEnvironmentIdIsSet();
    return fetchAll(spaceId, query);
  }

  /**
   * Fetch all memberships of this space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId the space identifier identifying the space.
   * @param query   define which space memberships to return.
   * @return the array of memberships.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMASpaceMembership> fetchAll(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    if (query == null) {
      return service.fetchAll(spaceId).blockingFirst();
    } else {
      return service.fetchAll(spaceId, query).blockingFirst();
    }
  }

  /**
   * Fetches one space membership by its id from Contentful.
   *
   * @param membershipId the id of the membership to be found.
   * @return null if no membership was found, otherwise the found membership.
   * @throws IllegalArgumentException        if configured space id is null.
   * @throws IllegalArgumentException        if membership id is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMASpaceMembership fetchOne(String membershipId) {
    throwIfEnvironmentIdIsSet();

    return fetchOne(spaceId, membershipId);
  }

  /**
   * Fetches one space membership by its id from Contentful.
   *
   * @param spaceId      the space this membership is hosted by.
   * @param membershipId the id of the membership to be found.
   * @return null if no membership was found, otherwise the found membership.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if membership id is null.
   */
  public CMASpaceMembership fetchOne(String spaceId, String membershipId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(membershipId, "membershipId");

    return service.fetchOne(spaceId, membershipId).blockingFirst();
  }

  /**
   * Create a new membership.
   *
   * @param membership the new membership to be created.
   * @return the newly created membership.
   * @throws IllegalArgumentException        if configured space id is null.
   * @throws IllegalArgumentException        if membership is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMASpaceMembership create(CMASpaceMembership membership) {
    throwIfEnvironmentIdIsSet();

    return create(spaceId, membership);
  }

  /**
   * Create a new membership.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId    the space id to host the membership.
   * @param membership the new membership to be created.
   * @return the newly created membership.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if membership is null.
   */
  public CMASpaceMembership create(String spaceId, CMASpaceMembership membership) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(membership, "membership");

    final CMASystem sys = membership.getSystem();
    membership.setSystem(null);

    try {
      return service.create(spaceId, membership).blockingFirst();
    } finally {
      membership.setSystem(sys);
    }
  }

  /**
   * Update the given membership instance on Contentful.
   * <p>
   * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
   * get an exception thrown.
   *
   * @param membership the membership fetched from Contentful, updated by caller, to be updated.
   * @return the updated membership.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if membership is null.
   * @throws IllegalArgumentException if membership id is null.
   * @throws IllegalArgumentException if membership does not have a version attached.
   */
  public CMASpaceMembership update(CMASpaceMembership membership) {
    assertNotNull(membership, "membership");

    final String id = getResourceIdOrThrow(membership, "membership");
    final String spaceId = getSpaceIdOrThrow(membership, "membership");
    final Integer version = getVersionOrThrow(membership, "update");

    final CMASystem sys = membership.getSystem();
    membership.setSystem(null);

    try {
      return service.update(spaceId, id, membership, version).blockingFirst();
    } finally {
      membership.setSystem(sys);
    }
  }

  /**
   * Delete the given membership instance.
   * <p>
   * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
   * get an exception thrown.
   *
   * @param membership the membership fetched from Contentful, updated by caller, to be deleted.
   * @return the code of the response (204 means success).
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if membership id is null.
   */
  public int delete(CMASpaceMembership membership) {
    final String id = getResourceIdOrThrow(membership, "membership");
    final String spaceId = getSpaceIdOrThrow(membership, "membership");

    final CMASystem sys = membership.getSystem();
    membership.setSystem(null);

    try {
      final Response<Void> response = service.delete(spaceId, id).blockingFirst();
      return response.code();
    } finally {
      membership.setSystem(sys);
    }
  }

  /**
   * Handler for asynchronous requests.
   */
  public class Async {
    /**
     * Fetch all memberships of this space, asynchronously.
     *
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see ModuleSpaceMemberships#fetchAll(String)
     */
    public CMACallback<CMAArray<CMASpaceMembership>> fetchAll(
        final CMACallback<CMAArray<CMASpaceMembership>> callback) {
      return defer(new DefFunc<CMAArray<CMASpaceMembership>>() {
        @Override CMAArray<CMASpaceMembership> method() {
          return ModuleSpaceMemberships.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch all memberships of this space, asynchronously.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId  the space identifier identifying the space.
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @see ModuleSpaceMemberships#fetchAll(String)
     */
    public CMACallback<CMAArray<CMASpaceMembership>> fetchAll(
        final String spaceId,
        final CMACallback<CMAArray<CMASpaceMembership>> callback) {
      return defer(new DefFunc<CMAArray<CMASpaceMembership>>() {
        @Override CMAArray<CMASpaceMembership> method() {
          return ModuleSpaceMemberships.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch all memberships of the configured space, asynchronously.
     *
     * @param query    define which space memberships to return.
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see ModuleSpaceMemberships#fetchAll(String)
     */
    public CMACallback<CMAArray<CMASpaceMembership>> fetchAll(
        final Map<String, String> query,
        final CMACallback<CMAArray<CMASpaceMembership>> callback) {
      return defer(new DefFunc<CMAArray<CMASpaceMembership>>() {
        @Override CMAArray<CMASpaceMembership> method() {
          return ModuleSpaceMemberships.this.fetchAll(query);
        }
      }, callback);
    }

    /**
     * Fetch all memberships of this space, asynchronously.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId  the space identifier identifying the space.
     * @param query    define which space memberships to return.
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @see ModuleSpaceMemberships#fetchAll(String)
     */
    public CMACallback<CMAArray<CMASpaceMembership>> fetchAll(
        final String spaceId,
        final Map<String, String> query,
        final CMACallback<CMAArray<CMASpaceMembership>> callback) {
      return defer(new DefFunc<CMAArray<CMASpaceMembership>>() {
        @Override CMAArray<CMASpaceMembership> method() {
          return ModuleSpaceMemberships.this.fetchAll(spaceId, query);
        }
      }, callback);
    }

    /**
     * Fetches one space membership by its id from Contentful asynchronously.
     *
     * @param membershipId the id of the membership to be found.
     * @param callback     the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if configured space id is null.
     * @throws IllegalArgumentException        if membership id is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see ModuleSpaceMemberships#fetchOne(String, String)
     */
    public CMACallback<CMASpaceMembership> fetchOne(
        final String membershipId,
        final CMACallback<CMASpaceMembership> callback) {
      return defer(new DefFunc<CMASpaceMembership>() {
        @Override CMASpaceMembership method() {
          return ModuleSpaceMemberships.this.fetchOne(membershipId);
        }
      }, callback);
    }

    /**
     * Fetches one space membership by its id from Contentful asynchronously.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId      the space this membership is hosted by.
     * @param membershipId the id of the membership to be found.
     * @param callback     the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if membership id is null.
     * @see ModuleSpaceMemberships#fetchOne(String, String)
     */
    public CMACallback<CMASpaceMembership> fetchOne(
        final String spaceId,
        final String membershipId,
        final CMACallback<CMASpaceMembership> callback) {
      return defer(new DefFunc<CMASpaceMembership>() {
        @Override CMASpaceMembership method() {
          return ModuleSpaceMemberships.this.fetchOne(spaceId, membershipId);
        }
      }, callback);
    }

    /**
     * Asynchronously create a new membership.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId    the space id to host the membership.
     * @param membership the new membership to be created.
     * @param callback   the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if configured space id is null.
     * @throws IllegalArgumentException if membership is null.
     * @see ModuleSpaceMemberships#create(String, CMASpaceMembership)
     */
    public CMACallback<CMASpaceMembership> create(
        final String spaceId,
        final CMASpaceMembership membership,
        final CMACallback<CMASpaceMembership> callback) {
      return defer(new DefFunc<CMASpaceMembership>() {
        @Override CMASpaceMembership method() {
          return ModuleSpaceMemberships.this.create(spaceId, membership);
        }
      }, callback);
    }

    /**
     * Asynchronously create a new membership.
     *
     * @param membership the new membership to be created.
     * @param callback   the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if configured space id is null.
     * @throws IllegalArgumentException        if membership is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see ModuleSpaceMemberships#create(String, CMASpaceMembership)
     */
    public CMACallback<CMASpaceMembership> create(
        final CMASpaceMembership membership,
        final CMACallback<CMASpaceMembership> callback) {
      return defer(new DefFunc<CMASpaceMembership>() {
        @Override CMASpaceMembership method() {
          return ModuleSpaceMemberships.this.create(membership);
        }
      }, callback);
    }

    /**
     * Update the given membership instance on Contentful, asynchronously.
     * <p>
     * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
     * get an exception thrown.
     *
     * @param membership the membership fetched from Contentful, updated by caller, to be updated.
     * @param callback   the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if membership is null.
     * @throws IllegalArgumentException if membership id is null.
     * @throws IllegalArgumentException if membership does not have a version attached.
     * @see ModuleSpaceMemberships#update(CMASpaceMembership)
     */
    public CMACallback<CMASpaceMembership> update(
        final CMASpaceMembership membership,
        final CMACallback<CMASpaceMembership> callback) {
      return defer(new DefFunc<CMASpaceMembership>() {
        @Override CMASpaceMembership method() {
          return ModuleSpaceMemberships.this.update(membership);
        }
      }, callback);
    }

    /**
     * Delete the given membership instance asynchronously.
     * <p>
     * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
     * get an exception thrown.
     *
     * @param membership the membership fetched from Contentful, updated by caller, to be deleted.
     * @param callback   the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if membership id is null.
     * @see ModuleSpaceMemberships#delete(CMASpaceMembership)
     */
    public CMACallback<Integer> delete(
        final CMASpaceMembership membership,
        final CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleSpaceMemberships.this.delete(membership);
        }
      }, callback);
    }
  }
}
