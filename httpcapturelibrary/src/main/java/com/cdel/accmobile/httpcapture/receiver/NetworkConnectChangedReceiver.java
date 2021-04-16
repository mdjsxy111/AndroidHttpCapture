package com.cdel.accmobile.httpcapture.receiver;

/**
 * @author zhangbaoyu
 * @time 2/28/21 6:17 PM
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cdel.accmobile.httpcapture.R;
import com.cdel.accmobile.httpcapture.util.WifiUtil;
import com.cdel.accmobile.httpcapture.widget.floatwindow.HttpCaptureOverlayWindow;

/**
 * Wi-Fi改变的监听
 *
 * @author zhangbaoyu
 * @time 2/28/21 4:54 PM
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null && info.isConnected()) {
                // 设置当前网络环境
                HttpCaptureOverlayWindow.getInstance(context).labelView.setText(WifiUtil.getCurrentWifiName(context));
            } else {
                // 设置没有网络
                HttpCaptureOverlayWindow.getInstance(context).labelView.setText(context.getString(R.string.debug_not_net));
            }
        }
    }
}