package com.contentful.java.cma.model.rich;

import java.util.HashMap;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * A leaf node of the rich text hierarchy.
 */
public class CMARichNode {
  @NonNull private final String nodeType;

  @Nullable protected Object data = new HashMap<>();

  /**
   * Create an instance, settings its node type.
   *
   * @param nodeType the type of node to be used for creating json.
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
