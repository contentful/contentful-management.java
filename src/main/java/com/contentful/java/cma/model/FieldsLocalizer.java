package com.contentful.java.cma.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldsLocalizer<Localizer> {
  private final String locale;

  private final Map<String, Map<String, Object>> fields;

  FieldsLocalizer(Map<String, Map<String, Object>> fields, String locale) {
    this.fields = fields;
    this.locale = locale;
  }

  @SuppressWarnings("unchecked")
  public Localizer set(String key, Object value) {
    if (!fields.containsKey(key)) {
      fields.put(key, new LinkedHashMap<String, Object>());
    }

    final Map<String, Object> field = fields.get(key);
    field.put(locale, value);

    return (Localizer) this;
  }

  @SuppressWarnings("unchecked")
  public <ValueType> ValueType get(String key) {
    final Map<String, Object> localized = fields.get(locale);
    return (ValueType) localized.get(key);
  }
}
