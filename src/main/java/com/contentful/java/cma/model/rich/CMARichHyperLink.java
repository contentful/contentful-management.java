package com.contentful.java.cma.model.rich;

import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMAType;

/**
 * This block represents a link to a website.
 */
public class CMARichHyperLink extends CMARichBlock {
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
   * @throws IllegalStateException if target is not a {@link String}
   * @throws IllegalStateException if target is not a {@link com.contentful.java.cma.model.CMALink}
   */
  public CMARichHyperLink(Object target) {
    this();
    if (target instanceof String || target instanceof CMALink) {
      this.data = target;
    } else {
      throw new IllegalStateException("Target " + target + " of type '"
          + target.getClass().getCanonicalName() + "'is neither a String, nor "
          + "a CMAAsset, nor a  CMAEntry.");
    }
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
