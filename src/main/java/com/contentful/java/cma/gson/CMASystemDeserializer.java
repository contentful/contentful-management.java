package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.CMABulkStatus;
import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMASystem;
import com.contentful.java.cma.model.CMAType;
import com.contentful.java.cma.model.CMAVisibility;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class CMASystemDeserializer implements JsonDeserializer<CMASystem> {
    @Override
    public CMASystem deserialize(JsonElement json,
                                 Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
        CMASystem system = new CMASystem();
        JsonObject sysObject = json.getAsJsonObject();

        if (sysObject.has("contentType")) {
            system.setContentType(context.deserialize(sysObject.get("contentType"),
                    CMALink.class));
        }
        if (sysObject.has("createdAt")) {
            system.setCreatedAt(sysObject.get("createdAt").getAsString());
        }
        if (sysObject.has("createdBy")) {
            system.setCreatedBy(context.deserialize(sysObject.get("createdBy"), CMALink.class));
        }
        if (sysObject.has("firstPublishedAt")) {
            system.setFirstPublishedAt(sysObject.get("firstPublishedAt").getAsString());
        }
        if (sysObject.has("id")) {
            system.setId(sysObject.get("id").getAsString());
        }
        if (sysObject.has("linkType")) {
            system.setLinkType(context.deserialize(sysObject.get("linkType"), CMAType.class));
        }
        if (sysObject.has("publishedAt")) {
            system.setPublishedAt(sysObject.get("publishedAt").getAsString());
        }
        if (sysObject.has("publishedBy")) {
            system.setPublishedBy(context.deserialize(sysObject.get("publishedBy"),
                    CMALink.class));
        }
        if (sysObject.has("publishedCounter")) {
            system.setPublishedCounter(sysObject.get("publishedCounter").getAsInt());
        }
        if (sysObject.has("publishedVersion")) {
            system.setPublishedVersion(sysObject.get("publishedVersion").getAsInt());
        }
        if (sysObject.has("space")) {
            system.setSpace(context.deserialize(sysObject.get("space"), CMALink.class));
        }
        if (sysObject.has("environment")) {
            system.setEnvironment(context.deserialize(sysObject.get("environment"),
                    CMALink.class));
        }
        if (sysObject.has("type")) {
            system.setType(context.deserialize(sysObject.get("type"), CMAType.class));
        }
        if (sysObject.has("updatedAt")) {
            system.setUpdatedAt(sysObject.get("updatedAt").getAsString());
        }
        if (sysObject.has("updatedBy")) {
            system.setUpdatedBy(context.deserialize(sysObject.get("updatedBy"), CMALink.class));
        }
        if (sysObject.has("version")) {
            system.setVersion(sysObject.get("version").getAsInt());
        }
        if (sysObject.has("visibility")) {
            system.setVisibility(context.deserialize(sysObject.get("visibility"),
                    CMAVisibility.class));
        }
        if (sysObject.has("organization")) {
            system.setOrganization(context.deserialize(sysObject.get("organization"),
                    CMALink.class));
        }
        if (sysObject.has("urn")) {
            system.setUrn(sysObject.get("urn").getAsString());
        }
        if (sysObject.has("archivedVersion")) {
            system.setArchivedVersion(sysObject.get("archivedVersion").getAsInt());
        }
        if (sysObject.has("status")) {
            JsonElement statusElement = sysObject.get("status");
            if (statusElement.isJsonObject()) {
                system.setEnvironmentalStatus(context.deserialize(sysObject.get("status"),
                        CMALink.class));
            } else if (statusElement.isJsonPrimitive()) {
                String statusStr = statusElement.getAsString();
                CMABulkStatus bulkStatus = CMABulkStatus.from(statusStr);
                system.setBulkActionStatus(bulkStatus);
            }
        }

        return system;
    }
}