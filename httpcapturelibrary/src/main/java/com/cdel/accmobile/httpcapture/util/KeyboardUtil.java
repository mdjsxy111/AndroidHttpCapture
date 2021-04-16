package com.cdel.accmobile.httpcapture.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘工具类
 *
 * @author zhangbaoyu
 * @time 2/25/21 9:54 PM
 */
public final class KeyboardUtil {

    private KeyboardUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 动态显示软键盘
     *
     * @param context 上下文
     * @param view    视图
     * @author zhangbaoyu
     * @time 2/25/21 9:56 PM
     */
    public static void showSoftInput(Context context, final View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 动态显示软键盘
     *
     * @param context 上下文
     * @param view    视图
     * @author zhangbaoyu
     * @time 2/25/21 9:56 PM
     */
    public static void hideSoftInput(Context context, final View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
