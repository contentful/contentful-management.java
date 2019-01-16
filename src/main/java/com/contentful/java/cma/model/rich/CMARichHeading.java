package com.contentful.java.cma.model.rich;

/**
 * Defines a headline of the text.
 * <p>
 * Can have an arbitrary level assigned, but useful probably between 1 and 6.
 */
public class CMARichHeading extends CMARichBlock {
  private final transient int level;

  /**
   * Create a heading block, describing a level elements deep nested heading.
   *
   * @param level a number indicating the level of this heading.
   */
  public CMARichHeading(int level) {
    super("heading-" + level);
    this.level = level;
  }

  /**
   * @return the current nesting level of this heading.
   */
  public int getLevel() {
    return level;
  }
}
