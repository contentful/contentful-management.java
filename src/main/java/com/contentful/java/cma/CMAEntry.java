package com.contentful.java.cma;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CMAEntry.
 */
public class CMAEntry extends StatefulResource {
  LinkedHashMap<String, Map<String, String>> fields;

  @Override public CMAEntry setId(String id) {
    return (CMAEntry) super.setId(id);
  }

  @SuppressWarnings("unchecked")
  public CMAEntry addField(String key, String value, String locale) {
    if (fields == null) {
      fields = new LinkedHashMap<String, Map<String, String>>();
    }

    Map<String, String> field = fields.get(key);
    if (field == null) {
      field = new LinkedHashMap<String, String>();
    }

    field.put(locale, value);
    fields.put(key, field);
    return this;
  }
}
