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

package com.contentful.java.cma.model;

/**
 * CMAWebhookTransformation
 * <p>
 * This class will be used to represent a transformation property.
 */
public class CMAWebhookTransformation {
  private String method;
  private String contentType;
  private Boolean includeContentLength;
  private Object body;

  /**
   * @return the custom HTTP method of this transformation
   */
  public String getMethod() {
    return method;
  }

  /**
   * Sets a HTTP method to this webhook transformation.
   *
   * @param method custom HTTP method
   * @return this webhook for chaining
   */
  public CMAWebhookTransformation setMethod(String method) {
    this.method = method;
    return this;
  }

  /**
   * @return the content type of this webhook transformation.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Sets a content type to this webhook transformation.
   *
   * @param contentType the contentType to set
   * @return this webhook for chaining
   */
  public CMAWebhookTransformation setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * Whether the content length is included in the webhook transformation.
   *
   * @return <code>true</code> if content length is included, <code>false</code> otherwise
   */
  public Boolean getIncludeContentLength() {
    return includeContentLength;
  }

  /**
   * Sets whether the content length is included in the webhook transformation.
   *
   * @param includeContentLength the includeContentLength flag to set
   * @return this webhook for chaining
   */
  public CMAWebhookTransformation setIncludeContentLength(Boolean includeContentLength) {
    this.includeContentLength = includeContentLength;
    return this;
  }

  /**
   * @return the webhook transformation body
   */
  public Object getBody() {
    return body;
  }

  /**
   * Sets the webhook transformation body.
   *
   * @param body the body of the transformation can be any arbitrary JSON data structure
   * @return this webhook for chaining
   */
  public CMAWebhookTransformation setBody(Object body) {
    this.body = body;
    return this;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override
  public String toString() {
    return "CMAWebhookTransformation [method=" + method
      + ", contentType=" + contentType
      + ", includeContentLength=" + includeContentLength
      + ", body=" + body + "]";
  }
}
