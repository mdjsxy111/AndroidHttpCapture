package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

/**
 * 悬浮窗控制类，根据android api动态选择FloatPhone和FloatToast的实现方式
 *
 * @author zhangbaoyu
 * @time 2/25/21 11:02 PM
 */
public class IFloatWindowImpl extends IFloatWindow {

    private FloatWindow.B mB;

    private FloatView mFloatView;

    private boolean isShow;

    private boolean once = true;

    private ValueAnimator mAnimator;

    private TimeInterpolator mDecelerateInterpolator;

    private float downX;

    private float downY;

    private float upX;

    private float upY;

    private boolean mClick = false;

    private int mSlop;

    private IFloatWindowImpl() {

    }

    IFloatWindowImpl(FloatWindow.B b) {
        mB = b;
        if (mB.mMoveType == MoveType.fixed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                mFloatView = new FloatPhone(b.mApplicationContext, mB.mPermissionListener);
            } else {
                mFloatView = new FloatToast(b.mApplicationContext);
            }
        } else {
            mFloatView = new FloatPhone(b.mApplicationContext, mB.mPermissionListener);
            initTouchEvent();
        }
        mFloatView.setSize(mB.mWidth, mB.mHeight);
        mFloatView.setGravity(mB.gravity, mB.xOffset, mB.yOffset);
        mFloatView.setView(mB.mView);
    }

    @Override
    public boolean show() {
        boolean result = true;
        if (once) {
            mFloatView.init();
            once = false;
            isShow = true;
        } else {
            if (isShow) {
                return true;
            }
            result = PermissionUtil.hasPermission(mB.mApplicationContext);
            if (result && getView().getParent() == null) {
                if (mFloatView instanceof FloatPhone) {
                    ((FloatPhone) mFloatView).attachToWindow();
                }
            }
            getView().setVisibility(View.VISIBLE);
            isShow = true;
        }
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onShow();
        }
        return result;
    }

    @Override
    public void hide() {
        if (once || !isShow) {
            return;
        }
        getView().setVisibility(View.INVISIBLE);
        isShow = false;
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onHide();
        }
    }

    @Override
    public boolean isShowing() {
        return isShow;
    }

    @Override
    void dismiss() {
        mFloatView.dismiss();
        isShow = false;
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onDismiss();
        }
    }

    @Override
    public void setViewStateListener(ViewStateListener viewStateListener) {
        mB.mViewStateListener = viewStateListener;
    }

    @Override
    public void setPermissionListener(PermissionListener permissionListener) {
        if (mB.mMoveType == MoveType.fixed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                ((FloatPhone) mFloatView).setPermissionListener(permissionListener);
            } else {// FloatToast
            }
        } else {
            ((FloatPhone) mFloatView).setPermissionListener(permissionListener);
        }
    }

    @Override
    public void updateX(int x) {
        checkMoveType();
        mB.xOffset = x;
        mFloatView.updateX(x);
    }

    @Override
    public void updateY(int y) {
        checkMoveType();
        mB.yOffset = y;
        mFloatView.updateY(y);
    }

    @Override
    public void updateX(int screenType, float ratio) {
        checkMoveType();
        mB.xOffset = (int) ((screenType == Screen.width ?
            ScreenUtil.getScreenWidth(mB.mApplicationContext) :
            ScreenUtil.getScreenHeight(mB.mApplicationContext)) * ratio);
        mFloatView.updateX(mB.xOffset);
    }

    @Override
    public void updateY(int screenType, float ratio) {
        checkMoveType();
        mB.yOffset = (int) ((screenType == Screen.width ?
            ScreenUtil.getScreenWidth(mB.mApplicationContext) :
            ScreenUtil.getScreenHeight(mB.mApplicationContext)) * ratio);
        mFloatView.updateY(mB.yOffset);
    }

    @Override
    public int getX() {
        return mFloatView.getX();
    }

    @Override
    public int getY() {
        return mFloatView.getY();
    }

    @Override
    public View getView() {
        mSlop = ViewConfiguration.get(mB.mApplicationContext).getScaledTouchSlop();
        return mB.mView;
    }

    private void checkMoveType() {
        if (mB.mMoveType == MoveType.fixed) {
            throw new IllegalArgumentException("FloatWindow of this tag is not allowed to move!");
        }
    }

    private void initTouchEvent() {
        switch (mB.mMoveType) {
            case MoveType.inactive:
                break;
            default:
                getView().setOnTouchListener(new View.OnTouchListener() {
                    float lastX, lastY, changeX, changeY;

                    int newX, newY;

                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                downX = event.getRawX();
                                downY = event.getRawY();
                                lastX = event.getRawX();
                                lastY = event.getRawY();
                                cancelAnimator();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                changeX = event.getRawX() - lastX;
                                changeY = event.getRawY() - lastY;
                                newX = (int) (mFloatView.getX() + changeX);
                                newY = (int) (mFloatView.getY() + changeY);
                                mFloatView.updateXY(newX, newY);
                                if (mB.mViewStateListener != null) {
                                    mB.mViewStateListener.onPositionUpdate(newX, newY);
                                }
                                lastX = event.getRawX();
                                lastY = event.getRawY();
                                break;
                            case MotionEvent.ACTION_UP:
                                upX = event.getRawX();
                                upY = event.getRawY();
                                mClick = (Math.abs(upX - downX) > mSlop) || (Math.abs(upY - downY) > mSlop);
                                switch (mB.mMoveType) {
                                    case MoveType.slide:
                                        int startX = mFloatView.getX();
                                        int endX = (startX * 2 + v.getWidth() > ScreenUtil.getScreenWidth(mB.mApplicationContext)) ?
                                            ScreenUtil.getScreenWidth(mB.mApplicationContext) - v.getWidth() - mB.mSlideRightMargin :
                                            mB.mSlideLeftMargin;
                                        mAnimator = ObjectAnimator.ofInt(startX, endX);
                                        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                int x = (int) animation.getAnimatedValue();
                                                mFloatView.updateX(x);
                                                if (mB.mViewStateListener != null) {
                                                    mB.mViewStateListener.onPositionUpdate(x, (int) upY);
                                                }
                                            }
                                        });
                                        startAnimator();
                                        break;
                                    case MoveType.back:
                                        PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("x", mFloatView.getX(), mB.xOffset);
                                        PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("y", mFloatView.getY(), mB.yOffset);
                                        mAnimator = ObjectAnimator.ofPropertyValuesHolder(pvhX, pvhY);
                                        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                int x = (int) animation.getAnimatedValue("x");
                                                int y = (int) animation.getAnimatedValue("y");
                                                mFloatView.updateXY(x, y);
                                                if (mB.mViewStateListener != null) {
                                                    mB.mViewStateListener.onPositionUpdate(x, y);
                                                }
                                            }
                                        });
                                        startAnimator();
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                        return mClick;
                    }
                });
        }
    }

    private void startAnimator() {
        if (mB.mInterpolator == null) {
            if (mDecelerateInterpolator == null) {
                mDecelerateInterpolator = new DecelerateInterpolator();
            }
            mB.mInterpolator = mDecelerateInterpolator;
        }
        mAnimator.setInterpolator(mB.mInterpolator);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator.removeAllUpdateListeners();
                mAnimator.removeAllListeners();
                mAnimator = null;
                if (mB.mViewStateListener != null) {
                    mB.mViewStateListener.onMoveAnimEnd();
                }
            }
        });
        mAnimator.setDuration(mB.mDuration).start();
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onMoveAnimStart();
        }
    }

    private void cancelAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

}
