package com.event2go.app.utils;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Iliya Gogolev on 11/9/15.
 */
public class GsonUTCdateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private final DateFormat dateFormat;

    public GsonUTCdateAdapter() {

        /**
         * for iOS use
         * https://parse.com/questions/android-sdk-parse-createdat-date-format
         */
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        dateFormat = new SimpleDateFormat("EEE MMM dd HH':'mm':'ss z yyyy", Locale.US);
        // converts the date to UTC which cannot be accessed with the default serializer
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(dateFormat.format(date));
    }

    @Override
    public synchronized Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        try {
            return dateFormat.parse(jsonElement.getAsString());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}