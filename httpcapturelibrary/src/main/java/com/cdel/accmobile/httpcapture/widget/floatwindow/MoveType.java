package com.cdel.accmobile.httpcapture.widget.floatwindow;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注解类，悬浮窗拖动方式
 *
 * @author zhangbaoyu
 * @time 2/25/21 11:03 PM
 */
public class MoveType {

    /**
     * 位置固定
     */
    static final int fixed = 0;

    /**
     * 不可拖动
     */
    public static final int inactive = 1;

    /**
     * 可拖动
     */
    public static final int active = 2;

    /**
     * 可拖动，释放后自动贴边 （默认）
     */
    public static final int slide = 3;

    /**
     * 可拖动，释放后自动回到原位置
     */
    public static final int back = 4;

    @IntDef({fixed, inactive, active, slide, back})
    @Retention(RetentionPolicy.SOURCE)
    @interface MOVE_TYPE {
    }
}
