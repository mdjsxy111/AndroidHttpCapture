package com.cdel.accmobile.httpcapture.widget.floatwindow.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;


import com.cdel.accmobile.httpcapture.widget.floatwindow.FloatWindowManager;

import java.lang.reflect.Method;

/**
 * 魅族手机处理
 *
 * @author zhangbaoyu
 * @time 2/25/21 10:28 PM
 */
public class MeizuUtils {
    private static final String TAG = "MeizuUtils";

    /**
     * 检测 meizu 悬浮窗权限
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 10:29 PM
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
     * 去魅族权限申请页面
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/25/21 10:29 PM
     */
    public static void applyPermission(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.putExtra("packageName", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                // 最新的魅族flyme 6.2.5 用上述方法获取权限失败, 不过又可以用下述方法获取权限了
                FloatWindowManager.commonROMPermissionApplyInternal(context);
            } catch (Exception eFinal) {
                Log.e(TAG, Log.getStackTraceString(eFinal));
            }
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
