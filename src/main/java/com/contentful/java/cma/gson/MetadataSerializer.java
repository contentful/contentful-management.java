package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.CMAMetadata;
import com.contentful.java.cma.model.CMASystem;
import com.contentful.java.cma.model.CMATag;
import com.contentful.java.cma.model.CMAType;
import com.contentful.java.cma.model.CMALink;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class MetadataSerializer implements JsonSerializer<CMAMetadata> {
    @Override
    public JsonElement serialize(CMAMetadata src, Type typeOfSrc, JsonSerializationContext ctx) {
        final JsonObject result = new JsonObject();

        // Always include tags field, even if empty
        result.add("tags", serializeTags(src.getTags()));

        // Serialize concepts if present
        if (src.getConcepts() != null && !src.getConcepts().isEmpty()) {
            result.add("concepts", serializeLinks(
                    src.getConcepts(), CMAType.TaxonomyConcept));
        }

        // Serialize taxonomies if present
        if (src.getTaxonomy() != null && !src.getTaxonomy().isEmpty()) {
            result.add("taxonomy", serializeLinks(
                    src.getTaxonomy(), CMAType.TaxonomyConceptScheme));
        }

        return result;
    }

    private JsonArray serializeTags(List<CMATag> tags) {
        JsonArray jsonArray = new JsonArray();
        if (tags != null) {
            for (CMATag tag : tags) {
                JsonObject tagObject = new JsonObject();
                JsonObject sysObject = new JsonObject();
                CMASystem system = tag.getSystem();

                sysObject.addProperty("type", CMAType.Link.toString());
                sysObject.addProperty("linkType", CMAType.Tag.toString());
                sysObject.addProperty("id", system.getId());

                tagObject.add("sys", sysObject);
                jsonArray.add(tagObject);
            }
        }
        return jsonArray;
    }

    private JsonArray serializeLinks(List<CMALink> links, CMAType linkType) {
        JsonArray jsonArray = new JsonArray();
        for (CMALink link : links) {
            JsonObject linkObject = new JsonObject();
            JsonObject sysObject = new JsonObject();

            sysObject.addProperty("type", CMAType.Link.toString());
            sysObject.addProperty("linkType", linkType.toString());
            sysObject.addProperty("id", link.getId());

            linkObject.add("sys", sysObject);
            jsonArray.add(linkObject);
        }
        return jsonArray;
    }
}
