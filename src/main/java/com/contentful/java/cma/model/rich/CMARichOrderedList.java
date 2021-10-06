package com.contentful.java.cma.model.rich;

/**
 * A list of elements, ordered by number.
 */
public class CMARichOrderedList extends CMARichBlock {
  /**
   * Create an ordered list.
   */
  public CMARichOrderedList() {
    super("ordered-list");
  }

  public CMARichOrderedList(Object data) {
    super("ordered-list", data);
  }
}
