package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMALocale;
import com.contentful.java.cma.model.CMASystem;

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
  public ModuleLocales(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId) {
    super(retrofit, callbackExecutor, spaceId, environmentId);
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
   * Fetch all locales of the configured space.
   *
   * @return the array of locales.
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMAArray<CMALocale> fetchAll() {
    return fetchAll(spaceId, environmentId);
  }

  /**
   * Fetch all locales of the given space and environment.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId       the space identifier, identifying the space.
   * @param environmentId the environment identifier, identifying the environment.
   * @return the array of locales.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if environmentId is null.
   */
  public CMAArray<CMALocale> fetchAll(String spaceId, String environmentId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");

    return service.fetchAll(spaceId, environmentId).blockingFirst();
  }

  /**
   * Fetches one locale by its id using the configured space and environment.
   *
   * @param localeId the id of the locale to be found.
   * @return null if no locale was found, otherwise the found locale.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if locale id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMALocale fetchOne(String localeId) {
    return fetchOne(spaceId, environmentId, localeId);
  }

  /**
   * Fetches one locale by its id from the given space and environment.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId       the space this environment is hosted in.
   * @param environmentId the environment this locale is hosted in.
   * @param localeId      the id of the locale to be found.
   * @return null if no locale was found, otherwise the found locale.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if environment id is null.
   * @throws IllegalArgumentException if locale id is null.
   */
  public CMALocale fetchOne(String spaceId, String environmentId, String localeId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(localeId, "localeId");

    return service.fetchOne(spaceId, environmentId, localeId).blockingFirst();
  }

  /**
   * Create a new locale in the configured space and environment.
   *
   * @param locale the new locale to be created.
   * @return the newly created locale.
   * @throws IllegalArgumentException if the configured space id is null.
   * @throws IllegalArgumentException if the configured locale is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMALocale create(CMALocale locale) {
    return create(spaceId, environmentId, locale);
  }

  /**
   * Create a new locale in the given space and environment.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId       the space id hosting the environment.
   * @param environmentId the environment id to host the locale.
   * @param locale        the new locale to be created.
   * @return the newly created locale.
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if environment is null.
   * @throws IllegalArgumentException if locale is null.
   */
  public CMALocale create(String spaceId, String environmentId, CMALocale locale) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(locale, "locale");

    final CMASystem sys = locale.getSystem();
    locale.setSystem(null);

    try {
      return service.create(spaceId, environmentId, locale).blockingFirst();
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
    final String environmentId = locale.getEnvironmentId();
    final Integer version = getVersionOrThrow(locale, "update");

    final CMASystem sys = locale.getSystem();
    locale.setSystem(null);

    try {
      return service.update(spaceId, environmentId, id, locale, version).blockingFirst();
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
   * @param locale the locale fetched from Contentful, updated by caller, to be deleted.
   * @return the code of the response (200 means success).
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if locale id is null.
   */
  public int delete(CMALocale locale) {
    final String id = getResourceIdOrThrow(locale, "locale");
    final String spaceId = getSpaceIdOrThrow(locale, "locale");
    final String environmentId = locale.getEnvironmentId();

    final CMASystem sys = locale.getSystem();
    locale.setSystem(null);

    try {
      final Response<Void> response = service.delete(spaceId, environmentId, id).blockingFirst();
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
     * Fetch all locales from the configured space and environment, asynchronously.
     *
     * @param callback a callback to be called, once the results are present.
     * @return a callback for the array fetched.
     * @throws IllegalArgumentException if configured space id is null.
     * @throws IllegalArgumentException if configured environment id is null.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     * @see ModuleLocales#fetchAll()
     */
    public CMACallback<CMAArray<CMALocale>> fetchAll(
        final CMACallback<CMAArray<CMALocale>> callback) {
      return defer(new DefFunc<CMAArray<CMALocale>>() {
        @Override CMAArray<CMALocale> method() {
          return ModuleLocales.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch all locales of the given space and environment, asynchronously.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId       the space identifier identifying the space.
     * @param environmentId the environment identifier identifying the space.
     * @param callback      a callback to be called, once the results are present.
     * @return a callback for the array fetched.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if environment id is null.
     * @see ModuleLocales#fetchAll(String, String)
     */
    public CMACallback<CMAArray<CMALocale>> fetchAll(
        final String spaceId,
        final String environmentId,
        final CMACallback<CMAArray<CMALocale>> callback) {
      return defer(new DefFunc<CMAArray<CMALocale>>() {
        @Override CMAArray<CMALocale> method() {
          return ModuleLocales.this.fetchAll(spaceId, environmentId);
        }
      }, callback);
    }

    /**
     * Fetches one locale by the configured space and environment, asynchronously.
     *
     * @param localeId the id of the locale to be found.
     * @param callback a callback to be called, once the results are present.
     * @return a callback handling the response.
     * @throws IllegalArgumentException if configured space id is null.
     * @throws IllegalArgumentException if configured environment id is null.
     * @throws IllegalArgumentException if locale id is null.
     * @see ModuleLocales#fetchOne(String)
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMALocale> fetchOne(
        final String localeId,
        final CMACallback<CMALocale> callback) {
      return defer(new DefFunc<CMALocale>() {
        @Override CMALocale method() {
          return ModuleLocales.this.fetchOne(localeId);
        }
      }, callback);
    }

    /**
     * Fetches one locale by its id and the given space and environment, asynchronously.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId       the space the given environment is hosted in.
     * @param environmentId the environment this locale is hosted in.
     * @param localeId      the id of the locale to be found.
     * @param callback      a callback to be called, once the results are present.
     * @return a callback handling the response.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if locale id is null.
     * @see ModuleLocales#fetchOne(String, String, String)
     */
    public CMACallback<CMALocale> fetchOne(
        final String spaceId,
        final String environmentId,
        final String localeId,
        final CMACallback<CMALocale> callback) {
      return defer(new DefFunc<CMALocale>() {
        @Override CMALocale method() {
          return ModuleLocales.this.fetchOne(
              spaceId,
              environmentId,
              localeId
          );
        }
      }, callback);
    }

    /**
     * Asynchronously create a new locale in the configured space and environment.
     *
     * @param locale   the new locale to be created.
     * @param callback a callback to be called, once the results are present.
     * @return a callback for the responses.
     * @throws IllegalArgumentException if configured space id is null.
     * @throws IllegalArgumentException if configured environment id is null.
     * @throws IllegalArgumentException if locale is null.
     * @see ModuleLocales#create(CMALocale)
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMALocale> create(
        final CMALocale locale,
        final CMACallback<CMALocale> callback) {
      return defer(new DefFunc<CMALocale>() {
        @Override CMALocale method() {
          return ModuleLocales.this.create(locale);
        }
      }, callback);
    }

    /**
     * Asynchronously create a new locale. Using the given space and environment.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId       the space id hosting the environment.
     * @param environmentId the environment id to host the locale.
     * @param locale        the new locale to be created.
     * @param callback      a callback to be called, once the results are present.
     * @return a callback for the responses.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if locale is null.
     * @see ModuleLocales#create(String, String, CMALocale)
     */
    public CMACallback<CMALocale> create(
        final String spaceId,
        final String environmentId,
        final CMALocale locale,
        final CMACallback<CMALocale> callback) {
      return defer(new DefFunc<CMALocale>() {
        @Override CMALocale method() {
          return ModuleLocales.this.create(
              spaceId,
              environmentId,
              locale
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
     * @param locale   the locale fetched from Contentful, updated by caller, to be deleted.
     * @param callback a callback to be called, once the results are present.
     * @return a callback for asynchronous interaction.
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if locale id is null.
     * @see ModuleLocales#delete(CMALocale)
     */
    public CMACallback<Integer> delete(
        final CMALocale locale,
        final CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleLocales.this.delete(
              locale
          );
        }
      }, callback);
    }
  }
}
