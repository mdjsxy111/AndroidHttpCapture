package com.cdel.accmobile.httpcapture.widget.floatwindow;

/**
 * 悬浮窗状态变化监听器
 *
 * @author zhangbaoyu
 * @time 2/25/21 11:07 PM
 */
public interface ViewStateListener {
    void onPositionUpdate(int x, int y);

    void onShow();

    void onHide();

    void onDismiss();

    void onMoveAnimStart();

    void onMoveAnimEnd();

}
