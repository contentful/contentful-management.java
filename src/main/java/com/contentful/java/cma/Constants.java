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

/**
 * Library constants.
 */
public final class Constants {
  private Constants() {
    throw new UnsupportedOperationException();
  }

  public static final String ENDPOINT_CMA = "https://api.contentful.com";

  @SuppressWarnings("UnusedDeclaration")
  public static enum CMAFieldType {
    Array,
    Boolean,
    Date,
    Integer,
    Link,
    Location,
    Number,
    Object,
    Symbol,
    Text
  }

  // Properties
  static final String SDK_PROPERTIES = "sdk.properties";
  static final String PROP_VERSION_NAME = "version.name";
}
