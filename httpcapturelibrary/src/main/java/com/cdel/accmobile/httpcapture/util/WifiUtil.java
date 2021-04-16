package com.cdel.accmobile.httpcapture.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cdel.accmobile.httpcapture.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Wi-Fi工具类
 *
 * @author zhangbaoyu
 * @time 2/28/21 4:21 PM
 */
public class WifiUtil {
    private static String TAG = WifiUtil.class.getSimpleName();

    public static int REQUEST_CODE = 1001;

    /**
     * wifi网络
     */
    private final static int WIFI_NET = 0;

    /**
     * 移动网络
     */
    private final static int MOBILE_NET = 1;

    /**
     * 没有连网
     */
    private final static int NOT_NET = -1;

    /**
     * 判断当前网络是否连接
     *
     * @param context 上下文
     * @return int 0:wifi ，1:移动网络， -1:没有连网
     * @author zhangbaoyu
     * @time 2/28/21 5:12 PM
     */
    public static int networkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (context != null) {
            if (Build.VERSION.SDK_INT < 23) {
                NetworkInfo mWiFiNetworkInfo = cm.getActiveNetworkInfo();
                if (mWiFiNetworkInfo != null) {
                    if (mWiFiNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        Log.e(TAG, "WIFI_NET");
                        return WIFI_NET;
                    } else if (mWiFiNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Log.e(TAG, "MOBILE_NET");
                        return MOBILE_NET;
                    }
                }
            } else {
                Network network = cm.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities nc = cm.getNetworkCapabilities(network);
                    if (nc != null) {
                        if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            Log.e(TAG, "WIFI_NET");
                            return WIFI_NET;
                        } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            Log.e(TAG, "MOBILE_NET");
                            return MOBILE_NET;
                        }
                    }
                }
            }
        }
        Log.e(TAG, "-NOT_NET");
        return NOT_NET;
    }

    /**
     * 获取当前手机所连接的wifi信息
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/28/21 4:21 PM
     */
    public static String getCurrentWifiName(Context context) {
        // 0:wifi ，1:移动网络， -1:没有连网
        int netType = WifiUtil.networkConnected(context);
        Log.e(TAG, "-" + netType);
        String wifiName;
        switch (netType) {
            case NOT_NET:
                wifiName = context.getString(R.string.debug_not_net);
                break;
            case MOBILE_NET:
                wifiName = context.getString(R.string.debug_mobile_net);
                break;
            case WIFI_NET:
                WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();
                if (TextUtils.isEmpty(ssid) || ssid.contains("unknown")) {
                    wifiName = context.getString(R.string.debug_net_initial);
                    break;
                }
                wifiName = whetherToRemoveTheDoubleQuotationMarks(ssid);
                break;
            default:
                wifiName = context.getString(R.string.debug_net_initial);
                break;
        }
        Log.e(TAG, "-" + wifiName);
        return wifiName;
    }

    /**
     * 根据Android的版本判断获取到的SSID是否有双引号
     *
     * @param ssid Wi-Fi名称
     * @author zhangbaoyu
     * @time 2/28/21 4:22 PM
     */
    private static String whetherToRemoveTheDoubleQuotationMarks(String ssid) {
        //获取Android版本号
        int deviceVersion = Build.VERSION.SDK_INT;
        if (deviceVersion >= 17) {
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
        }
        return ssid;
    }

    /**
     * 申请权限 android 10.0使用wifi api新添加的权限
     *
     * @param context 上下文
     * @author zhangbaoyu
     * @time 2/28/21 4:30 PM
     */
    public static boolean checkPermission(Context context) {
        List<String> permissionsList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 如果Android10及以上系统，需要以下权限
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE);
            return false;
        }
        return true;
    }

}
