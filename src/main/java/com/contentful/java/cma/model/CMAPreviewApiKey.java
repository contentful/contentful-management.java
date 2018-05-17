package com.contentful.java.cma.model;

/**
 * Class representing api keys created.
 */
public class CMAPreviewApiKey extends CMAResource {
  private String accessToken;

  /**
   * Create a new api key.
   */
  public CMAPreviewApiKey() {
    super(CMAType.PreviewApiKey);
  }

  /**
   * @return readonly api access token for this key.
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAApiKey { " + super.toString() + " "
        + "accessToken = " + getAccessToken() + ", "
        + "}";
  }
}
