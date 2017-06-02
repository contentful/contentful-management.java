package com.contentful.java.cma.model;

import java.util.LinkedList;
import java.util.List;

/**
 * What do allow or deny a role to do.
 */
public class CMAPolicy {
  public static final String ALLOW = "allow";
  public static final String DENY = "deny";

  private String effect;
  private CMAConstraint constraint;
  private Object actions;

  /**
   * @return either {@link #ALLOW} or {@link #DENY}
   */
  public String getEffect() {
    return effect;
  }

  /**
   * Set a general effect of this policy.
   *
   * @param effect either {@link #ALLOW} or {@link #DENY} or custom.
   * @return this instance fore ease of chaining.
   * @see #allow()
   * @see #deny()
   */
  public CMAPolicy setEffect(String effect) {
    this.effect = effect;
    return this;
  }

  /**
   * @return the constraints set on this policy.
   */
  public CMAConstraint getConstraint() {
    return constraint;
  }

  /**
   * Set the constraint to be used.
   *
   * @param constraint to be set.
   * @return this instance for chaining.
   */
  public CMAPolicy setConstraint(CMAConstraint constraint) {
    this.constraint = constraint;
    return this;
  }

  /**
   * This can method can return a string, 'all', a list of strings identifying the actions, or null.
   *
   * @return the actions set on this policy.
   */
  public Object getActions() {
    return actions;
  }

  /**
   * Replace the current actions with new ones.
   *
   * @param actions the actions replacing the current ones.
   * @return this instance for chaining.
   */
  public CMAPolicy setActions(List<String> actions) {
    this.actions = actions;
    return this;
  }

  /**
   * This policy describes action _allowed_ by the assigned role.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy allow() {
    return setEffect(ALLOW);
  }

  /**
   * This policy should describe actions which are _denied_ by the assigned role.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy deny() {
    return setEffect(DENY);
  }

  /**
   * Add reading to the allowed actions.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy read() {
    return addActionSafely("read");
  }

  /**
   * Add creating to the allowed actions.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy create() {
    return addActionSafely("create");
  }

  /**
   * Add updating to the allowed actions.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy update() {
    return addActionSafely("udpate");
  }

  /**
   * Add deleting to the allowed actions.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy delete() {
    return addActionSafely("delete");
  }

  /**
   * Add publishing to the allowed actions.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy publish() {
    return addActionSafely("publish");
  }

  /**
   * Add unpublishing to the allowed actions.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy unpublish() {
    return addActionSafely("unpublish");
  }

  /**
   * Add archiving to the allowed actions.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy archive() {
    return addActionSafely("archive");
  }

  /**
   * Add unarchiving to the allowed actions.
   *
   * @return this instance for chaining.
   */
  public CMAPolicy unarchive() {
    return addActionSafely("unarchive");
  }

  private CMAPolicy addActionSafely(String action) {
    if ("all".equals(actions)) {
      // actions is the 'all' string, so the action is already added.
      return this;
    }

    if (actions == null) {
      actions = new LinkedList<String>();
    }

    if (actions instanceof List) {
      List actionList = (List) actions;
      if (!actionList.contains(action)) {
        actionList.add(action);
      }
    }
    return this;
  }

  /**
   * @return Human readable representation of this instance.
   */
  @Override public String toString() {
    return "CMAPolicy{"
        + "effect='" + effect + '\''
        + ", constraint=" + constraint
        + ", actions=" + actions
        + '}';
  }
}
