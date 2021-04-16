package com.cdel.accmobile.httpcapture.model;

/**
 * @author zhangbaoyu
 * @time 1/18/21 5:07 PM
 */
public class CaptureInterfaceItemBean {

    /**
     * 请求接口地址
     */
    private String url;

    /**
     * 请求状态
     */
    private String status;

    /**
     * 耗时
     */
    private String time;

    /**
     * 请求时间
     */
    private String date;

    /**
     * 请求host
     */
    private String host;

    /**
     * 响应数据
     */
    private String responseStr;

    /**
     * 请求数据
     */
    private String requestStr;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResponseStr() {
        return responseStr;
    }

    public void setResponseStr(String responseStr) {
        this.responseStr = responseStr;
    }

    public String getRequestStr() {
        return requestStr;
    }

    public void setRequestStr(String requestStr) {
        this.requestStr = requestStr;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
