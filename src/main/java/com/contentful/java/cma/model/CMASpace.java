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

/**
 * Represents a resource of type Space.
 */
public class CMASpace extends CMAResource {
  // Name
  String name;

  /**
   * Sets the name for this Space.
   * Returns this {@code CMASpace} instance.
   */
  public CMASpace setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Returns the name of this Space.
   */
  public String getName() {
    return name;
  }
}
