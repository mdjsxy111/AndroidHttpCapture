package com.cdel.accmobile.httpcapture.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

/**
 * view工具类
 *
 * @author zhangbaoyu
 * @time 2/25/21 9:58 PM
 */
public class ViewUtil {

    /**
     * 根据指定的布局资源ID填充对应的View
     *
     * @param context  上下文
     * @param layoutId 布局ID
     * @param parent   父View
     * @author zhangbaoyu
     * @time 2/25/21 9:59 PM
     */
    public static View getItemView(Context context, @LayoutRes int layoutId, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }
}
