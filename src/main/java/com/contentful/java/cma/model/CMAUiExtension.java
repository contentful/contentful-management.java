package com.contentful.java.cma.model;

import com.contentful.java.cma.Constants.CMAFieldType;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to describe an ui extension.
 */
public class CMAUiExtension extends CMAResource {
  private static final int MAXIMUM_BYTE_COUNT = 200 * 1024;

  Extension extension;

  /**
   * Creates a new ui extension.
   */
  public CMAUiExtension() {
    super(CMAType.UiExtension);
  }

  /**
   * Update the id of this ui extension.
   *
   * @param id to be set.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override
  public CMAUiExtension setId(String id) {
    super.setId(id);
    return this;
  }

  /**
   * @return the extension description part of the ui extension.
   */
  public Extension getExtension() {
    if (extension == null) {
      extension = new Extension();
    }

    return extension;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAUiExtension { " + super.toString() + " "
        + "extension = " + getExtension() + " "
        + "}";
  }

  /**
   * Model holding the actual information of the extension.
   */
  public static class Extension {

    String name;
    List<FieldType> fieldTypes;
    @SerializedName("srcdoc") String sourceContent;
    @SerializedName("src") String sourceUrl;
    @SerializedName("sidebar") boolean onSidebar;

    /**
     * @return the name of this ui extension.
     */
    public String getName() {
      return name;
    }

    /**
     * Update the name of this ui extension.
     *
     * @param name the new name.
     * @return this instance for chaining.
     */
    public Extension setName(String name) {
      this.name = name;
      return this;
    }

    /**
     * @return a list of field types to be able to use this ui extension.
     */
    public List<FieldType> getFieldTypes() {
      return fieldTypes;
    }

    /**
     * Add a new content type field type to this ui extension.
     *
     * @param type the new type to be added.
     * @return this instance for chaining.
     */
    public Extension addFieldType(CMAFieldType type) {
      if (fieldTypes == null) {
        fieldTypes = new ArrayList<FieldType>();
      }

      fieldTypes.add(new FieldType().setType(type));
      return this;
    }

    /**
     * @return html source code representing the ui extension.
     */
    public String getSourceContent() {
      return sourceContent;
    }

    /**
     * Updates the html sourcode.
     * <p>
     * Either this one or a link to an extension online needs to be present once sending to
     * contentful.
     * <p>
     * May not be larger then 200kB.
     *
     * @param sourceContent the actual html source code of this ui extension.
     * @return this instance for chaining.
     * @throws IllegalArgumentException if source content is larger then 200kB.
     * @see #setSourceUrl(String)
     */
    public Extension setSourceContent(String sourceContent) {
      if (sourceContent.getBytes().length >= MAXIMUM_BYTE_COUNT) {
        throw new IllegalArgumentException(
            "Source content to big. Please provide less then 200kb of source code or use "
                + "sourceUrl.");
      }
      this.sourceContent = sourceContent;
      return this;
    }

    /**
     * @return a url to the source code.
     */
    public String getSourceUrl() {
      return sourceUrl;
    }

    /**
     * Link to the source code to be executed with this ui extension.
     * <p>
     * Either this one or the direct source code needs to be present once sending to
     * contentful.
     *
     * @param sourceUrl a link to an html page containing the source of the ui extension.
     * @return this instance for chaining.
     * @see #setSourceContent(String)
     */
    public Extension setSourceUrl(String sourceUrl) {
      this.sourceUrl = sourceUrl;
      return this;
    }

    /**
     * @return is this element set to be appearing on the sidebar?
     */
    public boolean isOnSidebar() {
      return onSidebar;
    }

    /**
     * Update whether this element should be appearing on the sidebar.
     *
     * @param available on sidebar or not?
     * @return this instance for chaining.
     */
    public Extension setIsOnSidebar(boolean available) {
      this.onSidebar = available;
      return this;
    }

    /**
     * @return a human readable string, representing the object.
     */
    @Override public String toString() {
      return "Extension { "
          + "fieldTypes = " + getFieldTypes() + ", "
          + "name = " + getName() + ", "
          + "onSidebar = " + isOnSidebar() + ", "
          + "sourceContent = " + getSourceContent() + ", "
          + "sourceUrl = " + getSourceUrl() + " "
          + "}";
    }

    /**
     * Holder of one field type this extension should be used on.
     */
    public static class FieldType {
      CMAFieldType type;

      /**
       * @return the type of the field
       */
      public CMAFieldType getType() {
        return type;
      }

      /**
       * Updates the type of this field with a new one.
       *
       * @param type the new content type field type.
       * @return this instance for chaining.
       */
      public FieldType setType(CMAFieldType type) {
        this.type = type;
        return this;
      }

      /**
       * @return a human readable string, representing the object.
       */
      @Override public String toString() {
        return "FieldType { "
            + "type = " + getType() + " "
            + "}";
      }
    }
  }
}
