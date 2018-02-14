package com.contentful.java.cma.model;

import com.contentful.java.cma.model.RateLimits.DefaultParser;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.String.format;

/**
 * This class will represent known Contentful exceptions
 */
public class CMAHttpException extends RuntimeException {
  /**
   * Error body potentially delivered with an error request.
   */
  public static class ErrorBody {
    /**
     * System properties of an error body
     */
    public static class Sys {
      String type;
      String id;

      /**
       * @return the type, which should be `Error`.
       */
      public String getType() {
        return type;
      }

      /**
       * @return the id of the error.
       */
      public String getId() {
        return id;
      }

      /**
       * @return a human readable string, representing the object.
       */
      @Override public String toString() {
        return "Sys { "
            + (getId() != null ? "id = " + getId() + ", " : "")
            + (getType() != null ? "type = " + getType() + " " : "")
            + "}";
      }
    }

    /**
     * Class to describe the error details.
     */
    public static class Details {
      /**
       * Which error did actually happen where?
       */
      public static class Error {
        String name;
        String details;
        String type;
        String filter;
        String value;
        Object path;

        /**
         * @return the name of this error.
         */
        public String getName() {
          return name;
        }

        /**
         * @return a detailed description of the error.
         */
        public String getDetails() {
          return details;
        }

        /**
         * @return the type of this error.
         */
        public String getType() {
          return type;
        }

        /**
         * @return the filter this error produced.
         */
        public String getFilter() {
          return filter;
        }

        /**
         * @return the value triggering this error.
         */
        public String getValue() {
          return value;
        }

        /**
         * @return a path contributing to this error.
         */
        public Object getPath() {
          return path;
        }

        /**
         * @return a human readable string, representing the object.
         */
        @Override public String toString() {
          return "Error { "
              + (getDetails() != null ? "details = " + getDetails() + ", " : "")
              + (getFilter() != null ? "filter = " + getFilter() + ", " : "")
              + (getName() != null ? "name = " + getName() + ", " : "")
              + (getPath() != null ? "path = " + getPath() + ", " : "")
              + (getType() != null ? "type = " + getType() + ", " : "")
              + (getValue() != null ? "value = " + getValue() + " " : "")
              + "}";
        }
      }

      String type;
      String space;
      List<Error> errors;
      List<String> keys;

      /**
       * @return the type of this detail.
       */
      public String getType() {
        return type;
      }

      /**
       * @return the space given to the error.
       */
      public String getSpace() {
        return space;
      }

      /**
       * @return the list of errors encountered
       */
      public List<Error> getErrors() {
        return errors;
      }

      /**
       * @return a list of keys contributing to this error.
       */
      public List<String> getKeys() {
        return keys;
      }

      /**
       * @return a human readable string, representing the object.
       */
      @Override public String toString() {
        return "Details { "
            + (getErrors() != null ? "errors = " + getErrors() + ", " : "")
            + (getSpace() != null ? "space = " + getSpace() + ", " : "")
            + (getKeys() != null ? "keys = " + getKeys() + ", " : "")
            + (getType() != null ? "type = " + getType() + " " : "")
            + "}";
      }
    }

    Sys sys;
    String message;
    String requestId;
    Details details;

    /**
     * @return the sys of this response, containing the error id.
     */
    public Sys getSys() {
      return sys;
    }

    /**
     * @return the message this error contained.
     */
    public String getMessage() {
      return message;
    }

    /**
     * @return the request id used to help the Contentful staff.
     */
    public String getRequestId() {
      return requestId;
    }

    /**
     * @return more information about the error.
     */
    public Details getDetails() {
      return details;
    }

    /**
     * @return a human readable string, representing the object.
     */
    @Override public String toString() {
      return "ErrorBody { "
          + (getDetails() != null ? "details = " + getDetails() + ", " : "")
          + (getMessage() != null ? "message = " + getMessage() + ", " : "")
          + (getRequestId() != null ? "requestId = " + getRequestId() + ", " : "")
          + (getSys() != null ? "sys = " + getSys() + " " : "")
          + "}";
    }
  }

  private final Request request;
  private final Response response;

  private final RateLimits ratelimits;

  private ErrorBody errorBody;

  /**
   * Construct an error response.
   * <p>
   * This constructor will fill the exception with easy accessible values, like
   * {@link #responseCode()}. {@link #responseMessage()}, but also
   * {@link #rateLimitReset()}.
   *
   * @param request  the request issuing the error.
   * @param response the response from the server to this faulty request.
   */
  public CMAHttpException(Request request, Response response) {
    this.request = request;
    this.response = response;

    try {
      final String body = response.body() != null ? response.body().string() : null;
      this.errorBody = new GsonBuilder().create().fromJson(body, ErrorBody.class);
    } catch (IOException e) {
      this.errorBody = null;
    }

    final Map<String, List<String>> headers = response.headers().toMultimap();
    this.ratelimits = new DefaultParser().parse(headers);
  }

  /**
   * Convert exception to human readable form.
   *
   * @return a string representing this exception.
   */
  @Override
  public String toString() {
    if (errorBody == null) {
      return format(
          Locale.getDefault(),
          "FAILED \n\t%s\n\t↳ Header{%s}\n\t%s\n\t↳ Header{%s}",
          request.toString(),
          headersToString(request.headers()),
          response.toString(),
          headersToString(response.headers()));
    } else {
      return format(
          Locale.getDefault(),
          "FAILED %s\n\t%s\n\t↳ Header{%s}\n\t%s\n\t↳ Header{%s}",
          errorBody.toString(),
          request.toString(),
          headersToString(request.headers()),
          response.toString(),
          headersToString(response.headers()));
    }
  }

  /**
   * @return the response code of the request.
   */
  public int responseCode() {
    return response.code();
  }

  /**
   * @return the message the server returned.
   */
  public String responseMessage() {
    return response.message();
  }

  /**
   * @return the hourly rate limit or -1 if header not send
   */
  public int rateLimitHourLimit() {
    return ratelimits.getHourLimit();
  }

  /**
   * @return the number of remaining requests that can be made in the hour or -1 if header not send
   */
  public int rateLimitHourRemaining() {
    return ratelimits.getHourRemaining();
  }

  /**
   * @return the per second rate limit or -1 if header not send
   */
  public int rateLimitSecondLimit() {
    return ratelimits.getSecondLimit();
  }

  /**
   * @return the number of remaining requests that can be made per second or -1 if header not send
   */
  public int rateLimitSecondRemaining() {
    return ratelimits.getSecondRemaining();
  }

  /**
   * @return the number of seconds until the user can make a next request or -1 if header not send
   */
  public int rateLimitReset() {
    return ratelimits.getReset();
  }

  /**
   * @return a modeled error body response.
   */
  public ErrorBody getErrorBody() {
    return errorBody;
  }

  private String headersToString(Headers headers) {
    final StringBuilder builder = new StringBuilder();

    String divider = "";
    for (final String name : headers.names()) {
      final String value = headers.get(name);
      builder.append(divider);
      builder.append(name);
      builder.append(": ");
      builder.append(value);

      if ("".equals(divider)) {
        divider = ", ";
      }
    }

    return builder.toString();
  }
}
