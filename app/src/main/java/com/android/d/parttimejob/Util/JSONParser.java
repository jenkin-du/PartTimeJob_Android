package com.android.d.parttimejob.Util;


import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JSONParser {

    /**
     * 将类转换成json字符串
     */
    public static String toJSONString(Object obj){
        Gson gson=new Gson();
        return gson.toJson(obj);
    }

    /**
     * 将json字符串转换成Java类
     */
    public static <T> T toJavaBean(String jsonStr, Type type){
        Gson gson=new Gson();
       return gson.fromJson(jsonStr,type);
    }

    /**
     * 将json字符串转换成Java类
     */
    public static <T> T toJavaBean(String jsonStr, Class<T> cls){
        Gson gson=new Gson();
        return gson.fromJson(jsonStr,cls);
    }
}
