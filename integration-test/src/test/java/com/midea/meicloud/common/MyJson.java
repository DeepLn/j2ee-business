package com.midea.meicloud.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class MyJson {
    private static Gson gson = new GsonBuilder().create();

    static public String toJson(Object obj){
        return gson.toJson(obj);
    }

    static public <T> T fromJson(String strJson, Type typeOfT) {
        return gson.fromJson(strJson, typeOfT);
    }
}
