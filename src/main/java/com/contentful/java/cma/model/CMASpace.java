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

/**
 * Represents a resource of type Space.
 */
public class CMASpace extends CMAResource {
  // Name
  String name;

  /**
   * Create a space using the default types for the system property.
   */
  public CMASpace() {
    super(CMAType.Space);
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public CMASpace setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  /**
   * Sets the name for this Space.
   *
   * @param name the name of the Space to be set
   * @return this {@code CMASpace} instance.
   */
  public CMASpace setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return the name of this Space.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the id of this space.
   *
   * @param id to be set.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override public CMASpace setId(String id) {
    return super.setId(id);
  }

  /**
   * Set the version of this space.
   *
   * @param version the version to be set.
   * @return this space instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override
  public CMASpace setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Sets the id of this space.
   *
   * @param id the id to be set.
   * @return this space instance for chaining.
   * @see #setId(String)
   */
  @SuppressWarnings("unchecked") @Override
  public CMASpace setSpaceId(String id) {
    return super.setId(id);
  }

  /**
   * This method returns the actual id of this space.
   *
   * @return the id of this space
   * @see #getId()
   */
  @Override public String getSpaceId() {
    return super.getId();
  }
}
