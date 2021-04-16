package com.accmobile.httpcapturedemo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangbaoyu
 * @time 2/19/21 4:23 PM
 */
public class DateUtil {
    public static final String PATTERN_HMS = "HH:mm:ss";

    public DateUtil() {
    }

    public static String getString(Date date, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        } catch (Exception var3) {
            var3.printStackTrace();
            return "";
        }
    }
}
