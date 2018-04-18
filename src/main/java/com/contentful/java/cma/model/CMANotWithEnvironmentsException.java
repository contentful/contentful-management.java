package com.contentful.java.cma.model;

/**
 * Exception defining the behavioural limits of using configured spaces and environments.
 */
public class CMANotWithEnvironmentsException extends RuntimeException {

  /**
   * Please do not use configured environments with specific endpoints.
   * <p>
   * For more information on the differences, please compare
   * {@link com.contentful.java.cma.ModuleApiKeys#fetchAll()} and
   * {@link com.contentful.java.cma.ModuleApiKeys#fetchAll(String)} for example.
   */
  public CMANotWithEnvironmentsException() {
    super("An endpoint that does not support environments was reached.\n\n"
        + "\tIn order to avoid unintentional behaviour, this endpoint got disabled for use with\n"
        + "\tconfigured environments.\n\n"
        + "\tSince you have specified an environment id through Client.Builder.setEnvironmentId,\n"
        + "\tplease create a new CMAClient not using an environment id to use this endpoint, or\n"
        + "\tuse the method variant overwriting the space id and environment id.");
  }
}
