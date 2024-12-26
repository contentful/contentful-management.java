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

import java.util.LinkedHashMap;

/**
 * Represents a resource of type Asset.
 */
public class CMAAsset extends CMAResource {
  // Map of fields
  Fields fields = new Fields();
  public CMAMetadata metadata;

  /**
   * Create a new asset, setting the system's type field.
   */
  public CMAAsset() {
    super(CMAType.Asset);
  }

  /**
   * @return fields for this asset.
   */
  public Fields getFields() {
    return fields;
  }

  /**
   * Sets new fields for this Asset.
   *
   * @param fields fields to overwrite the current set of fields.
   * @return this {@code CMAAsset} instance
   */
  public CMAAsset setFields(Fields fields) {
    this.fields = fields;
    return this;
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAAsset setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  /* Gets the metadata for this asset.
   *
   * @return The {@link CMAMetadata} instance containing metadata like tag.
   */
  public CMAMetadata getMetadata() {
    return metadata;
  }
  /**
   * Sets the metadata for this asset.
   *
   * @param metadata The {@link CMAMetadata} instance to associate with this asset.
   * @return This {@code CMAAsset} instance for method chaining.
   */
  public CMAAsset setMetadata(CMAMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Convenience: Update the id of this entry without going through {@link #getSystem()}.
   *
   * @param id to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAAsset setId(String id) {
    return super.setId(id);
  }

  /**
   * Convenience for getting the version of this resource.
   *
   * @return the calling instance for chaining.
   */
  @Override public Integer getVersion() {
    return super.getVersion();
  }

  /**
   * Convenience: Update the version of this entry without going through {@link #getSystem()}.
   *
   * @param version to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAAsset setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Convenience: Update the space id of this entry without going through {@link #getSystem()}.
   *
   * @param spaceId to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAAsset setSpaceId(String spaceId) {
    return super.setSpaceId(spaceId);
  }

  /**
   * Convenience method for setting an environment id.
   *
   * @param environmentId the id to be set.
   * @return the calling {@link CMAResource} for chaining.
   */
  @SuppressWarnings("unchecked")
  public CMAAsset setEnvironmentId(String environmentId) {
    return super.setEnvironmentId(environmentId);
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAAsset {" + super.toString()
        + " fields = " + getFields()
        + " metadata = " + getMetadata()
        + "}";
  }

  /**
   * Collect all fields of an asset.
   */
  public static class Fields {
    LinkedHashMap<String, String> title;
    LinkedHashMap<String, String> description;
    LinkedHashMap<String, CMAAssetFile> file;

    /**
     * Retrieve the description to the given locale.
     *
     * @param locale which locale should the description be in?
     * @return a string representing the description in the given locale.
     */
    public String getDescription(String locale) {
      if (description == null) {
        return null;
      }

      return description.get(locale);
    }

    /**
     * Set the description in a specific locale.
     *
     * @param locale      the locale to be used. Think 'en-US'.
     * @param description the text of the description, localized into the locale.
     * @return the {@link Fields} instance calling, used for chaining.
     */
    public Fields setDescription(String locale, String description) {
      if (this.description == null) {
        this.description = new LinkedHashMap<String, String>();
      }

      this.description.put(locale, description);
      return this;
    }

    /**
     * What is the title of this asset in a given locale?
     *
     * @param locale the locale to check for a title
     * @return the localized title, or null if no entry for this locale was found.
     */
    public String getTitle(String locale) {
      if (title == null) {
        return null;
      }

      return title.get(locale);
    }

    /**
     * Update or set title in a given locale.
     * <p>
     * If a title in the given locale is present, update it, otherwise create a new
     * one.
     *
     * @param locale which locale to be used.
     * @param title  the title corresponding to this locale.
     * @return The same {@link Fields} instance used to calling other setters for chaining.
     */
    public Fields setTitle(String locale, String title) {
      if (this.title == null) {
        this.title = new LinkedHashMap<String, String>();
      }

      this.title.put(locale, title);
      return this;
    }

    /**
     * Return the localized file of this asset.
     * <p>
     * Returns  {@link CMAAssetFile} for the given locale.
     *
     * @param locale which locale should be used to select the right file?
     * @return the CMAAssetFile listed under the given locale.
     */
    public CMAAssetFile getFile(String locale) {
      if (file == null) {
        return null;
      }

      return file.get(locale);
    }

    /**
     * Update or create a new file for this asset.
     *
     * @param locale which locale to be used?
     * @param file   the actual file to be set.
     * @return For chaining, return the same instance getting called with.
     */
    public Fields setFile(String locale, CMAAssetFile file) {
      if (this.file == null) {
        this.file = new LinkedHashMap<String, CMAAssetFile>();
      }

      this.file.put(locale, file);
      return this;
    }

    /**
     * Localize access to fields by the same locale.
     * <p>
     * Use this to not keep repeating the same locale for fields.
     *
     * @param locale a code of a locale. Think `en-US`.
     * @return a visitor to localize all upcoming field access.
     */
    public Localized localize(String locale) {
      return new Localized(locale);
    }

    /**
     * @param locale the locale to be used for converting.
     * @return a human readable string, representing the object.
     */
    public String toString(String locale) {
      return "CMAAsset.Fields {"
          + "description = " + getDescription(locale) + ", "
          + "file = " + getFile(locale) + ", "
          + "title = " + getTitle(locale)
          + "}";
    }

    /**
     * For debugging, it returns only the en-US locales!
     *
     * @return a human readable string, representing the object.
     * @see #toString(String)
     */
    @Override public String toString() {
      return toString("en-US");
    }

    /**
     * Localize all fields with a given locale.
     * <p>
     * Please use {@link Fields#localize(String)} to see how to create an instance.
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
       * @return a localized version of {@link Fields#getDescription(String)}
       */
      public String getDescription() {
        return Fields.this.getDescription(locale);
      }

      /**
       * Change the localized version of the description.
       *
       * @param value the description to be set.
       * @return the Localized Fields instance calling this method.
       * @see Fields#setDescription(String, String)
       */
      public Localized setDescription(String value) {
        Fields.this.setDescription(locale, value);
        return this;
      }

      /**
       * @return a localized version of {@link Fields#getTitle(String)}
       */
      public String getTitle() {
        return Fields.this.getTitle(locale);
      }

      /**
       * Change the localized version of the title.
       *
       * @param value the title to be set.
       * @return the Localized Fields instance calling this method.
       * @see Fields#setTitle(String, String)
       */
      public Localized setTitle(String value) {
        Fields.this.setTitle(locale, value);
        return this;
      }

      /**
       * @return a localized version of {@link Fields#getFile(String)}
       */
      public CMAAssetFile getFile() {
        return Fields.this.getFile(locale);
      }

      /**
       * Change the localized version of the file.
       *
       * @param value the file to be set.
       * @return the Localized Fields instance calling this method.
       * @see Fields#setFile(String, CMAAssetFile)
       */
      public Localized setFile(CMAAssetFile value) {
        Fields.this.setFile(locale, value);
        return this;
      }

      /**
       * @return a human readable string, representing the object.
       */
      @Override public String toString() {
        return "Localized {"
            + "locale = " + locale + ", "
            + "description = " + getDescription() + ", "
            + "file = " + getFile() + ", "
            + "title = " + getTitle() + " "
            + "}";
      }
    }
  }
}
