package com.contentful.java.cma.model;

import com.google.gson.annotations.SerializedName;

public enum CMAEnvironmentStatus {
  @SerializedName("ready")
  Ready,
  @SerializedName("queued")
  Queued,
  @SerializedName("creating")
  Creating,
  @SerializedName("failed")
  Failed
}
