package com.contentful.java.cma.interceptor;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This interceptor will only be used for throwing an exception, once the server returns an error.
 */
public class ErrorInterceptor implements Interceptor {

  /**
   * Intercepts chain to check for unsuccessful requests.
   *
   * @param chain provided by the framework to check
   * @return the response if no error occurred
   * @throws IOException will get thrown if response code is unsuccessful
   */
  @Override public Response intercept(Chain chain) throws IOException {
    final Request request = chain.request();
    final Response response = chain.proceed(request);

    if (!response.isSuccessful()) {
      IOException ioException;
      try {
        ioException = createExceptionWithBody(request, response);
      } catch (IOException e) {
        ioException = createException(request, response);
      }

      throw ioException;
    }
    return response;
  }

  private IOException createExceptionWithBody(Request request, Response response) throws IOException {
    return new IOException(
        String.format(
            Locale.getDefault(),
            "FAILED REQUEST: %s\n\t… %s\n\t… Body%s",
            request.toString(),
            response.toString(),
            new String(response.body().bytes()))
    );
  }

  private IOException createException(Request request, Response response) {
    return new IOException(
        String.format(
            Locale.getDefault(),
            "FAILED REQUEST: %s\n\t… %s",
            request.toString(),
            response.toString())
    );
  }
}
