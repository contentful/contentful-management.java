package com.contentful.java.cma.gson;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

/**
 * This class will take a json blob and deserialize a snapshot from it.
 */
public class OptionsJsonDeserializer implements JsonDeserializer<Map<String, String>> {

  /**
   *
   * @param json
   * @param typeOfT
   * @param context
   * @return
   * @throws JsonParseException
   */
  @Override
  public Map<String, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (json.isJsonArray()) {
      Map<String, String> map = new HashMap<>();
      JsonArray array = json.getAsJsonArray();
      array.forEach(item -> {
        if (item.isJsonPrimitive()) {
          map.put(item.getAsString(), null);
        } else if (item.isJsonObject()) {
          item.getAsJsonObject().entrySet().forEach(entry -> {
            map.put(entry.getKey(), entry.getValue().getAsString());
          });
        }
      });

      return map;
    }

    return Collections.emptyMap();
  }
}