package com.contentful.java.cma.model.rich;

/**
 * A block of nodes rendered as a direct quote.
 */
public class CMARichQuote extends CMARichBlock {
  /**
   * Create a quote node.
   */
  public CMARichQuote() {
    super("blockquote");
  }

  public CMARichQuote(Object data) {
    super("blockquote", data);
  }
}
