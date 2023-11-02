package com.contentful.java.cma.model;

import java.util.ArrayList;
import java.util.List;

public class CMAEntryPatch {
    private final List<CMAEntryFieldPatch> fieldUpdates = new ArrayList<>();

    public void add(String fieldPath, Object value) {
        fieldUpdates.add(new CMAEntryFieldPatch(fieldPath, value));
    }

    public List<CMAEntryFieldPatch> getFieldUpdates() {
        return fieldUpdates;
    }
}
