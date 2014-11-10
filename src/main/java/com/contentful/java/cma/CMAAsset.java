package com.contentful.java.cma;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CMAAsset.
 */
public class CMAAsset extends StatefulResource {
  LinkedHashMap<String, Map<String, Object>> fields;

  @Override public CMAAsset setId(String id) {
    return (CMAAsset) super.setId(id);
  }

  @SuppressWarnings("unchecked")
  public CMAAsset addField(String key, Object value, String locale) {
    if (fields == null) {
      fields = new LinkedHashMap<String, Map<String, Object>>();
    }

    Map<String, Object> field = fields.get(key);
    if (field == null) {
      field = new LinkedHashMap<String, Object>();
    }

    field.put(locale, value);
    fields.put(key, field);
    return this;
  }
}
