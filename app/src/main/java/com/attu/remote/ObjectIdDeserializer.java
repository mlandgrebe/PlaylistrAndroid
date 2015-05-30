package com.attu.remote;

import com.attu.models.ObjectId;
import com.google.gson.*;

import java.lang.reflect.Type;
// http://craigsmusings.com/2011/04/09/deserializing-mongodb-ids-and-dates-with-gson/
/**
 * Created by patrick on 5/30/15.
 */
public class ObjectIdDeserializer implements JsonDeserializer<ObjectId> {
    @Override
    public ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement idElement = json.getAsJsonObject().get("_id");

//        return new Gson().fromJson(idElement, ObjectId.class);
        return new ObjectId(json.getAsJsonObject().get("$oid").getAsString());
    }
}
