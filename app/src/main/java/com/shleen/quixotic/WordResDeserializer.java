package com.shleen.quixotic;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;

public class WordResDeserializer implements JsonDeserializer<WordRes> {

    @Override
    public WordRes deserialize(JsonElement paramJsonElement, Type paramType,
                               JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = paramJsonElement.getAsJsonObject();
        try {
            jsonObject.get("results").toString();
        } catch (NullPointerException e) {
            return null;
        }

        JsonObject pronunciation = new JsonObject();
        try {
            if (jsonObject.get("pronunciation").toString().charAt(0) != '{') {
                pronunciation.addProperty("all", jsonObject.get("pronunciation").getAsString());
            }
            else {
                pronunciation = jsonObject.getAsJsonObject("pronunciation");
            }
        } catch (NullPointerException e) {
            pronunciation.addProperty("all", "");
        }
        jsonObject.add("pronunciation", pronunciation);

        return new Gson().fromJson(jsonObject, WordRes.class);
    }
}
