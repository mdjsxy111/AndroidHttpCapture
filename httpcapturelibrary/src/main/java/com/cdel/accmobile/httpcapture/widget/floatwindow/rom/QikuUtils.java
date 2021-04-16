package com.cdel.accmobile.httpcapture.widget.floatwindow.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 奇酷手机处理
 *
 * @author zhangbaoyu
 * @time 2/25/21 10:35 PM
 */
public class QikuUtils {
    private static final String TAG = "QikuUtils";

    /**
     * 检测 360 悬浮窗权限
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:35 PM
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.KITKAT) {
            //OP_SYSTEM_ALERT_WINDOW = 24;
            return checkOp(context, 24);
        }
        return true;
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
            Log.e("", "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * 去360权限申请页面
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 10:36 PM
     */
    public static void applyPermission(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Log.e(TAG, "can't open permission page with particular name, please use " +
                    "\"adb shell dumpsys activity\" command and tell me the name of the float window permission page");
            }
        }
    }

    /**
     * intent是否可用
     *
     * @param intent  Intent
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 10:31 PM
     */
    private static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}
