package com.contentful.java.cma.model.rich;

/**
 * A node representing a table row
 */
public class CMARichTableRow extends CMARichBlock {
  /**
   * Create a table row.
   */
  public CMARichTableRow() {
    super("table-row");
  }

  public CMARichTableRow(Object data) {
    super("table-row", data);
  }
}
