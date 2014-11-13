package com.contentful.java.cma;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tomxor on 13/11/14.
 */
public class CMALocale extends CMAResource {
  String name;
  String code;
  boolean publish;

  @SerializedName("default")
  boolean isDefault;

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }

  public boolean isPublished() {
    return publish;
  }

  public boolean isDefault() {
    return isDefault;
  }
}
