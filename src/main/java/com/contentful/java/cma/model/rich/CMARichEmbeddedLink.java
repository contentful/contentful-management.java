package com.contentful.java.cma.model.rich;

import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMAType;

/**
 * This node is an inline link to a CMAEntry
 *
 * @see com.contentful.java.cma.model.CMAEntry
 */
public class CMARichEmbeddedLink extends CMARichHyperLink {
  private final transient boolean inline;

  /**
   * Create a link pointing to a CMAEntry.
   *
   * @param target an entry to be pointed to.
   * @param inline should be inline or block
   */
  public CMARichEmbeddedLink(Object target, boolean inline) {
    super(target);
    this.inline = inline;
  }

  /**
   * Create a link pointing to a CMAEntry.
   *
   * @param target an entry to be pointed to.
   */
  public CMARichEmbeddedLink(Object target) {
    this(target, false);
  }

  /**
   * @return the internal representation of this node type.
   */
  @Override public String getNodeType() {
    if (data instanceof CMALink) {
      final CMAType linkType = ((CMALink) data).getSystem().getLinkType();
      final String block = inline ? "inline" : "block";

      if (linkType == CMAType.Asset) {
        return "embedded-asset-" + block;
      } else if (linkType == CMAType.Entry) {
        return "embedded-entry-" + block;
      }
    }
    return super.getNodeType();
  }
}
