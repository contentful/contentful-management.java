package com.contentful.java.cma.model.rich;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import java.util.HashMap;

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
   * Create an instance, settings its node type and data.
   *
   * @param nodeType the type of node to be used for creating json.
   * @param data the data of node.
   */
  protected CMARichNode(String nodeType, Object data) {
    this.nodeType = nodeType;

    if (data != null) {
      this.data = data;
    }
  }

  /**
   * @return the internal node type.
   */
  public String getNodeType() {
    return nodeType;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
