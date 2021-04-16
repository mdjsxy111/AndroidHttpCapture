package com.accmobile.httpcapturedemo.activity;

import android.view.View;
import android.widget.Button;

import com.accmobile.httpcapturedemo.R;
import com.cdel.accmobile.httpcapture.activity.HttpCaptureHomeActivity;
import com.cdel.accmobile.httpcapture.base.BaseActivity;
import com.cdel.accmobile.httpcapture.util.HttpCaptureSPUtil;

public class MainActivity extends BaseActivity {

    private Button mBtDebugEnter, mBtDebugGet, mBtDebugPost, mBtDebugH5;

    private boolean isOpen;

    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        getLeftView().setVisibility(View.GONE);
        mBtDebugEnter = findViewById(R.id.bt_debug_enter);
        mBtDebugGet = findViewById(R.id.bt_debug_get);
        mBtDebugPost = findViewById(R.id.bt_debug_post);
        mBtDebugH5 = findViewById(R.id.bt_debug_h5);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        isOpen = HttpCaptureSPUtil.getIsOpenCapture();
    }

    @Override
    public void setListeners() {
        mBtDebugEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpCaptureHomeActivity.start(MainActivity.this);
            }
        });
        mBtDebugGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    startActivity(GetSingleActivity.class);
                } else {
                    showShortToast("请先开启抓包入口");
                }
            }
        });
        mBtDebugPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    startActivity(PostVideoListActivity.class);
                } else {
                    showShortToast("请先开启抓包入口");
                }
            }
        });
        mBtDebugH5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    startActivity(WebViewActivity.class);
                } else {
                    showShortToast("请先开启抓包入口");
                }
            }
        });
    }

    @Override
    public void initData() {
    }

}