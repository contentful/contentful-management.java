package com.contentful.java.cma;

/**
 * Custom logger interface, used for logging network traffic.
 */
public interface Logger {
  /**
   * Determine the level of logging
   */
  enum Level {
    /**
     * Do not log anything.
     */
    NONE,

    /**
     * Do basic logging, not all requests will be logged.
     */
    BASIC,

    /**
     * Log all requests.
     */
    FULL
  }

  /**
   * Abstract method to be implemented by client.
   *
   * This method gets called, once there is something to log
   *
   * @param message to be delivered to the logger.
   */
  void log(String message);
}
