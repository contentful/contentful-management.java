/*
 * Copyright (C) 2014 Contentful GmbH
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

package com.contentful.java.cma;

import java.util.HashMap;
import java.util.Map;

/**
 * CMAResource.
 */
public class CMAResource {
  HashMap<String, Object> sys;

  @SuppressWarnings("unchecked")
  public CMAResource setId(String id) {
    if (sys == null) {
      sys = new HashMap();
    }
    sys.put("id", id);
    return this;
  }

  public Integer getVersion() {
    if (sys != null) {
      Double value = (Double) sys.get("version");
      if (value != null) {
        return value.intValue();
      }
    }
    return null;
  }

  public String getResourceId() {
    return getSysAttribute("id");
  }

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

  @SuppressWarnings("unchecked")
  <T> T getSysAttribute(String attr) {
    if (sys != null) {
      return (T) sys.get(attr);
    }
    return null;
  }
}
