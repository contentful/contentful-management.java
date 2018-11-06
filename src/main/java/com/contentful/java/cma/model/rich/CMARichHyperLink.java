package com.contentful.java.cma.model.rich;

import java.util.List;

/**
 * This block represents a link to a website.
 */
public class CMARichHyperLink extends CMARichBlock {
  Object data;

  /**
   * Create a new hyper link.
   *
   * @param target point to the target.
   */
  public CMARichHyperLink(Object target) {
    this.data = target;
  }

  /**
   * @return the target this link points to.
   */
  public Object getData() {
    return data;
  }

  @Override public CMARichHyperLink setContent(List<CMARichNode> content) {
    super.setContent(content);
    return this;
  }

  @Override public CMARichHyperLink addContent(CMARichNode... content) {
    super.addContent(content);
    return this;
  }
}
