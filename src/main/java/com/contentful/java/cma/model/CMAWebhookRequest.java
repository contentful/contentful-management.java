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
 * Model class representing a request done by Contentful to a webhook.
 */
public class CMAWebhookRequest {
  String url;
  String method;
  Map<String, String> headers;
  String body;

  /**
   * @return An url this request was issued to.
   */
  public String getUrl() {
    return url;
  }

  /**
   * @return The http method (aka POST, GET, DELETE, PUT) used for this request.
   */
  public String getMethod() {
    return method;
  }

  /**
   * @return All headers used, including custom headers, are returned by this method.
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * @return A JSON String representing the item affected.
   */
  public String getJSONBody() {
    return body;
  }
}
