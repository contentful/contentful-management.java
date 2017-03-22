package com.contentful.java.cma.model;

/**
 * This class represents a link to another resource in Contentful.
 * <p>
 * It contains a type and a linked type in its system property.
 */
public class CMALink extends CMAResource {
  /**
   * Create a generic link.
   * <p>
   * Depending on the path you are uploading/changing this entry in, you might need to
   * specify a {@link CMASystem#setLinkType(CMAType)} to make this link fully functional.
   */
  public CMALink() {
    super(CMAType.Link);
  }

  /**
   * Create a fully specified link, with type and linkedType.
   *
   * @param linkedType the type this link links to.
   */
  public CMALink(CMAType linkedType) {
    super(CMAType.Link);
    system.setLinkType(linkedType);
  }
}
