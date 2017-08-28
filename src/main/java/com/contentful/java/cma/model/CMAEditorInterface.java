package com.contentful.java.cma.model;

import com.contentful.java.cma.ModuleEditorInterfaces;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This model can be used to control the appearance and usability of ui elements on Contentful.
 */
public class CMAEditorInterface extends CMAResource {

  List<Control> controls;

  /**
   * This method only exists for compatibility reasons.
   * <p>
   * You cannot create and upload new editor interfaces to Contentful. Please consider updating
   * an existing one by fetching it first and then using
   * {@link ModuleEditorInterfaces#update(CMAEditorInterface)} on it.
   */
  public CMAEditorInterface() {
    super(CMAType.EditorInterface);
  }

  /**
   * Adds a new control to the list of controls.
   * <p>
   * It might create a new list of controls, if no list is created.
   *
   * @param control to be added to the list.
   * @return this instance for chaining.
   */
  public CMAEditorInterface addControl(Control control) {
    if (controls == null) {
      controls = new ArrayList<Control>();
    }

    controls.add(control);
    return this;
  }

  /**
   * @return the list of all controls.
   */
  public List<Control> getControls() {
    return controls;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAEditorInterface { " + super.toString() + " "
        + "controls = " + getControls() + " "
        + "}";
  }

  /**
   * This class represents one control of the editor interface.
   */
  public static class Control {

    private static final String SETTINGS_KEY_HELP_TEXT = "helpText";
    String fieldId;
    String widgetId;
    Map<String, String> settings;

    /**
     * @return the field id this control applies to.
     */
    public String getFieldId() {
      return fieldId;
    }

    /**
     * Change the id of the field for this control.
     *
     * @param fieldId new id to be set.
     * @return this instance for chaining.
     */
    public Control setFieldId(String fieldId) {
      this.fieldId = fieldId;
      return this;
    }

    /**
     * @return the current set build in widget or null if a custom ui extension is used.
     */
    public BuildInWidgetId getBuildInWidgetId() {
      for (final BuildInWidgetId id : BuildInWidgetId.values()) {
        if (id.name().toLowerCase().equals(widgetId.toLowerCase())) {
          return id;
        }
      }

      return null;
    }

    /**
     * Use a build in widget.
     * <p>
     * For custom ids, as in for using a custom ui extension widgets, please take a look at
     * {@link Control#setWidgetId(String)}.
     *
     * @param buildInWidgetId an id pointing to a build in widget.
     * @return this instance for chaining.
     */
    public Control setBuildInWidgetId(BuildInWidgetId buildInWidgetId) {
      this.widgetId = buildInWidgetId.name();
      return this;
    }

    /**
     * @return the current widget id as a string.
     * @see CMAEditorInterface.Control.BuildInWidgetId
     * @see CMAEditorInterface.Control#setBuildInWidgetId(BuildInWidgetId)
     */
    public String getWidgetId() {
      return widgetId;
    }

    /**
     * Set the id of the widget.
     * <p>
     * Use this setter if you want to use a custom ui extension widget, and take a look at
     * {@link #setBuildInWidgetId(BuildInWidgetId)} if you want to select a build in editor
     * widget.
     *
     * @param widgetId the new widget id
     * @return this instance for chaining.
     * @see CMAEditorInterface.Control.BuildInWidgetId
     * @see CMAEditorInterface.Control#setBuildInWidgetId(BuildInWidgetId)
     */
    public Control setWidgetId(String widgetId) {
      this.widgetId = widgetId;
      return this;
    }

    /**
     * Adds a new custom setting to the settings.
     * <p>
     * Side effect: might create a new map, if no map was created before.
     *
     * @param key   the settings key to be added.
     * @param value the value of the key to be added.
     * @return this instance for chaining.
     */
    public Control addSetting(String key, String value) {
      if (settings == null) {
        settings = new LinkedHashMap<String, String>();
      }

      settings.put(key, value);
      return this;
    }

    /**
     * Adds a new custom help text to the settings.
     * <p>
     * Side effect: might create a new map, if no map was created before.
     *
     * @param helpText display this text to the user.
     * @return this instance for chaining.
     */
    public Control addHelpText(String helpText) {
      if (settings == null) {
        settings = new LinkedHashMap<String, String>();
      }

      settings.put(SETTINGS_KEY_HELP_TEXT, helpText);
      return this;
    }

    /**
     * @return the set help text or null if none is set.
     */
    public String getHelpText() {
      return settings != null ? settings.get(SETTINGS_KEY_HELP_TEXT) : null;
    }

    /**
     * @return a list of currently set settings, or null if none is set.
     */
    public Map<String, String> getSettings() {
      return settings;
    }

    /**
     * @return a human readable string, representing the object.
     */
    @Override public String toString() {
      return "Control { "
          + "fieldId = " + getFieldId() + ", "
          + "settings = " + getSettings() + ", "
          + "widgetId = " + getWidgetId() + " "
          + "}";
    }

    /**
     * Enum holding the build in values of a widget id.
     * <p>
     * For custom ids, as for using a custom UI extension, please take a look at
     * {@link #setWidgetId(String)}
     */
    public enum BuildInWidgetId {
      @SerializedName("assetGalleryEditor") AssetGalleryEditor,
      @SerializedName("assetLinkEditor") AssetLinkEditor,
      @SerializedName("assetLinksEditor") AssetLinksEditor,
      @SerializedName("boolean") Boolean,
      @SerializedName("checkbox") Checkbox,
      @SerializedName("datePicker") DatePicker,
      @SerializedName("dropdown") Dropdown,
      @SerializedName("entryCardEditor") EntryCardEditor,
      @SerializedName("entryCardsEditor") EntryCardsEditor,
      @SerializedName("entryLinkEditor") EntryLinkEditor,
      @SerializedName("entryLinksEditor") EntryLinksEditor,
      @SerializedName("kalturaEditor") KalturaEditor,
      @SerializedName("kalturaMultiVideoEditor") KalturaMultiVideoEditor,
      @SerializedName("listInput") ListInput,
      @SerializedName("locationEditor") LocationEditor,
      @SerializedName("markdown") Markdown,
      @SerializedName("multipleLine") MultipleLine,
      @SerializedName("numberEditor") NumberEditor,
      @SerializedName("objectEditor") ObjectEditor,
      @SerializedName("ooyalaEditor") OoyalaEditor,
      @SerializedName("ooyalaMultiAssetEditor") OoyalaMultiAssetEditor,
      @SerializedName("radio") Radio,
      @SerializedName("rating") Rating,
      @SerializedName("singleLine") SingleLine,
      @SerializedName("slugEditor") SlugEditor,
      @SerializedName("tagEditor") TagEditor,
      @SerializedName("urlEditor") UrlEditor
    }
  }
}