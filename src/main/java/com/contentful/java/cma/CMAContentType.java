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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a resource of type Content Type.
 */
public class CMAContentType extends StatefulResource {
  // Name
  String name;

  // List of fields
  List<CMAField> fields;

  /**
   * Sets the ID for this Content Type.
   * Returns this {@code CMAContentType} instance
   */
  @Override public CMAContentType setId(String id) {
    return (CMAContentType) super.setId(id);
  }

  /**
   * Adds a new field.
   *
   * @param field CMAField instance
   * @return this {@code CMAContentType}
   */
  public CMAContentType addField(CMAField field) {
    if (fields == null) {
      fields = new ArrayList<CMAField>();
    }

    fields.add(field);
    return this;
  }

  /**
   * Sets the name for this Content Type.
   *
   * @param name name
   * @return this {@code CMAContentType}
   */
  public CMAContentType setName(String name) {
    this.name = name;
    return this;
  }
}
