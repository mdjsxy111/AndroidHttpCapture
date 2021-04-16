package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdel.accmobile.httpcapture.R;
import com.cdel.accmobile.httpcapture.activity.HttpCaptureHomeActivity;
import com.cdel.accmobile.httpcapture.receiver.NetworkConnectChangedReceiver;
import com.cdel.accmobile.httpcapture.util.HttpCaptureSPUtil;
import com.cdel.accmobile.httpcapture.util.WifiUtil;

/**
 * 抓包悬浮球
 *
 * @author zhangbaoyu
 * @time 2/1/21 8:55 AM
 */
public class HttpCaptureOverlayWindow {

    private static HttpCaptureOverlayWindow instance;

    private NetworkConnectChangedReceiver networkConnectChangedReceiver;

    public static HttpCaptureOverlayWindow getInstance(Context context) {
        if (instance == null) {
            instance = new HttpCaptureOverlayWindow(context);
        }
        return instance;
    }

    private Context context;

    private View view;

    public TextView labelView;

    /**
     * 权限监听
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:41 PM
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {
            // 悬浮窗权限授权成功
        }

        @Override
        public void onFail() {
            // 悬浮窗权限授权失败
        }
    };

    /**
     * 显示状态监听
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:41 PM
     */
    private ViewStateListener viewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {

        }

        @Override
        public void onShow() {
        }

        @Override
        public void onHide() {
        }

        @Override
        public void onDismiss() {
        }

        @Override
        public void onMoveAnimStart() {

        }

        @Override
        public void onMoveAnimEnd() {

        }

    };

    private HttpCaptureOverlayWindow(Context c) {
        this.context = c.getApplicationContext();
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.debug_float_window, null, false);
        labelView = view.findViewById(R.id.tv_debug_enter);
        ImageView mIvClose = view.findViewById(R.id.iv_debug_close);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpCaptureHomeActivity.start(context);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpCaptureSPUtil.setIsOpenCapture(false);
                hide();
            }
        });
        labelView.setText(WifiUtil.getCurrentWifiName(context));
        int width = context.getResources().getDimensionPixelSize(R.dimen.dp_52);
        FloatWindow
            .with(context)
            .setView(view)
            .setWidth(width)
            .setHeight(width)
            .setX(screenWidth - width)
            .setY(screenWidth, 0.3f)
            .setMoveType(MoveType.slide, 0, 0)
            .setViewStateListener(viewStateListener)
            .setPermissionListener(permissionListener)
            .build();
        // 注册网络改变的广播
        registerNetworkConnect();
    }

    /**
     * 显示悬浮窗
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:42 PM
     */
    public boolean show() {
        if (FloatWindow.get().isShowing()) {
            return true;
        }
        return FloatWindow.get().show();
    }

    /**
     * 悬浮窗是否打开
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:42 PM
     */
    public boolean isShowing() {
        return FloatWindow.get().isShowing();
    }

    /**
     * 关闭悬浮窗
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:42 PM
     */
    public void hide() {
        FloatWindow.get().hide();
    }

    /**
     * 销毁悬浮窗
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:42 PM
     */
    public void destroy() {
        context.unregisterReceiver(networkConnectChangedReceiver);
        FloatWindow.destroy();
        instance = null;
    }

    /**
     * 注册网络改变的广播
     *
     * @author zhangbaoyu
     * @time 2/28/21 5:53 PM
     */
    private void registerNetworkConnect() {
        IntentFilter filter = new IntentFilter();
        //监听wifi连接（手机与路由器之间的连接）
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        //监听互联网连通性（也就是是否已经可以上网了），当然只是指wifi网络的范畴
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //这个是监听网络状态的，包括了wifi和移动网络。
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        context.registerReceiver(networkConnectChangedReceiver, filter);
    }
}
