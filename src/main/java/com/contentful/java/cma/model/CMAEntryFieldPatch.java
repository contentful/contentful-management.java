package com.contentful.java.cma.model;

public class CMAEntryFieldPatch {
    private final String fieldPath;
    private final Object value;

    CMAEntryFieldPatch(String fieldPath, Object value) {
        this.fieldPath = fieldPath;
        this.value = value;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public Object getValue() {
        return value;
    }
}
