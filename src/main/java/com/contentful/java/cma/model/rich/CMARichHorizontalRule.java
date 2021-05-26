package com.contentful.java.cma.model.rich;

import java.util.ArrayList;
import java.util.List;

/**
 * A node representing a division, called a horizontal rule.
 */
public class CMARichHorizontalRule extends CMARichNode {
  private List<String> content = new ArrayList<>();

  /**
   * Construct a horizontal rule node.
   */
  public CMARichHorizontalRule() {
    super("hr");
  }
}
