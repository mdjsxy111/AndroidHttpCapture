package com.accmobile.httpcapturedemo.http;

import com.lzy.okhttputils.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by ${USER_NAME} on 2018/10/28.
 */
public abstract class StringResultCallBack extends StringCallback {

    public abstract void onStringResult(String sResult);

    public abstract void onStringError();

    @Override
    public void onSuccess(final String s, Call call, Response response) {
        onStringResult(s);
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        onStringError();
    }
}
