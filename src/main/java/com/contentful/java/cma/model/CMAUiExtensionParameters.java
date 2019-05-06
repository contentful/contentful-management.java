package com.contentful.java.cma.model;

import java.util.ArrayList;

/**
 * Model class to describe an ui extension parameters.
 */
public class CMAUiExtensionParameters {
  private ArrayList<CMAUiExtensionParameter> installation;

  private ArrayList<CMAUiExtensionParameter> instance;

  /**
   * @return a list of installation parameters
   */
  public ArrayList<CMAUiExtensionParameter> getInstallation() {
    return installation;
  }

  /**
   * Add an installation parameter
   *
   * @param parameter
   * return this {@code CMAUiExtensionParameters} instance
   */
  public CMAUiExtensionParameters addInstallation(CMAUiExtensionParameter parameter) {
    if (installation == null) {
      installation = new ArrayList<CMAUiExtensionParameter>();
    }

    installation.add(parameter);
    return this;
  }

  /**
   * @return a list of instance parameters
   */
  public ArrayList<CMAUiExtensionParameter> getInstance() {
    return installation;
  }

  /**
   * Add an installation parameter
   *
   * @param parameter
   * return this {@code CMAUiExtensionParameters} instance
   */
  public CMAUiExtensionParameters addInstance(CMAUiExtensionParameter parameter) {
    if (instance == null) {
      instance = new ArrayList<CMAUiExtensionParameter>();
    }

    instance.add(parameter);
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAUiExtensionParameter { "
      + "installation = " + getInstallation() + ", "
      + "instance = " + getInstance() + " "
      + "}";
  }
}
