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

/**
 * Represents a resource of type environment.
 */
public class CMAEnvironment extends CMAResource {
  // Name
  String name;

  /**
   * Create a environment using the default types for the system property.
   */
  public CMAEnvironment() {
    super(CMAType.Environment);
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public CMAEnvironment setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  /**
   * @return the name of this environment.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name for this environment.
   *
   * @param name the name of the environment to be set
   * @return this {@code CMAEnvironment} instance.
   */
  public CMAEnvironment setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the id of this environment.
   *
   * @param id to be set.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override public CMAEnvironment setId(String id) {
    return super.setId(id);
  }

  /**
   * Override retrieval of the environment id here, since the id of this environment is it's id.
   *
   * @return the ID of the Environment associated with this resource,
   * {@link com.contentful.java.cma.Constants#DEFAULT_ENVIRONMENT} if it does not exist.
   */
  @Override public String getEnvironmentId() {
    return getId();
  }

  /**
   * Convenience method for setting an environment id.
   *
   * @param environmentId the id to be set.
   * @param <T>           An implementation of CMAResource, normally used for chaining setter
   *                      methods.
   * @return the calling {@link CMAResource} for chaining.
   */
  @Override @SuppressWarnings("unchecked")
  public <T extends CMAResource> T setEnvironmentId(String environmentId) {
    return (T) setId(environmentId);
  }

  /**
   * Set the version of this environment.
   *
   * @param version the version to be set.
   * @return this environment instance for chaining.
   */
  @SuppressWarnings("unchecked") @Override
  public CMAEnvironment setVersion(Integer version) {
    return super.setVersion(version);
  }

  /**
   * Return the state of this environment
   *
   * @return one of {@link CMAEnvironmentStatus} to indicate the status of the environment.
   */
  public CMAEnvironmentStatus getStatus() {
    final CMALink link = system.getEnvironmentalStatus();
    if (link == null) {
      return null;
    }

    final String id = link.getId().toLowerCase();

    for (final CMAEnvironmentStatus status : CMAEnvironmentStatus.values()) {
      final String statusName = status.name().toLowerCase();
      if (statusName.equals(id)) {
        return status;
      }
    }

    return null;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAEnvironment { " + super.toString() + " "
        + "name = " + getName() + " "
        + "}";
  }
}
