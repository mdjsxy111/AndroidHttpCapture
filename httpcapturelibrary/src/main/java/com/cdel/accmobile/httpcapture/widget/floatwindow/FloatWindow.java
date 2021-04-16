package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.animation.TimeInterpolator;
import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * 悬浮窗管理类，用来控制多个悬浮窗的状态
 *
 * @author zhangbaoyu
 * @time 2/25/21 10:56 PM
 */
public class FloatWindow {

    private FloatWindow() {

    }

    private static final String DEFAULT_TAG = "default_float_window_tag";

    /**
     * 用来存放多个IFloatWindow的键值映射
     */
    private static Map<String, IFloatWindow> mFloatWindowMap;

    /**
     * IFloatWindow
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:57 PM
     */
    public static IFloatWindow get() {
        return get(DEFAULT_TAG);
    }

    /**
     * IFloatWindow
     *
     * @param tag 标签
     * @author zhangbaoyu
     * @time 2/25/21 10:57 PM
     */
    public static IFloatWindow get(@NonNull String tag) {
        return mFloatWindowMap == null ? null : mFloatWindowMap.get(tag);
    }

    private static B mBuilder = null;

    @MainThread
    public static B with(@NonNull Context applicationContext) {
        return mBuilder = new B(applicationContext);
    }

    /**
     * 销毁默认标签的IFloatWindow实例
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:57 PM
     */
    public static void destroy() {
        destroy(DEFAULT_TAG);
    }

    /**
     * 销毁指定标签的IFloatWindow实例
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:57 PM
     */
    public static void destroy(String tag) {
        if (mFloatWindowMap == null || !mFloatWindowMap.containsKey(tag)) {
            return;
        }
        mFloatWindowMap.get(tag).dismiss();
        mFloatWindowMap.remove(tag);
    }

    /**
     * FloatWindow内建类，通过Builder模式简化创建FloatWindow的流程
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:58 PM
     */
    public static class B {
        Context mApplicationContext;

        View mView;

        private int mLayoutId;

        int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;

        int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        int gravity = Gravity.TOP | Gravity.START;

        int xOffset;

        int yOffset;

        boolean mShow = true;

        Class[] mActivities;

        int mMoveType = MoveType.slide;

        int mSlideLeftMargin;

        int mSlideRightMargin;

        long mDuration = 300;

        TimeInterpolator mInterpolator;

        private String mTag = DEFAULT_TAG;

        PermissionListener mPermissionListener;

        ViewStateListener mViewStateListener;

        private B() {

        }

        B(Context applicationContext) {
            mApplicationContext = applicationContext;
        }

        public B setView(@NonNull View view) {
            mView = view;
            return this;
        }

        public B setView(@LayoutRes int layoutId) {
            mLayoutId = layoutId;
            return this;
        }

        public B setWidth(int width) {
            mWidth = width;
            return this;
        }

        public B setHeight(int height) {
            mHeight = height;
            return this;
        }

        public B setWidth(@Screen.screenType int screenType, float ratio) {
            mWidth = (int) ((screenType == Screen.width ?
                ScreenUtil.getScreenWidth(mApplicationContext) :
                ScreenUtil.getScreenHeight(mApplicationContext)) * ratio);
            return this;
        }


        public B setHeight(@Screen.screenType int screenType, float ratio) {
            mHeight = (int) ((screenType == Screen.width ?
                ScreenUtil.getScreenWidth(mApplicationContext) :
                ScreenUtil.getScreenHeight(mApplicationContext)) * ratio);
            return this;
        }


        public B setX(int x) {
            xOffset = x;
            return this;
        }

        public B setY(int y) {
            yOffset = y;
            return this;
        }

        public B setX(@Screen.screenType int screenType, float ratio) {
            xOffset = (int) ((screenType == Screen.width ?
                ScreenUtil.getScreenWidth(mApplicationContext) :
                ScreenUtil.getScreenHeight(mApplicationContext)) * ratio);
            return this;
        }

        public B setY(@Screen.screenType int screenType, float ratio) {
            yOffset = (int) ((screenType == Screen.width ?
                ScreenUtil.getScreenWidth(mApplicationContext) :
                ScreenUtil.getScreenHeight(mApplicationContext)) * ratio);
            return this;
        }

        /**
         * 设置 Activity 过滤器，用于指定在哪些界面显示悬浮窗，默认全部界面都显示
         *
         * @param show       　过滤类型,子类类型也会生效
         * @param activities 　过滤界面
         * @author zhangbaoyu
         * @time 2/25/21 10:58 PM
         */
        public B setFilter(boolean show, @NonNull Class... activities) {
            mShow = show;
            mActivities = activities;
            return this;
        }

        /**
         * 设置带边距的贴边动画，只有 moveType 为 MoveType.slide，设置边距才有意义，这个方法不标准，后面调整
         *
         * @param moveType 贴边动画 MoveType.slide
         * @author zhangbaoyu
         * @time 2/25/21 10:58 PM
         */
        public B setMoveType(@MoveType.MOVE_TYPE int moveType) {
            return setMoveType(moveType, 0, 0);
        }

        /**
         * 设置带边距的贴边动画，只有 moveType 为 MoveType.slide，设置边距才有意义，这个方法不标准，后面调整
         *
         * @param moveType         贴边动画 MoveType.slide
         * @param slideLeftMargin  贴边动画左边距，默认为 0
         * @param slideRightMargin 贴边动画右边距，默认为 0
         * @author zhangbaoyu
         * @time 2/25/21 10:58 PM
         */
        public B setMoveType(@MoveType.MOVE_TYPE int moveType, int slideLeftMargin, int slideRightMargin) {
            mMoveType = moveType;
            mSlideLeftMargin = slideLeftMargin;
            mSlideRightMargin = slideRightMargin;
            return this;
        }

        /**
         * 移动风格
         *
         * @param duration     时长
         * @param interpolator 内插器
         * @author zhangbaoyu
         * @time 2/25/21 10:59 PM
         */
        public B setMoveStyle(long duration, @Nullable TimeInterpolator interpolator) {
            mDuration = duration;
            mInterpolator = interpolator;
            return this;
        }

        /**
         * 设置标签
         *
         * @param tag 标签
         * @author zhangbaoyu
         * @time 2/25/21 11:00 PM
         */
        public B setTag(@NonNull String tag) {
            mTag = tag;
            return this;
        }

        /**
         * 设置权限监听
         *
         * @param listener 权限监听
         * @author zhangbaoyu
         * @time 2/25/21 11:01 PM
         */
        public B setPermissionListener(PermissionListener listener) {
            mPermissionListener = listener;
            return this;
        }

        /**
         * 设置状态监听
         *
         * @param listener 状态监听
         * @author zhangbaoyu
         * @time 2/25/21 11:01 PM
         */
        public B setViewStateListener(ViewStateListener listener) {
            mViewStateListener = listener;
            return this;
        }

        /**
         * 构建
         *
         * @author zhangbaoyu
         * @time 2/25/21 11:01 PM
         */
        public void build() {
            if (mFloatWindowMap == null) {
                mFloatWindowMap = new HashMap<>();
            }
            if (mFloatWindowMap.containsKey(mTag)) {
                return;
            }
            if (mView == null && mLayoutId == 0) {
                throw new IllegalArgumentException("View has not been set!");
            }
            if (mView == null) {
                LayoutInflater inflate = (LayoutInflater) mApplicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mView = inflate.inflate(mLayoutId, null);
            }
            IFloatWindow floatWindowImpl = new IFloatWindowImpl(this);
            mFloatWindowMap.put(mTag, floatWindowImpl);
        }

    }
}
