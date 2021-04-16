package com.accmobile.httpcapturedemo;

import android.app.Application;

import com.accmobile.httpcapturedemo.http.BaseOkGoUtils;
import com.cdel.accmobile.httpcapture.manager.HttpCaptureManager;


/**
 * @author zhangbaoyu
 * @time 2/19/21 2:33 PM
 */
public class App extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        BaseOkGoUtils.init(this);
        HttpCaptureManager.getInstance().init(this);
    }

    /**
     * 获取Application上下文
     *
     * @author zhangbaoyu
     * @time 2/26/21 11:27 AM
     */
    public static Application getApplication() {
        return application;
    }
}
