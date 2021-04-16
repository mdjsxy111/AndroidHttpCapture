package com.cdel.accmobile.httpcapture.activity;

import android.os.Bundle;
import android.view.View;

import com.cdel.accmobile.httpcapture.R;
import com.cdel.accmobile.httpcapture.base.BaseActivity;
import com.cdel.accmobile.httpcapture.manager.CaptureConstants;
import com.cdel.accmobile.httpcapture.widget.JsonViewer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 显示json的页面
 *
 * @author zhangbaoyu
 * @time 2/25/21 9:24 PM
 */
public class HttpCaptureJsonActivity extends BaseActivity {

    /**
     * 显示json的控件
     */
    private JsonViewer jsonViewer;

    private String jsonStr;

    @Override
    public int setLayoutId() {
        return R.layout.activity_debug_json;
    }

    @Override
    public void initViews() {
        jsonViewer = findViewById(R.id.jsonView);
    }

    @Override
    public void initToolBar() {
        getTitleView().setText(getString(R.string.debug_json_view));
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
    }

    @Override
    public void initData() {
        if (getIntent() == null) {
            return;
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jsonStr = bundle.getString(CaptureConstants.JSON_STR);
            try {
                // 显示json数据
                JSONObject jsonObject = new JSONObject(jsonStr);
                jsonViewer.setJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}