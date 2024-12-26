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

package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.CMAField;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * FieldTypeAdapter.
 */
public class FieldTypeAdapter implements JsonSerializer<CMAField> {
  private static final String ATTR_ID = "id";
  private static final String ATTR_NAME = "name";
  private static final String ATTR_TYPE = "type";
  private static final String ATTR_LINK_TYPE = "linkType";
  private static final String ATTR_REQUIRED = "required";
  private static final String ATTR_DISABLED = "disabled";
  private static final String ATTR_OMITTED = "omitted";
  private static final String ATTR_LOCALIZED = "localized";
  private static final String ATTR_VALIDATIONS = "validations";
  private static final String ATTR_ARRAY_ITEMS = "items";
  private static final String ATTR_DEFAULT_VALUE = "defaultValue";


  private static <T> void add(JsonObject json, String property, T value) {
    if (value != null) {
      if (value instanceof Boolean) {
        if ((Boolean) value) {
          json.addProperty(property, true);
        }
      } else if (value instanceof String) {
        json.addProperty(property, (String) value);
      } else {
        json.addProperty(property, value.toString());
      }
    }
  }

  /**
   * Serialize all fields for content types.
   *
   * @param field   the content type field to be serialized
   * @param type    the type to be used.
   * @param context the json context to be used.
   * @return a json object representing the field.
   */
  @Override
  public JsonElement serialize(CMAField field, Type type, JsonSerializationContext context) {
    JsonObject json = new JsonObject();

    add(json, ATTR_ID, field.getId());
    add(json, ATTR_NAME, field.getName());
    add(json, ATTR_TYPE, field.getType());
    add(json, ATTR_LINK_TYPE, field.getLinkType());
    add(json, ATTR_REQUIRED, field.isRequired());
    add(json, ATTR_DISABLED, field.isDisabled());
    add(json, ATTR_OMITTED, field.isOmitted());
    add(json, ATTR_LOCALIZED, field.isLocalized());

    List<Map<String, Object>> validations = field.getValidations();
    if (validations != null) {
      json.add(ATTR_VALIDATIONS, context.serialize(validations,
              new TypeToken<List<Map<String, Object>>>() {

      }.getType()));
    }

    Map<String, Object> defaultValue = field.getDefaultValue();
    if (defaultValue != null) {
      json.add(ATTR_DEFAULT_VALUE, context.serialize(defaultValue, Map.class));
    }

    Map<String, Object> arrayItems = field.getArrayItems();
    if (arrayItems != null) {
      json.add(ATTR_ARRAY_ITEMS, context.serialize(arrayItems, Map.class));
    }

    return json;
  }
}
