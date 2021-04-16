package com.cdel.accmobile.httpcapture.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangbaoyu
 * @time 2/3/21 4:47 PM
 */
public class HttpCaptureGsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private HttpCaptureGsonUtil() {
    }

    /**
     * 对象转json
     *
     * @param t 对象
     * @author zhangbaoyu
     * @time 2/25/21 9:51 PM
     */
    public static <T> String objectToJson(T t) {
        String json = null;
        try {
            json = gson.toJson(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * list转json
     *
     * @param list 集合
     * @author zhangbaoyu
     * @time 2/25/21 9:51 PM
     */
    public static <T> String listToJson(List<T> list) {
        String json = null;
        try {
            json = gson.toJson(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * json转对象
     *
     * @param jsonString json数据
     * @param cls        对象类
     * @author zhangbaoyu
     * @time 2/25/21 9:52 PM
     */
    public static <T> T jsonToObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * json转list
     *
     * @param json json数据
     * @param cls  对象类
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

}
