package com.contentful.java.cma.model;

import java.util.List;

public class CMAEntities  {

    List<CMAResource> items;

    public List<CMAResource> getItems() {
        return items;
    }

    public CMAEntities setItems(List<CMAResource> items) {
        this.items = items;
        return this;
    }
}
