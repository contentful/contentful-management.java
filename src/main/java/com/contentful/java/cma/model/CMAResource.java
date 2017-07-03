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

import com.google.gson.annotations.SerializedName;

/**
 * Base CMA resource. All `things` you will be retrieving and sending from and to Contentful
 * will be based on this class.
 */
public class CMAResource {

  @SerializedName("sys")
  CMASystem system = new CMASystem();

  public CMAResource(CMAType type) {
    getSystem().setType(type);
  }

  /**
   * @return the system field.
   */
  public CMASystem getSystem() {
    return system;
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public <T extends CMAResource> T setSystem(CMASystem system) {
    this.system = system;
    return (T) this;
  }

  /**
   * @return the {@code sys.id} value, null if it is does not exist.
   */
  public String getId() {
    return getSystem().getId();
  }

  /**
   * Sets the ID for this CMAResource.
   *
   * @param id to be set.
   * @return this {@code CMAResource} instance.
   */
  @SuppressWarnings("unchecked")
  public <T extends CMAResource> T setId(String id) {
    getSystem().setId(id);
    return (T) this;
  }

  /**
   * @return the {@code sys.version} value, null if it does not exist.
   */
  public Integer getVersion() {
    return getSystem().getVersion();
  }

  /**
   * Convenience method for setting a version.
   *
   * @param version the version number to be set.
   * @param <T>     the type of the CMAResource calling.
   * @return the calling {@link CMAResource} for chaining.
   */
  @SuppressWarnings("unchecked")
  public <T extends CMAResource> T setVersion(Integer version) {
    getSystem().setVersion(version);
    return (T) this;
  }

  /**
   * @return the ID of the Space associated with this resource, null if it does not exist.
   */
  public String getSpaceId() {
    final CMALink space = getSystem().getSpace();
    if (space != null) {
      return space.getSystem().getId();
    }
    return null;
  }

  /**
   * Convenience method for setting a space id.
   *
   * @param spaceId the id to be set.
   * @param <T>     An implementation of CMAResource, normally used for chaining setter methods.
   * @return the calling {@link CMAResource} for chaining.
   */
  @SuppressWarnings("unchecked")
  public <T extends CMAResource> T setSpaceId(String spaceId) {
    if (getSystem().getSpace() == null) {
      getSystem().space = new CMALink(CMAType.Space);
    }

    getSystem().space.setId(spaceId);

    return (T) this;
  }

  /**
   * @return true if this resource is archived.
   */
  public Boolean isArchived() {
    return system != null && system.getArchivedVersion() != null;
  }

  /**
   * @return true if this resource is published.
   */
  public Boolean isPublished() {
    return system != null && system.getPublishedVersion() != null;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAResource { "
        + "system = " + getSystem() + " "
        + "}";
  }
}