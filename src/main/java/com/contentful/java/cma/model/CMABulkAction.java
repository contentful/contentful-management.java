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
public class CMABulkAction extends CMAResource {
  String action;
  CMAError error;
  CMAPayload payload;

  public CMABulkAction() {
    super(CMAType.Environment);
  }

  public String getAction() {
    return action;
  }

  public CMABulkAction setAction(String action) {
    this.action = action;
    return this;
  }

  public CMAError getError() {
    return error;
  }

  public void setError(CMAError error) {
    this.error = error;
  }

  public CMAPayload getPayload() {
    return payload;
  }

  public CMABulkAction setPayload(CMAPayload payload) {
    this.payload = payload;
    return this;
  }

  @SuppressWarnings("unchecked")
  public CMABulkAction setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  @Override public String toString() {
    return "CMAEnvironment { " + super.toString() + " "
        + "action = " + getAction() + " "
        + "}";
  }
}
