package com.accmobile.httpcapturedemo.http;

import android.app.Application;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cookie.store.MemoryCookieStore;

import java.util.logging.Level;

/**
 * OkHttp初始化类
 *
 * @author zhangbaoyu
 * @time 2/19/21 2:40 PM
 */
public class BaseOkGoUtils {

    public static void init(Application application) {
        //必须调用初始化
        OkHttpUtils.init(application);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor("OkHttp");
        interceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        interceptor.setColorLevel(Level.INFO);
        OkHttpUtils.getInstance().addInterceptor(interceptor);
        OkHttpUtils.getInstance().setCookieStore(new MemoryCookieStore());
        OkHttpUtils.getInstance().setConnectTimeout(10000);
    }
}
