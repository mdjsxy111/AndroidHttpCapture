package com.cdel.accmobile.httpcapture.widget.floatwindow.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * oppo手机处理
 *
 * @author zhangbaoyu
 * @time 2/25/21 10:33 PM
 */
public class OppoUtils {

    private static final String TAG = "OppoUtils";

    /**
     * 检测 oppo悬浮窗权限
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 10:34 PM
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
            Log.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * oppo ROM 权限申请
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 10:34 PM
     */
    public static void applyOppoPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
