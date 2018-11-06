package com.contentful.java.cma.model.rich;

import java.util.List;

/**
 * A paragraph of nodes, usually rendered together.
 */
public class CMARichParagraph extends CMARichBlock {
  @Override public CMARichParagraph setContent(List<CMARichNode> content) {
    super.setContent(content);
    return this;
  }

  @Override public CMARichParagraph addContent(CMARichNode... content) {
    super.addContent(content);
    return this;
  }
}
