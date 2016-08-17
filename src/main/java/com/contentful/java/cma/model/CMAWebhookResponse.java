/*
 * Copyright (C) 2016 Contentful GmbH
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

import java.util.Map;

/**
 * Model representing the response Contentful received from calling the Webhook.
 */
public class CMAWebhookResponse {
  String url;
  String body;
  Map<String, String> headers;
  Integer statusCode;

  /**
   * @return The url called by Contentful from this Webhook.
   */
  public String getUrl() {
    return url;
  }

  /**
   * @return The body Contentful received from calling the Webhook.
   */
  public String getBody() {
    return body;
  }

  /**
   * @return The headers returned by calling the Webhook.
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * @return A status code, returned by calling the Webhook.
   */
  public Integer getStatusCode() {
    return statusCode;
  }
}
