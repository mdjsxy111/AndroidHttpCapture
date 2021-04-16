package com.accmobile.httpcapturedemo.activity;

import android.view.View;
import android.widget.TextView;

import com.accmobile.httpcapturedemo.R;
import com.accmobile.httpcapturedemo.entity.SingleBean;
import com.accmobile.httpcapturedemo.http.AccOkHttpUtil;
import com.accmobile.httpcapturedemo.http.StringResultCallBack;
import com.cdel.accmobile.httpcapture.base.BaseActivity;
import com.cdel.accmobile.httpcapture.util.HttpCaptureGsonUtil;

public class GetSingleActivity extends BaseActivity {

    private TextView mTvSingleStr;

    @Override
    public int setLayoutId() {
        return R.layout.activity_get_single;
    }

    @Override
    public void initViews() {
        mTvSingleStr = findViewById(R.id.tv_single);
    }

    @Override
    public void initToolBar() {
        getTitleView().setText(R.string.get_agent);
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
        AccOkHttpUtil.getPoetryOne(new StringResultCallBack() {
            @Override
            public void onStringResult(String sResult) {
                SingleBean singleBean = HttpCaptureGsonUtil.jsonToObject(sResult, SingleBean.class);
                if (singleBean != null && singleBean.getCode() == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvSingleStr.setText(singleBean.getResult());
                        }
                    });
                }
            }

            @Override
            public void onStringError() {

            }
        });

    }
}