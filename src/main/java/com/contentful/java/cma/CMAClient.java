/*
 * Copyright (C) 2014 Contentful GmbH
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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;

/**
 * The CMAClient is used to request information from the server. Contrary to the delivery
 * API, a client is not associated with one Space, but with one user.
 */
public class CMAClient {
  // Modules
  final ModuleAssets modAssets;
  final ModuleContentTypes modContentTypes;
  final ModuleEntries modEntries;
  final ModuleSpaces modSpaces;

  private CMAClient(Builder builder) {
    if (builder.accessToken == null) {
      throw new IllegalArgumentException("No access token was set.");
    }

    // Gson
    Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
      @Override public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(GsonExclude.class) != null;
      }

      @Override public boolean shouldSkipClass(Class<?> aClass) {
        return false;
      }
    }).create();

    // Retrofit RestAdapter
    RestAdapter.Builder restBuilder =
        new RestAdapter.Builder().setEndpoint("https://api.contentful.com")
            .setRequestInterceptor(createInterceptor(builder))
            .setConverter(new GsonConverter(gson))
            .setLogLevel(RestAdapter.LogLevel.FULL);

    // Endpoint
    if (builder.endpoint != null) {
      restBuilder.setEndpoint(builder.endpoint);
    }

    // Client
    if (builder.client != null) {
      restBuilder.setClient(builder.client);
    }

    RestAdapter adapter = restBuilder.build();

    // Modules
    this.modAssets = new ModuleAssets(adapter.create(ServiceAssets.class));
    this.modContentTypes = new ModuleContentTypes(adapter.create(ServiceContentTypes.class));
    this.modEntries = new ModuleEntries(adapter.create(ServiceEntries.class));
    this.modSpaces = new ModuleSpaces(adapter.create(ServiceSpaces.class));
  }

  /**
   * Creates and returns a {@code RequestInterceptor} from the given {@code builder}.
   */
  private RequestInterceptor createInterceptor(Builder builder) {
    final String accessToken = builder.accessToken;
    return new RequestInterceptor() {
      @Override public void intercept(RequestFacade requestFacade) {
        requestFacade.addHeader("Authorization", "Bearer " + accessToken);
        requestFacade.addHeader("Content-Type", "application/vnd.contentful.management.v1+json");
      }
    };
  }

  /**
   * Returns the Assets module.
   */
  public ModuleAssets assets() {
    return modAssets;
  }

  /**
   * Returns the Content Types module.
   */
  public ModuleContentTypes contentTypes() {
    return modContentTypes;
  }

  /**
   * Returns the Entries module.
   */
  public ModuleEntries entries() {
    return modEntries;
  }

  /**
   * Returns the Spaces module.
   */
  public ModuleSpaces spaces() {
    return modSpaces;
  }

  /**
   * Builder.
   */
  public static class Builder {
    String accessToken;
    Client client;
    String endpoint;

    Builder setEndpoint(String endpoint) {
      this.endpoint = endpoint;
      return this;
    }

    /**
     * Sets the access token for this client.
     *
     * @param accessToken access token
     * @return this {@link Builder} instance
     */
    public Builder setAccessToken(String accessToken) {
      if (accessToken == null) {
        throw new IllegalArgumentException("Cannot call setAccessToken() with null value.");
      }
      this.accessToken = accessToken;
      return this;
    }

    /**
     * Sets a custom HTTP client.
     *
     * @param client Client
     * @return this {@link Builder} instance
     */
    public Builder setClient(Client client) {
      if (client == null) {
        throw new IllegalArgumentException("Cannot call setClient() with null value.");
      }
      this.client = client;
      return this;
    }

    /**
     * Returns a {@link CMAClient} out of this {@link Builder}.
     */
    public CMAClient build() {
      return new CMAClient(this);
    }
  }
}
