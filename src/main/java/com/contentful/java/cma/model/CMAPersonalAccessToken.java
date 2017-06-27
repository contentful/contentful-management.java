package com.contentful.java.cma.model;

import com.contentful.java.cma.ModulePersonalAccessTokens;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model storing the information about a personal access token.
 * <p>
 * Once a personal access token is created, it's token field will be returned only once: directly
 * as a result of the create call.
 */
public class CMAPersonalAccessToken extends CMAResource {

  /**
   * Enum holding the allowed values of scope.
   */
  public enum Scope {
    @SerializedName("content_management_read")Read,
    @SerializedName("content_management_manage")Manage
  }

  String name;
  String revokedAt;
  List<Scope> scopes;
  String token;

  /**
   * Create a new personal access token to be uploaded to Contentful.
   */
  public CMAPersonalAccessToken() {
    super(CMAType.PersonalAccessToken);
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public CMAPersonalAccessToken setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  /**
   * Sets the name for this personal access token.
   *
   * @param name the name of the personal access token to be set.
   * @return this {@code CMAPersonalAccessToken} instance.
   */
  public CMAPersonalAccessToken setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return the name of this personal access token.
   */
  public String getName() {
    return name;
  }

  /**
   * @return the time this token was revoked, or null if it is still active.
   * @see ModulePersonalAccessTokens#revoke(CMAPersonalAccessToken)
   */
  public String getRevokedAt() {
    return revokedAt;
  }

  /**
   * Add a new scope to the list of scopes. Creates a new list of scopes if no scopes found.
   *
   * @param scope the new scope to be added.
   * @return this instance for chaining.
   */
  public CMAPersonalAccessToken addScope(Scope scope) {
    if (scopes == null) {
      scopes = new ArrayList<Scope>();
    }

    scopes.add(scope);
    return this;
  }

  /**
   * @return all added scopes or null if no scopes where added.
   */
  public List<Scope> getScopes() {
    return scopes;
  }

  /**
   * This getter returns the token string of this token.
   * <p>
   * This string is to be used instead of the CMA token used to create it. Sadly it is only
   * available directly after the token got created, as a response to the create call. Subsequent
   * calls of fetching it by id, or as a collection request, will not return it, so please save it
   * securely after creation.
   *
   * @return the token string if just created or null.
   */
  public String getToken() {
    return token;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAPersonalAccessToken { " + super.toString() + " "
        + "name = " + getName() + ", "
        + "revokedAt = " + getRevokedAt() + ", "
        + "scopes = " + getScopes() + ", "
        + "token = " + getToken() + " "
        + "}";
  }
}
