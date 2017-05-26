package com.contentful.java.cma.model;

/**
 * Create a user structure.
 */
public class CMAUser extends CMAResource {

  String firstName;
  String lastName;
  String avatarUrl;
  String email;
  Boolean activated;
  Integer signInCount;
  Boolean confirmed;

  /**
   * Initialize the user.
   */
  public CMAUser() {
    super(CMAType.User);
  }

  /**
   * @return first name of this user.
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @return the last name of this user.
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @return url of an avatar (i.e. image) representing this user.
   */
  public String getAvatarUrl() {
    return avatarUrl;
  }

  /**
   * @return the email address of this user.
   */
  public String getEmail() {
    return email;
  }

  /**
   * @return is this user activated?
   */
  public Boolean getActivated() {
    return activated;
  }

  /**
   * @return How often did the user sign in?
   */
  public Integer getSignInCount() {
    return signInCount;
  }

  /**
   * @return true if the user is confirmed, false otherwise.
   */
  public Boolean getConfirmed() {
    return confirmed;
  }

  /**
   * @return a human readable string, representing the user's information.
   */
  @Override public String toString() {
    return "CMAUser{"
        + "firstName='" + firstName + '\'' + ", "
        + "lastName='" + lastName + '\'' + ", "
        + "avatarUrl='" + avatarUrl + '\'' + ", "
        + "email='" + email + '\'' + ", "
        + "activated=" + activated + ", "
        + "signInCount=" + signInCount + ", "
        + "confirmed=" + confirmed + '}';
  }
}
