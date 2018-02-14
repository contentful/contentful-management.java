/*
 * Copyright (C) 2018 Contentful GmbH
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

//BEGIN TO LONG CODE LINES

import com.contentful.java.cma.gson.EntrySerializer;
import com.contentful.java.cma.gson.FieldTypeAdapter;
import com.contentful.java.cma.interceptor.AuthorizationHeaderInterceptor;
import com.contentful.java.cma.interceptor.ContentTypeInterceptor;
import com.contentful.java.cma.interceptor.ContentfulUserAgentHeaderInterceptor;
import com.contentful.java.cma.interceptor.ContentfulUserAgentHeaderInterceptor.Section;
import com.contentful.java.cma.interceptor.ContentfulUserAgentHeaderInterceptor.Section.OperatingSystem;
import com.contentful.java.cma.interceptor.ContentfulUserAgentHeaderInterceptor.Section.Version;
import com.contentful.java.cma.interceptor.ErrorInterceptor;
import com.contentful.java.cma.interceptor.LogInterceptor;
import com.contentful.java.cma.interceptor.RateLimitInterceptor;
import com.contentful.java.cma.interceptor.RateLimitsListener;
import com.contentful.java.cma.interceptor.UserAgentHeaderInterceptor;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMAField;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Properties;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.contentful.java.cma.Constants.DEFAULT_CONTENT_TYPE;
import static com.contentful.java.cma.Constants.OCTET_STREAM_CONTENT_TYPE;
import static com.contentful.java.cma.Logger.Level.NONE;
import static com.contentful.java.cma.build.GeneratedBuildParameters.PROJECT_VERSION;
import static com.contentful.java.cma.interceptor.ContentfulUserAgentHeaderInterceptor.Section.Version.parse;
import static com.contentful.java.cma.interceptor.ContentfulUserAgentHeaderInterceptor.Section.os;
import static com.contentful.java.cma.interceptor.ContentfulUserAgentHeaderInterceptor.Section.platform;
import static com.contentful.java.cma.interceptor.ContentfulUserAgentHeaderInterceptor.Section.sdk;
//END TO LONG CODE LINES

/**
 * The CMAClient is used to request information from the server. Contrary to the delivery
 * API, a client is not associated with one Space, but with one user.
 */
public class CMAClient {
  // Gson
  private static Gson gson;

  // Modules
  private final ModuleApiKeys moduleApiKeys;
  private final ModuleAssets moduleAssets;
  private final ModuleContentTypes moduleContentTypes;
  private final ModuleEditorInterfaces moduleEditorInterfaces;
  private final ModuleEntries moduleEntries;
  private final ModuleLocales moduleLocales;
  private final ModuleOrganizations moduleOrganizations;
  private final ModulePersonalAccessTokens modulePersonalAccessTokens;
  private final ModuleRoles moduleRoles;
  private final ModuleSpaceMemberships moduleSpaceMemberships;
  private final ModuleSpaces moduleSpaces;
  private final ModuleUsers moduleUsers;
  private final ModuleWebhooks moduleWebhooks;
  private final ModuleUiExtensions moduleUiExtensions;
  private final ModuleUploads moduleUploads;

  // Executors
  Executor callbackExecutor;

  private CMAClient(Builder cmaBuilder) {
    if (cmaBuilder.accessToken == null) {
      throw new IllegalArgumentException("No access token was set.");
    }

    // Retrofit Retrofit
    Retrofit.Builder retrofitBuilder =
        new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.ENDPOINT_CMA);

    retrofitBuilder = setEndpoint(retrofitBuilder, cmaBuilder.coreEndpoint);
    retrofitBuilder.callFactory(
        cmaBuilder.coreCallFactory == null
            ? cmaBuilder.defaultCoreCallFactoryBuilder().build()
            : cmaBuilder.coreCallFactory
    );

    setCallbackExecutor(cmaBuilder);
    Retrofit retrofit = retrofitBuilder.build();

    // copy settings for upload, and change endpoint and call factory
    retrofitBuilder.baseUrl(Constants.ENDPOINT_UPLOAD);
    retrofitBuilder = setEndpoint(retrofitBuilder, cmaBuilder.uploadEndpoint);
    retrofitBuilder.callFactory(
        cmaBuilder.uploadCallFactory == null
            ? cmaBuilder.defaultUploadCallFactoryBuilder().build()
            : cmaBuilder.uploadCallFactory
    );
    Retrofit uploadRetrofit = retrofitBuilder.build();

    // Modules
    this.moduleApiKeys = new ModuleApiKeys(retrofit, callbackExecutor);
    this.moduleAssets = new ModuleAssets(retrofit, callbackExecutor);
    this.moduleContentTypes = new ModuleContentTypes(retrofit, callbackExecutor);
    this.moduleEditorInterfaces = new ModuleEditorInterfaces(retrofit, callbackExecutor);
    this.moduleEntries = new ModuleEntries(retrofit, callbackExecutor);
    this.moduleLocales = new ModuleLocales(retrofit, callbackExecutor);
    this.moduleOrganizations = new ModuleOrganizations(retrofit, callbackExecutor);
    this.modulePersonalAccessTokens = new ModulePersonalAccessTokens(retrofit, callbackExecutor);
    this.moduleRoles = new ModuleRoles(retrofit, callbackExecutor);
    this.moduleSpaceMemberships = new ModuleSpaceMemberships(retrofit, callbackExecutor);
    this.moduleSpaces = new ModuleSpaces(retrofit, callbackExecutor);
    this.moduleUiExtensions = new ModuleUiExtensions(retrofit, callbackExecutor);
    this.moduleUsers = new ModuleUsers(retrofit, callbackExecutor);
    this.moduleWebhooks = new ModuleWebhooks(retrofit, callbackExecutor);

    this.moduleUploads = new ModuleUploads(uploadRetrofit, callbackExecutor);
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
   * Configures CMA core endpoint.
   */
  private Retrofit.Builder setEndpoint(Retrofit.Builder retrofitBuilder
      , String endpoint) {
    if (endpoint != null) {
      return retrofitBuilder.baseUrl(endpoint);
    }
    return retrofitBuilder;
  }

  /**
   * @return the api keys module.
   */
  public ModuleApiKeys apiKeys() {
    return moduleApiKeys;
  }

  /**
   * @return the Assets module.
   */
  public ModuleAssets assets() {
    return moduleAssets;
  }

  /**
   * @return the Content Types module.
   */
  public ModuleContentTypes contentTypes() {
    return moduleContentTypes;
  }

  /**
   * @return the Editor Interface module.
   */
  public ModuleEditorInterfaces editorInterfaces() {
    return moduleEditorInterfaces;
  }

  /**
   * @return the Entries module.
   */
  public ModuleEntries entries() {
    return moduleEntries;
  }

  /**
   * @return the Organizations module.
   */
  public ModuleOrganizations organizations() {
    return moduleOrganizations;
  }

  /**
   * @return the Personal Access Token module.
   */
  public ModulePersonalAccessTokens personalAccessTokens() {
    return modulePersonalAccessTokens;
  }

  /**
   * @return the Roles module.
   */
  public ModuleRoles roles() {
    return moduleRoles;
  }

  /**
   * @return the Spaces module.
   */
  public ModuleSpaces spaces() {
    return moduleSpaces;
  }

  /**
   * @return the Webhooks module.
   */
  public ModuleWebhooks webhooks() {
    return moduleWebhooks;
  }

  /**
   * @return the Users module.
   */
  public ModuleUsers users() {
    return moduleUsers;
  }

  /**
   * @return the Users module.
   */
  public ModuleUiExtensions uiExtensions() {
    return moduleUiExtensions;
  }

  /**
   * @return the SpaceMembership module.
   */
  public ModuleSpaceMemberships spaceMemberships() {
    return moduleSpaceMemberships;
  }

  /**
   * @return the Upload module.
   */
  public ModuleUploads uploads() {
    return moduleUploads;
  }

  /**
   * @return the Locales module.
   */
  public ModuleLocales locales() {
    return moduleLocales;
  }


  /**
   * Builder.
   */
  public static class Builder {
    String accessToken;
    Call.Factory coreCallFactory;
    Call.Factory uploadCallFactory;
    Logger logger;
    Logger.Level logLevel = NONE;
    String coreEndpoint;
    String uploadEndpoint;
    Section application;
    Section integration;
    Executor callbackExecutor;
    RateLimitsListener rateLimitListener;

    /**
     * Overrides the default remote URL for core modules.
     *
     * @param remoteUrl String representing the remote URL
     * @return this {@link Builder} instance
     * @see #setUploadEndpoint(String)
     */
    public Builder setCoreEndpoint(String remoteUrl) {
      if (remoteUrl == null) {
        throw new IllegalArgumentException("Cannot call setCoreEndpoint() with null.");
      }
      this.coreEndpoint = remoteUrl;
      return this;
    }

    /**
     * Overrides the remote URL for upload module.
     *
     * @param remoteUrl String representing the remote URL
     * @return this {@link Builder} instance
     * @see #setCoreEndpoint(String)
     */
    public Builder setUploadEndpoint(String remoteUrl) {
      if (remoteUrl == null) {
        throw new IllegalArgumentException("Cannot call setUploadEndpoint() with null.");
      }
      this.uploadEndpoint = remoteUrl;
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
     * Sets a custom HTTP call factory for core modules.
     * <p>
     * Please also add a {@link AuthorizationHeaderInterceptor} {@link ContentTypeInterceptor} if
     * you are using a custom call factory to ensure properly setup http headers.
     *
     * @param callFactory {@link okhttp3.Call.Factory} instance
     * @return this {@code Builder} instance
     */
    public Builder setCoreCallFactory(Call.Factory callFactory) {
      if (callFactory == null) {
        throw new IllegalArgumentException("Cannot call setCallFactory() with null.");
      }

      this.coreCallFactory = callFactory;
      return this;
    }

    /**
     * Sets a custom HTTP call factory for the upload module.
     *
     * @param callFactory {@link okhttp3.Call.Factory} instance
     * @return this {@code Builder} instance
     * @see #setCoreCallFactory(Call.Factory) Call.Factory for needed interceptors.
     */
    public Builder setUploadCallFactory(Call.Factory callFactory) {
      if (callFactory == null) {
        throw new IllegalArgumentException("Cannot call setCallFactory() with null.");
      }

      this.uploadCallFactory = callFactory;
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
     * Tell the client which application this is.
     * <p>
     * It might be used for internal tracking of Contentfuls tools.
     *
     * @param name    the name of the app.
     * @param version the version in semver of the app.
     * @return this builder for chaining.
     */
    public Builder setApplication(String name, String version) {
      this.application = Section.app(name, parse(version));
      return this;
    }

    /**
     * Which integration is used.
     *
     * @param name    name of the integration.
     * @param version the version of the integration.
     * @return this builder for chaining.
     */
    public Builder setIntegration(String name, String version) {
      this.integration = Section.integration(name, parse(version));
      return this;
    }

    /**
     * Add a listener to receive information about rate limits.
     *
     * @param listener a listener interface to be informed, as soon as a rate limit header is found.
     * @return this builder for chaining.
     */
    public Builder setRateLimitListener(RateLimitsListener listener) {
      this.rateLimitListener = listener;
      return this;
    }

    /**
     * @return a {@link CMAClient} out of this {@link Builder}.
     */
    public CMAClient build() {
      return new CMAClient(this);
    }

    /**
     * @return default core call factory builder, used by the sdk.
     */
    public OkHttpClient.Builder defaultCoreCallFactoryBuilder() {
      final OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
          .addInterceptor(new AuthorizationHeaderInterceptor(accessToken))
          .addInterceptor(new UserAgentHeaderInterceptor(getUserAgent()))
          .addInterceptor(new ContentfulUserAgentHeaderInterceptor(
              createCustomHeaderSections(application, integration))
          )
          .addInterceptor(new ContentTypeInterceptor(DEFAULT_CONTENT_TYPE))
          .addInterceptor(new ErrorInterceptor());

      if (rateLimitListener != null) {
        okBuilder
            .addInterceptor(
                new RateLimitInterceptor(rateLimitListener)
            );
      }

      return setLogger(okBuilder);
    }

    /**
     * @return default update api call factory builder, used by the sdk.
     */
    public OkHttpClient.Builder defaultUploadCallFactoryBuilder() {
      final OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
          .addInterceptor(new AuthorizationHeaderInterceptor(accessToken))
          .addInterceptor(new UserAgentHeaderInterceptor(getUserAgent()))
          .addInterceptor(new ContentfulUserAgentHeaderInterceptor(
              createCustomHeaderSections(application, integration))
          )
          .addInterceptor(new ContentTypeInterceptor(OCTET_STREAM_CONTENT_TYPE))
          .addInterceptor(new ErrorInterceptor());

      return setLogger(okBuilder);
    }

    private String getUserAgent() {
      return String.format(
          "contentful-management.java/%s",
          PROJECT_VERSION);
    }

    Section[] createCustomHeaderSections(Section application, Section integration) {
      final Properties properties = System.getProperties();

      return new Section[]{
          sdk(
              "contentful-management.java",
              parse(PROJECT_VERSION)
          ),
          platform(
              "java",
              parse(properties.getProperty("java.runtime.version"))
          ),
          os(
              OperatingSystem.parse(properties.getProperty("os.name")),
              Version.parse(properties.getProperty("os.version"))
          ),
          application,
          integration
      };
    }

    private OkHttpClient.Builder setLogger(OkHttpClient.Builder okBuilder) {
      if (logger != null) {
        switch (logLevel) {
          case NONE:
          default:
            break;
          case BASIC:
            return okBuilder.addInterceptor(new LogInterceptor(logger));
          case FULL:
            return okBuilder.addNetworkInterceptor(new LogInterceptor(logger));
        }
      } else {
        if (logLevel != NONE) {
          throw new IllegalArgumentException(
              "Cannot log to a null logger. Please set either no logLevel or set a custom Logger");
        }
      }
      return okBuilder;
    }
  }
}
