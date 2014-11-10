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
