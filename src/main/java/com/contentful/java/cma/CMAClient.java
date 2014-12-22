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

import com.contentful.java.cma.model.CMAField;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.concurrent.Executor;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;

/**
 * The CMAClient is used to request information from the server. Contrary to the delivery
 * API, a client is not associated with one Space, but with one user.
 */
public class CMAClient {
  // User Agent
  static String sUserAgent;

  // Modules
  final ModuleAssets modAssets;
  final ModuleContentTypes modContentTypes;
  final ModuleEntries modEntries;
  final ModuleSpaces modSpaces;

  // PropertiesReader
  final PropertiesReader propertiesReader;

  // Executors
  Executor callbackExecutor;

  private CMAClient(Builder builder) {
    if (builder.accessToken == null) {
      throw new IllegalArgumentException("No access token was set.");
    }

    // Retrofit RestAdapter
    RestAdapter.Builder restBuilder =
        new RestAdapter.Builder().setEndpoint(Constants.ENDPOINT_CMA)
            .setConverter(new GsonConverter(createGson()))
            .setRequestInterceptor(createInterceptor(builder));

    setEndpoint(builder, restBuilder);
    setClient(builder, restBuilder);
    setLogLevel(builder, restBuilder);
    setCallbackExecutor(builder);
    RestAdapter adapter = restBuilder.build();

    // Modules
    this.modAssets = new ModuleAssets(adapter, callbackExecutor);
    this.modContentTypes = new ModuleContentTypes(adapter, callbackExecutor);
    this.modEntries = new ModuleEntries(adapter, callbackExecutor);
    this.modSpaces = new ModuleSpaces(adapter, callbackExecutor);

    // PropertiesReader
    this.propertiesReader = new PropertiesReader();
  }

  /**
   * Sets the callback executor.
   */
  private void setCallbackExecutor(Builder clientBuilder) {
    if (clientBuilder.callbackExecutor == null) {
      callbackExecutor = Platform.get().callbackExecutor();
    } else {
      callbackExecutor = clientBuilder.callbackExecutor;
    }
  }

  /**
   * Configures log level.
   */
  private void setLogLevel(Builder clientBuilder, RestAdapter.Builder restBuilder) {
    if (clientBuilder.logLevel != null) {
      restBuilder.setLogLevel(clientBuilder.logLevel);
    }
  }

  /**
   * Configures a custom client.
   */
  private void setClient(Builder clientBuilder, RestAdapter.Builder restBuilder) {
    if (clientBuilder.clientProvider != null) {
      restBuilder.setClient(clientBuilder.clientProvider);
    }
  }

  /**
   * Configures CMA endpoint.
   */
  private void setEndpoint(Builder clientBuilder, RestAdapter.Builder restBuilder) {
    if (clientBuilder.endpoint != null) {
      restBuilder.setEndpoint(clientBuilder.endpoint);
    }
  }

  /**
   * Creates and returns a custom {@code Gson} instance.
   */
  static Gson createGson() {
    return new GsonBuilder().registerTypeAdapter(
        CMAField.class, new FieldTypeAdapter()).create();
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
        requestFacade.addHeader("User-Agent", getUserAgent(propertiesReader));
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
   * Sets the value for {@code sUserAgent} from properties (if needed) and returns it.
   */
  String getUserAgent(PropertiesReader reader) {
    if (sUserAgent == null) {
      try {
        String versionName = reader.getField(Constants.PROP_VERSION_NAME);
        sUserAgent = String.format("contentful-management.java/%s", versionName);
      } catch (IOException e) {
        throw new RuntimeException("Unable to retrieve version name.", e);
      }
    }
    return sUserAgent;
  }

  /**
   * Builder.
   */
  public static class Builder {
    String accessToken;
    Client.Provider clientProvider;
    RestAdapter.LogLevel logLevel;
    String endpoint;
    Executor callbackExecutor;

    /**
     * Overrides the default remote URL.
     *
     * @param remoteUrl String representing the remote URL
     * @return this {@link Builder} instance
     */
    public Builder setEndpoint(String remoteUrl) {
      if (remoteUrl == null) {
        throw new IllegalArgumentException("Cannot call setEndpoint() with null.");
      }
      this.endpoint = remoteUrl;
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
        throw new IllegalArgumentException("Cannot call setAccessToken() with null.");
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
    public Builder setClient(final Client client) {
      if (client == null) {
        throw new IllegalArgumentException("Cannot call setClient() with null.");
      }
      return setClientProvider(new Client.Provider() {
        @Override public Client get() {
          return client;
        }
      });
    }

    /**
     * Sets a custom HTTP client provider.
     *
     * @param clientProvider {@link retrofit.client.Client.Provider} instance
     * @return this {@code Builder} instance
     */
    public Builder setClientProvider(Client.Provider clientProvider) {
      if (clientProvider == null) {
        throw new IllegalArgumentException("Cannot call setClientProvider() with null.");
      }

      this.clientProvider = clientProvider;
      return this;
    }

    /**
     * Sets the executor to use when invoking asynchronous callbacks.
     *
     * @param executor Executor on which any {@link CMACallback} methods will be invoked. This
     * defaults to execute on the main thread for Android projects. For non-Android
     * projects this defaults to the same thread of the HTTP client.
     * @return this {@code Builder} instance
     */
    public Builder setCallbackExecutor(Executor executor) {
      if (executor == null) {
        throw new IllegalArgumentException("Cannot call setCallbackExecutor() with null.");
      }

      this.callbackExecutor = executor;
      return this;
    }

    /**
     * Sets the log level for this client.
     *
     * @param logLevel {@link retrofit.RestAdapter.LogLevel} value
     * @return this {@code Builder} instance
     */
    public Builder setLogLevel(RestAdapter.LogLevel logLevel) {
      if (logLevel == null) {
        throw new IllegalArgumentException("Cannot call setLogLevel() with null.");
      }
      this.logLevel = logLevel;
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
