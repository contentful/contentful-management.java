package com.contentful.java.cma.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * CMAUiExtensionParameters
 * <p>
 * This class will be used to represent a UI extension parameters.
 */
public class CMAUiExtensionParameters {

  @SerializedName("installation")
  private List<CMAUiExtensionParameter> installationParameters = new ArrayList<>();

  @SerializedName("instance")
  private List<CMAUiExtensionParameter> instanceParameters = new ArrayList<>();

  /**
   * @return a list of installation parameters.
   */
  public List<CMAUiExtensionParameter> getInstallation() {
    return installationParameters;
  }

  /**
   * Add an installation parameter that are set when extensions are created/updated
   * in a space.
   *
   * @param parameter
   * @return this {@code CMAUiExtensionParameters} instance.
   */
  public CMAUiExtensionParameters addInstallationParameters(CMAUiExtensionParameter parameter) {
    installationParameters.add(parameter);
    return this;
  }

  /**
   * @return a list of instance parameters.
   */
  public List<CMAUiExtensionParameter> getInstance() {
    return instanceParameters;
  }

  /**
   * Add an instance parameter that are set when a space member with access to the
   * Content Model assigns an extension to a field of a content type.
   *
   * @param parameter.
   * @return this {@code CMAUiExtensionParameters} instance.
   */
  public CMAUiExtensionParameters addInstanceParameters(CMAUiExtensionParameter parameter) {
    instanceParameters.add(parameter);
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAUiExtensionParameter { "
      + "installationParameters = " + getInstallation() + ", "
      + "instanceParameters = " + getInstance() + " "
      + "}";
  }
}
