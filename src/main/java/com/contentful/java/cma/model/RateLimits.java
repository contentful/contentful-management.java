package com.contentful.java.cma.model;

import java.util.List;
import java.util.Map;

/**
 * Class to summarize the rate limits Contentful returns.
 */
public class RateLimits {
  private int hourLimit;
  private int hourRemaining;
  private int reset;
  private int secondLimit;
  private int secondRemaining;

  /**
   * @return the number of requests remaining per hour in total.
   */
  public int getHourLimit() {
    return hourLimit;
  }

  /**
   * @return the number of requests remaining currently.
   */
  public int getHourRemaining() {
    return hourRemaining;
  }

  /**
   * @return when does the rate limit counter reset?
   */
  public int getReset() {
    return reset;
  }

  /**
   * @return the number of requests remaining per hour in total.
   */
  public int getSecondLimit() {
    return secondLimit;
  }

  /**
   * @return the number of requests remaining currently.
   */
  public int getSecondRemaining() {
    return secondRemaining;
  }

  /**
   * Interface for parsing rate limits.
   */
  public interface Parser {
    String HEADER_HOUR_LIMIT = "X-Contentful-RateLimit-Hour-Limit";
    String HEADER_HOUR_REMAINING = "X-Contentful-RateLimit-Hour-Remaining";
    String HEADER_SECOND_LIMIT = "X-Contentful-RateLimit-Second-Limit";
    String HEADER_SECOND_REMAINING = "X-Contentful-RateLimit-Second-Remaining";
    String HEADER_RESET = "X-Contentful-RateLimit-Reset";

    /**
     * Convert Contentfuls http headers to ratelimits
     *
     * @param headers the header of an http request to be parsed
     * @return a full fledged RateLimits object, filled in with the data available from headers.
     */
    RateLimits parse(Map<String, List<String>> headers);
  }

  /**
   * Default parser for rate limits, comming from HTTP Headers.
   */
  public static class DefaultParser implements Parser {
    /**
     * Analyzes and converts a map of given headers to its rate limits.
     *
     * @param headers map of headers returned in an HTTP response from Contentful.
     * @return a RateLimits object, filled with available rate limit header information.
     */
    @Override public RateLimits parse(Map<String, List<String>> headers) {
      return new Builder()
          .setHourLimit(findLimit(headers, HEADER_HOUR_LIMIT))
          .setHourRemaining(findLimit(headers, HEADER_HOUR_REMAINING))
          .setSecondLimit(findLimit(headers, HEADER_SECOND_LIMIT))
          .setSecondRemaining(findLimit(headers, HEADER_SECOND_REMAINING))
          .setReset(findLimit(headers, HEADER_RESET))
          .build();
    }

    private int findLimit(Map<String, List<String>> headers, String key) {
      if (headers.containsKey(key) && headers.get(key) != null) {
        try {
          return Integer.parseInt(headers.get(key).get(0));
        } catch (NumberFormatException e) {
          return -1;
        }
      } else {
        return 0;
      }
    }
  }

  /**
   * Builder for the rate limits
   */
  static class Builder {
    RateLimits limits;

    /**
     * Create a new builder.
     */
    public Builder() {
      limits = new RateLimits();
    }

    /**
     * set hourLimit for the rate limits
     */
    public Builder setHourLimit(int hourLimit) {
      limits.hourLimit = hourLimit;
      return this;
    }

    /**
     * set hourRemaining for the rate limits
     */
    public Builder setHourRemaining(int hourRemaining) {
      limits.hourRemaining = hourRemaining;
      return this;
    }

    /**
     * set reset for the rate limits
     */
    public Builder setReset(int reset) {
      limits.reset = reset;
      return this;
    }

    /**
     * set secondLimit for the rate limits
     */
    public Builder setSecondLimit(int secondLimit) {
      limits.secondLimit = secondLimit;
      return this;
    }

    /**
     * set secondRemaining for the rate limits
     */
    public Builder setSecondRemaining(int secondRemaining) {
      limits.secondRemaining = secondRemaining;
      return this;
    }

    public RateLimits build() {
      return limits;
    }
  }
}
