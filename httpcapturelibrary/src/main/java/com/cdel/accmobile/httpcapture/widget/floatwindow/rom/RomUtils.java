package com.cdel.accmobile.httpcapture.widget.floatwindow.rom;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 通用手机处理
 *
 * @author zhangbaoyu
 * @time 2/25/21 10:37 PM
 */
public class RomUtils {
    private static final String TAG = "RomUtils";

    /**
     * 获取 emui 版本号
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:37 PM
     */
    public static double getEmuiVersion() {
        try {
            String emuiVersion = getSystemProperty("ro.build.version.emui");
            String version = emuiVersion.substring(emuiVersion.indexOf("_") + 1);
            return Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 4.0;
    }

    /**
     * 获取小米 com.cdel.accmobile.debugging.widget.floatwindow.rom 版本号，获取失败返回 -1
     *
     * @return miui com.cdel.accmobile.debugging.widget.floatwindow.rom version code, if fail , return -1
     * @author zhangbaoyu
     * @time 2/25/21 10:37 PM
     */
    public static int getMiuiVersion() {
        String version = getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                Log.e(TAG, "get miui version code error, version : " + version);
            }
        }
        return -1;
    }

    /**
     * 获取系统属性
     *
     * @param propName 属性名称
     * @author zhangbaoyu
     * @time 2/25/21 10:38 PM
     */
    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    /**
     * 检查是否为华为系统
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:38 PM
     */
    public static boolean checkIsHuaweiRom() {
        return Build.MANUFACTURER.contains("HUAWEI");
    }

    /**
     * 检测是否为小米系统
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:39 PM
     */
    public static boolean checkIsMiuiRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    /**
     * 检测是否为魅族系统
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:39 PM
     */
    public static boolean checkIsMeizuRom() {
        String meizuFlymeOSFlag = getSystemProperty("ro.build.display.id");
        if (TextUtils.isEmpty(meizuFlymeOSFlag)) {
            return false;
        } else if (meizuFlymeOSFlag.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测是否为360系统
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:39 PM
     */
    public static boolean checkIs360Rom() {
        return Build.MANUFACTURER.contains("QiKU")
            || Build.MANUFACTURER.contains("360");
    }

    /**
     * 检测是否为oppo系统
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:40 PM
     */
    public static boolean checkIsOppoRom() {
        return Build.MANUFACTURER.contains("OPPO") || Build.MANUFACTURER.contains("oppo");
    }
}
