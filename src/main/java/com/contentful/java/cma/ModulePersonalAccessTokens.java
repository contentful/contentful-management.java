/*
 * Copyright (C) 2017 Contentful GmbH
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
import com.contentful.java.cma.model.CMAPersonalAccessToken;
import com.contentful.java.cma.model.CMASystem;

import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Personal Access Token Module.
 */
public final class ModulePersonalAccessTokens extends AbsModule<ServicePersonalAccessTokens> {
  final Async async;

  /**
   * Create this module.
   *
   * @param retrofit         the retrofit instance to be used to create the service.
   * @param callbackExecutor to tell on which thread it should run.
   */
  public ModulePersonalAccessTokens(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServicePersonalAccessTokens createService(Retrofit retrofit) {
    return retrofit.create(ServicePersonalAccessTokens.class);
  }

  /**
   * @return a list of personal access tokens.
   */
  public CMAArray<CMAPersonalAccessToken> fetchAll() {
    return service.fetchAll().toBlocking().first();
  }

  /**
   * @return one personal access token.
   */
  public CMAPersonalAccessToken fetchOne(String tokenId) {
    return service.fetchOne(tokenId).toBlocking().first();
  }

  /**
   * Create a new personal access token.
   * <p>
   * This method is the _only_ time you will see the personal access token value. Please keep
   * it save after this call, because a {@link #fetchAll()} or a {@link #fetchOne(String)} will
   * _not_ return it!
   *
   * @return the just created token, containing the access token string.
   */
  public CMAPersonalAccessToken create(CMAPersonalAccessToken token) {
    final CMASystem sys = token.getSystem();
    token.setSystem(null);

    try {
      return service.create(token).toBlocking().first();
    } finally {
      token.setSystem(sys);
    }
  }

  /**
   * Revoke a delivery api key.
   *
   * @return the just revoked key.
   */
  public CMAPersonalAccessToken revoke(CMAPersonalAccessToken token) {
    return service.revoke(token.getId()).toBlocking().first();
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
     * Fetch all personal access tokens.
     *
     * @return the callback to be informed about success or failure.
     */
    public CMACallback<CMAArray<CMAPersonalAccessToken>> fetchAll(
        CMACallback<CMAArray<CMAPersonalAccessToken>> callback) {
      return defer(new DefFunc<CMAArray<CMAPersonalAccessToken>>() {
        @Override CMAArray<CMAPersonalAccessToken> method() {
          return ModulePersonalAccessTokens.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch only one personal access token.
     *
     * @return the callback to be informed about success or failure.
     */
    public CMACallback<CMAPersonalAccessToken> fetchOne(
        final String tokenId,
        CMACallback<CMAPersonalAccessToken> callback) {
      return defer(new DefFunc<CMAPersonalAccessToken>() {
        @Override CMAPersonalAccessToken method() {
          return ModulePersonalAccessTokens.this.fetchOne(tokenId);
        }
      }, callback);
    }

    /**
     * Create a new personal access token.
     *
     * @return the callback to be informed about success or failure.
     */
    public CMACallback<CMAPersonalAccessToken> create(
        final CMAPersonalAccessToken token,
        CMACallback<CMAPersonalAccessToken> callback) {
      return defer(new DefFunc<CMAPersonalAccessToken>() {
        @Override CMAPersonalAccessToken method() {
          return ModulePersonalAccessTokens.this.create(token);
        }
      }, callback);
    }

    /**
     * Revoke a personal access token.
     *
     * @return the callback to be informed about success or failure.
     */
    public CMACallback<CMAPersonalAccessToken> revoke(
        final CMAPersonalAccessToken token,
        CMACallback<CMAPersonalAccessToken> callback) {
      return defer(new DefFunc<CMAPersonalAccessToken>() {
        @Override CMAPersonalAccessToken method() {
          return ModulePersonalAccessTokens.this.revoke(token);
        }
      }, callback);
    }
  }
}