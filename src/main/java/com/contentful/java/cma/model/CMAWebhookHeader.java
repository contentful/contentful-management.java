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

package com.contentful.java.cma.model;

/**
 * A custom header for webhooks.
 */
public class CMAWebhookHeader {
  private final String key;
  private final String value;

  /**
   * Create new header, using key and value.
   * @param key for this header.
   * @param value stored in this header.
   */
  public CMAWebhookHeader(String key, String value) {
    this.key = key;
    this.value = value;
  }

  /**
   * @return A key associated with this header.
   */
  public String getKey() {
    return key;
  }

  /**
   * @return The value associated with this header.
   */
  public String getValue() {
    return value;
  }
}
