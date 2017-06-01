package com.contentful.java.cma.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model class, exposing a space membership.
 */
public class CMASpaceMembership extends CMAResource {
  private boolean admin;
  private CMALink user;
  private List<CMALink> roles;
  private String email;

  /**
   * Create a new membership.
   */
  public CMASpaceMembership() {
    super(CMAType.SpaceMembership);
  }

  /**
   * Update admin flag.
   * <p>
   * If you set this flag to false, you have to provide some roles this membership should be part
   * of.
   *
   * @param admin true, if this membership should be an admin.
   * @return this membership for ease of chaining.
   * @see #setRoles(CMALink...)
   * @see #addRole(CMALink)
   */
  public CMASpaceMembership setAdmin(boolean admin) {
    this.admin = admin;
    return this;
  }

  /**
   * @return a link to the user of this membership
   */
  public CMALink getUser() {
    return user;
  }

  /**
   * Update or set the user for this membership.
   *
   * @param user which user should be part of this membership?
   * @return this membership for ease of chaining.
   */
  public CMASpaceMembership setUser(CMALink user) {
    this.user = user;
    return this;
  }

  /**
   * @return a list of links to the roles this membership contains.
   */
  public List<CMALink> getRoles() {
    return roles;
  }

  /**
   * Replace all roles with the given argument.
   *
   * @param roles the roles to be used.
   * @return this instance of membership for ease of chaining.
   * @throws IllegalArgumentException if roles is null.
   * @throws IllegalArgumentException if roles does not contain any elements.
   */
  public CMASpaceMembership setRoles(CMALink... roles) {
    if (roles == null) {
      throw new IllegalArgumentException("Roles cannot be null!");
    }
    if (roles.length <= 0) {
      throw new IllegalArgumentException("Roles cannot be empty!");
    }

    this.roles = new ArrayList<CMALink>(Arrays.asList(roles));
    return this;
  }

  /**
   * Add a role to the list of roles.
   *
   * @param role the role to be used, needs to be not null.
   * @return this membership for ease of chaining.
   * @throws IllegalArgumentException if role is null.
   */
  public CMASpaceMembership addRole(CMALink role) {
    if (role == null) {
      throw new IllegalArgumentException("Role cannot be null!");
    }

    if (roles == null) {
      roles = new ArrayList<CMALink>();
    }

    this.roles.add(role);
    return this;
  }

  /**
   * Get email of membership.
   * <p>
   * This email will only be available, if this membership is created a new. You will never see this
   * email address coming back from memberships.
   *
   * @return the set email address.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Set the email address when creating a new membership.
   *
   * @param email to be used for inviting people.
   * @return this membership for ease of chaining.
   * @throws IllegalArgumentException if email is null.
   * @throws IllegalArgumentException if email does not contain an '@'.
   */
  public CMASpaceMembership setEmail(String email) {
    if (email == null) {
      throw new IllegalArgumentException("email cannot be null.");
    }
    if (!email.contains("@")) {
      throw new IllegalArgumentException("email needs to contain an '@' symbol.");
    }

    this.email = email;
    return this;
  }

  /**
   * @return is this membership an administrator?
   */
  public boolean isAdmin() {
    return admin;
  }

  /**
   * Update administrator state of this membership.
   *
   * @param admin state of this membership.
   * @return this instance fore ease of chaining.
   */
  public CMASpaceMembership setIsAdmin(boolean admin) {
    this.admin = admin;
    return this;
  }

  /**
   * Sets the id of this space membership.
   *
   * @param id to be set.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override public CMASpaceMembership setId(String id) {
    return super.setId(id);
  }

  /**
   * Set the version of this space membership.
   *
   * @param version the version to be set.
   * @return this space instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override
  public CMASpaceMembership setVersion(Integer version) {
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
  public CMASpaceMembership setSpaceId(String id) {
    return super.setId(id);
  }

  /**
   * This method returns the actual id of this space.
   *
   * @return the id of this space
   * @see #getId()
   */
  @Override public String getSpaceId() {
    return super.getId();
  }
}
