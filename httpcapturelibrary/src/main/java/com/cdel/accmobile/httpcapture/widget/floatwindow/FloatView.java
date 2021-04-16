package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.view.View;

/**
 * 悬浮窗父类
 *
 * @author zhangbaoyu
 * @time 2/25/21 10:56 PM
 */
abstract class FloatView {

    abstract void setSize(int width, int height);

    abstract void setView(View view);

    abstract void setGravity(int gravity, int xOffset, int yOffset);

    abstract void init();

    abstract void dismiss();

    void updateXY(int x, int y) {
    }

    void updateX(int x) {
    }

    void updateY(int y) {
    }

    int getX() {
        return 0;
    }

    int getY() {
        return 0;
    }

}
