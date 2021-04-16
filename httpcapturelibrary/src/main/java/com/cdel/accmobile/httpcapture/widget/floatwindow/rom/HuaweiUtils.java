package com.cdel.accmobile.httpcapture.widget.floatwindow.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 华为手机处理
 *
 * @author zhangbaoyu
 * @time 2/25/21 10:07 PM
 */
public class HuaweiUtils {
    private static final String TAG = "HuaweiUtils";

    /**
     * 检测 Huawei 悬浮窗权限
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 10:26 PM
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version > Build.VERSION_CODES.KITKAT) {
            // OP_SYSTEM_ALERT_WINDOW = 24;
            return checkOp(context, 24);
        }
        return true;
    }

    /**
     * 去华为权限申请页面
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 10:27 PM
     */
    public static void applyPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 悬浮窗管理页面
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");
            intent.setComponent(comp);
            if (RomUtils.getEmuiVersion() == 3.1) {
                // emui 3.1 的适配
                context.startActivity(intent);
            } else {
                // emui 3.0 的适配 悬浮窗管理页面
                comp = new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
                intent.setComponent(comp);
                context.startActivity(intent);
            }
        } catch (SecurityException e) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 华为权限管理，跳转到本app的权限管理页面,这个需要华为接口权限，未解决
            ComponentName comp = new ComponentName("com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (ActivityNotFoundException e) {
            /**
             * 手机管家版本较低 HUAWEI SC-UL10
             */
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 权限管理页面 android4.4
            ComponentName comp = new ComponentName("com.Android.settings", "com.android.settings.permission.TabItem");
            intent.setComponent(comp);
            context.startActivity(intent);
            e.printStackTrace();
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (Exception e) {
            // 抛出异常时提示信息
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    /**
     * @param context 上下文
     * @param op      OP_SYSTEM_ALERT_WINDOW
     * @author zhangbaoyu
     * @time 2/25/21 10:28 PM
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            Log.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }
}


