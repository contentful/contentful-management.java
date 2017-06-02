package com.contentful.java.cma.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * What constraints are applied to this rule?
 */
public class CMAConstraint {

  /**
   * Define a path to a field.
   */
  public static class Path {
    private String doc;

    /**
     * Set the path.
     *
     * @param doc the path to the document, like "fields.name.nopenotok".
     **/
    public Path setDoc(String doc) {
      this.doc = doc;
      return this;
    }

    /**
     * @return the doc part of the path.
     */
    public String getDoc() {
      return doc;
    }

    /**
     * @return Human readable representation of this instance.
     */
    @Override public String toString() {
      return "Path{"
          + "doc='" + doc + '\''
          + '}';
    }
  }

  /**
   * Create an equals object. Used for deciding whether the path to a field containts a given value.
   */
  public static class Equals extends ArrayList<Object> {
    /**
     * @return which path this equals is targeting.
     */
    public Path getPath() {
      if (size() == 2) {
        final Object path = this.get(0);
        if (path instanceof Path) {
          return (Path) path;
        } else if (path instanceof Map) {
          Map mappedPath = (Map) path;
          final Path objectivicedPath = new Path().setDoc((String) mappedPath.get("doc"));
          setPath(objectivicedPath);
          return objectivicedPath;
        }
        // missing else for if type is different.
      }

      return null;
    }

    /**
     * Update the path component.
     *
     * @param path the new path to be applied.
     * @return this instance for chaining
     */

    public Equals setPath(Path path) {
      if (size() == 0) {
        add(path);
      } else {
        set(0, path);
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
      return "Equals{"
          + "doc='" + getPath() != null ? getPath().doc : "<null>" + "\',"
          + "value='" + getValue() + '\''
          + '}';
    }
  }

  private CMAConstraint[] and;
  private CMAConstraint[] or;
  private CMAConstraint[] not;
  private Equals equals;
  private Path[] paths;

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
   * @return this instance for chaining.
   */
  public CMAConstraint setEquals(Equals equals) {
    this.equals = equals;
    return this;
  }

  /**
   * On which paths does this constraint act?
   *
   * @return a array of paths to be act upon.
   */
  public Path[] getPaths() {
    return paths;
  }

  /**
   * Set the array of paths to be used.
   *
   * @param paths the new paths to be set.
   * @return this instance for chaining.
   */
  public CMAConstraint setPaths(Path... paths) {
    this.paths = paths;
    return this;
  }

  /**
   * @return Human readable representation of this instance.
   */
  @Override public String toString() {
    return "CMAConstraint{"
        + "and=" + Arrays.toString(and)
        + ", or=" + Arrays.toString(or)
        + ", not=" + Arrays.toString(not)
        + ", equals=" + equals
        + ", paths=" + Arrays.toString(paths)
        + '}';
  }
}
