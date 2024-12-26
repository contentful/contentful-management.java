package com.contentful.java.cma.model.rich;

/**
 * Rich Text table.
 */
public class CMARichTable extends CMARichBlock {
  /**
   * Create a table.
   */
  public CMARichTable() {
    super("table");
  }

  public CMARichTable(Object data) {
    super("table", data);
  }
}
