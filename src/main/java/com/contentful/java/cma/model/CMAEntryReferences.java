package com.contentful.java.cma.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of CMA resources.
 */
public class CMAEntryReferences extends CMAResource {
  // List with root item
  List<CMAEntry> items;

  // Map of included references
  Includes includes;

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
   * @return a map of included assets.
   */
  public List<CMAAsset> getAssets() {
    return (includes != null) ? includes.assets : new ArrayList<>();
  }

  /**
   * @return a map of included entries.
   */
  public List<CMAEntry> getEntries() {
    return (includes != null) ? includes.entries : new ArrayList<>();
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAArray { " + super.toString() + " "
        + "assets = " + getAssets() + ", "
        + "entries = " + getEntries() + ", "
        + "items = " + getItems() + ", "
        + "}";
  }

  static class Includes {
    @SerializedName("Asset") List<CMAAsset> assets;

    @SerializedName("Entry") List<CMAEntry> entries;
  }
}
