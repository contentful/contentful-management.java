/*
 * Copyright (C) 2019 Contentful GmbH
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

/**
 * Represents a resource of type environment.
 */
public class CMATag extends CMAResource {
  // Name
  String name;

  /**
   * Create a tag using the default types for the system property.
   */
  public CMATag() {
    super(CMAType.Tag);
  }

  @Override
  public CMAVisibility getVisibility() {
    return super.getVisibility();
  }

  @Override
  public <T extends CMAResource> T setVisibility(CMAVisibility visibility) {
    return super.setVisibility(visibility);
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public CMATag setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  /**
   * @return the name of this tag.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name for this tag.
   *
   * @param name the name of the tag to be set
   * @return this {@code CMATag} instance.
   */
  public CMATag setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the id of this tag.
   *
   * @param id to be set.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override public CMATag setId(String id) {
    return super.setId(id);
  }

  /**
   * Override retrieval of the tag id here, since the id of this tag is it's id.
   *
   * @return the ID of the tag
   */
  @Override public String getId() {
    return super.getId();
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMATag { " + super.toString() + " "
        + "name = " + getName() + " "
        + "}";
  }
}
