package com.contentful.java.cma.model;

import com.google.gson.annotations.SerializedName;

public enum CMAVisibility {
    @SerializedName("private") privateVisibility,
    @SerializedName("public") publicVisibility,
}
