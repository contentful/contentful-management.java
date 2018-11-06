package com.contentful.java.cma.model.rich;

import java.util.List;

/**
 * Defines a headline of the text.
 * <p>
 * Can have an arbitrary level assigned, but useful probably between 1 and 6.
 */
public class CMARichHeading extends CMARichBlock {
  private final int level;

  /**
   * Create a heading block, describing a level elements deep nested heading.
   *
   * @param level a number indicating the level of this heading.
   */
  public CMARichHeading(int level) {
    this.level = level;
  }

  /**
   * Create a level one heading block.
   */
  public CMARichHeading() {
    this(1);
  }

  /**
   * @return the current nesting level of this heading.
   */
  public int getLevel() {
    return level;
  }

  @Override public CMARichHeading setContent(List<CMARichNode> content) {
    super.setContent(content);
    return this;
  }

  @Override public CMARichHeading addContent(CMARichNode... content) {
    super.addContent(content);
    return this;
  }
}
