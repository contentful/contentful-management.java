package com.contentful.java.cma.model.rich;

import java.util.List;

/**
 * A list of elements, ordered by number.
 */
public class CMARichOrderedList extends CMARichBlock {
  @Override public CMARichOrderedList setContent(List<CMARichNode> content) {
    super.setContent(content);
    return this;
  }

  @Override public CMARichOrderedList addContent(CMARichNode... content) {
    super.addContent(content);
    return this;
  }
}
