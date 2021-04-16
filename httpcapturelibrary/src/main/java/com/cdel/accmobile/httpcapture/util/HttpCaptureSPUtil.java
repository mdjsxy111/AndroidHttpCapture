package com.cdel.accmobile.httpcapture.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cdel.accmobile.httpcapture.manager.CaptureConstants;

/**
 * SP工具类
 *
 * @author zhangbaoyu
 * @time 3/2/21 10:03 AM
 */
public class HttpCaptureSPUtil {

    private static Context mContext;

    private static SharedPreferences sp;

    /**
     * sp初始化
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 3/2/21 10:09 AM
     */
    public static void init(Context context) {
        mContext = context;
        sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "debug_share_date";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key    唯一标识
     * @param object 设置数据
     * @return 返回对象
     * @author zhangbaoyu
     * @time 3/2/21 10:00 AM
     */
    public static void setParam(String key, Object object) {
        if (sp != null) {
            String type = object.getClass().getSimpleName();
            SharedPreferences.Editor editor = sp.edit();
            if ("String".equals(type)) {
                editor.putString(key, (String) object);
            } else if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) object);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) object);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float) object);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long) object);
            }
            editor.apply();
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key           唯一标识
     * @param defaultObject 默认数据
     * @return 返回对象
     * @author zhangbaoyu
     * @time 3/2/21 10:00 AM
     */
    public static Object getParam(String key, Object defaultObject) {
        if (sp != null) {
            String type = defaultObject.getClass().getSimpleName();
            if ("String".equals(type)) {
                return sp.getString(key, (String) defaultObject);
            } else if ("Integer".equals(type)) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if ("Boolean".equals(type)) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if ("Float".equals(type)) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if ("Long".equals(type)) {
                return sp.getLong(key, (Long) defaultObject);
            }
        }
        return null;
    }

    /**
     * 设置调试模式是否开启
     *
     * @param isOpen 是否开启
     * @author zhangbaoyu
     * @time 3/2/21 10:00 AM
     */
    public static void setIsOpenCapture(boolean isOpen) {
        setParam(CaptureConstants.CAPTURE_IS_OPEN_KEY, isOpen);
    }

    /**
     * 获取调试模式开关
     *
     * @author zhangbaoyu
     * @time 3/2/21 10:01 AM
     */
    public static boolean getIsOpenCapture() {
        boolean isOpen;
        Object object = getParam(CaptureConstants.CAPTURE_IS_OPEN_KEY, false);
        if (object == null) {
            isOpen = false;
        } else {
            isOpen = (boolean) object;
        }
        return isOpen;
    }

}