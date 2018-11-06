package com.contentful.java.cma.model.rich;

import java.util.List;

/**
 * A block of nodes rendered as a direct quote.
 */
public class CMARichQuote extends CMARichBlock {
  @Override public CMARichQuote setContent(List<CMARichNode> content) {
    super.setContent(content);
    return this;
  }

  @Override public CMARichQuote addContent(CMARichNode... content) {
    super.addContent(content);
    return this;
  }
}
