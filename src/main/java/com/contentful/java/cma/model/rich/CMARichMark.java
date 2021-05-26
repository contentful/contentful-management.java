package com.contentful.java.cma.model.rich;

/**
 * How to draw a given text.
 * <p>
 * Subclasses are used for further differentiation.
 */
public class CMARichMark {
  private final String type;

  protected CMARichMark(String type) {
    this.type = type;
  }

  /**
   * A bold mark of a rich text.
   */
  public static class CMARichMarkBold extends CMARichMark {
    public CMARichMarkBold() {
      super("bold");
    }
  }

  /**
   * Declares the text as being displayed in italics.
   */
  public static class CMARichMarkItalic extends CMARichMark {
    public CMARichMarkItalic() {
      super("italic");
    }
  }

  /**
   * Marker for making the rich text displayed as underline.
   */
  public static class CMARichMarkUnderline extends CMARichMark {
    public CMARichMarkUnderline() {
      super("underline");
    }
  }

  /**
   * The text marked by this marker should be represented by Code.
   */
  public static class CMARichMarkCode extends CMARichMark {
    public CMARichMarkCode() {
      super("code");
    }
  }

  /**
   * Any custom marker for a given rich text.
   */
  public static class CMARichMarkCustom extends CMARichMark {
    /**
     * Create a custom marker using the given type.
     *
     * @param type which type should this marker have?
     */
    public CMARichMarkCustom(String type) {
      super(type);
    }
  }
}
