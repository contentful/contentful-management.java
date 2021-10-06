package com.contentful.java.cma.model.rich;

/**
 * Representation of a block of unordered items.
 */
public class CMARichUnorderedList extends CMARichBlock {
  /**
   * Creates an unordered list.
   */
  public CMARichUnorderedList() {
    super("unordered-list");
  }

  public CMARichUnorderedList(Object data) {
    super("unordered-list", data);
  }
}