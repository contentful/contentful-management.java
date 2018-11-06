package com.contentful.java.cma.model.rich;

import java.util.List;

/**
 * A block representing an item inside a list.
 */
public class CMARichListItem extends  CMARichBlock {
  @Override public CMARichListItem setContent(List<CMARichNode> content) {
    super.setContent(content);
    return this;
  }

  @Override public CMARichListItem addContent(CMARichNode... content) {
    super.addContent(content);
    return this;
  }
}
