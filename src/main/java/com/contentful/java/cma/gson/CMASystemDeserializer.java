package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.CMABulkStatus;
import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMASystem;
import com.contentful.java.cma.model.CMAType;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class CMASystemDeserializer implements JsonDeserializer<CMASystem> {
    @Override
    public CMASystem deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        CMASystem system = new CMASystem();
        JsonObject sysObject = json.getAsJsonObject();

        // Deserialize ID
        if (sysObject.has("id")) {
            system.setId(sysObject.get("id").getAsString());
        }

        // Deserialize Type
        if (sysObject.has("type")) {
            system.setType(CMAType.valueOf(sysObject.get("type").getAsString()));
        }

        // Deserialize CreatedAt
        if (sysObject.has("createdAt")) {
            system.setCreatedAt(sysObject.get("createdAt").getAsString());
        }

        // Deserialize UpdatedAt
        if (sysObject.has("updatedAt")) {
            system.setUpdatedAt(sysObject.get("updatedAt").getAsString());
        }

        // Deserialize Version
        if (sysObject.has("version")) {
            system.setVersion(sysObject.get("version").getAsInt());
        }

        // Deserialize Space, if present
        if (sysObject.has("space")) {
            JsonObject spaceObject = sysObject.getAsJsonObject("space");
            CMALink spaceLink = context.deserialize(spaceObject, CMALink.class);
            system.setSpace(spaceLink);
        }

        // Deserialize Environment, if present
        if (sysObject.has("environment")) {
            JsonObject environmentObject = sysObject.getAsJsonObject("environment");
            CMALink environmentLink = context.deserialize(environmentObject, CMALink.class);
            system.setEnvironment(environmentLink);
        }

        // Special handling for "status" to differentiate between CMALink and CMABulkStatus
        if (sysObject.has("status")) {
            JsonElement statusElement = sysObject.get("status");
            if (statusElement.isJsonObject()) {
                // It's likely an environment status
                CMALink environmentStatus = context.deserialize(statusElement, CMALink.class);
                system.setEnvironmentalStatus(environmentStatus);
            } else if (statusElement.isJsonPrimitive()) {
                // It's likely a bulk action status
                String statusStr = statusElement.getAsString();
                CMABulkStatus bulkStatus = CMABulkStatus.from(statusStr);
                system.setBulkActionStatus(bulkStatus);
            }
        }

        // Add manual deserialization for other fields as necessary

        return system;
    }
}