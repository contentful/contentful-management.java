package com.contentful.java.cma.model.rich;

import java.util.List;

/**
 * Representation of a block of unordered items.
 */
public class CMARichUnorderedList extends CMARichBlock {
  @Override public CMARichUnorderedList setContent(List<CMARichNode> content) {
    super.setContent(content);
    return this;
  }

  @Override public CMARichUnorderedList addContent(CMARichNode... content) {
    super.addContent(content);
    return this;
  }
}
