package com.cdel.accmobile.httpcapture.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @author zhangbaoyu
 * @time 2/4/21 2:46 PM
 */
public class JsonUtil {

    /**
     * 判断json格式是否正确
     *
     * @param jsonStr json数据
     * @author zhangbaoyu
     * @time 1/29/21 6:04 PM
     */
    public static boolean isJson(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        return true;
    }
}
