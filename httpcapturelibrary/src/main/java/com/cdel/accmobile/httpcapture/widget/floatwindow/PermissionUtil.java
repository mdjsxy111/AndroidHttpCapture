package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 权限工具类
 *
 * @author zhangbaoyu
 * @time 2/25/21 11:04 PM
 */
public class PermissionUtil {

    /**
     * 判断是否拥有弹悬浮窗的权限
     *
     * @param context 上下文
     * @return true 已授权 false 未授权
     * @author zhangbaoyu
     * @time 2/25/21 11:04 PM
     */
    public static boolean hasPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return hasPermissionBelowMarshmallow(context);
        }
        return true;
    }

    /**
     * OnActivityResult回调后判断是否拥有弹悬浮窗的权限
     *
     * @param context 上下文
     * @return true 已授权 false 未授权
     * @author zhangbaoyu
     * @time 2/25/21 11:04 PM
     */
    static boolean hasPermissionOnActivityResult(Context context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            return hasPermissionForO(context);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return hasPermissionBelowMarshmallow(context);
        }
        return true;
    }

    /**
     * 6.0以下判断是否有权限
     * 理论上6.0以上才需处理权限，但有的国内rom在6.0以下就添加了权限
     * 其实此方式也可以用于判断6.0以上版本，只不过有更简单的canDrawOverlays代替
     *
     * @author zhangbaoyu
     * @time 2/25/21 11:05 PM
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static boolean hasPermissionBelowMarshmallow(Context context) {
        try {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Method dispatchMethod = AppOpsManager.class.getMethod("checkOp", int.class, int.class, String.class);
            //AppOpsManager.OP_SYSTEM_ALERT_WINDOW = 24
            return AppOpsManager.MODE_ALLOWED == (Integer) dispatchMethod.invoke(
                manager, 24, Binder.getCallingUid(), context.getApplicationContext().getPackageName());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 用于判断8.0时是否有权限，仅用于OnActivityResult
     * 针对8.0官方bug:在用户授予权限后Settings.canDrawOverlays或checkOp方法判断仍然返回false
     *
     * @author zhangbaoyu
     * @time 2/25/21 11:05 PM
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean hasPermissionForO(Context context) {
        try {
            WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (mgr == null) return false;
            View viewToAdd = new View(context);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
            viewToAdd.setLayoutParams(params);
            mgr.addView(viewToAdd, params);
            mgr.removeView(viewToAdd);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestAlertWindowPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 判断是否为debug或release
     *
     * @param context
     * @author zhangbaoyu
     * @time 2/6/21 6:08 PM
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
