package com.contentful.java.cma.model;

/**
 * CMATag.
 * <p>
 * This class will be used to represent a tag on the CMA SDK.
 */
public class CMATag extends CMAResource {

  String name;

  /**
   * Create a new tag, specifying the type in the system property.
   */
  public CMATag() {
    super(CMAType.Tag);
  }

  /**
   * Set the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public CMATag setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  /**
   * Convenience: Update the id of tag without going through {@link #getSystem()}.
   *
   * @param id to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMATag setId(String id) {
    return super.setId(id);
  }

  /**
   * Convenience: Update the version of tag without going through {@link #getSystem()}.
   *
   * @param version to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMATag setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Convenience: Update the space id of tag without going through {@link #getSystem()}.
   *
   * @param spaceId to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMATag setSpaceId(String spaceId) {
    return super.setSpaceId(spaceId);
  }

  /**
   * Convenience: get the visibility without going through {@link #getSystem()}.
   *
   * @return the {@code sys.visibility} value, null if it does not exist.
   */
  public CMAVisibility getVisibility() {
    return getSystem().getVisibility();
  }

  /**
   * @return the tag's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the tag.
   *
   * @param name the tag's name to be set.
   * @return the calling instance for chaining.
   */
  public CMATag setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMATag { " + super.toString() + " "
        + "name = " + getName() + " "
        + "}";
  }
}

