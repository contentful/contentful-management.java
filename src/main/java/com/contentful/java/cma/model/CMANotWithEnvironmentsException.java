package com.contentful.java.cma.model;

public class CMANotWithEnvironmentsException extends RuntimeException {
  public CMANotWithEnvironmentsException() {
    super("You reached an endpoint that does not use environments.\n\n"
        + "\tIn order to avoid unintentional behaviour, we disabled this endpoint for "
        + "environments.\n\n"
        + "\tSince you have specified an environment id through Client.Builder.setEnvironmentId, \n"
        + "\tplease create a new CMAClient not using an environment id to use this endpoint.");
  }
}
