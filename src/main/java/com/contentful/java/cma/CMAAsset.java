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
