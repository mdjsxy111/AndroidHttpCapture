package com.cdel.accmobile.httpcapture.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 解决TextView设置textIsSelectable=true后,第一次点击无效，第二次以后有效。
 *
 * @author zhangbaoyu
 * @time 3/3/21 5:12 PM
 */
public class SelectableTextView extends AppCompatTextView {

    /**
     * 记录按下时间
     */
    private long mLastActionDownTime = 0L;

    public SelectableTextView(Context context) {
        super(context);
    }

    public SelectableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastActionDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                long actionUpTime = System.currentTimeMillis();
                if (actionUpTime - mLastActionDownTime < ViewConfiguration.getLongPressTimeout()) {
                    // 如果点击触摸的时间小于长按的时间，执行点击事件
                    result = true;
                    onVisibilityChanged(this, GONE);
                    callOnClick();
                } else {
                    // 长按事件,即不处理点击事件
                    result = false;
                }
                break;
        }
        if (result) {
            // 返回点击事件
            return true;
        } else {
            // 如果没有处理  就走父类方法 使其支持复制粘贴功能
            return super.onTouchEvent(event);
        }
    }
}
