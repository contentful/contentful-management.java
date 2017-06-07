/*
 * Copyright (C) 2017 Contentful GmbH
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

import java.util.LinkedHashMap;

/**
 * Represents a resource of type Entry.
 */
public class CMAEntry extends CMAResource {
  /**
   * Localize all fields with a given locale.
   * <p>
   * Please use {@link CMAEntry#localize(String)} to see how to create an instance.
   */
  public class Localized {
    private final String locale;

    /**
     * Internal method for creating the Localized fields.
     *
     * @param locale the locale, like 'en-US', to be used.
     */
    Localized(String locale) {
      this.locale = locale;
    }

    /**
     * Get a localized field.
     *
     * @param key the key of the value to be returned.
     * @param <T> the type of the value to be returned.
     * @return a value based on the key given here and the locale set earlier.
     */
    public <T> T getField(String key) {
      return CMAEntry.this.getField(key, locale);
    }

    /**
     * Change the value of a localized field.
     *
     * @param key   identifier of the field to be changed.
     * @param value value of the new field.
     * @param <T>   type of the field.
     * @return an instance of the calling {@link Localized} instance, for easy chaining.
     */
    public <T> Localized setField(String key, T value) {
      CMAEntry.this.setField(key, locale, value);
      return this;
    }

    /**
     * @return a human readable string, representing the object.
     */
    @Override public String toString() {
      return "Localized { "
          + "locale = " + locale + ", "
          + "fields = " + fields + " "
          + "}";
    }
  }

  // Map of fields
  LinkedHashMap<String, LinkedHashMap<String, Object>> fields;

  /**
   * Create an entry, filling the system property
   */
  public CMAEntry() {
    super(CMAType.Entry);
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public CMAEntry setSystem(CMASystem system) {
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
  @Override public CMAEntry setId(String id) {
    return super.setId(id);
  }

  /**
   * Convenience: Update the version of this entry without going through {@link #getSystem()}.
   *
   * @param version to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAEntry setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Convenience: Update the space id of this entry without going through {@link #getSystem()}.
   *
   * @param spaceId to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAEntry setSpaceId(String spaceId) {
    return super.setSpaceId(spaceId);
  }

  /**
   * Creates a new field with the given {@code value}.
   * If a field named {@code key} already exists it will be replaced.
   *
   * @param key    field key
   * @param locale locale
   * @param value  value
   * @return this {@code CMAEntry}
   */
  public CMAEntry setField(String key, String locale, Object value) {
    if (fields == null) {
      fields = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
    }

    LinkedHashMap<String, Object> field = fields.get(key);
    if (field == null) {
      field = new LinkedHashMap<String, Object>();
    }

    field.put(locale, value);
    fields.put(key, field);
    return this;
  }

  /**
   * Return a specific localized field.
   *
   * @param key    the key of the field
   * @param locale the locale of the key
   * @param <T>    the type of the return value
   * @return the value requested or null, if something (fields, key, locale) was not found.
   */
  @SuppressWarnings("unchecked")
  public <T> T getField(String key, String locale) {
    if (fields == null) {
      return null;
    }

    LinkedHashMap<String, Object> field = fields.get(key);
    if (field == null) {
      return null;
    } else {
      return (T) field.get(locale);
    }
  }

  /**
   * @return a map of fields for this Entry.
   */
  public LinkedHashMap<String, LinkedHashMap<String, Object>> getFields() {
    return fields;
  }

  /**
   * Sets a map of fields for this Entry.
   *
   * @param fields the fields to be set
   * @return this {@code CMAEntry} instance
   */
  public CMAEntry setFields(LinkedHashMap<String, LinkedHashMap<String, Object>> fields) {
    this.fields = fields;
    return this;
  }

  /**
   * Manipulate fields with the same locale.
   *
   * @param locale the locale to be used for accessing fields.
   * @return an instance of {@link Localized} to be used for setting subsequent fields.
   */
  public Localized localize(String locale) {
    return new Localized(locale);
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAEntry { " + super.toString() + " "
        + "fields = " + getFields() + " "
        + "}";
  }
}
