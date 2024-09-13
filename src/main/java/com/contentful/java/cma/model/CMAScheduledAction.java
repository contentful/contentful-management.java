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
 * Represents model for scheduled action.
 */
public class CMAScheduledAction extends CMAResource {
  CMALink entity;
  CMALink environment;

  CMAScheduledFor scheduledFor;

  String action;

  public CMAScheduledAction(CMAType type) {
    super(type);
  }

  public CMALink getEntity() {
    return entity;
  }

  public void setEntity(CMALink entity) {
    this.entity = entity;
  }

  public CMALink getEnvironment() {
    return environment;
  }

  public void setEnvironment(CMALink environment) {
    this.environment = environment;
  }

  public CMAScheduledFor getScheduledFor() {
    return scheduledFor;
  }

  public void setScheduledFor(CMAScheduledFor scheduledFor) {
    this.scheduledFor = scheduledFor;
  }


  public String getAction() {
    return action;
  }

  public CMAScheduledAction setAction(String action) {
    this.action = action;
    return this;
  }

  @SuppressWarnings("unchecked")
  public CMAScheduledAction setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  @Override public String toString() {
    return "CMAScheduledAction { " + super.toString() + " "
        + "action = " + getAction() + " "
        + "}";
  }
}
