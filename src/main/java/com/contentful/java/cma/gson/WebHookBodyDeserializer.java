package com.contentful.java.cma.gson;

import com.contentful.java.cma.model.*;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class will take a json blob and deserialize a WebHook from it.
 */
public class WebHookBodyDeserializer implements JsonDeserializer<CMAWebhookTransformation> {

  @Override
  public CMAWebhookTransformation deserialize(JsonElement json, Type
      typeOfT, JsonDeserializationContext context) throws JsonParseException {
    final CMAWebhookTransformation result = new CMAWebhookTransformation();
    final JsonObject jsonObject = json.getAsJsonObject();
    final JsonPrimitive contentType = jsonObject.getAsJsonObject().getAsJsonPrimitive("contentType");
    if(contentType != null) {
      result.setContentType(contentType.getAsString());
    }
    final JsonPrimitive method = jsonObject.getAsJsonObject().getAsJsonPrimitive("method");
    if(method != null) {
      result.setMethod(method.getAsString());
    }
    final JsonPrimitive includeContentLength = jsonObject.getAsJsonObject().getAsJsonPrimitive("includeContentLength");
    if(includeContentLength != null) {
      result.setIncludeContentLength(includeContentLength.getAsBoolean());
    }

    final JsonElement body = jsonObject.getAsJsonObject().get("body");
    if(body != null) {
      result.setBody(body.toString());
    }
    return result;
  }
}
