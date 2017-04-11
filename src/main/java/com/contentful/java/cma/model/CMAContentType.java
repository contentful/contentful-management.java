/*
 * Copyright (C) 2017 Contentful GmbH
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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a resource of type Content Type.
 */
public class CMAContentType extends CMAResource {
  // Name
  String name;

  // Description Field
  String description;

  // Display Field
  String displayField;

  // List of fields
  List<CMAField> fields;

  /**
   * Create a CMAContentType, filling it's {@link CMASystem} field.
   */
  public CMAContentType() {
    super(CMAType.ContentType);
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

  /**
   * @return the name of this Content Type.
   */
  public String getName() {
    return name;
  }

  /**
   * @return a list of fields for this Content Type.
   */
  public List<CMAField> getFields() {
    return fields;
  }

  /**
   * Sets a list of fields for this Content Type.
   *
   * @param fields List of fields
   * @return this {@code CMAContentType} instance
   */
  public CMAContentType setFields(List<CMAField> fields) {
    this.fields = fields;
    return this;
  }

  /**
   * @return the display field of this Content Type.
   */
  public String getDisplayField() {
    return displayField;
  }

  /**
   * Sets the display field for this Content Type.
   *
   * @param displayField the field to be set
   * @return this {@code CMAContentType} instance
   */
  public CMAContentType setDisplayField(String displayField) {
    this.displayField = displayField;
    return this;
  }

  /**
   * @return the description of this Content Type.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description field for this Content Type.
   *
   * @param description the description to be set
   * @return this {@code CMAContentType} instance
   */
  public CMAContentType setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public CMAContentType setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  /**
   * Convenience: Update the id of this entry without going through {@link #getSystem()}.
   *
   * @param id to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAContentType setId(String id) {
    return super.setId(id);
  }

  /**
   * Convenience: Update the version of this entry without going through {@link #getSystem()}.
   *
   * @param version to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAContentType setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Convenience for getting the version of this resource.
   *
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public Integer getVersion() {
    return super.getVersion();
  }

  /**
   * Convenience: Update the space id of this entry without going through {@link #getSystem()}.
   *
   * @param spaceId to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAContentType setSpaceId(String spaceId) {
    return super.setSpaceId(spaceId);
  }

}
