package com.contentful.java.cma.interceptor;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import java.io.IOException;

/**
 * Interceptor to add content type header to requests
 */
public class ContentTypeInterceptor implements Interceptor {
  public static final String HEADER_NAME = "Content-Type";
  public static final String PATCH_CONTENT_TYPE = "application/json-patch+json";

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
   * Method called by framework, to enrich current request with the header information requested.
   *
   * @param chain the execution chain for the request.
   * @return the response received
   * @throws IOException if chain had an error
   */
  @Override public Response intercept(Chain chain) throws IOException {
    final Request request = chain.request();

    String requestContentType = isPatch(request) ? PATCH_CONTENT_TYPE : this.contentType;
    final Request.Builder builder = request.newBuilder().addHeader(HEADER_NAME, requestContentType);

    if (request.body() != null) {
      rewriteBodyWithCustomContentType(request, builder, getMediaType(requestContentType));
    }

    final Request contentTypeRequest = builder.build();
    return chain.proceed(contentTypeRequest);
  }

  private void rewriteBodyWithCustomContentType(
          Request request, Request.Builder builder, MediaType mediaType
  )
      throws IOException {
    final Buffer sink = new Buffer();
    request.body().writeTo(sink);

    final byte[] content = sink.readByteArray();
    final RequestBody body = RequestBody.create(mediaType, content);

    if ("POST".equals(request.method())) {
      builder.post(body);
    } else if ("PUT".equals(request.method())) {
      builder.put(body);
    } else if (isPatch(request)) {
      builder.patch(body);
    }
  }

  private MediaType getMediaType(String requestContentType) {
    return MediaType.parse(requestContentType);
  }

  private boolean isPatch(Request request) {
    return "PATCH".equals(request.method());
  }
}
