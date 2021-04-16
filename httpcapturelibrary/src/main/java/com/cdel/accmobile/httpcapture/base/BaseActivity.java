package com.cdel.accmobile.httpcapture.base;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cdel.accmobile.httpcapture.R;

/**
 * BaseActivity基类
 *
 * @author zhangbaoyu
 * @time 2/25/21 9:34 PM
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseInterface {

    private LinearLayout parentLinearLayout;

    private TextView mBarTitle, mBarRightTv;

    private Button mBarLeftBtn;

    public Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置toolbar布局
        initTitleView(R.layout.toolbar_layout);
        // 设置内容布局
        setContentView(setLayoutId());
        // 隐藏actionbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
        // 初始化布局控件
        initViews();
        // 初始化toolBar
        initToolBar();
        // 设置布局监听
        setListeners();
        // 初始化数据
        initData();
    }

    /**
     * 设置toolbar
     *
     * @param titleLayoutResID toolbar布局id
     * @author zhangbaoyu
     * @time 2/7/21 2:53 PM
     */
    private void initTitleView(@LayoutRes int titleLayoutResID) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parentLinearLayout);
        LayoutInflater.from(this).inflate(titleLayoutResID, parentLinearLayout, true);
        mBarTitle = findViewById(R.id.bar_title);
        mBarLeftBtn = findViewById(R.id.bar_left_btn);
        mBarRightTv = findViewById(R.id.bar_right_tv);
    }

    /**
     * 设置内容布局
     *
     * @param layoutResID 内容布局id
     * @author zhangbaoyu
     * @time 2/7/21 3:07 PM
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);
    }

    /**
     * 获取bar title view
     *
     * @author zhangbaoyu
     * @time 2/7/21 3:52 PM
     */
    public TextView getTitleView() {
        return mBarTitle;
    }

    /**
     * 获取bar left view
     *
     * @author zhangbaoyu
     * @time 2/7/21 3:52 PM
     */
    public Button getLeftView() {
        return mBarLeftBtn;
    }

    /**
     * 获取bar right view
     *
     * @author zhangbaoyu
     * @time 2/7/21 3:52 PM
     */
    public TextView getRightView() {
        return mBarRightTv;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 跳转到指定的Activity
     *
     * @param targetActivity 要跳转的目标Activity
     * @author zhangbaoyu
     * @time 2/7/21 10:00 AM
     */
    protected final void startActivity(@NonNull Class<?> targetActivity) {
        startActivity(new Intent(this, targetActivity));
    }

    /**
     * 跳转到指定的Activity
     *
     * @param bundle         Activity之间传递数据，Intent的Extra key为Constant.EXTRA_NAME.DATA
     * @param targetActivity 要跳转的目标Activity
     * @author zhangbaoyu
     * @time 2/7/21 10:00 AM
     */
    protected final void startActivity(@NonNull Bundle bundle, @NonNull Class<?> targetActivity) {
        final Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(this, targetActivity);
        startActivity(intent);
    }

    /**
     * 显示长时间Toast
     *
     * @param text 文本
     * @author zhangbaoyu
     * @time 2/7/21 9:59 AM
     */
    public void showLongToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示短时间Toast
     *
     * @param text 文本
     * @author zhangbaoyu
     * @time 2/7/21 9:59 AM
     */
    public void showShortToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 是否为debug模式
     *
     * @author zhangbaoyu
     * @time 2/7/21 11:58 AM
     */
    public boolean isDebug() {
        return getApplicationContext().getApplicationInfo() != null &&
            (getApplicationContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}