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

  /**
   * Change ths system property to a new one.
   *
   * @param system the system property to be set.
   * @return this instance of a link for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMALink setSystem(CMASystem system) {
    return super.setSystem(system);
  }

  /**
   * Set this ids value.
   *
   * @param id to be set.
   * @return This instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMALink setId(String id) {
    return super.setId(id);
  }

  /**
   * Change this instances version number.
   *
   * @param version the version number to be set.
   * @return this instance to be chained.
   */
  @SuppressWarnings("unchecked")
  @Override public CMALink setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Set the id of the space this link.
   *
   * @param spaceId the id to be set.
   * @return this instance for ease of chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMALink setSpaceId(String spaceId) {
    return super.setSpaceId(spaceId);
  }
}
