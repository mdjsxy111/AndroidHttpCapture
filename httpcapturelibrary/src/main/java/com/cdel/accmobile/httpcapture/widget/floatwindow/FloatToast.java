package com.cdel.accmobile.httpcapture.widget.floatwindow;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

 /**
  * 悬浮窗实现类，自定义 toast 方式显示悬浮窗，无需申请权限
  *
  * @author zhangbaoyu
  * @time 2/25/21 10:56 PM
  */
class FloatToast extends FloatView {

   private Toast toast;

   private Object TN;
   private Method show;
   private Method hide;

   private int width;
   private int height;

   FloatToast(Context applicationContext) {
       toast = new Toast(applicationContext);
   }

   @Override
   public void setSize(int width, int height) {
       this.width = width;
       this.height = height;
   }

   @Override
   public void setView(View view) {
       toast.setView(view);
       initTN();
   }

   @Override
   public void setGravity(int gravity, int xOffset, int yOffset) {
       toast.setGravity(gravity, xOffset, yOffset);
   }

   @Override
   public void init() {
       try {
           show.invoke(TN);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   @Override
   public void dismiss() {
       try {
           hide.invoke(TN);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   private void initTN() {
       try {
           Field tnField = toast.getClass().getDeclaredField("mTN");
           tnField.setAccessible(true);
           TN = tnField.get(toast);
           show = TN.getClass().getMethod("show");
           hide = TN.getClass().getMethod("hide");
           Field tnParamsField = TN.getClass().getDeclaredField("mParams");
           tnParamsField.setAccessible(true);
           WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(TN);
           params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                   | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
           params.width = width;
           params.height = height;
           params.windowAnimations = 0;
           Field tnNextViewField = TN.getClass().getDeclaredField("mNextView");
           tnNextViewField.setAccessible(true);
           tnNextViewField.set(TN, toast.getView());

       } catch (Exception e) {
           e.printStackTrace();
       }
   }


}
