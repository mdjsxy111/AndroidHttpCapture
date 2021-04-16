package com.cdel.accmobile.httpcapture.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cdel.accmobile.httpcapture.R;
import com.cdel.accmobile.httpcapture.base.BaseActivity;
import com.cdel.accmobile.httpcapture.manager.HttpCaptureData;
import com.cdel.accmobile.httpcapture.util.HttpCaptureSPUtil;
import com.cdel.accmobile.httpcapture.util.WifiUtil;
import com.cdel.accmobile.httpcapture.widget.floatwindow.FloatWindowManager;
import com.cdel.accmobile.httpcapture.widget.floatwindow.HttpCaptureOverlayWindow;
import com.cdel.accmobile.httpcapture.widget.floatwindow.PermissionUtil;

/**
 * 抓包首页
 *
 * @author zhangbaoyu
 * @time 2/25/21 9:24 PM
 */
public class HttpCaptureHomeActivity extends BaseActivity {

    private TextView mTvDebugClean, mTvDebugNet, mTvDebugRequests;

    private CheckBox mCbCapture;

    /**
     * 悬浮小球
     */
    private HttpCaptureOverlayWindow httpCaptureOverlayWindow;

    private boolean isOpenPermisstion;

    @Override
    public int setLayoutId() {
        return R.layout.activity_debug_home;
    }

    @Override
    public void initViews() {
        mTvDebugNet = findViewById(R.id.tv_debug_net);
        mTvDebugRequests = findViewById(R.id.tv_debug_requests);
        mTvDebugClean = findViewById(R.id.tv_debug_clean);
        mCbCapture = findViewById(R.id.cb_debug_enter);
        isOpenPermisstion = WifiUtil.checkPermission(mContext);
        if (isOpenPermisstion) {
            // 创建悬浮小球
            httpCaptureOverlayWindow = HttpCaptureOverlayWindow.getInstance(mContext);
        }
    }

    @Override
    public void initToolBar() {
        getTitleView().setText(getString(R.string.debug_agent_package));
    }

    @Override
    public void setListeners() {
        getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvDebugRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HttpCaptureListActivity.class);
            }
        });
        mTvDebugClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpCaptureData.getSingleton().httpCaptureList.clear();
                showShortToast(getString(R.string.debug_clean_success));
            }
        });
        mCbCapture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 保存是否打开调试模式状态
                HttpCaptureSPUtil.setIsOpenCapture(isChecked);
                if (isChecked && !PermissionUtil.hasPermission(mContext)) {
                    buttonView.setChecked(false);
                    // 申请悬浮权限
                    if (!FloatWindowManager.getInstance().applyOrShowFloatWindow(mContext)) {
                        return;
                    }
                }
                // 控制小球显隐
                configOverlayView(isChecked);
            }
        });
    }

    @Override
    public void initData() {

    }

    /**
     * 是否显示调试悬浮窗
     *
     * @param isChecked true:显示，false:隐藏
     * @author zhangbaoyu
     * @time 2/1/21 9:32 AM
     */
    private void configOverlayView(boolean isChecked) {
        if (httpCaptureOverlayWindow != null) {
            if (isChecked) {
                httpCaptureOverlayWindow.show();
            } else {
                httpCaptureOverlayWindow.hide();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtil.hasPermission(mContext)) {
            // 获取debug状态是否开启
            boolean isOpenCapture = HttpCaptureSPUtil.getIsOpenCapture();
            // 设置开关状态
            mCbCapture.setChecked(isOpenCapture);
            // 如果有权限可进行显示悬浮入口
            configOverlayView(isOpenCapture);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WifiUtil.REQUEST_CODE) {
            // 创建悬浮小球
            if (httpCaptureOverlayWindow == null) {
                httpCaptureOverlayWindow = HttpCaptureOverlayWindow.getInstance(mContext);
            }
            // 显示Wi-Fi名称
            httpCaptureOverlayWindow.labelView.setText(WifiUtil.getCurrentWifiName(mContext));
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, HttpCaptureHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}