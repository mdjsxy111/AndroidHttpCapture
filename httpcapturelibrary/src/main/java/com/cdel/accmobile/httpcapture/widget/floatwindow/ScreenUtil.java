package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

/**
 * 屏幕工具类
 *
 * @author zhangbaoyu
 * @time 2/25/21 11:07 PM
 */
public class ScreenUtil {
    private static final String NAVIGATION = "navigationBarBackground";

    private static final String XIAOMI_FULLSCREEN_GESTURE = "force_fsg_nav_bar";

    /**
     * 获取屏幕高度(px)
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 11:08 PM
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 11:08 PM
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取虚拟按键的高度
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 11:08 PM
     */
    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 11:09 PM
     */
    public static int getHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

    /**
     * 获取屏幕真实高度
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 11:09 PM
     */
    public static int getRealHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        return realHeight;
    }

    /**
     * 判断虚拟按键是否显示 华为手机
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 11:09 PM
     */
    public static boolean isNavigationBarShow(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            boolean result = realSize.y != size.y;
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !menu && !back;
        }
    }

    /**
     * 判断虚拟按键是否显示 小米手机
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 11:10 PM
     */
    public static boolean isNavigationBarShowXiaoMi(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        int screenHeight = dm.heightPixels;
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(realDisplayMetrics);
        } else {
            Class c;
            try {
                c = Class.forName("android.view.Display");
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, realDisplayMetrics);
            } catch (Exception e) {
                realDisplayMetrics.setToDefaults();
                e.printStackTrace();
            }
        }
        int screenRealHeight = realDisplayMetrics.heightPixels;
        return (screenRealHeight - screenHeight) > 0;
    }

    /**
     * 判断虚拟导航栏是否显示
     *
     * @param context 上下文对象
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     * @author zhangbaoyu
     * @time 2/25/21 11:10 PM
     */
    public static boolean checkNavigationBarShow(Activity context) {
        if (context == null) {
            return true;
        }
        boolean show;
        Display display = context.getWindow().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(point);
        }
        View decorView = context.getWindow().getDecorView();
        Configuration conf = context.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            show = (point.x != contentView.getWidth());
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            show = (rect.bottom != point.y);
        }
        boolean xiaomi = xiaomiNavigationGestureEnabled(context);
        boolean huawei = isNavigationBarExist(context);
        if (xiaomi) {
            return false;
        }
        if (!huawei) {
            return false;
        }
        return show;
    }

    /**
     * 判断全面屏幕手势是否开启
     *
     * @author zhangbaoyu
     * @time 2/25/21 11:11 PM
     */
    public static boolean xiaomiNavigationGestureEnabled(Context context) {
        int val = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val = Settings.Global.getInt(context.getContentResolver(), XIAOMI_FULLSCREEN_GESTURE, 0);
        }
        return val != 0;
    }

    /**
     * 判断华为mate20是否显示虚拟按键
     *
     * @author zhangbaoyu
     * @time 2/25/21 11:11 PM
     */
    public static boolean isNavigationBarExist(@NonNull Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0; i < vp.getChildCount(); i++) {
                vp.getChildAt(i).getContext().getPackageName();
                if (vp.getChildAt(i).getId() != -1 && NAVIGATION.equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
