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
 * CMAClient.
 */
public class CMAClient {
  final AssetsModule assetsModule;
  final ContentTypesModule contentTypesModule;
  final EntriesModule entriesModule;
  final SpacesModule spacesModule;

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
    this.assetsModule = new AssetsModule(adapter.create(ServiceAssets.class));
    this.contentTypesModule = new ContentTypesModule(adapter.create(ServiceContentTypes.class));
    this.entriesModule = new EntriesModule(adapter.create(ServiceEntries.class));
    this.spacesModule = new SpacesModule(adapter.create(ServiceSpaces.class));
  }

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
  public AssetsModule assets() {
    return assetsModule;
  }

  /**
   * Returns the Content Types module.
   */
  public ContentTypesModule contentTypes() {
    return contentTypesModule;
  }

  /**
   * Returns the Entries module.
   */
  public EntriesModule entries() {
    return entriesModule;
  }

  /**
   * Returns the Spaces module.
   */
  public SpacesModule spaces() {
    return spacesModule;
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
     * @param accessToken Access token
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
