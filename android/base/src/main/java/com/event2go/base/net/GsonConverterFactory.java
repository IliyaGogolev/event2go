package com.event2go.base.net;

import android.net.ParseException;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 */
public final class GsonConverterFactory extends Converter.Factory {
    private final Gson gson;

    private GsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFactory create() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonUtcDateAdapter())
                .registerTypeAdapter(Pair.class, new GsonPairAdapter())
                .create();

        return create(gson);
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFactory create(Gson gson) {
        return new GsonConverterFactory(gson);
    }

//    @Override
//    public Converter<?> get(Type type) {
//        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
//        return new GsonConverter<>(gson, adapter);
//    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }

    // Convert UTC date string to Local Date type.
    private static class GsonUtcDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        public static final String UTC_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String UTC = "UTC";
        private final DateFormat dateFormat;

        public GsonUtcDateAdapter() {

            dateFormat = new SimpleDateFormat(UTC_DATETIME_FORMAT, Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
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
            } catch (java.text.ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    private static class GsonPairAdapter implements JsonDeserializer<Pair> {

        public static final String KEY = "Key";
        public static final String VALUE = "Value";

        @Override
        public synchronized Pair deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            JsonElement key = jsonObject.get(KEY);
            JsonElement value = jsonObject.get(VALUE);

            return new Pair<>(
                    gson.fromJson(key, Object.class),
                    gson.fromJson(value, Object.class)
            );
        }
    }
}