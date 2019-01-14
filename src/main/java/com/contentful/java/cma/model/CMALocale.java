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

import com.google.gson.annotations.SerializedName;

/**
 * CMALocale.
 * <p>
 * This class will be used to represent a locale on the CMA SDK.
 */
public class CMALocale extends CMAResource {
  String name;
  String code;
  String fallbackCode;
  boolean optional;

  @SerializedName("default")
  boolean isDefault;

  boolean contentManagementApi;
  boolean contentDeliveryApi;

  /**
   * Create a new locale, specifying the type in the system property.
   */
  public CMALocale() {
    super(CMAType.Locale);
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public CMALocale setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  /**
   * Convenience: Update the id of this entry without going through {@link #getSystem()}.
   *
   * @param id to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMALocale setId(String id) {
    return super.setId(id);
  }

  /**
   * Convenience: Update the version of this entry without going through {@link #getSystem()}.
   *
   * @param version to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMALocale setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Convenience: Update the space id of this entry without going through {@link #getSystem()}.
   *
   * @param spaceId to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMALocale setSpaceId(String spaceId) {
    return super.setSpaceId(spaceId);
  }

  /**
   * What is the human readable name of this locale?
   *
   * @return the locale's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Change or create a human readable name of this locale.
   *
   * @param name name of the locale to be set.
   * @return the instance calling this method for ease of chaining.
   */
  public CMALocale setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get the code of this locale.
   * <p>
   * This code will be one of IETF specification.
   *
   * @return the code of the locale.
   * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF</a>
   */
  public String getCode() {
    return code;
  }

  /**
   * Set the code of the locale.
   *
   * @param code IETF code to be set.
   * @return this instance for chaining.
   * @see #getCode()
   */
  public CMALocale setCode(String code) {
    this.code = code;
    return this;
  }

  /**
   * @return the current fallback locale to be used once a localized field is not set in CDA.
   */
  public String getFallbackCode() {
    return fallbackCode;
  }

  /**
   * Set the fallback locale to be used once a localized field is not set in CDA.
   *
   * @param fallbackCode an IETF code to represent the fallback.
   * @return this intance for ease of chaining.
   */
  public CMALocale setFallbackCode(String fallbackCode) {
    this.fallbackCode = fallbackCode;
    return this;
  }

  /**
   * @return true when this locale is optional, aka, can contain null fields.
   */
  public boolean isOptional() {
    return optional;
  }

  /**
   * Control whether this locale is optional.
   *
   * @param optional a flag indicating optionality status of this locale.
   * @return this instance to ease chaining.
   */
  public CMALocale setOptional(boolean optional) {
    this.optional = optional;
    return this;
  }

  /**
   * @return whether this is the default locale.
   */
  public boolean isDefault() {
    return isDefault;
  }

  /**
   * Change this locale to be the default, or remove this flag from it.
   *
   * @param isDefault set this to true, if you want this locale to be the default one.
   * @return this instance for ease of chaining.
   *
   * @deprecated Setting this to default will not get send to the backend. This method will get
   * removed in the next major release.
   */
  @Deprecated
  public CMALocale setDefault(boolean isDefault) {
    this.isDefault = isDefault;
    return this;
  }

  /**
   * @return true if this locale is used on the content management api.
   */
  public boolean isContentManagementApi() {
    return contentManagementApi;
  }

  /**
   * Change this locale to be the  used on the management api.
   *
   * @param contentManagementApi to update the value with.
   * @return this instance for ease of chaining.
   */
  public CMALocale setContentManagementApi(boolean contentManagementApi) {
    this.contentManagementApi = contentManagementApi;
    return this;
  }

  /**
   * @return true if this locale is used on the content delivery api.
   */
  public boolean isContentDeliveryApi() {
    return contentDeliveryApi;
  }

  /**
   * Change this locale to be available in the content deliver api.
   *
   * @param contentDeliveryApi true if available.
   * @return this instance for ease of chaining.
   */
  public CMALocale setContentDeliveryApi(boolean contentDeliveryApi) {
    this.contentDeliveryApi = contentDeliveryApi;
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMALocale { " + super.toString() + " "
        + "code = " + getCode() + ", "
        + "contentDeliveryApi = " + isContentDeliveryApi() + ", "
        + "contentManagementApi = " + isContentManagementApi() + ", "
        + "fallbackCode = " + getFallbackCode() + ", "
        + "isDefault = " + isDefault() + ", "
        + "name = " + getName() + ", "
        + "optional = " + isOptional() + " "
        + "}";
  }
}

