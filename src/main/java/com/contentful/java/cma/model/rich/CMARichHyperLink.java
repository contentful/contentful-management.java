package com.contentful.java.cma.model.rich;

import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMAType;

/**
 * This block represents a link to a website.
 */
public class CMARichHyperLink extends CMARichBlock {
  Object data;

  /**
   * Create a new hyper link
   */
  public CMARichHyperLink() {
    super("hyperlink");
  }

  /**
   * Create a new hyper link.
   *
   * @param target point to the target.
   */
  public CMARichHyperLink(Object target) {
    this();
    this.data = target;
  }

  /**
   * @return the target this link points to.
   */
  public Object getData() {
    return data;
  }

  /**
   * @return the internal depending on data node type.
   */
  @Override public String getNodeType() {
    if (data instanceof CMALink) {
      final CMAType linkType = ((CMALink) data).getSystem().getLinkType();
      if (linkType == CMAType.Asset) {
        return "asset-hyperlink";
      } else if (linkType == CMAType.Entry) {
        return "entry-hyperlink";
      }
    }
    return super.getNodeType();
  }
}
