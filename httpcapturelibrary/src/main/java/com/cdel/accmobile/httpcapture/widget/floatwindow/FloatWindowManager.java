package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.cdel.accmobile.httpcapture.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.cdel.accmobile.httpcapture.widget.floatwindow.rom.HuaweiUtils;
import com.cdel.accmobile.httpcapture.widget.floatwindow.rom.MeizuUtils;
import com.cdel.accmobile.httpcapture.widget.floatwindow.rom.MiuiUtils;
import com.cdel.accmobile.httpcapture.widget.floatwindow.rom.OppoUtils;
import com.cdel.accmobile.httpcapture.widget.floatwindow.rom.QikuUtils;
import com.cdel.accmobile.httpcapture.widget.floatwindow.rom.RomUtils;

/**
 * 浮动窗口权限管理
 *
 * @author zhangbaoyu
 * @time 2/25/21 10:48 PM
 */
public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";

    private Dialog dialog;

    private Context mContext;

    private FloatWindowManager() {
        if (FloatWindowManagerHolder.instance != null) {
            throw new IllegalStateException();
        }
    }

    public static FloatWindowManager getInstance() {
        return FloatWindowManagerHolder.instance;
    }

    public static void commonROMPermissionApplyInternal(Context context) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = Settings.class;
        Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

        Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public boolean applyOrShowFloatWindow(Context context) {
        if (context == null) {
            return true;
        }
        this.mContext = context;
        // 判断是否有权限
        if (checkPermission()) {
            return true;
        } else {
            applyPermission();
            return false;
        }
    }

    /**
     * 检测手机系统并申请权限
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:49 PM
     */
    private boolean checkPermission() {
        // 6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck();
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck();
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck();
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck();
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck();
            }
        }
        return commonROMPermissionCheck();
    }

    /**
     * 检测华为
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:50 PM
     */
    private boolean huaweiPermissionCheck() {
        return HuaweiUtils.checkFloatWindowPermission(mContext);
    }

    /**
     * 检测小米
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:50 PM
     */
    private boolean miuiPermissionCheck() {
        return MiuiUtils.checkFloatWindowPermission(mContext);
    }

    /**
     * 检测魅族
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:51 PM
     */
    private boolean meizuPermissionCheck() {
        return MeizuUtils.checkFloatWindowPermission(mContext);
    }

    /**
     * 检测奇酷
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:51 PM
     */
    private boolean qikuPermissionCheck() {
        return QikuUtils.checkFloatWindowPermission(mContext);
    }

    /**
     * 检测oppo
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:51 PM
     */
    private boolean oppoROMPermissionCheck() {
        return OppoUtils.checkFloatWindowPermission(mContext);
    }

    /**
     * 检测通用手机
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:51 PM
     */
    private boolean commonROMPermissionCheck() {
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck();
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, mContext);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    /**
     * 去权限设置页面
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:51 PM
     */
    public void applyPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply();
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply();
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply();
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply();
            } else if (RomUtils.checkIsOppoRom()) {
                oppoROMPermissionApply();
            }
        }
        commonROMPermissionApply();
    }

    /**
     * 权限设置页面-360
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:52 PM
     */
    private void ROM360PermissionApply() {
        showConfirmDialog(new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    QikuUtils.applyPermission(mContext);
                } else {
                    Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 权限设置页面-华为
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:52 PM
     */
    private void huaweiROMPermissionApply() {
        showConfirmDialog(new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    HuaweiUtils.applyPermission(mContext);
                } else {
                    Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 权限设置页面-魅族
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:52 PM
     */
    private void meizuROMPermissionApply() {
        showConfirmDialog(new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MeizuUtils.applyPermission(mContext);
                } else {
                    Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 权限设置页面-魅族
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:52 PM
     */
    private void miuiROMPermissionApply() {
        showConfirmDialog(new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MiuiUtils.applyMiuiPermission(mContext);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 权限设置页面-oppo
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:52 PM
     */
    private void oppoROMPermissionApply() {
        showConfirmDialog(new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OppoUtils.applyOppoPermission(mContext);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 权限设置页面-通用
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:52 PM
     */
    private void commonROMPermissionApply() {
        // 这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showConfirmDialog(new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                commonROMPermissionApplyInternal(mContext);
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        } else {
                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                        }
                    }
                });
            }
        }
    }

    /**
     * 开启悬浮提示窗
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:54 PM
     */
    private void showConfirmDialog(OnConfirmResult result) {
        showConfirmDialog(mContext.getResources().getString(R.string.floatview_permission_content), result);
    }

    private void showConfirmDialog(String message, final OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new AlertDialog.Builder(mContext).setCancelable(true).setTitle("")
            .setMessage(message)
            .setPositiveButton(mContext.getResources().getString(R.string.floatview_permission_agree),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirmResult(true);
                        dialog.dismiss();
                    }
                }).setNegativeButton(mContext.getResources().getString(R.string.floatview_permission_cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirmResult(false);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 释放弹窗
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:54 PM
     */
    public void release() {
        if (dialog != null) {
            dialog.dismiss();
            dialog.cancel();
            dialog = null;
        }
        if (mContext != null) {
            mContext = null;
        }
    }

    private interface OnConfirmResult {

        /**
         * 确认结果
         *
         * @param confirm true 为开启，false为忽略
         * @author zhangbaoyu
         * @time 2/25/21 10:55 PM
         */
        void confirmResult(boolean confirm);
    }

    private static final class FloatWindowManagerHolder {
        private static FloatWindowManager instance = new FloatWindowManager();
    }
}
