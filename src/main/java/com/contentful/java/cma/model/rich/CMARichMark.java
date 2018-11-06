package com.contentful.java.cma.model.rich;

/**
 * How to draw a given text.
 * <p>
 * Subclasses are used for further differentiation.
 */
public class CMARichMark {
  /**
   * A bold mark of a rich text.
   */
  public static class CMARichMarkBold extends CMARichMark {
  }

  /**
   * Declares the text as being displayed in italics.
   */
  public static class CMARichMarkItalic extends CMARichMark {
  }

  /**
   * Marker for making the rich text displayed as underline.
   */
  public static class CMARichMarkUnderline extends CMARichMark {
  }

  /**
   * The text marked by this marker should be represented by Code.
   */
  public static class CMARichMarkCode extends CMARichMark {
  }

  /**
   * Any custom marker for a given rich text.
   */
  public static class CMARichMarkCustom extends CMARichMark {
    private final String type;

    /**
     * Create a custom marker using the given type.
     *
     * @param type which type should this marker have?
     */
    public CMARichMarkCustom(String type) {
      this.type = type;
    }

    /**
     * @return the custom type of the marker.
     */
    public String getType() {
      return type;
    }
  }
}
