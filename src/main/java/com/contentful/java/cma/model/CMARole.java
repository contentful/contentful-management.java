package com.contentful.java.cma.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a role in Contentful.
 */
public class CMARole extends CMAResource {

  private String name;
  private String description;
  private List<CMAPolicy> policies;
  private CMAPermissions permissions;

  /**
   * Creates a new role.
   */
  public CMARole() {
    super(CMAType.Role);
  }

  /**
   * What is the name of this role?
   *
   * @return the name of this role.
   */
  public String getName() {
    return name;
  }

  /**
   * Update the name of the role.
   *
   * @param name human readable string representing the name.
   * @return this instance for chaining.
   */
  public CMARole setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Description of this role, human readable.
   *
   * @return a string describing this role.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Update or set the description of this role.
   *
   * @param description a non null string representing the description
   * @return this instance for ease of chaining.
   */
  public CMARole setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * @return a list of policies this role contains.
   */
  public List<CMAPolicy> getPolicies() {
    return policies;
  }

  /**
   * Update the complete list of policies.
   *
   * @param policies the new policies to be used.
   */
  public void setPolicies(List<CMAPolicy> policies) {
    this.policies = policies;
  }

  /**
   * Add a new policy to the existing ones, creating a new list if needed.
   *
   * @param policy the policy to be added.
   * @return this instance for chaining.
   */
  public CMARole addPolicy(CMAPolicy policy) {
    if (policies == null) {
      policies = new ArrayList<CMAPolicy>();
    }

    policies.add(policy);
    return this;
  }

  /**
   * @return the permissions set.
   */
  public CMAPermissions getPermissions() {
    return permissions;
  }

  /**
   * Replace the currently set permissions with a new one.
   *
   * @param permissions new permissions to be set
   * @return this instance for chaining.
   */
  public CMARole setPermissions(CMAPermissions permissions) {
    this.permissions = permissions;
    return this;
  }

  /**
   * Sets the id of this space membership.
   *
   * @param id to be set.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override public CMARole setId(String id) {
    return super.setId(id);
  }

  /**
   * Set the version of this space membership.
   *
   * @param version the version to be set.
   * @return this space instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override
  public CMARole setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Sets the id of this space memberships space.
   *
   * @param id the id to be set.
   * @return this space instance for chaining.
   * @see #setId(String)
   */
  @SuppressWarnings("unchecked") @Override
  public CMARole setSpaceId(String id) {
    return super.setSpaceId(id);
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMARole { " + super.toString() + " "
        + "description = " + getDescription() + ", "
        + "name = " + getName() + ", "
        + "permissions = " + getPermissions() + ", "
        + "policies = " + getPolicies() + " "
        + "}";
  }
}
