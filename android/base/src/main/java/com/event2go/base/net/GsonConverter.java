/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.event2go.base.net;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonConverter<T> implements Converter<ResponseBody, T> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final TypeAdapter<T> typeAdapter;
    private final Gson gson;

    GsonConverter(Gson gson, TypeAdapter<T> typeAdapter) {

        this.typeAdapter = typeAdapter;
        this.gson = gson;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            return typeAdapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}