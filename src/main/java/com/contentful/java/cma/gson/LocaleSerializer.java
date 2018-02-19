package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.CMALocale;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class LocaleSerializer implements JsonSerializer<CMALocale> {
  @Override
  public JsonElement serialize(CMALocale src, Type typeOfSrc, JsonSerializationContext context) {
    final JsonObject result = new JsonObject();
    result.add("code", context.serialize(src.getCode()));
    result.add("name", context.serialize(src.getName()));
    result.add("fallbackCode", context.serialize(src.getFallbackCode()));
    result.add("contentManagementApi", context.serialize(src.isContentManagementApi()));
    result.add("contentDeliveryApi", context.serialize(src.isContentDeliveryApi()));
    return result;
  }
}
