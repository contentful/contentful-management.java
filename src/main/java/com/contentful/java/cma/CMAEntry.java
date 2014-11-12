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
