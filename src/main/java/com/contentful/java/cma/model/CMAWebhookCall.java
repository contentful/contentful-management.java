/*
 * Copyright (C) 2018 Contentful GmbH
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
 * Model class representing an overview of a call to a webhook
 */
public class CMAWebhookCall extends CMAResource {
  Integer statusCode;
  String responseAt;
  List<String> errors;
  String eventType;
  String url;
  String requestAt;

  /**
   * Create a webhook call, setting the type in it's system property.
   */
  public CMAWebhookCall() {
    super(CMAType.WebhookCallOverview);
  }

  /**
   * @return The http status code the call to the webhook returned.
   */
  public Integer getStatusCode() {
    return statusCode;
  }

  /**
   * @return The time the response of the webhook was issued.
   */
  public String getResponseAt() {
    return responseAt;
  }

  /**
   * @return A list of errors occurred during this call of the webhook.
   */
  public List<String> getErrors() {
    return errors;
  }

  /**
   * @return The type of event issuing this callback.
   */
  public String getEventType() {
    return eventType;
  }

  /**
   * @return Which url was called on this webhook.
   */
  public String getUrl() {
    return url;
  }

  /**
   * @return When did Contentful issue this webhook request?
   */
  public String getRequestAt() {
    return requestAt;
  }

  /**
   * @param spaceId the id to be set.
   * @return this object for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAWebhookCall setSpaceId(String spaceId) {
    return super.setSpaceId(spaceId);
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    return "CMAWebhookCall { " + super.toString() + " "
        + "errors = " + getErrors() + ", "
        + "eventType = " + getEventType() + ", "
        + "requestAt = " + getRequestAt() + ", "
        + "responseAt = " + getResponseAt() + ", "
        + "statusCode = " + getStatusCode() + ", "
        + "url = " + getUrl() + " "
        + "}";
  }
}
