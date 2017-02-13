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

import java.util.HashMap;
import java.util.Map;

/**
 * Base CMA resource.
 */
public class CMAResource {
  // System attributes
  HashMap<String, Object> sys;

  /**
   * Sets the ID for this CMAResource.
   * @param id to be set
   * @return this {@code CMAResource} instance
   */
  @SuppressWarnings("unchecked")
  public <T extends CMAResource> T setId(String id) {
    if (sys == null) {
      sys = new HashMap();
    }
    sys.put("id", id);
    return (T) this;
  }

  /**
   * @return the {@code sys.version} value, null if it does not exist.
   */
  public Integer getVersion() {
    if (sys != null) {
      Double value = (Double) sys.get("version");
      if (value != null) {
        return value.intValue();
      }
    }
    return null;
  }

  /**
   * @return the {@code sys.id} value, null if it is does not exist.
   */
  public String getResourceId() {
    return getSysAttribute("id");
  }

  /**
   * @return the ID of the Space associated with this resource, null if it does not exist.
   */
  public String getSpaceId() {
    Map space = getSysAttribute("space");
    if (space != null) {
      Map spaceSys = (Map) space.get("sys");
      if (spaceSys != null) {
        return (String) spaceSys.get("id");
      }
    }
    return null;
  }

  /**
   * @return the value of the system attribute {@code attr}, null if it does not exist.
   */
  @SuppressWarnings("unchecked") <T> T getSysAttribute(String attr) {
    if (sys != null) {
      return (T) sys.get(attr);
    }
    return null;
  }

  /**
   * @return a map of system attributes.
   */
  public HashMap<String, Object> getSys() {
    return sys;
  }

  /**
   * Sets a map of system attributes.
   *
   * @param sys sets the sys hash.
   */
  public void setSys(HashMap<String, Object> sys) {
    this.sys = sys;
  }
}