package com.contentful.java.cma.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * What constraints are applied to this rule?
 */
public class CMAConstraint {

  private CMAConstraint[] and;
  private CMAConstraint[] or;
  private CMAConstraint[] not;
  private Equals equals;
  private FieldKeyPath[] fieldKeyPaths;

  /**
   * Which constraints have to be all satisfied?
   *
   * @return the and constraints.
   */
  public CMAConstraint[] getAnd() {
    return and;
  }

  /**
   * Set a new constraints which have all to be satisfied.
   *
   * @param and constraints to be satisfied.
   * @return this constraint to for chaining.
   */
  public CMAConstraint setAnd(CMAConstraint... and) {
    this.and = and;
    return this;
  }

  /**
   * Which array of constraints where only one needs to be satisfied?
   *
   * @return the or constraints.
   */
  public CMAConstraint[] getOr() {
    return or;
  }

  /**
   * Set a new list of constraints where only one be satisfied.
   *
   * @param or constraints of which one needs to be satisfied.
   * @return this constraint to for chaining.
   */
  public CMAConstraint setOr(CMAConstraint... or) {
    this.or = or;
    return this;
  }

  /**
   * Which constraints do need to be not satisfied?
   * <p>
   * This effectively negates all the constraints.
   *
   * @return a array of not satisfied constraints.
   */
  public CMAConstraint[] getNot() {
    return not;
  }

  /**
   * Set a new array of constraints which must not be satisfied.
   *
   * @param not a constraint not to be true.
   * @return this constraint to for chaining.
   */
  public CMAConstraint setNot(CMAConstraint... not) {
    this.not = not;
    return this;
  }

  /**
   * Which condition has to be satisfied to satisfy this constraint?
   *
   * @return an equals condition.
   */
  public Equals getEquals() {
    return equals;
  }

  /**
   * Update the condition to be satisfied to satisfy this constraint.
   *
   * @param equals the equals constraint.
   * @return this instance for chaining.
   */
  public CMAConstraint setEquals(Equals equals) {
    this.equals = equals;
    return this;
  }

  /**
   * On which fieldKeyPaths does this constraint act?
   *
   * @return a array of fieldKeyPaths to be act upon.
   */
  public FieldKeyPath[] getFieldKeyPaths() {
    return fieldKeyPaths;
  }

  /**
   * Set the array of fieldKeyPaths to be used.
   *
   * @param fieldKeyPaths the new fieldKeyPaths to be set.
   * @return this instance for chaining.
   */
  public CMAConstraint setFieldKeyPaths(FieldKeyPath... fieldKeyPaths) {
    this.fieldKeyPaths = fieldKeyPaths;
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAConstraint { "
        + "and = " + Arrays.toString(getAnd()) + ", "
        + "equals = " + getEquals() + ", "
        + "not = " + Arrays.toString(getNot()) + ", "
        + "or = " + Arrays.toString(getOr()) + ", "
        + "fieldKeyPaths = " + Arrays.toString(getFieldKeyPaths()) + " "
        + "}";
  }

  /**
   * Define a path to a field.
   */
  public static class FieldKeyPath {
    private String doc;

    /**
     * @return the doc part of the path.
     */
    public String getDoc() {
      return doc;
    }

    /**
     * Set the path.
     *
     * @param doc the path to the document, like "fields.name.nopenotok".
     * @return this instance for chaining.
     **/
    public FieldKeyPath setDoc(String doc) {
      this.doc = doc;
      return this;
    }

    /**
     * @return a human readable string, representing the object.
     */
    @Override public String toString() {
      return "FieldKeyPath { "
          + "doc = " + getDoc() + " "
          + "}";
    }
  }

  /**
   * Create an equals deciding whether the path to a field constraints a given value.
   */
  public static class Equals extends ArrayList<Object> {
    /**
     * @return which path this equals is targeting.
     */
    public FieldKeyPath getPath() {
      if (size() == 2) {
        final Object path = this.get(0);
        if (path instanceof FieldKeyPath) {
          return (FieldKeyPath) path;
        } else if (path instanceof Map) {
          Map<String, String> mappedPath = (Map<String, String>) path;
          final FieldKeyPath objectifiedPath = new FieldKeyPath()
              .setDoc(mappedPath.get("doc"));
          setPath(objectifiedPath);
          return objectifiedPath;
        }
        // missing else: fall through to return `null`, not a valid state.
      }

      return null;
    }

    /**
     * Update the fieldKeyPath component.
     *
     * @param fieldKeyPath the new fieldKeyPath to be applied.
     * @return this instance for chaining
     */
    public Equals setPath(FieldKeyPath fieldKeyPath) {
      if (size() == 0) {
        add(fieldKeyPath);
      } else {
        set(0, fieldKeyPath);
      }
      return this;
    }

    /**
     * @return the value to be used for comparison.
     */
    public String getValue() {
      if (size() == 2) {
        return (String) this.get(1);
      } else {
        return null;
      }
    }

    /**
     * Update the value to be used in comparison.
     *
     * @param value the value.
     * @return this instance for chaining.
     */
    public Equals setValue(String value) {
      switch (size()) {
        case 0:
          add(null);
        case 1:
          add(value);
          break;
        case 2:
          set(1, value);
        default:
          break;
      }

      return this;
    }

    /**
     * @return Human readable representation of this instance.
     */
    @Override public String toString() {
      return "Equals {"
          + "doc = " + getPath() != null ? getPath().doc : "<null>" + ", "
          + "value = " + getValue() + " "
          + "}";
    }
  }
}
