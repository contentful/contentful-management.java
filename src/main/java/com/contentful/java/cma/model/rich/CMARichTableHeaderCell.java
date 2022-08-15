package com.contentful.java.cma.model.rich;

/**
 * A node representing a table header cell
 */
public class CMARichTableHeaderCell extends CMARichBlock {
  /**
   * Create a table header cell.
   */
  public CMARichTableHeaderCell() {
    super("table-header-cell");
  }

  public CMARichTableHeaderCell(Object data) {
    super("table-header-cell", data);
  }
}
