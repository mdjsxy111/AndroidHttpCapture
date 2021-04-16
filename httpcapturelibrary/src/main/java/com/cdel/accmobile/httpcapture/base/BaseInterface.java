package com.cdel.accmobile.httpcapture.base;

/**
 * @author zhangbaoyu
 * @time 2/7/21 9:47 AM
 */
public interface BaseInterface {

    /**
     * 设置布局文件
     *
     * @author zhangbaoyu
     * @time 2/7/21 9:48 AM
     */
    int setLayoutId();

    /**
     * 初始化View控件
     *
     * @param view 根布局View
     * @author zhangbaoyu
     * @time 2/7/21 9:48 AM
     */
    void initViews();

    /**
     * 初始化toolBar
     *
     * @author zhangbaoyu
     * @time 2/7/21 3:02 PM
     */
    void initToolBar();

    /**
     * 设置监听
     *
     * @author zhangbaoyu
     * @time 2/7/21 9:50 AM
     */
    void setListeners();

    /**
     * 初始化数据
     *
     * @author zhangbaoyu
     * @time 2/7/21 9:50 AM
     */
    void initData();
}
