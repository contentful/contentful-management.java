package com.contentful.java.cma.model;

import java.util.List;
import java.util.Map;

/**
 * Represents a collection of CMA resources.
 */
public class CMAEntryReferences extends CMAResource {
  // List with root item
  List<CMAEntry> items;

  // Map of included references
  Map<String, List<CMAEntry>> includes;

  /**
   * Create a new Asset, setting type and create a system.
   */
  public CMAEntryReferences() {
    super(CMAType.Array);
  }

  /**
   * @return the list of resources for this array.
   */
  public List<CMAEntry> getItems() {
    return items;
  }

  /**
   * @return a map of included resources.
   */
  public Map<String, List<CMAEntry>> getIncludes() {
    return includes;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAArray { " + super.toString() + " "
        + "includes = " + getIncludes() + ", "
        + "items = " + getItems() + ", "
        + "}";
  }
}
