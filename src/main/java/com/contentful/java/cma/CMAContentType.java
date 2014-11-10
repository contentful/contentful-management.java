package com.contentful.java.cma;

import java.util.ArrayList;
import java.util.List;

/**
 * CMAContentType.
 */
public class CMAContentType extends StatefulResource {
  String name;
  List<CMAField> fields;

  @Override public CMAContentType setId(String id) {
    return (CMAContentType) super.setId(id);
  }

  public CMAContentType addField(CMAField field) {
    if (fields == null) {
      fields = new ArrayList<CMAField>();
    }

    fields.add(field);
    return this;
  }

  public CMAContentType setName(String name) {
    this.name = name;
    return this;
  }
}
