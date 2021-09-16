package com.contentful.java.cma.model;

/**
 * CMAConfiguration
 */
public class CMAConfiguration {
  // URL
  String url;

  // Content Type
  String contentType;

  // Enabled
  boolean enabled;

  // Example
  boolean example;

  /**
   * @return the {@code url} attribute of this configuration.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the {@code url} attribute for this configuration.
   *
   * @param url the url to be set
   * @return this {@code CMAConfiguration} instance
   */
  public CMAConfiguration setUrl(String url) {
    this.url = url;
    return this;
  }

  /**
   * @return the {@code contentType} attribute of this configuration.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Sets the {@code contentType} attribute for this configuration.
   *
   * @param contentType the contentType to be set
   * @return this {@code CMAConfiguration} instance
   */
  public CMAConfiguration setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * @return the {@code enabled} attribute of this configuration.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Sets the {@code enabled} attribute value.
   *
   * @param enabled boolean indicating whether or not this configuration is
   *                enabled.
   * @return this {@code CMAConfiguration} instance
   */
  public CMAConfiguration setEnabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * @return the {@code example} attribute of this configuration.
   */
  public boolean isExample() {
    return example;
  }

  /**
   * Sets the {@code example} attribute value.
   *
   * @param example boolean indicating whether or not this configuration is
   *                example.
   * @return this {@code CMAConfiguration} instance
   */
  public CMAConfiguration setExample(boolean example) {
    this.example = example;
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAConfiguration { "
            + "url = " + getUrl() + ", "
            + "contentType = " + getContentType() + ", "
            + "enabled = " + isEnabled() + ", "
            + "example = " + isExample() + " "
            + "}";
  }
}
