package com.contentful.java.cma.model.rich;

/**
 * A node representing a table cell
 */
public class CMARichTableCell extends CMARichBlock {
  /**
   * Create a table cell.
   */
  public CMARichTableCell() {
    super("table-cell");
  }

  public CMARichTableCell(Object data) {
    super("table-cell", data);
  }
}
