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

import com.contentful.java.cma.Constants.CMAFieldType;

/**
 * CMAField.
 */
public class CMAField {
  // ID
  String id;

  // Name
  String name;

  // Type
  CMAFieldType type;

  // Link Type
  String linkType;

  /**
   * Sets the ID for this field.
   * Returns this {@code CMAField} instance
   */
  public CMAField setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Sets the name for this field.
   * Returns this {@code CMAField} instance
   */
  public CMAField setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the type for this field.
   * Returns this {@code CMAField} instance
   */
  public CMAField setType(CMAFieldType type) {
    this.type = type;
    return this;
  }

  /**
   * Sets the link type for this field.
   * Returns this {@code CMAField} instance
   */
  public CMAField setLinkType(String linkType) {
    this.linkType = linkType;
    return this;
  }

  /**
   * Returns the {@code id} attribute of this field.
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the {@code name} attribute of this field.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the {@code type} attribute of this field.
   */
  public CMAFieldType getType() {
    return type;
  }

  /**
   * Returns the {@code linkType} attribute of this field.
   */
  public String getLinkType() {
    return linkType;
  }
}
