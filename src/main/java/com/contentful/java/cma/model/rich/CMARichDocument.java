package com.contentful.java.cma.model.rich;

import java.util.List;

/**
 * The base of a rich text field, containing all the other nodes.
 */
public class CMARichDocument extends CMARichBlock {
  @Override public CMARichDocument setContent(List<CMARichNode> content) {
    super.setContent(content);
    return this;
  }

  @Override public CMARichBlock addContent(CMARichNode... content) {
    super.addContent(content);
    return this;
  }
}
