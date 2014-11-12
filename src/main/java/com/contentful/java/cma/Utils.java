package com.contentful.java.cma;

import java.io.IOException;
import java.util.Properties;

/**
 * SDK Utilities.
 */
class Utils {
  private Utils() {
  }

  static final String SDK_PROPERTIES = "sdk.properties";
  static final String PROP_VERSION_NAME = "version.name";

  static String getFromProperties(String field) throws IOException {
    Properties properties = new Properties();
    properties.load(Utils.class.getClassLoader().getResourceAsStream(SDK_PROPERTIES));
    return properties.getProperty(field);
  }
}
