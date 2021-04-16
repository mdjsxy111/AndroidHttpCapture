package com.cdel.accmobile.httpcapture.manager;

import android.text.TextUtils;

import com.cdel.accmobile.httpcapture.model.CaptureInterfaceItemBean;
import com.cdel.accmobile.httpcapture.util.HttpCaptureSPUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 接口调试数据工具类
 *
 * @author zhangbaoyu
 * @time 1/21/21 4:08 PM
 */
public class HttpCaptureData {

    /**
     * 抓包数据集合
     */
    public List<CaptureInterfaceItemBean> httpCaptureList;

    /**
     * 抓包数据对象
     */
    private CaptureInterfaceItemBean captureInterfaceItemBean;

    /**
     * 筛选的接口
     */
    private String mFilter;

    private HttpCaptureData() {
        init();
    }

    public static HttpCaptureData getSingleton() {
        return Inner.instance;
    }

    private static class Inner {
        public static final HttpCaptureData instance = new HttpCaptureData();
    }

    /**
     * 初始化方法，用于创建list
     *
     * @author zhangbaoyu
     * @time 2/25/21 9:40 PM
     */
    private void init() {
        // 将list加锁，避免多线程操作时数据错乱
        httpCaptureList = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * 创建抓包数据对象
     *
     * @author zhangbaoyu
     * @time 2/25/21 9:41 PM
     */
    public HttpCaptureData create() {
        boolean isOpen = HttpCaptureSPUtil.getIsOpenCapture();
        if (isOpen) {
            // 如果抓包入口开启后，才创建对象
            captureInterfaceItemBean = new CaptureInterfaceItemBean();
        }
        return this;
    }

    /**
     * 设置域名
     *
     * @param host 域名地址
     * @author zhangbaoyu
     * @time 2/25/21 9:43 PM
     */
    public HttpCaptureData setCaptureHost(String host) {
        if (captureInterfaceItemBean != null) {
            captureInterfaceItemBean.setHost(host);
        }
        return this;
    }

    /**
     * 设置请求地址
     *
     * @param url 请求地址
     * @author zhangbaoyu
     * @time 2/25/21 9:43 PM
     */
    public HttpCaptureData setCaptureUrl(String url) {
        if (captureInterfaceItemBean != null) {
            captureInterfaceItemBean.setUrl(url);
            if (!TextUtils.isEmpty(mFilter) && !TextUtils.isEmpty(url)) {
                // 如果该地址已被过滤，就将该对象滞空，不进行添加
                if (url.contains(mFilter)) {
                    mFilter = null;
                    captureInterfaceItemBean = null;
                }
            }
        }
        return this;
    }

    /**
     * 设置过滤地址
     *
     * @param filter 过滤地址
     * @author zhangbaoyu
     * @time 2/25/21 9:46 PM
     */
    public HttpCaptureData setCaptureFilter(String filter) {
        mFilter = filter;
        if (!TextUtils.isEmpty(filter)) {
            if (captureInterfaceItemBean != null) {
                String url = captureInterfaceItemBean.getUrl();
                if (!TextUtils.isEmpty(url)) {
                    // 如果该地址已被过滤，就将该对象滞空，不进行添加
                    if (url.contains(filter)) {
                        mFilter = null;
                        captureInterfaceItemBean = null;
                    }
                }
            }
        }
        return this;
    }

    /**
     * 设置请求数据
     *
     * @param requestStr 请求数据
     * @author zhangbaoyu
     * @time 2/25/21 9:48 PM
     */
    public HttpCaptureData setCaptureRequestStr(String requestStr) {
        if (captureInterfaceItemBean != null) {
            captureInterfaceItemBean.setRequestStr(requestStr);
        }
        return this;
    }

    /**
     * 设置时间
     *
     * @param date 时间
     * @author zhangbaoyu
     * @time 2/25/21 9:48 PM
     */
    public HttpCaptureData setCaptureDate(String date) {
        if (captureInterfaceItemBean != null) {
            captureInterfaceItemBean.setDate(date);
        }
        return this;
    }

    /**
     * 设置时长
     *
     * @param time 时长
     * @author zhangbaoyu
     * @time 2/25/21 9:49 PM
     */
    public HttpCaptureData setCaptureTime(String time) {
        if (captureInterfaceItemBean != null) {
            captureInterfaceItemBean.setTime(time);
        }
        return this;
    }

    /**
     * 设置接口请求状态
     *
     * @param status 接口状态
     * @author zhangbaoyu
     * @time 2/25/21 9:49 PM
     */
    public HttpCaptureData setCaptureStatus(String status) {
        if (captureInterfaceItemBean != null) {
            captureInterfaceItemBean.setStatus(status);
        }
        return this;
    }

    /**
     * 设置响应数据
     *
     * @param responseStr 响应数据
     * @author zhangbaoyu
     * @time 2/25/21 9:50 PM
     */
    public HttpCaptureData setCaptureResponseStr(String responseStr) {
        if (captureInterfaceItemBean != null) {
            captureInterfaceItemBean.setResponseStr(responseStr);
        }
        return this;
    }

    /**
     * 添加接口数据
     *
     * @author zhangbaoyu
     * @time 1/21/21 4:37 PM
     */
    public void add() {
        if (captureInterfaceItemBean != null) {
            List<CaptureInterfaceItemBean> captureList = HttpCaptureData.getSingleton().httpCaptureList;
            captureList.add(0, captureInterfaceItemBean);
            // 最多200条
            if (captureList.size() > 200) {
                captureList.remove(200);
            }
        }
    }

}
