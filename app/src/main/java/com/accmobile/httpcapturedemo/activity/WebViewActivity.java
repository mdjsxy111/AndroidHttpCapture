package com.accmobile.httpcapturedemo.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.accmobile.httpcapturedemo.R;
import com.accmobile.httpcapturedemo.util.DateUtil;
import com.cdel.accmobile.httpcapture.base.BaseActivity;
import com.cdel.accmobile.httpcapture.manager.HttpCaptureData;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebViewActivity extends BaseActivity {

    private WebView mWebView;

    private String url = "https://www.baidu.com/";

    private long startNs;

    private long tookMs;

    @Override
    public int setLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initViews() {
        mWebView = findViewById(R.id.webview);
    }

    @Override
    public void initToolBar() {
        getTitleView().setText(R.string.h5_agent);
    }

    @Override
    public void setListeners() {
        getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        loadUrl(mWebView, url, new OnLoadFinishListener() {
            @Override
            public void onLoadFinish() {
                String dateStr = DateUtil.getString(new Date(), DateUtil.PATTERN_HMS);
                HttpCaptureData.getSingleton().create()
                    .setCaptureUrl(url)
                    .setCaptureHost("h5-")
                    .setCaptureTime(tookMs + "ms")
                    .setCaptureDate(dateStr)
                    .add();
            }
        });
    }

    private void loadUrl(final WebView webView, String url, final OnLoadFinishListener listener) {
        if (TextUtils.isEmpty(url))
            return;
        // 启用javascript
        webView.getSettings().setJavaScriptEnabled(true);
        //用这个方法判断是否加载完成，不过会触发多次，就是会出现多次newProgress==100的情况
        webView.setWebChromeClient(new WebChromeClient() {
            private AtomicBoolean mIsLoadFinish = new AtomicBoolean(false);

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.e("onProgressChanged", newProgress + "");
                if (newProgress == 10) {
                    startNs = System.nanoTime();
                }
                if (newProgress == 100) {
                    if (!mIsLoadFinish.compareAndSet(false, true)) {
                        return;
                    }
                    if (listener != null) {
                        tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
                        listener.onLoadFinish();
                    }
                }
            }
        });
        webView.loadUrl(url);
    }

    private interface OnLoadFinishListener {
        void onLoadFinish();
    }
}