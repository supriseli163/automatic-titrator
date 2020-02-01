package com.jh.automatic_titrator.common.utils;

import com.google.gson.Gson;

/**
 * Created by apple on 2016/12/18.
 */

public class JsonHelper {
    private static Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
