package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.CMAAsset;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMAMetadata;
import com.contentful.java.cma.model.CMAResource;
import com.contentful.java.cma.model.CMASystem;
import com.contentful.java.cma.model.CMAType;
import com.contentful.java.cma.model.rich.CMARichBlock;
import com.contentful.java.cma.model.rich.CMARichHyperLink;
import com.contentful.java.cma.model.rich.CMARichNode;
import com.contentful.java.cma.model.rich.RichTextFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.contentful.java.cma.model.CMAType.Link;

/**
 * Serialize an entry from Contentful
 */
public class EntrySerializer implements JsonSerializer<CMAEntry>, JsonDeserializer<CMAEntry> {

  private final Gson freshGson = new Gson();

  /**
   * Make sure all fields are mapped in the locale - value way.
   *
   * @param src     the source to be edited.
   * @param type    the type to be used.
   * @param context the json context to be changed.
   * @return a created json element.
   */
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

    CMAMetadata metadata = src.getMetadata();
    if (metadata != null) {
      result.add("metadata", context.serialize(metadata));
    }

    final CMASystem sys = src.getSystem();
    if (sys != null) {
      result.add("sys", context.serialize(sys));
    }
    return result;
  }

  @Override
  public CMAEntry deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    final CMAEntry entry = new Gson().fromJson(json, CMAEntry.class); // default deserialization
    RichTextFactory.resolveRichTextField(entry);
    return entry;
  }

  private JsonObject serializeField(JsonSerializationContext context,
                                    LinkedHashMap<String, Object> values) {
    JsonObject field = new JsonObject();
    for (String locale : values.keySet()) {
      Object localized = values.get(locale);
      if (localized instanceof CMAResource) {
        field.add(locale, toLink(context, (CMAResource) localized));
      } else if (localized instanceof List) {
        field.add(locale, serializeList(context, (List<Object>) localized));
      } else if (localized instanceof CMARichNode) {
        field.add(locale, serializeRichNode(context, (CMARichNode) localized));
      } else if (localized != null) {
        field.add(locale, context.serialize(localized));
      }
    }
    if (field.entrySet().size() > 0) {
      return field;
    }
    return null;
  }

  private JsonArray serializeList(JsonSerializationContext context, List<Object> list) {
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

  private JsonElement serializeRichNode(JsonSerializationContext context, CMARichNode node) {
    if (node instanceof CMARichHyperLink) {
      return serializeRichHyperlink(context, (CMARichHyperLink) node);
    } else if (node instanceof CMARichBlock) {
      return serializeRichBlock(context, (CMARichBlock) node);
    } else {
      return context.serialize(node);
    }
  }

  private JsonElement serializeRichBlock(JsonSerializationContext context, CMARichBlock block) {
    final JsonObject jsonBlock = freshGson.toJsonTree(block).getAsJsonObject();

    final JsonArray jsonContent = new JsonArray(block.getContent().size());
    for (final CMARichNode contentNode : block.getContent()) {
      jsonContent.add(serializeRichNode(context, contentNode));
    }

    jsonBlock.add("content", jsonContent);

    return jsonBlock;
  }

  private JsonElement serializeRichHyperlink(JsonSerializationContext context,
                                             CMARichHyperLink link) {
    final JsonObject jsonLink = freshGson.toJsonTree(link).getAsJsonObject();

    jsonLink.addProperty("nodeType", link.getNodeType());

    final Map<String, Object> data = new LinkedHashMap<>(1);
    if (link.getData() instanceof CMAResource) {
      data.put("target", toLink(context, (CMAResource) link.getData()));
    } else {
      data.put("uri", link.getData());
      data.remove("target");
    }

    jsonLink.add("data", context.serialize(data));
    return jsonLink;
  }

  private JsonObject toLink(JsonSerializationContext context, CMAResource resource) {
    final String id = resource.getId();
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Entry contains link to draft resource (has no ID).");
    }

    CMAType linkedType = resource.getSystem().getLinkType();
    if (linkedType == null) {
      if (resource instanceof CMAAsset) {
        linkedType = CMAType.Asset;
      } else if (resource instanceof CMAEntry) {
        linkedType = CMAType.Entry;
      }
    }

    JsonObject sys = new JsonObject();
    sys.addProperty("type", Link.toString());
    sys.addProperty("linkType", linkedType.toString());
    sys.addProperty("id", id);

    JsonObject result = new JsonObject();
    result.add("sys", context.serialize(sys));

    return result;
  }
}
