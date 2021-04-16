package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.view.View;

/**
 * 悬浮窗接口
 *
 * @author zhangbaoyu
 * @time 2/25/21 11:02 PM
 */
public abstract class IFloatWindow {

    public abstract boolean show();

    public abstract void hide();

    public abstract boolean isShowing();

    public abstract int getX();

    public abstract int getY();

    public abstract void updateX(int x);

    public abstract void updateX(@Screen.screenType int screenType, float ratio);

    public abstract void updateY(int y);

    public abstract void updateY(@Screen.screenType int screenType, float ratio);

    public abstract View getView();

    abstract void dismiss();

    public abstract void setViewStateListener(ViewStateListener viewStateListener);

    public abstract void setPermissionListener(PermissionListener permissionListener);

}
