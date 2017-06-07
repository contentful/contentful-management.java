package com.contentful.java.cma.model;

/**
 * Class representing an uploaded file, to be used as an asset.
 */
public class CMAUpload extends CMAResource {
  /**
   * create an upload
   */
  public CMAUpload() {
    super(CMAType.Upload);
  }

  /**
   * Update the current instances system property to a complete new one.
   *
   * @param system sets the system property.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAUpload setSystem(CMASystem system) {
    return super.setSystem(system);
  }

  /**
   * Sets this resources id.
   *
   * @param id to be set
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAUpload setId(String id) {
    return super.setId(id);
  }

  /**
   * Sets the version of this resource.
   *
   * @param version the version number to be set.
   * @return this instance of CMAUpload for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAUpload setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Set the space id in a chainable manner.
   *
   * @param spaceId the id to be set.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAUpload setSpaceId(String spaceId) {
    return super.setSpaceId(spaceId);
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAUpload { " + super.toString() + " }";
  }
}
