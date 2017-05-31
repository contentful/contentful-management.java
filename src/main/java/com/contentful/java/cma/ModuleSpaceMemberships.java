package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMASpaceMembership;
import com.contentful.java.cma.model.CMASystem;

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
   * @param retrofit
   * @param callbackExecutor
   */
  public ModuleSpaceMemberships(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
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
   * Fetch all memberships of this space.
   *
   * @param spaceId the space identifier identifying the space.
   * @return the array of memberships.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMASpaceMembership> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId).toBlocking().first();
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

    return service.fetchOne(spaceId, membershipId).toBlocking().first();
  }

  /**
   * Create a new membership.
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
      return service.create(spaceId, membership).toBlocking().first();
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
   * @param spaceId    the id of the space to be used.
   * @param membership the membership fetched from contentful, updated by caller, to be updated.
   * @return the updated membership.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if membership is null.
   * @throws IllegalArgumentException if membership id is null.
   * @throws IllegalArgumentException if membership does not have a version attached.
   */
  public CMASpaceMembership update(String spaceId, CMASpaceMembership membership) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(membership, "membership");

    final String id = getResourceIdOrThrow(membership, "membership");
    final Integer version = getVersionOrThrow(membership, "update");

    final CMASystem sys = membership.getSystem();
    membership.setSystem(null);

    try {
      return service.update(spaceId, id, membership, version).toBlocking().first();
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
   * @param spaceId    the id of the space to be used.
   * @param membership the membership fetched from contentful, updated by caller, to be deleted.
   * @return an empty string on success.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if membership id is null.
   */
  public int delete(String spaceId, CMASpaceMembership membership) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(membership, "membership");

    final String id = getResourceIdOrThrow(membership, "membership");

    final CMASystem sys = membership.getSystem();
    membership.setSystem(null);

    try {
      final Response<Void> response = service.delete(spaceId, id).toBlocking().first();
      return response.code();
    } finally {
      membership.setSystem(sys);
    }
  }

  /**
   * Handler for asynchronous requests.
   */
  public final class Async {
    /**
     * Fetch all memberships of this space, asynchronously.
     *
     * @param spaceId the space identifier identifying the space.
     * @return a callback for the array fetched.
     * @throws IllegalArgumentException if spaceId is null.
     * @see ModuleSpaceMemberships#fetchAll(String)
     */
    public CMACallback<CMAArray<CMASpaceMembership>> fetchAll(
        final String spaceId,
        final CMACallback<CMAArray<CMASpaceMembership>> callback
    ) {
      return defer(new DefFunc<CMAArray<CMASpaceMembership>>() {
        @Override CMAArray<CMASpaceMembership> method() {
          return ModuleSpaceMemberships.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetches one space membership by its id from Contentful asynchronously.
     *
     * @param spaceId      the space this membership is hosted by.
     * @param membershipId the id of the membership to be found.
     * @return a callback handling the response.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if membership id is null.
     * @see ModuleSpaceMemberships#fetchOne(String, String)
     */
    public CMACallback<CMASpaceMembership> fetchOne(
        final String spaceId,
        final String membershipId,
        final CMACallback<CMASpaceMembership> callback
    ) {
      return defer(new DefFunc<CMASpaceMembership>() {
        @Override CMASpaceMembership method() {
          return ModuleSpaceMemberships.this.fetchOne(
              spaceId, membershipId
          );
        }
      }, callback);
    }

    /**
     * Asynchronously create a new membership.
     *
     * @param spaceId    the space id to host the membership.
     * @param membership the new membership to be created.
     * @return a callback for the responses.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if membership is null.
     * @see ModuleSpaceMemberships#create(String, CMASpaceMembership)
     */
    public CMACallback<CMASpaceMembership> create(
        final String spaceId,
        final CMASpaceMembership membership,
        final CMACallback<CMASpaceMembership> callback
    ) {
      return defer(new DefFunc<CMASpaceMembership>() {
        @Override CMASpaceMembership method() {
          return ModuleSpaceMemberships.this.create(
              spaceId, membership
          );
        }
      }, callback);
    }

    /**
     * Update the given membership instance on Contentful, asynchronously.
     * <p>
     * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
     * get an exception thrown.
     *
     * @param spaceId    the id of the space to be used.
     * @param membership the membership fetched from contentful, updated by caller, to be updated.
     * @return the updated membership callback.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if membership is null.
     * @throws IllegalArgumentException if membership id is null.
     * @throws IllegalArgumentException if membership does not have a version attached.
     * @see ModuleSpaceMemberships#update(String, CMASpaceMembership)
     */
    public CMACallback<CMASpaceMembership> update(
        final String spaceId,
        final CMASpaceMembership membership,
        final CMACallback<CMASpaceMembership> callback
    ) {
      return defer(new DefFunc<CMASpaceMembership>() {
        @Override CMASpaceMembership method() {
          return ModuleSpaceMemberships.this.update(
              spaceId, membership
          );
        }
      }, callback);
    }

    /**
     * Delete the given membership instance asynchronously.
     * <p>
     * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
     * get an exception thrown.
     *
     * @param spaceId    the id of the space to be used.
     * @param membership the membership fetched from contentful, updated by caller, to be deleted.
     * @return a callback for asynchronous interaction.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if membership id is null.
     * @see ModuleSpaceMemberships#delete(String, CMASpaceMembership)
     */
    public CMACallback<Integer> delete(
        final String spaceId,
        final CMASpaceMembership membership,
        final CMACallback<Integer> callback
    ) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleSpaceMemberships.this.delete(
              spaceId, membership
          );
        }
      }, callback);
    }
  }
}
