package com.contentful.java.cma.model.rich;

import io.reactivex.annotations.NonNull;

/**
 * A leaf node of the rich text hierarchy.
 */
public class CMARichNode {
  @NonNull private final String nodeType;

  /**
   * Create an instance, settings its node type.
   */
  protected CMARichNode(String nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * @return the internal node type.
   */
  public String getNodeType() {
    return nodeType;
  }
}
