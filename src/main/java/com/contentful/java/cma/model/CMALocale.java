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

import com.google.gson.annotations.SerializedName;

/**
 * CMALocale.
 */
public class CMALocale extends CMAResource {
  String name;
  String code;
  boolean publish;

  @SerializedName("default")
  boolean isDefault;

  /**
   * @return the {@code name} attribute of this locale.
   */
  public String getName() {
    return name;
  }

  /**
   * @return the {@code code} attribute of this locale.
   */
  public String getCode() {
    return code;
  }

  /**
   * @return the {@code publish} attribute of this locale.
   */
  public boolean isPublished() {
    return publish;
  }

  /**
   * @return the {@code default} attribute of this locale.
   */
  public boolean isDefault() {
    return isDefault;
  }
}

