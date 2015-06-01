package com.attu.remote;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by patrick on 5/31/15.
 */
public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
            jsonDeserializationContext) throws JsonParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        JsonElement dateEl = jsonElement.getAsJsonObject().get("date");
        if (dateEl != null) {
            try {
                return df.parse(dateEl.getAsString());
            } catch (ParseException e) {
                System.out.println("e = " + e.getMessage());
                return null;
            }
        }

        return null;
    }
}
