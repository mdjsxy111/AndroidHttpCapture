package com.cdel.accmobile.httpcapture.manager;

import android.content.Context;

import com.cdel.accmobile.httpcapture.util.HttpCaptureSPUtil;
import com.cdel.accmobile.httpcapture.widget.floatwindow.HttpCaptureOverlayWindow;
import com.cdel.accmobile.httpcapture.widget.floatwindow.PermissionUtil;

/**
 * Debugging管理类
 *
 * @author zhangbaoyu
 * @time 2/22/21 9:29 PM
 */
public class HttpCaptureManager {


    private HttpCaptureManager() {
    }

    public static HttpCaptureManager getInstance() {
        return DebuggingManagerHolder.instance;
    }

    private static final class DebuggingManagerHolder {
        private static HttpCaptureManager instance = new HttpCaptureManager();
    }

    public void init(Context context) {
        // DebugSPUtil初始化
        HttpCaptureSPUtil.init(context);
        // 调试模式小球的开关
        showFloatEnter(context);
    }

    private void showFloatEnter(Context context) {
        // 如果有权限可进行显示悬浮入口
        if (PermissionUtil.hasPermission(context)) {
            // 获取debug状态是否开启
            boolean debugIsOpen = HttpCaptureSPUtil.getIsOpenCapture();
            if (debugIsOpen) {
                HttpCaptureOverlayWindow.getInstance(context).show();
            }
        }
    }

}
