package com.contentful.java.cma.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor to add authorization header to requests
 */
public class ContentTypeInterceptor implements Interceptor {
  public static final String HEADER_NAME = "Content-Type";
  private final String contentType;

  /**
   * Create Header interceptor, saving parameters.
   *
   * @param contentType type header to be send with all of the requests
   */
  public ContentTypeInterceptor(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Method called by framework, to enrich current request chain with the header information requested.
   *
   * @param chain the execution chain for the request.
   * @return the response received
   * @throws IOException
   */
  @Override public Response intercept(Chain chain) throws IOException {
    final Request request = chain.request();

    return chain.proceed(request.newBuilder()
        .addHeader(HEADER_NAME, contentType)
        .build());
  }
}
