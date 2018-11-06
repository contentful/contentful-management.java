package com.contentful.java.cma.model.rich;

import java.util.List;

/**
 * This node is an inline link to a CMAEntry
 *
 * @see com.contentful.java.cma.model.CMAEntry
 */
public class CMARichEmbeddedLink extends CMARichHyperLink {
  /**
   * Create a link pointing to a CMAEntry.
   *
   * @param target an entry to be pointed to.
   */
  public CMARichEmbeddedLink(Object target) {
    super(target);
  }

  @Override public CMARichEmbeddedLink setContent(List<CMARichNode> content) {
    super.setContent(content);
    return this;
  }

  @Override public CMARichEmbeddedLink addContent(CMARichNode... content) {
    super.addContent(content);
    return this;
  }
}
