package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.CMAContentType;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMASnapshot;
import com.contentful.java.cma.model.CMASystem;
import com.contentful.java.cma.model.CMAType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * This class will take a json blob and deserialize a snapshot from it.
 */
public class SnapshotDeserializer implements JsonDeserializer<CMASnapshot> {

  /**
   * Inspect json payload and generate either a content type snapshot from it or an entry snap shot.
   *
   * @param json    the json blob to be converted
   * @param typeOfT the type to be converted
   * @param context the json context for updating
   * @return a snapshot, parsed from the json
   * @throws JsonParseException if the json object to be parsed is malformed.
   */
  @Override
  public CMASnapshot deserialize(JsonElement json, Type
      typeOfT, JsonDeserializationContext context) throws JsonParseException {
    final CMASnapshot result = new CMASnapshot();
    final JsonObject jsonObject = json.getAsJsonObject();
    final CMASystem system = context.deserialize(jsonObject.get("sys"), CMASystem.class);
    result.setSystem(system);

    final JsonObject snapshot = jsonObject.getAsJsonObject("snapshot");
    final String type = snapshot.getAsJsonObject("sys").getAsJsonPrimitive("type").getAsString();
    if (CMAType.ContentType.name().equals(type)) {
      result.setSnapshot(context.deserialize(snapshot, CMAContentType.class));
    } else if (CMAType.Entry.name().equals(type)) {
      result.setSnapshot(context.deserialize(snapshot, CMAEntry.class));
    }
    return result;
  }
}
