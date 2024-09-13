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

import java.util.List;
import java.util.Map;

/**
 * Represents a collection of CMA resources.
 */
public class CMAArray<T extends CMAResource> extends CMAResource {
  // List of resources
  List<T> items;

  // Map of included resources
  Map<String, Object> includes;

  // Total number of items
  int total;

  // Skip
  int skip;

  // Limit
  int limit;

  CMAPages pages;

  /**
   * Create a new Asset, setting type and create a system.
   */
  public CMAArray() {
    super(CMAType.Array);
  }

  /**
   * @return the list of resources for this array.
   */
  public List<T> getItems() {
    return items;
  }

  /**
   * @return the total number of resources contained within this array.
   */
  public int getTotal() {
    return total;
  }

  /**
   * @return the {@code skip} attribute for this array.
   */
  public int getSkip() {
    return skip;
  }

  /**
   * @return the {@code limit} attribute for this array.
   */
  public int getLimit() {
    return limit;
  }

  /**
   * @return a map of included resources.
   */
  public Map<String, Object> getIncludes() {
    return includes;
  }

  public CMAPages getPages() {
    return pages;
  }

  public void setPages(CMAPages pages) {
    this.pages = pages;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAArray { " + super.toString() + " "
        + "includes = " + getIncludes() + ", "
        + "items = " + getItems() + ", "
        + "limit = " + getLimit() + ", "
        + "skip = " + getSkip() + ", "
        + "total = " + getTotal() + " "
        + "}";
  }


}
