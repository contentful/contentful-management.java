package com.contentful.java.cma.model;

import com.google.gson.annotations.SerializedName;

/**
 * Some of Contentful resources can have visibility property. It can be public | private
 */
public enum CMAVisibility {
  @SerializedName("public") Public,
  @SerializedName("private") Private
}
