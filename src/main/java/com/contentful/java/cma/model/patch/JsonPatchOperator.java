package com.contentful.java.cma.model.patch;

import com.google.gson.annotations.SerializedName;

public enum JsonPatchOperator {
    @SerializedName("add")
    ADD,
    @SerializedName("remove")
    REMOVE
}
