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

import java.util.List;

/**
 * Detail of one specific call of a Webhook.
 */
public class CMAWebhookCallDetail extends CMAResource {
  String url;
  String eventType;
  String requestAt;
  CMAWebhookRequest request;
  String responseAt;
  CMAWebhookResponse response;
  Integer statusCode;
  List<String> errors;

  /**
   * @return The url called from Contentful.
   */
  public String getUrl() {
    return url;
  }

  /**
   * @return The type of event issuing this call to the Webhook.
   */
  public String getEventType() {
    return eventType;
  }

  /**
   * @return The timestamp Contentful issued this Webhook.
   */
  public String getRequestAt() {
    return requestAt;
  }

  /**
   * @return The request this Webhook used.
   */
  public CMAWebhookRequest getRequest() {
    return request;
  }

  /**
   * @return When did the response return on Contentful?
   */
  public String getResponseAt() {
    return responseAt;
  }

  /**
   * @return The actual response from calling the Webhook.
   */
  public CMAWebhookResponse getResponse() {
    return response;
  }

  /**
   * @return A http response code indicating success or failure of calling this Webhook.
   */
  public Integer getStatusCode() {
    return statusCode;
  }

  /**
   * @return A list of errors encountered while executing the webhook.
   */
  public List<String> getErrors() {
    return errors;
  }
}
