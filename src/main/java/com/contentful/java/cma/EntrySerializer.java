package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAAsset;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMAResource;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.contentful.java.cma.Constants.CMAFieldType.Link;

final class EntrySerializer implements JsonSerializer<CMAEntry> {
  @Override
  public JsonElement serialize(CMAEntry src, Type type, JsonSerializationContext context) {
    JsonObject fields = new JsonObject();
    for (Map.Entry<String, LinkedHashMap<String, Object>> field : src.getFields().entrySet()) {
      LinkedHashMap<String, Object> value = field.getValue();
      if (value == null) {
        continue;
      }
      String fieldId = field.getKey();
      JsonObject jsonField = serializeField(context, field.getValue());
      if (jsonField != null) {
        fields.add(fieldId, jsonField);
      }
    }

    JsonObject result = new JsonObject();
    result.add("fields", fields);

    HashMap<String, Object> sys = src.getSys();
    if (sys != null) {
      result.add("sys", context.serialize(sys));
    }
    return result;
  }

  private JsonObject serializeField(JsonSerializationContext context,
      LinkedHashMap<String, Object> values) {
    JsonObject field = new JsonObject();
    for (String locale : values.keySet()) {
      Object localized = values.get(locale);
      if (localized instanceof CMAResource) {
        field.add(locale, toLink(context, (CMAResource) localized));
      } else if (localized instanceof List) {
        field.add(locale, serializeList(context, (List) localized));
      } else if (localized != null) {
        field.add(locale, context.serialize(localized));
      }
    }
    if (field.entrySet().size() > 0) {
      return field;
    }
    return null;
  }

  private JsonArray serializeList(JsonSerializationContext context, List list) {
    JsonArray array = new JsonArray();
    for (Object item : list) {
      if (item instanceof CMAResource) {
        array.add(toLink(context, (CMAResource) item));
      } else {
        array.add(context.serialize(item));
      }
    }
    return array;
  }

  private JsonObject toLink(JsonSerializationContext context, CMAResource resource) {
    String id = resource.getResourceId();
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Entry contains link to draft resource (has no ID).");
    }
    String type = (String) resource.getSys().get("type");
    if (type == null) {
      if (resource instanceof CMAAsset) {
        type = "Asset";
      } else if (resource instanceof CMAEntry) {
        type = "Entry";
      }
    }
    JsonObject sys = new JsonObject();
    sys.addProperty("type", Link.toString());
    sys.addProperty("linkType", type);
    sys.addProperty("id", id);

    JsonObject result = new JsonObject();
    result.add("sys", context.serialize(sys));

    return result;
  }
}
