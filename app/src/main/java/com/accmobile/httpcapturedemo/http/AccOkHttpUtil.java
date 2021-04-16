package com.accmobile.httpcapturedemo.http;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpParams;

public class AccOkHttpUtil {

    public static void getPoetryOne(StringResultCallBack stringResultCallBack) {
        OkHttpUtils.get(APIS.getPoetryUrl).execute(stringResultCallBack);
    }

    public static void postVideoList(HttpParams httpParams, StringResultCallBack stringResultCallBack) {
        OkHttpUtils.post(APIS.postVideoUrl)
            .params(httpParams)
            .execute(stringResultCallBack);
    }

}
