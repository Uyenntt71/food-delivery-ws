package com.food_delivery.util;

import com.google.gson.Gson;

public class CacheUtils {
    public static String objToString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object stringToObject(String json, Class c) {
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }
}
