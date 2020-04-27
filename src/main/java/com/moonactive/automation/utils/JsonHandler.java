package com.moonactive.automation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonHandler {
    private static final Gson GSON = new GsonBuilder().create();

    private JsonHandler() { }

    public static JsonArray asList(String json, String path) {
        String[] paths = path.split("\\.");
        JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
        for (int i = 0; i < paths.length - 1; i++) {
            jsonObject = jsonObject.getAsJsonObject(paths[i]);
        }
        return jsonObject.getAsJsonArray(paths[paths.length - 1]);
    }

    public static JsonObject getJsonObjectFromJsonArray(int index, String fieldName, JsonArray jsonArray) {
        return jsonArray.get(index).getAsJsonObject().get(fieldName).getAsJsonObject();
    }

    public static String getItemStringFromJsonArray(int index, String fieldName, JsonArray jsonArray){
        return jsonArray.get(index).getAsJsonObject().get(fieldName).getAsString();
    }

    public static String getItemStringFromJsonObject(JsonObject jsonObject, String fieldName){
        return jsonObject.get(fieldName).getAsString();
    }
}
