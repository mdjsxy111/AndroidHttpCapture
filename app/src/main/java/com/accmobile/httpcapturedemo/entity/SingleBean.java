package com.accmobile.httpcapturedemo.entity;

/**
 * @author zhangbaoyu
 * @time 2/23/21 7:30 PM
 */
public class SingleBean {

    /**
     * code : 200
     * message : 成功!
     * result : 因有人恶意刷接口，导致接口调用频繁，接口已经不能稳定运行，所以计划近期下线，积德吧朋友，如果长期如此，所有接口将面临关闭。
     */

    private int code;

    private String message;

    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
