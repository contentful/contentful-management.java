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
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMATag;
import retrofit2.Retrofit;

import java.util.concurrent.Executor;

/**
 * Content tags Module.
 */
public class ModuleTags extends AbsModule<ServiceContentTags> {
  final Async async;

  /**
   * Create tags module.
   *
   * @param retrofit                the retrofit instance to be used to create the service.
   * @param callbackExecutor        to tell on which thread it should run.
   * @param spaceId                 the space to be used when not given.
   * @param environmentId           the environment to be used when not given.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModuleTags(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    this.async = new Async();
  }

  @Override
  protected ServiceContentTags createService(Retrofit retrofit) {
    return retrofit.create(ServiceContentTags.class);
  }

  /**
   * Create an tag using the given tag id.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)}.
   *
   * @return {@link CMATag} result instance
   * @throws IllegalArgumentException if space id is null.
   */
  public CMATag create(String environmentId,
                       String spaceId,
                       String tagId,
                       String name,
                       String visibility) {
    assertNotNull(spaceId, "spaceId");

    final CMATag tag = new CMATag();
    tag.setName(name);
    tag.setId(tagId);
    tag.setVisibility(visibility);
    return service.create(spaceId, environmentId, tagId, tag).blockingFirst();
  }

  /**
   * Delete an tag.
   *
   * @return Integer representing the result (204, or an error code)
   * @throws IllegalArgumentException if environment's space id is null.
   * @throws IllegalArgumentException if tag's id is null.
   */
  public Integer delete(String spaceId,
                        String environmentId,
                        String tagId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(tagId, "tagId");

    return service.delete(
            spaceId,
            environmentId,
            tagId
    ).blockingFirst().code();
  }

  /**
   * Fetch all tags of the configured space.
   *
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if configured space id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAArray<CMATag> fetchAll() {
    return fetchAll(spaceId, environmentId);
  }

  /**
   * Fetch all tag of the given space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)}.
   *
   * @param spaceId space ID
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if environment's space id is null.
   */
  public CMAArray<CMATag> fetchAll(String spaceId, String environmentId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    return service.fetchAll(spaceId, environmentId).blockingFirst();
  }

  /**
   * Fetch a tag with a given {@code environmentId} from the configured space.
   *
   * @param tagId tag ID
   * @return {@link CMATag} result instance
   * @throws IllegalArgumentException if the configured space id is null.
   * @throws IllegalArgumentException if environment's id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMATag fetchOne(String tagId) {
    return fetchOne(spaceId, environmentId, tagId);
  }

  /**
   * Fetch a tag with a given {@code environmentId} and space.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)}.
   *
   * @param spaceId       space ID
   * @param environmentId environment ID
   * @return {@link CMATag} result instance
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if environment's id is null.
   */
  public CMATag fetchOne(String spaceId, String environmentId, String tagId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    return service.fetchOne(spaceId, environmentId, tagId).blockingFirst();
  }

  /**
   * Update a tag.
   *
   * @return {@link CMATag} result instance
   * @throws IllegalArgumentException if tag's name is null.
   * @throws IllegalArgumentException if tag's  id is null.
   */
  public CMATag update(String name, String tagId) {
    assertNotNull(name, "name");
    assertNotNull(tagId, "tagId");

    final CMATag tag = new CMATag();
    tag.setName(name);
    tag.setId(tagId);
    return service.update(
        spaceId,
        environmentId,
        tagId,
        tag
    ).blockingFirst();
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
     * Create an tag in the configured space.
     *
     * @param callback    Callback
     * @return the given CMACallback instance
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMATag> create(
            String spaceId,
            String environmentId,
            String tagId,
            String name,
            String visibility,
            CMACallback<CMATag> callback) {
      return defer(new DefFunc<CMATag>() {
        @Override
        CMATag method() {
          return ModuleTags.this.create(spaceId,
                  environmentId,
                  tagId,
                  name,
                  visibility);
        }
      }, callback);
    }

    /**
     * Delete a tag with given id.
     *
     * @param tagId tagId to be deleted
     * @param callback    Callback
     * @return the given CMACallback instance
     */
    public CMACallback<Integer> delete(String spaceId,
                                       String environmentId,
                                       String tagId,
                                       CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override
        Integer method() {
          return ModuleTags.this.delete(spaceId, environmentId, tagId);
        }
      }, callback);
    }

    /**
     * Fetch all tags
     * <p>
     *
     * @param callback Inform about results on the callback.
     * @return the given {@link CMACallback} instance.
     * @throws IllegalArgumentException if configured space id is null.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAArray<CMATag>> fetchAll(
        CMACallback<CMAArray<CMATag>> callback) {
      return defer(new DefFunc<CMAArray<CMATag>>() {
        @Override
        CMAArray<CMATag> method() {
          return ModuleTags.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch all tags of the given space.
     * <p>
     * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)}.
     *
     * @param spaceId  Id of the space to host tag in
     * @param callback Inform about results on the callback.
     * @return the given {@link CMACallback} instance.
     * @throws IllegalArgumentException if space id is null.
     */
    public CMACallback<CMAArray<CMATag>> fetchAll(
        final String spaceId,
        final String environmentId,
        CMACallback<CMAArray<CMATag>> callback) {
      return defer(new DefFunc<CMAArray<CMATag>>() {
        @Override
        CMAArray<CMATag> method() {
          return ModuleTags.this.fetchAll(spaceId, environmentId);
        }
      }, callback);
    }

    /**
     * Fetch an tag with a given tagId and the configured space.
     *
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if environment's space id is null.
     * @throws IllegalArgumentException if environment's id is null.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMATag> fetchOne(
        final String tagId,
        CMACallback<CMATag> callback) {
      return defer(new DefFunc<CMATag>() {
        @Override
        CMATag method() {
          return ModuleTags.this.fetchOne(tagId);
        }
      }, callback);
    }

    /**
     * Fetch an tag with a given {@code tagId} from the space and environment given.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)}.
     *
     * @param spaceId       Id of the space to host tag in
     * @param environmentId environment ID
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if space id is null.
     * @throws IllegalArgumentException if environment id is null.
     */
    public CMACallback<CMATag> fetchOne(
        final String spaceId,
        final String environmentId,
        final String tagId,
        CMACallback<CMATag> callback) {
      return defer(new DefFunc<CMATag>() {
        @Override
        CMATag method() {
          return ModuleTags.this.fetchOne(spaceId, environmentId, tagId);
        }
      }, callback);
    }

    /**
     * Update a tag.
     *
     * @param callback    Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMATag> update(
        final String name,
        final String tagId,
        CMACallback<CMATag> callback) {
      return defer(new DefFunc<CMATag>() {
        @Override
        CMATag method() {
          return ModuleTags.this.update(name, tagId);
        }
      }, callback);
    }
  }
}
