package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.patch.CMAEntryJsonPatchItem;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Serialize an entry patch item to Contentful
 */
public class CMAEntryJsonPatchItemSerializer
        implements JsonSerializer<CMAEntryJsonPatchItem>, JsonDeserializer<CMAEntryJsonPatchItem> {
    private final EntrySerializer entrySerializer = new EntrySerializer();

    /**
     * Make sure field value is mapped same way as in CMAEntry update.
     *
     * @param src     the source to be edited.
     * @param type    the type to be used.
     * @param context the json context to be changed.
     * @return a created json element.
     */
    @Override
    public JsonElement serialize(CMAEntryJsonPatchItem src, Type type,
                                 JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("op", context.serialize(src.getOp()));
        result.add("path", context.serialize(src.getPath()));

        JsonElement value;
        if (src.getValue() != null) {
            value = entrySerializer.getSerializedFieldValue(context, src.getValue());
        } else {
            value = context.serialize(null);
        }
        result.add("value", value);

        return result;
    }

    @Override
    public CMAEntryJsonPatchItem deserialize(JsonElement json, Type type,
                                             JsonDeserializationContext context)
            throws JsonParseException {
        return new Gson()
                .fromJson(json, CMAEntryJsonPatchItem.class); // default deserialization
    }
}
