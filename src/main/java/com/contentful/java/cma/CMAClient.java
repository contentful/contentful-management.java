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

import com.contentful.java.cma.interceptor.AuthorizationHeaderInterceptor;
import com.contentful.java.cma.interceptor.ErrorInterceptor;
import com.contentful.java.cma.interceptor.LogInterceptor;
import com.contentful.java.cma.interceptor.UserAgentHeaderInterceptor;
import com.contentful.java.cma.model.CMAAsset;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMAField;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The CMAClient is used to request information from the server. Contrary to the delivery
 * API, a client is not associated with one Space, but with one user.
 */
public class CMAClient {
  // User Agent
  static String sUserAgent;

  // Gson
  private static Gson gson;

  // Modules
  final ModuleAssets modAssets;
  final ModuleContentTypes modContentTypes;
  final ModuleEntries modEntries;
  final ModuleSpaces modSpaces;

  // PropertiesReader
  final PropertiesReader propertiesReader;

  // Executors
  Executor callbackExecutor;

  private CMAClient(Builder cmaBuilder) {
    if (cmaBuilder.accessToken == null) {
      throw new IllegalArgumentException("No access token was set.");
    }

    // PropertiesReader
    this.propertiesReader = new PropertiesReader();

    // Retrofit Retrofit
    Retrofit.Builder retrofitBuilder =
        new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(Constants.ENDPOINT_CMA);

    retrofitBuilder = setEndpoint(cmaBuilder, retrofitBuilder);
    retrofitBuilder = setCallFactory(cmaBuilder, retrofitBuilder);

    setCallbackExecutor(cmaBuilder);
    Retrofit retrofit = retrofitBuilder.build();

    // Modules
    this.modAssets = new ModuleAssets(retrofit, callbackExecutor);
    this.modContentTypes = new ModuleContentTypes(retrofit, callbackExecutor);
    this.modEntries = new ModuleEntries(retrofit, callbackExecutor);
    this.modSpaces = new ModuleSpaces(retrofit, callbackExecutor);
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
   * Configures a custom client.
   */
  private Retrofit.Builder setCallFactory(Builder cmaBuilder, Retrofit.Builder retrofitBuilder) {
    if (cmaBuilder.callFactory == null) {
      OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
          .addInterceptor(new AuthorizationHeaderInterceptor(cmaBuilder.accessToken))
          .addInterceptor(new UserAgentHeaderInterceptor(getUserAgent(propertiesReader)))
          .addInterceptor(new ErrorInterceptor());

      okBuilder = setLogger(okBuilder, cmaBuilder);

      return retrofitBuilder.callFactory(okBuilder.build());
    } else {
      return retrofitBuilder.callFactory(cmaBuilder.callFactory);
    }
  }

  private OkHttpClient.Builder setLogger(OkHttpClient.Builder okBuilder, Builder cmaBuilder) {
    if (cmaBuilder.logger != null) {
      switch (cmaBuilder.logLevel) {
        case NONE:
          break;
        case BASIC:
          return okBuilder.addInterceptor(new LogInterceptor(cmaBuilder.logger));
        case FULL:
          return okBuilder.addNetworkInterceptor(new LogInterceptor(cmaBuilder.logger));
      }
    } else {
      if (cmaBuilder.logLevel != Logger.Level.NONE) {
        throw new IllegalArgumentException("Cannot log to a null logger. Please set either logLevel to None, or do set a Logger");
      }
    }
    return okBuilder;
  }

  /**
   * Configures CMA endpoint.
   */
  private Retrofit.Builder setEndpoint(Builder clientBuilder, Retrofit.Builder retrofitBuilder) {
    if (clientBuilder.endpoint != null) {
      return retrofitBuilder.baseUrl(clientBuilder.endpoint);
    }
    return retrofitBuilder;
  }

  /**
   * Creates and returns a custom {@code Gson} instance.
   */
  static Gson createGson() {
    if (gson == null) {
      gson = new GsonBuilder()
          .registerTypeAdapter(CMAField.class, new FieldTypeAdapter())
          .registerTypeAdapter(CMAEntry.class, new EntrySerializer())
          .create();
    }

    return gson;
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
    Call.Factory callFactory;
    Logger logger;
    Logger.Level logLevel = Logger.Level.NONE;
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
     * Sets a custom HTTP call factory.
     *
     * @param callFactory {@link okhttp3.Call.Factory} instance
     * @return this {@code Builder} instance
     */
    public Builder setCallFactory(Call.Factory callFactory) {
      if (callFactory == null) {
        throw new IllegalArgumentException("Cannot call setCallFactory() with null.");
      }

      this.callFactory = callFactory;
      return this;
    }

    /**
     * Sets the executor to use when invoking asynchronous callbacks.
     *
     * @param executor Executor on which any {@link CMACallback} methods will be invoked. This
     *                 defaults to execute on the main thread for Android projects. For non-Android
     *                 projects this defaults to the same thread of the HTTP client.
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
     * Sets the logger to be used for logging all network requests or all application requests.
     *
     * @param logger {@link Logger}
     * @return this {@code Builder} instance
     */
    public Builder setLogger(Logger logger) {
      if (logger == null) {
        throw new IllegalArgumentException("Do not set a null logger");
      }

      this.logger = logger;
      return this;
    }

    /**
     * Sets the log level for this client.
     *
     * @param logLevel {@link Logger.Level} value
     * @return this {@code Builder} instance
     */
    public Builder setLogLevel(Logger.Level logLevel) {
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
