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

package com.contentful.java.cma;

/**
 * Library constants.
 */
public final class Constants {
  public static final String ENDPOINT_CMA = "https://api.contentful.com/";
  public static final String ENDPOINT_UPLOAD = "https://upload.contentful.com/";
  // Content Type Header Values
  public static final String DEFAULT_CONTENT_TYPE = "application/vnd.contentful.management.v1+json";

  //BEGIN TO LONG CODE LINES
  public static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";
  public static final String DEFAULT_ENVIRONMENT = "master";

  private Constants() {
    throw new UnsupportedOperationException();
  }
  /**
   * @see <a href="https://www.contentful.com/developers/docs/concepts/data-model/#fields">guide</a>
   */
  //END TO LONG CODE LINES
  @SuppressWarnings("UnusedDeclaration")
  public enum CMAFieldType {
    Array,
    Boolean,
    Date,
    Integer,
    Link,
    Location,
    Number,
    Object,
    Symbol,
    Text,
    RichText
  }
}
