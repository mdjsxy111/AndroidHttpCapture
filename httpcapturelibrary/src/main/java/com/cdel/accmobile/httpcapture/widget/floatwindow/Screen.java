package com.cdel.accmobile.httpcapture.widget.floatwindow;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注解类，尺寸属性
 *
 * @author zhangbaoyu
 * @time 2/25/21 11:06 PM
 */
public class Screen {
    public static final int width = 0;

    public static final int height = 1;

    @IntDef({width, height})
    @Retention(RetentionPolicy.SOURCE)
    @interface screenType {
    }
}
