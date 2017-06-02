package com.contentful.java.cma.model;

import com.google.gson.annotations.SerializedName;

/**
 * What can the user change? The content structure? The content values? The space?
 */
public class CMAPermissions {
  @SerializedName("ContentModel")
  private Object contentModel;
  @SerializedName("ContentDelivery")
  private Object contentDelivery;
  @SerializedName("Settings")
  private Object settings;

  /**
   * Return what the user can do with the content model aka content type.
   *
   * @return a string of 'all', null or an empty list if not set.
   */
  public Object getContentModel() {
    return contentModel;
  }

  /**
   * Set the permission to change the content model.
   * <p>
   * Currently only a string of 'all' is allowed.
   *
   * @return this instance for chaining.
   */
  public CMAPermissions setContentModel(Object contentModel) {
    this.contentModel = contentModel;
    return this;
  }

  /**
   * @return a string of 'all' if this role is allowed to change the values of entries' fields.
   */
  public Object getContentDelivery() {
    return contentDelivery;
  }

  /**
   * Set the permission of content delivery changes.
   *
   * @param contentDelivery of 'all' to affect all of the content delivery values.
   * @return this instance for chaining.
   * @see #setContentModel(Object)
   */
  public CMAPermissions setContentDelivery(Object contentDelivery) {
    this.contentDelivery = contentDelivery;
    return this;
  }

  /**
   * @return 'all' String if this role is allowed to change the space settings.
   */
  public Object getSettings() {
    return settings;
  }

  /**
   * Update the space settings changing ability of the current role.
   *
   * @param settings an object representing the permissions of changing the settings.
   * @return this instance for chaining.
   */
  public CMAPermissions setSettings(Object settings) {
    this.settings = settings;
    return this;
  }

  /**
   * @return Human readable representation of this instance.
   */
  @Override public String toString() {
    return "CMAPermissions{"
        + "contentModel=" + contentModel
        + ", contentDelivery=" + contentDelivery
        + ", settings=" + settings
        + '}';
  }
}
