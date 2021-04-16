package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 系统工具类
 *
 * @author zhangbaoyu
 * @time 2/25/21 11:06 PM
 */
class Rom {
    /**
     * 判断intent是否存在
     *
     * @author zhangbaoyu
     * @time 2/25/21 11:06 PM
     */
    static boolean isIntentAvailable(Intent intent, Context context) {
        return intent != null && context.getPackageManager().queryIntentActivities(
            intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * 获取系统属性值
     *
     * @param name 属性名
     * @author zhangbaoyu
     * @time 2/25/21 11:06 PM
     */
    static String getProp(String name) {
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            String line = input.readLine();
            input.close();
            return line;
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
