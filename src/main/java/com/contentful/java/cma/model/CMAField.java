/*
 * Copyright (C) 2019 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.contentful.java.cma.Constants.CMAFieldType;
import com.google.gson.annotations.SerializedName;

/**
 * CMAField.
 */
public class CMAField {
  // ID
  String id;

  // Name
  String name;

  // Type
  CMAFieldType type;

  // Link Type
  String linkType;

  // Validations
  List<Map<String, Object>> validations;

  // Default value
  protected Map<String, Object> defaultValue;

  // Allowed resources
  List<Map<String, Object>> allowedResources;

  // Array Items
  @SerializedName("items")
  HashMap<String, Object> arrayItems;

  // Required
  boolean required;

  // Disabled
  boolean disabled;

  // Omitted
  boolean omitted;

  // Localized
  boolean localized;

  /**
   * Sets the {@code disabled} attribute value.
   *
   * @param disabled boolean indicating whether or not this field is disabled
   *                 Returns this {@code CMAField} instance
   * @return this {@code CMAField} instance
   */
  public CMAField setDisabled(boolean disabled) {
    this.disabled = disabled;
    return this;
  }

  /**
   * Sets the {@code omitted} attribute value.
   *
   * @param omitted boolean indicating whether or not this field is complete omitted
   *                Returns this {@code CMAField} instance
   * @return this {@code CMAField} instance
   */
  public CMAField setOmitted(boolean omitted) {
    this.omitted = omitted;
    return this;
  }

  /**
   * @return the {@code id} attribute of this field.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the ID for this field.
   *
   * @param id the id to be set
   * @return this {@code CMAField} instance
   */
  public CMAField setId(String id) {
    this.id = id;
    return this;
  }
  /**
   * @return the {@code defaultValue} attribute value as a {@code Map}.
   */
  public Map<String, Object> getDefaultValue() {
    return defaultValue;
  }
  /**
   * Sets the default value.
   *
   * @param defaultValue the defaultValue to be set
   * @return this {@code CMAField} instance
   */
  public CMAField setDefaultValue(Map<String, Object> defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
   * @return the {@code name} attribute of this field.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name for this field.
   *
   * @param name the name to be set
   * @return this {@code CMAField} instance
   */
  public CMAField setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return the {@code type} attribute of this field.
   */
  public CMAFieldType getType() {
    return type;
  }

  /**
   * Sets the type for this field.
   *
   * @param type the type to be set
   * @return this {@code CMAField} instance
   */
  public CMAField setType(CMAFieldType type) {
    this.type = type;
    return this;
  }

  /**
   * @return the {@code linkType} attribute of this field.
   */
  public String getLinkType() {
    return linkType;
  }

  /**
   * Sets the link type for this field.
   *
   * @param linkType the type of link to be set
   * @return this {@code CMAField} instance
   */
  public CMAField setLinkType(String linkType) {
    this.linkType = linkType;
    return this;
  }

  /**
   * @return a {@code List} of validation rules for this field.
   */
  public List<Map<String, Object>> getValidations() {
    return validations;
  }

  /**
   * Sets a {@code List} of validation rules for this field.
   *
   * @param validations validations list
   *                    Returns this {@code CMAField} instance
   * @return this {@code CMAField} instance
   */
  public CMAField setValidations(List<Map<String, Object>> validations) {
    this.validations = validations;
    return this;
  }

  /**
   * @return a {@code List} of allowed resources.
   */
  public List<Map<String, Object>> getAllowedResources() {
    return allowedResources;
  }

  /**
   * Sets a {@code List} of allowed resources for this field.
   *
   * @param allowedResources allowed resources list
   *                    Returns this {@code CMAField} instance
   * @return this {@code CMAField} instance
   */

  public CMAField setAllowedResources(List<Map<String, Object>> allowedResources) {
    this.allowedResources = allowedResources;
    return this;
  }


  /**
   * @return the {@code items} attribute value as a {@code Map}.
   */
  public Map<String, Object> getArrayItems() {
    return arrayItems;
  }

  /**
   * Sets the {@code items} attribute value.
   *
   * @param arrayItems Map instance
   * @return this {@code CMAField} instance
   */
  public CMAField setArrayItems(Map<String, Object> arrayItems) {
    this.arrayItems = new HashMap<>(arrayItems);
    return this;
  }

  /**
   * @return the {@code required} attribute of this field.
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Sets the {@code required} attribute value.
   *
   * @param required boolean indicating whether or not this field is required
   *                 Returns this {@code CMAField} instance
   * @return this {@code CMAField} instance
   */
  public CMAField setRequired(boolean required) {
    this.required = required;
    return this;
  }

  /**
   * @return the {@code disabled} attribute of this field.
   */
  public Boolean isDisabled() {
    return disabled;
  }

  /**
   * @return the {@code omitted} attribute of this field.
   */
  public Boolean isOmitted() {
    return omitted;
  }

  /**
   * @return the {@code localized} attribute of this field.
   */
  public boolean isLocalized() {
    return localized;
  }

  /**
   * Sets the {@code localized} attribute value.
   *
   * @param localized boolean indicating whether or not this field is localized
   *                  Returns this {@code CMAField} instance
   * @return this {@code CMAField} instance
   */
  public CMAField setLocalized(boolean localized) {
    this.localized = localized;
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAField { "
        + "arrayItems = " + getArrayItems() + ", "
        + "disabled = " + isDisabled() + ", "
        + "id = " + getId() + ", "
        + "linkType = " + getLinkType() + ", "
        + "localized = " + isLocalized() + ", "
        + "name = " + getName() + ", "
        + "omitted = " + isOmitted() + ", "
        + "required = " + isRequired() + ", "
        + "type = " + getType() + ", "
        + "validations = " + getValidations() + " "
        + "}";
  }
}
