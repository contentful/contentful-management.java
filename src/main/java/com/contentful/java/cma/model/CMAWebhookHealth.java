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

/**
 * Model class representing a health report of this webhook.
 */
public class CMAWebhookHealth extends CMAResource {
  /**
   * Model representing the spread of calls on this Webhook.
   */
  public static class CMAWebhookHealthCall {
    Integer total;
    Integer healthy;

    /**
     * @return Number of calls happened to this Webhook in total.
     */
    public Integer getTotal() {
      return total;
    }

    /**
     * @return Number of calls healthy on this Webhook.
     */
    public Integer getHealthy() {
      return healthy;
    }
  }

  CMAWebhookHealthCall calls;

  /**
   * @return Returns Object containing Counts of calls of this Webhook.
   */
  public CMAWebhookHealthCall getCalls() {
    return calls;
  }
}
