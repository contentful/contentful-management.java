package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMALocale;
import com.contentful.java.cma.model.CMASystem;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This module contains all the locale options available to this SDK.
 */
public class ModuleLocales extends AbsModule<ServiceLocales> {
  private final Async async;

  /**
   * Create a new locale module.
   *
   * @param retrofit         the retrofit instance to be used to create the service.
   * @param callbackExecutor to tell on which thread it should run.
   */
  public ModuleLocales(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    async = new Async();
  }

  /**
   * Initialize retrofit with the current service.
   *
   * @param retrofit the instance to use the service with.
   * @return a populated instance of the service to be used.
   */
  @Override protected ServiceLocales createService(Retrofit retrofit) {
    return retrofit.create(ServiceLocales.class);
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
   * Fetch all locales of this space.
   *
   * @param spaceId the space identifier identifying the space.
   * @return the array of locales.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMALocale> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId).blockingFirst();
  }

  /**
   * Fetch specific locales of this space.
   *
   * @param spaceId the space identifier identifying the space.
   * @param query   the search criteria to search for.
   * @return the array of locales.
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMALocale> fetchAll(String spaceId, Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    if (query == null) {
      return service.fetchAll(spaceId).blockingFirst();
    } else {
      return service.fetchAll(spaceId, query).blockingFirst();
    }
  }

  /**
   * Fetches one locale by its id from Contentful.
   *
   * @param spaceId  the space this locale is hosted by.
   * @param localeId the id of the locale to be found.
   * @return null if no locale was found, otherwise the found locale.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if locale id is null.
   */
  public CMALocale fetchOne(String spaceId, String localeId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(localeId, "localeId");

    return service.fetchOne(spaceId, localeId).blockingFirst();
  }

  /**
   * Create a new locale.
   *
   * @param spaceId the space id to host the locale.
   * @param locale  the new locale to be created.
   * @return the newly created locale.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if locale is null.
   */
  public CMALocale create(String spaceId, CMALocale locale) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(locale, "locale");

    final CMASystem sys = locale.getSystem();
    locale.setSystem(null);

    try {
      return service.create(spaceId, locale).blockingFirst();
    } finally {
      locale.setSystem(sys);
    }
  }

  /**
   * Update the given locale instance on Contentful.
   * <p>
   * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
   * get an exception thrown.
   *
   * @param locale the locale fetched from Contentful, updated by caller, to be updated.
   * @return the updated locale.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if locale is null.
   * @throws IllegalArgumentException if locale id is null.
   * @throws IllegalArgumentException if locale does not have a version attached.
   */
  public CMALocale update(CMALocale locale) {
    assertNotNull(locale, "locale");

    final String id = getResourceIdOrThrow(locale, "locale");
    final String spaceId = getSpaceIdOrThrow(locale, "locale");
    final Integer version = getVersionOrThrow(locale, "update");

    final CMASystem sys = locale.getSystem();
    locale.setSystem(null);

    try {
      return service.update(spaceId, id, locale, version).blockingFirst();
    } finally {
      locale.setSystem(sys);
    }
  }

  /**
   * Delete the given locale instance.
   * <p>
   * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
   * get an exception thrown.
   *
   * @param spaceId the id of the space to be used.
   * @param locale  the locale fetched from Contentful, updated by caller, to be deleted.
   * @return the code of the response (200 means success).
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if locale id is null.
   */
  public int delete(String spaceId, CMALocale locale) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(locale, "locale");

    final String id = getResourceIdOrThrow(locale, "locale");

    final CMASystem sys = locale.getSystem();
    locale.setSystem(null);

    try {
      final Response<Void> response = service.delete(spaceId, id).blockingFirst();
      return response.code();
    } finally {
      locale.setSystem(sys);
    }
  }

  /**
   * Handler for asynchronous requests.
   */
  public final class Async {
    /**
     * Fetch all locales of this space, asynchronously.
     *
     * @param spaceId  the space identifier identifying the space.
     * @param callback a callback to be called, once the results are present.
     * @return a callback for the array fetched.
     * @throws IllegalArgumentException if spaceId is null.
     * @see ModuleLocales#fetchAll(String)
     */
    public CMACallback<CMAArray<CMALocale>> fetchAll(
        final String spaceId,
        final CMACallback<CMAArray<CMALocale>> callback) {
      return defer(new DefFunc<CMAArray<CMALocale>>() {
        @Override CMAArray<CMALocale> method() {
          return ModuleLocales.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch specific locales of this space.
     *
     * @param spaceId  the space identifier identifying the space.
     * @param query    the search criteria to search for.
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @see ModuleLocales#fetchAll(String)
     */
    public CMACallback<CMAArray<CMALocale>> fetchAll(
        final String spaceId,
        final Map<String, String> query,
        final CMACallback<CMAArray<CMALocale>> callback) {
      return defer(new DefFunc<CMAArray<CMALocale>>() {
        @Override CMAArray<CMALocale> method() {
          return ModuleLocales.this.fetchAll(spaceId, query);
        }
      }, callback);
    }

    /**
     * Fetches one locale by its id from Contentful asynchronously.
     *
     * @param spaceId  the space this locale is hosted by.
     * @param localeId the id of the locale to be found.
     * @param callback a callback to be called, once the results are present.
     * @return a callback handling the response.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if locale id is null.
     * @see ModuleLocales#fetchOne(String, String)
     */
    public CMACallback<CMALocale> fetchOne(
        final String spaceId,
        final String localeId,
        final CMACallback<CMALocale> callback) {
      return defer(new DefFunc<CMALocale>() {
        @Override CMALocale method() {
          return ModuleLocales.this.fetchOne(
              spaceId, localeId
          );
        }
      }, callback);
    }

    /**
     * Asynchronously create a new locale.
     *
     * @param spaceId  the space id to host the locale.
     * @param locale   the new locale to be created.
     * @param callback a callback to be called, once the results are present.
     * @return a callback for the responses.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if locale is null.
     * @see ModuleLocales#create(String, CMALocale)
     */
    public CMACallback<CMALocale> create(
        final String spaceId,
        final CMALocale locale,
        final CMACallback<CMALocale> callback) {
      return defer(new DefFunc<CMALocale>() {
        @Override CMALocale method() {
          return ModuleLocales.this.create(
              spaceId, locale
          );
        }
      }, callback);
    }

    /**
     * Update the given locale instance on Contentful, asynchronously.
     * <p>
     * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
     * get an exception thrown.
     *
     * @param locale   the locale fetched from Contentful, updated by caller, to be updated.
     * @param callback a callback to be called, once the results are present.
     * @return the updated locale callback.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if locale is null.
     * @throws IllegalArgumentException if locale id is null.
     * @throws IllegalArgumentException if locale does not have a version attached.
     * @see ModuleLocales#update(CMALocale)
     */
    public CMACallback<CMALocale> update(
        final CMALocale locale,
        final CMACallback<CMALocale> callback) {
      return defer(new DefFunc<CMALocale>() {
        @Override CMALocale method() {
          return ModuleLocales.this.update(
              locale
          );
        }
      }, callback);
    }

    /**
     * Delete the given locale instance asynchronously.
     * <p>
     * Please make sure that the instance provided is fetched from Contentful. Otherwise you will
     * get an exception thrown.
     *
     * @param spaceId  the id of the space to be used.
     * @param locale   the locale fetched from Contentful, updated by caller, to be deleted.
     * @param callback a callback to be called, once the results are present.
     * @return a callback for asynchronous interaction.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if locale id is null.
     * @see ModuleLocales#delete(String, CMALocale)
     */
    public CMACallback<Integer> delete(
        final String spaceId,
        final CMALocale locale,
        final CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleLocales.this.delete(
              spaceId, locale
          );
        }
      }, callback);
    }
  }
}
