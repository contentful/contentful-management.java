package com.contentful.java.cma.model.rich;

/**
 * Rich Text table cell.
 */
public class CMARichTableCell extends CMARichBlock {
  /**
   * Create a table.
   */
  public CMARichTableCell() {
    super("table-cell");
  }

  public CMARichTableCell(Object data) {
    super("table-cell", data);
  }
}
