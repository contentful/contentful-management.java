package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.CMAMetadata;
import com.contentful.java.cma.model.CMASystem;
import com.contentful.java.cma.model.CMATag;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class MetadataSerializer implements JsonSerializer<CMAMetadata> {
    @Override
    public JsonElement serialize(CMAMetadata src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject result = new JsonObject();
        result.add("tags", serializeTags(src.getTags()));
        return result;
    }

    private JsonArray serializeTags(List<CMATag> tags) {
        final JsonArray jsonArray = new JsonArray();
        for (final CMATag tag : tags) {
            final JsonObject tagObject = new JsonObject();
            final JsonObject sysObject = new JsonObject();
            final CMASystem system = tag.getSystem();
            sysObject.addProperty("type", system.getType().toString());
            sysObject.addProperty("linkType", system.getLinkType().toString());
            sysObject.addProperty("id", system.getId());
            tagObject.add("sys", sysObject);
            jsonArray.add(tagObject);
        }
        return jsonArray;
    }
}
