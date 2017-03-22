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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one webhook definition.
 *
 * A valid webhook needs to at least have called {@link #setName(String)},  {@link #setUrl(String)},
 * {@link #addTopic(CMAWebhookTopic)}.
 */
public class CMAWebhook extends CMAResource {
  String name;
  String url;

  List<CMAWebhookTopic> topics;
  List<CMAWebhookHeader> headers;

  @SerializedName("httpBasicUsername")
  String user;
  @SerializedName("httpBasicPassword")
  String password;

  /**
   * Create a webhoook, setting up the system attribute for it.
   */
  public CMAWebhook() {
    super(CMAType.WebhookDefinition);
  }

  /**
   * Sets the system field.
   *
   * @param system sets the system property.
   */
  @SuppressWarnings("unchecked")
  public CMAWebhook setSystem(CMASystem system) {
    this.system = system;
    return this;
  }

  /**
   * Convenience: Update the id of this entry without going through {@link #getSystem()}.
   *
   * @param id to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAWebhook setId(String id) {
    return (CMAWebhook) super.setId(id);
  }

  /**
   * Convenience: Update the version of this entry without going through {@link #getSystem()}.
   *
   * @param version to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAWebhook setVersion(Integer version) {
    return (CMAWebhook) super.setVersion(version);
  }

  /**
   * Convenience: Update the space id of this entry without going through {@link #getSystem()}.
   *
   * @param spaceId to be set.
   * @return the calling instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @Override public CMAWebhook setSpaceId(String spaceId) {
    return (CMAWebhook) super.setSpaceId(spaceId);
  }

  /**
   * Set the name of this webhook.
   *
   * @param name to identify this webhook
   * @return this webhook for chaining
   */
  public CMAWebhook setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the url to be called when this webhook is triggered.
   *
   * @param url complete, existing url to be used
   * @return this webhook for chaining
   */
  public CMAWebhook setUrl(String url) {
    this.url = url;
    return this;
  }

  /**
   * Add a topic this webhook should be triggered on.
   *
   * @param topic a valid enum value for the trigger reason.
   * @return this webhook for chaining
   */
  public CMAWebhook addTopic(CMAWebhookTopic topic) {
    if (this.topics == null) {
      this.topics = new ArrayList<CMAWebhookTopic>();
    }

    this.topics.add(topic);
    return this;
  }

  /**
   * Adds a custom http header to the call done by this webhook.
   *
   * @param key   HTTP header key to be used (aka 'X-My-Header-Name')
   * @param value HTTP header value to be used (aka 'this-is-my-name')
   * @return this webhook for chaining.
   */
  public CMAWebhook addHeader(String key, String value) {
    if (this.headers == null) {
      this.headers = new ArrayList<CMAWebhookHeader>();
    }

    this.headers.add(new CMAWebhookHeader(key, value));
    return this;
  }

  /**
   * Set authorization parameter for basic HTTP authorization on the url to be called by this
   * webhook.
   *
   * @param user     username to be used
   * @param password password to be used (cannot be retrieved, only updated!)
   * @return this webhook for chaining.
   */
  public CMAWebhook setBasicAuthorization(String user, String password) {
    this.user = user;
    this.password = password;
    return this;
  }

  /**
   * @return the name of this webhook
   */
  public String getName() {
    return name;
  }

  /**
   * @return Url to the server to be called, if this webhook is to be triggered
   */
  public String getUrl() {
    return url;
  }

  /**
   * @return A copy of the topics this webhook is interested in.
   */
  public List<CMAWebhookTopic> getTopics() {
    return new ArrayList<CMAWebhookTopic>(topics);
  }

  /**
   * @return A copy of the custom headers this webhook uses when triggered.
   */
  public List<CMAWebhookHeader> getHeaders() {
    return new ArrayList<CMAWebhookHeader>(headers);
  }

  /**
   * @return the http basic auth user set.
   */
  public String getUser() {
    return user;
  }
}
