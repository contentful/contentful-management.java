/*
 * Copyright (C) 2014 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a resource of type Asset.
 */
public class CMAAsset extends StatefulResource {
  // Map of fields
  LinkedHashMap<String, Map<String, Object>> fields;

  /**
   * Sets the ID for this Asset.
   * Returns this {@code CMAAsset} instance
   */
  @Override public CMAAsset setId(String id) {
    return (CMAAsset) super.setId(id);
  }

  /**
   * Creates a new field. If a field with the given key already exists it will be replaced.
   *
   * @param key field key
   * @param value value
   * @param locale locale
   * @return this {@code CMAAsset} instance
   */
  @SuppressWarnings("unchecked")
  public CMAAsset setField(String key, Object value, String locale) {
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

  /**
   * Returns a map of fields for this Asset.
   */
  public LinkedHashMap<String, Map<String, Object>> getFields() {
    return fields;
  }

  /**
   * Sets a map of fields for this Asset.
   *
   * @param fields Map of fields
   * @return this {@code CMAAsset} instance
   */
  public CMAAsset setFields(LinkedHashMap<String, Map<String, Object>> fields) {
    this.fields = fields;
    return this;
  }
}
