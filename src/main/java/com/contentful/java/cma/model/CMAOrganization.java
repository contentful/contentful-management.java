package com.contentful.java.cma.model;

/**
 * Model class representing one organization.
 */
public class CMAOrganization extends CMAResource {
  private String name;

  /**
   * Creates a new organization.
   */
  public CMAOrganization() {
    super(CMAType.Organization);
  }

  /**
   * @return the name of this organization.
   */
  public String getName() {
    return name;
  }

  /**
   * Update the name of the organization.
   *
   * @param name new name to be set
   * @return this instance for chaining.
   */
  public CMAOrganization setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAOrganizations { " + super.toString() + " "
        + "name = " + getName() + " "
        + "}";
  }
}
