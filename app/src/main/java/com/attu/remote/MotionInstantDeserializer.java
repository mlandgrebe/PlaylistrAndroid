package com.attu.remote;

import com.attu.data.MotionInstant;
import com.attu.models.PointLocation;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by patrick on 6/1/15.
 */
public class MotionInstantDeserializer implements JsonDeserializer<MotionInstant> {
    @Override
    public MotionInstant deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
            jsonDeserializationContext) throws JsonParseException {
        double norm = jsonElement.getAsJsonObject().get("norm").getAsDouble();
        long timestamp = jsonElement.getAsJsonObject().get("time").getAsLong() / 1000;

        return new MotionInstant(timestamp, norm);
    }
}
