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

/**
 * Represents a resource of type Entry.
 */
public class CMAEntry extends StatefulResource {
  // Map of fields
  LinkedHashMap<String, LinkedHashMap<String, Object>> fields;

  /**
   * Sets the ID for this Entry.
   * Returns this {@code CMAEntry} instance
   */
  @Override public CMAEntry setId(String id) {
    return (CMAEntry) super.setId(id);
  }

  /**
   * Creates a new field with the given {@code value}.
   * If a field named {@code key} already exists it will be replaced.
   *
   * @param key field key
   * @param value value
   * @param locale locale
   * @return this {@code CMAEntry}
   */
  public CMAEntry setField(String key, Object value, String locale) {
    if (fields == null) {
      fields = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
    }

    LinkedHashMap<String, Object> field = fields.get(key);
    if (field == null) {
      field = new LinkedHashMap<String, Object>();
    }

    field.put(locale, value);
    fields.put(key, field);
    return this;
  }

  /**
   * Returns a map of fields for this Entry.
   */
  public LinkedHashMap<String, LinkedHashMap<String, Object>> getFields() {
    return fields;
  }

  /**
   * Sets a map of fields for this Entry.
   *
   * @param fields
   * @return this {@code CMAEntry} instance
   */
  public CMAEntry setFields(LinkedHashMap<String, LinkedHashMap<String, Object>> fields) {
    this.fields = fields;
    return this;
  }
}
