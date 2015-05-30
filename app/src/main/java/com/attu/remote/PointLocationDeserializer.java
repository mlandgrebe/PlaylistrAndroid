package com.attu.remote;

import com.attu.models.ObjectId;
import com.attu.models.PointLocation;
import com.google.gson.*;
import org.json.JSONArray;

import java.lang.reflect.Type;

/**
 * Created by patrick on 5/30/15.
 */
public class PointLocationDeserializer implements JsonDeserializer<PointLocation> {
    @Override
    public PointLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {
        System.out.println(json);
        JsonArray coords = json.getAsJsonObject().get("coordinates").getAsJsonArray();
        System.out.println(coords);
        return new PointLocation(coords.get(0).getAsDouble(),
                coords.get(1).getAsDouble());

    }
}
