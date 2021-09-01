package com.contentful.java.cma;

import java.util.HashMap;
import java.util.Map;

/**
 * Internal class for storing default http query parameter.
 */
class DefaultQueryParameter {

  /**
   * Use these parameter for every fetch, unless overwritten by user.
   * <p>
   * Right now set the limit to 100.
   */
  static final HashMap<String, String> FETCH = new HashMap<String, String>();

  static {
    FETCH.put("limit", "100");
  }

  /**
   * Do not initiate this class. Its only used in static contexts.
   *
   * @throws UnsupportedOperationException since it should not be used.
   */
  private DefaultQueryParameter() {
    throw new UnsupportedOperationException("Do not create instance from this class!");
  }

  /**
   * Update a given map with some default values if not already present in map.
   *
   * @param target   the map to be filled with values, if a key for them is not set already.
   * @param defaults the list of defaults, to be set.
   * @return the same map if no change had to be made, a new map otherwise.
   */
  static Map<String, String> putIfNotSet(Map<String, String> target, Map<String, String> defaults) {
    boolean needsChange = defaults.keySet().stream().anyMatch(key -> !target.containsKey(key));
    if (needsChange) {
      Map<String, String> copy = new HashMap<>(target);
      for (final String key : defaults.keySet()) {
        if (!copy.containsKey(key)) {
          copy.put(key, defaults.get(key));
        }
      }
      return copy;
    }
    return target;
  }
}
