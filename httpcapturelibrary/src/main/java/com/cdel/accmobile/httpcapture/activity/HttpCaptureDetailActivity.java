package com.cdel.accmobile.httpcapture.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cdel.accmobile.httpcapture.R;
import com.cdel.accmobile.httpcapture.base.BaseActivity;
import com.cdel.accmobile.httpcapture.manager.CaptureConstants;
import com.cdel.accmobile.httpcapture.util.JsonUtil;
import com.cdel.accmobile.httpcapture.widget.SelectableTextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 接口调试详情页
 *
 * @author zhangbaoyu
 * @time 1/21/21 4:40 PM
 */
public class HttpCaptureDetailActivity extends BaseActivity {

    private SelectableTextView mTvUrl, mTvRequestStr, mTvResponseStr;

    private String requestStr, responseStr;

    @Override
    public int setLayoutId() {
        return R.layout.activity_debug_detail;
    }

    @Override
    public void initViews() {
        mTvUrl = findViewById(R.id.tv_url);
        mTvRequestStr = findViewById(R.id.requestJson);
        mTvResponseStr = findViewById(R.id.responseJson);
    }

    @Override
    public void initToolBar() {
        getTitleView().setText(getString(R.string.debug_detail_title));
        getRightView().setVisibility(View.GONE);
    }

    @Override
    public void setListeners() {
        getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvRequestStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(CaptureConstants.JSON_STR, requestStr);
                startActivity(bundle, HttpCaptureJsonActivity.class);
            }
        });
        mTvResponseStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(CaptureConstants.JSON_STR, responseStr);
                startActivity(bundle, HttpCaptureJsonActivity.class);
            }
        });
    }

    @Override
    public void initData() {
        Bundle extras = getIntent().getExtras();
        String urlStr = extras.getString(CaptureConstants.URL_KEY);
        requestStr = extras.getString(CaptureConstants.REQUEST_KEY);
        responseStr = extras.getString(CaptureConstants.RESPONSE_KEY);
        mTvUrl.setText(urlStr);
        showJsonView(mTvRequestStr, requestStr);
        showJsonView(mTvResponseStr, responseStr);
    }

    /**
     * 显示json
     *
     * @param tvJson 用于显示非json格式
     * @param json   数据
     * @author zhangbaoyu
     * @time 2/2/21 6:42 PM
     */
    private void showJsonView(TextView tvJson, String json) {
        JSONObject jsonObject = null;
        try {
            // 判断是否为json数据
            if (JsonUtil.isJson(json)) {
                // 如果是json，就以json格式显示
                jsonObject = new JSONObject(json);
                tvJson.setText(jsonObject.toString(1));
            } else {
                tvJson.setText(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}