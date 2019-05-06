package com.contentful.java.cma.model;

import com.contentful.java.cma.gson.OptionsJsonDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Model class to describe an ui extension parameter.
 */
public class CMAUiExtensionParameter {
  String id;

  String name;

  String description;

  CMAUiExtensionParameterType type;

  boolean required;

  @SerializedName("default")
  String defaultValue;

  @JsonAdapter(OptionsJsonDeserializer.class)
  private Map<String, String> options;

  private Map<String, String> labels;

  /**
   * @return the {@code id} attribute of this parameter.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the id for this parameter.
   *
   * @param id the id to be set
   * @return this {@code CMAUiExtensionParameter} instance
   */
  public CMAUiExtensionParameter setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * @return the {@code name} attribute of this parameter.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name for this parameter.
   *
   * @param name the name to be set
   * @return this {@code CMAUiExtensionParameter} instance
   */
  public CMAUiExtensionParameter setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return the {@code name} description of this parameter.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the description of this parameter.
   *
   * @param description the description of this parameter.
   * @return this {@code CMAUiExtensionParameter} instance
   */
  public CMAUiExtensionParameter setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * @return the {@code type} attribute of this parameter.
   */
  public CMAUiExtensionParameterType getType() {
    return type;
  }

  /**
   * Sets the type for this parameter.
   *
   * @param type the type to be set
   * @return this {@code CMAUiExtensionParameter} instance
   */
  public CMAUiExtensionParameter setType(CMAUiExtensionParameterType type) {
    this.type = type;
    return this;
  }

  /**
   * @return the {@code required} attribute of this parameter.
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Sets the {@code required} attribute value.
   *
   * @param required boolean indicating whether or not this parameter is required
   * @return this {@code CMAUiExtensionParameter} instance
   */
  public CMAUiExtensionParameter setRequired(boolean required) {
    this.required = required;
    return this;
  }

  /**
   * @return the {@code defaultValue} description of this parameter.
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Set the default value of this parameter.
   *
   * @param defaultValue the default value of this parameter.
   * @return this {@code CMAUiExtensionParameter} instance
   */
  public CMAUiExtensionParameter setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
   * @return the options of this parameter
   */
  public HashMap<String, String> getOptions() {
    return options;
  }

  /**
   * Add an option.
   *
   * @param key
   * @param value
   * @return this {@code CMAUiExtensionParameter} instance
   */
  public CMAUiExtensionParameter addOption(String key, String value) {
    if (options == null) {
      options = new HashMap<>();
    }
    options.put(key, value);
    return this;
  }

  /**
   * @return the options of this parameter
   */
  public HashMap<String, String> getLabels() {
    return labels;
  }

  /**
   * Add a label.
   *
   * @param key
   * @param value
   * @return this {@code CMAUiExtensionParameter} instance
   */
  public CMAUiExtensionParameter addLabel(String key, String value) {
    if (labels == null) {
      labels = new HashMap<>();
    }
    labels.put(key, value);
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAUiExtensionParameter { "
      + "id = " + getId() + ", "
      + "name = " + getName() + ", "
      + "description = " + getDescription() + ", "
      + "type = " + getType() + ", "
      + "required = " + isRequired() + ", "
      + "default = " + getDefaultValue() + ", "
      + "options = " + getOptions() + ", "
      + "labels = " + getLabels() + " "
      + "}";
  }
}
