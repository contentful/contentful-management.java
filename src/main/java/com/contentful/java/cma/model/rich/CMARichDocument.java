package com.contentful.java.cma.model.rich;

/**
 * The base of a rich text field, containing all the other nodes.
 */
public class CMARichDocument extends CMARichBlock {
  /**
   * Create a new document.
   */
  public CMARichDocument() {
    super("document");
  }
}
