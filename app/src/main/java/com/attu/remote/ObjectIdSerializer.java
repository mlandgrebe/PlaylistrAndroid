package com.attu.remote;

import com.attu.models.ObjectId;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by patrick on 5/30/15.
 */
public class ObjectIdSerializer implements JsonSerializer<ObjectId> {
    @Override
    public JsonElement serialize(ObjectId src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("$oid", src.getOid());
        return json;
    }
}
